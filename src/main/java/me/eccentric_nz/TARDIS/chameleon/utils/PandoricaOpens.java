/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class PandoricaOpens {

    private final TARDIS plugin;
    private final int[] opening = new int[]{ 1005, 1006, 1007, 1002 };
    private final int[] closing = new int[]{ 1007, 1006, 1005, 1001 };
    private final BukkitScheduler scheduler;

    public PandoricaOpens(TARDIS plugin) {
        this.plugin = plugin;
        this.scheduler = this.plugin.getServer().getScheduler();
    }

    /*
      {"predicate": {"custom_model_data":1001}, "model": "tardis:block/pandorica/pandorica"}, // closed
      {"predicate": {"custom_model_data":1002}, "model": "tardis:block/pandorica/pandorica_open"}, // fully open
      {"predicate": {"custom_model_data":1003}, "model": "tardis:block/pandorica/pandorica_stained"},
      {"predicate": {"custom_model_data":1004}, "model": "tardis:block/pandorica/pandorica_glass"},
      {"predicate": {"custom_model_data":1005}, "model": "tardis:block/pandorica/pandorica_75"}, // start opening
      {"predicate": {"custom_model_data":1006}, "model": "tardis:block/pandorica/pandorica_50"}, // mid opening
      {"predicate": {"custom_model_data":1007}, "model": "tardis:block/pandorica/pandorica_25"} // almost opened

      5, 6, 7, 2 // opening
      7, 6, 5, 1 // closing
     */
    public void animate(ArmorStand stand, boolean open) {
        EntityEquipment ee = stand.getEquipment();
        ItemStack is = ee.getHelmet();
        ItemMeta im = is.getItemMeta();
        long delay = 5;
        for (int i = 0; i < 4; i++) {
            int cmd = (open) ? opening[i] : closing[i];
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                im.setCustomModelData(cmd);
                is.setItemMeta(im);
                ee.setHelmet(is, true);
            }, delay * i);
        }
    }
}
