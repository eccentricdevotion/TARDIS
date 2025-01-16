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
package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.utils.Follow;
import me.eccentric_nz.tardisweepingangels.utils.FollowerFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

public class FollowCommand {

    private final TARDIS plugin;

    public FollowCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean follow(CommandSender sender) {
        if (sender instanceof Player player) {
            // get the entity the player is looking at
            Entity husk = FollowerFinder.getEntity(player, EntityType.HUSK);
            if (husk == null) {
                plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_NOT_LOOKING");
                return true;
            }
            // check if monster is already following
            TWAFollower follower = (TWAFollower) ((CraftEntity) husk).getHandle();
            if (follower.isFollowing()) {
                plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_FOLLOWING");
                return true;
            }
            // set following status
            PersistentDataContainer pdc = husk.getPersistentDataContainer();
            if (pdc.has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                Follow.toggle(plugin, player, (Husk) husk, "Ood", true);
            } else if (pdc.has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                Follow.toggle(plugin, player, (Husk) husk, "Judoon", true);
            } else if (pdc.has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                Follow.toggle(plugin, player, (Husk) husk, "K9", true);
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "CMD_PLAYER");
        }
        return true;
    }
}
