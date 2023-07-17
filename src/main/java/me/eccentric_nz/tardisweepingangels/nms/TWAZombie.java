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

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;

/**
 * Custom entity class for Cybermen, Empty Children, Slitheen, Sontarans, Vashata Nerada, and Zygons
 *
 * @author eccentric_nz
 */
public class TWAZombie extends Zombie {

    private boolean isAnimating = false;
    private int task = -1;
    private int i = 0;
    private int[] frames = new int[]{0, 1, 2, 1, 0, 3, 4, 3};
    private long delay = 3;

    public TWAZombie(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD)) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            CompoundTag nbt = is.getTag();
            if (!isPathFinding()) {
                Bukkit.getScheduler().cancelTask(task);
                nbt.putInt("CustomModelData", 405);
                isAnimating = false;
            } else if (!isAnimating) {
                // play move animation
                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    int cmd = getTarget() != null ? 406 : 400;
                    nbt.putInt("CustomModelData", cmd + frames[i]);
                    i++;
                    if (i == frames.length) {
                        i = 0;
                    }
                }, 1L, delay);
                isAnimating = true;
            }
        }
        super.aiStep();
    }
}
