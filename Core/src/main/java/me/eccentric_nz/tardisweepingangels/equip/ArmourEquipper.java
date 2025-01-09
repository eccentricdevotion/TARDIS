package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.custommodels.keys.ArmourVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.CybermanVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.DroidVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.EmptyChildVariant;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.monsters.cybermen.CyberType;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ArmourEquipper {

    public NamespacedKey dress(LivingEntity entity, Monster monster) {
        // helmet
        ItemStack head = new ItemStack(monster.getMaterial());
        ItemMeta headMeta = head.getItemMeta();
        headMeta.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER, 99);
        // get armour key once, so random variants (OOD, CLOCKWORK_DROIDS, CYBERMEN) have the same chestplate and leggings
        NamespacedKey armour = monster.getArmourKey();
        // get head variant (CLOCKWORK_DROIDS, CYBERMEN)
        NamespacedKey headModel;
        switch (monster) {
            case CLOCKWORK_DROID -> headModel = (armour.equals(ArmourVariant.CLOCKWORK_DROID.getKey())) ? monster.getHeadModel() : DroidVariant.CLOCKWORK_DROID_FEMALE_HEAD.getKey();
            case CYBERMAN -> headModel = CyberType.CYBER_HEADS.getOrDefault(armour, CybermanVariant.CYBERMAN_HEAD.getKey());
            default -> headModel = monster.getHeadModel();
        }
        headMeta.setItemModel(headModel);
        EquippableComponent headComponent = headMeta.getEquippable();
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
        headMeta.setDisplayName(ChatColor.WHITE + name);
        head.setItemMeta(headMeta);
        entity.getEquipment().setHelmet(head);
        // chest
        ItemStack body = new ItemStack(monster.getMaterial());
        ItemMeta bodyMeta = body.getItemMeta();
        bodyMeta.setItemModel(ArmourVariant.CHESTPLATE.getKey());
        bodyMeta.setDisplayName(monster.getName() + " Chestplate");
        EquippableComponent bodyComponent = bodyMeta.getEquippable();
        bodyComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        bodyComponent.setSlot(EquipmentSlot.CHEST);
        bodyComponent.setModel(armour);
        bodyMeta.setEquippable(bodyComponent);
        bodyMeta.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER, 99);
        body.setItemMeta(bodyMeta);
        entity.getEquipment().setChestplate(body);
        // leggings
        ItemStack legs = new ItemStack(monster.getMaterial());
        ItemMeta legsMeta = legs.getItemMeta();
        legsMeta.setItemModel(ArmourVariant.LEGGINGS.getKey());
        legsMeta.setDisplayName(monster.getName() + " Leggings");
        EquippableComponent legsComponent = legsMeta.getEquippable();
        legsComponent.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        legsComponent.setSlot(EquipmentSlot.LEGS);
        legsComponent.setModel(armour);
        legsMeta.setEquippable(legsComponent);
        legsMeta.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER, 99);
        legs.setItemMeta(legsMeta);
        entity.getEquipment().setLeggings(legs);
        return armour;
    }
}
