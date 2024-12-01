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
    
    private final NamespacedKey[] framesDalekSec = new NamespacedKey[]{
            DalekSecVariant.DALEK_SEC_0.getKey(),
            DalekSecVariant.DALEK_SEC_1.getKey(),
            DalekSecVariant.DALEK_SEC_2.getKey(),
            DalekSecVariant.DALEK_SEC_1.getKey(),
            DalekSecVariant.DALEK_SEC_0.getKey(),
            DalekSecVariant.DALEK_SEC_3.getKey(),
            DalekSecVariant.DALEK_SEC_4.getKey(),
            DalekSecVariant.DALEK_SEC_3.getKey()
    };
    private final NamespacedKey[] framesDalekSecTarget = new NamespacedKey[]{
            DalekSecVariant.DALEK_SEC_ATTACKING_0.getKey(),
            DalekSecVariant.DALEK_SEC_ATTACKING_1.getKey(),
            DalekSecVariant.DALEK_SEC_ATTACKING_2.getKey(),
            DalekSecVariant.DALEK_SEC_ATTACKING_1.getKey(),
            DalekSecVariant.DALEK_SEC_ATTACKING_0.getKey(),
            DalekSecVariant.DALEK_SEC_ATTACKING_3.getKey(),
            DalekSecVariant.DALEK_SEC_ATTACKING_4.getKey(),
            DalekSecVariant.DALEK_SEC_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesHath = new NamespacedKey[]{
            HathVariant.HATH_0.getKey(),
            HathVariant.HATH_1.getKey(),
            HathVariant.HATH_2.getKey(),
            HathVariant.HATH_1.getKey(),
            HathVariant.HATH_0.getKey(),
            HathVariant.HATH_3.getKey(),
            HathVariant.HATH_4.getKey(),
            HathVariant.HATH_3.getKey()
    };
    private final NamespacedKey[] framesHathTarget = new NamespacedKey[]{
            HathVariant.HATH_ATTACKING_0.getKey(),
            HathVariant.HATH_ATTACKING_1.getKey(),
            HathVariant.HATH_ATTACKING_2.getKey(),
            HathVariant.HATH_ATTACKING_1.getKey(),
            HathVariant.HATH_ATTACKING_0.getKey(),
            HathVariant.HATH_ATTACKING_3.getKey(),
            HathVariant.HATH_ATTACKING_4.getKey(),
            HathVariant.HATH_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesIceWarrior = new NamespacedKey[]{
            IceWarriorVariant.ICE_WARRIOR_0.getKey(),
            IceWarriorVariant.ICE_WARRIOR_1.getKey(),
            IceWarriorVariant.ICE_WARRIOR_2.getKey(),
            IceWarriorVariant.ICE_WARRIOR_1.getKey(),
            IceWarriorVariant.ICE_WARRIOR_0.getKey(),
            IceWarriorVariant.ICE_WARRIOR_3.getKey(),
            IceWarriorVariant.ICE_WARRIOR_4.getKey(),
            IceWarriorVariant.ICE_WARRIOR_3.getKey()
    };
    private final NamespacedKey[] framesIceWarriorTarget = new NamespacedKey[]{
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_0.getKey(),
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_1.getKey(),
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_2.getKey(),
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_1.getKey(),
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_0.getKey(),
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_3.getKey(),
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_4.getKey(),
            IceWarriorVariant.ICE_WARRIOR_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesStrax = new NamespacedKey[]{
            StraxVariant.STRAX_0.getKey(),
            StraxVariant.STRAX_1.getKey(),
            StraxVariant.STRAX_2.getKey(),
            StraxVariant.STRAX_1.getKey(),
            StraxVariant.STRAX_0.getKey(),
            StraxVariant.STRAX_3.getKey(),
            StraxVariant.STRAX_4.getKey(),
            StraxVariant.STRAX_3.getKey()
    };
    private final NamespacedKey[] framesStraxTarget = new NamespacedKey[]{
            StraxVariant.STRAX_ATTACKING_0.getKey(),
            StraxVariant.STRAX_ATTACKING_1.getKey(),
            StraxVariant.STRAX_ATTACKING_2.getKey(),
            StraxVariant.STRAX_ATTACKING_1.getKey(),
            StraxVariant.STRAX_ATTACKING_0.getKey(),
            StraxVariant.STRAX_ATTACKING_3.getKey(),
            StraxVariant.STRAX_ATTACKING_4.getKey(),
            StraxVariant.STRAX_ATTACKING_3.getKey()
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
                    case MANGROVE_PROPAGULE -> im.setItemModel(DalekSecVariant.DALEK_SEC_STATIC.getKey());
                    case CRIMSON_BUTTON -> im.setItemModel(DavrosVariant.DAVROS.getKey());
                    case PUFFERFISH -> im.setItemModel(HathVariant.HATH_STATIC.getKey());
                    case SNOWBALL -> im.setItemModel(IceWarriorVariant.ICE_WARRIOR_STATIC.getKey());
                    case BAKED_POTATO -> im.setItemModel(StraxVariant.STRAX_STATIC.getKey());
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
