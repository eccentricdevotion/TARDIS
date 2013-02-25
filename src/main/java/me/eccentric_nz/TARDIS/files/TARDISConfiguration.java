/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

/**
 * The Alpha Centauran Table Tennis Club is a club established by the Alpha
 * Centaurans for the play of table tennis. The species is naturally gifted at
 * the game, since they possess six arms. The Doctor is a member of the Club.
 *
 * @author eccentric_nz
 */
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
        boolOptions.put("sfx", true);
        boolOptions.put("the_end", false);
        boolOptions.put("use_worldguard", true);
        boolOptions.put("respect_worldguard", true);
        boolOptions.put("respect_towny", true);
        boolOptions.put("respect_worldborder", true);
        boolOptions.put("create_worlds", true);
        boolOptions.put("check_for_updates", true);
        boolOptions.put("materialise", true);
        // integer
        intOptions.put("border_radius", 64);
        intOptions.put("comehere", 400);
        intOptions.put("confirm_timeout", 15);
        intOptions.put("creeper_recharge", 150);
        intOptions.put("full_charge", 5000);
        intOptions.put("hide", 500);
        intOptions.put("inventory_group", 0);
        intOptions.put("jettison", 75);
        intOptions.put("lightning_recharge", 300);
        intOptions.put("nether_min", 4250);
        intOptions.put("player", 25);
        intOptions.put("random", 75);
        intOptions.put("recharge_distance", 20);
        intOptions.put("rooms.ARBORETUM.cost", 325);
        intOptions.put("rooms.BAKER.cost", 350);
        intOptions.put("rooms.BEDROOM.cost", 475);
        intOptions.put("rooms.EMPTY.cost", 250);
        intOptions.put("rooms.GRAVITY.cost", 625);
        intOptions.put("rooms.ANTIGRAVITY.cost", 625);
        intOptions.put("rooms.HARMONY.cost", 450);
        intOptions.put("rooms.KITCHEN.cost", 450);
        intOptions.put("rooms.LIBRARY.cost", 550);
        intOptions.put("rooms.PASSAGE.cost", 200);
        intOptions.put("rooms.POOL.cost", 450);
        intOptions.put("rooms.VAULT.cost", 350);
        intOptions.put("rooms.WOOD.cost", 350);
        intOptions.put("rooms.WORKSHOP.cost", 400);
        intOptions.put("the_end_min", 5500);
        intOptions.put("timeout", 5);
        intOptions.put("timeout_height", 135);
        intOptions.put("tp_radius", 256);
        intOptions.put("travel", 100);
        // string
        strOptions.put("default_world_name", "myridiculouslylongworldnameiscalledcuthbert");
        strOptions.put("key", "STICK");
        strOptions.put("jettison_seed", "TNT");
        strOptions.put("full_charge_item", "NETHER_STAR");
        strOptions.put("rooms.ARBORETUM.seed", "LEAVES");
        strOptions.put("rooms.BAKER.seed", "ENDER_STONE");
        strOptions.put("rooms.BEDROOM.seed", "GLOWSTONE");
        strOptions.put("rooms.EMPTY.seed", "GLASS");
        strOptions.put("rooms.GRAVITY.seed", "MOSSY_COBBLESTONE");
        strOptions.put("rooms.ANTIGRAVITY.seed", "SANDSTONE");
        strOptions.put("rooms.HARMONY.seed", "BRICK_STAIRS");
        strOptions.put("rooms.KITCHEN.seed", "PUMPKIN");
        strOptions.put("rooms.LIBRARY.seed", "BOOKSHELF");
        strOptions.put("rooms.PASSAGE.seed", "CLAY");
        strOptions.put("rooms.POOL.seed", "SNOW_BLOCK");
        strOptions.put("rooms.VAULT.seed", "DISPENSER");
        strOptions.put("rooms.WOOD.seed", "WOOD");
        strOptions.put("rooms.WORKSHOP.seed", "NETHER_BRICK");
        strOptions.put("gamemode", "survival");
    }

    /**
     * Checks that the config file contains all the required entries. If entries
     * are missing, then they are added with default values. Also checks that
     * all current server worlds are added to the config, and any deleted worlds
     * are removed.
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
        if (!config.isConfigurationSection("rechargers")) {
            plugin.getConfig().createSection("rechargers");
            i++;
        }
        if (i > 0) {
            plugin.console.sendMessage(plugin.pluginName + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to config");
        }
        // worlds
        doWorlds();
        plugin.saveConfig();
    }

    public void doWorlds() {
        List<World> worlds = plugin.getServer().getWorlds();
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
    }
}
