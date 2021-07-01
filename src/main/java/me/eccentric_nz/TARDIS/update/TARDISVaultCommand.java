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
package me.eccentric_nz.TARDIS.update;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetVault;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISVaultCommand {

    private final TARDIS plugin;

    TARDISVaultCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean addDropChest(Player player, int id, Block b) {
        // player is in their own TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (rst.resultSet() && rst.getTardis_id() != id) {
            TARDISMessage.send(player, "CMD_ONLY_TL");
            return true;
        }
        Location l = b.getLocation();
        // determine the min x, y, z coords
        int mx = l.getBlockX() % 16;
        if (mx < 0) {
            mx += 16;
        }
        int mz = l.getBlockZ() % 16;
        if (mz < 0) {
            mz += 16;
        }
        int x = l.getBlockX() - mx;
        int y = l.getBlockY() - (l.getBlockY() % 16);
        int z = l.getBlockZ() - mz;
        String pos = l.toString();
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", id);
        set.put("location", pos);
        set.put("x", x);
        set.put("y", y);
        set.put("z", z);
        // is there an existing drop chest record?
        ResultSetVault rsv = new ResultSetVault(plugin, id);
        if (rsv.resultSet()) {
            HashMap<String, Object> whereVault = new HashMap<>();
            whereVault.put("v_id", rsv.getVault_id());
            plugin.getQueryFactory().doUpdate("vaults", set, whereVault);
        } else {
            plugin.getQueryFactory().doInsert("vaults", set);
        }
        return true;
    }
}
