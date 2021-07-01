/*
 * Copyright (C) 2021 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.destroyers.TARDISExterminator;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISExterminateCommand {

    private final TARDIS plugin;

    TARDISExterminateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doExterminate(Player player, boolean messagePlayer) {
        if (TARDISPermission.hasPermission(player, "tardis.exterminate")) {
            if (messagePlayer) {
                TextComponent textComponent = new TextComponent(plugin.getLanguage().getString("EXTERMINATE_CONFIRM"));
                textComponent.setColor(ChatColor.AQUA);
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis exterminate 6z@3=V!Q7*/O_OB^"));
                TARDISMessage.send(player, "EXTERMINATE_CHECK");
                player.spigot().sendMessage(textComponent);
                return true;
            } else {
                return new TARDISExterminator(plugin).exterminate(player);
            }
        } else {
            TARDISMessage.send(player, "NO_PERM_DELETE");
            return true;
        }
    }
}
