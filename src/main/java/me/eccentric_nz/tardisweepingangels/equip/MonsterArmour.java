/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistrySet;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ArmourVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class MonsterArmour {

    public static ItemStack makeEquippable(Monster monster, EquipmentSlot slot) {
        NamespacedKey key = TARDISWeepingAngels.PDC_KEYS.get(monster);
        TARDIS.plugin.debug(key.toString());
        ItemStack armour = ItemStack.of(monster.getMaterial(), 1);
        armour.setData(DataComponentTypes.CUSTOM_NAME, Component.text(monster.getName() + " " + TARDISStringUtils.uppercaseFirst(slot.name())));
        armour.setData(DataComponentTypes.ITEM_MODEL, slot == EquipmentSlot.CHEST ? ArmourVariant.CHESTPLATE.getKey() : ArmourVariant.LEGGINGS.getKey());
        armour.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(slot)
                .allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                        TypedKey.create(RegistryKey.ENTITY_TYPE, monster.getEntityType().getKey()),
                        TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
                ))
                .assetId(key)
                .build());
        return armour;
    }
}
