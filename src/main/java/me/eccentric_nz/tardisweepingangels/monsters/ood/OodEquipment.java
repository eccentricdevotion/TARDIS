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
package me.eccentric_nz.tardisweepingangels.monsters.ood;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.equip.DisguiseEquipper;
import me.eccentric_nz.tardisweepingangels.equip.FollowerEquipper;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OodEquipment {

    public static void set(Player player, Entity entity, boolean disguise) {
        int colour = 0;
        int r = TARDISConstants.RANDOM.nextInt(100);
        if (r > 70) {
            colour = (r > 85) ? 20 : 10;
        }
        ItemStack head = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("Ood Head");
        headMeta.setCustomModelData((disguise) ? 29 : 2 + colour);
        head.setItemMeta(headMeta);
        if (!disguise) {
            new FollowerEquipper().setHelmetAndInvisibilty(player, entity, Monster.OOD, head);
        } else {
            new DisguiseEquipper().setHelmetAndInvisibilty(entity, head);
        }
    }
}
