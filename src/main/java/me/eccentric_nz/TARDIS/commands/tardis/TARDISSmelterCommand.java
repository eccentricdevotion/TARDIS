/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetSmelterCheck;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.UPDATEABLE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISSmelterCommand {

    private final TARDIS plugin;

    TARDISSmelterCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean addDropChest(Player player, UPDATEABLE updateable) {
        // check permission
        if (!TARDISPermission.hasPermission(player, "tardis.room.smelter")) {
            TARDISMessage.send(player, "UPDATE_NO_PERM", "Smelter room drop chest");
            return true;
        }
        // player is a Time Lord
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
            return false;
        }
        int id = rs.getTardis_id();
        // get chest location
        Block b = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
        if (!updateable.getMaterialChoice().getChoices().contains(b.getType())) {
            TARDISMessage.send(player, "UPDATE_CONDENSER");
            return true;
        }
        // player is in TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TARDISMessage.send(player, "NOT_IN_TARDIS");
            return false;
        }
        int thisid = rst.getTardis_id();
        if (thisid != id) {
            TARDISMessage.send(player, "CMD_ONLY_TL");
            return false;
        }
        if (!plugin.getUtils().inTARDISWorld(player)) {
            TARDISMessage.send(player, "UPDATE_IN_WORLD");
            return true;
        }
        Location l = b.getLocation();
        String pos = l.toString();
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        set.put("location", pos);
        set.put("chest_type", updateable.toString());
        set.put("x", 0);
        set.put("y", 0);
        set.put("z", 0);
        // is there an existing DROP record?
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("tardis_id", id);
        wheres.put("chest_type", "DROP");
        ResultSetSmelterCheck rssc = new ResultSetSmelterCheck(plugin, wheres);
        if (rssc.resultSet()) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("v_id", rssc.getSmelter_id());
            plugin.getQueryFactory().doUpdate("vaults", set, where);
        } else {
            // check if FUEL or SMELT exists
            HashMap<String, Object> wherefs = new HashMap<>();
            wherefs.put("tardis_id", id);
            wherefs.put("chest_type", updateable.toString());
            ResultSetSmelterCheck rsfs = new ResultSetSmelterCheck(plugin, wherefs);
            if (rsfs.resultSet()) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("v_id", rsfs.getSmelter_id());
                plugin.getQueryFactory().doUpdate("vaults", set, where);
            } else {
                plugin.getQueryFactory().doInsert("vaults", set);
            }
        }
        TARDISMessage.send(player, "SMELTER_SET", updateable.toString(), (updateable.equals(UPDATEABLE.FUEL)) ? "SMELT" : "FUEL");
        return true;
    }
}
