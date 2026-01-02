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
package me.eccentric_nz.TARDIS.rotors;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TimeRotorAnimation implements Runnable {

    private final ItemFrame frame;
    private final int[] frames;
    private final String which;
    private int i = 1;

    public TimeRotorAnimation(ItemFrame frame, int[] frames, String which) {
        this.frame = frame;
        this.frames = frames;
        this.which = which;
    }

    @Override
    public void run() {
        ItemStack is = frame.getItem();
        if (!is.getType().isAir()) {
            ItemMeta im = is.getItemMeta();
            if (im != null) {
                im.setItemModel(new NamespacedKey(TARDIS.plugin, "time_rotor_" + which + "_" + frames[i]));
                is.setItemMeta(im);
                frame.setItem(is, false);
                i++;
                if (i == frames.length) {
                    i = 0;
                }
            }
        }
    }
}
