package me.eccentric_nz.tardisshop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.TardisAPI;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import me.eccentric_nz.tardisshop.database.TARDISShopDatabase;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemBreak;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemDespawn;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemExplode;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TARDISShop extends JavaPlugin {

    private static boolean twaEnabled = false;
    private static TardisAPI tardisapi;
    private final TARDISShopDatabase service = TARDISShopDatabase.getInstance();
    private final HashMap<UUID, TARDISShopItem> settingItem = new HashMap<>();
    private final Set<UUID> removingItem = new HashSet<>();
    private String pluginName;
    private Economy economy;
    private TARDIS tardis;
    private NamespacedKey itemKey;
    private Material blockMaterial;
    private FileConfiguration itemsConfig;

    public static boolean isTWAEnabled() {
        return twaEnabled;
    }

    public static TardisAPI getTardisAPI() {
        return tardisapi;
    }

    @Override
    public void onDisable() {
        try {
            if (service.connection != null) {
                service.connection.close();
            }
        } catch (SQLException e) {
            debug("Could not close database connection: " + e);
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        String enable = "";
        /* Get TARDIS */
        Plugin p = pm.getPlugin("TARDIS");
        if (p == null || !p.isEnabled()) {
            enable = "TARDIS";
        }
        copy("items.yml");
        tardis = (TARDIS) p;
        tardisapi = tardis.getTardisAPI();
        /* Get Vault */
        Plugin vault = pm.getPlugin("Vault");
        if (vault == null || !vault.isEnabled()) {
            enable = "Vault";
        }
        if (pm.isPluginEnabled("TARDISWeepingAngels")) {
            twaEnabled = true;
        }
        if (enable.isEmpty()) {
            setupEconomy();
            PluginDescriptionFile pdfFile = getDescription();
            pluginName = ChatColor.GOLD + "[" + pdfFile.getName() + "]" + ChatColor.RESET + " ";
            pm.registerEvents(new TARDISShopItemInteract(this), this);
            pm.registerEvents(new TARDISShopItemDespawn(this), this);
            pm.registerEvents(new TARDISShopItemBreak(this), this);
            pm.registerEvents(new TARDISShopItemExplode(this), this);
            itemKey = new NamespacedKey(this, "tardis_shop_item");
            blockMaterial = Material.valueOf(getConfig().getString("block"));
            itemsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "items.yml"));
            TARDISShopCommand command = new TARDISShopCommand(this);
            getCommand("tardisshop").setExecutor(command);
            getCommand("tardisshop").setTabCompleter(command);
            try {
                String path = getDataFolder() + File.separator + "TARDISShop.db";
                service.setConnection(path);
                service.createTables();
            } catch (Exception e) {
                getServer().getConsoleSender().sendMessage(pluginName + "Connection and Tables Error: " + e);
            }
        } else {
            getServer().getConsoleSender().sendMessage(pluginName + ChatColor.RED + "This plugin requires " + enable + " to function, disabling...");
            pm.disablePlugin(this);
        }
    }

    //Loading economy API from Vault
    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

    public String getPluginName() {
        return pluginName;
    }

    public Economy getEconomy() {
        return economy;
    }

    public NamespacedKey getItemKey() {
        return itemKey;
    }

    public HashMap<UUID, TARDISShopItem> getSettingItem() {
        return settingItem;
    }

    public Set<UUID> getRemovingItem() {
        return removingItem;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public FileConfiguration getItemsConfig() {
        return itemsConfig;
    }

    public void debug(Object o) {
        if (getConfig().getBoolean("debug") == true) {
            getServer().getConsoleSender().sendMessage("[TARDISShop Debug] " + o);
        }
    }

    /**
     * Copies a file to the TARDISShop plugin directory if it is not present.
     *
     * @param filename the name of the file to copy
     * @return a File
     */
    private File copy(String filename) {
        String filepath = getDataFolder() + File.separator + filename;
        InputStream in = getResource(filename);
        return TARDISFileCopier.copy(filepath, in, false);
    }
}
