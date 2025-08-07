/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.TardisDoorVariant;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DoorAnimator {

    private final TARDIS plugin;
    private final ItemDisplay display;

    private int taskID;
    private NamespacedKey model = null;
    private int frame = 0;

    public DoorAnimator(TARDIS plugin, ItemDisplay display) {
        this.plugin = plugin;
        this.display = display;
    }

    public void animate(boolean close) {
        Location location = display.getLocation();
        ItemStack is = display.getItemStack();
        Material material = is.getType();
        DoorAnimationData data = close ? Door.getCloseData(material) : Door.getOpenData(material);
        TARDISSounds.playTARDISSound(location, data.sound());
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (frame > data.animation().length - 1) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
                return;
            }
            if (material == Material.IRON_DOOR && !plugin.getConfig().getBoolean("police_box.animated_door")) {
                model = close ? TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey() : TardisDoorVariant.TARDIS_DOOR_OPEN.getKey();
                frame = data.animation().length - 1;
            } else {
                model = data.animation()[frame];
            }
            ItemMeta im = is.getItemMeta();
            im.setItemModel(model);
            is.setItemMeta(im);
            display.setItemStack(is);
            frame++;
        }, 2L, data.ticks());
    }
}
