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
package me.eccentric_nz.TARDIS.travel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * All things related to time travel.
 *
 * All TARDISes built after a certain point, including the Type 40 the Doctor
 * uses, have a mathematically modelled duplicate of the Eye of harmony with all
 * its attendant features.
 *
 * @author eccentric_nz
 */
public class TARDISTimeTravel {

    private static final int[] startLoc = new int[6];
    private Location dest;
    private final TARDIS plugin;
    private final int attempts;

    public TARDISTimeTravel(TARDIS plugin) {
        this.plugin = plugin;
        // add good materials
        this.attempts = plugin.getConfig().getInt("travel.random_attempts");
    }

    /**
     * Retrieves a random location determined from the TARDIS repeater or
     * terminal settings.
     *
     * @param p a player object used to check permissions against.
     * @param rx the data bit setting of the x-repeater, this determines the
     * distance in the x direction.
     * @param rz the data bit setting of the z-repeater, this determines the
     * distance in the z direction.
     * @param ry the data bit setting of the y-repeater, this determines the
     * multiplier for both the x and z directions.
     * @param d the direction the TARDIS Police Box faces.
     * @param e the environment(s) the player has chosen (or is allowed) to
     * travel to.
     * @param this_world the world the Police Box is currently in
     * @param malfunction whether there should be a malfunction
     * @param current
     * @return a random Location
     */
    @SuppressWarnings("deprecation")
    public Location randomDestination(Player p, byte rx, byte rz, byte ry, COMPASS d, String e, World this_world, boolean malfunction, Location current) {
        int startx, starty, startz, resetx, resetz, listlen;
        World randworld;
        int count;
        Random rand = new Random();
        // get max_radius from config
        int max = plugin.getConfig().getInt("travel.tp_radius");
        int quarter = (max + 4 - 1) / 4;
        int range = quarter + 1;
        int wherex = 0, highest = 252, wherez = 0;
        // get worlds
        Set<String> worldlist = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
        List<World> allowedWorlds = new ArrayList<>();

        if (e.equals("THIS")) {
            allowedWorlds.add(this_world);
        } else {
            worldlist.forEach((o) -> {
                World ww = plugin.getServer().getWorld(o);
                if (ww != null) {
                    String env = ww.getEnvironment().toString();
                    // Catch all non-nether and non-end ENVIRONMENT types and assume they're normal
                    if (!env.equals("NETHER") && !env.equals("THE_END")) {
                        env = "NORMAL";
                    }
                    if (e.equalsIgnoreCase(env)) {
                        if (plugin.getConfig().getBoolean("travel.include_default_world") || !plugin.getConfig().getBoolean("creation.default_world")) {
                            if (plugin.getConfig().getBoolean("worlds." + o) || malfunction) {
                                allowedWorlds.add(ww);
                            }
                        } else {
                            if (!o.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                                if (plugin.getConfig().getBoolean("worlds." + o) || malfunction) {
                                    allowedWorlds.add(ww);
                                }
                            }
                        }
                    }
                    // remove the world the Police Box is in
                    if (allowedWorlds.size() > 1 && allowedWorlds.contains(this_world)) {
                        allowedWorlds.remove(this_world);
                    }
                    // remove the world if the player doesn't have permission
                    if (allowedWorlds.size() > 1 && plugin.getConfig().getBoolean("travel.per_world_perms") && !p.hasPermission("tardis.travel." + o)) {
                        allowedWorlds.remove(ww);
                    }
                }
            });
        }
        listlen = allowedWorlds.size();
        // random world
        randworld = allowedWorlds.get(rand.nextInt(listlen));
        if (randworld.getEnvironment().equals(Environment.NETHER)) {
            for (int n = 0; n < attempts; n++) {
                wherex = randomX(rand, range, quarter, rx, ry, e, current);
                wherez = randomZ(rand, range, quarter, rz, ry, e, current);
                if (safeNether(randworld, wherex, wherez, d, p)) {
                    break;
                }
            }
        }
        if (randworld.getEnvironment().equals(Environment.THE_END)) {
            if (plugin.getPlanetsConfig().getBoolean("planets." + randworld.getName() + ".void")) {
                // any location will do!
                int voidx = randomX(rand, range, quarter, rx, ry, e, current);
                int voidy = rand.nextInt(240) + 5;
                int voidz = randomZ(rand, range, quarter, rz, ry, e, current);
                return new Location(randworld, voidx, voidy, voidz);
            }
            for (int n = 0; n < attempts; n++) {
                wherex = rand.nextInt(240);
                wherez = rand.nextInt(240);
                wherex -= 120;
                wherez -= 120;
                // get the spawn point
                Location endSpawn = randworld.getSpawnLocation();
                highest = randworld.getHighestBlockYAt(endSpawn.getBlockX() + wherex, endSpawn.getBlockZ() + wherez);
                if (highest > 40) {
                    Block currentBlock = randworld.getBlockAt(wherex, highest, wherez);
                    Location chunk_loc = currentBlock.getLocation();
                    if (plugin.getPluginRespect().getRespect(chunk_loc, new Parameters(p, FLAG.getNoMessageFlags()))) {
                        while (!randworld.getChunkAt(chunk_loc).isLoaded()) {
                            randworld.getChunkAt(chunk_loc).load();
                        }
                        // get start location for checking there is enough space
                        int gsl[] = getStartLocation(chunk_loc, d);
                        startx = gsl[0];
                        resetx = gsl[1];
                        starty = chunk_loc.getBlockY() + 1;
                        startz = gsl[2];
                        resetz = gsl[3];
                        count = safeLocation(startx, starty, startz, resetx, resetz, randworld, d);
                    } else {
                        count = 1;
                    }
                } else {
                    count = 1;
                }
                if (count == 0) {
                    break;
                }
            }
            dest = (highest > 0) ? new Location(randworld, wherex, highest, wherez) : null;
        }
        // Assume every non-nether/non-END world qualifies as NORMAL.
        if (!randworld.getEnvironment().equals(Environment.NETHER) && !randworld.getEnvironment().equals(Environment.THE_END)) {
            if (plugin.getPlanetsConfig().getBoolean("planets." + randworld.getName() + ".void")) {
                // any location will do!
                int voidx = randomX(rand, range, quarter, rx, ry, e, current);
                int voidy = rand.nextInt(240) + 5;
                int voidz = randomZ(rand, range, quarter, rz, ry, e, current);
                return new Location(randworld, voidx, voidy, voidz);
            }
            long timeout = System.currentTimeMillis() + (plugin.getConfig().getLong("travel.timeout") * 1000);
            while (true) {
                if (System.currentTimeMillis() < timeout) {
                    // reset count
                    count = 0;
                    // randomX(Random rand, int range, int quarter, byte rx, byte ry, int max)
                    wherex = randomX(rand, range, quarter, rx, ry, e, current);
                    wherez = randomZ(rand, range, quarter, rz, ry, e, current);
                    highest = randworld.getHighestBlockYAt(wherex, wherez);
                    if (highest > 3) {
                        Block currentBlock = randworld.getBlockAt(wherex, highest, wherez);
                        if ((currentBlock.getRelative(BlockFace.DOWN).getType().equals(Material.WATER) || currentBlock.getRelative(BlockFace.DOWN).getType().equals(Material.STATIONARY_WATER)) && plugin.getConfig().getBoolean("travel.land_on_water") == false) {
                            // check if submarine is on
                            HashMap<String, Object> wheres = new HashMap<>();
                            wheres.put("uuid", p.getUniqueId().toString());
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wheres);
                            if (rsp.resultSet()) {
                                if (rsp.isSubmarineOn() && TARDISStaticUtils.isOceanBiome(currentBlock.getBiome())) {
                                    // get submarine location
                                    TARDISMessage.send(p, "SUB_SEARCH");
                                    Location underwater = submarine(currentBlock, d);
                                    if (underwater != null) {
                                        // get TARDIS id
                                        HashMap<String, Object> wherep = new HashMap<>();
                                        wherep.put("uuid", p.getUniqueId().toString());
                                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
                                        if (rst.resultSet()) {
                                            plugin.getTrackerKeeper().getSubmarine().add(rst.getTardis_id());
                                        }
                                        return underwater;
                                    } else {
                                        count = 1;
                                    }
                                } else if (!rsp.isSubmarineOn()) {
                                    count = 1;
                                }
                            } else {
                                count = 1;
                            }
                        } else {
                            if (TARDISConstants.GOOD_MATERIALS.contains(currentBlock.getType())) {
                                currentBlock = currentBlock.getRelative(BlockFace.DOWN);
                            }
                            Location chunk_loc = currentBlock.getLocation();
                            if (plugin.getPluginRespect().getRespect(chunk_loc, new Parameters(p, FLAG.getNoMessageFlags()))) {
                                while (!randworld.getChunkAt(chunk_loc).isLoaded()) {
                                    randworld.getChunkAt(chunk_loc).load();
                                }
                                // get start location for checking there is enough space
                                int gsl[] = getStartLocation(chunk_loc, d);
                                startx = gsl[0];
                                resetx = gsl[1];
                                starty = chunk_loc.getBlockY() + 1;
                                startz = gsl[2];
                                resetz = gsl[3];
                                count = safeLocation(startx, starty, startz, resetx, resetz, randworld, d);
                            } else {
                                count = 1;
                            }
                        }
                    } else {
                        count = 1;
                    }
                    if (count == 0) {
                        break;
                    }
                } else {
                    if (!plugin.getPluginRespect().getRespect(new Location(randworld, wherex, highest, wherez), new Parameters(p, FLAG.getNoMessageFlags()))) {
                        return null;
                    } else {
                        highest = plugin.getConfig().getInt("travel.timeout_height");
                        break;
                    }
                }
            }
            dest = new Location(randworld, wherex, highest, wherez);
        }
        return dest;
    }

    /**
     * Checks if a random location is safe for the TARDIS Police Box to land at.
     * The Police Box requires a clear 4 x 3 x 4 (d x w x h) area.
     *
     * @param startx a starting position in the x direction.
     * @param starty a starting position in the y direction.
     * @param startz a starting position in the z direction.
     * @param resetx a copy of the starting x position to return to.
     * @param resetz a copy of the starting z position to return to.
     * @param w the world the location check will take place in.
     * @param d the direction the Police Box is facing.
     * @return the number of unsafe blocks
     */
    @SuppressWarnings("deprecation")
    public static int safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS d) {
        int level, row, col, rowcount, colcount, count = 0;
        switch (d) {
            case EAST:
            case WEST:
                rowcount = 3;
                colcount = 4;
                break;
            default:
                rowcount = 4;
                colcount = 3;
                break;
        }
        for (level = 0; level < 4; level++) {
            for (row = 0; row < rowcount; row++) {
                for (col = 0; col < colcount; col++) {
                    Material mat = w.getBlockAt(startx, starty, startz).getType();
                    if (!TARDISConstants.GOOD_MATERIALS.contains(mat)) {
                        // check for siege cube
                        if (TARDIS.plugin.getConfig().getBoolean("siege.enabled") && mat.equals(Material.HUGE_MUSHROOM_1) && w.getBlockAt(startx, starty, startz).getData() == (byte) 14) {
                            continue;
                        } else {
                            count++;
                        }
                    }
                    startx += 1;
                }
                startx = resetx;
                startz += 1;
            }
            startz = resetz;
            starty += 1;
        }
        return count;
    }

    /**
     * Checks if a location is safe for the TARDIS Police Box to land at. Used
     * for debugging purposes only. The Police Box requires a clear 4 x 3 x 4 (d
     * x w x h) area.
     *
     * @param loc
     * @param d the direction the Police Box is facing.
     */
    public void testSafeLocation(Location loc, COMPASS d) {
        final World w = loc.getWorld();
        final int starty = loc.getBlockY();
        int sx, sz;
        switch (d) {
            case EAST:
                sx = loc.getBlockX() - 2;
                sz = loc.getBlockZ() - 1;
                break;
            case SOUTH:
                sx = loc.getBlockX() - 1;
                sz = loc.getBlockZ() - 2;
                break;
            default:
                sx = loc.getBlockX() - 1;
                sz = loc.getBlockZ() - 1;
                break;
        }
        int row, col;
        switch (d) {
            case EAST:
            case WEST:
                row = 2;
                col = 3;
                break;
            default:
                row = 3;
                col = 2;
                break;
        }
        final int r = row;
        final int c = col;
        final int startx = sx;
        final int startz = sz;
        TARDISBlockSetters.setBlock(w, startx, starty, startz, 80, (byte) 0);
        TARDISBlockSetters.setBlock(w, startx, starty, startz + row, 80, (byte) 0);
        TARDISBlockSetters.setBlock(w, startx + col, starty, startz, 80, (byte) 0);
        TARDISBlockSetters.setBlock(w, startx + col, starty, startz + row, 80, (byte) 0);
        TARDISBlockSetters.setBlock(w, startx, starty + 3, startz, 80, (byte) 0);
        TARDISBlockSetters.setBlock(w, startx + col, starty + 3, startz, 80, (byte) 0);
        TARDISBlockSetters.setBlock(w, startx, starty + 3, startz + row, 80, (byte) 0);
        TARDISBlockSetters.setBlock(w, startx + col, starty + 3, startz + row, 80, (byte) 0);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TARDISBlockSetters.setBlock(w, startx, starty, startz, 0, (byte) 0);
            TARDISBlockSetters.setBlock(w, startx, starty, startz + r, 0, (byte) 0);
            TARDISBlockSetters.setBlock(w, startx + c, starty, startz, 0, (byte) 0);
            TARDISBlockSetters.setBlock(w, startx + c, starty, startz + r, 0, (byte) 0);
            TARDISBlockSetters.setBlock(w, startx, starty + 3, startz, 0, (byte) 0);
            TARDISBlockSetters.setBlock(w, startx + c, starty + 3, startz, 0, (byte) 0);
            TARDISBlockSetters.setBlock(w, startx, starty + 3, startz + r, 0, (byte) 0);
            TARDISBlockSetters.setBlock(w, startx + c, starty + 3, startz + r, 0, (byte) 0);
        }, 300L);
    }

    /**
     * Gets the starting location for safe location checking.
     *
     * @param loc a location object to check.
     * @param d the direction the Police Box is facing.
     * @return an array containing x and z coordinates
     */
    public static int[] getStartLocation(Location loc, COMPASS d) {
        switch (d) {
            case EAST:
                startLoc[0] = loc.getBlockX() - 2;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() - 1;
                startLoc[3] = startLoc[2];
                break;
            case SOUTH:
                startLoc[0] = loc.getBlockX() - 1;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() - 2;
                startLoc[3] = startLoc[2];
                break;
            default:
                startLoc[0] = loc.getBlockX() - 1;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() - 1;
                startLoc[3] = startLoc[2];
                break;
        }
        return startLoc;
    }

    /**
     * Checks whether a NETHER location is safe to land at.
     *
     * @param nether a Nether world to search in.
     * @param wherex an x co-ordinate.
     * @param wherez a z co-ordinate.
     * @param d the direction the Police Box is facing.
     * @param p the player to check permissions for
     * @return true or false
     */
    @SuppressWarnings("deprecation")
    public boolean safeNether(World nether, int wherex, int wherez, COMPASS d, Player p) {
        boolean safe = false;
        int startx, starty, startz, resetx, resetz, count;
        int wherey = 100;
        Block startBlock = nether.getBlockAt(wherex, wherey, wherez);
        while (!startBlock.getType().equals(Material.AIR)) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
        }
        int air = 0;
        while (startBlock.getType().equals(Material.AIR) && startBlock.getLocation().getBlockY() > 30) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
            air++;
        }
        Material mat = startBlock.getType();
        if (plugin.getGeneralKeeper().getGoodNether().contains(mat) && air >= 4) {
            Location netherLocation = startBlock.getLocation();
            int netherLocY = netherLocation.getBlockY();
            netherLocation.setY(netherLocY + 1);
            if (plugin.getPluginRespect().getRespect(netherLocation, new Parameters(p, FLAG.getNoMessageFlags()))) {
                // get start location for checking there is enough space
                int gsl[] = getStartLocation(netherLocation, d);
                startx = gsl[0];
                resetx = gsl[1];
                starty = netherLocation.getBlockY();
                startz = gsl[2];
                resetz = gsl[3];
                count = safeLocation(startx, starty, startz, resetx, resetz, nether, d);
            } else {
                count = 1;
            }
            if (count == 0) {
                safe = true;
                dest = netherLocation;
            }
        }
        return safe;
    }

    /**
     * Returns a random positive or negative x integer.
     *
     * @param rand an object of type Random.
     * @param range the maximum the random number can be.
     * @param quarter one fourth of the max_distance config option.
     * @param rx the data bit of the x-repeater setting.
     * @param ry the data bit of the y-repeater setting.
     * @param max the max_distance config option.
     */
    private int randomX(Random rand, int range, int quarter, byte rx, byte ry, String e, Location l) {
        int currentx = (e.equals("THIS")) ? l.getBlockX() : 0;
        int wherex;
        wherex = rand.nextInt(range);
        // add the distance from the x and z repeaters
        if (rx <= 3) {
            wherex += quarter;
        }
        if (rx >= 4 && rx <= 7) {
            wherex += (quarter * 2);
        }
        if (rx >= 8 && rx <= 11) {
            wherex += (quarter * 3);
        }
        if (rx >= 12 && rx <= 15) {
            wherex += (quarter * 4);
        }
        // add chance of negative values
        if (rand.nextInt(2) == 1) {
            wherex = 0 - wherex;
        }
        // use multiplier based on position of third (y) repeater
        if (ry >= 4 && ry <= 7) {
            wherex *= 2;
        }
        if (ry >= 8 && ry <= 11) {
            wherex *= 3;
        }
        if (ry >= 12 && ry <= 15) {
            wherex *= 4;
        }
        return wherex + currentx;
    }

    /**
     * Returns a random positive or negative z integer.
     *
     * @param rand an object of type Random.
     * @param range the maximum the random number can be.
     * @param quarter one fourth of the max_distance config option.
     * @param rz the data bit of the x-repeater setting.
     * @param ry the data bit of the y-repeater setting.
     * @param max the max_distance config option.
     */
    private int randomZ(Random rand, int range, int quarter, byte rz, byte ry, String e, Location l) {
        int currentz = (e.equals("THIS")) ? l.getBlockZ() : 0;
        int wherez;
        wherez = rand.nextInt(range);
        // add the distance from the x and z repeaters
        if (rz <= 3) {
            wherez += quarter;
        }
        if (rz >= 4 && rz <= 7) {
            wherez += (quarter * 2);
        }
        if (rz >= 8 && rz <= 11) {
            wherez += (quarter * 3);
        }
        if (rz >= 12 && rz <= 15) {
            wherez += (quarter * 4);
        }
        // add chance of negative values
        if (rand.nextInt(2) == 1) {
            wherez = 0 - wherez;
        }
        // use multiplier based on position of third (y) repeater
        if (ry >= 4 && ry <= 7) {
            wherez *= 2;
        }
        if (ry >= 8 && ry <= 11) {
            wherez *= 3;
        }
        if (ry >= 12 && ry <= 15) {
            wherez *= 4;
        }
        return wherez + currentz;
    }

    @SuppressWarnings("deprecation")
    public Location submarine(Block b, COMPASS d) {
        Block block = b;
        while (true) {
            block = block.getRelative(BlockFace.DOWN);
            if (!block.getType().equals(Material.STATIONARY_WATER) && !block.getType().equals(Material.WATER) && !block.getType().equals(Material.ICE)) {
                break;
            }
        }
        Location loc = block.getRelative(BlockFace.UP).getLocation();
        for (int n = 0; n < attempts; n++) {
            if (isSafeSubmarine(loc, d)) {
                return loc;
            } else {
                loc.setY(loc.getY() + 1);
            }
        }
        return (isSafeSubmarine(loc, d)) ? loc : null;
    }

    @SuppressWarnings("deprecation")
    public boolean isSafeSubmarine(Location l, COMPASS d) {
        int[] s = getStartLocation(l, d);
        int level, row, col, rowcount, colcount, count = 0;
        int starty = l.getBlockY();
        switch (d) {
            case EAST:
            case WEST:
                rowcount = 3;
                colcount = 4;
                break;
            default:
                rowcount = 4;
                colcount = 3;
                break;
        }
        for (level = 0; level < 4; level++) {
            for (row = 0; row < rowcount; row++) {
                for (col = 0; col < colcount; col++) {
                    Material mat = l.getWorld().getBlockAt(s[0], starty, s[2]).getType();
                    if (!TARDISConstants.GOOD_WATER.contains(mat)) {
                        count++;
                    }
                    s[0] += 1;
                }
                s[0] = s[1];
                s[2] += 1;
            }
            s[2] = s[3];
            starty += 1;
        }
        return (count == 0);
    }
}
