/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISCaveFinder {

    private final TARDIS plugin;

    public TARDISCaveFinder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public Location searchCave(Player p, int id) {
        Location l = null;
        // get the current TARDIS location
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
        if (rsc.resultSet()) {
            World w = rsc.getWorld();
            int startx = rsc.getX();
            int startz = rsc.getZ();
            COMPASS d = rsc.getDirection();
            // Assume all non-nether/non-end world environments are NORMAL
            if (!w.getEnvironment().equals(World.Environment.NETHER) && !w.getEnvironment().equals(World.Environment.THE_END)) {
                int limitx = 2000;
                int limitz = 2000;
                int step = 25;
                // search in a random direction
                Integer[] directions = new Integer[]{0, 1, 2, 3};
                Collections.shuffle(Arrays.asList(directions));
                for (int i = 0; i < 4; i++) {
                    switch (directions[i]) {
                        case 0:
                            // east
                            p.sendMessage(plugin.pluginName + "Looking east...");
                            for (int east = startx; east < east + limitx; east += step) {
                                Check chk = isThereRoom(w, east, startz, d);
                                if (chk.isSafe()) {
                                    p.sendMessage(plugin.pluginName + "Cave found in an easterly direction!");
                                    return new Location(w, east, chk.getY(), startz);
                                }
                            }
                            break;
                        case 1:
                            // south
                            p.sendMessage(plugin.pluginName + "Looking south...");
                            for (int south = startz; south < south + limitz; south += step) {
                                Check chk = isThereRoom(w, startx, south, d);
                                if (chk.isSafe()) {
                                    p.sendMessage(plugin.pluginName + "Cave found in a southerly direction!");
                                    return new Location(w, startx, chk.getY(), south);
                                }
                            }
                            break;
                        case 2:
                            // west
                            p.sendMessage(plugin.pluginName + "Looking west...");
                            for (int west = startx; west > west - limitx; west -= step) {
                                Check chk = isThereRoom(w, west, startz, d);
                                if (chk.isSafe()) {
                                    p.sendMessage(plugin.pluginName + "Cave found in a westerly direction!");
                                    return new Location(w, west, chk.getY(), startz);
                                }
                            }
                            break;
                        case 3:
                            // north
                            p.sendMessage(plugin.pluginName + "Looking north...");
                            for (int north = startz; north > north - limitz; north -= step) {
                                Check chk = isThereRoom(w, startx, north, d);
                                if (chk.isSafe()) {
                                    p.sendMessage(plugin.pluginName + "Cave found in a northerly direction!");
                                    return new Location(w, startx, chk.getY(), north);
                                }
                            }
                            break;
                    }
                }
            } else {
                p.sendMessage(plugin.pluginName + "You cannot travel to a cave in the " + w.getEnvironment().toString() + "!");
            }
        } else {
            p.sendMessage(plugin.pluginName + "Could not get the TARDIS's current location!");
        }
        return l;
    }

    private Check isThereRoom(World w, int x, int z, COMPASS d) {
        Check ret = new Check();
        ret.setSafe(false);
        for (int y = 35; y > 14; y--) {
            if (w.getBlockAt(x, y, z).getType().equals(Material.AIR)) {
                plugin.debug("Found AIR");
                int yy = getLowestAirBlock(w, x, y, z);
                // check there is enough height for the police box
                if (yy <= y - 3 && w.getBlockAt(x - 1, yy - 1, z - 1).getType().equals(Material.STONE)) {
                    // check there is room for the police box
                    if (w.getBlockAt(x - 1, yy, z - 1).getType().equals(Material.AIR)
                            && w.getBlockAt(x - 1, yy, z).getType().equals(Material.AIR)
                            && w.getBlockAt(x - 1, yy, z + 1).getType().equals(Material.AIR)
                            && w.getBlockAt(x, yy, z - 1).getType().equals(Material.AIR)
                            && w.getBlockAt(x, yy, z + 1).getType().equals(Material.AIR)
                            && w.getBlockAt(x + 1, yy, z - 1).getType().equals(Material.AIR)
                            && w.getBlockAt(x + 1, yy, z).getType().equals(Material.AIR)
                            && w.getBlockAt(x + 1, yy, z + 1).getType().equals(Material.AIR)) {
                        // finally check there is space to exit the police box
                        boolean safe = false;
                        switch (d) {
                            case NORTH:
                                if (w.getBlockAt(x - 1, yy, z + 2).getType().equals(Material.AIR)
                                        && w.getBlockAt(x, yy, z + 2).getType().equals(Material.AIR)
                                        && w.getBlockAt(x + 1, yy, z + 2).getType().equals(Material.AIR)) {
                                    safe = true;
                                }
                                break;
                            case WEST:
                                if (w.getBlockAt(x + 2, yy, z - 1).getType().equals(Material.AIR)
                                        && w.getBlockAt(x + 2, yy, z).getType().equals(Material.AIR)
                                        && w.getBlockAt(x + 2, yy, z + 1).getType().equals(Material.AIR)) {
                                    safe = true;
                                }
                                break;
                            case SOUTH:
                                if (w.getBlockAt(x - 1, yy, z - 2).getType().equals(Material.AIR)
                                        && w.getBlockAt(x, yy, z - 2).getType().equals(Material.AIR)
                                        && w.getBlockAt(x + 1, yy, z - 2).getType().equals(Material.AIR)) {
                                    safe = true;
                                }
                                break;
                            default:
                                if (w.getBlockAt(x - 2, yy, z - 1).getType().equals(Material.AIR)
                                        && w.getBlockAt(x - 2, yy, z).getType().equals(Material.AIR)
                                        && w.getBlockAt(x - 2, yy, z + 1).getType().equals(Material.AIR)) {
                                    safe = true;
                                }
                                break;
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

    private int getLowestAirBlock(World w, int x, int y, int z) {
        int yy = y;
        while (w.getBlockAt(x, yy, z).getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
            yy--;
        }
        return yy;
    }

    private class Check {

        private boolean safe;
        private int y;

        public boolean isSafe() {
            return safe;
        }

        public void setSafe(boolean safe) {
            this.safe = safe;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
