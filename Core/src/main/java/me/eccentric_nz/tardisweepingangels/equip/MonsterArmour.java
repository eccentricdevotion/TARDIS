package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ArmourVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
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
        ItemStack armour = new ItemStack(monster.getMaterial(), 1);
        ItemMeta meta = armour.getItemMeta();
        meta.setDisplayName(monster.getName() + " " + TARDISStringUtils.uppercaseFirst(slot.name()));
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
