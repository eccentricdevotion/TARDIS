/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetVault;
import me.eccentric_nz.TARDIS.enumeration.SmelterChest;
import me.eccentric_nz.TARDIS.rooms.VaultDrop;
import org.bukkit.Location;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class TARDISVaultListener implements Listener {

    private final TARDIS plugin;

    public TARDISVaultListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVaultDropChestClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder(false);
        if (holder instanceof Container container) {
            Location l = container.getLocation();
            if (!plugin.getUtils().inTARDISWorld(l)) {
                return;
            }
            String loc = l.toString();
            // check is drop chest
            ResultSetVault rs = new ResultSetVault(plugin);
            if (!rs.fromLocationAndChestType(loc, SmelterChest.DROP)) {
                return;
            }
            new VaultDrop(plugin).processItems(inv, rs);
        }
    }
}
