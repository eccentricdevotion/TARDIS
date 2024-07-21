/*
 * Copyright (C) 2024 eccentric_nz
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISFloorCommand {

    private final TARDIS plugin;

    public TARDISFloorCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setFloorOrWallBlock(Player player, String[] args) {
        String pref = args[0];
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_WALL", pref);
            return false;
        }
        String wall_mat;
        if (args.length > 2) {
            String t = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            wall_mat = t.toUpperCase(Locale.ENGLISH);
        } else {
            wall_mat = args[1].toUpperCase(Locale.ENGLISH);
        }
        if (!TARDISWalls.BLOCKS.contains(Material.getMaterial(wall_mat))) {
            String message = (wall_mat.equals("HELP")) ? "WALL_LIST" : "WALL_NOT_VALID";
            plugin.getMessenger().send(player, TardisModule.TARDIS, message, pref);
            TARDISWalls.BLOCKS.forEach((w) -> player.sendMessage(w.toString()));
            return true;
        }
        HashMap<String, Object> setw = new HashMap<>();
        setw.put(pref, wall_mat);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        TARDIS.plugin.getQueryFactory().doUpdate("player_prefs", setw, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_MAT_SET", TARDISStringUtils.uppercaseFirst(pref));
        return true;
    }
}
