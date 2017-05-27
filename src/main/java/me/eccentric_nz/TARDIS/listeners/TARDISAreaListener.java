/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Within the first nanosecond of landing in a new location, the TARDIS
 * chameleon circuit analyses the surrounding area, calculates a
 * twelve-dimensional data map of all objects within a thousand-mile radius and
 * then determines which outer shell would best blend in with the environment.
 *
 * @author eccentric_nz
 */
public class TARDISAreaListener implements Listener {

    private final TARDIS plugin;

    public TARDISAreaListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking blocks. If the player's name is contained in
     * various tracking HashMaps then we know that they are trying to create a
     * TARDIS area.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAreaInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        if (block != null) {
            if (plugin.getTrackerKeeper().getArea().containsKey(uuid) && !plugin.getTrackerKeeper().getBlock().containsKey(uuid)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.getTardisArea().areaCheckInExisting(block_loc)) {
                    String locStr = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ();
                    plugin.getTrackerKeeper().getBlock().put(uuid, locStr);
                    TARDISMessage.send(player, "AREA_END_INFO", ChatColor.GREEN + "/tardisarea end" + ChatColor.RESET);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getTrackerKeeper().getArea().remove(uuid);
                        plugin.getTrackerKeeper().getBlock().remove(uuid);
                    }, 1200L);
                } else {
                    TARDISMessage.send(player, "AREA_INSIDE");
                }
            } else if (plugin.getTrackerKeeper().getBlock().containsKey(uuid) && plugin.getTrackerKeeper().getEnd().containsKey(uuid)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.getTardisArea().areaCheckInExisting(block_loc)) {
                    String[] firstblock = plugin.getTrackerKeeper().getBlock().get(uuid).split(":");
                    if (!block_loc.getWorld().getName().equals(firstblock[0])) {
                        TARDISMessage.send(player, "AREA_WORLD");
                        return;
                    }
                    int y = block_loc.getBlockY();
                    if (y != (TARDISNumberParsers.parseInt(firstblock[2]))) {
                        TARDISMessage.send(player, "AREA_Y");
                        return;
                    }
                    int minx, minz, maxx, maxz;
                    if (TARDISNumberParsers.parseInt(firstblock[1]) < block_loc.getBlockX()) {
                        minx = TARDISNumberParsers.parseInt(firstblock[1]);
                        maxx = block_loc.getBlockX();
                    } else {
                        minx = block_loc.getBlockX();
                        maxx = TARDISNumberParsers.parseInt(firstblock[1]);
                    }
                    if (TARDISNumberParsers.parseInt(firstblock[3]) < block_loc.getBlockZ()) {
                        minz = TARDISNumberParsers.parseInt(firstblock[3]);
                        maxz = block_loc.getBlockZ();
                    } else {
                        minz = block_loc.getBlockZ();
                        maxz = TARDISNumberParsers.parseInt(firstblock[3]);
                    }
                    String n = plugin.getTrackerKeeper().getArea().get(uuid);
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("area_name", n);
                    set.put("world", firstblock[0]);
                    set.put("minx", minx);
                    set.put("minz", minz);
                    set.put("maxx", maxx);
                    set.put("maxz", maxz);
                    set.put("y", y + 1);
                    qf.doInsert("areas", set);
                    TARDISMessage.send(player, "AREA_SAVED", plugin.getTrackerKeeper().getArea().get(uuid));
                    plugin.getTrackerKeeper().getArea().remove(uuid);
                    plugin.getTrackerKeeper().getBlock().remove(uuid);
                    plugin.getTrackerKeeper().getEnd().remove(uuid);
                } else {
                    TARDISMessage.send(player, "AREA_INSIDE");
                }
            }
        }
    }
}
