package me.eccentric_nz.TARDIS.files;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TARDISConfiguration {

    private final TARDIS plugin;
    private FileConfiguration config = null;
    private File configFile = null;
    HashMap<String, String> strOptions = new HashMap<String, String>();
    HashMap<String, Integer> intOptions = new HashMap<String, Integer>();
    HashMap<String, Boolean> boolOptions = new HashMap<String, Boolean>();

    public TARDISConfiguration(TARDIS plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), TARDISConstants.CONFIG_FILE_NAME);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        // boolean
        boolOptions.put("debug", false);
        boolOptions.put("bonus_chest", true);
        boolOptions.put("chameleon", true);
        boolOptions.put("default_world", false);
        boolOptions.put("give_key", false);
        boolOptions.put("include_default_world", false);
        boolOptions.put("land_on_water", true);
        boolOptions.put("name_tardis", false);
        boolOptions.put("nether", false);
        boolOptions.put("platform", false);
        boolOptions.put("protect_blocks", true);
        boolOptions.put("require_spout", false);
        boolOptions.put("sfx", true);
        boolOptions.put("the_end", false);
        boolOptions.put("use_worldguard", true);
        boolOptions.put("respect_worldguard", true);
        boolOptions.put("respect_towny", true);
        boolOptions.put("respect_worldborder", true);
        // integer
        intOptions.put("timeout_height", 135);
        intOptions.put("timeout", 5);
        intOptions.put("tp_radius", 256);
        intOptions.put("border_radius", 256);
        // string
        strOptions.put("default_world_name", "myridiculouslylongworldnameiscalledcuthbert");
        strOptions.put("key", "STICK");
    }

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
        if (i > 0) {
            plugin.console.sendMessage(plugin.pluginName + "Added '" + ChatColor.AQUA + i + ChatColor.RESET + "' new items to config");
        }
        // add worlds
        List<World> worlds = this.plugin.getServer().getWorlds();
        for (World w : worlds) {
            String worldname = "worlds." + w.getName();
            if (!config.contains(worldname)) {
                plugin.getConfig().set(worldname, true);
                plugin.console.sendMessage(plugin.pluginName + "Added '" + w.getName() + "' to config. To exclude this world run: /tardis admin exclude " + w.getName());
            }
        }
        plugin.saveConfig();
        // now remove worlds that may have been deleted
        Set<String> cWorlds = plugin.getConfig().getConfigurationSection("worlds").getKeys(true);
        for (String cw : cWorlds) {
            if (plugin.getServer().getWorld(cw) == null) {
                plugin.getConfig().set("worlds." + cw, null);
                plugin.console.sendMessage(plugin.pluginName + "Removed '" + cw + " from config.yml");
            }
        }
        plugin.saveConfig();
    }
}