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
package me.eccentric_nz.tardisweepingangels.monsters.silurians;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.eccentric_nz.TARDIS.travel.Check;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class CaveFinder {

    private static final List<BlockFace> directions = Arrays.asList(BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH);

    public static Location searchSpawnPoint(Location playerLocation) {
        World w = playerLocation.getWorld();
        int startx = playerLocation.getBlockX();
        int starty = playerLocation.getBlockY();
        int startz = playerLocation.getBlockZ();
        if (worldCheck(w)) {
            int plusx = startx + 24;
            int plusz = startz + 24;
            int minusx = startx - 24;
            int minusz = startz - 24;
            int step = 4;
            // search in a random direction
            Collections.shuffle(directions);
                    for (int i = 0; i < 4; i++) {
                        switch (directions.get(i)) {
                            case EAST -> {
                                for (int east = plusx; east > startx; east -= step) {
                                    Check chk = isThereRoom(w, east, startz, starty);
                                    if (chk.isSafe()) {
                                        return new Location(w, east, chk.getY(), startz);
                                    }
                                }
                            }
                            case SOUTH -> {
                                for (int south = plusz; south > startz; south -= step) {
                                    Check chk = isThereRoom(w, startx, south, starty);
                                    if (chk.isSafe()) {
                                        return new Location(w, startx, chk.getY(), south);
                                    }
                                }
                            }
                            case WEST -> {
                                for (int west = minusx; west < startx; west += step) {
                                    Check chk = isThereRoom(w, west, startz, starty);
                                    if (chk.isSafe()) {
                                        return new Location(w, west, chk.getY(), startz);
                                    }
                                }
                            }
                            default -> { // NORTH
                                for (int north = minusz; north < startz; north += step) {
                                    Check chk = isThereRoom(w, startx, north, starty);
                                    if (chk.isSafe()) {
                                        return new Location(w, startx, chk.getY(), north);
                                    }
                                }
                            }
                        }
                    }
        }
        return null;
    }

    private static Check isThereRoom(World w, int x, int z, int sy) {
        Check ret = new Check();
        ret.setSafe(false);
        for (int y = sy + 8; y > sy - 8; y--) {
            if (w.getBlockAt(x, y, z).getType().isAir()) {
                int yy = getLowestAirBlock(w, x, y, z);
                // check there is enough height for the Silurian
                if (yy <= y - 2 && Tag.BASE_STONE_OVERWORLD.isTagged(w.getBlockAt(x - 1, yy - 1, z - 1).getType())) {
                    // check there is room for the Silurian
                    if (w.getBlockAt(x - 1, yy, z - 1).getType().isAir()
                            && w.getBlockAt(x - 1, yy, z).getType().isAir()
                            && w.getBlockAt(x - 1, yy, z + 1).getType().isAir()
                            && w.getBlockAt(x, yy, z - 1).getType().isAir()
                            && w.getBlockAt(x, yy, z + 1).getType().isAir()
                            && w.getBlockAt(x + 1, yy, z - 1).getType().isAir()
                            && w.getBlockAt(x + 1, yy, z).getType().isAir()
                            && w.getBlockAt(x + 1, yy, z + 1).getType().isAir()) {
                        ret.setSafe(true);
                        ret.setY(yy);
                    }
                }
            }
        }
        return ret;
    }

    private static int getLowestAirBlock(World w, int x, int y, int z) {
        int yy = y;
        while (w.getBlockAt(x, yy, z).getRelative(BlockFace.DOWN).getType().isAir()) {
            yy--;
        }
        return yy;
    }

    private static boolean worldCheck(World w) {
        if (w.getGenerator() != null && !w.getGenerator().shouldGenerateCaves()) {
            // caves not generated
            return false;
        }
        Location spawn = w.getSpawnLocation();
        int y = w.getHighestBlockYAt(spawn);
        if (y < 15) {
            return false;
        } else {
            // move 20 blocks north
            spawn.setZ(spawn.getBlockZ() - 20);
            int ny = w.getHighestBlockYAt(spawn);
            spawn.setX(spawn.getBlockZ() + 20);
            int ey = w.getHighestBlockYAt(spawn);
            spawn.setZ(spawn.getBlockZ() + 20);
            int sy = w.getHighestBlockYAt(spawn);
            return (y != ny && y != ey && y != sy);
        }
    }
}
