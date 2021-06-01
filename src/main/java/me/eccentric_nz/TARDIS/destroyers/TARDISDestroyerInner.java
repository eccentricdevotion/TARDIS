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
package me.eccentric_nz.tardis.destroyers;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.builders.TARDISInteriorPostioning;
import me.eccentric_nz.tardis.builders.TARDISTIPSData;
import me.eccentric_nz.tardis.enumeration.Schematic;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collections;
import java.util.HashMap;

/**
 * Destroys the inner tardis.
 * <p>
 * If a tardis landed in the same space and time as another tardis, a time ram could occur, destroying both TARDISes,
 * their occupants and even cause a black hole that would tear a hole in the universe
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerInner {

	private final TARDISPlugin plugin;

	public TARDISDestroyerInner(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Destroys the inside of the tardis.
	 *
	 * @param schematic the name of the schematic file to use can be DEFAULT, BIGGER or DELUXE.
	 * @param id        the unique key of the record for this tardis in the database.
	 * @param w         the world where the tardis is to be built.
	 * @param slot      the TIPS slot number
	 */
	public void destroyInner(Schematic schematic, int id, World w, int slot) {
		// destroy tardis
		if (!plugin.getConfig().getBoolean("creation.create_worlds") &&
			!plugin.getConfig().getBoolean("creation.default_world")) {
			plugin.debug(TARDISPlugin.plugin.getLanguage().getString("CONFIG_CREATION_WORLD"));
			return;
		}
		Location wgl;
		TARDISInteriorPostioning tips = new TARDISInteriorPostioning(plugin);
		TARDISTIPSData coords;
		if (schematic.getPermission().equals("junk")) {
			coords = tips.getTIPSJunkData();
		} else {
			coords = tips.getTIPSData(slot);
		}
		tips.reclaimChunks(w, id);
		wgl = new Location(w, coords.getMinX(), 64, coords.getMinZ());
		// remove blocks saved to blocks table (iron/gold/diamond/emerald)
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		plugin.getQueryFactory().doDelete("blocks", where);
		// remove from protectBlockMap - remove(id) would only remove the first one
		plugin.getGeneralKeeper().getProtectBlockMap().values().removeAll(Collections.singleton(id));
		if (plugin.isWorldGuardOnServer()) {
			plugin.getWorldGuardUtils().removeRegion(wgl);
		}
	}
}
