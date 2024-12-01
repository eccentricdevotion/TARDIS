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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.custommodels.keys.K9Variant;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class HeadBuilder {

    public static ItemStack getItemStack(Monster monster) {
        if (monster == Monster.K9 || monster == Monster.TOCLAFANE) {
            return null;
        }
        Material material = monster.getMaterial();
        NamespacedKey model = monster.getHeadModel();
        if (material == null || model == null) {
            return null;
        }
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER, 99);
        String head = switch (monster) {
            case HEADLESS_MONK -> "Headless Monk Hood";
            case MIRE -> "Mire Helmet";
            default -> monster.getName() + " Head";
        };
        im.setDisplayName(ChatColor.WHITE + head);
        im.setItemModel(model);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getK9() {
        ItemStack is = new ItemStack(Material.BONE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "K9");
        im.setItemModel(K9Variant.K9.getKey());
        is.setItemMeta(im);
        return is;
    }
}
