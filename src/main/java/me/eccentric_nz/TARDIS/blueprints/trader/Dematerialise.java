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
package me.eccentric_nz.TARDIS.blueprints.trader;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mannequin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Dematerialise implements Runnable {

    private final TARDIS plugin;
    private final Mannequin mannequin;
    private int task;
    private int i = 0;
    private final ItemStack is;

    public Dematerialise(TARDIS plugin, Mannequin mannequin) {
        this.plugin = plugin;
        this.mannequin = mannequin;
        is = this.mannequin.getEquipment().getItemInOffHand();
    }

    @Override
    public void run() {
        NamespacedKey model;
        if (i < 4) {
            if (i % 2 == 0) { // stained
                model = ChameleonVariant.TYPE_40_STAINED.getKey();
            } else if (i % 4 == 1) { // glass
                model = ChameleonVariant.TYPE_40_GLASS.getKey();
            } else { // preset
                model = ChameleonVariant.TYPE_40_OPEN.getKey();
            }
            i++;
            ItemMeta im = is.getItemMeta();
            im.setItemModel(model);
            is.setItemMeta(im);
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            mannequin.remove();
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
