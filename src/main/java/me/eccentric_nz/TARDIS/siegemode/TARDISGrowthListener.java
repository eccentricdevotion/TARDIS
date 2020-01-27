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
package me.eccentric_nz.TARDIS.siegemode;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISGrowthListener implements Listener {

    private final TARDIS plugin;
    private final int chance;
    private final List<Material> plants = Arrays.asList(Material.AIR, Material.BEETROOTS, Material.CACTUS, Material.CARROTS, Material.COCOA, Material.WHEAT, Material.MELON_STEM, Material.NETHER_WART, Material.POTATOES, Material.PUMPKIN_STEM, Material.SUGAR_CANE);

    public TARDISGrowthListener(TARDIS plugin) {
        this.plugin = plugin;
        chance = this.plugin.getConfig().getInt("siege.growth");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlantGrowth(BlockGrowEvent event) {
        Block plant = event.getBlock();
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
            if (area.isInSiegeArea(l) && TARDISConstants.RANDOM.nextInt(100) < chance) {
                // grow an extra step
                if (species.equals(Material.AIR)) {
                    // with cactus and sugar cane the block returned is AIR
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plant.getRelative(BlockFace.UP).setBlockData(plant.getRelative(BlockFace.DOWN).getType().createBlockData()), 3L);
                } else {
                    // get current level
                    Ageable ageable = (Ageable) plant;
                    int data = ageable.getAge();
                    switch (species) {
                        case CACTUS:
                        case SUGAR_CANE:
                            // with cactus and sugar cane the block returned is AIR
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plant.getRelative(BlockFace.UP).setBlockData(plant.getRelative(BlockFace.DOWN).getType().createBlockData()), 3L);
                            break;
                        case BEETROOTS:
                        case CARROTS:
                        case WHEAT:
                        case MELON_STEM:
                        case POTATOES:
                        case PUMPKIN_STEM:
                            // fully grown is 7
                            if (data < 6) {
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> ageable.setAge(data + 2), 3L);
                            }
                            break;
                        case COCOA:
                            // fully grown is 3
                            if (data < 4 && TARDISConstants.RANDOM.nextInt(100) < 25) {
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> ageable.setAge(data + 8), 3L);
                            }
                            break;
                        default: // NETHER_WARTS
                            // fully grown is 3
                            if (data < 2 && TARDISConstants.RANDOM.nextInt(100) < 33) {
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> ageable.setAge(data + 2), 3L);
                            }
                            break;
                    }
                }
                break;
            }
        }
    }
}
