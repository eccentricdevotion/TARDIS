/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.api.profile.WorldGroupProfile;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.perms.TARDISGroupManagerHandler;
import me.eccentric_nz.TARDIS.perms.TARDISPermissionsExHandler;
import me.eccentric_nz.TARDIS.perms.TARDISbPermissionsHandler;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

/**
 * The Time Vortex is the dimension through which all time travellers pass. The
 * Vortex was built by the Time Lords as a transdimensional spiral that
 * connected all points in space and time.
 *
 * @author eccentric_nz
 */
public class TARDISSpace {

    private final TARDIS plugin;
    public World tardisWorld = null;

    public TARDISSpace(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets custom world for the specified TARDIS. If the world does not exist,
     * it is created.
     *
     * @param name the name of this world
     * @return a new TARDID World
     */
    public World getTardisWorld(String name) {
        if (tardisWorld == null) {
            String gm = plugin.getConfig().getString("creation.gamemode").toLowerCase(Locale.ENGLISH);
            if (plugin.getPM().isPluginEnabled("MultiWorld")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw create " + name + " plugin:TARDISChunkGenerator");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw load " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw setflag " + name + " PvP false");
                if (gm.equalsIgnoreCase("creative")) {
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "mw setflag " + name + " CreativeWorld true");
                }
                tardisWorld = plugin.getServer().getWorld(name);
            } else if (plugin.isMVOnServer()) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv create " + name + " NORMAL -g TARDISChunkGenerator -t FLAT");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set hidden true " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set weather false " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set portalform none " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set adjustspawn false " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set pvp false " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set mode " + gm + " " + name);
                tardisWorld = plugin.getServer().getWorld(name);
            } else {
                tardisWorld = WorldCreator.name(name).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(new TARDISChunkGenerator()).createWorld();
            }
            // set the time to night
            tardisWorld.setTime(14000L);
            // add world to config, but disabled by default
            plugin.getConfig().set("worlds." + name, false);
            plugin.saveConfig();
            String inventory_group = plugin.getConfig().getString("creation.inventory_group");
            if (plugin.getPM().isPluginEnabled("My Worlds")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds load " + name + ":TARDISChunkGenerator");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds weather always sunny " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds gamemode " + gm + " " + name);
                if (plugin.getConfig().getBoolean("creation.keep_night")) {
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds time always night " + name);
                }
                if (!inventory_group.equals("0")) {
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "world inventory merge " + name + " " + inventory_group);
                }
                plugin.getServer().dispatchCommand(plugin.getConsole(), "world config save");
            }
            if (plugin.isMVIOnServer()) {
                if (!inventory_group.equals("0")) {
                    MultiverseInventories mi = (MultiverseInventories) plugin.getPM().getPlugin("Multiverse-Inventories");
                    WorldGroupProfile wgp = mi.getGroupManager().getGroup(inventory_group);
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
        return tardisWorld;
    }

    public void keepNight() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                timechk();
            }
        }, 60L, 1200L);
    }

    private void timechk() {
        List<World> serverWorlds = plugin.getServer().getWorlds();
        for (World w : serverWorlds) {
            if (w.getName().contains("TARDIS_") && w.getEnvironment().equals(Environment.NORMAL)) {
                Long now = w.getTime();
                Long dawn = 14000L;
                Long dusk = 21000L;
                if (now < dawn || now > dusk) {
                    // set the time to dawn
                    w.setTime(dawn);
                }
            }
        }
    }

    public void createDefaultWorld(String name) {
        if (plugin.getPM().isPluginEnabled("MultiWorld")) {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mw create " + name + " plugin:TARDISChunkGenerator");
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mw load " + name);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mw setflag " + name + " PvP false");
        } else if (plugin.isMVOnServer()) {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mv create " + name + " NORMAL -g TARDISChunkGenerator -t FLAT");
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set hidden true " + name);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set weather false " + name);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set portalform none " + name);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set adjustspawn false " + name);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "mv modify set pvp false " + name);
        } else {
            WorldCreator.name(name).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(new TARDISChunkGenerator()).createWorld();
        }
        // add world to config, but disabled by default
        plugin.getConfig().set("worlds." + name, false);
        plugin.saveConfig();
        if (plugin.getPM().isPluginEnabled("My Worlds")) {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds load " + name + ":TARDISChunkGenerator");
            plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds weather always sunny " + name);
            if (plugin.getConfig().getBoolean("creation.keep_night")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds time always night " + name);
            }
            plugin.getServer().dispatchCommand(plugin.getConsole(), "world config save");
        }
    }
}
