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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

class MicroscopeUtils {

    static final HashMap<UUID, ItemStack> STORED_STACKS = new HashMap<>();

    static boolean hasItemInHand(ItemStack is, Material type, TARDIS plugin) {
        if (is == null) {
            return false;
        }
        if (!is.getType().equals(type)) {
            return false;
        }
        // does it have item meta
        ItemMeta im = is.getItemMeta();
        if (im == null) {
            return false;
        }
        if (!im.hasDisplayName()) {
            return false;
        }
        if (!im.hasItemModel()) {
            return false;
        }
        return im.getPersistentDataContainer().has(plugin.getMicroscopeKey(), PersistentDataType.STRING);
    }

    static void reduceInHand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        int count = itemStack.getAmount();
        if (count - 1 > 0) {
            itemStack.setAmount(count - 1);
        } else {
            itemStack = null;
        }
        player.getInventory().setItemInMainHand(itemStack);
    }
}
