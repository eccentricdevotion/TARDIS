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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.UUID;

public class TARDISSonicFreeze {

    public static Player getTargetPlayer(Player player) {
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.PLAYER);
        return result != null ? (Player) result.getHitEntity() : null;
    }

    public static void immobilise(TARDIS plugin, Player player, Player target) {
        // freeze the closest player
        long cool = System.currentTimeMillis();
        if ((!plugin.getTrackerKeeper().getCooldown().containsKey(player.getUniqueId()) || plugin.getTrackerKeeper().getCooldown().get(player.getUniqueId()) < cool)) {
            plugin.getTrackerKeeper().getCooldown().put(player.getUniqueId(), cool + (plugin.getConfig().getInt("sonic.freeze_cooldown") * 1000L));
            plugin.getMessenger().send(target, TardisModule.TARDIS, "FREEZE", player.getName());
            UUID uuid = target.getUniqueId();
            plugin.getTrackerKeeper().getFrozenPlayers().add(uuid);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getFrozenPlayers().remove(uuid), 100L);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FREEZE_NO");
        }
    }
}
