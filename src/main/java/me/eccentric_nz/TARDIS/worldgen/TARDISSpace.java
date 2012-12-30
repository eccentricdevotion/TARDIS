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
package me.eccentric_nz.TARDIS.worldgen;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

/**
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
            tardisWorld = WorldCreator.name(name).type(WorldType.LARGE_BIOMES).environment(World.Environment.THE_END).generator(new TARDISChunkGenerator()).createWorld();
            if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
                plugin.getServer().dispatchCommand(plugin.console, "mv import " + name + " the_end -g TARDIS");
            }
            if (plugin.pm.isPluginEnabled("WorldBorder")) {
                // wb <world> set <radius> <x> <z>
                plugin.getServer().dispatchCommand(plugin.console, "wb " + name + " set " + plugin.getConfig().getInt("border_radius") + " 0 0");
            }
        }
        return tardisWorld;
    }
}