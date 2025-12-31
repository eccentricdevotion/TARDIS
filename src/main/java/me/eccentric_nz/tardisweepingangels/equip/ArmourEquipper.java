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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ArmourEquipper {

    public NamespacedKey dress(LivingEntity entity, Monster monster) {
        // helmet
        ItemStack head = ItemStack.of(monster.getMaterial());
        ItemMeta headMeta = head.getItemMeta();
        headMeta.getPersistentDataContainer().set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99);
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
        headMeta.setItemModel(headModel);
        EquippableComponent headComponent = headMeta.getEquippable();
        headComponent.setDamageOnHurt(false);
        headComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        headComponent.setSlot(EquipmentSlot.HEAD);
        if (monster.equals(Monster.EMPTY_CHILD)) {
            headComponent.setCameraOverlay(EmptyChildVariant.EMPTY_CHILD_OVERLAY.getKey());
        }
        headMeta.setEquippable(headComponent);
        String name = switch (monster) {
            case HEADLESS_MONK -> "Headless Monk Hood";
            case MIRE -> "Mire Helmet";
            default -> monster.getName() + " Head";
        };
        headMeta.displayName(ComponentUtils.toWhite(name));
        head.setItemMeta(headMeta);
        entity.getEquipment().setHelmet(head);
        // chest
        ItemStack body = ItemStack.of(monster.getMaterial());
        ItemMeta bodyMeta = body.getItemMeta();
        bodyMeta.setItemModel(ArmourVariant.CHESTPLATE.getKey());
        bodyMeta.displayName(Component.text(monster.getName() + " Chestplate"));
        EquippableComponent bodyComponent = bodyMeta.getEquippable();
        bodyComponent.setDamageOnHurt(false);
        bodyComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        bodyComponent.setSlot(EquipmentSlot.CHEST);
        bodyComponent.setModel(armour);
        bodyMeta.setEquippable(bodyComponent);
        bodyMeta.getPersistentDataContainer().set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99);
        body.setItemMeta(bodyMeta);
        entity.getEquipment().setChestplate(body);
        // leggings
        ItemStack legs = ItemStack.of(monster.getMaterial());
        ItemMeta legsMeta = legs.getItemMeta();
        legsMeta.setItemModel(ArmourVariant.LEGGINGS.getKey());
        legsMeta.displayName(Component.text(monster.getName() + " Leggings"));
        EquippableComponent legsComponent = legsMeta.getEquippable();
        legsComponent.setDamageOnHurt(false);
        legsComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        legsComponent.setSlot(EquipmentSlot.LEGS);
        legsComponent.setModel(armour);
        legsMeta.setEquippable(legsComponent);
        legsMeta.getPersistentDataContainer().set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99);
        legs.setItemMeta(legsMeta);
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
