/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders;

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.World;
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
     * Gets THE_END world for the specified TARDIS. If the world does not exist,
     * it is created.
     *
     * @param name the name of this world
     */
    public World getTardisWorld(String name) {
        if (tardisWorld == null) {
            tardisWorld = WorldCreator.name(name).type(WorldType.LARGE_BIOMES).environment(World.Environment.NORMAL).generator(new TARDISChunkGenerator()).createWorld();
            // set the time to night
            tardisWorld.setTime(14000L);
            // add world to config, but disabled by default
            plugin.getConfig().set("worlds:" + name, false);
            plugin.saveConfig();
            if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
                plugin.getServer().dispatchCommand(plugin.console, "mv import " + name + " normal -g TARDISChunkGenerator");
                plugin.getServer().dispatchCommand(plugin.console, "mv modify set animals false " + name);
                plugin.getServer().dispatchCommand(plugin.console, "mv modify set monsters false " + name);
                plugin.getServer().dispatchCommand(plugin.console, "mv modify set hidden true " + name);
            }
            if (plugin.pm.isPluginEnabled("WorldBorder")) {
                // wb <world> set <radius> <x> <z>
                plugin.getServer().dispatchCommand(plugin.console, "wb " + name + " set " + plugin.getConfig().getInt("border_radius") + " 0 0");
            }
        }
        return tardisWorld;
    }

    public void keepNight() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                timechk();
            }
        }, 60L, 1200L);
    }

    private void timechk() {
        List<World> serverWorlds = plugin.getServer().getWorlds();
        for (World w : serverWorlds) {
            if (w.getName().contains("TARDIS_WORLD_")) {
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
}