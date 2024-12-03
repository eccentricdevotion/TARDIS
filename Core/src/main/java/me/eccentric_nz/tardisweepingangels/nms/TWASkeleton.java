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

import me.eccentric_nz.TARDIS.custommodels.keys.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Custom entity class for Headless Monks, Mire, Silent, Silurians, and Weeping Angels
 *
 * @author eccentric_nz
 */
public class TWASkeleton extends Skeleton {

    private final NamespacedKey[] framesMonk = new NamespacedKey[]{
            MonkVariant.HEADLESS_MONK_0.getKey(),
            MonkVariant.HEADLESS_MONK_1.getKey(),
            MonkVariant.HEADLESS_MONK_2.getKey(),
            MonkVariant.HEADLESS_MONK_1.getKey(),
            MonkVariant.HEADLESS_MONK_0.getKey(),
            MonkVariant.HEADLESS_MONK_3.getKey(),
            MonkVariant.HEADLESS_MONK_4.getKey(),
            MonkVariant.HEADLESS_MONK_3.getKey()
    };
    private final NamespacedKey[] framesMonkTarget = new NamespacedKey[]{
            MonkVariant.HEADLESS_MONK_ATTACKING_0.getKey(),
            MonkVariant.HEADLESS_MONK_ATTACKING_1.getKey(),
            MonkVariant.HEADLESS_MONK_ATTACKING_2.getKey(),
            MonkVariant.HEADLESS_MONK_ATTACKING_1.getKey(),
            MonkVariant.HEADLESS_MONK_ATTACKING_0.getKey(),
            MonkVariant.HEADLESS_MONK_ATTACKING_3.getKey(),
            MonkVariant.HEADLESS_MONK_ATTACKING_4.getKey(),
            MonkVariant.HEADLESS_MONK_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesMire = new NamespacedKey[]{
            MireVariant.THE_MIRE_0.getKey(),
            MireVariant.THE_MIRE_1.getKey(),
            MireVariant.THE_MIRE_2.getKey(),
            MireVariant.THE_MIRE_1.getKey(),
            MireVariant.THE_MIRE_0.getKey(),
            MireVariant.THE_MIRE_3.getKey(),
            MireVariant.THE_MIRE_4.getKey(),
            MireVariant.THE_MIRE_3.getKey()
    };
    private final NamespacedKey[] framesMireTarget = new NamespacedKey[]{
            MireVariant.THE_MIRE_ATTACKING_0.getKey(),
            MireVariant.THE_MIRE_ATTACKING_1.getKey(),
            MireVariant.THE_MIRE_ATTACKING_2.getKey(),
            MireVariant.THE_MIRE_ATTACKING_1.getKey(),
            MireVariant.THE_MIRE_ATTACKING_0.getKey(),
            MireVariant.THE_MIRE_ATTACKING_3.getKey(),
            MireVariant.THE_MIRE_ATTACKING_4.getKey(),
            MireVariant.THE_MIRE_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesSilent = new NamespacedKey[]{
            SilentVariant.SILENT_0.getKey(),
            SilentVariant.SILENT_1.getKey(),
            SilentVariant.SILENT_2.getKey(),
            SilentVariant.SILENT_1.getKey(),
            SilentVariant.SILENT_0.getKey(),
            SilentVariant.SILENT_3.getKey(),
            SilentVariant.SILENT_4.getKey(),
            SilentVariant.SILENT_3.getKey()
    };
    private final NamespacedKey[] framesSilentTarget = new NamespacedKey[]{
            SilentVariant.SILENT_ATTACKING_0.getKey(),
            SilentVariant.SILENT_ATTACKING_1.getKey(),
            SilentVariant.SILENT_ATTACKING_2.getKey(),
            SilentVariant.SILENT_ATTACKING_1.getKey(),
            SilentVariant.SILENT_ATTACKING_0.getKey(),
            SilentVariant.SILENT_ATTACKING_3.getKey(),
            SilentVariant.SILENT_ATTACKING_4.getKey(),
            SilentVariant.SILENT_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesSilurian = new NamespacedKey[]{
            SilurianVariant.SILURIAN_0.getKey(),
            SilurianVariant.SILURIAN_1.getKey(),
            SilurianVariant.SILURIAN_2.getKey(),
            SilurianVariant.SILURIAN_1.getKey(),
            SilurianVariant.SILURIAN_0.getKey(),
            SilurianVariant.SILURIAN_3.getKey(),
            SilurianVariant.SILURIAN_4.getKey(),
            SilurianVariant.SILURIAN_3.getKey()
    };
    private final NamespacedKey[] framesSilurianTarget = new NamespacedKey[]{
            SilurianVariant.SILURIAN_ATTACKING_0.getKey(),
            SilurianVariant.SILURIAN_ATTACKING_1.getKey(),
            SilurianVariant.SILURIAN_ATTACKING_2.getKey(),
            SilurianVariant.SILURIAN_ATTACKING_1.getKey(),
            SilurianVariant.SILURIAN_ATTACKING_0.getKey(),
            SilurianVariant.SILURIAN_ATTACKING_3.getKey(),
            SilurianVariant.SILURIAN_ATTACKING_4.getKey(),
            SilurianVariant.SILURIAN_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesAngel = new NamespacedKey[]{
            WeepingAngelVariant.WEEPING_ANGEL_0.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_1.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_2.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_1.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_0.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_3.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_4.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_3.getKey()
    };
    private final NamespacedKey[] framesAngelTarget = new NamespacedKey[]{
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_0.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_1.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_2.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_1.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_0.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_3.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_4.getKey(),
            WeepingAngelVariant.WEEPING_ANGEL_ATTACKING_3.getKey()
    };
    private double oldX;
    private double oldZ;
    private int i = 0;
    private boolean beaming = false;

    public TWASkeleton(EntityType<? extends Skeleton> type, Level level) {
        super(type, level);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 3 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            Entity passenger = getFirstPassenger();
            if (passenger instanceof Guardian guardian) {
                beaming = guardian.hasActiveAttackTarget();
            }
            if ((oldX == getX() && oldZ == getZ()) || beaming) {
                switch (bukkit.getType()) {
                    case RED_CANDLE -> im.setItemModel(MonkVariant.HEADLESS_MONK_STATIC.getKey());
                    case NETHERITE_SCRAP -> im.setItemModel(MireVariant.THE_MIRE_STATIC.getKey());
                    case END_STONE -> im.setItemModel(beaming ? SilentVariant.SILENT_BEAMING.getKey() : SilentVariant.SILENT_STATIC.getKey());
                    case FEATHER -> im.setItemModel(SilurianVariant.SILURIAN_STATIC.getKey());
                    case BRICK -> im.setItemModel(WeepingAngelVariant.WEEPING_ANGEL_STATIC.getKey());
                }
                i = 0;
            } else {
                // play move animation
                boolean hasTarget = getTarget() != null;
                switch (bukkit.getType()) {
                    case RED_CANDLE -> im.setItemModel(hasTarget ? framesMonkTarget[i] : framesMonk[i]);
                    case NETHERITE_SCRAP -> im.setItemModel(hasTarget ? framesMireTarget[i] : framesMire[i]);
                    case END_STONE -> im.setItemModel(hasTarget ? framesSilentTarget[i] : framesSilent[i]);
                    case FEATHER -> im.setItemModel(hasTarget ? framesSilurianTarget[i] : framesSilent[i]);
                    case BRICK -> im.setItemModel(hasTarget ? framesAngelTarget[i] : framesAngel[i]);
                }
                i++;
                if (i == framesMonk.length) {
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
