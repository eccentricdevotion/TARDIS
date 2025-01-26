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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HeatBlockRunnable implements Runnable {

    private static final List<Vector> INNER_VECTORS = new ArrayList<>(List.of(new Vector(-1.0, 0.0, 1.0), new Vector(0.0, 0.0, 1.0), new Vector(1.0, 0.0, 1.0), new Vector(-1.0, 0.0, 0.0), new Vector(1.0, 0.0, 0.0), new Vector(-1.0, 0.0, -1.0), new Vector(0.0, 0.0, -1.0), new Vector(1.0, 0.0, -1.0)));
    private static final List<Vector> OUTER_VECTORS = new ArrayList<>(List.of(new Vector(-2.0, 0.0, 2.0), new Vector(-1.0, 0.0, 2.0), new Vector(0.0, 0.0, 2.0), new Vector(1.0, 0.0, 2.0), new Vector(2.0, 0.0, 2.0), new Vector(-2.0, 0.0, 1.0), new Vector(2.0, 0.0, 1.0), new Vector(-2.0, 0.0, 0.0), new Vector(2.0, 0.0, 0.0), new Vector(-2.0, 0.0, -1.0), new Vector(2.0, 0.0, -1.0), new Vector(-2.0, 0.0, -2.0), new Vector(-1.0, 0.0, -2.0), new Vector(0.0, 0.0, -2.0), new Vector(1.0, 0.0, -2.0), new Vector(2.0, 0.0, -2.0)));
    private final TARDIS plugin;

    public HeatBlockRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (String s : plugin.getTrackerKeeper().getHeatBlocks()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(s);
            if (location != null && location.getBlock().getType() == Material.BARRIER) {
                // get the item display
                ItemDisplay display = TARDISDisplayItemUtils.get(location.getBlock());
                if (display != null) {
                    ItemStack is = display.getItemStack();
                    if (is.getType() == Material.RED_CONCRETE && is.hasItemMeta()
                            && is.getItemMeta().getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.STRING)) {
                        // it's a heat block
                        meltBlock(location);
                    }
                }
            }
        }
    }

    private void meltBlock(Location location) {
        if (location != null) {
            int inner_count = 0;
            World world = location.getWorld();
            if (world != null) {
                Collections.shuffle(INNER_VECTORS);
                for (Vector inner : INNER_VECTORS) {
                    if (TARDISConstants.RANDOM.nextInt(100) > 25) {
                        Block ib = location.getWorld().getHighestBlockAt(location.clone().add(inner));
                        Material im = ib.getType();
                        if (im.equals(Material.SNOW)) {
                            Snow snow = (Snow) ib.getBlockData();
                            int layers = snow.getLayers() - 1;
                            if (layers > 0) {
                                snow.setLayers(layers);
                                ib.setBlockData(snow);
                            } else {
                                ib.setBlockData(TARDISConstants.AIR);
                            }
                            return;
                        } else if (im.equals(Material.ICE) || im.equals(Material.PACKED_ICE)) {
                            ib.setBlockData(TARDISConstants.WATER);
                            return;
                        }
                        inner_count++;
                        if (inner_count == 8) {
                            Collections.shuffle(OUTER_VECTORS);
                            // not melting left in the inner blocks
                            for (Vector outer : OUTER_VECTORS) {
                                Block ob = location.getWorld().getHighestBlockAt(location.clone().add(outer));
                                Material om = ob.getType();
                                if (om.equals(Material.SNOW)) {
                                    ob.setBlockData(TARDISConstants.AIR);
                                    return;
                                } else if (om.equals(Material.ICE) || om.equals(Material.PACKED_ICE)) {
                                    ob.setBlockData(TARDISConstants.WATER);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
