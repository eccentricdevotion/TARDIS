package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisweepingangels.equip.ArmourStandEquipment;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

public class RomanCommand {

    public void equip(Player player, String which) {
        // get the armour stand player is looking at
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.ARMOR_STAND);
        if (result == null) {
            TARDIS.plugin.getMessenger().send(player, TardisModule.TARDIS, "WA_STAND");
            return;
        }
        ArmorStand as = (ArmorStand) result.getHitEntity();
        if (as != null) {
            as.setArms(false);
            as.getPersistentDataContainer().set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 1);
            ItemStack head = ItemStack.of(Material.DEAD_BRAIN_CORAL_FAN, 1);
            ItemMeta headMeta = head.getItemMeta();
            headMeta.displayName(Component.text(TARDISStringUtils.capitalise(which) + " Soldier Head"));
            headMeta.setItemModel(new NamespacedKey(TARDIS.plugin, "soldier_" + which + "_static"));
            head.setItemMeta(headMeta);
            ArmourStandEquipment.setHelmetOnly(as, head);
        }
    }
}
