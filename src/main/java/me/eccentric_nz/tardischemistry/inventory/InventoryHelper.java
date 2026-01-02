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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischemistry.inventory;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischemistry.compound.CompoundInventory;
import me.eccentric_nz.tardischemistry.lab.LabInventory;
import me.eccentric_nz.tardischemistry.product.ProductInventory;
import me.eccentric_nz.tardischemistry.reducer.ReducerInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryHelper implements Listener {

    private final TARDIS plugin;

    public InventoryHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChemistryInventoryClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        InventoryType type = view.getTopInventory().getType();
        if (type == InventoryType.CHEST) {
            InventoryHolder holder = event.getInventory().getHolder(false);
            if (holder instanceof CompoundInventory || holder instanceof LabInventory ||
                    holder instanceof ProductInventory || holder instanceof ReducerInventory) {
                Player player = (Player) event.getPlayer();
                List<ItemStack> leftovers = new ArrayList<>();
                for (ItemStack is : view.getTopInventory().getContents()) {
                    if (is != null && !is.getType().equals(Material.BOWL)) {
                        leftovers.add(is);
                    }
                }
                if (!leftovers.isEmpty()) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        HashMap<Integer, ItemStack> notadded = player.getInventory().addItem(leftovers.toArray(new ItemStack[0]));
                        if (!notadded.isEmpty()) {
                            Location location = player.getLocation();
                            for (ItemStack is : notadded.values()) {
                                location.getWorld().dropItemNaturally(location, is);
                            }
                        }
                    }, 1L);
                }
            }
        }
    }
}
