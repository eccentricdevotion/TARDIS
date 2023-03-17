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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.ArmourStandFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class RemoveCommand {

    private final TARDIS plugin;

    public RemoveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean remove(CommandSender sender) {
        if (sender instanceof Player player) {
            UUID uuid = player.getUniqueId();
            if (TARDISWeepingAngels.getFollowTasks().containsKey(uuid)) {
                TARDISMessage.send(player, MODULE.MONSTERS, "WA_STAY");
                return true;
            }
            ArmorStand stand = ArmourStandFinder.getStand(player);
            if (stand == null || (!stand.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && !stand.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER))) {
                TARDISMessage.send(player, MODULE.MONSTERS, "WA_ENTITY");
                return true;
            } else {
                if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER) && !player.hasPermission("tardisweepingangels.remove.judoon")) {
                    TARDISMessage.send(player, MODULE.MONSTERS, "WA_NO_PERM", "Judoon");
                    return true;
                } else if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER) && !player.hasPermission("tardisweepingangels.remove.k9")) {
                    TARDISMessage.send(player, MODULE.MONSTERS, "WA_NO_PERM", "K9");
                    return true;
                } else if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER) && !player.hasPermission("tardisweepingangels.remove.ood")) {
                    TARDISMessage.send(player, MODULE.MONSTERS, "WA_NO_PERM", "Ood");
                    return true;
                }
                UUID storedUuid = stand.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if ((storedUuid != null && storedUuid.equals(uuid)) || stand.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER)) {
                    stand.remove();
                } else {
                    TARDISMessage.send(player, MODULE.MONSTERS, "WA_NOT_OWNER");
                }
            }
        } else {
            TARDISMessage.send(sender, MODULE.MONSTERS, "CMD_PLAYER");
        }
        return true;
    }
}
