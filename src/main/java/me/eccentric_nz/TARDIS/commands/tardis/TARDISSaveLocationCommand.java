/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.regex.Pattern;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISSaveLocationCommand {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_]{2,16}");
    private final TARDIS plugin;

    TARDISSaveLocationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doSave(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.save")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return false;
            }
            if (args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return false;
            }
            if (!LETTERS_NUMBERS.matcher(args[1]).matches()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NAME_NOT_VALID");
                return false;
            } else if (args[1].equalsIgnoreCase("hide") || args[1].equalsIgnoreCase("rebuild") || args[1].equalsIgnoreCase("home")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_RESERVED");
                return false;
            } else {
                int id = rs.getTardis().getTardis_id();
                // check has unique name
                HashMap<String, Object> wherename = new HashMap<>();
                wherename.put("tardis_id", id);
                wherename.put("dest_name", args[1]);
                wherename.put("type", 0);
                ResultSetDestinations rsd = new ResultSetDestinations(plugin, wherename, false);
                if (rsd.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_EXISTS");
                    return true;
                }
                // get current destination
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                    return true;
                }
                String w = rsc.getWorld().getName();
                if (w.startsWith("TARDIS_")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NO_TARDIS");
                    return true;
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", id);
                set.put("dest_name", args[1]);
                set.put("world", w);
                set.put("x", rsc.getX());
                set.put("y", rsc.getY());
                set.put("z", rsc.getZ());
                set.put("direction", rsc.getDirection().toString());
                set.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
                if (args.length > 2 && args[2].equalsIgnoreCase("true")) {
                    set.put("preset", rs.getTardis().getPreset().toString());
                }
                if (plugin.getQueryFactory().doSyncInsert("destinations", set) < 0) {
                    return false;
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_SET", args[1]);
                    return true;
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
