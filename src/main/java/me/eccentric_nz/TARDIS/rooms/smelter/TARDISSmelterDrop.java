/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms.smelter;

import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TARDISSmelterDrop {

    public void processItems(Inventory inv, List<Chest> fuelChests, List<Chest> oreChests) {
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
