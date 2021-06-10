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
package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetBlueprint;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

class TARDISRevokeCommand {

    private final TARDISPlugin plugin;

    TARDISRevokeCommand(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    boolean removePermission(CommandSender sender, String[] args) {
        // tardisadmin revoke [player] [permission]
        if (args.length < 3) {
            TARDISMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        Player player = plugin.getServer().getPlayer(args[1]);
        if (player == null) {
            TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
            return true;
        }
        int id = new ResultSetBlueprint(plugin).getRecordId(player.getUniqueId().toString(), args[2].toLowerCase());
        if (id != -1) {
            // delete record
            HashMap<String, Object> where = new HashMap<>();
            where.put("bp_id", id);
            plugin.getQueryFactory().doDelete("blueprint", where);
            TARDISMessage.send(sender, "BLUEPRINT_REVOKED");
        } else {
            TARDISMessage.send(sender, "BLUEPRINT_NOT_FOUND");
        }
        return true;
    }
}
