/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Locale;

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
            TARDISMessage.send(sender, "PREF_WALL", pref);
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
        if (!TARDISWalls.BLOCKS.contains(Material.valueOf(wall_mat))) {
            String message = (wall_mat.equals("HELP")) ? "WALL_LIST" : "WALL_NOT_VALID";
            TARDISMessage.send(sender, message, pref);
            TARDISWalls.BLOCKS.forEach((w) -> sender.sendMessage(w.toString()));
            return true;
        }
        QueryFactory qf = new QueryFactory(plugin);
        // check if player_prefs record
        HashMap<String, Object> wherepp = new HashMap<>();
        wherepp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        HashMap<String, Object> setpp = new HashMap<>();
        if (!rsp.resultSet()) {
            String otherpref = (pref.equals("floor")) ? "wall" : "floor";
            String otherset = (pref.equals("floor")) ? "ORANGE_WOOL" : "GREY_WOOL";
            // create a player_prefs record
            setpp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            setpp.put("player", "junk");
            setpp.put(pref, wall_mat);
            setpp.put(otherpref, otherset);
            qf.doInsert("player_prefs", setpp);
        } else {
            setpp.put(pref, wall_mat);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            qf.doUpdate("player_prefs", setpp, where);
        }
        TARDISMessage.send(sender, "PREF_MAT_SET", TARDISPrefsCommands.ucfirst(pref));
        return true;
    }
}
