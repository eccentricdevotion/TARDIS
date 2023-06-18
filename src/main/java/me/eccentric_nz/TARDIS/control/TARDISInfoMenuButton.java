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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import net.md_5.bungee.api.ChatColor;
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
        player.sendMessage(plugin.getLanguage().getString("TIS_INFO"));
        plugin.getUpdateGUI().sendTextComponent("> TARDIS ", "M", "anual", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "I", "tems", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "C", "omponents", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "S", "onic Components", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "D", "isks", player);
        plugin.getUpdateGUI().sendTextComponent("> C", "o", "mmands", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "T", "ARDIS Types", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "R", "ooms", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "F", "ood & Accessories", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "P", "lanets", player);
        plugin.getUpdateGUI().sendTextComponent("> ", "E", "xit", player);
    }
}
