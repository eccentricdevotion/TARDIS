/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRoomLister {

    private final TARDIS plugin;
    private final Player player;
    private final LinkedHashMap<String, List<String>> options;

    public TARDISRoomLister(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.options = createRoomOptions(this.player);
    }

    public void list() {
        TARDISMessage.send(player, "ROOM_INFO", String.format("%d", plugin.getGeneralKeeper().getRoomArgs().size()));
        for (Map.Entry<String, List<String>> map : options.entrySet()) {
            player.sendMessage(map.getKey());
            if (map.getValue().size() > 0) {
                for (String s : map.getValue()) {
                    player.sendMessage("    " + s);
                }
            } else {
                TARDISMessage.send(player, true, "ROOM_NONE");
            }
        }
        TARDISMessage.send(player, "ROOM_GALLERY");
    }

    private LinkedHashMap<String, List<String>> createRoomOptions(Player player) {
        LinkedHashMap<String, List<String>> room_options = new LinkedHashMap<String, List<String>>();
        List<String> default_rooms = new ArrayList<String>();
        List<String> custom_rooms = new ArrayList<String>();
        for (String room : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + room + ".enabled")) {
                ChatColor colour = (player.hasPermission("tardis.room." + room)) ? ChatColor.GREEN : ChatColor.RED;
                if (plugin.getRoomsConfig().getBoolean("rooms." + room + ".user")) {
                    custom_rooms.add(colour + room);
                } else {
                    default_rooms.add(colour + room);
                }
            }
        }
        room_options.put("Default Rooms", default_rooms);
        room_options.put("Custom Rooms", custom_rooms);
        return room_options;
    }
}
