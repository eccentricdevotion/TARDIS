/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Using data collected from the Interstitial Antenna, the Mean Free Path
 * Tracker shows a TARDIS' planned path through the Space-Time Vortex. It allows
 * the operator to spot approaching turbulence in a TARDIS' flight path.
 *
 * @author eccentric_nz
 */
public class TARDISRegulatorSlot {

    public final List<Integer> bounds = Arrays.asList(new Integer[]{
        0, 1, 2, 3, 4,
        9, 10, 11, 12, 13,
        18, 19, 20, 21, 22,
        27, 28, 29, 30, 31,
        36, 37, 38, 39, 40
    });
    public final ItemStack box;
    public final ItemStack vortex;

    public TARDISRegulatorSlot() {
        this.box = new ItemStack(Material.WOOL, 1, (byte) 11);
        ItemMeta ler = this.box.getItemMeta();
        ler.setDisplayName("Regulator");
        this.box.setItemMeta(ler);
        this.vortex = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
    }

    public int upSlot(int current_slot) {
        return current_slot - 9;
    }

    public int leftSlot(int current_slot) {
        return current_slot - 1;
    }

    public int rightSlot(int current_slot) {
        return current_slot + 1;
    }

    public int downSlot(int current_slot) {
        return current_slot + 9;
    }
}
