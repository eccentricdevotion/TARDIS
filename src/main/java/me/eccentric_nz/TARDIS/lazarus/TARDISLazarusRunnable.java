/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Wall;

/**
 * The Genetic Manipulation Device uses hypersonic sound waves to destabilise the cell structure, then a metagenic
 * programme to manipulate the coding in the protein strands.
 *
 * @author eccentric_nz
 */
class TARDISLazarusRunnable implements Runnable {

    private static final int LOOPS = 12;
    private final TARDIS plugin;
    private final Block block;
    private final Wall NORTH_SOUTH = (Wall) Bukkit.createBlockData("minecraft:cobblestone_wall[east=tall,north=none,south=none,up=false,waterlogged=false,west=tall]");
    private final Wall EAST_WEST = (Wall) Bukkit.createBlockData("minecraft:cobblestone_wall[east=none,north=tall,south=tall,up=false,waterlogged=false,west=none]");
    private final Wall NORTH_SOUTH_MOSSY = (Wall) Bukkit.createBlockData("minecraft:mossy_cobblestone_wall[east=tall,north=none,south=none,up=false,waterlogged=false,west=tall]");
    private final Wall EAST_WEST_MOSSY = (Wall) Bukkit.createBlockData("minecraft:mossy_cobblestone_wall[east=none,north=tall,south=tall,up=false,waterlogged=false,west=none]");
    private Wall MOSSY;
    private Wall PLAIN;
    private int taskID;
    private int i = 0;

    TARDISLazarusRunnable(TARDIS plugin, Block block) {
        this.plugin = plugin;
        this.block = block;
    }

    @Override
    public void run() {
        if (i < LOOPS) {
            plugin.getGeneralKeeper().getFaces().forEach((face) -> {
                switch (face) {
                    case EAST, WEST -> {
                        MOSSY = EAST_WEST_MOSSY;
                        PLAIN = EAST_WEST;
                    }
                    default -> {
                        MOSSY = NORTH_SOUTH_MOSSY;
                        PLAIN = NORTH_SOUTH;
                    }
                }
                if ((i % 4) == face.ordinal()) {
                    // set mossy
                    block.getRelative(face).setBlockData(MOSSY);
                    block.getRelative(face).getRelative(BlockFace.UP).setBlockData(MOSSY);
                } else {
                    // set plain
                    block.getRelative(face).setBlockData(PLAIN);
                    block.getRelative(face).getRelative(BlockFace.UP).setBlockData(PLAIN);
                }
            });
            i++;
        } else {
            plugin.getGeneralKeeper().getFaces().forEach((face) -> {
                switch (face) {
                    case EAST, WEST -> PLAIN = EAST_WEST;
                    default -> PLAIN = NORTH_SOUTH;
                }
                block.getRelative(face).setBlockData(PLAIN);
                block.getRelative(face).getRelative(BlockFace.UP).setBlockData(PLAIN);
            });
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
