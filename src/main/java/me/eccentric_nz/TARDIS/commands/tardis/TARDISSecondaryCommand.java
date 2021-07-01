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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISSecondaryCommand {

    private final TARDIS plugin;

    TARDISSecondaryCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean startSecondary(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.update")) {
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            UUID uuid = player.getUniqueId();
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(uuid.toString())) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            if (args[1].equalsIgnoreCase("remove")) {
                plugin.getTrackerKeeper().getSecondaryRemovers().put(player.getUniqueId(), rs.getTardis_id());
                TARDISMessage.send(player, "SEC_REMOVE_CLICK_BLOCK");
                return true;
            }
            Updateable updateable;
            try {
                updateable = Updateable.valueOf(TARDISStringUtils.toScoredUppercase(args[1]));
            } catch (IllegalArgumentException e) {
                TARDISMessage.send(player, "UPDATE_NOT_VALID");
                return false;
            }
            if (!updateable.isSecondary()) {
                TARDISMessage.send(player, "UPDATE_NOT_VALID");
                return false;
            }
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TARDISMessage.send(player, "NOT_IN_TARDIS");
                return false;
            }
            plugin.getTrackerKeeper().getSecondary().put(uuid, updateable);
            TARDISMessage.send(player, "UPDATE_CLICK", updateable.getName());
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
