package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

import java.util.List;

public class ArmourEquipper {

    public void dress(LivingEntity entity, Monster monster) {
        // helmet
        ItemStack head = new ItemStack(monster.getMaterial());
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setItemModel(monster.getHeadModel());
        EquippableComponent headComponent = headMeta.getEquippable();
        headComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        headComponent.setSlot(EquipmentSlot.HEAD);
        headComponent.setModel(monster.getArmourKey());
        headMeta.setEquippable(headComponent);
        head.setItemMeta(headMeta);
        entity.getEquipment().setHelmet(head);
        // chest
        ItemStack body = new ItemStack(monster.getMaterial());
        ItemMeta bodyMeta = body.getItemMeta();
//        bodyMeta.setItemModel(monster.getHeadModel());
        EquippableComponent bodyComponent = bodyMeta.getEquippable();
        bodyComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        bodyComponent.setSlot(EquipmentSlot.CHEST);
        bodyComponent.setModel(monster.getArmourKey());
        bodyMeta.setEquippable(bodyComponent);
        body.setItemMeta(bodyMeta);
        entity.getEquipment().setChestplate(body);
        // leggings
        ItemStack legs = new ItemStack(monster.getMaterial());
        ItemMeta legsMeta = legs.getItemMeta();
//        legsMeta.setItemModel(monster.getHeadModel());
        EquippableComponent legsComponent = legsMeta.getEquippable();
        legsComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        legsComponent.setSlot(EquipmentSlot.LEGS);
        legsComponent.setModel(monster.getArmourKey());
        legsMeta.setEquippable(legsComponent);
        legs.setItemMeta(legsMeta);
        entity.getEquipment().setLeggings(legs);
    }
}
