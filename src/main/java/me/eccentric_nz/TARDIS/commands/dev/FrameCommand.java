/*
 * Copyright (C) 2026 eccentric_nz
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
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

/**
 * @author eccentric_nz
 */
public class FrameCommand {

    private final TARDIS plugin;

    public FrameCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static ItemFrame getItemFrame(Player player) {
        ItemFrame frame = null;
        // get the item frame player is looking at
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.ITEM_FRAME);
        return result != null ? (ItemFrame) result.getHitEntity() : null;
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
