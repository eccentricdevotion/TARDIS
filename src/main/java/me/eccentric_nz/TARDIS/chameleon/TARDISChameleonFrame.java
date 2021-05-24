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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.enumeration.Control;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TARDISChameleonFrame {

	private final TARDISPlugin plugin;

	public TARDISChameleonFrame(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public void updateChameleonFrame(int id, PRESET preset) {
		// is there a Chameleon frame record for this tardis?
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		where.put("type", Control.FRAME.getId());
		ResultSetControls rsc = new ResultSetControls(plugin, where, false);
		if (rsc.resultSet()) {
			// get location of Chameleon frame
			Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
			if (location != null) {
				for (Entity e : location.getChunk().getEntities()) {
					if (e instanceof ItemFrame) {
						if (compareLocations(e.getLocation(), location)) {
							ItemFrame frame = (ItemFrame) e;
							ItemStack is = new ItemStack(preset.getGuiDisplay());
							ItemMeta im = is.getItemMeta();
							im.setDisplayName(preset.toString());
							is.setItemMeta(im);
							frame.setItem(is, true);
							break;
						}
					}
				}
			}
		}
	}

	private boolean compareLocations(Location one, Location two) {
		return one.getBlockX() == two.getBlockX() && one.getBlockY() == two.getBlockY() && one.getBlockZ() == two.getBlockZ();
	}
}
