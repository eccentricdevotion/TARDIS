/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.monsters.k9;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.UUID;

public class K9Follow {

    public static boolean run(TARDIS plugin, Player player, ArmorStand stand, String[] args) {
        if (!TARDISPermission.hasPermission(player, "tardisweepingangels.follow.k9")) {
            TARDISMessage.send(player, MODULE.MONSTERS, "WA_PERM_FOLLOW", "K9");
            return true;
        }
        if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
            UUID uuid = player.getUniqueId();
            UUID k9Id = stand.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
            if (k9Id.equals(uuid)) {
                double speed = (args.length == 2) ? Math.min(Double.parseDouble(args[1]) / 100.0d, 0.5d) : 0.15d;
                int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new K9WalkRunnable(stand, speed, player), 2L, 2L);
                TARDISWeepingAngels.getFollowTasks().put(uuid, taskId);
            } else {
                TARDISMessage.send(player, MODULE.MONSTERS, "WA_NOT_YOURS", "K9");
            }
        } else {
            TARDISMessage.send(player, MODULE.MONSTERS, "WA_BROKEN", "K9");
        }
        return true;
    }
}
