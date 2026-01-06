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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class SonicReplant implements Runnable {

    private final TARDIS plugin;
    private final Block block;
    private final Block under;
    private final Material type;
    private final Material air = Material.AIR;
    private final List<Material> dirt = List.of(Material.DIRT, Material.COARSE_DIRT, Material.PODZOL, Material.GRASS_BLOCK);
    private final Material log = Material.JUNGLE_LOG;
    private final Material soil = Material.FARMLAND;
    private final Material soul = Material.SOUL_SAND;
    private final HashMap<BlockFace, BlockFace> c_data = new HashMap<>();

    public SonicReplant(TARDIS plugin, Block block, Material type) {
        this.plugin = plugin;
        this.block = block;
        under = block.getRelative(BlockFace.DOWN);
        this.type = type;
        c_data.put(BlockFace.NORTH, BlockFace.SOUTH);
        c_data.put(BlockFace.WEST, BlockFace.EAST);
        c_data.put(BlockFace.SOUTH, BlockFace.NORTH);
        c_data.put(BlockFace.EAST, BlockFace.WEST);
    }

    @Override
    public void run() {
        switch (type) {
            case BEETROOT_SEEDS -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.BEETROOTS.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.BEETROOT));
                }
            }
            case CACTUS -> {
                if (dirt.contains(under.getType()) || Tag.SAND.isTagged(under.getType()) && block.getType().equals(air)) {
                    block.setBlockData(Material.CACTUS.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.CACTUS));
                }
            }
            case CARROT -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.CARROTS.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.CARROT));
                }
            }
            case COCOA_BEANS -> {
                if (block.getType().equals(air)) {
                    plugin.getGeneralKeeper().getFaces().forEach((f) -> {
                        // only jungle logs
                        if (block.getRelative(f).getType().equals(log)) {
                            Cocoa cocoa = (Cocoa) Material.COCOA.createBlockData();
                            cocoa.setFacing(c_data.get(f));
                            cocoa.setAge(0);
                            block.setBlockData(cocoa);
                        }
                    });
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.COCOA_BEANS, 1));
                }
            }
            case MELON_SEEDS -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.MELON_STEM.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.MELON_SEEDS));
                }
            }
            case NETHER_WART -> {
                if (under.getType().equals(soul) && block.getType().equals(air)) {
                    block.setBlockData(Material.NETHER_WART.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.NETHER_WART));
                }
            }
            case POTATO -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.POTATOES.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.POTATO));
                }
            }
            case PUMPKIN_SEEDS -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.PUMPKIN_STEM.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.PUMPKIN_SEEDS));
                }
            }
            case SUGAR_CANE -> {
                if (dirt.contains(under.getType()) || Tag.SAND.isTagged(under.getType()) && block.getType().equals(air)) {
                    block.setBlockData(Material.SUGAR_CANE.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.SUGAR_CANE));
                }
            }
            case WHEAT_SEEDS -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.WHEAT.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.WHEAT_SEEDS));
                }
            }
            case SWEET_BERRIES -> {
                if (dirt.contains(under.getType()) && block.getType().equals(air)) {
                    block.setBlockData(Material.SWEET_BERRY_BUSH.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.SWEET_BERRIES));
                }
            }
            case PITCHER_POD -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.PITCHER_CROP.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.PITCHER_PLANT));
                }
            }
            case TORCHFLOWER, TORCHFLOWER_SEEDS -> {
                if (under.getType().equals(soil) && block.getType().equals(air)) {
                    block.setBlockData(Material.TORCHFLOWER_CROP.createBlockData());
                } else {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemStack.of(Material.TORCHFLOWER));
                }
            }
            default -> {

            }
        }
    }
}
