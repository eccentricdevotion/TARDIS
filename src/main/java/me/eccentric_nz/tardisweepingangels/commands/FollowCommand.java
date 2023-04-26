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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonFollow;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Follow;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodFollow;
import me.eccentric_nz.tardisweepingangels.utils.ArmourStandFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FollowCommand {

    private final TARDIS plugin;

    public FollowCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean follow(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (TARDISWeepingAngels.getFollowTasks().containsKey(player.getUniqueId())) {
                TARDISMessage.send(player, TardisModule.MONSTERS, "WA_FOLLOWING");
                return true;
            }
            // get the armour stand
            ArmorStand stand = ArmourStandFinder.getStand(player);
            if (stand == null) {
                TARDISMessage.send(player, TardisModule.MONSTERS, "WA_NOT_LOOKING");
                return true;
            }
            PersistentDataContainer pdc = stand.getPersistentDataContainer();
            if (pdc.has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER)) {
                OodFollow.run(plugin, player, stand, args);
            } else if (pdc.has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER)) {
                JudoonFollow.run(plugin, player, stand, args);
            } else if (pdc.has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER)) {
                K9Follow.run(plugin, player, stand, args);
            } else {
                TARDISMessage.send(player, TardisModule.MONSTERS, "WA_NOT_LOOKING");
            }
        } else {
            TARDISMessage.send(sender, TardisModule.MONSTERS, "CMD_PLAYER");
        }
        return true;
    }
}
