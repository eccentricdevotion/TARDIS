/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.messaging.TableGenerator.Alignment;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISUpdateLister {

    private final Player player;
    private final TableGenerator tg;

    public TARDISUpdateLister(Player player) {
        this.player = player;
        tg = new TableGenerator(Alignment.LEFT, Alignment.LEFT);
    }

    public void list() {
        TARDISMessage.send(player, "UPDATE_INFO");
        for (String line : createUpdateOptions()) {
            player.sendMessage(line);
        }
    }

    private List<String> createUpdateOptions() {
        tg.addRow(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Command argument", ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Description");
        tg.addRow();
        tg.addRow("TARDIS Controls", "");
        tg.addRow(ChatColor.GREEN + "artron", ChatColor.DARK_GREEN + "Artron Energy Capacitor button");
        tg.addRow(ChatColor.GREEN + "back", ChatColor.DARK_GREEN + "Previous Location button");
        tg.addRow(ChatColor.GREEN + "button", ChatColor.DARK_GREEN + "Random Location button");
        tg.addRow(ChatColor.GREEN + "handbrake", ChatColor.DARK_GREEN + "Handbrake");
        tg.addRow(ChatColor.GREEN + "world-repeater", ChatColor.DARK_GREEN + "World Type selector");
        tg.addRow(ChatColor.GREEN + "x-repeater", ChatColor.DARK_GREEN + "Random x coordinate setter");
        tg.addRow(ChatColor.GREEN + "y-repeater", ChatColor.DARK_GREEN + "Distance multipler");
        tg.addRow(ChatColor.GREEN + "z-repeater", ChatColor.DARK_GREEN + "Random z coordinate setter");
        tg.addRow(ChatColor.GREEN + "control", ChatColor.DARK_GREEN + "TARDIS Control Menu");
        tg.addRow(ChatColor.GREEN + "flight", ChatColor.DARK_GREEN + "TARDIS Flight Mode button");
        tg.addRow();
        tg.addRow("User Interfaces", "");
        tg.addRow(ChatColor.RED + "advanced", ChatColor.DARK_RED + "TARDIS Advanced Console");
        tg.addRow(ChatColor.RED + "ars", ChatColor.DARK_RED + "Architectural Reconfiguration System");
        tg.addRow(ChatColor.RED + "chameleon", ChatColor.DARK_RED + "Chameleon Circuit");
        tg.addRow(ChatColor.RED + "direction", ChatColor.DARK_RED + "Direction Item Frame");
        tg.addRow(ChatColor.RED + "info", ChatColor.DARK_RED + "TARDIS Information System");
        tg.addRow(ChatColor.RED + "save-sign", ChatColor.DARK_RED + "Saved locations and TARDIS areas");
        tg.addRow(ChatColor.RED + "telepathic", ChatColor.DARK_RED + "Telepathic Circuit");
        tg.addRow(ChatColor.RED + "temporal", ChatColor.DARK_RED + "Temporal Relocator");
        tg.addRow(ChatColor.RED + "terminal", ChatColor.DARK_RED + "Destination Terminal");
        tg.addRow(ChatColor.RED + "storage", ChatColor.DARK_RED + "Disk Storage Container");
        tg.addRow(ChatColor.RED + "generator", ChatColor.DARK_RED + "Sonic Generator");
        tg.addRow();
        tg.addRow("Spawn Locations", "");
        tg.addRow(ChatColor.BLUE + "creeper", ChatColor.DARK_BLUE + "Artron Charged Creeper");
        tg.addRow(ChatColor.BLUE + "eps", ChatColor.DARK_BLUE + "Emergency Programme One");
        tg.addRow(ChatColor.BLUE + "farm", ChatColor.DARK_BLUE + "Farm room");
        tg.addRow(ChatColor.BLUE + "rail", ChatColor.DARK_BLUE + "Rail room");
        tg.addRow(ChatColor.BLUE + "stable", ChatColor.DARK_BLUE + "Stable room");
        tg.addRow(ChatColor.BLUE + "stall", ChatColor.DARK_BLUE + "Llama stall room");
        tg.addRow(ChatColor.BLUE + "vault", ChatColor.DARK_BLUE + "Vault room drop chest");
        tg.addRow(ChatColor.BLUE + "village", ChatColor.DARK_BLUE + "Village room");
        tg.addRow(ChatColor.BLUE + "igloo", ChatColor.DARK_BLUE + "Igloo room");
        tg.addRow(ChatColor.BLUE + "beacon", ChatColor.DARK_BLUE + "Beacon toggle block");
        tg.addRow();
        tg.addRow("Others", "");
        tg.addRow(ChatColor.LIGHT_PURPLE + "condenser", ChatColor.DARK_PURPLE + "Artron Energy Condenser");
        tg.addRow(ChatColor.LIGHT_PURPLE + "door", ChatColor.DARK_PURPLE + "TARDIS Interior Door");
        tg.addRow(ChatColor.LIGHT_PURPLE + "backdoor", ChatColor.DARK_PURPLE + "TARDIS back doors");
        tg.addRow(ChatColor.LIGHT_PURPLE + "keyboard", ChatColor.DARK_PURPLE + "Keyboard Input sign");
        tg.addRow(ChatColor.LIGHT_PURPLE + "scanner", ChatColor.DARK_PURPLE + "Exterior Scanner button");
        tg.addRow(ChatColor.LIGHT_PURPLE + "map", ChatColor.DARK_PURPLE + "TARDIS Scanner Map");
        tg.addRow(ChatColor.LIGHT_PURPLE + "light", ChatColor.DARK_PURPLE + "Console Light switch");
        tg.addRow(ChatColor.LIGHT_PURPLE + "toggle_wool", ChatColor.DARK_PURPLE + "Toggle Black Wool behind door");
        tg.addRow(ChatColor.LIGHT_PURPLE + "zero", ChatColor.DARK_PURPLE + "Zero room transmat button");
        tg.addRow(ChatColor.LIGHT_PURPLE + "frame", ChatColor.DARK_PURPLE + "Chameleon Item Frame");
        tg.addRow(ChatColor.LIGHT_PURPLE + "dispenser", ChatColor.DARK_PURPLE + "Custard Cream Dispenser");
        tg.addRow(ChatColor.LIGHT_PURPLE + "forcefield", ChatColor.DARK_PURPLE + "TARDIS Force Field button");
        tg.addRow(ChatColor.LIGHT_PURPLE + "bell", ChatColor.DARK_PURPLE + "TARDIS Cloister bell button");
        tg.addRow(ChatColor.LIGHT_PURPLE + "rotor", ChatColor.DARK_PURPLE + "Time Rotor item frame");
        return tg.generate(TableGenerator.Receiver.CLIENT, true, true);
    }
}
