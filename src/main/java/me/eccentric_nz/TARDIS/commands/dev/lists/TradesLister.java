/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.dev.lists;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.command.CommandSender;

import java.util.*;

public class TradesLister {

    private final TARDIS plugin;

    public TradesLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void listConsolesAndRooms() {
        plugin.debug("#consoles");
        plugin.debug("consoles:");
        for (Map.Entry<String, Schematic> entry : Desktops.getBY_NAMES().entrySet()) {
            plugin.debug("  " + entry.getKey() + ":");
            plugin.debug("    material: " + entry.getValue().getSeed());
            int consoleAmount = plugin.getArtronConfig().getInt("upgrades." + entry.getValue().getPermission()) / 250;
            plugin.debug("    amount: " + consoleAmount);
        }
        plugin.debug("#rooms");
        plugin.debug("rooms:");
        for (String room : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            plugin.debug("  " + room + ":");
            plugin.debug("    material: " + plugin.getRoomsConfig().getString("rooms." + room + ".seed"));
            int roomAmount = plugin.getRoomsConfig().getInt("rooms." + room + ".cost") / 20;
            plugin.debug("    amount: " + roomAmount);
        }
    }
}
