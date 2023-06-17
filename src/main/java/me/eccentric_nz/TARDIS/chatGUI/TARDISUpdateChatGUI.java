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
package me.eccentric_nz.TARDIS.chatGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class TARDISUpdateChatGUI {

    private final TARDIS plugin;

    public TARDISUpdateChatGUI(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static void sendTextComponent(String first, String value, String split, Player player) {
        // TODO
        TextComponent tcf = new TextComponent(first);
        tcf.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        TextComponent tck = new TextComponent(value);
        tck.setColor(net.md_5.bungee.api.ChatColor.WHITE);
        tcf.addExtra(tck);
        TextComponent tcl = new TextComponent(split);
        tcl.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        tcf.addExtra(tcl);
        tcf.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        tcf.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisinfo " + value));
        player.spigot().sendMessage(tcf);
    }

    public boolean showInterface(Player player, String[] args) {
        if (args.length == 1) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SECTION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getSections().forEach((s) -> player.spigot().sendMessage(s));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("controls")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SECTION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getControls().forEach((c) -> player.spigot().sendMessage(c));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("interfaces")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_INTERFACE");
            player.sendMessage("------");
            plugin.getJsonKeeper().getInterfaces().forEach((i) -> player.spigot().sendMessage(i));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("locations")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_LOCATION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getLocations().forEach((l) -> player.spigot().sendMessage(l));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("others")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_OTHER");
            player.sendMessage("------");
            plugin.getJsonKeeper().getOthers().forEach((o) -> player.spigot().sendMessage(o));
            player.sendMessage("------");
            return true;
        }
        return false;
    }
}
