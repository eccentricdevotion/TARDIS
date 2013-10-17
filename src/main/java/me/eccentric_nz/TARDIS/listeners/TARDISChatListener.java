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
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        final String saved = event.getPlayer().getName();
        String chat = event.getMessage();
        if (chat != null && chat.equalsIgnoreCase("tardis rescue accept")) {
            if (plugin.trackChat.containsKey(saved)) {
                final Player rescuer = plugin.getServer().getPlayer(plugin.trackChat.get(saved));
                final TARDISRescue res = new TARDISRescue(plugin);
                plugin.trackChat.remove(saved);
                // delay it so the chat appears before the message
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        res.tryRescue(rescuer, saved);
                        rescuer.sendMessage(plugin.pluginName + "Release the handbrake to start rescuing " + saved);
                    }
                }, 2L);
            } else {
                event.getPlayer().sendMessage(plugin.pluginName + "Rescue request timed out! You need to respond within 60 seconds.");
            }
        }
    }
}
