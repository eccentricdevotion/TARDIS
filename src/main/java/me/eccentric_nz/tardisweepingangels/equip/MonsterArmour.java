/*
 * Copyright (C) 2025 eccentric_nz
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

import java.util.List;

public class MonsterArmour {

    public static ItemStack makeEquippable(Monster monster, EquipmentSlot slot) {
        NamespacedKey key = TARDISWeepingAngels.PDC_KEYS.get(monster);
        TARDIS.plugin.debug(key.toString());
        ItemStack armour = ItemStack.of(monster.getMaterial(), 1);
        ItemMeta meta = armour.getItemMeta();
        meta.displayName(Component.text(monster.getName() + " " + TARDISStringUtils.uppercaseFirst(slot.name())));
        meta.setItemModel(slot == EquipmentSlot.CHEST ? ArmourVariant.CHESTPLATE.getKey() : ArmourVariant.LEGGINGS.getKey());
        EquippableComponent equippable = meta.getEquippable();
        equippable.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        equippable.setSlot(slot);
        equippable.setModel(key);
        meta.setEquippable(equippable);
        armour.setItemMeta(meta);
        return armour;
    }
}
