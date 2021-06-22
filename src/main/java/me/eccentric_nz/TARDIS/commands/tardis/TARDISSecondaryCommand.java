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
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisSecondaryCommand {

    private final TardisPlugin plugin;

    TardisSecondaryCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean startSecondary(Player player, String[] args) {
        if (TardisPermission.hasPermission(player, "tardis.update")) {
            if (args.length < 2) {
                TardisMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            UUID uuid = player.getUniqueId();
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUUID(uuid.toString())) {
                TardisMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            if (args[1].equalsIgnoreCase("remove")) {
                plugin.getTrackerKeeper().getSecondaryRemovers().put(player.getUniqueId(), rs.getTardisId());
                TardisMessage.send(player, "SEC_REMOVE_CLICK_BLOCK");
                return true;
            }
            Updateable updateable;
            try {
                updateable = Updateable.valueOf(TardisStringUtils.toScoredUppercase(args[1]));
            } catch (IllegalArgumentException e) {
                TardisMessage.send(player, "UPDATE_NOT_VALID");
                return false;
            }
            if (!updateable.isSecondary()) {
                TardisMessage.send(player, "UPDATE_NOT_VALID");
                return false;
            }
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TardisMessage.send(player, "NOT_IN_TARDIS");
                return false;
            }
            plugin.getTrackerKeeper().getSecondary().put(uuid, updateable);
            TardisMessage.send(player, "UPDATE_CLICK", updateable.getName());
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
