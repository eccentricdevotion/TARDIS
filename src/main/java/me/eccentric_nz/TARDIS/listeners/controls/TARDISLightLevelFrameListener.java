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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISLightLevelFrameListener implements Listener {

    private final TARDIS plugin;
    private final int[] interior_level = new int[]{2, 5, 10, 15};
    private final int[] exterior_level = new int[]{2, 4, 6, 8};

    public TARDISLightLevelFrameListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLightLevelClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame frame) {
            // check if it is a TARDIS monitor item frame
            Location l = frame.getLocation();
            ResultSetLightLevel rs = new ResultSetLightLevel(plugin, l.toString());
            if (rs.resultSet()) {
                // which switch is it?
                int start = (rs.getType() == 49) ? 1000 : 3000;
                // is the power off?
                if (!rs.isPowered()) {
                    start += 1000;
                }
                int limit = start + 3;
                ItemStack is = frame.getItem();
                ItemMeta im = is.getItemMeta();
                if (im.hasCustomModelData()) {
                    // switch the switches
                    int cmd = im.getCustomModelData() + 1;
                    if (cmd > limit) {
                        cmd = start;
                    }
                    im.setCustomModelData(cmd);
                    is.setItemMeta(im);
                    frame.setItem(is);
                    // save the level to the database
                    int setLevel = (rs.getLevel() + 1) > 3 ? 0 : rs.getLevel() + 1;
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("secondary", setLevel);
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("c_id", rs.getControlId());
                    plugin.getQueryFactory().doSyncUpdate("controls", set, where);
                    // alter light levels
                    int light_level;
                    if (rs.getType() == 49) {
                        // exterior
                        if (!rs.isPoliceBox()) {
                            return;
                        }
                        light_level = exterior_level[setLevel];
                        // get current TARDIS location
                        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, rs.getTardis_id());
                        if (rsc.resultSet()) {
                            if (rsc.getWorld() == null) {
                                return;
                            }
                            Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                            while (!location.getChunk().isLoaded()) {
                                location.getChunk().load();
                            }
                            Block light = location.getBlock().getRelative(BlockFace.UP, 2);
                            if (light.getBlockData() instanceof Levelled levelled) {
                                levelled.setLevel(light_level);
                                light.setBlockData(levelled);
                            }
                        }
                    } else {
                        // interior
                        light_level = interior_level[setLevel];
                        // get TARDIS lights
                        HashMap<String, Object> whereLight = new HashMap<>();
                        whereLight.put("tardis_id", rs.getTardis_id());
                        ResultSetLamps rsl = new ResultSetLamps(plugin, whereLight, true);
                        if (rsl.resultSet()) {
                            for (Block block : rsl.getData()) {
                                if (block.getBlockData() instanceof Levelled levelled) {
                                    levelled.setLevel(light_level);
                                    block.setBlockData(levelled);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
