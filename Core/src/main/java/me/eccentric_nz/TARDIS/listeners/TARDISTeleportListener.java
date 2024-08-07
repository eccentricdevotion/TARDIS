/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        TeleportCause cause = event.getCause();
        if (causes.contains(cause)) {
            Player player = event.getPlayer();
            if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(player.getUniqueId())) {
                if (plugin.getTrackerKeeper().getStillFlyingNotReturning().contains(player.getUniqueId())) {
                    // dis-allow teleporting while flying
                    event.setCancelled(true);
                } else {
                    player.resetPlayerTime();
                }
                return;
            }
            String world_from = event.getFrom().getWorld().getName();
            String world_to = event.getTo().getWorld().getName();
            String uuid = player.getUniqueId().toString();
            if (world_from.contains("TARDIS") && !world_to.contains("TARDIS")) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                plugin.getQueryFactory().doDelete("travellers", where);
                if (!cause.equals(TeleportCause.PLUGIN)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "OCCUPY_AUTO");
                }
                // stop tracking telepaths
                plugin.getTrackerKeeper().getTelepaths().remove(player.getUniqueId());
                // reset player time
                player.resetPlayerTime();
            } else if (world_to.contains("TARDIS") && !cause.equals(TeleportCause.PLUGIN)) {
                ResultSetTardisID rsid = new ResultSetTardisID(plugin);
                // if TIPS determine tardis_id from player location
                if (plugin.getConfig().getBoolean("creation.default_world")) {
                    if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && player.hasPermission("tardis.create_world")) {
                        if (!rsid.fromUUID(uuid)) {
                            return;
                        }
                    } else {
                        int slot = TARDISInteriorPostioning.getTIPSSlot(player.getLocation());
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
