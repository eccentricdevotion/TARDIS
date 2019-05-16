/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.perms.TARDISGroupManagerHandler;
import me.eccentric_nz.TARDIS.perms.TARDISPermissionsExHandler;
import me.eccentric_nz.TARDIS.perms.TARDISbPermissionsHandler;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.util.List;

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
            tardisWorld = WorldCreator.name(name).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(new TARDISChunkGenerator()).generateStructures(false).createWorld();
            // set the time to night
            tardisWorld.setTime(14000L);
            // add world to config, but time travel disabled by default
            plugin.getPlanetsConfig().set("planets." + name + ".enabled", true);
            plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
            plugin.getPlanetsConfig().set("planets." + name + ".resource_pack", "default");
            plugin.getPlanetsConfig().set("planets." + name + ".gamemode", "SURVIVAL");
            plugin.getPlanetsConfig().set("planets." + name + ".world_type", "FLAT");
            plugin.getPlanetsConfig().set("planets." + name + ".environment", "NORMAL");
            plugin.getPlanetsConfig().set("planets." + name + ".void", true);
            plugin.getPlanetsConfig().set("planets." + name + ".generator", "TARDISChunkGenerator");
            plugin.savePlanetsConfig();
            String inventory_group = plugin.getConfig().getString("creation.inventory_group");
            if (!inventory_group.equals("0")) {
                switch (plugin.getInvManager()) {
//                    case MULTI:
//                        // No API to add world to group
//                        HashMap<String, String> migroups = MIYamlFiles.getGroups();
//                        migroups.put(name, inventory_group);
//                        // save YAML file and reload
//                        break;
                    case MULTIVERSE:
                        MultiverseInventories mi = (MultiverseInventories) plugin.getPM().getPlugin("Multiverse-Inventories");
                        WorldGroup wgp = mi.getGroupManager().getGroup(inventory_group);
                        wgp.addWorld(name);
                        break;
//                    case PER_WORLD:
//                        // No API to add world to group
//                        PerWorldInventory pwi = (PerWorldInventory) plugin.getPM().getPlugin("PerWorldInventory");
//                        GroupManager pwigm = pwi.getGroupManager();
//                        Group gf = pwigm.getGroup(inventory_group);
//                        gf.getWorlds().add(name);
//                        break;
                    default:
                        break;
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
        return tardisWorld;
    }

    public void keepNight() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::timechk, 60L, 1200L);
    }

    private void timechk() {
        List<World> serverWorlds = plugin.getServer().getWorlds();
        serverWorlds.forEach((w) -> {
            if (w.getName().contains("tardis_") && w.getEnvironment().equals(Environment.NORMAL)) {
                Long now = w.getTime();
                Long dawn = 14000L;
                Long dusk = 21000L;
                if (now < dawn || now > dusk) {
                    // set the time to dawn
                    w.setTime(dawn);
                }
            }
        });
    }

    public void createDefaultWorld(String name) {
        WorldCreator.name(name).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator("TARDISChunkGenerator").generateStructures(false).createWorld();
        // add world to config, but disabled by default
        plugin.getPlanetsConfig().set("planets." + name + ".enabled", true);
        plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
        plugin.getPlanetsConfig().set("planets." + name + ".resource_pack", "default");
        plugin.getPlanetsConfig().set("planets." + name + ".gamemode", "SURVIVAL");
        plugin.getPlanetsConfig().set("planets." + name + ".world_type", "FLAT");
        plugin.getPlanetsConfig().set("planets." + name + ".environment", "NORMAL");
        plugin.getPlanetsConfig().set("planets." + name + ".void", true);
        plugin.getPlanetsConfig().set("planets." + name + ".generator", "TARDISChunkGenerator");
        plugin.savePlanetsConfig();
    }
}
