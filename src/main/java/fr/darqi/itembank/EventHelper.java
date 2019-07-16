package fr.darqi.itembank;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EventHelper {

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

}
