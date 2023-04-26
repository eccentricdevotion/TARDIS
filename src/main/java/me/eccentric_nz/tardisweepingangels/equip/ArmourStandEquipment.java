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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ArmourStandEquipment {

    public void setStandEquipment(ArmorStand as, Monster monster, boolean small) {
        as.setSmall(small);
        as.setArms(false);
        as.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER, 1);
        ItemStack head = new ItemStack(monster.getMaterial(), 1);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(monster.getName() + " Head");
        headMeta.setCustomModelData(monster.getCustomModelData());
        head.setItemMeta(headMeta);
        setHelmetOnly(as, head);
    }

    private void setHelmetOnly(ArmorStand as, ItemStack is) {
        EntityEquipment ee = as.getEquipment();
        ee.setChestplate(null);
        ee.setLeggings(null);
        ee.setBoots(null);
        ee.setHelmet(is);
        ee.setItemInMainHand(null);
        ee.setItemInOffHand(null);
        as.setVisible(false);
    }
}
