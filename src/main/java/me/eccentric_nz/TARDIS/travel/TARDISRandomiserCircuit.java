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
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * The Randomiser is a device that ensures a TARDIS lands at unpredictable times
 * and places. When activated it scrambles the TARDIS' coordinate settings,
 * giving the Doctor even less control than usual over the destination of his
 * ship.
 *
 * @author eccentric_nz
 */
public class TARDISRandomiserCircuit {

    private final TARDIS plugin;
    private final Random random = new Random();
    private Location dest;

    public TARDISRandomiserCircuit(TARDIS plugin) {
        this.plugin = plugin;
    }

    public Location getRandomlocation(Player p, COMPASS d) {
        // get a random world
        Set<String> worldlist = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
        List<String> allowedWorlds = new ArrayList<String>();
        for (String o : worldlist) {
            World ww = plugin.getServer().getWorld(o);
            if (ww != null) {
                if (plugin.getConfig().getBoolean("travel.include_default_world") || !plugin.getConfig().getBoolean("creation.default_world")) {
                    if (plugin.getConfig().getBoolean("worlds." + o)) {
                        allowedWorlds.add(o);
                    }
                } else {
                    if (!o.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                        if (plugin.getConfig().getBoolean("worlds." + o)) {
                            allowedWorlds.add(o);
                        }
                    }
                }
                // remove the world if the player doesn't have permission
                if (allowedWorlds.size() > 1 && plugin.getConfig().getBoolean("travel.per_world_perms") && !p.hasPermission("tardis.travel." + o)) {
                    allowedWorlds.remove(o);
                }
            }
        }
        Parameters params = new Parameters(p, FLAG.getDefaultFlags());
        params.setCompass(d);
        return plugin.getTardisAPI().getRandomLocation(allowedWorlds, null, params);
//        int listlen = allowedWorlds.size();
//        // random world
//        World w = allowedWorlds.get(random.nextInt(listlen));
//        // get the limits of the world
//        int minX;
//        int maxX;
//        int minZ;
//        int maxZ;
//        // is WorldBorder enabled?
//        if (plugin.getPM().isPluginEnabled("WorldBorder")) {
//            BorderData border = Config.Border(w.getName());
//            minX = (int) border.getX() - border.getRadiusX();
//            maxX = (int) border.getX() + border.getRadiusX();
//            minZ = (int) border.getZ() - border.getRadiusZ();
//            maxZ = (int) border.getZ() + border.getRadiusZ();
//        } else {
//            // use config
//            int cx = plugin.getConfig().getInt("travel.random_circuit.x");
//            int cz = plugin.getConfig().getInt("travel.random_circuit.z");
//            minX = 0 - cx;
//            maxX = cx;
//            minZ = 0 - cz;
//            maxZ = cz;
//        }
//        // compensate for nether 1:8 ratio if necessary
//        Environment env = w.getEnvironment();
//        if (env.equals(Environment.NETHER)) {
//            minX /= 8;
//            maxX /= 8;
//            minZ /= 8;
//            maxZ /= 8;
//        }
//        // get ranges
//        int rangeX = Math.abs(minX) + maxX;
//        int rangeZ = Math.abs(minZ) + maxZ;
//        // loop till random attempts limit reached
//        test:
//        for (int n = 0; n < plugin.getConfig().getInt("travel.random_attempts"); n++) {
//            // get random values in range
//            int randX = random.nextInt(rangeX);
//            int randZ = random.nextInt(rangeZ);
//            // get the x coord
//            int x = minX + randX;
//            // get the z coord
//            int z = minZ + randZ;
//            switch (env) {
//                case NETHER:
//                    if (safeNether(w, x, z, d, p)) {
//                        break test;
//                    }
//                    break;
//                case THE_END:
//                    if (safeEnd(w, d, p)) {
//                        break test;
//                    }
//                    break;
//                default:
//                    // get the y coord
//                    if (safeOverworld(w, x, z, d, p)) {
//                        if ((dest.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.WATER) || dest.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.STATIONARY_WATER)) && plugin.getUtils().isOceanBiome(dest.getBlock().getBiome())) {
//                            if (safeSubmarine(dest, d, p)) {
//                                break test;
//                            }
//                        }
//                        break test;
//                    }
//                    break;
//            }
//        }
//        return dest;
    }

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
                int gsl[] = TARDISTimeTravel.getStartLocation(netherLocation, d);
                startx = gsl[0];
                resetx = gsl[1];
                starty = netherLocation.getBlockY();
                startz = gsl[2];
                resetz = gsl[3];
                count = TARDISTimeTravel.safeLocation(startx, starty, startz, resetx, resetz, nether, d);
            } else {
                count = 1;
            }
            if (count == 0) {
                safe = true;
                this.dest = netherLocation;
            }
        }
        return safe;
    }

    private boolean safeEnd(World randworld, COMPASS d, Player p) {
        boolean safe = false;
        int count;
        int wherex = random.nextInt(240);
        int wherez = random.nextInt(240);
        wherex -= 120;
        wherez -= 120;
        // get the spawn point
        Location endSpawn = randworld.getSpawnLocation();
        int highest = randworld.getHighestBlockYAt(endSpawn.getBlockX() + wherex, endSpawn.getBlockZ() + wherez);
        if (highest > 40) {
            Block currentBlock = randworld.getBlockAt(wherex, highest, wherez);
            Location chunk_loc = currentBlock.getLocation();
            if (plugin.getPluginRespect().getRespect(chunk_loc, new Parameters(p, FLAG.getNoMessageFlags()))) {
                while (!randworld.getChunkAt(chunk_loc).isLoaded()) {
                    randworld.getChunkAt(chunk_loc).load();
                }
                // get start location for checking there is enough space
                int gsl[] = TARDISTimeTravel.getStartLocation(chunk_loc, d);
                int startx = gsl[0];
                int resetx = gsl[1];
                int starty = chunk_loc.getBlockY() + 1;
                int startz = gsl[2];
                int resetz = gsl[3];
                count = TARDISTimeTravel.safeLocation(startx, starty, startz, resetx, resetz, randworld, d);
            } else {
                count = 1;
            }
        } else {
            count = 1;
        }
        if (count == 0) {
            safe = true;
            dest = (highest > 0) ? new Location(randworld, wherex, highest, wherez) : null;
        }
        return safe;
    }

    private boolean safeOverworld(World world, int wherex, int wherez, COMPASS d, Player p) {
        boolean safe = false;
        int count;
        int highest = world.getHighestBlockYAt(wherex, wherez);
        if (highest > 3) {
            Block currentBlock = world.getBlockAt(wherex, highest, wherez);
            if (TARDISConstants.GOOD_MATERIALS.contains(currentBlock.getType())) {
                currentBlock = currentBlock.getRelative(BlockFace.DOWN);
            }
            Location overworld = currentBlock.getLocation();
            if (plugin.getPluginRespect().getRespect(overworld, new Parameters(p, FLAG.getNoMessageFlags()))) {
                while (!world.getChunkAt(overworld).isLoaded()) {
                    world.getChunkAt(overworld).load();
                }
                // get start location for checking there is enough space
                int gsl[] = TARDISTimeTravel.getStartLocation(overworld, d);
                int startx = gsl[0];
                int resetx = gsl[1];
                int starty = overworld.getBlockY() + 1;
                int startz = gsl[2];
                int resetz = gsl[3];
                count = TARDISTimeTravel.safeLocation(startx, starty, startz, resetx, resetz, world, d);
            } else {
                count = 1;
            }
        } else {
            count = 1;
        }
        if (count == 0) {
            dest = new Location(world, wherex, highest, wherez);
            safe = true;
        }
        return safe;
    }

    private boolean safeSubmarine(Location l, COMPASS d, Player p) {
        boolean safe = false;
        int count = 0;
        Block block = l.getBlock();
        while (true) {
            block = block.getRelative(BlockFace.DOWN);
            if (!block.getType().equals(Material.STATIONARY_WATER) && !block.getType().equals(Material.WATER) && !block.getType().equals(Material.ICE)) {
                break;
            }
        }
        Location loc = block.getRelative(BlockFace.UP).getLocation();
        for (int n = 0; n < 5; n++) {
            int[] s = TARDISTimeTravel.getStartLocation(loc, d);
            int level, row, col, rowcount, colcount;
            int starty = loc.getBlockY();
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
            for (level = starty; level < starty + 4; level++) {
                for (row = s[0]; row < s[0] + rowcount; row++) {
                    for (col = s[2]; col < s[2] + colcount; col++) {
                        Material mat = loc.getWorld().getBlockAt(row, level, col).getType();
                        if (!TARDISConstants.GOOD_WATER.contains(mat)) {
                            count++;
                        }
                    }
                }
            }
            if (count == 0) {
                safe = true;
                // get TARDIS id
                HashMap<String, Object> wherep = new HashMap<String, Object>();
                wherep.put("uuid", p.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
                if (rst.resultSet()) {
                    plugin.getTrackerKeeper().getSubmarine().add(rst.getTardis_id());
                }
                dest = loc;
                break;
            } else {
                loc.setY(loc.getY() + 1);
            }
        }
        return safe;
    }
}
