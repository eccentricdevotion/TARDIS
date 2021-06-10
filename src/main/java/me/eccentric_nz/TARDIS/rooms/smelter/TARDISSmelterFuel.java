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
package me.eccentric_nz.tardis.rooms.smelter;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class TARDISSmelterFuel {

    public void processItems(Inventory inv, List<Chest> fuelChests) {
        // process fuel chest contents
        HashMap<Material, Integer> fuels = new HashMap<>();
        HashMap<Material, Integer> remainders = new HashMap<>();
        for (ItemStack is : inv.getContents()) {
            if (is != null) {
                Material m = is.getType();
                if (m.isFuel()) {
                    int amount = (fuels.containsKey(m)) ? fuels.get(m) + is.getAmount() : is.getAmount();
                    fuels.put(m, amount);
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
        // return remainder to fuel chest
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
