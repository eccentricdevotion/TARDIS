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
package me.eccentric_nz.tardis.planets;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Objects;

public class TardisAliasResolver {

    private static final HashMap<String, TardisPlanet> PLANETS = new HashMap<>();

    public static String getWorldAlias(World world) {
        return getWorldAlias(world.getName());
    }

    public static String getWorldAlias(String world) {
        return TardisPlugin.plugin.getPlanetsConfig().getString("planets." + world + ".alias", world);
    }

    public static World getWorldFromAlias(String alias) {
        World world = Bukkit.getServer().getWorld(alias);
        if (world != null) {
            return world;
        } else {
            for (TardisPlanet planet : PLANETS.values()) {
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
            for (TardisPlanet planet : PLANETS.values()) {
                if (planet.getAlias().equalsIgnoreCase(alias)) {
                    return planet.getName();
                }
            }
        }
        return "";
    }

    public static void createAliasMap() {
        for (String s : Objects.requireNonNull(TardisPlugin.plugin.getPlanetsConfig().getConfigurationSection("planets")).getKeys(false)) {
            World world = Bukkit.getServer().getWorld(s);
            if (world != null) {
                String alias = TardisPlugin.plugin.getPlanetsConfig().getString("planets." + s + ".alias", s);
                TardisPlanet tp = new TardisPlanet();
                tp.setAlias(!alias.isEmpty() ? alias : s);
                tp.setName(s);
                tp.setWorld(world);
                if (!alias.isEmpty()) {
                    PLANETS.put(alias, tp);
                } else {
                    PLANETS.put(s, tp);
                }
            }
        }
    }

    public static HashMap<String, TardisPlanet> getPlanets() {
        return PLANETS;
    }
}
