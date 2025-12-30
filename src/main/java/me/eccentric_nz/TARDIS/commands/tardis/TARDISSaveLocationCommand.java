/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.regex.Pattern;

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
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
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
                plugin.getMessenger().sendColouredCommand(player, "SAVE_RESERVED", "/tardis home", plugin);
                return false;
            } else {
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardisId();
                // check for memory circuit
                TARDISCircuitChecker tcc = null;
                if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, false)) {
                    tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasMemory()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MEM_CIRCUIT");
                    return true;
                }
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
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                if (!rsc.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                    return true;
                }
                Current current = rsc.getCurrent();
                String w = current.location().getWorld().getName();
                if (w.startsWith("TARDIS_")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NO_TARDIS");
                    return true;
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", id);
                set.put("dest_name", args[1]);
                set.put("world", w);
                set.put("x", current.location().getBlockX());
                set.put("y", current.location().getBlockY());
                set.put("z", current.location().getBlockZ());
                set.put("direction", current.direction().toString());
                set.put("submarine", (current.submarine()) ? 1 : 0);
                if (args.length > 2 && args[2].equalsIgnoreCase("true")) {
                    set.put("preset", tardis.getPreset().toString());
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
