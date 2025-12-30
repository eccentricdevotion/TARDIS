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
package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ButtonModel {

    public void setState(ItemDisplay display, TARDIS plugin, ConsoleInteraction interaction) {
        if (display == null) {
            return;
        }
        display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON, 1, 1);
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        NamespacedKey model = im.getItemModel();
        if (model == null) {
            model = interaction.getCustomModel();
        } else if (model.getKey().endsWith("_0")) {
            NamespacedKey pressed = new NamespacedKey(plugin, model.getKey().replace("_0", "_1"));
            im.setItemModel(pressed);
            is.setItemMeta(im);
            display.setItemStack(is);
        } else {
            model = new NamespacedKey(plugin, model.getKey().replace("_1", "_0"));
        }
        NamespacedKey released = model;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);
            im.setItemModel(released);
            is.setItemMeta(im);
            display.setItemStack(is);
        }, 10);
    }
}
