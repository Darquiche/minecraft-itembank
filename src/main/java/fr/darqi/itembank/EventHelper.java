package fr.darqi.itembank;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EventHelper implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();

        Material m = event.getMaterial();
        ItemStack i = event.getItem();

        Material curr = Material.matchMaterial(ItemBank.ibCurrency);

        if (m == curr) {
            if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                if (!ItemBank.ibOnClick){
                    //no action if config says NO!
                    return;
                }
                if (p.hasPermission("itembank.use.click")) {
                    //value of his 'hand'
                    int n = i.getAmount();
                    int value = ItemBank.ibValue;
                    int money = value * n;

                    //deposit item in his vault.
                    EconomyResponse r = ItemBank.econ.depositPlayer(p, money);

                    if(r.transactionSuccess()) {
                        i.setAmount(0);
                        p.sendMessage(String.format(ChatColor.GREEN + "You have just deposited: " + ChatColor.RESET + "%s\n" + ChatColor.GREEN + "Your account: " + ChatColor.RESET + "%s", ItemBank.econ.format(r.amount), ItemBank.econ.format(r.balance)));
                    } else {
                        p.sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
                    }

                } else {
                    p.sendMessage(String.format(ChatColor.RED + "You need permission: itembank.use.click"));
                }
            }
        }
    }

    public void deposit(Material mat, Player p, Material curr, Sign sign) {
        if (mat == curr && ItemBank.ibOnClick) {
            //if in hand material = currency and OnClick feature is activated: no action.
            return;
        }
        ItemStack[] inv = p.getInventory().getContents(); //get player inv content
        int m = 0;
        for (ItemStack anInv : inv) {
            if (anInv != null) {
                if (anInv.getType() == curr) { //for each item in player inv, if the item = currency, increment total value
                    m = m + anInv.getAmount();

                }
            }
        }
        if (m == 0) {
            //if no item, no action
            return;
        }
        p.getInventory().remove(curr); //remove the items
        EconomyResponse r = ItemBank.econ.depositPlayer(p, m); //and deposit the value in player's vault
        String bank = sign.getLine(2); //get optional bank's name
        if (!r.transactionSuccess()) {
            p.sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
            return;
        }
        if (bank != null && !bank.equals("")) {
            p.sendMessage(String.format(ChatColor.AQUA + "You have just deposited all your %s in " + bank + ChatColor.AQUA + ".\nDeposited: " + ChatColor.RESET + "%s\n" + ChatColor.AQUA + "Your account: " + ChatColor.RESET + "%s", ItemBank.ibCurrency, ItemBank.econ.format(r.amount), ItemBank.econ.format(r.balance)));
        } else {
            p.sendMessage(String.format(ChatColor.AQUA + "You have just deposited all your %s.\nDeposited: " + ChatColor.RESET + "%s\n" + ChatColor.AQUA + "Your account: " + ChatColor.RESET + "%s", ItemBank.ibCurrency, ItemBank.econ.format(r.amount), ItemBank.econ.format(r.balance)));
        }
    }

    private boolean isWallSignPost(Material block) {
        if (block == Material.SPRUCE_WALL_SIGN) {
            return true;
        }
        if (block == Material.ACACIA_WALL_SIGN) {
            return true;
        }
        if (block == Material.BIRCH_WALL_SIGN) {
            return true;
        }
        if (block == Material.DARK_OAK_WALL_SIGN) {
            return true;
        }
        if (block == Material.JUNGLE_WALL_SIGN) {
            return true;
        }
        if (block == Material.OAK_WALL_SIGN) {
            return true;
        }
        if (block == Material.LEGACY_WALL_SIGN) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Action a = event.getAction(); //get action
        if (a != Action.RIGHT_CLICK_BLOCK) {
            //no action if it's not a right click on a block.
            return;
        }
        if (!ItemBank.ibSign) {
            //no action if config says NO!
            return;
        }
        Material mat = event.getMaterial(); //get player in hand material
        Player p = event.getPlayer(); //get player
        Material curr = Material.matchMaterial(ItemBank.ibCurrency); //convert string to material
        Block b = event.getClickedBlock(); //get clicked block
        if (isWallSignPost(b.getType())) {
            Sign sign = (Sign) b.getState(); //get sign object
            if (ChatColor.stripColor(sign.getLine(0)).equals("[DEPOSIT]")) {
                //deposit sign
                deposit(mat, p, curr, sign);
            } else if (ChatColor.stripColor(sign.getLine(0)).equals("[WITHDRAW]")) {
                //withdraw sign
                if (p.hasPermission("itembank.use.sign.withdraw")) {
                    //TODO: fix gui
                    ItemBank.menu.show(p);
                }
            } else if (ChatColor.stripColor(sign.getLine(0)).equals("[ATM]")) {
                //atm sign
                if (p.hasPermission("itembank.use.sign.atm")) {
                    if (p.isSneaking()){
                        //change atm state
                        if (ChatColor.stripColor(sign.getLine(1)).equals("[DEPOSIT]")) {
                            sign.setLine(1, ChatColor.BLUE + "[WITHDRAW]");
                        } else {
                            sign.setLine(1, ChatColor.BLUE + "[DEPOSIT]");
                        }
                        sign.update();
                    } else {
                        //use atm
                        if (ChatColor.stripColor(sign.getLine(1)).equals("[DEPOSIT]")) {
                            if (p.hasPermission("itembank.use.sign.deposit")) {
                                deposit(mat, p, curr, sign);
                            } else {
                                p.sendMessage(ChatColor.RED + "You need permission: itembank.use.sign.deposit");
                            }
                        } else if (ChatColor.stripColor(sign.getLine(1)).equals("[WITHDRAW]")) {
                            if (p.hasPermission("itembank.use.sign.withdraw")) {
                                //TODO: fix gui
                                ItemBank.menu.show(p);
                            }
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You need permission: itembank.use.sign.atm");
                }
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        String deposit = e.getLine(0);

        if (deposit.equalsIgnoreCase("[deposit]")){
            if (p.hasPermission("itembank.create.sign.deposit")) {
                e.setLine(0, ChatColor.BLUE + "[DEPOSIT]");
                e.setLine(1, ChatColor.GREEN + "*****");
                String bank = e.getLine(2);
                if (bank != null && !bank.equals("")){
                    e.setLine(2, ChatColor.BLUE + bank);
                    e.setLine(3,"");
                    p.sendMessage(ChatColor.GREEN + "Deposit sign " + bank + " successfully created.");
                } else {
                    e.setLine(2,"");
                    e.setLine(3,"");
                    p.sendMessage(ChatColor.GREEN + "Deposit sign successfully created.");
                }
            } else {
                e.setLine(0, ChatColor.RED + "Forbidden.");
                p.sendMessage(ChatColor.RED + "You need permission: itembank.create.sign.deposit");
            }
        }
        if (deposit.equalsIgnoreCase("[withdraw]")){
            if (p.hasPermission("itembank.create.sign.withdraw")) {
                e.setLine(0, ChatColor.BLUE + "[WITHDRAW]");
                e.setLine(1, ChatColor.GREEN + "*****");
                String bank = e.getLine(2);
                if (bank != null && !bank.equals("")){
                    e.setLine(2, ChatColor.BLUE + bank);
                    e.setLine(3,"");
                    p.sendMessage(ChatColor.GREEN + "Withdraw sign " + bank + " successfully created.");
                } else {
                    e.setLine(2,"");
                    e.setLine(3,"");
                    p.sendMessage(ChatColor.GREEN + "Withdraw sign successfully created.");
                }
            } else {
                e.setLine(0, ChatColor.RED + "Forbidden.");
                p.sendMessage(ChatColor.RED + "You need permission: itembank.create.sign.withdraw");
            }
        }
        if (deposit.equalsIgnoreCase("[atm]")){
            if (p.hasPermission("itembank.create.sign.atm")) {
                e.setLine(0, ChatColor.DARK_PURPLE + "[ATM]");
                e.setLine(1, ChatColor.BLUE + "[DEPOSIT]");
                String bank = e.getLine(2);
                if (bank != null && !bank.equals("")){
                    e.setLine(2, ChatColor.BLUE + bank);
                    e.setLine(3,"");
                    p.sendMessage(ChatColor.GREEN + "ATM sign " + bank + " successfully created.");
                } else {
                    e.setLine(2,"");
                    e.setLine(3,"");
                    p.sendMessage(ChatColor.GREEN + "ATM sign successfully created.");
                }
            } else {
                e.setLine(0, ChatColor.RED + "Forbidden.");
                p.sendMessage(ChatColor.RED + "You need permission: itembank.create.sign.atm");
            }
        }
    }

    @EventHandler
    public void onBlockDamage(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();
        if (isWallSignPost(b.getType()))  {
            Sign sign = (Sign) b.getState(); //get sign object
            if (ChatColor.stripColor(sign.getLine(0)).equals("[ATM]") || ChatColor.stripColor(sign.getLine(0)).equals("[DEPOSIT]") || ChatColor.stripColor(sign.getLine(0)).equals("[WITHDRAW]")) {
                if (!p.hasPermission("itembank.break.sign")) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "You need permission: itembank.break.sign");
                }
            }
        }
    }

}
