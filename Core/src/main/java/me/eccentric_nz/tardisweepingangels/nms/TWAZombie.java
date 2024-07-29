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

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Custom entity class for Cybermen, Empty Children, Slitheen, Sontarans, Vashata Nerada, and Zygons
 *
 * @author eccentric_nz
 */
public class TWAZombie extends Zombie {

    private final int[] frames = new int[]{0, 1, 2, 1, 0, 3, 4, 3};
    private double oldX;
    private double oldZ;
    private int i = 0;
    private int variant = 0;

    public TWAZombie(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 3 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (oldX == getX() && oldZ == getZ()) {
                im.setCustomModelData(405 + variant);
                i = 0;
            } else {
                // play move animation
                int cmd = getTarget() != null ? 406 + variant : 400 + variant;
                im.setCustomModelData(cmd + frames[i]);
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

    public void setVariant(int variant) {
        this.variant = variant;
    }
}
