/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChatListener implements Listener {

    private final TARDIS plugin;

    public TARDISChatListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        String saved = event.getPlayer().getName();
        if (plugin.trackChat.containsKey(saved)) {
            String chat = event.getMessage();
            if (chat.equalsIgnoreCase("tardis rescue accept")) {
                Player rescuer = plugin.getServer().getPlayer(plugin.trackChat.get(saved));
                TARDISRescue res = new TARDISRescue(plugin);
                res.tryRescue(rescuer, saved);
                rescuer.sendMessage(plugin.pluginName + "Release the handbrake to start rescuing " + saved);
                plugin.trackChat.remove(saved);
            }
        } else {
            event.getPlayer().sendMessage(plugin.pluginName + "Rescue request timed out! You need to respond within 60 seconds.");
        }
    }
}
