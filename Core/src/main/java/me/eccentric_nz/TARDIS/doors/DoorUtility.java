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
package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Map;

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
        if (!im.hasDisplayName() || !im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.STRING) || !im.hasItemModel()) {
            return;
        }
        // set an Interaction
        TARDISDisplayItemUtils.set(location, im.getItemModel().getKey(), true);
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

    public static void debugPortal(String block) {
        TARDIS.plugin.debug("Failed to remove portal entry!");
        TARDIS.plugin.debug(block);
        TARDIS.plugin.debug("Current stored locations:");
        // get open portals
        for (Map.Entry<Location, TARDISTeleportLocation> map : TARDIS.plugin.getTrackerKeeper().getPortals().entrySet()) {
            // only portals in police box worlds
            if (map.getKey().getWorld().getName().contains("TARDIS")) {
                continue;
            }
            if (map.getValue().isAbandoned()) {
                continue;
            }
            TARDIS.plugin.debug(map.getKey().toString());
        }
    }

    public static void playDoorSound(Player player, boolean open, Location location, boolean minecart) {
        if (open) {
            if (!minecart) {
                TARDISSounds.playTARDISSound(location, "tardis_door_close");
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0F, 1.0F);
            }
        } else if (!minecart) {
            TARDISSounds.playTARDISSound(location, "tardis_door_open");
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
        }
    }
}
