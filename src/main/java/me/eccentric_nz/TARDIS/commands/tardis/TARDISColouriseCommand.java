/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 * As with the rest of the Doctor's TARDIS, the aesthetic design of the time
 * rotor occasionally changed throughout the Doctor's travels. As it varied
 * through designs, it alternated between being a single column and a series of
 * components that moved into each other from above and below.
 *
 * @author eccentric_nz
 */
public class TARDISColouriseCommand {

    private final TARDIS plugin;

    public TARDISColouriseCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean updateBeaconGlass(Player player) {
        if (!player.hasPermission("tardis.upgrade")) {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
        // check they are still in the TARDIS world
        if (!plugin.getUtils().inTARDISWorld(player)) {
            TARDISMessage.send(player, "CMD_IN_WORLD");
            return true;
        }
        // must have a TARDIS
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        Tardis tardis = rs.getTardis();
        SCHEMATIC console = tardis.getSchematic();
        if (!console.hasBeacon()) {
            TARDISMessage.send(player, "COLOUR_NOT_VALID");
            return true;
        }
        if (console.mustUseSonic()) {
            TARDISMessage.send(player, "COLOUR_SONIC");
            return true;
        }
        int ownerid = tardis.getTardis_id();
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TARDISMessage.send(player, "NOT_IN_TARDIS");
            return true;
        }
        int thisid = rst.getTardis_id();
        // must be timelord of the TARDIS
        if (thisid != ownerid) {
            TARDISMessage.send(player, "CMD_ONLY_TL");
            return true;
        }
        // track the player
        plugin.getTrackerKeeper().getBeaconColouring().add(player.getUniqueId());
        return true;
    }
}
