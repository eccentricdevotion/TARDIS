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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.FollowerFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class RemoveCommand {

    private final TARDIS plugin;

    public RemoveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean remove(CommandSender sender) {
        if (sender instanceof Player player) {
            UUID uuid = player.getUniqueId();
            // monster equipped armour stands
            ArmorStand stand = (ArmorStand) FollowerFinder.getEntity(player, EntityType.ARMOR_STAND);
            if (stand != null && stand.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER)) {
                stand.remove();
                return true;
            }
            // monster followers - Ood, Judoon, K9
            Husk husk = (Husk) FollowerFinder.getEntity(player, EntityType.HUSK);
            if (husk == null || !husk.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_ENTITY");
                return true;
            } else {
                if (husk.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID) && !TARDISPermission.hasPermission(player, "tardisweepingangels.remove.judoon")) {
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_NO_PERM", "Judoon");
                    return true;
                } else if (husk.getPersistentDataContainer().has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID) && !TARDISPermission.hasPermission(player, "tardisweepingangels.remove.k9")) {
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_NO_PERM", "K9");
                    return true;
                } else if (husk.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID) && !TARDISPermission.hasPermission(player, "tardisweepingangels.remove.ood")) {
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_NO_PERM", "Ood");
                    return true;
                }
                UUID storedUuid = husk.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (storedUuid != null && storedUuid.equals(uuid)) {
                    husk.remove();
                } else {
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_NOT_OWNER");
                }
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "CMD_PLAYER");
        }
        return true;
    }
}
