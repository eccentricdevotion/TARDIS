/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.builders.TardisInteriorPositioning;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Teleportation is a form of matter transmission and can be either a process of physical/psychological will or a
 * technological one.
 *
 * @author eccentric_nz
 */
public class TardisTeleportListener implements Listener {

    private final TardisPlugin plugin;
    private final List<TeleportCause> causes = new ArrayList<>();

    public TardisTeleportListener(TardisPlugin plugin) {
        this.plugin = plugin;
        causes.add(TeleportCause.PLUGIN);
        causes.add(TeleportCause.COMMAND);
        causes.add(TeleportCause.UNKNOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        TeleportCause cause = event.getCause();
        if (causes.contains(cause)) {
            String world_from = Objects.requireNonNull(event.getFrom().getWorld()).getName();
            String world_to = Objects.requireNonNull(Objects.requireNonNull(event.getTo()).getWorld()).getName();
            Player p = event.getPlayer();
            String uuid = p.getUniqueId().toString();
            if (world_from.contains("tardis") && !world_to.contains("tardis")) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                plugin.getQueryFactory().doDelete("travellers", where);
                if (!cause.equals(TeleportCause.PLUGIN)) {
                    TardisMessage.send(p, "OCCUPY_AUTO");
                }
                // stop tracking telepaths
                plugin.getTrackerKeeper().getTelepaths().remove(p.getUniqueId());
            } else if (world_to.contains("tardis") && !cause.equals(TeleportCause.PLUGIN)) {
                ResultSetTardisId rsid = new ResultSetTardisId(plugin);
                // if TIPS determine tardis_id from player location
                if (plugin.getConfig().getBoolean("creation.default_world")) {
                    if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && p.hasPermission("tardis.create_world")) {
                        if (!rsid.fromUUID(uuid)) {
                            return;
                        }
                    } else {
                        int slot = TardisInteriorPositioning.getTIPSSlot(p.getLocation());
                        if (!rsid.fromTIPSSlot(slot)) {
                            return;
                        }
                    }
                } else if (!rsid.fromUUID(uuid)) {
                    return;
                }
                // remove potential existing records from travellers first
                HashMap<String, Object> wherer = new HashMap<>();
                wherer.put("uuid", uuid);
                plugin.getQueryFactory().doDelete("travellers", wherer);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    HashMap<String, Object> wherei = new HashMap<>();
                    wherei.put("tardis_id", rsid.getTardisId());
                    wherei.put("uuid", uuid);
                    plugin.getQueryFactory().doInsert("travellers", wherei);
                }, 2L);
            }
        }
    }
}
