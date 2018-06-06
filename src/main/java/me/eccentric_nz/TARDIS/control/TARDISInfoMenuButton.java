/*
 * Copyright (C) 2018 eccentric_nz
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
        player.sendMessage("§6> TARDIS §fM§6anual");
        player.sendMessage("§6> §fI§6tems");
        player.sendMessage("§6> §fC§6omponents");
        player.sendMessage("§6> §fS§6onic Components");
        player.sendMessage("§6> §fD§6isks");
        player.sendMessage("§6> C§fo§6mmands");
        player.sendMessage("§6> §fT§6ARDIS Types");
        player.sendMessage("§6> §fR§6ooms");
        player.sendMessage("§6> §fF§6ood & Accessories");
        player.sendMessage("§6> §fP§6lanets");
        player.sendMessage("§6> §fE§6xit");
    }
}
