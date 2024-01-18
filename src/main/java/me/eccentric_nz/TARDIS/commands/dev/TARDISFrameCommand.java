/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.monsters.weeping_angels.Blink;
import me.eccentric_nz.tardisweepingangels.utils.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

/**
 *
 * @author eccentric_nz
 */
public class TARDISFrameCommand {

    private final TARDIS plugin;

    public TARDISFrameCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static ItemFrame getItemFrame(Player player) {
        ItemFrame frame = null;
        // get the armour stand player is looking at
        Location observerPos = player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());
        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(16));
        // Get nearby entities
        for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
            // Bounding box of the given player
            Vector3D targetPos = new Vector3D(target.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (target.getType().equals(EntityType.ITEM_FRAME) && Blink.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (frame == null || frame.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                    return (ItemFrame) target;
                }
            }
        }
        return null;
    }

    public boolean toggle(Player player, boolean lock, boolean rotor) {
        // get the item frame the player is targeting
        ItemFrame frame = getItemFrame(player);
        if (frame != null) {
            frame.setFixed(lock);
            frame.setVisible(!lock);
            if (lock && !rotor) {
                frame.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
            } else {
                frame.getPersistentDataContainer().remove(plugin.getCustomBlockKey());
            }
            plugin.getMessenger().message(player, "ItemFrame " + ((lock) ? "locked" : "unlocked") + "!");
        } else {
            plugin.getMessenger().message(player, "You are not looking at an ItemFrame!");
        }
        return true;
    }
}
