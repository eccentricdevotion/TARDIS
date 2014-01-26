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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * Teleportation is a form of matter transmission and can be either a process of
 * physical/psychological will or a technological one.
 *
 * @author eccentric_nz
 */
public class TARDISTeleportListener implements Listener {

    private final TARDIS plugin;
    private final List<TeleportCause> causes = new ArrayList<TeleportCause>();

    public TARDISTeleportListener(TARDIS plugin) {
        this.plugin = plugin;
        causes.add(TeleportCause.PLUGIN);
        causes.add(TeleportCause.COMMAND);
        causes.add(TeleportCause.UNKNOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event) {
        TeleportCause cause = event.getCause();
        if (causes.contains(cause)) {
            String world_from = event.getFrom().getWorld().getName();
            String world_to = event.getTo().getWorld().getName();
            if (world_from.contains("TARDIS") && !world_to.contains("TARDIS")) {
                Player p = event.getPlayer();
                String playerNameStr = p.getName();
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("player", playerNameStr);
                new QueryFactory(plugin).doDelete("travellers", where);
                if (!cause.equals(TeleportCause.PLUGIN)) {
                    p.sendMessage(plugin.pluginName + "You left the TARDIS, setting OCCUPIED to false");
                }
            }
        }
    }
}
