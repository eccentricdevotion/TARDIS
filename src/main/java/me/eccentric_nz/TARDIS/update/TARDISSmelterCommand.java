/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.update;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSmelterCheck;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISSmelterCommand {

    private final TARDIS plugin;

    TARDISSmelterCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean addDropChest(Player player, Updateable updateable, int id, Block b) {
        // player is in their own TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (rst.resultSet() && rst.getTardis_id() != id) {
            TARDISMessage.send(player, "CMD_ONLY_TL");
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
            HashMap<String, Object> whereSmeltCheck = new HashMap<>();
            whereSmeltCheck.put("v_id", rssc.getSmelter_id());
            plugin.getQueryFactory().doUpdate("vaults", set, whereSmeltCheck);
        } else {
            // check if FUEL or SMELT exists
            HashMap<String, Object> whereFuelSmelt = new HashMap<>();
            whereFuelSmelt.put("tardis_id", id);
            whereFuelSmelt.put("chest_type", updateable.toString());
            ResultSetSmelterCheck rsfs = new ResultSetSmelterCheck(plugin, whereFuelSmelt);
            if (rsfs.resultSet()) {
                HashMap<String, Object> whereVault = new HashMap<>();
                whereVault.put("v_id", rsfs.getSmelter_id());
                plugin.getQueryFactory().doUpdate("vaults", set, whereVault);
            } else {
                plugin.getQueryFactory().doInsert("vaults", set);
            }
        }
        TARDISMessage.send(player, "SMELTER_SET", updateable.toString(), (updateable.equals(Updateable.FUEL)) ? "SMELT" : "FUEL");
        return true;
    }
}
