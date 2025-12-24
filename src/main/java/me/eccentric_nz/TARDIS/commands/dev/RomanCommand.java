package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.ArmourStandEquipment;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class RomanCommand {

    public void equip(Player player, String which) {
        // get the armour stand player is looking at
        Location observerPos = player.getEyeLocation();
        TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
        TARDISVector3D observerStart = new TARDISVector3D(observerPos);
        TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
        ArmorStand as = null;
        // get nearby entities
        for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
            // Bounding box of the given player
            TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
            TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
            TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (target.getType().equals(EntityType.ARMOR_STAND) && TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (as == null || as.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                    as = (ArmorStand) target;
                }
            }
        }
        if (as != null) {
            as.setArms(false);
            as.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER, 1);
            ItemStack head = ItemStack.of(Material.DEAD_BRAIN_CORAL_FAN, 1);
            ItemMeta headMeta = head.getItemMeta();
            headMeta.displayName(Component.text(TARDISStringUtils.capitalise(which) + " Soldier Head"));
            headMeta.setItemModel(new NamespacedKey(TARDIS.plugin, "soldier_" + which + "_static"));
            head.setItemMeta(headMeta);
            ArmourStandEquipment.setHelmetOnly(as, head);
        }
    }
}
