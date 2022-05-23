/*
 * Copyright (C) 2022 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;

public class TARDISAliasResolver {

    private static final HashMap<String, TARDISPlanet> planets = new HashMap<>();

    public static String getWorldAlias(World world) {
        return getWorldAlias(world.getName());
    }

    public static String getWorldAlias(String world) {
        return TARDIS.plugin.getPlanetsConfig().getString("planets." + world + ".alias", world);
    }

    public static World getWorldFromAlias(String alias) {
        World world = Bukkit.getServer().getWorld(alias);
        if (world != null) {
            return world;
        } else {
            for (TARDISPlanet planet : planets.values()) {
                if (planet.getAlias().equalsIgnoreCase(alias)) {
                    return planet.getWorld();
                }
            }
        }
        return null;
    }

    public static String getWorldNameFromAlias(String alias) {
        World world = Bukkit.getServer().getWorld(alias);
        if (world != null) {
            return alias;
        } else {
            for (TARDISPlanet planet : planets.values()) {
                if (planet.getAlias().equalsIgnoreCase(alias)) {
                    return planet.getName();
                }
            }
        }
        return "";
    }

    public static void createAliasMap() {
        for (String s : TARDIS.plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
            World world = Bukkit.getServer().getWorld(s);
            if (world != null) {
                String alias = TARDIS.plugin.getPlanetsConfig().getString("planets." + s + ".alias", s);
                TARDISPlanet tp = new TARDISPlanet();
                tp.setAlias(!alias.isEmpty() ? alias : s);
                tp.setName(s);
                tp.setWorld(world);
                if (!alias.isEmpty()) {
                    planets.put(alias, tp);
                } else {
                    planets.put(s, tp);
                }
            }
        }
    }

    public static HashMap<String, TARDISPlanet> getPlanets() {
        return planets;
    }
}
