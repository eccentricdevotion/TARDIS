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

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * In 21st century London, Rory has his father, Brian Williams, over to help fix
 * a light bulb. After saying the fixture may be the problem, the sound of the
 * TARDIS materialisation is heard. The TARDIS materialises around them,
 * shocking Brian in place.
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
    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player zero = event.getPlayer();
        if (plugin.getTrackerKeeper().getTrackZeroRoomOccupants().contains(zero.getName())) {
            event.setCancelled(true);
            zero.sendMessage(plugin.getPluginName() + MESSAGE.NOT_IN_ZERO.getText());
        } else {
            List<Player> inZeroRoom = plugin.getServer().getWorld("TARDIS_Zero_Room").getPlayers();
            for (Player p : inZeroRoom) {
                event.getRecipients().remove(p);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player zero = event.getPlayer();
        if (plugin.getTrackerKeeper().getTrackZeroRoomOccupants().contains(zero.getName())) {
            event.setCancelled(true);
            zero.sendMessage(plugin.getPluginName() + MESSAGE.NOT_IN_ZERO.getText());
        }
    }
}
