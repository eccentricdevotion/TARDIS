/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TVMConfig {

    private final TARDISVortexManipulator plugin;
    HashMap<String, String> strOptions = new HashMap<>();
    HashMap<String, Integer> intOptions = new HashMap<>();
    HashMap<String, Boolean> boolOptions = new HashMap<>();
    private final FileConfiguration config;
    private final File configFile;

    public TVMConfig(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        // boolean
        boolOptions.put("allow.beacon", true);
        boolOptions.put("allow.lifesigns", true);
        boolOptions.put("allow.look_at_block", true);
        boolOptions.put("allow.messaging", true);
        boolOptions.put("allow.multiple", true);
        boolOptions.put("allow.teleport", true);
        boolOptions.put("debug", false);
        boolOptions.put("respect.factions", true);
        boolOptions.put("respect.griefprevention", true);
        boolOptions.put("respect.towny", true);
        boolOptions.put("respect.worldborder", true);
        boolOptions.put("respect.worldguard", true);
        boolOptions.put("storage.mysql.useSSL", false);
        // integer
        intOptions.put("lifesign_scan_distance", 16);
        intOptions.put("max_look_at_distance", 50);
        intOptions.put("recipe.amount", 1);
        intOptions.put("tachyon_use.beacon", 1000);
        intOptions.put("tachyon_use.lifesigns", 15);
        intOptions.put("tachyon_use.max", 1000);
        intOptions.put("tachyon_use.message", 5);
        intOptions.put("tachyon_use.recharge", 25);
        intOptions.put("tachyon_use.recharge_interval", 600);
        intOptions.put("tachyon_use.travel.coords", 200);
        intOptions.put("tachyon_use.travel.random", 100);
        intOptions.put("tachyon_use.travel.saved", 50);
        intOptions.put("tachyon_use.travel.to_block", 75);
        intOptions.put("tachyon_use.travel.world", 150);
        intOptions.put("block_travel_malfunction_chance", 0);
        intOptions.put("storage.mysql.port", 3306);
        // string
        strOptions.put("date_format", "dd/MM/YY HH:mm");
        strOptions.put("recipe.ingredients.B", "STONE_BUTTON");
        strOptions.put("recipe.ingredients.C", "COMPASS");
        strOptions.put("recipe.ingredients.G", "GLASS");
        strOptions.put("recipe.ingredients.I", "IRON_INGOT");
        strOptions.put("recipe.ingredients.O", "GOLD_INGOT");
        strOptions.put("recipe.ingredients.W", "CLOCK");
        strOptions.put("recipe.lore", "Cheap and nasty time travel");
        strOptions.put("recipe.result", "CLOCK");
        strOptions.put("recipe.shape", "BBG,WOC,III");
        strOptions.put("storage.database", "sqlite");
        strOptions.put("storage.mysql.password", "mysecurepassword");
        strOptions.put("storage.mysql.host", "localhost");
        strOptions.put("storage.mysql.database", "TVM");
        strOptions.put("storage.mysql.user", "bukkit");
        strOptions.put("storage.mysql.prefix", "");
    }

    /**
     * Checks that the configuration file contains all the required entries. If entries are missing, then they are added
     * with default values.
     */
    public void checkConfig() {
        int i = 0;
        // boolean values
        for (Map.Entry<String, Boolean> entry : boolOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : intOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : strOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        if (config.getString("recipe.ingredients.W").equals("WATCH")) {
            plugin.getConfig().set("recipe.ingredients.W", "CLOCK");
            plugin.getConfig().set("recipe.result", "CLOCK");
            i++;
        }
        // check mysql settings
        if (config.contains("storage.mysql.url")) {
            // mysql://localhost:3306/TARDIS
            String[] firstSplit = config.getString("storage.mysql.url").split(":");
            String host = firstSplit[1].substring(2);
            String[] secondSplit = firstSplit[2].split("/");
            String port = secondSplit[0];
            String database = secondSplit[1];
            plugin.getConfig().set("storage.mysql.host", host);
            plugin.getConfig().set("storage.mysql.port", port);
            plugin.getConfig().set("storage.mysql.database", database);
            plugin.getConfig().set("storage.mysql.url", null);
            i++;
        }
        if (i > 0) {
            plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
        }
        plugin.saveConfig();
    }
}
