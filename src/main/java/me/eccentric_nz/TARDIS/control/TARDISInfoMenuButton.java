/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISInfoMenuButton {

    private final TARDIS plugin;
    private final Player player;

    public TARDISInfoMenuButton(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void clickButton() {
        plugin.getTrackerKeeper().getInfoMenu().put(player.getUniqueId(), TARDISInfoMenu.TIS);
        player.sendMessage(ChatColor.GOLD + "-----------TARDIS Information System-----------");
        player.sendMessage(ChatColor.GOLD + "---*" + plugin.getLanguage().getString("TIS_INFO") + "*---");
        player.sendMessage(ChatColor.GOLD + "> TARDIS " + ChatColor.WHITE + "M" + ChatColor.GOLD + "anual");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "I" + ChatColor.GOLD + "tems");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "C" + ChatColor.GOLD + "omponents");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "S" + ChatColor.GOLD + "onic Components");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "D" + ChatColor.GOLD + "isks");
        player.sendMessage(ChatColor.GOLD + "> C" + ChatColor.WHITE + "o" + ChatColor.GOLD + "mmands");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "T" + ChatColor.GOLD + "ARDIS Types");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "R" + ChatColor.GOLD + "ooms");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "F" + ChatColor.GOLD + "ood & Accessories");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "P" + ChatColor.GOLD + "lanets");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.WHITE + "E" + ChatColor.GOLD + "xit");
    }
}
