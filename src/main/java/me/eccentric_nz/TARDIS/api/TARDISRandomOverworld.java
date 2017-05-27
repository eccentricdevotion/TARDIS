/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.api;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRandomOverworld extends TARDISRandomLocation {

    private final TARDIS plugin;
    private final Parameters param;
    private final List<World> worlds;
    private final Random random = new Random();
    private Location dest;

    public TARDISRandomOverworld(TARDIS plugin, List<String> list, Parameters param) {
        super(plugin, list, param);
        worlds = getWorlds(list);
        this.plugin = plugin;
        this.param = param;
    }

    @Override
    public Location getlocation() {
        WorldAndRange war = getWorldandRange(worlds);
        // loop till random attempts limit reached
        for (int n = 0; n < plugin.getConfig().getInt("travel.random_attempts"); n++) {
            // get random values in range
            int randX = random.nextInt(war.getRangeX());
            int randZ = random.nextInt(war.getRangeZ());
            // get the x coord
            int x = war.getMinX() + randX;
            // get the z coord
            int z = war.getMinZ() + randZ;
            Location tmp = new Location(war.getW(), x, 64, z);
            while (!war.getW().getChunkAt(tmp).isLoaded()) {
                war.getW().getChunkAt(tmp).load();
            }
            // get the y coord
            if (param.spaceTardis()) {
                if (safeOverworld(war.getW(), x, z, param.getCompass(), param.getPlayer())) {
                    if ((dest.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.WATER) || dest.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.STATIONARY_WATER)) && TARDISStaticUtils.isOceanBiome(dest.getBlock().getBiome())) {
                        if (safeSubmarine(dest, param.getCompass(), param.getPlayer())) {
                            break;
                        }
                    }
                    break;
                }
            } else {
                // space for a player / check plugin respect
                int highest = war.getW().getHighestBlockYAt(x, z);
                Location chk = new Location(war.getW(), x, highest, z);
                if (plugin.getPluginRespect().getRespect(chk, param)) {
                    return chk;
                }
            }
        }
        return dest;
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
            if (plugin.getPluginRespect().getRespect(overworld, param)) {
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
                HashMap<String, Object> wherep = new HashMap<>();
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
