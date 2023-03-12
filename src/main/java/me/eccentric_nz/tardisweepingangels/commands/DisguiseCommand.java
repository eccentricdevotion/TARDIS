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
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.equip.RemoveEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Equipment;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneEquipment;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class DisguiseCommand {

    private final TARDISWeepingAngels plugin;

    public DisguiseCommand(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    public boolean disguise(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        // check monster type
        String upper = args[1].toUpperCase();
        Monster monster;
        try {
            monster = Monster.valueOf(upper);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(plugin.pluginName + "Invalid monster type!");
            return true;
        }
        Player player = null;
       if (sender instanceof ConsoleCommandSender) {
            // check argument length
            if (args.length < 4) {
                sender.sendMessage(plugin.pluginName + "You must supply a player UUID when using this command from the console!");
                return true;
            }
            UUID uuid = UUID.fromString(args[3]);
            player = plugin.getServer().getPlayer(uuid);
        }
        if (sender instanceof Player) {
            player = (Player) sender;
        }
         if (player == null) {
            sender.sendMessage(plugin.pluginName + "Command can only be used by a player, or a player UUID must be supplied!");
            return true;
        }
        if (args.length < 3 || (!args[2].equalsIgnoreCase("on") && !args[2].equalsIgnoreCase("off"))) {
            player.sendMessage(plugin.pluginName + "You need to specify if the disguise should be on or off!");
            return true;
        }
        PlayerInventory inv = player.getInventory();
        if (args[2].equalsIgnoreCase("on") && (inv.getBoots() != null || inv.getChestplate() != null || inv.getHelmet() != null || inv.getLeggings() != null)) {
            player.sendMessage(plugin.pluginName + "Your armour slots must be empty before using this command!");
            return true;
        }
        if (args[2].equalsIgnoreCase("on")) {
            switch (monster) {
                case DALEK -> DalekEquipment.set(player, true);
                case JUDOON -> JudoonEquipment.set(null, player, true);
                case K9 -> K9Equipment.set(null, player, true);
                case OOD -> OodEquipment.set(null, player, true);
                case TOCLAFANE -> ToclafaneEquipment.set(player, true);
                // CYBERMAN, EMPTY_CHILD, HATH, HEADLESS_MONK, ICE_WARRIOR, SEA_DEVIL, SILENT,
                // SILURIAN, SLITHEEN, SONTARAN, STRAX, MIRE, VASHTA_NERADA, WEEPING_ANGEL, ZYGON
                default -> new Equipper(monster, player, true, false).setHelmetAndInvisibilty();
            }
        } else {
            RemoveEquipment.set(player);
        }
        return true;
    }
}
