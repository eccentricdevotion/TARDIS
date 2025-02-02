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
package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

public class TISRoomInfo {

    private final TARDIS plugin;

    public TISRoomInfo(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void show(Player player, TARDISInfoMenu item) {
        player.sendMessage("---");
        player.sendMessage("[" + item.getName() + "]");
        plugin.getMessenger().messageWithColour(player, TARDISDescription.valueOf(item.toString()).getDesc(), "#FFAA00");
        String r = item.toString();
        plugin.getMessenger().messageWithColour(player, "Seed Block: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"), "#FFAA00");
        plugin.getMessenger().messageWithColour(player, "Offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"), "#FFAA00");
        plugin.getMessenger().messageWithColour(player, "Cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"), "#FFAA00");
        plugin.getMessenger().messageWithColour(player, "Enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"), "#FFAA00");
    }
}
