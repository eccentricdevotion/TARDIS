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
import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardisweepingangels.monsters.cybermen.CyberType;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ArmourEquipper {

    public NamespacedKey dress(LivingEntity entity, Monster monster) {
        // helmet
        ItemStack head = ItemStack.of(monster.getMaterial());
        head.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        // get armour key once, so random variants (OOD, CLOCKWORK_DROIDS, CYBERMEN) have the same chestplate and leggings
        NamespacedKey armour = monster.getArmourKey();
        // get head variant (CLOCKWORK_DROIDS, CYBERMEN)
        NamespacedKey headModel;
        switch (monster) {
            case CLOCKWORK_DROID -> headModel = (armour.equals(ArmourVariant.CLOCKWORK_DROID.getKey())) ? monster.getHeadModel() : DroidVariant.CLOCKWORK_DROID_FEMALE_HEAD.getKey();
            case CYBERMAN -> headModel = CyberType.CYBER_HEADS.getOrDefault(armour, CybermanVariant.CYBERMAN_HEAD.getKey());
            case DAVROS -> headModel = DavrosVariant.DAVROS.getKey();
            default -> headModel = monster.getHeadModel();
        }
        head.setData(DataComponentTypes.ITEM_MODEL, headModel);
        Equippable.Builder equippable = Equippable.equippable(EquipmentSlot.HEAD);
        equippable.damageOnHurt(false);
        equippable.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, monster.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        if (monster.equals(Monster.EMPTY_CHILD)) {
            equippable.cameraOverlay(EmptyChildVariant.EMPTY_CHILD_OVERLAY.getKey());
        }
        head.setData(DataComponentTypes.EQUIPPABLE, equippable.build());
        String name = switch (monster) {
            case HEADLESS_MONK -> "Headless Monk Hood";
            case MIRE -> "Mire Helmet";
            default -> monster.getName() + " Head";
        };
        head.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(name));
        entity.getEquipment().setHelmet(head);
        // chest
        ItemStack body = ItemStack.of(monster.getMaterial());
        body.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.CHESTPLATE.getKey());
        body.setData(DataComponentTypes.CUSTOM_NAME, Component.text(monster.getName() + " Chestplate"));
        Equippable.Builder bodyBuilder = Equippable.equippable(EquipmentSlot.CHEST);
        bodyBuilder.damageOnHurt(false);
        bodyBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, monster.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        bodyBuilder.assetId(armour);
        body.setData(DataComponentTypes.EQUIPPABLE, bodyBuilder.build());
        body.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        entity.getEquipment().setChestplate(body);
        // leggings
        ItemStack legs = ItemStack.of(monster.getMaterial());
        legs.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.LEGGINGS.getKey());
        legs.setData(DataComponentTypes.CUSTOM_NAME, Component.text(monster.getName() + " Leggings"));
        Equippable.Builder legsBuilder = Equippable.equippable(EquipmentSlot.LEGS);
        legsBuilder.damageOnHurt(false);
        legsBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, monster.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        legsBuilder.assetId(armour);
        legs.setData(DataComponentTypes.EQUIPPABLE, legsBuilder.build());
        legs.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        entity.getEquipment().setLeggings(legs);
        if (!(entity instanceof Player)) {
            // give monster extra health
            AttributeInstance attribute = entity.getAttribute(Attribute.MAX_HEALTH);
            attribute.setBaseValue(24.0d);
            entity.setHealth(24.0d);
        }
        return armour;
    }
}
