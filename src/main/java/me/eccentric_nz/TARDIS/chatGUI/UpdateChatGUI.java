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

public class UpdateChatGUI {

    private final TARDIS plugin;

    public UpdateChatGUI(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void showInterface(Player player, String arg) {
        if (arg.isEmpty()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SECTION");
            plugin.getMessenger().message(player, "------");
            plugin.getJsonKeeper().getSections().forEach(player::sendMessage);
            plugin.getMessenger().message(player, "------");
            return;
        }
        if (arg.equalsIgnoreCase("controls")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_CONTROL");
            plugin.getMessenger().message(player, "------");
            plugin.getJsonKeeper().getControls().forEach(player::sendMessage);
            plugin.getMessenger().message(player, "------");
            return;
        }
        if (arg.equalsIgnoreCase("interfaces")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_INTERFACE");
            plugin.getMessenger().message(player, "------");
            plugin.getJsonKeeper().getInterfaces().forEach(player::sendMessage);
            plugin.getMessenger().message(player, "------");
            return;
        }
        if (arg.equalsIgnoreCase("locations")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_LOCATION");
            plugin.getMessenger().message(player, "------");
            plugin.getJsonKeeper().getLocations().forEach(player::sendMessage);
            plugin.getMessenger().message(player, "------");
            return;
        }
        if (arg.equalsIgnoreCase("sensors")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SENSOR");
            plugin.getMessenger().message(player, "------");
            plugin.getJsonKeeper().getSensors().forEach(player::sendMessage);
            plugin.getMessenger().message(player, "------");
            return;
        }
        if (arg.equalsIgnoreCase("others")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_OTHER");
            plugin.getMessenger().message(player, "------");
            plugin.getJsonKeeper().getOthers().forEach(player::sendMessage);
            plugin.getMessenger().message(player, "------");
        }
    }
}
