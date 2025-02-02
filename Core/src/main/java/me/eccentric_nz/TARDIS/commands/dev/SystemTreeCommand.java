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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSystemUpgrades;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.TARDISSystemTreeGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SystemTreeCommand {

    private final TARDIS plugin;

    public SystemTreeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player) {
        String uuid = player.getUniqueId().toString();
        // get TARDIS player is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            return true;
        }
        int id = rst.getTardis_id();
        // must be the owner of the TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        wheret.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_OWNER");
            return true;
        }
        // get player's artron energy level
        ResultSetSystemUpgrades rsp = new ResultSetSystemUpgrades(plugin, id, uuid);
        if (!rsp.resultset()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_TRAVEL_FIRST");
            return true;
        }
        SystemUpgrade current = rsp.getData();
        ItemStack[] menu = new TARDISSystemTreeGUI(plugin, current).getInventory();
        Inventory upgrades = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS System Upgrades");
        upgrades.setContents(menu);
        player.openInventory(upgrades);
        return true;
    }
}
