package fr.darqi.itembank;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ItemBank extends JavaPlugin implements Listener {

    private static final Logger log = Logger.getLogger("Minecraft");
    private FileConfiguration config = getConfig();

    static Gui menu;

    static String name;
    static String version;
    static String author;
    static String website;

    static String ibCurrency;
    static int ibValue;
    static boolean ibOnClick;
    static boolean ibSign;

    static Economy econ = null;
    EventHelper evn = new EventHelper();

    @Override
    public void onLoad(){
        config.addDefault("item", "GOLD_NUGGET");
        config.addDefault("value",1);
        config.addDefault("on_click",true);
        config.addDefault("sign",true);
        config.addDefault("withdraw.value_1", 1);
        config.addDefault("withdraw.value_2", 5);
        config.addDefault("withdraw.value_3", 10);
        config.addDefault("withdraw.value_4", 50);
        config.addDefault("withdraw.value_5", 100);
        config.addDefault("withdraw.value_6", 500);
        config.addDefault("withdraw.value_7", 1000);
        config.addDefault("withdraw.value_8", 1500);
        config.addDefault("withdraw.value_9", 2000);
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onEnable(){
        PluginDescriptionFile pdf = getDescription();
        name = pdf.getName();
        version = pdf.getVersion();
        author = pdf.getAuthors().get(0);
        website = pdf.getWebsite();

        ibCurrency = config.getString("item");
        ibValue = config.getInt("value");
        ibOnClick = config.getBoolean("on_click");
        ibSign = config.getBoolean("sign");

        int ibWdValue1 = config.getInt("withdraw.value_1");
        int ibWdValue2 = config.getInt("withdraw.value_2");
        int ibWdValue3 = config.getInt("withdraw.value_3");
        int ibWdValue4 = config.getInt("withdraw.value_4");
        int ibWdValue5 = config.getInt("withdraw.value_5");
        int ibWdValue6 = config.getInt("withdraw.value_6");
        int ibWdValue7 = config.getInt("withdraw.value_7");
        int ibWdValue8 = config.getInt("withdraw.value_8");
        int ibWdValue9 = config.getInt("withdraw.value_9");

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] Disabled due to no Vault dependency found!", name));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        log.info(String.format("[%s] Economy method: [%s] found.", name, econ.getName()));

        getServer().getPluginManager().registerEvents(evn, this);
        this.getCommand("ib").setExecutor(new Commands());

        menu = new Gui(this, ibWdValue1, ibWdValue2, ibWdValue3, ibWdValue4, ibWdValue5, ibWdValue6, ibWdValue7, ibWdValue8, ibWdValue9);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        log.info("onDisable is called!");
    }
}
