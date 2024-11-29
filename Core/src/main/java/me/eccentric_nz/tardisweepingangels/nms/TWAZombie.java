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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Custom entity class for Clockwork Droid, Cybermen, Empty Children, Slitheen, Sontarans, Vashata Nerada, and Zygons
 *
 * @author eccentric_nz
 */
public class TWAZombie extends Zombie {

    private final NamespacedKey[] framesDroid = new NamespacedKey[]{
            DroidVariant.CLOCKWORK_DROID_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_2.getKey(),
            DroidVariant.CLOCKWORK_DROID_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_3.getKey(),
            DroidVariant.CLOCKWORK_DROID_4.getKey(),
            DroidVariant.CLOCKWORK_DROID_3.getKey()
    };
    private final NamespacedKey[] framesDroidTarget = new NamespacedKey[]{
            DroidVariant.CLOCKWORK_DROID_ATTACKING_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_ATTACKING_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_ATTACKING_2.getKey(),
            DroidVariant.CLOCKWORK_DROID_ATTACKING_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_ATTACKING_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_ATTACKING_3.getKey(),
            DroidVariant.CLOCKWORK_DROID_ATTACKING_4.getKey(),
            DroidVariant.CLOCKWORK_DROID_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesDroidFemale = new NamespacedKey[]{
            DroidVariant.CLOCKWORK_DROID_FEMALE_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_2.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_3.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_4.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_3.getKey()
    };
    private final NamespacedKey[] framesDroidFemaleTarget = new NamespacedKey[]{
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_2.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_1.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_0.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_3.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_4.getKey(),
            DroidVariant.CLOCKWORK_DROID_FEMALE_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesCyberman = new NamespacedKey[]{
            CybermanVariant.CYBERMAN_0.getKey(),
            CybermanVariant.CYBERMAN_1.getKey(),
            CybermanVariant.CYBERMAN_2.getKey(),
            CybermanVariant.CYBERMAN_1.getKey(),
            CybermanVariant.CYBERMAN_0.getKey(),
            CybermanVariant.CYBERMAN_3.getKey(),
            CybermanVariant.CYBERMAN_4.getKey(),
            CybermanVariant.CYBERMAN_3.getKey()
    };
    private final NamespacedKey[] framesCybermanTarget = new NamespacedKey[]{
            CybermanVariant.CYBERMAN_ATTACKING_0.getKey(),
            CybermanVariant.CYBERMAN_ATTACKING_1.getKey(),
            CybermanVariant.CYBERMAN_ATTACKING_2.getKey(),
            CybermanVariant.CYBERMAN_ATTACKING_1.getKey(),
            CybermanVariant.CYBERMAN_ATTACKING_0.getKey(),
            CybermanVariant.CYBERMAN_ATTACKING_3.getKey(),
            CybermanVariant.CYBERMAN_ATTACKING_4.getKey(),
            CybermanVariant.CYBERMAN_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesEmptyChild = new NamespacedKey[]{
            EmptyChildVariant.EMPTY_CHILD_0.getKey(),
            EmptyChildVariant.EMPTY_CHILD_1.getKey(),
            EmptyChildVariant.EMPTY_CHILD_2.getKey(),
            EmptyChildVariant.EMPTY_CHILD_1.getKey(),
            EmptyChildVariant.EMPTY_CHILD_0.getKey(),
            EmptyChildVariant.EMPTY_CHILD_3.getKey(),
            EmptyChildVariant.EMPTY_CHILD_4.getKey(),
            EmptyChildVariant.EMPTY_CHILD_3.getKey()
    };
    private final NamespacedKey[] framesEmptyChildTarget = new NamespacedKey[]{
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_0.getKey(),
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_1.getKey(),
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_2.getKey(),
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_1.getKey(),
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_0.getKey(),
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_3.getKey(),
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_4.getKey(),
            EmptyChildVariant.EMPTY_CHILD_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesSlitheen = new NamespacedKey[]{
            SlitheenVariant.SLITHEEN_0.getKey(),
            SlitheenVariant.SLITHEEN_1.getKey(),
            SlitheenVariant.SLITHEEN_2.getKey(),
            SlitheenVariant.SLITHEEN_1.getKey(),
            SlitheenVariant.SLITHEEN_0.getKey(),
            SlitheenVariant.SLITHEEN_3.getKey(),
            SlitheenVariant.SLITHEEN_4.getKey(),
            SlitheenVariant.SLITHEEN_3.getKey()
    };
    private final NamespacedKey[] framesSlitheenTarget = new NamespacedKey[]{
            SlitheenVariant.SLITHEEN_ATTACKING_0.getKey(),
            SlitheenVariant.SLITHEEN_ATTACKING_1.getKey(),
            SlitheenVariant.SLITHEEN_ATTACKING_2.getKey(),
            SlitheenVariant.SLITHEEN_ATTACKING_1.getKey(),
            SlitheenVariant.SLITHEEN_ATTACKING_0.getKey(),
            SlitheenVariant.SLITHEEN_ATTACKING_3.getKey(),
            SlitheenVariant.SLITHEEN_ATTACKING_4.getKey(),
            SlitheenVariant.SLITHEEN_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesSontaran = new NamespacedKey[]{
            SontaranVariant.SONTARAN_0.getKey(),
            SontaranVariant.SONTARAN_1.getKey(),
            SontaranVariant.SONTARAN_2.getKey(),
            SontaranVariant.SONTARAN_1.getKey(),
            SontaranVariant.SONTARAN_0.getKey(),
            SontaranVariant.SONTARAN_3.getKey(),
            SontaranVariant.SONTARAN_4.getKey(),
            SontaranVariant.SONTARAN_3.getKey()
    };
    private final NamespacedKey[] framesSontaranTarget = new NamespacedKey[]{
            SontaranVariant.SONTARAN_ATTACKING_0.getKey(),
            SontaranVariant.SONTARAN_ATTACKING_1.getKey(),
            SontaranVariant.SONTARAN_ATTACKING_2.getKey(),
            SontaranVariant.SONTARAN_ATTACKING_1.getKey(),
            SontaranVariant.SONTARAN_ATTACKING_0.getKey(),
            SontaranVariant.SONTARAN_ATTACKING_3.getKey(),
            SontaranVariant.SONTARAN_ATTACKING_4.getKey(),
            SontaranVariant.SONTARAN_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesVashta = new NamespacedKey[]{
            VashtaNeradaVariant.VASHTA_NERADA_0.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_1.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_2.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_1.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_0.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_3.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_4.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_3.getKey()
    };
    private final NamespacedKey[] framesVashtaTarget = new NamespacedKey[]{
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_0.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_1.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_2.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_1.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_0.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_3.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_4.getKey(),
            VashtaNeradaVariant.VASHTA_NERADA_ATTACKING_3.getKey()
    };
    private final NamespacedKey[] framesZygon = new NamespacedKey[]{
            ZygonVariant.ZYGON_0.getKey(),
            ZygonVariant.ZYGON_1.getKey(),
            ZygonVariant.ZYGON_2.getKey(),
            ZygonVariant.ZYGON_1.getKey(),
            ZygonVariant.ZYGON_0.getKey(),
            ZygonVariant.ZYGON_3.getKey(),
            ZygonVariant.ZYGON_4.getKey(),
            ZygonVariant.ZYGON_3.getKey()
    };
    private final NamespacedKey[] framesZygonTarget = new NamespacedKey[]{
            ZygonVariant.ZYGON_ATTACKING_0.getKey(),
            ZygonVariant.ZYGON_ATTACKING_1.getKey(),
            ZygonVariant.ZYGON_ATTACKING_2.getKey(),
            ZygonVariant.ZYGON_ATTACKING_1.getKey(),
            ZygonVariant.ZYGON_ATTACKING_0.getKey(),
            ZygonVariant.ZYGON_ATTACKING_3.getKey(),
            ZygonVariant.ZYGON_ATTACKING_4.getKey(),
            ZygonVariant.ZYGON_ATTACKING_3.getKey()
    };
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
                switch (bukkit.getType()) {
                    case MANGROVE_PROPAGULE -> im.setItemModel(DalekSecVariant.DALEK_SEC_STATIC.getKey());
                    case SUGAR -> im.setItemModel(EmptyChildVariant.EMPTY_CHILD_STATIC.getKey());
                    case TURTLE_EGG -> im.setItemModel(SlitheenVariant.SLITHEEN_STATIC.getKey());
                    case POTATO -> im.setItemModel(SontaranVariant.SONTARAN_STATIC.getKey());
                    case BOOK -> im.setItemModel(VashtaNeradaVariant.VASHTA_NERADA_STATIC.getKey());
                    case PAINTING -> im.setItemModel(ZygonVariant.ZYGON_STATIC.getKey());
                    case HOST_ARMOR_TRIM_SMITHING_TEMPLATE -> im.setItemModel(variant == 1 ? DroidVariant.CLOCKWORK_DROID_FEMALE_STATIC.getKey() : DroidVariant.CLOCKWORK_DROID_STATIC.getKey());
                }
                i = 0;
            } else {
                // play move animation
                boolean hasTarget = getTarget() != null;
                switch (bukkit.getType()) {
                    case MANGROVE_PROPAGULE -> im.setItemModel(hasTarget ? framesCybermanTarget[i] : framesCyberman[i]);
                    case SUGAR -> im.setItemModel(hasTarget ? framesEmptyChildTarget[i] : framesEmptyChild[i]);
                    case TURTLE_EGG -> im.setItemModel(hasTarget ? framesSlitheenTarget[i] : framesSlitheen[i]);
                    case POTATO -> im.setItemModel(hasTarget ? framesSontaranTarget[i] : framesSontaran[i]);
                    case BOOK -> im.setItemModel(hasTarget ? framesVashtaTarget[i] : framesVashta[i]);
                    case PAINTING -> im.setItemModel(hasTarget ? framesZygonTarget[i] : framesZygon[i]);
                    case HOST_ARMOR_TRIM_SMITHING_TEMPLATE -> im.setItemModel(hasTarget ? variant == 1 ? framesDroidFemaleTarget[1] : framesDroidTarget[i] : variant == 1 ? framesDroidFemale[i] : framesDroid[i]);
                }
                i++;
                if (i == framesCyberman.length) {
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
