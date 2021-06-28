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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetDestinations;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TardisReorderSavedLocationCommand {

    private final TardisPlugin plugin;

    TardisReorderSavedLocationCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean doReorderSave(Player player, String[] args) {
        if (TardisPermission.hasPermission(player, "tardis.save")) {
            if (args.length < 3) {
                TardisMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUuid(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NO_TARDIS");
                return false;
            }
            int id = rs.getTardisId();
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("dest_name", args[1]);
            whered.put("tardis_id", id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                TardisMessage.send(player, "SAVE_NOT_FOUND");
                return false;
            }
            if (args[1].equalsIgnoreCase("home")) {
                TardisMessage.send(player, "SAVE_REORDER");
                return false;
            }
            if (!NumberUtils.isNumber(args[2])) {
                TardisMessage.send(player, "ARG_LAST_NUMBER");
                return false;
            }
            int slot = TardisNumberParsers.parseInt(args[2]);
            // check slot is not occupied
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("tardis_id", id);
            wheres.put("slot", slot);
            ResultSetDestinations rss = new ResultSetDestinations(plugin, wheres, false);
            if (rss.resultSet()) {
                TardisMessage.send(player, "DEST_SLOT", rss.getDestName());
                return true;
            }
            int destId = rsd.getDestId();
            HashMap<String, Object> did = new HashMap<>();
            did.put("dest_id", destId);
            HashMap<String, Object> set = new HashMap<>();
            set.put("slot", slot);
            plugin.getQueryFactory().doUpdate("destinations", set, did);
            TardisMessage.send(player, "DEST_REORDERED", args[2]);
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
