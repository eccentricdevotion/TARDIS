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
package me.eccentric_nz.TARDIS.chatGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

public class TARDISUpdateChatGUI {

    private final TARDIS plugin;

    public TARDISUpdateChatGUI(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean showInterface(Player player, String[] args) {
        if (args.length == 1) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SECTION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getSections().forEach(player::sendMessage);
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("controls")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_CONTROL");
            player.sendMessage("------");
            plugin.getJsonKeeper().getControls().forEach(player::sendMessage);
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("interfaces")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_INTERFACE");
            player.sendMessage("------");
            plugin.getJsonKeeper().getInterfaces().forEach(player::sendMessage);
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("locations")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_LOCATION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getLocations().forEach(player::sendMessage);
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("sensors")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SENSOR");
            player.sendMessage("------");
            plugin.getJsonKeeper().getSensors().forEach(player::sendMessage);
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("others")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_OTHER");
            player.sendMessage("------");
            plugin.getJsonKeeper().getOthers().forEach(player::sendMessage);
            player.sendMessage("------");
            return true;
        }
        return false;
    }
}
