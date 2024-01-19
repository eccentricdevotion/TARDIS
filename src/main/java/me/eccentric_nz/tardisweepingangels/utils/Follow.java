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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Follow {

    public static void toggle(TARDIS plugin, Player player, Entity husk, String which, boolean follow) {
        if (!TARDISPermission.hasPermission(player, "tardisweepingangels.follow." + which.toLowerCase())) {
            plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_PERM_FOLLOW", which);
            return;
        }
        if (husk.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
            UUID uuid = player.getUniqueId();
            UUID huskId = husk.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
            if (uuid.equals(huskId)) {
                TWAFollower follower = (TWAFollower) ((CraftEntity) husk).getHandle();
                follower.setFollowing(follow);
                plugin.getMessenger().send(player, TardisModule.MONSTERS, (follow) ? "WA_FOLLOWING_START" : "WA_FOLLOWING_END", which);
            } else {
                plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_NOT_YOURS", which);
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_BROKEN", which);
        }
    }
}
