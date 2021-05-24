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
package me.eccentric_nz.tardis.api;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISRandomLocation {

	private final TARDISPlugin plugin;

	TARDISRandomLocation(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public Location getLocation() {
		return null;
	}

	final List<World> getWorlds(List<String> list) {
		List<World> worlds = new ArrayList<>();
		list.forEach((s) -> {
			World o = TARDISAliasResolver.getWorldFromAlias(s);
			if (o != null) {
				worlds.add(o);
			}
		});
		return worlds;
	}

	WorldAndRange getWorldAndRange(List<World> worlds) {
		int listLength = worlds.size();
		World w;
		int minX;
		int maxX;
		int minZ;
		int maxZ;
		int rangeX;
		int rangeZ;
		// random world
		w = worlds.get(TARDISConstants.RANDOM.nextInt(listLength));
		World.Environment env = w.getEnvironment();
		// set default by using config values
		int cx = plugin.getConfig().getInt("travel.random_circuit.x");
		int cz = plugin.getConfig().getInt("travel.random_circuit.z");
		minX = -cx;
		maxX = cx;
		minZ = -cz;
		maxZ = cz;
		// get the limits of the world
		// is WorldBorder enabled, and is there a border set for this world?
		if (plugin.getPM().isPluginEnabled("WorldBorder") && Config.Border(w.getName()) != null) {
			BorderData border = Config.Border(w.getName());
			minX = (int) border.getX() - border.getRadiusX();
			maxX = (int) border.getX() + border.getRadiusX();
			minZ = (int) border.getZ() - border.getRadiusZ();
			maxZ = (int) border.getZ() + border.getRadiusZ();
		} else {
			// check vanilla world border
			WorldBorder wb = w.getWorldBorder();
			int size = (int) wb.getSize() / 2;
			Location centre = wb.getCenter();
			if (size < 30000000) {
				minX = centre.getBlockX() - size;
				minZ = centre.getBlockZ() - size;
				maxX = centre.getBlockX() + size;
				maxZ = centre.getBlockZ() + size;
			}
		}
		// compensate for nether 1:8 ratio if necessary
		if (env.equals(World.Environment.NETHER)) {
			minX /= 8;
			maxX /= 8;
			minZ /= 8;
			maxZ /= 8;
		}
		// just set the end values
		if (env.equals(World.Environment.THE_END)) {
			minX = -120;
			maxX = 120;
			minZ = -120;
			maxZ = 120;
		}
		// get ranges
		rangeX = Math.abs(minX) + maxX;
		rangeZ = Math.abs(minZ) + maxZ;
		return new WorldAndRange(w, minX, minZ, rangeX, rangeZ);
	}
}
