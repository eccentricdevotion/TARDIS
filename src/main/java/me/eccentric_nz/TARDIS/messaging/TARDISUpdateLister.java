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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.update.TARDISUpdateableCategory;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISUpdateLister {

    private final Player player;

    public TARDISUpdateLister(Player player) {
        this.player = player;
    }

    public void list() {
        TARDISMessage.send(player, "UPDATE_INFO");
        TARDISMessage.message(player, ChatColor.GRAY + "Hover over command argument to see a description");
        TARDISMessage.message(player, ChatColor.GRAY + "Click to run the /tardis update command");
        TARDISMessage.message(player, "");
        for (TARDISUpdateableCategory category : TARDISUpdateableCategory.values()) {
            TARDISMessage.message(player, category.getName());
            for (Updateable updateable : Updateable.values()) {
                if (updateable.getCategory() == category) {
                    TextComponent tcu = new TextComponent(updateable.getName());
                    tcu.setColor(category.getColour());
                    tcu.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(updateable.getDescription())));
                    tcu.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardis update " + updateable.getName()));
                    player.spigot().sendMessage(tcu);
                }
            }
            TARDISMessage.message(player, "");
        }
    }
}
