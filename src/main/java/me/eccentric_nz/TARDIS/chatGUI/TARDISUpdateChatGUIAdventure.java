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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class TARDISUpdateChatGUIAdventure implements TARDISUpdateChatGUI {

    private final TARDIS plugin;

    public TARDISUpdateChatGUIAdventure(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendTextComponent(String first, String value, String split, Player player) {
        TextComponent tcf = Component.text(first, NamedTextColor.GOLD)
                .append(Component.text(value, NamedTextColor.WHITE))
                .append(Component.text(split, NamedTextColor.GOLD))
                .hoverEvent(HoverEvent.showText(Component.text("Click me!")))
                .clickEvent(ClickEvent.runCommand("/tardisinfo " + value));
        player.sendMessage(tcf);
    }

    @Override
    public boolean showInterface(Player player, String[] args) {
        if (args.length == 1) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SECTION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getSections().forEach((s) -> player.sendMessage((TextComponent) s));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("controls")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_SECTION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getControls().forEach((c) -> player.sendMessage((TextComponent) c));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("interfaces")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_INTERFACE");
            player.sendMessage("------");
            plugin.getJsonKeeper().getInterfaces().forEach((i) -> player.sendMessage((TextComponent) i));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("locations")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_LOCATION");
            player.sendMessage("------");
            plugin.getJsonKeeper().getLocations().forEach((l) -> player.sendMessage((TextComponent) l));
            player.sendMessage("------");
            return true;
        }
        if (args[1].equalsIgnoreCase("others")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_OTHER");
            player.sendMessage("------");
            plugin.getJsonKeeper().getOthers().forEach((o) -> player.sendMessage((TextComponent) o));
            player.sendMessage("------");
            return true;
        }
        return false;
    }
}
