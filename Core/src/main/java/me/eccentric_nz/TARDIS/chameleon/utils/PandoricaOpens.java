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
package me.eccentric_nz.TARDIS.chameleon.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class PandoricaOpens {

    private final TARDIS plugin;
    private final NamespacedKey[] opening = new NamespacedKey[]{
            ChameleonVariant.PANDORICA_75.getKey(),
            ChameleonVariant.PANDORICA_50.getKey(),
            ChameleonVariant.PANDORICA_25.getKey(),
            ChameleonVariant.PANDORICA_OPEN.getKey()
    };
    private final NamespacedKey[] closing = new NamespacedKey[]{
            ChameleonVariant.PANDORICA_25.getKey(),
            ChameleonVariant.PANDORICA_50.getKey(),
            ChameleonVariant.PANDORICA_75.getKey(),
            ChameleonVariant.PANDORICA_CLOSED.getKey()
    };
    private final BukkitScheduler scheduler;

    public PandoricaOpens(TARDIS plugin) {
        this.plugin = plugin;
        this.scheduler = this.plugin.getServer().getScheduler();
    }

    public void animate(ArmorStand stand, boolean open) {
        Location location = stand.getLocation();
        while (!location.getChunk().isLoaded()) {
            location.getChunk().load();
        }
        EntityEquipment ee = stand.getEquipment();
        ItemStack is = ee.getHelmet();
        ItemMeta im = is.getItemMeta();
        long delay = 5;
        for (int i = 0; i < 4; i++) {
            String sound;
            NamespacedKey model;
            if (open) {
                sound = "pandorica_open";
                model = opening[i];
            } else {
                sound = "pandorica_close";
                model = closing[i];
            }
            TARDISSounds.playTARDISSound(stand.getLocation(), sound);
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                im.setItemModel(model);
                is.setItemMeta(im);
                ee.setHelmet(is, true);
            }, delay * i);
        }
    }
}
