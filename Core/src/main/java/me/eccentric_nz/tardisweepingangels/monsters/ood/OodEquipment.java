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
package me.eccentric_nz.tardisweepingangels.monsters.ood;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodeldata.keys.RottenFlesh;
import me.eccentric_nz.tardisweepingangels.equip.DisguiseEquipper;
import me.eccentric_nz.tardisweepingangels.equip.FollowerEquipper;
import me.eccentric_nz.tardisweepingangels.nms.TWAOod;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OodEquipment {

    public static void set(OfflinePlayer player, Entity entity, boolean disguise, boolean random) {
        if (random) {
            TWAOod ood = (TWAOod) ((CraftEntity) entity).getHandle();
            ood.setRedeye(TARDISConstants.RANDOM.nextBoolean());
            int chance = TARDISConstants.RANDOM.nextInt(100);
            if (chance < 15) {
                ood.setColour(OodColour.BLUE);
            }
            if (chance > 85) {
                ood.setColour(OodColour.BROWN);
            }
        }
        ItemStack head = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("Ood Head");
        if (!disguise) {
            headMeta.setItemModel(RottenFlesh.OOD_BLACK_STATIC.getKey());
            head.setItemMeta(headMeta);
            new FollowerEquipper().setHelmetAndInvisibilty(player, entity, Monster.OOD, head);
        } else {
            int chance = TARDISConstants.RANDOM.nextInt(100);
            NamespacedKey colour = RottenFlesh.OOD_BLACK_STATIC.getKey();
            if (chance < 15) {
                colour = RottenFlesh.OOD_BLUE_STATIC.getKey();
            }
            if (chance > 85) {
                colour =  RottenFlesh.OOD_BROWN_STATIC.getKey();
            }
            headMeta.setItemModel(colour);
            head.setItemMeta(headMeta);
            new DisguiseEquipper().setHelmetAndInvisibilty(entity, head);
        }
    }
}
