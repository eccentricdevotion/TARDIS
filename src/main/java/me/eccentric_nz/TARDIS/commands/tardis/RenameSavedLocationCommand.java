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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author eccentric_nz
 */
public class RenameSavedLocationCommand {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_]{2,16}");
    private final TARDIS plugin;

    public RenameSavedLocationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void doRenameSave(Player player, String oldName, String newName) {
        if (TARDISPermission.hasPermission(player, "tardis.save")) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return;
            }
            int id = rs.getTardisId();
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("dest_name", oldName);
            whered.put("tardis_id", id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                plugin.getMessenger().sendColouredCommand(player, "SAVE_NOT_FOUND", "/tardis list saves", plugin);
                return;
            }
            if (!LETTERS_NUMBERS.matcher(newName).matches()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NAME_NOT_VALID");
            } else if (newName.equalsIgnoreCase("hide") || oldName.equalsIgnoreCase("rebuild") || oldName.equalsIgnoreCase("home")) {
                plugin.getMessenger().sendColouredCommand(player, "SAVE_RESERVED", "/tardis home", plugin);
            } else {
                int destID = rsd.getDest_id();
                HashMap<String, Object> did = new HashMap<>();
                did.put("dest_id", destID);
                HashMap<String, Object> set = new HashMap<>();
                set.put("dest_name", newName);
                plugin.getQueryFactory().doUpdate("destinations", set, did);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_RENAMED", newName);
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
        }
    }
}
