/*
 * Copyright (C) 2022 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISRoomLister {

    private final TARDIS plugin;
    private final Player player;
    private final LinkedHashMap<String, List<String>> options;

    public TARDISRoomLister(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        options = createRoomOptions();
    }

    public void list() {
        TARDISMessage.send(player, "ROOM_INFO", String.format("%d", plugin.getGeneralKeeper().getRoomArgs().size()));
        TARDISMessage.message(player, ChatColor.GRAY + "Click a room name to suggest a command");
        TARDISMessage.message(player, "");
        options.forEach((key, value) -> {
            player.sendMessage(key);
            if (value.size() > 0) {
                value.forEach((s) -> {
                    TextComponent tcr = new TextComponent("    " + s);
                    ChatColor colour = (TARDISPermission.hasPermission(player, "tardis.room." + s.toLowerCase())) ? ChatColor.GREEN : ChatColor.RED;
                    tcr.setColor(colour);
                    tcr.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
                    tcr.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardis room " + s));
                    player.spigot().sendMessage(tcr);
                });
            } else {
                TARDISMessage.send(player, "ROOM_NONE");
            }
        });
        TARDISMessage.send(player, "ROOM_GALLERY");
        TextComponent tcg = new TextComponent("https://eccentricdevotion.github.io/TARDIS/room-gallery");
        tcg.setColor(ChatColor.AQUA);
        tcg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        tcg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://eccentricdevotion.github.io/TARDIS/room-gallery"));
        player.spigot().sendMessage(tcg);
    }

    private LinkedHashMap<String, List<String>> createRoomOptions() {
        LinkedHashMap<String, List<String>> room_options = new LinkedHashMap<>();
        List<String> default_rooms = new ArrayList<>();
        List<String> custom_rooms = new ArrayList<>();
        plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false).forEach((room) -> {
            if (plugin.getRoomsConfig().getBoolean("rooms." + room + ".enabled")) {
                if (plugin.getRoomsConfig().getBoolean("rooms." + room + ".user")) {
                    custom_rooms.add(room);
                } else {
                    default_rooms.add(room);
                }
            }
        });
        room_options.put("Default Rooms", default_rooms);
        room_options.put("Custom Rooms", custom_rooms);
        return room_options;
    }
}
