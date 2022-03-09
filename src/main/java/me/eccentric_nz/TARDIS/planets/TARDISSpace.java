/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.planets;

import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.WorldGroup;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.InventoryManager;
import me.eccentric_nz.TARDIS.perms.TARDISGroupManagerHandler;
import me.eccentric_nz.TARDIS.perms.TARDISPermissionsExHandler;
import me.eccentric_nz.TARDIS.perms.TARDISbPermissionsHandler;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

/**
 * The Time Vortex is the dimension through which all time travellers pass. The Vortex was built by the Time Lords as a
 * transdimensional spiral that connected all points in space and time.
 *
 * @author eccentric_nz
 */
public class TARDISSpace {

    private final TARDIS plugin;
    private World tardisWorld = null;

    public TARDISSpace(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets custom world for the specified TARDIS. If the world does not exist, it is created.
     *
     * @param name the name of this world
     * @return a new TARDIS World
     */
    public World getTardisWorld(String name) {
        if (tardisWorld == null) {
            tardisWorld = WorldCreator.name(name).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator("TARDISChunkGenerator:void").generateStructures(false).createWorld();
            // add world to config, but time travel disabled by default
            plugin.getPlanetsConfig().set("planets." + name + ".enabled", true);
            plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
            plugin.getPlanetsConfig().set("planets." + name + ".resource_pack", "default");
            plugin.getPlanetsConfig().set("planets." + name + ".gamemode", "SURVIVAL");
            plugin.getPlanetsConfig().set("planets." + name + ".world_type", "FLAT");
            plugin.getPlanetsConfig().set("planets." + name + ".environment", "NORMAL");
            plugin.getPlanetsConfig().set("planets." + name + ".void", true);
            plugin.getPlanetsConfig().set("planets." + name + ".generator", "TARDISChunkGenerator:void");
            plugin.getPlanetsConfig().set("planets." + name + ".gamerules.doWeatherCycle", false);
            plugin.getPlanetsConfig().set("planets." + name + ".gamerules.doDaylightCycle", false);
            plugin.savePlanetsConfig();
            String inventory_group = plugin.getConfig().getString("creation.inventory_group");
            if (!inventory_group.equals("0")) {
                if (plugin.getInvManager() == InventoryManager.MULTIVERSE) {
                    MultiverseInventories mi = (MultiverseInventories) plugin.getPM().getPlugin("Multiverse-Inventories");
                    WorldGroup wgp = mi.getGroupManager().getGroup(inventory_group);
                    wgp.addWorld(name);
                }
            }
            if (plugin.getPM().isPluginEnabled("WorldBorder")) {
                // wb <world> set <radius> <x> <z>
                plugin.getServer().dispatchCommand(plugin.getConsole(), "wb " + name + " set " + plugin.getConfig().getInt("creation.border_radius") + " 0 0");
            }
            if (plugin.getConfig().getBoolean("creation.add_perms")) {
                if (plugin.getPM().isPluginEnabled("GroupManager")) {
                    TARDISGroupManagerHandler tgmh = new TARDISGroupManagerHandler(plugin);
                    String player = name.substring(13);
                    tgmh.addPerms(player);
                }
                if (plugin.getPM().isPluginEnabled("bPermissions")) {
                    TARDISbPermissionsHandler tbph = new TARDISbPermissionsHandler(plugin);
                    String player = name.substring(13);
                    tbph.addPerms(player);
                }
                if (plugin.getPM().isPluginEnabled("PermissionsEx")) {
                    TARDISPermissionsExHandler tpesxh = new TARDISPermissionsExHandler(plugin);
                    String player = name.substring(13);
                    tpesxh.addPerms(player);
                }
            }
        }
        tardisWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        tardisWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        // set the time to night
        tardisWorld.setTime(14000L);
        return tardisWorld;
    }

    public void createDefaultWorld(String name) {
        WorldCreator.name(name).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator("TARDISChunkGenerator:void").generateStructures(false).createWorld();
        // add world to config, but disabled by default
        plugin.getPlanetsConfig().set("planets." + name + ".enabled", true);
        plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
        plugin.getPlanetsConfig().set("planets." + name + ".resource_pack", "default");
        plugin.getPlanetsConfig().set("planets." + name + ".gamemode", "SURVIVAL");
        plugin.getPlanetsConfig().set("planets." + name + ".world_type", "FLAT");
        plugin.getPlanetsConfig().set("planets." + name + ".environment", "NORMAL");
        plugin.getPlanetsConfig().set("planets." + name + ".void", true);
        plugin.getPlanetsConfig().set("planets." + name + ".generator", "TARDISChunkGenerator:void");
        plugin.savePlanetsConfig();
    }
}
