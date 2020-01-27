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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISUpdateLister {

    private final Player player;
    private final LinkedHashMap<String, List<String>> options;

    TARDISUpdateLister(Player player) {
        this.player = player;
        options = createUpdateOptions();
    }

    public void list() {
        TARDISMessage.send(player, "UPDATE_INFO");
        player.sendMessage(ChatColor.GRAY + "    Command argument" + ChatColor.RESET + " - " + ChatColor.DARK_GRAY + "Description");
        options.forEach((key, value) -> {
            player.sendMessage(key);
            value.forEach((s) -> player.sendMessage("    " + s));
        });
    }

    private LinkedHashMap<String, List<String>> createUpdateOptions() {
        LinkedHashMap<String, List<String>> update_options = new LinkedHashMap<>();
        List<String> controls = new ArrayList<>();
        controls.add(ChatColor.GREEN + "artron" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Artron Energy Capacitor button");
        controls.add(ChatColor.GREEN + "back" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Previous Location button");
        controls.add(ChatColor.GREEN + "button" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Random Location button");
        controls.add(ChatColor.GREEN + "handbrake" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Handbrake");
        controls.add(ChatColor.GREEN + "world-repeater" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "World Type selector");
        controls.add(ChatColor.GREEN + "x-repeater" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Random x coordinate setter");
        controls.add(ChatColor.GREEN + "y-repeater" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Distance multipler");
        controls.add(ChatColor.GREEN + "z-repeater" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "Random z coordinate setter");
        controls.add(ChatColor.GREEN + "control" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Control Menu");
        controls.add(ChatColor.GREEN + "flight" + ChatColor.RESET + " - " + ChatColor.DARK_GREEN + "TARDIS Flight Mode button");
        update_options.put("TARDIS Controls", controls);
        List<String> guis = new ArrayList<>();
        guis.add(ChatColor.RED + "advanced" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "TARDIS Advanced Console");
        guis.add(ChatColor.RED + "ars" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Architectural Reconfiguration System");
        guis.add(ChatColor.RED + "chameleon" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Chameleon Circuit");
        guis.add(ChatColor.RED + "direction" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Direction Item Frame");
        guis.add(ChatColor.RED + "info" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "TARDIS Information System");
        guis.add(ChatColor.RED + "save-sign" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Saved locations and TARDIS areas");
        guis.add(ChatColor.RED + "telepathic" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Telepathic Circuit");
        guis.add(ChatColor.RED + "temporal" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Temporal Relocator");
        guis.add(ChatColor.RED + "terminal" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Destination Terminal");
        guis.add(ChatColor.RED + "storage" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Disk Storage Container");
        guis.add(ChatColor.RED + "generator" + ChatColor.RESET + " - " + ChatColor.DARK_RED + "Sonic Generator");
        update_options.put("TARDIS User Interfaces", guis);
        List<String> spawns = new ArrayList<>();
        spawns.add(ChatColor.BLUE + "creeper" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Artron Charged Creeper");
        spawns.add(ChatColor.BLUE + "eps" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Emergency Programme One");
        spawns.add(ChatColor.BLUE + "farm" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Farm room");
        spawns.add(ChatColor.BLUE + "rail" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Rail room");
        spawns.add(ChatColor.BLUE + "stable" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Stable room");
        spawns.add(ChatColor.BLUE + "stall" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Llama stall room");
        spawns.add(ChatColor.BLUE + "vault" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Vault room drop chest");
        spawns.add(ChatColor.BLUE + "village" + ChatColor.RESET + " - " + ChatColor.DARK_BLUE + "Village room");
        update_options.put("TARDIS Internal Spawn Locations", spawns);
        List<String> misc = new ArrayList<>();
        misc.add(ChatColor.LIGHT_PURPLE + "condenser" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Artron Energy Condenser");
        misc.add(ChatColor.LIGHT_PURPLE + "door" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "TARDIS Interior Door");
        misc.add(ChatColor.LIGHT_PURPLE + "backdoor" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "TARDIS back doors");
        misc.add(ChatColor.LIGHT_PURPLE + "keyboard" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Keyboard Input sign");
        misc.add(ChatColor.LIGHT_PURPLE + "scanner" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Exterior Scanner button");
        misc.add(ChatColor.LIGHT_PURPLE + "light" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Console Light switch");
        misc.add(ChatColor.LIGHT_PURPLE + "toggle_wool" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Toggle Black Wool behind door");
        misc.add(ChatColor.LIGHT_PURPLE + "zero" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Zero room transmat button");
        misc.add(ChatColor.LIGHT_PURPLE + "beacon" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Beacon toggle block");
        misc.add(ChatColor.LIGHT_PURPLE + "frame" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Chameleon Item Frame");
        misc.add(ChatColor.LIGHT_PURPLE + "dispenser" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "Custard Cream Dispenser");
        misc.add(ChatColor.LIGHT_PURPLE + "forcefield" + ChatColor.RESET + " - " + ChatColor.DARK_PURPLE + "TARDIS Force Field button");
        update_options.put("Others", misc);
        return update_options;
    }
}
