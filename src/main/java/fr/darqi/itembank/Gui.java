package fr.darqi.itembank;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class Gui {

    private Inventory inv;
    private ItemStack v1, v2, v3, v4, v5, v6, v7, v8, v9;
    private int i1, i2, i3, i4, i5, i6, i7, i8, i9;

    private Material curr = Material.matchMaterial(ItemBank.ibCurrency); //convert string to material

    public Gui(Plugin p, int val1, int val2, int val3, int val4, int val5, int val6, int val7, int val8, int val9) {
        inv = Bukkit.getServer().createInventory(null, 9, "Withdraw");

        i1 = val1;
        i2 = val2;
        i3 = val3;
        i4 = val4;
        i5 = val5;
        i6 = val6;
        i7 = val7;
        i8 = val8;
        i9 = val9;

        v1 = createItem(DyeColor.WHITE, ChatColor.BLUE + "Withdraw: Option 1", i1);
        v2 = createItem(DyeColor.YELLOW, ChatColor.BLUE + "Withdraw: Option 2", i2);
        v3 = createItem(DyeColor.ORANGE, ChatColor.BLUE + "Withdraw: Option 3", i3);
        v4 = createItem(DyeColor.PINK, ChatColor.BLUE + "Withdraw: Option 4", i4);
        v5 = createItem(DyeColor.MAGENTA, ChatColor.BLUE + "Withdraw: Option 5", i5);
        v6 = createItem(DyeColor.PURPLE, ChatColor.BLUE + "Withdraw: Option 6", i6);
        v7 = createItem(DyeColor.BLUE, ChatColor.BLUE + "Withdraw: Option 7", i7);
        v8 = createItem(DyeColor.GRAY, ChatColor.BLUE + "Withdraw: Option 8", i8);
        v9 = createItem(DyeColor.BLACK, ChatColor.BLUE + "Withdraw: Option 9", i9);

        inv.setItem(0, v1);
        inv.setItem(1, v2);
        inv.setItem(2, v3);
        inv.setItem(3, v4);
        inv.setItem(4, v5);
        inv.setItem(5, v6);
        inv.setItem(6, v7);
        inv.setItem(7, v8);
        inv.setItem(8, v9);

        //Bukkit.getServer().getPluginManager().registerEvents(this, p);
    }


    private ItemStack createItem(DyeColor dc, String name, int v) {
        ItemStack i = new Wool(dc).toItemStack(1);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(String.format("Receive %s", v)));
        i.setItemMeta(im);
        return i;
    }

    private void givePlayer(Player p, int money) {
        ItemStack give = new ItemStack(curr, money);
        int availableSlots = availableSlots(p);
        int stackSize = give.getMaxStackSize();
        double slotsNeeded = Math.ceil(money / stackSize);
        int extraSlot = money % stackSize;

        boolean possible = true;

        if (extraSlot != 0) {
            slotsNeeded++;
        }
        if (slotsNeeded > availableSlots) {
            possible = false;
        }

        if (possible) {
            EconomyResponse r = ItemBank.econ.withdrawPlayer(p, money); //withdraw items
            if(r.transactionSuccess()) {
                p.getInventory().addItem(give);
                p.sendMessage(String.format(ChatColor.GREEN + "You have just withdrawn: " + ChatColor.RESET + "%s\n" + ChatColor.GREEN + "Your account: " + ChatColor.RESET + "%s", ItemBank.econ.format(r.amount), ItemBank.econ.format(r.balance)));
            } else {
                p.sendMessage(String.format(ChatColor.RED + "An error occured: %s", r.errorMessage));
            }
        } else {
            p.sendMessage(ChatColor.RED + "An error occured: not enough space in your inventory");
        }
    }

    private int availableSlots(Player player) {
        Inventory inv = player.getInventory();
        int check = 0;
        for (ItemStack item : inv.getStorageContents()) {
            if (item == null) {
                check++;
            }
        }
        return check;
    }

}
