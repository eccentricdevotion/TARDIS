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
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * A police box is a telephone kiosk that can be used by members of the public
 * wishing to get help from the police. Early in the First Doctor's travels, the
 * TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the
 * TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISPoliceBoxRebuilder {

    private final TARDIS plugin;
    private TARDISConstants.COMPASS d;
    private Location location;
    private int tid;
    private int mat;
    private byte data;

    public TARDISPoliceBoxRebuilder(TARDIS plugin, Location location, int mat, byte data, int tid, TARDISConstants.COMPASS d) {
        this.plugin = plugin;
        this.d = d;
        this.location = location;
        this.tid = tid;
        this.mat = mat;
        this.data = data;
    }

    /**
     * Builds the TARDIS Police Box.
     */
    public void rebuildPoliceBox() {
        int plusx, minusx, x, plusz, minusz, z;
        byte sd = 0, grey = 8;
        byte mds = data, mdw = data, mdn = data, mde = data, bds = data, bdw = data, bdn = data, bde = data;
        final World world;
        // expand placed blocks to a police box
        double lowX = location.getX();
        double lowY = location.getY();
        double lowZ = location.getZ();
        location.setX(lowX + 0.5);
        location.setY(lowY + 2);
        location.setZ(lowZ + 0.5);
        // get relative locations
        x = location.getBlockX();
        plusx = (location.getBlockX() + 1);
        minusx = (location.getBlockX() - 1);
        final int y = location.getBlockY();
        final int plusy = (location.getBlockY() + 1), minusy = (location.getBlockY() - 1), down2y = (location.getBlockY() - 2), down3y = (location.getBlockY() - 3);
        z = (location.getBlockZ());
        plusz = (location.getBlockZ() + 1);
        minusz = (location.getBlockZ() - 1);
        world = location.getWorld();
        int south = mat, west = mat, north = mat, east = mat, signx = 0, signz = 0;
        // get direction police box is facing, place block under door if block is in list of blocks an iron door cannot go on
        switch (d) {
            case SOUTH:
                //if (yaw >= 315 || yaw < 45)
                plugin.utils.setBlockCheck(world, x, down3y, minusz, 35, grey, tid); // door is here if player facing south
                sd = 2;
                signx = x;
                signz = (minusz - 1);
                south = 71;
                mds = 8;
                bds = 1;
                break;
            case EAST:
                //if (yaw >= 225 && yaw < 315)
                plugin.utils.setBlockCheck(world, minusx, down3y, z, 35, grey, tid); // door is here if player facing east
                sd = 4;
                signx = (minusx - 1);
                signz = z;
                east = 71;
                mde = 8;
                bde = 0;
                break;
            case NORTH:
                //if (yaw >= 135 && yaw < 225)
                plugin.utils.setBlockCheck(world, x, down3y, plusz, 35, grey, tid); // door is here if player facing north
                sd = 3;
                signx = x;
                signz = (plusz + 1);
                north = 71;
                mdn = 8;
                bdn = 3;
                break;
            case WEST:
                //if (yaw >= 45 && yaw < 135)
                plugin.utils.setBlockCheck(world, plusx, down3y, z, 35, grey, tid); // door is here if player facing west
                sd = 5;
                signx = (plusx + 1);
                signz = z;
                west = 71;
                mdw = 8;
                bdw = 2;
                break;
        }
        // bottom layer corners
        plugin.utils.setBlock(world, plusx, down2y, plusz, mat, data);
        plugin.utils.setBlock(world, minusx, down2y, plusz, mat, data);
        plugin.utils.setBlock(world, minusx, down2y, minusz, mat, data);
        plugin.utils.setBlock(world, plusx, down2y, minusz, mat, data);
        // middle layer corners
        plugin.utils.setBlock(world, plusx, minusy, plusz, mat, data);
        plugin.utils.setBlock(world, minusx, minusy, plusz, mat, data);
        plugin.utils.setBlock(world, minusx, minusy, minusz, mat, data);
        plugin.utils.setBlock(world, plusx, minusy, minusz, mat, data);
        // top layer
        switch (mat) {
            case 18:
                plugin.utils.setBlock(world, x, y, z, 17, data);
                break;
            case 46:
                plugin.utils.setBlock(world, x, y, z, 35, (byte) 14);
                break;
            case 79:
                plugin.utils.setBlock(world, x, y, z, 35, (byte) 3);
                break;
            case 89:
                plugin.utils.setBlock(world, x, y, z, 35, (byte) 4);
                break;
            default:
                plugin.utils.setBlock(world, x, y, z, mat, data);
                break;
        }
        plugin.utils.setBlock(world, plusx, y, z, mat, data); // east
        plugin.utils.setBlock(world, plusx, y, plusz, mat, data);
        plugin.utils.setBlock(world, x, y, plusz, mat, data); // south
        plugin.utils.setBlock(world, minusx, y, plusz, mat, data);
        plugin.utils.setBlock(world, minusx, y, z, mat, data); // west
        plugin.utils.setBlock(world, minusx, y, minusz, mat, data);
        plugin.utils.setBlock(world, x, y, minusz, mat, data); // north
        plugin.utils.setBlock(world, plusx, y, minusz, mat, data);
        // set sign
        plugin.utils.setBlock(world, signx, y, signz, 68, sd);
        Block sign = world.getBlockAt(signx, y, signz);
        if (sign.getType().equals(Material.WALL_SIGN)) {
            Sign s = (Sign) sign.getState();
            if (plugin.getConfig().getBoolean("name_tardis")) {
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", tid);
                ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
                if (rst.resultSet()) {
                    String owner = rst.getOwner();
                    if (owner.length() > 14) {
                        s.setLine(0, owner.substring(0, 12) + "'s");
                    } else {
                        s.setLine(0, owner + "'s");
                    }
                }
            }
            s.setLine(1, ChatColor.WHITE + "POLICE");
            s.setLine(2, ChatColor.WHITE + "BOX");
            s.update();
        }
        // put torch on top
        if (mat == 79) {
            plugin.utils.setBlock(world, x, plusy, z, 76, (byte) 5);
        } else {
            plugin.utils.setBlock(world, x, plusy, z, 50, (byte) 5);
        }
        // bottom layer with door bottom
        plugin.utils.setBlock(world, plusx, down2y, z, west, bdw);
        plugin.utils.setBlock(world, x, down2y, plusz, north, bdn);
        plugin.utils.setBlock(world, minusx, down2y, z, east, bde);
        plugin.utils.setBlock(world, x, down2y, minusz, south, bds);
        // middle layer with door top
        plugin.utils.setBlock(world, plusx, minusy, z, west, mdw);
        plugin.utils.setBlock(world, x, minusy, plusz, north, mdn);
        plugin.utils.setBlock(world, minusx, minusy, z, east, mde);
        plugin.utils.setBlock(world, x, minusy, minusz, south, mds);
    }
}
