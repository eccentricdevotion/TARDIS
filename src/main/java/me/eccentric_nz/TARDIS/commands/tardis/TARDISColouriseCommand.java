/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * As with the rest of the Doctor's TARDIS, the aesthetic design of the time rotor occasionally changed throughout the
 * Doctor's travels. As it varied through designs, it alternated between being a single column and a series of
 * components that moved into each other from above and below.
 *
 * @author eccentric_nz
 */
class TARDISColouriseCommand {

    private final TARDIS plugin;

    TARDISColouriseCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean updateBeaconGlass(Player player) {
        if (!TARDISPermission.hasPermission(player, "tardis.upgrade")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        // check they are still in the TARDIS world
        if (!plugin.getUtils().inTARDISWorld(player)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_IN_WORLD");
            return true;
        }
        // must have a TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return true;
        }
        Tardis tardis = rs.getTardis();
        Schematic console = tardis.getSchematic();
        if (!console.hasBeacon()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "COLOUR_NOT_VALID");
            return true;
        }
        if (console.mustUseSonic()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "COLOUR_SONIC");
            return true;
        }
        int ownerid = tardis.getTardisId();
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
            return true;
        }
        int thisid = rst.getTardis_id();
        // must be timelord of the TARDIS
        if (thisid != ownerid) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_ONLY_TL");
            return true;
        }
        // track the player for 60 seconds
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getBeaconColouring().add(uuid);
        // message player
        plugin.getMessenger().send(player, TardisModule.TARDIS, "COLOUR_TIME");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getBeaconColouring().remove(uuid), 1200L);
        return true;
    }
}
