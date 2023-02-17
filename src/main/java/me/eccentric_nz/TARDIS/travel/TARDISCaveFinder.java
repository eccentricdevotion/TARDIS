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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISCaveFinder {

    private final TARDIS plugin;

    public TARDISCaveFinder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static int getLowestAirBlock(World w, int x, int y, int z) {
        int yy = y;
        while (w.getBlockAt(x, yy, z).getRelative(BlockFace.DOWN).getType().isAir() && yy > w.getMinHeight() + 7) {
            yy--;
        }
        return yy;
    }

    public Location searchCave(Player p, int id) {
        // get the current TARDIS location
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
        if (rsc.resultSet()) {
            World w = rsc.getWorld();
            int startx = rsc.getX();
            int startz = rsc.getZ();
            COMPASS d = rsc.getDirection();
            // Assume all non-nether/non-end world environments are NORMAL
            boolean hoth = (w.getGenerator() != null && w.getGenerator().getClass().getName().contains("hothgenerator"));
            if (!w.getEnvironment().equals(World.Environment.NETHER) && !w.getEnvironment().equals(World.Environment.THE_END) && !hoth) {
                if (worldCheck(w)) {
                    int plusx = startx + 2000;
                    int plusz = startz + 2000;
                    int minusx = startx - 2000;
                    int minusz = startz - 2000;
                    int step = 25;
                    // search in a random direction
                    Integer[] directions = new Integer[]{0, 1, 2, 3};
                    Collections.shuffle(Arrays.asList(directions));
                    for (int i = 0; i < 4; i++) {
                        switch (directions[i]) {
                            case 0 -> {
                                // east
                                TARDISMessage.send(p, "LOOK_E");
                                for (int east = startx; east < plusx; east += step) {
                                    Check chk = isThereRoom(w, east, startz, d);
                                    if (chk.isSafe()) {
                                        TARDISMessage.send(p, "CAVE_E");
                                        return new Location(w, east, chk.getY(), startz);
                                    }
                                }
                            }
                            case 1 -> {
                                // south
                                TARDISMessage.send(p, "LOOK_S");
                                for (int south = startz; south < plusz; south += step) {
                                    Check chk = isThereRoom(w, startx, south, d);
                                    if (chk.isSafe()) {
                                        TARDISMessage.send(p, "CAVE_S");
                                        return new Location(w, startx, chk.getY(), south);
                                    }
                                }
                            }
                            case 2 -> {
                                // west
                                TARDISMessage.send(p, "LOOK_W");
                                for (int west = startx; west > minusx; west -= step) {
                                    Check chk = isThereRoom(w, west, startz, d);
                                    if (chk.isSafe()) {
                                        TARDISMessage.send(p, "CAVE_W");
                                        return new Location(w, west, chk.getY(), startz);
                                    }
                                }
                            }
                            case 3 -> {
                                // north
                                TARDISMessage.send(p, "LOOK_N");
                                for (int north = startz; north > minusz; north -= step) {
                                    Check chk = isThereRoom(w, startx, north, d);
                                    if (chk.isSafe()) {
                                        TARDISMessage.send(p, "CAVE_N");
                                        return new Location(w, startx, chk.getY(), north);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                String env = (w.getGenerator().getClass().getName().contains("hothgenerator")) ? "Hoth World System" : w.getEnvironment().toString();
                TARDISMessage.send(p, "CAVE_NO_TRAVEL", env);
            }
        } else {
            TARDISMessage.send(p, "CURRENT_NOT_FOUND");
        }
        return null;
    }

    private Check isThereRoom(World w, int x, int z, COMPASS d) {
        Check ret = new Check();
        ret.setSafe(false);
        for (int y = 35; y > w.getMinHeight() + 14; y--) {
            if (w.getBlockAt(x, y, z).getType().isAir()) {
                int yy = getLowestAirBlock(w, x, y, z);
                // check there is enough height for the police box
                if (yy <= y - 3 && !Tag.BASE_STONE_OVERWORLD.isTagged(w.getBlockAt(x - 1, yy - 1, z - 1).getType())) {
                    // check there is room for the police box
                    if (w.getBlockAt(x - 1, yy, z - 1).getType().isAir() && w.getBlockAt(x - 1, yy, z).getType().isAir() && w.getBlockAt(x - 1, yy, z + 1).getType().isAir() && w.getBlockAt(x, yy, z - 1).getType().isAir() && w.getBlockAt(x, yy, z + 1).getType().isAir() && w.getBlockAt(x + 1, yy, z - 1).getType().isAir() && w.getBlockAt(x + 1, yy, z).getType().isAir() && w.getBlockAt(x + 1, yy, z + 1).getType().isAir()) {
                        // finally check there is space to exit the police box
                        boolean safe = false;
                        switch (d) {
                            case NORTH -> {
                                if (w.getBlockAt(x - 1, yy, z + 2).getType().isAir() && w.getBlockAt(x, yy, z + 2).getType().isAir() && w.getBlockAt(x + 1, yy, z + 2).getType().isAir()) {
                                    safe = true;
                                }
                            }
                            case WEST -> {
                                if (w.getBlockAt(x + 2, yy, z - 1).getType().isAir() && w.getBlockAt(x + 2, yy, z).getType().isAir() && w.getBlockAt(x + 2, yy, z + 1).getType().isAir()) {
                                    safe = true;
                                }
                            }
                            case SOUTH -> {
                                if (w.getBlockAt(x - 1, yy, z - 2).getType().isAir() && w.getBlockAt(x, yy, z - 2).getType().isAir() && w.getBlockAt(x + 1, yy, z - 2).getType().isAir()) {
                                    safe = true;
                                }
                            }
                            default -> {
                                if (w.getBlockAt(x - 2, yy, z - 1).getType().isAir() && w.getBlockAt(x - 2, yy, z).getType().isAir() && w.getBlockAt(x - 2, yy, z + 1).getType().isAir()) {
                                    safe = true;
                                }
                            }
                        }
                        if (safe) {
                            ret.setSafe(true);
                            ret.setY(yy);
                        }
                    }
                }
            }
        }
        return ret;
    }

    private boolean worldCheck(World w) {
        if (w.getGenerator() != null && !w.getGenerator().shouldGenerateCaves()) {
            // caves not generated
            return false;
        }
        Location spawn = w.getSpawnLocation();
        int y = w.getHighestBlockYAt(spawn);
        if (y < w.getMinHeight() + 15) {
            // possibly a flat world
            return false;
        } else if (w.getBlockAt(spawn.getBlockX(), w.getMinHeight(), spawn.getBlockZ()).getType().isAir()) {
            // possibly a void world
            return false;
        } else {
            // move 20 blocks north
            spawn.setZ(spawn.getBlockZ() - 100);
            int ny = w.getHighestBlockYAt(spawn);
            spawn.setX(spawn.getBlockX() + 100);
            int ey = w.getHighestBlockYAt(spawn);
            spawn.setZ(spawn.getBlockZ() + 100);
            int sy = w.getHighestBlockYAt(spawn);
            // possibly a flat world too
            return (y != ny || y != ey || y != sy);
        }
    }

    public static class Check {

        private boolean safe;
        private int y;

        boolean isSafe() {
            return safe;
        }

        void setSafe(boolean safe) {
            this.safe = safe;
        }

        int getY() {
            return y;
        }

        void setY(int y) {
            this.y = y;
        }
    }
}
