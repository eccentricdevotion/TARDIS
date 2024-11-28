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
package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.custommodeldata.keys.Kelp;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Custom entity class for Sea Devils
 *
 * @author eccentric_nz
 */
public class TWADrowned extends Drowned {

    private final NamespacedKey[] frames = new NamespacedKey[]{
            Kelp.SEA_DEVIL_0.getKey(),
            Kelp.SEA_DEVIL_1.getKey(),
            Kelp.SEA_DEVIL_2.getKey(),
            Kelp.SEA_DEVIL_1.getKey(),
            Kelp.SEA_DEVIL_0.getKey(),
            Kelp.SEA_DEVIL_3.getKey(),
            Kelp.SEA_DEVIL_4.getKey(),
            Kelp.SEA_DEVIL_3.getKey()
    };
    private final NamespacedKey[] framesSwimming = new NamespacedKey[]{
            Kelp.SEA_DEVIL_SWIMMING_0.getKey(),
            Kelp.SEA_DEVIL_SWIMMING_1.getKey(),
            Kelp.SEA_DEVIL_SWIMMING_2.getKey(),
            Kelp.SEA_DEVIL_SWIMMING_1.getKey(),
            Kelp.SEA_DEVIL_SWIMMING_0.getKey(),
            Kelp.SEA_DEVIL_SWIMMING_3.getKey(),
            Kelp.SEA_DEVIL_SWIMMING_4.getKey(),
            Kelp.SEA_DEVIL_SWIMMING_3.getKey()
    };
    private int i = 0;
    private double oldX;
    private double oldZ;

    public TWADrowned(EntityType<? extends Drowned> type, Level level) {
        super(type, level);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 3 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (oldX == getX() && oldZ == getZ()) {
                im.setItemModel(Kelp.SEA_DEVIL_STATIC.getKey());
                i = 0;
            } else {
                // play move animation
                im.setItemModel(isSwimming() ? framesSwimming[i] : frames[i]);
                i++;
                if (i == frames.length) {
                    i = 0;
                }
            }
            bukkit.setItemMeta(im);
            setItemSlot(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(bukkit));
            oldX = getX();
            oldZ = getZ();
        }
        super.aiStep();
    }
}
