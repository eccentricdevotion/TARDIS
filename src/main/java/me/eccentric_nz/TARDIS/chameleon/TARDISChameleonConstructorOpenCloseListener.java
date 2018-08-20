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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author eccentric_nz
 */
public final class TARDISChameleonConstructorOpenCloseListener implements Listener {

    private final TARDIS plugin;

    public TARDISChameleonConstructorOpenCloseListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChameleonConstructorOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        String inv_name = inv.getTitle();
        if (inv_name.equals(ChatColor.DARK_RED + "Chameleon Construction")) {
            Player player = ((Player) event.getPlayer());
            plugin.getTrackerKeeper().getConstructors().add(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChameleonConstructorClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        String inv_name = inv.getTitle();
        if (inv_name.equals(ChatColor.DARK_RED + "Chameleon Construction")) {
            Player player = ((Player) event.getPlayer());
            if (plugin.getTrackerKeeper().getConstructors().contains(player.getUniqueId())) {
                // abort
                // drop any user placed items in the inventory
                for (int s = 18; s < 54; s++) {
                    if (s != 26 && s != 43 && s != 52) {
                        ItemStack userStack = inv.getItem(s);
                        if (userStack != null) {
                            player.getWorld().dropItemNaturally(player.getLocation(), userStack);
                        }
                    }
                }
            }
        }
    }
}
