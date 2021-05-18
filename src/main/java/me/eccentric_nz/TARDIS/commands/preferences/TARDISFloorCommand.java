/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISFloorCommand {

	boolean setFloorOrWallBlock(Player player, String[] args) {
		String pref = args[0];
		if (args.length < 2) {
			TARDISMessage.send(player, "PREF_WALL", pref);
			return false;
		}
		String wall_mat;
		if (args.length > 2) {
			int count = args.length;
			StringBuilder buf = new StringBuilder();
			for (int i = 1; i < count; i++) {
				buf.append(args[i]).append("_");
			}
			String tmp = buf.toString();
			String t = tmp.substring(0, tmp.length() - 1);
			wall_mat = t.toUpperCase(Locale.ENGLISH);
		} else {
			wall_mat = args[1].toUpperCase(Locale.ENGLISH);
		}
		if (!TARDISWalls.BLOCKS.contains(Material.getMaterial(wall_mat))) {
			String message = (wall_mat.equals("HELP")) ? "WALL_LIST" : "WALL_NOT_VALID";
			TARDISMessage.send(player, message, pref);
			TARDISWalls.BLOCKS.forEach((w) -> player.sendMessage(w.toString()));
			return true;
		}
		HashMap<String, Object> setw = new HashMap<>();
		setw.put(pref, wall_mat);
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", player.getUniqueId().toString());
		TARDIS.plugin.getQueryFactory().doUpdate("player_prefs", setw, where);
		TARDISMessage.send(player, "PREF_MAT_SET", TARDISStringUtils.uppercaseFirst(pref));
		return true;
	}
}
