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
package me.eccentric_nz.tardisweepingangels.commands;

import java.util.UUID;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.ArmourStandFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class RemoveCommand {

    private final TARDISWeepingAngels plugin;

    public RemoveCommand(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    public boolean remove(CommandSender sender) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            sender.sendMessage(plugin.pluginName + "Command can only be used by a player!");
            return true;
        }
        UUID uuid = player.getUniqueId();
        if (plugin.getFollowTasks().containsKey(uuid)) {
            player.sendMessage(plugin.pluginName + "Please tell your follower to stay before removing it! /twa stay");
            return true;
        }
        ArmorStand stand = ArmourStandFinder.getStand(player);
        if (stand == null || (!stand.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && !stand.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER))) {
            player.sendMessage(plugin.pluginName + "You are not looking at a TARDISWeepingAngels entity!");
            return true;
        } else {
            if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER) && !player.hasPermission("tardisweepingangels.remove.judoon")) {
                player.sendMessage(plugin.pluginName + "You don't have permission to remove a Judoon!");
                return true;
            } else if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER) && !player.hasPermission("tardisweepingangels.remove.k9")) {
                player.sendMessage(plugin.pluginName + "You don't have permission to remove K9!");
                return true;
            } else if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER) && !player.hasPermission("tardisweepingangels.remove.ood")) {
                player.sendMessage(plugin.pluginName + "You don't have permission to remove an Ood!");
                return true;
            }
            UUID storedUuid = stand.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
            if ((storedUuid != null && storedUuid.equals(uuid)) || stand.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER)) {
                stand.remove();
            } else {
                player.sendMessage(plugin.pluginName + "That is not your TARDISWeepingAngels entity!");
            }
        }
        return true;
    }
}
