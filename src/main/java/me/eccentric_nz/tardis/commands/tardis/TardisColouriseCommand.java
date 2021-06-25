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
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * As with the rest of the Doctor's tardis, the aesthetic design of the time rotor occasionally changed throughout the
 * Doctor's travels. As it varied through designs, it alternated between being a single column and a series of
 * components that moved into each other from above and below.
 *
 * @author eccentric_nz
 */
class TardisColouriseCommand {

    private final TardisPlugin plugin;

    TardisColouriseCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    void updateBeaconGlass(Player player) {
        if (!TardisPermission.hasPermission(player, "tardis.upgrade")) {
            TardisMessage.send(player, "NO_PERMS");
            return;
        }
        // check they are still in the tardis world
        if (!plugin.getUtils().inTardisWorld(player)) {
            TardisMessage.send(player, "CMD_IN_WORLD");
            return;
        }
        // must have a tardis
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TardisMessage.send(player, "NOT_A_TIMELORD");
            return;
        }
        Tardis tardis = rs.getTardis();
        Schematic console = tardis.getSchematic();
        if (!console.hasBeacon()) {
            TardisMessage.send(player, "COLOUR_NOT_VALID");
            return;
        }
        if (console.mustUseSonic()) {
            TardisMessage.send(player, "COLOUR_SONIC");
            return;
        }
        int ownerid = tardis.getTardisId();
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TardisMessage.send(player, "NOT_IN_TARDIS");
            return;
        }
        int thisid = rst.getTardisId();
        // must be timelord of the tardis
        if (thisid != ownerid) {
            TardisMessage.send(player, "CMD_ONLY_TL");
            return;
        }
        // track the player for 60 seconds
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getBeaconColouring().add(uuid);
        // message player
        TardisMessage.send(player, "COLOUR_TIME");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getBeaconColouring().remove(uuid), 1200L);
    }
}
