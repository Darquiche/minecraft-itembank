package fr.darqi.itembank;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {

    //method called when somebody uses /ib [args}
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String ibInfos = String.format(ChatColor.YELLOW + "----- [%s - Infos] -----\n" + ChatColor.AQUA + "Plugin version: [%s]\n" + ChatColor.DARK_GREEN + "Website: %s \nDeveloped by %s", ItemBank.name, ItemBank.version, ItemBank.website, ItemBank.author);
        String ibHelp = String.format(ChatColor.YELLOW + "----- [%s] -----\n" + ChatColor.RESET + "/ib help: Shows this message.\n/ib info: Shows infos about ItemBank.\n/ib admin: Shows help message about admin commands.", ItemBank.name);
        String ibAdminHelp = String.format(ChatColor.YELLOW + "----- [%s - Admin] -----\n" + ChatColor.RESET + "/ib config: Shows infos about ItemBank's config.\n/ib debug: Gives you some [%s] to test the plugin.", ItemBank.name, ItemBank.ibCurrency);
        String ibConfigMsg = String.format(ChatColor.YELLOW + "----- [%s - Config] -----\n" + ChatColor.DARK_PURPLE + "Hooked economy system: [%s]\nThis plugin uses Vault to hook your economy plugin.\nCurrency: [%s]\nThis item is used like money on your server.\nValue: [%s]\nOne item has this value.\nOn Click feature: [%s]\nThis feature allows players to right click with item to deposit it.\nSign: [%s]\nThis feature allows you to create deposit/withdraw/atm signs that players can use.", ItemBank.name, ItemBank.econ.getName(), ItemBank.ibCurrency, ItemBank.ibValue, ItemBank.ibOnClick, ItemBank.ibSign);

        if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
            // called when: /ib help
            if (sender.hasPermission("itembank.commands.ib")) {
                sender.sendMessage(ibHelp);
            } else {
                sender.sendMessage(ChatColor.RED + "You need permission: itembank.commands.ib");
            }
        } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("infos")) {
            // called when: /ib info
            if (sender.hasPermission("itembank.commands.ib")) {
                sender.sendMessage(ibInfos);
            } else {
                sender.sendMessage(ChatColor.RED + "You need permission: itembank.commands.ib");
            }
        } else if (args[0].equalsIgnoreCase("admin-help") || args[0].equalsIgnoreCase("admin")) {
            // called when: /ib admin
            if (sender.hasPermission("itembank.commands.ib.admin")) {
                sender.sendMessage(ibAdminHelp);
            } else {
                sender.sendMessage(ChatColor.RED + "You need permission: itembank.commands.ib.admin");
            }
        } else if (args[0].equalsIgnoreCase("config")) {
            // called when: /ib config
            if (sender.hasPermission("itembank.commands.ib.admin")) {
                sender.sendMessage(ibConfigMsg);
            } else {
                sender.sendMessage(ChatColor.RED + "You need permission: itembank.commands.ib.admin");
            }
        } else if (args[0].equalsIgnoreCase("debug")) {
            // called when: /ib debug
            if (sender.hasPermission("itembank.commands.ib.admin")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Material m = Material.matchMaterial(ItemBank.ibCurrency);
                    ItemStack curr = new ItemStack(m, 3);
                    player.getInventory().addItem(curr);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You need permission: itembank.commands.ib.admin");
            }
        }
        return true;
    }

}
