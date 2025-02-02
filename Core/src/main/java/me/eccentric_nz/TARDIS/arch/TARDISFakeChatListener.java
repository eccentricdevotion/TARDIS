/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.arch;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author eccentric_nz
 */
public class TARDISFakeChatListener implements Listener {

    private final TARDIS plugin;

    public TARDISFakeChatListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJohnSmithChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId())) {
            return;
        }
        String currentDisplayName = player.getDisplayName();
        String newDisplayName = currentDisplayName.replace(player.getName(), plugin.getTrackerKeeper().getJohnSmith().get(player.getUniqueId()).getName());
        player.setDisplayName(newDisplayName);
    }
}
