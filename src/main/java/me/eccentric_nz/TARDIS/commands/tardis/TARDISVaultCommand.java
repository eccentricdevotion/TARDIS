/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISVaultCommand {

    private final TARDIS plugin;

    public TARDISVaultCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean addDropChest(Player player) {
        // check permission
        if (!player.hasPermission("tardis.vault")) {
            TARDISMessage.send(player, "UPDATE_NO_PERM", "Vault room drop chest");
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
        if (!b.getType().equals(Material.CHEST) && !b.getType().equals(Material.TRAPPED_CHEST)) {
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
        new QueryFactory(plugin).doInsert("vaults", set);
        TARDISMessage.send(player, "VAULT_SORTER_SET");
        return true;
    }
}
