/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.equip.RemoveEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Equipment;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneEquipment;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.Locale;
import java.util.UUID;

public class DisguiseCommand {

    private final TARDIS plugin;

    public DisguiseCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean disguise(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        // check monster type
        String upper = args[1].toUpperCase(Locale.ROOT);
        Monster monster;
        try {
            monster = Monster.valueOf(upper);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_INVALID");
            return true;
        }
        Player player = null;
        if (sender instanceof ConsoleCommandSender) {
            // check argument length
            if (args.length < 4) {
                plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_UUID");
                return true;
            }
            UUID uuid = UUID.fromString(args[3]);
            player = plugin.getServer().getPlayer(uuid);
        }
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_UUID");
            return true;
        }
        if (args.length < 3 || (!args[2].equalsIgnoreCase("on") && !args[2].equalsIgnoreCase("off"))) {
            plugin.getMessenger().send(player, TardisModule.MONSTERS, "TWA_ON_OFF");
            return true;
        }
        PlayerInventory inv = player.getInventory();
        if (args[2].equalsIgnoreCase("on") && (inv.getBoots() != null || inv.getChestplate() != null || inv.getHelmet() != null || inv.getLeggings() != null)) {
            plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_ARMOUR");
            return true;
        }
        if (args[2].equalsIgnoreCase("on")) {
            switch (monster) {
                case DALEK -> DalekEquipment.set(player, true);
                case K9 -> K9Equipment.set(null, player, true);
                case TOCLAFANE -> ToclafaneEquipment.set(player, true);
                // CYBERMAN, EMPTY_CHILD, HATH, HEADLESS_MONK, ICE_WARRIOR, RACNOSS, SEA_DEVIL, SILENT,
                // SILURIAN, SLITHEEN, SONTARAN, STRAX, MIRE, VASHTA_NERADA, WEEPING_ANGEL, ZYGON
                // JUDOON, OOD
                default -> new Equipper(monster, player, true).setHelmetAndInvisibility();
            }
        } else {
            RemoveEquipment.set(player);
        }
        return true;
    }
}
