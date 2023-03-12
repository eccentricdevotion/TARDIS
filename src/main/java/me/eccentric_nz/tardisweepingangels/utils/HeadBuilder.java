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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class HeadBuilder {

    public static ItemStack getItemStack(Monster monster) {
        Material material = null;
        int cmd = 3;
        if (monster != Monster.K9 && monster != Monster.TOCLAFANE) {
            material = monster.getMaterial();
            cmd = monster.getHeadModelData();
        }
        if (material == null) {
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
        im.setDisplayName(head);
        im.setCustomModelData(cmd);
        is.setItemMeta(im);
        return is;
    }
}
