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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetVault;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicSorterListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TARDISVaultListener implements Listener {

    private final TARDIS plugin;

    public TARDISVaultListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)

    public void onVaultDropChestClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            Location l = chest.getLocation();
            if (plugin.getUtils().inTARDISWorld(l)) {
                String loc = l.toString();
                // check is drop chest
                ResultSetVault rs = new ResultSetVault(plugin, loc);
                if (!rs.resultSet()) {
                    return;
                }
                // sort contents
                TARDISSonicSorterListener.sortInventory(inv);
                World w = chest.getWorld();
                // get vault dimensions
                int sx = rs.getX();
                int sy = rs.getY();
                int sz = rs.getZ();
                // loop through vault blocks
                for (int y = sy; y < (sy + 16); y++) {
                    for (int x = sx; x < (sx + 16); x++) {
                        for (int z = sz; z < (sz + 16); z++) {
                            // get the block
                            Block b = w.getBlockAt(x, y, z);
                            // check if it is a chest (but not the drop chest)
                            if ((b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) && !loc.equals(b.getLocation().toString())) {
                                Chest c = (Chest) b.getState();
                                // get chest contents
                                Inventory cinv = c.getBlockInventory();
                                // make sure there is a free slot
                                if (cinv.firstEmpty() != -1) {
                                    ItemStack[] cc = cinv.getContents();
                                    List<Material> mats = new ArrayList<>();
                                    // find unique item stack materials
                                    for (ItemStack is : cc) {
                                        if (is != null) {
                                            Material m = is.getType();
                                            if (!mats.contains(m)) {
                                                mats.add(m);
                                            }
                                        }
                                    }
                                    // for each material found, see if there are any stacks of it in the drop chest
                                    mats.forEach((m) -> {
                                        int slot = inv.first(m);
                                        while (slot != -1 && cinv.firstEmpty() != -1) {
                                            // get the item stack
                                            ItemStack get = inv.getItem(slot);
                                            // remove the stack from the drop chest
                                            inv.setItem(slot, null);
                                            // put it in the chest
                                            cinv.setItem(cinv.firstEmpty(), get);
                                            // sort the chest
                                            TARDISSonicSorterListener.sortInventory(cinv);
                                            // get any other stacks
                                            slot = inv.first(m);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
