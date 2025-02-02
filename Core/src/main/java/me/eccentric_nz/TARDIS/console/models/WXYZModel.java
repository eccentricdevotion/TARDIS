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
package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledButton;
import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WXYZModel {

    /*
    W => 1
    X => 3
    Y => 2
    Z => 4
     */
    public void setState(ItemDisplay display, TARDIS plugin, int which) {
        if (display == null) {
            return;
        }
        display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON, 1, 1);
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (which) {
            case 4 -> im.setItemModel(ModelledButton.WXYZ_Z.getKey());
            case 3 -> im.setItemModel(ModelledButton.WXYZ_X.getKey());
            case 2 -> im.setItemModel(ModelledButton.WXYZ_Y.getKey());
            case 1 -> im.setItemModel(ModelledButton.WXYZ_W.getKey());
            default -> im.setItemModel(ModelledButton.WXYZ_0.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);
            im.setItemModel(ModelledButton.WXYZ_0.getKey());
            is.setItemMeta(im);
            display.setItemStack(is);
        }, 10);
    }
}
