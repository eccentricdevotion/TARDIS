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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetSmelter;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicSorterListener;
import me.eccentric_nz.TARDIS.utility.Smelter;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class TARDISSmelterListener implements Listener {

    private final TARDIS plugin;

    public TARDISSmelterListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSmelterDropChestClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            String loc = chest.getLocation().toString();
            // check is drop chest
            ResultSetSmelter rs = new ResultSetSmelter(plugin, loc);
            if (!rs.resultSet()) {
                return;
            }
            // sort contents
            TARDISSonicSorterListener.sortInventory(inv);
            // get fuel chests
            List<Chest> fuelChests = rs.getFuelChests();
            List<Chest> oreChests = rs.getOreChests();
            // process drop chest contents
            HashMap<Material, Integer> fuels = new HashMap<>();
            HashMap<Material, Integer> ores = new HashMap<>();
            HashMap<Material, Integer> remainders = new HashMap<>();
            for (ItemStack is : inv.getContents()) {
                if (is != null) {
                    Material m = is.getType();
                    if (m.isFuel()) {
                        int amount = (fuels.containsKey(m)) ? fuels.get(m) + is.getAmount() : is.getAmount();
                        fuels.put(m, amount);
                        inv.remove(is);
                    }
                    if (Smelter.isSmeltable(m)) {
                        int amount = (ores.containsKey(m)) ? ores.get(m) + is.getAmount() : is.getAmount();
                        ores.put(m, amount);
                        inv.remove(is);
                    }
                }
            }
            // process fuels
            int fsize = fuelChests.size();
            fuels.forEach((key, value) -> {
                int remainder = value % fsize;
                if (remainder > 0) {
                    remainders.put(key, remainder);
                }
                int distrib = value / fsize;
                fuelChests.forEach((fc) -> {
                    HashMap<Integer, ItemStack> leftoverfuel = fc.getInventory().addItem(new ItemStack(key, distrib));
                    if (!leftoverfuel.isEmpty()) {
                        for (ItemStack f : leftoverfuel.values()) {
                            Material fm = f.getType();
                            int amount = (remainders.containsKey(fm)) ? remainders.get(fm) + f.getAmount() : f.getAmount();
                            remainders.put(fm, amount);
                        }
                    }
                });
            });
            // process ores
            int osize = oreChests.size();
            ores.forEach((key, value) -> {
                int remainder = value % osize;
                if (remainder > 0) {
                    remainders.put(key, remainder);
                }
                int distrib = value / osize;
                oreChests.forEach((fc) -> {
                    HashMap<Integer, ItemStack> leftoverore = fc.getInventory().addItem(new ItemStack(key, distrib));
                    if (!leftoverore.isEmpty()) {
                        for (ItemStack o : leftoverore.values()) {
                            Material om = o.getType();
                            int amount = (remainders.containsKey(om)) ? remainders.get(om) + o.getAmount() : o.getAmount();
                            remainders.put(om, amount);
                        }
                    }
                });
            });
            // return remainder to drop chest
            remainders.forEach((key, value) -> {
                int max = key.getMaxStackSize();
                if (value > max) {
                    int remainder = value % max;
                    for (int i = 0; i < value / max; i++) {
                        inv.addItem(new ItemStack(key, max));
                    }
                    if (remainder > 0) {
                        inv.addItem(new ItemStack(key, remainder));
                    }
                } else {
                    inv.addItem(new ItemStack(key, value));
                }
            });
        }
    }
}
