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
package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 *
 * @author eccentric_nz
 */
public class Heads {

    public static HashMap<Monster, ItemStack> STILL = getStationary();

    private static HashMap<Monster, ItemStack> getStationary() {
        HashMap<Monster, ItemStack> map = new HashMap<>();
        for (Monster monster : Monster.values()) {
            org.bukkit.inventory.ItemStack is = new org.bukkit.inventory.ItemStack(monster.getMaterial());
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(405);
            im.setDisplayName(monster.getName() + " Head");
            is.setItemMeta(im);
            map.put(monster, CraftItemStack.asNMSCopy(is));
        }
        return map;
    }
}
