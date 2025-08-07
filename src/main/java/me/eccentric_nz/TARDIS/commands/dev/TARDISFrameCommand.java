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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author eccentric_nz
 */
public class TARDISFrameCommand {

    private final TARDIS plugin;

    public TARDISFrameCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static ItemFrame getItemFrame(Player player) {
        ItemFrame frame = null;
        // get the item frame player is looking at
        Location observerPos = player.getEyeLocation();
        TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
        TARDISVector3D observerStart = new TARDISVector3D(observerPos);
        TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
        // Get nearby entities
        for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
            // Bounding box of the given player
            TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
            TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
            TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (target.getType().equals(EntityType.ITEM_FRAME) && TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
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
