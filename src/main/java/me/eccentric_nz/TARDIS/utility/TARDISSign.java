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
package me.eccentric_nz.TARDIS.utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class TARDISSign {

    // TODO
    public static void sendSignLink(Player player) {
        TextComponent start = new TextComponent("Click the link to view the TARDIS wiki: ");
        TextComponent link = new TextComponent("https://eccentricdevotion.github.io/TARDIS/site-map.html");
        link.setColor(ChatColor.GREEN);
        link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://eccentricdevotion.github.io/TARDIS/site-map.html"));
        start.addExtra(link);
        player.spigot().sendMessage(start);
    }
}
