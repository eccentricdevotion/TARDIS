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

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Custom entity class for Dalek Sec, Hath, Ice Warrior, and Strax
 *
 * @author eccentric_nz
 */
public class TWAZombifiedPiglin extends ZombifiedPiglin {

    /*
    DALEK_SEC
    DAVROS
    HATH
    ICE_WARRIOR
    STRAX
     */
    private final NamespacedKey[] framesDalekSec = new NamespacedKey[]{
            MangrovePropagule.DALEK_SEC_0.getKey(),
            MangrovePropagule.DALEK_SEC_1.getKey(),
            MangrovePropagule.DALEK_SEC_2.getKey(),
            MangrovePropagule.DALEK_SEC_1.getKey(),
            MangrovePropagule.DALEK_SEC_0.getKey(),
            MangrovePropagule.DALEK_SEC_3.getKey(),
            MangrovePropagule.DALEK_SEC_4.getKey(),
            MangrovePropagule.DALEK_SEC_3.getKey()
    };
    private final NamespacedKey[] framesDalekSecTarget = new NamespacedKey[]{
            MangrovePropagule.DALEK_SEC_ATTACKING_0.getKey(),
            MangrovePropagule.DALEK_SEC_ATTACKING_1.getKey(),
            MangrovePropagule.DALEK_SEC_ATTACKING_2.getKey(),
            MangrovePropagule.DALEK_SEC_ATTACKING_1.getKey(),
            MangrovePropagule.DALEK_SEC_ATTACKING_0.getKey(),
            MangrovePropagule.DALEK_SEC_ATTACKING_3.getKey(),
            MangrovePropagule.DALEK_SEC_ATTACKING_4.getKey(),
            MangrovePropagule.DALEK_SEC_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesHath = new NamespacedKey[]{
            Pufferfish.HATH_0.getKey(),
            Pufferfish.HATH_1.getKey(),
            Pufferfish.HATH_2.getKey(),
            Pufferfish.HATH_1.getKey(),
            Pufferfish.HATH_0.getKey(),
            Pufferfish.HATH_3.getKey(),
            Pufferfish.HATH_4.getKey(),
            Pufferfish.HATH_3.getKey()
    };
    private final NamespacedKey[] framesHathTarget = new NamespacedKey[]{
            Pufferfish.HATH_ATTACKING_0.getKey(),
            Pufferfish.HATH_ATTACKING_1.getKey(),
            Pufferfish.HATH_ATTACKING_2.getKey(),
            Pufferfish.HATH_ATTACKING_1.getKey(),
            Pufferfish.HATH_ATTACKING_0.getKey(),
            Pufferfish.HATH_ATTACKING_3.getKey(),
            Pufferfish.HATH_ATTACKING_4.getKey(),
            Pufferfish.HATH_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesIceWarrior = new NamespacedKey[]{
            Snowball.ICE_WARRIOR_0.getKey(),
            Snowball.ICE_WARRIOR_1.getKey(),
            Snowball.ICE_WARRIOR_2.getKey(),
            Snowball.ICE_WARRIOR_1.getKey(),
            Snowball.ICE_WARRIOR_0.getKey(),
            Snowball.ICE_WARRIOR_3.getKey(),
            Snowball.ICE_WARRIOR_4.getKey(),
            Snowball.ICE_WARRIOR_3.getKey()
    };
    private final NamespacedKey[] framesIceWarriorTarget = new NamespacedKey[]{
            Snowball.ICE_WARRIOR_ATTACKING_0.getKey(),
            Snowball.ICE_WARRIOR_ATTACKING_1.getKey(),
            Snowball.ICE_WARRIOR_ATTACKING_2.getKey(),
            Snowball.ICE_WARRIOR_ATTACKING_1.getKey(),
            Snowball.ICE_WARRIOR_ATTACKING_0.getKey(),
            Snowball.ICE_WARRIOR_ATTACKING_3.getKey(),
            Snowball.ICE_WARRIOR_ATTACKING_4.getKey(),
            Snowball.ICE_WARRIOR_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesStrax = new NamespacedKey[]{
            BakedPotato.STRAX_0.getKey(),
            BakedPotato.STRAX_1.getKey(),
            BakedPotato.STRAX_2.getKey(),
            BakedPotato.STRAX_1.getKey(),
            BakedPotato.STRAX_0.getKey(),
            BakedPotato.STRAX_3.getKey(),
            BakedPotato.STRAX_4.getKey(),
            BakedPotato.STRAX_3.getKey()
    };
    private final NamespacedKey[] framesStraxTarget = new NamespacedKey[]{
            BakedPotato.STRAX_ATTACKING_0.getKey(),
            BakedPotato.STRAX_ATTACKING_1.getKey(),
            BakedPotato.STRAX_ATTACKING_2.getKey(),
            BakedPotato.STRAX_ATTACKING_1.getKey(),
            BakedPotato.STRAX_ATTACKING_0.getKey(),
            BakedPotato.STRAX_ATTACKING_3.getKey(),
            BakedPotato.STRAX_ATTACKING_4.getKey(),
            BakedPotato.STRAX_ATTACKING_3.getKey()
    };
    private double oldX;
    private double oldZ;
    private int i = 0;

    public TWAZombifiedPiglin(EntityType<? extends ZombifiedPiglin> type, Level level) {
        super(type, level);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 3 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (oldX == getX() && oldZ == getZ()) {
                switch (bukkit.getType()) {
                    case MANGROVE_PROPAGULE -> im.setItemModel(MangrovePropagule.DALEK_SEC_STATIC.getKey());
                    case CRIMSON_BUTTON -> im.setItemModel(CrimsonButton.DAVROS.getKey());
                    case PUFFERFISH -> im.setItemModel(Pufferfish.HATH_STATIC.getKey());
                    case SNOWBALL -> im.setItemModel(Snowball.ICE_WARRIOR_STATIC.getKey());
                    case BAKED_POTATO -> im.setItemModel(BakedPotato.STRAX_STATIC.getKey());
                }
                i = 0;
            } else {
                // play move animation
                boolean hasTarget = (getTarget() != null);
                switch (bukkit.getType()) {
                    case MANGROVE_PROPAGULE -> im.setItemModel(hasTarget ? framesDalekSecTarget[i] : framesDalekSec[i]);
                    case PUFFERFISH -> im.setItemModel(hasTarget ? framesHathTarget[i] : framesHath[i]);
                    case SNOWBALL -> im.setItemModel(hasTarget ? framesIceWarriorTarget[i] : framesIceWarrior[i]);
                    case BAKED_POTATO -> im.setItemModel(hasTarget ? framesStraxTarget[i] : framesStrax[i]);
                    default -> { } // no animation for DAVROS
                }
                i++;
                if (i == framesHath.length) {
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
