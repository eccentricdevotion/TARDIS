/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGarden;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.ArrayList;
import java.util.List;

public class TARDISBonemealListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> smallFlowers;

    public TARDISBonemealListener(TARDIS plugin) {
        this.plugin = plugin;
        smallFlowers = new ArrayList<>(Tag.SMALL_FLOWERS.getValues());
        smallFlowers.remove(Material.WITHER_ROSE);
        smallFlowers.remove(Material.TORCHFLOWER);
        smallFlowers.remove(Material.WILDFLOWERS);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBonemeal(BlockFertilizeEvent event) {
        Location location = event.getBlock().getLocation();
        // check location
        if (!plugin.getUtils().inTARDISWorld(location)) {
            return;
        }
        // is it a flower garden?
        int id = inGarden(location);
        if (id == -1) {
            return;
        }
        // is it the player's garden?
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(event.getPlayer().getUniqueId().toString()) && rst.getTardisId() == id) {
            int y = location.getBlockY() + 1;
            for (BlockState state : event.getBlocks()) {
                Block block = state.getBlock();
                if (block.getY() > y) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        state.setType(Material.AIR);
                        state.update(true);
                    }, 1L);
                } else if (block.getType() == Material.AIR) {
                    // change to a random flower
                    Material flower = smallFlowers.get(TARDISConstants.RANDOM.nextInt(smallFlowers.size()));
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        state.setType(flower);
                        state.update(true);
                    }, 1L);
                }
            }
        } else {
            event.setCancelled(true);
            plugin.getMessenger().send(event.getPlayer(), TardisModule.TARDIS, "GARDEN_TIMELORD");
        }
    }

    private int inGarden(Location location) {
        ResultSetGarden rsg = new ResultSetGarden(plugin, location);
        return (rsg.resultSet()) ? rsg.getTardis_id() : -1;
    }
}
