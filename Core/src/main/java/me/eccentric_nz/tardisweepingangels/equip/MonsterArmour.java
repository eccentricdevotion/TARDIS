package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

import java.util.List;

public class MonsterArmour {

    public static ItemStack makeEquippable(Monster monster, EquipmentSlot slot, NamespacedKey key) {
        ItemStack armour = new ItemStack(monster.getMaterial(), 1);
        ItemMeta meta = armour.getItemMeta();
        meta.setDisplayName(monster.getName() + " " + TARDISStringUtils.uppercaseFirst(slot.name()));
        meta.setCustomModelData(3000 + slot.ordinal());
        EquippableComponent equippable = meta.getEquippable();
        equippable.setAllowedEntities(List.of(monster.getEntityType(), EntityType.PLAYER));
        equippable.setSlot(slot);
        equippable.setModel(key);
        meta.setEquippable(equippable);
        armour.setItemMeta(meta);
        return armour;
    }
}
