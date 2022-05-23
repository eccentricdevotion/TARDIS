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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISInfoMenuButton {

    private final TARDIS plugin;
    private final Player player;

    TARDISInfoMenuButton(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void clickButton() {
        plugin.getTrackerKeeper().getInfoMenu().put(player.getUniqueId(), TARDISInfoMenu.TIS);
        player.sendMessage(ChatColor.GOLD + "-----------TARDIS Information System-----------");
        player.sendMessage(plugin.getLanguage().getString("TIS_INFO"));
        TARDISUpdateChatGUI.sendTextComponent("> TARDIS ", "M", "anual", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "I", "tems", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "C", "omponents", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "S", "onic Components", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "D", "isks", player);
        TARDISUpdateChatGUI.sendTextComponent("> C", "o", "mmands", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "T", "ARDIS Types", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "R", "ooms", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "F", "ood & Accessories", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "P", "lanets", player);
        TARDISUpdateChatGUI.sendTextComponent("> ", "E", "xit", player);
    }
}
