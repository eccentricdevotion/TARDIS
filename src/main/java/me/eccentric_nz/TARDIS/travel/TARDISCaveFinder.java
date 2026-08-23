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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISCaveFinder {

    private final TARDIS plugin;
    private final List<BlockFace> directions = new ArrayList<>(List.of(BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH));

    public TARDISCaveFinder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static int getLowestAirBlock(World world, int x, int y, int z) {
        while (world.getBlockAt(x, y, z).getRelative(BlockFace.DOWN).getType().isAir() && y > world.getMinHeight() + 7) {
            y--;
        }
        return y;
    }

    public static boolean worldCheck(World world) {
        if (world.getGenerator() != null && !world.getGenerator().shouldGenerateCaves()) {
            // caves not generated
            return false;
        }
        Location spawn = world.getSpawnLocation();
        int y = world.getHighestBlockYAt(spawn);
        if (y < world.getMinHeight() + 15) {
            // possibly a flat world
            return false;
        } else if (world.getBlockAt(spawn.getBlockX(), world.getMinHeight(), spawn.getBlockZ()).getType().isAir()) {
            // possibly a void world
            return false;
        } else {
            // move 20 blocks north
            spawn.setZ(spawn.getBlockZ() - 100);
            int ny = world.getHighestBlockYAt(spawn);
            spawn.setX(spawn.getBlockX() + 100);
            int ey = world.getHighestBlockYAt(spawn);
            spawn.setZ(spawn.getBlockZ() + 100);
            int sy = world.getHighestBlockYAt(spawn);
            // possibly a flat world too
            return (y != ny || y != ey || y != sy);
        }
    }

    public Location searchCave(Player player, int id, Location location) {
        // get the current TARDIS location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            COMPASS d = rsc.getCurrent().direction();
            return find(location, d, player, 16, 4);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
        }
        return null;
    }

    public Location searchCave(Player player, int id) {
        // get the current TARDIS location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            Current current = rsc.getCurrent();
            COMPASS d = current.direction();
            return find(current.location(), d, player, 2048, 32);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
        }
        return null;
    }

    private Location find(Location location, COMPASS direction, Player player, int limit, int step) {
        World w = location.getWorld();
        int startx = location.getBlockX();
        int startz = location.getBlockZ();
        // assume all non-nether/non-end world environments are NORMAL
        boolean hoth = (w.getGenerator() != null && w.getGenerator().getClass().getName().contains("hothgenerator"));
        if (!w.getEnvironment().equals(World.Environment.NETHER) && !w.getEnvironment().equals(World.Environment.THE_END) && !hoth) {
            if (worldCheck(w)) {
                int plusx = startx + limit;
                int plusz = startz + limit;
                int minusx = startx - limit;
                int minusz = startz - limit;
                // search in a random direction
                Collections.shuffle(directions);
                for (int i = 0; i < 4; i++) {
                    switch (directions.get(i)) {
                        case EAST -> {
                            if (player != null) {
                                plugin.getMessenger().sendStatus(player, "LOOK_E");
                            }
                            for (int east = startx; east < plusx; east += step) {
                                Check chk = isThereRoom(w, east, startz, direction);
                                if (chk.isSafe()) {
                                    if (player != null) {
                                        plugin.getMessenger().sendStatus(player, "CAVE_E");
                                    }
                                    return new Location(w, east, chk.getY(), startz);
                                }
                            }
                        }
                        case SOUTH -> {
                            if (player != null) {
                                plugin.getMessenger().sendStatus(player, "LOOK_S");
                            }
                            for (int south = startz; south < plusz; south += step) {
                                Check chk = isThereRoom(w, startx, south, direction);
                                if (chk.isSafe()) {
                                    if (player != null) {
                                        plugin.getMessenger().sendStatus(player, "CAVE_S");
                                    }
                                    return new Location(w, startx, chk.getY(), south);
                                }
                            }
                        }
                        case WEST -> {
                            if (player != null) {
                                plugin.getMessenger().sendStatus(player, "LOOK_W");
                            }
                            for (int west = startx; west > minusx; west -= step) {
                                Check chk = isThereRoom(w, west, startz, direction);
                                if (chk.isSafe()) {
                                    if (player != null) {
                                        plugin.getMessenger().sendStatus(player, "CAVE_W");
                                    }
                                    return new Location(w, west, chk.getY(), startz);
                                }
                            }
                        }
                        default -> { // NORTH
                            if (player != null) {
                                plugin.getMessenger().sendStatus(player, "LOOK_N");
                            }
                            for (int north = startz; north > minusz; north -= step) {
                                Check chk = isThereRoom(w, startx, north, direction);
                                if (chk.isSafe()) {
                                    if (player != null) {
                                        plugin.getMessenger().sendStatus(player, "CAVE_N");
                                    }
                                    return new Location(w, startx, chk.getY(), north);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            String env = (w.getGenerator().getClass().getName().contains("hothgenerator")) ? "Hoth World System" : w.getEnvironment().toString();
            if (player != null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CAVE_NO_TRAVEL", env);
            }
        }
        return null;
    }

    private Check isThereRoom(World world, int x, int z, COMPASS direction) {
        Check ret = new Check();
        ret.setSafe(false);
        // the probability of cave generation is higher at y=-56 —> y=47
        for (int y = 35; y > world.getMinHeight() + 14; y--) {
            if (world.getBlockAt(x, y, z).getType().isAir()) {
                int yy = getLowestAirBlock(world, x, y, z);
                // check there is enough height for the police box
                if (yy <= y - 3 && Tag.BASE_STONE_OVERWORLD.isTagged(world.getBlockAt(x - 1, yy - 1, z - 1).getType())) {
                    // check there is room for the police box
                    if (world.getBlockAt(x - 1, yy, z - 1).getType().isAir() && world.getBlockAt(x - 1, yy, z).getType().isAir() && world.getBlockAt(x - 1, yy, z + 1).getType().isAir() && world.getBlockAt(x, yy, z - 1).getType().isAir() && world.getBlockAt(x, yy, z + 1).getType().isAir() && world.getBlockAt(x + 1, yy, z - 1).getType().isAir() && world.getBlockAt(x + 1, yy, z).getType().isAir() && world.getBlockAt(x + 1, yy, z + 1).getType().isAir()) {
                        // finally check there is space to exit the police box
                        boolean safe = false;
                        switch (direction) {
                            case NORTH -> {
                                if (world.getBlockAt(x - 1, yy, z + 2).getType().isAir() && world.getBlockAt(x, yy, z + 2).getType().isAir() && world.getBlockAt(x + 1, yy, z + 2).getType().isAir()) {
                                    safe = true;
                                }
                            }
                            case WEST -> {
                                if (world.getBlockAt(x + 2, yy, z - 1).getType().isAir() && world.getBlockAt(x + 2, yy, z).getType().isAir() && world.getBlockAt(x + 2, yy, z + 1).getType().isAir()) {
                                    safe = true;
                                }
                            }
                            case SOUTH -> {
                                if (world.getBlockAt(x - 1, yy, z - 2).getType().isAir() && world.getBlockAt(x, yy, z - 2).getType().isAir() && world.getBlockAt(x + 1, yy, z - 2).getType().isAir()) {
                                    safe = true;
                                }
                            }
                            default -> {
                                if (world.getBlockAt(x - 2, yy, z - 1).getType().isAir() && world.getBlockAt(x - 2, yy, z).getType().isAir() && world.getBlockAt(x - 2, yy, z + 1).getType().isAir()) {
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
}
