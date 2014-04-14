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
     * within 60 seconds of a Timelord sending a rescue request, a player rescue
     * attempt is made.
     *
     * @param event a player typing in chat
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        final UUID saved = event.getPlayer().getUniqueId();
        String chat = event.getMessage();
        if (chat != null && chat.equalsIgnoreCase("tardis rescue accept")) {
            if (plugin.getTrackerKeeper().getTrackChat().containsKey(saved)) {
                final Player rescuer = plugin.getServer().getPlayer(plugin.getTrackerKeeper().getTrackChat().get(saved));
                final TARDISRescue res = new TARDISRescue(plugin);
                plugin.getTrackerKeeper().getTrackChat().remove(saved);
                // delay it so the chat appears before the message
                final String player = event.getPlayer().getName();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (res.tryRescue(rescuer, saved)) {
                            TARDISMessage.send(rescuer, plugin.getPluginName() + "Release the handbrake to start rescuing " + player);
                        }
                    }
                }, 2L);
            } else {
                TARDISMessage.send(event.getPlayer(), plugin.getPluginName() + "Rescue request timed out! You need to respond within 60 seconds.");
            }
        }
    }
}
