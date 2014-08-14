/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.travel.TARDISRescue;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * In 21st century London, Rory has his father, Brian Williams, over to help fix
 * a light bulb. After saying the fixture may be the problem, the sound of the
 * TARDIS materialisation is heard. The TARDIS materialises around them,
 * shocking Brian in place.
 *
 * @author eccentric_nz
 */
public class TARDISChatListener implements Listener {

    private final TARDIS plugin;

    public TARDISChatListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player typing "tardis rescue accept". If the player types it
     * within 60 seconds of a Time Lord sending a rescue request, a player
     * rescue attempt is made.
     *
     * @param event a player typing in chat
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        final UUID saved = event.getPlayer().getUniqueId();
        String chat = event.getMessage();
        if (chat != null && (chat.equalsIgnoreCase("tardis rescue accept") || chat.equalsIgnoreCase("tardis request accept"))) {
            if (plugin.getTrackerKeeper().getChat().containsKey(saved)) {
                final Player rescuer = plugin.getServer().getPlayer(plugin.getTrackerKeeper().getChat().get(saved));
                final TARDISRescue res = new TARDISRescue(plugin);
                plugin.getTrackerKeeper().getChat().remove(saved);
                // delay it so the chat appears before the message
                final String player = event.getPlayer().getName();
                final boolean request = (chat.equalsIgnoreCase("tardis request accept"));
                final String message = (chat.equalsIgnoreCase("tardis request accept")) ? "REQUEST_RELEASE" : "RESCUE_RELEASE";
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (res.tryRescue(rescuer, saved, request)) {
                            TARDISMessage.send(rescuer, message, player);
                        }
                    }
                }, 2L);
            } else {
                final String message = (chat.equalsIgnoreCase("tardis request accept")) ? "REQUEST_TIMEOUT" : "RESCUE_TIMEOUT";
                TARDISMessage.send(event.getPlayer(), message);
            }
        }
    }
}
