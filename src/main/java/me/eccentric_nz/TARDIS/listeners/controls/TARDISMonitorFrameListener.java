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
package me.eccentric_nz.TARDIS.listeners.controls;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.camera.TARDISCamera;
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.monitor.MonitorSnapshot;
import me.eccentric_nz.TARDIS.monitor.MonitorUtils;
import me.eccentric_nz.TARDIS.monitor.Snapshot;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

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
            // check if it is a TARDIS monitor item frame
            Location l = frame.getLocation();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l.toString());
            where.put("type", 46);
            ResultSetControls rs = new ResultSetControls(plugin, where, false);
            if (rs.resultSet()) {
                UUID uuid = player.getUniqueId();
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.MONITOR)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "TARDIS Monitor");
                    return;
                }
                if (player.isSneaking() && TARDISPermission.hasPermission(player, "tardis.camera")) {
                    // not while travelling
                    if (isTravelling(rs.getTardis_id())) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CAMERA_NO_TRAVEL");
                        return;
                    }
                    // get the preset - only custom model presets
                    ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
                    if (rsp.fromID(rs.getTardis_id()) && rsp.getPreset().usesArmourStand()) {
                        new TARDISCamera(plugin).viewExterior(player, rs.getTardis_id(), rsp.getPreset() == ChameleonPreset.PANDORICA);
                    }
                } else {
                    ItemStack is = frame.getItem();
                    ItemMeta im = is.getItemMeta();
                    if (im.hasItemModel()) {
                        // switch the switches
                        NamespacedKey cmd = im.getItemModel();
                        switch (cmd.getKey().split("_")[2]) {
                            case "left" -> im.setItemModel(ModelledControl.MONITOR_FRAME_MIDDLE.getKey());
                            case "middle" -> im.setItemModel(ModelledControl.MONITOR_FRAME_RIGHT.getKey());
                            // right
                            default -> im.setItemModel(ModelledControl.MONITOR_FRAME_LEFT.getKey());
                        }
                        is.setItemMeta(im);
                        frame.setItem(is);
                        // get the monitor item frame, from the same block location
                        ItemFrame mapFrame = MonitorUtils.getItemFrameFromLocation(l, frame.getUniqueId());
                        if (mapFrame != null) {
                            // does it have a filled map?
                            ItemStack map = mapFrame.getItem();
                            if (map.getType() == Material.FILLED_MAP) {
                                // get the TARDIS the player is inside
                                HashMap<String, Object> wheret = new HashMap<>();
                                wheret.put("uuid", uuid.toString());
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
                                        MonitorUtils.updateSnapshot(door, 128, map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isTravelling(int id) {
        return (plugin.getTrackerKeeper().getDematerialising().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
    }
}
