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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.rooms.TardisWalls;
import me.eccentric_nz.tardis.utility.TardisStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TardisFloorCommand {

    boolean setFloorOrWallBlock(Player player, String[] args) {
        String pref = args[0];
        if (args.length < 2) {
            TardisMessage.send(player, "PREF_WALL", pref);
            return false;
        }
        String wall_mat;
        if (args.length > 2) {
            String t = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            wall_mat = t.toUpperCase(Locale.ENGLISH);
        } else {
            wall_mat = args[1].toUpperCase(Locale.ENGLISH);
        }
        if (!TardisWalls.BLOCKS.contains(Material.getMaterial(wall_mat))) {
            String message = (wall_mat.equals("HELP")) ? "WALL_LIST" : "WALL_NOT_VALID";
            TardisMessage.send(player, message, pref);
            TardisWalls.BLOCKS.forEach((w) -> player.sendMessage(w.toString()));
            return true;
        }
        HashMap<String, Object> setw = new HashMap<>();
        setw.put(pref, wall_mat);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        TardisPlugin.plugin.getQueryFactory().doUpdate("player_prefs", setw, where);
        TardisMessage.send(player, "PREF_MAT_SET", TardisStringUtils.uppercaseFirst(pref));
        return true;
    }
}