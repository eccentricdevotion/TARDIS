package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class DoorUtility {

    /**
     * Spawn an Item Display entity
     *
     * @param plugin   a TARDIS plugin instance
     * @param player   the player placing the door
     * @param location the location to spawn the entity at
     */
    public static void set(TARDIS plugin, Player player, Location location) {
        ItemStack is = player.getInventory().getItemInMainHand();
        ItemStack single = is.clone();
        single.setAmount(1);
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
            return;
        }
        // set an Interaction
        TARDISDisplayItemUtils.set(location, 10000, true);
        // set an ItemDisplay
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.add(0.5d, 0.0d, 0.5d), EntityType.ITEM_DISPLAY);
        display.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 10000);
        display.setItemStack(single);
        display.setPersistent(true);
        display.setInvulnerable(true);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        float yaw = getLookAtYaw(player);
        // set display rotation
        display.setRotation(yaw, 0);
    }

    public static float getLookAtYaw(Player player) {
        Vector motion = player.getEyeLocation().getDirection().multiply(-1);
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0;
        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        double y = (-yaw * 180 / Math.PI);
        return (float) Math.round(y / 90) * 90;
    }
}
