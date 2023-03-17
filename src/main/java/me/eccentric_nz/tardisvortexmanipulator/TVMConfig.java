/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author eccentric_nz
 */
public class TVMConfig {

    private final TARDIS plugin;
    HashMap<String, String> strOptions = new HashMap<>();
    HashMap<String, Integer> intOptions = new HashMap<>();
    HashMap<String, Boolean> boolOptions = new HashMap<>();
    private final FileConfiguration config;
    private final File configFile;

    public TVMConfig(TARDIS plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "vortex_manipulator.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        // boolean
        boolOptions.put("allow.beacon", true);
        boolOptions.put("allow.lifesigns", true);
        boolOptions.put("allow.look_at_block", true);
        boolOptions.put("allow.messaging", true);
        boolOptions.put("allow.multiple", true);
        boolOptions.put("allow.teleport", true);
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
                plugin.getVortexConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : intOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getVortexConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : strOptions.entrySet()) {
            if (!config.contains(entry.getKey())) {
                plugin.getVortexConfig().set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        if (i > 0) {
            plugin.getServer().getConsoleSender().sendMessage(MODULE.VORTEX_MANIPULATOR.getName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
        }
        try {
            String handlesPath = plugin.getDataFolder() + File.separator + "monsters.yml";
            config.save(new File(handlesPath));
            plugin.getLogger().log(Level.INFO, "Updated monsters.yml");
        } catch (IOException io) {
            plugin.debug("Could not save monsters.yml, " + io.getMessage());
        }
    }
}
