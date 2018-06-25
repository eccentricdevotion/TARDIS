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
package me.eccentric_nz.TARDIS.flight;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Directional Unit is part of the Main Time Mechanism. It takes the Space-Time Coordinates for departure and
 * arrival and converts them into Epsilon Coordinates for travel through the Space-Time Vortex. For this reason it is
 * essential to accurately know your current location to ensure a successful flight.
 *
 * @author eccentric_nz
 */
class TARDISRegulatorInventory {

    private final ItemStack[] regulator;

    public TARDISRegulatorInventory() {
        regulator = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] is = new ItemStack[54];
        for (int col = 0; col < 37; col += 9) {
            for (int row = 0; row < 5; row++) {
                int s = col + row;
                if (s != 20) {
                    is[s] = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                }
            }
        }
        // direction pad up
        ItemStack pad_up = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta up = pad_up.getItemMeta();
        up.setDisplayName("Up");
        pad_up.setItemMeta(up);
        is[16] = pad_up;
        // regulator
        ItemStack wobb = new ItemStack(Material.BLUE_WOOL, 1);
        ItemMeta ler = wobb.getItemMeta();
        ler.setDisplayName("Regulator");
        wobb.setItemMeta(ler);
        is[20] = wobb;
        // direction pad left
        ItemStack pad_left = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta left = pad_left.getItemMeta();
        left.setDisplayName("Left");
        pad_left.setItemMeta(left);
        is[24] = pad_left;
        // direction pad right
        ItemStack pad_right = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta right = pad_right.getItemMeta();
        right.setDisplayName("Right");
        pad_right.setItemMeta(right);
        is[26] = pad_right;
        // direction pad down
        ItemStack pad_down = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta down = pad_down.getItemMeta();
        down.setDisplayName("Down");
        pad_down.setItemMeta(down);
        is[34] = pad_down;

        return is;
    }

    public ItemStack[] getRegulator() {
        return regulator;
    }
}
