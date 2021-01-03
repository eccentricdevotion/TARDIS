/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Teleportation is a form of matter transmission and can be either a process of physical/psychological will or a
 * technological one.
 *
 * @author eccentric_nz
 */
public class TARDISTeleportListener implements Listener {

    private final TARDIS plugin;
    private final List<TeleportCause> causes = new ArrayList<>();

    public TARDISTeleportListener(TARDIS plugin) {
        this.plugin = plugin;
        causes.add(TeleportCause.PLUGIN);
        causes.add(TeleportCause.COMMAND);
        causes.add(TeleportCause.UNKNOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        TeleportCause cause = event.getCause();
        if (causes.contains(cause)) {
            String world_from = event.getFrom().getWorld().getName();
            String world_to = event.getTo().getWorld().getName();
            Player p = event.getPlayer();
            if (world_from.contains("TARDIS") && !world_to.contains("TARDIS")) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", p.getUniqueId().toString());
                plugin.getQueryFactory().doDelete("travellers", where);
                if (!cause.equals(TeleportCause.PLUGIN)) {
                    TARDISMessage.send(p, "OCCUPY_AUTO");
                }
                // stop tracking telepaths
                plugin.getTrackerKeeper().getTelepaths().remove(p.getUniqueId());
            } else if (world_to.contains("TARDIS") && !cause.equals(TeleportCause.PLUGIN)) {
                ResultSetTardisID rsid = new ResultSetTardisID(plugin);
                // if TIPS determine tardis_id from player location
                if (plugin.getConfig().getBoolean("creation.default_world") && !p.hasPermission("tardis.create_world")) {
                    int slot = TARDISInteriorPostioning.getTIPSSlot(p.getLocation());
                    if (!rsid.fromTIPSSlot(slot)) {
                        return;
                    }
                } else if (!rsid.fromUUID(p.getUniqueId().toString())) {
                    return;
                }
                int id = rsid.getTardis_id();
                HashMap<String, Object> wherei = new HashMap<>();
                wherei.put("tardis_id", id);
                wherei.put("uuid", p.getUniqueId().toString());
                plugin.getQueryFactory().doInsert("travellers", wherei);
            }
        }
    }
}
