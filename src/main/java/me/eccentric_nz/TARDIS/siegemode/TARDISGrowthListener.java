/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.siegemode;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISGrowthListener implements Listener {

    private final TARDIS plugin;
    private final int chance;
    private final Random random = new Random();
    private final List<Material> plants = Arrays.asList(Material.AIR, Material.CACTUS, Material.CARROT, Material.COCOA, Material.CROPS, Material.MELON_STEM, Material.NETHER_WARTS, Material.POTATO, Material.PUMPKIN_STEM, Material.SUGAR_CANE_BLOCK);

    public TARDISGrowthListener(TARDIS plugin) {
        this.plugin = plugin;
        chance = this.plugin.getConfig().getInt("siege.growth");
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onPlantGrowth(BlockGrowEvent event) {
        final Block plant = event.getBlock();
        Material species = plant.getType();
        if (!plants.contains(species)) {
            return;
        }
        Location l = plant.getLocation();
        String w = l.getWorld().getName();
        if (!plugin.getTrackerKeeper().getSiegeGrowthAreas().containsKey(w)) {
            return;
        }
        for (TARDISSiegeArea area : plugin.getTrackerKeeper().getSiegeGrowthAreas().get(w)) {
            if (area.isInSiegeArea(l) && random.nextInt(100) < chance) {
                // get current data
                final byte data = plant.getData();
                // grow an extra step
                switch (species) {
                    case CACTUS:
                    case SUGAR_CANE_BLOCK:
                    case AIR:
                        plugin.debug("setting another block on top");
                        // with cactus and sugar cane the block returned is AIR
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                plant.getRelative(BlockFace.UP).setType(plant.getRelative(BlockFace.DOWN).getType());
                            }
                        }, 3L);
                        break;
                    case CARROT:
                    case CROPS:
                    case MELON_STEM:
                    case POTATO:
                    case PUMPKIN_STEM:
                        // fully grown is 7
                        if (data < 6) {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plant.setData((byte) (data + 2));
                                }
                            }, 3L);
                        }
                        break;
                    case COCOA:
                        // fully grown is 3
                        if (data < 4 && random.nextInt(100) < 25) {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plant.setData((byte) (data + 8));
                                }
                            }, 3L);
                        }
                        break;
                    default: // NETHER_WARTS
                        // fully grown is 3
                        if (data < 2 && random.nextInt(100) < 33) {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    plant.setData((byte) (data + 2));
                                }
                            }, 3L);
                        }
                        break;
                }
                break;
            }
        }
    }
}
