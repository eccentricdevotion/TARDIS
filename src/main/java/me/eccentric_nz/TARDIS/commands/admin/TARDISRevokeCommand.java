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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlueprint;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

class TARDISRevokeCommand {

    private final TARDIS plugin;

    TARDISRevokeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean removePermission(CommandSender sender, String[] args) {
        // tardisadmin revoke [player] [permission]
        if (args.length < 3) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        Player player = plugin.getServer().getPlayer(args[1]);
        if (player == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return true;
        }
        int id = new ResultSetBlueprint(plugin).getRecordId(player.getUniqueId().toString(), args[2].toLowerCase(Locale.ROOT));
        if (id != -1) {
            // delete record
            HashMap<String, Object> where = new HashMap<>();
            where.put("bp_id", id);
            plugin.getQueryFactory().doDelete("blueprint", where);
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "BLUEPRINT_REVOKED");
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "BLUEPRINT_NOT_FOUND");
        }
        return true;
    }
}
