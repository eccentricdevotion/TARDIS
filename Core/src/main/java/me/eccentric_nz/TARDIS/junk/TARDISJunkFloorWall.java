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
package me.eccentric_nz.TARDIS.junk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISJunkFloorWall {

    private final TARDIS plugin;

    TARDISJunkFloorWall(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setJunkWallOrFloor(CommandSender sender, String[] args) {
        String pref = args[0].toLowerCase(Locale.ENGLISH);
        // check args
        if (args.length < 2) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PREF_WALL", pref);
            return false;
        }
        String wall_mat;
        if (args.length > 2) {
            String t = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            wall_mat = t.toUpperCase(Locale.ENGLISH);
        } else {
            wall_mat = args[1].toUpperCase(Locale.ENGLISH);
        }
        if (!TARDISWalls.BLOCKS.contains(Material.valueOf(wall_mat))) {
            String message = (wall_mat.equals("HELP")) ? "WALL_LIST" : "WALL_NOT_VALID";
            plugin.getMessenger().send(sender, TardisModule.TARDIS, message, pref);
            TARDISWalls.BLOCKS.forEach((w) -> sender.sendMessage(w.toString()));
            return true;
        }
        // check if player_prefs record
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, "00000000-aaaa-bbbb-cccc-000000000000");
        HashMap<String, Object> setpp = new HashMap<>();
        if (!rsp.resultSet()) {
            String otherpref = (pref.equals("floor")) ? "wall" : "floor";
            String otherset = (pref.equals("floor")) ? "ORANGE_WOOL" : "LIGHT_GRAY_WOOL";
            // create a player_prefs record
            setpp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            setpp.put("player", "junk");
            setpp.put(pref, wall_mat);
            setpp.put(otherpref, otherset);
            plugin.getQueryFactory().doInsert("player_prefs", setpp);
        } else {
            setpp.put(pref, wall_mat);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            plugin.getQueryFactory().doUpdate("player_prefs", setpp, where);
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "PREF_MAT_SET", TARDISStringUtils.uppercaseFirst(pref));
        return true;
    }
}
