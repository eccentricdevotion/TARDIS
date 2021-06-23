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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.chatgui.TardisChatGuiUpdater;
import me.eccentric_nz.tardis.info.TardisInfoMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TardisInfoMenuButton {

    private final TardisPlugin plugin;
    private final Player player;

    TardisInfoMenuButton(TardisPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void clickButton() {
        plugin.getTrackerKeeper().getInfoMenu().put(player.getUniqueId(), TardisInfoMenu.TIS);
        player.sendMessage(ChatColor.GOLD + "-----------TARDIS Information System-----------");
        player.sendMessage(ChatColor.GOLD + "---*" + plugin.getLanguage().getString("TIS_INFO") + "*---");
        TardisChatGuiUpdater.sendTextComponent("> TARDIS ", "M", "anual", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "I", "tems", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "C", "omponents", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "S", "onic Components", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "D", "isks", player);
        TardisChatGuiUpdater.sendTextComponent("> C", "o", "mmands", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "T", "ARDIS Types", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "R", "ooms", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "F", "ood & Accessories", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "P", "lanets", player);
        TardisChatGuiUpdater.sendTextComponent("> ", "E", "xit", player);
    }
}
