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
package me.eccentric_nz.TARDIS.listeners;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * In 21st century London, Rory has his father, Brian Williams, over to help fix a light bulb. After saying the fixture
 * may be the problem, the sound of the TARDIS materialisation is heard. The TARDIS materialises around them, shocking
 * Brian in place.
 *
 * @author eccentric_nz
 */
public class TARDISZeroRoomChatListener implements Listener {

    private final TARDIS plugin;

    public TARDISZeroRoomChatListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Prevents the occupants of zero rooms from sending or receiving chat.
     *
     * @param event a player typing in chat
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player zero = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(zero.getUniqueId())) {
            event.setCancelled(true);
            plugin.getMessenger().send(zero, TardisModule.TARDIS, "NOT_IN_ZERO");
        } else if (plugin.getServer().getWorld("TARDIS_Zero_Room") != null) {
            List<Player> inZeroRoom = plugin.getServer().getWorld("TARDIS_Zero_Room").getPlayers();
            inZeroRoom.forEach((p) -> event.getRecipients().remove(p));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_ZERO");
            return;
        }
        UUID uuid = player.getUniqueId();
        String command = event.getMessage().toLowerCase(Locale.ENGLISH);
        if (plugin.getTrackerKeeper().getTelepaths().containsKey(uuid)) {
            if (command.contains("tardis ") || command.contains("tardistravel ") || command.contains("ttravel ")) {
                UUID owner = plugin.getTrackerKeeper().getTelepaths().get(uuid);
                Player timelord = plugin.getServer().getPlayer(owner);
                if (timelord != null && timelord.isOnline()) {
                    // message console so it is logged
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, " Companion [" + player.getName() + "] ran a telepathic command as Time Lord [" + timelord.getName() + "]");
                    if (command.contains("rescue") && command.contains(timelord.getName().toLowerCase(Locale.ENGLISH))) {
                        // track the timelord
                        plugin.getTrackerKeeper().getTelepathicRescue().put(owner, uuid);
                    }
                    // if it is a tardis command run it as the time lord
                    event.setPlayer(timelord);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TELEPATHIC_RUN", command);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TELEPATHIC_ONLINE");
                }
            }
            // always stop tracking the player
            plugin.getTrackerKeeper().getTelepaths().remove(uuid);
        }
    }
}
