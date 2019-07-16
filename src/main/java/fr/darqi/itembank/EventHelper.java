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
                p.sendMessage(ChatColor.RED + "You need: itembank.create.sign.deposit");
            }
        }
        if (deposit.equalsIgnoreCase("[withdraw]")){
            if (p.hasPermission("itembank.create.sign.withdraw")) {
                String valFirst = e.getLine(1);
                String valSecond = e.getLine(2);
                String valThird = e.getLine(3);
                try {
                    int first = Integer.parseInt(valFirst);
                    int second = Integer.parseInt(valSecond);
                    int third = Integer.parseInt(valThird);

                    if (first != 0 && second != 0 && third != 0) {
                        e.setLine(0, ChatColor.BLUE + "[WITHDRAW]");
                        e.setLine(1, ChatColor.AQUA + valFirst);
                        e.setLine(2, ChatColor.AQUA + valSecond);
                        e.setLine(3, ChatColor.AQUA + valThird);

                        p.sendMessage(ChatColor.GREEN + "Withdraw sign successfully created.");
                    } else {
                        e.setLine(0, ChatColor.RED + "Error.");
                        p.sendMessage(ChatColor.RED + "Tnteger(s) must be > 0.");
                    }

                } catch (NumberFormatException ex) {
                    e.setLine(0, ChatColor.RED + "Error.");
                    p.sendMessage(ChatColor.RED + "Please enter an integer ( > 0 ) on each line.\ne.g [withdraw] on first line, then 10, then 100, and 1000.");
                }
            } else {
                e.setLine(0, ChatColor.RED + "Forbidden.");
                p.sendMessage(ChatColor.RED + "You need: itembank.create.sign.withdraw");
            }
        }
        if (deposit.equalsIgnoreCase("[atm]")){
            if (p.hasPermission("itembank.create.sign.atm")) {
                p.sendMessage(ChatColor.GREEN + "ATM sign successfully created.");
            } else {
                e.setLine(0, ChatColor.RED + "Forbidden.");
                p.sendMessage(ChatColor.RED + "You need: itembank.create.sign.atm");
            }
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
        Action a = event.getAction();
        if (a != Action.RIGHT_CLICK_BLOCK) {
            //no action if it's not a right click on a block.
            return;
        }
        if (!ItemBank.ibSign) {
            //no action if config says NO!
            return;
        }
        Material mat = event.getMaterial();
        Player p = event.getPlayer();
        Material curr = Material.matchMaterial(ItemBank.ibCurrency);
        Block b = event.getClickedBlock();
        if (isWallSignPost(b.getType())) {
            Sign sign = (Sign) b.getState();
            if (ChatColor.stripColor(sign.getLine(0)).equals("[DEPOSIT]")) {
                //deposit sign
                if (p.hasPermission("itembank.use.sign.deposit")) {
                    if (mat == curr) {
                        return;
                    }
                    ItemStack[] inv = p.getInventory().getContents();
                    int total = 0;
                    int m = 0;
                    for (int i = 0; i < inv.length; i++) {
                        if (inv[i] != null) {
                            if (inv[i].getType() == curr) {
                                m = m + inv[i].getAmount();

                            }
                            total = total + 1;
                        }
                    }
                    if (m == 0) {
                        return;
                    }
                    p.getInventory().remove(curr);
                    EconomyResponse r = ItemBank.econ.depositPlayer(p, m);
                    String bank = sign.getLine(2);
                    if (!r.transactionSuccess()) {
                        p.sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
                        return;
                    }
                    if (bank != null && !bank.equals("")) {
                        p.sendMessage(String.format(ChatColor.AQUA + "You have just deposited all your %s in " + bank + ".\nDeposited: " + ChatColor.RESET + "%s\n" + ChatColor.AQUA + "Your account: " + ChatColor.RESET + "%s", ItemBank.ibCurrency, ItemBank.econ.format(r.amount), ItemBank.econ.format(r.balance)));
                    } else {
                        p.sendMessage(String.format(ChatColor.AQUA + "You have just deposited all your %s.\nDeposited: " + ChatColor.RESET + "%s\n" + ChatColor.AQUA + "Your account: " + ChatColor.RESET + "%s", ItemBank.ibCurrency, ItemBank.econ.format(r.amount), ItemBank.econ.format(r.balance)));
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You need: itembank.use.sign.deposit");
                }
            } else if (ChatColor.stripColor(sign.getLine(0)).equals("[WITHDRAW]")) {
                //withdraw sign
                if (p.hasPermission("itembank.use.sign.withdraw")) {
                    if (p.isSneaking()) {
                        //change value
                        String strFirst = sign.getLine(1);
                        String strSecond = sign.getLine(2);
                        String strThird = sign.getLine(3);
                        if (strFirst.startsWith(String.valueOf(ChatColor.RED))){
                            sign.setLine(1, ChatColor.AQUA + ChatColor.stripColor(strFirst));
                            sign.setLine(2, ChatColor.RED + ChatColor.stripColor(strSecond));
                        } else if (strSecond.startsWith(String.valueOf(ChatColor.RED))){
                            sign.setLine(2, ChatColor.AQUA + ChatColor.stripColor(strSecond));
                            sign.setLine(3, ChatColor.RED + ChatColor.stripColor(strThird));
                        } else if (strThird.startsWith(String.valueOf(ChatColor.RED))){
                            sign.setLine(3, ChatColor.AQUA + ChatColor.stripColor(strThird));
                            sign.setLine(1, ChatColor.RED + ChatColor.stripColor(strFirst));
                        } else {
                            sign.setLine(1, ChatColor.RED + ChatColor.stripColor(strFirst));
                        }
                    } else {
                        String test = sign.getLine(1);
                        if (test.startsWith(String.valueOf(ChatColor.AQUA))){
                            String strip = ChatColor.stripColor(test);
                            p.sendMessage(ChatColor.stripColor(test));
                            //log.info(ChatColor.stripColor(test));
                            sign.setLine(1, ChatColor.RED + strip);
                        } else {
                            p.sendMessage("false");
                        }
                    }
                }
            }
        }
    }

}
