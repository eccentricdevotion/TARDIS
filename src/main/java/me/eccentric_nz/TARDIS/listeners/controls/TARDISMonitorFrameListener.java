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
package me.eccentric_nz.TARDIS.listeners.controls;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.monitor.MonitorUtils;
import me.eccentric_nz.TARDIS.monitor.MonitorSnapshot;
import me.eccentric_nz.TARDIS.monitor.Snapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISMonitorFrameListener implements Listener {

    private final TARDIS plugin;

    public TARDISMonitorFrameListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMonitorFrameClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame frame) {
            Player player = event.getPlayer();
            // check if it is a TARDIS direction item frame
            Location l = frame.getLocation();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l.toString());
            where.put("type", 46);
            ResultSetControls rs = new ResultSetControls(plugin, where, false);
            if (rs.resultSet()) {
                ItemStack is = frame.getItem();
                ItemMeta im = is.getItemMeta();
                if (im.hasCustomModelData()) {
                    // switch the switches
                    int cmd = im.getCustomModelData() + 1;
                    if (cmd > 4) {
                        cmd = 2;
                    }
                    im.setCustomModelData(cmd);
                    is.setItemMeta(im);
                    // get the monitor item frame, from the same block location
                    ItemFrame mapFrame = MonitorUtils.getItemFrameFromLocation(l, frame.getUniqueId());
                    if (mapFrame != null) {
                        // does it have a filled map?
                        ItemStack map = mapFrame.getItem();
                        if (map.getType() == Material.FILLED_MAP) {
                            // get the TARDIS the player is inside
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("uuid", player.getUniqueId().toString());
                            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                            if (rst.resultSet()) {
                                int id = rst.getTardis_id();
                                // get door location
                                Snapshot snapshot = MonitorUtils.getLocationAndDirection(id, false);
                                Location door = snapshot.getLocation();
                                if (door != null) {
                                    // load chunks
                                    MonitorSnapshot.loadChunks(plugin, door, false, snapshot.getDirection(), id, 128);
                                    // update the map
                                    MonitorUtils.updateSnapshot(door, player, 128, map);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
