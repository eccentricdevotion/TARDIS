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
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * A police box is a telephone kiosk that can be used by members of the public
 * wishing to get help from the police. Early in the First Doctor's travels, the
 * TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due a malfunction in its chameleon circuit, the
 * TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class TARDISInstaPoliceBox {

    private final TARDIS plugin;
    private TARDISConstants.COMPASS d;
    private Location location;
    private int tid;
    private int mat;
    private byte data;
    private String p;
    private boolean mal;
    private int lamp;
    private boolean plain;

    public TARDISInstaPoliceBox(TARDIS plugin, Location location, int mat, byte data, int tid, TARDISConstants.COMPASS d, String p, boolean mal, int lamp, boolean plain) {
        this.plugin = plugin;
        this.d = d;
        this.location = location;
        this.tid = tid;
        this.mat = mat;
        this.data = data;
        this.p = p;
        this.mal = mal;
        this.lamp = lamp;
        this.plain = plain;
    }

    /**
     * Builds the TARDIS Police Box.
     */
    public void buildPoliceBox() {
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
        String doorloc = "";
        // platform
        plugin.buildPB.addPlatform(location, false, d, p, tid);
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> ps = new HashMap<String, Object>();
        ps.put("tardis_id", tid);
        String loc = "";
        // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
        switch (d) {
            case SOUTH:
                //if (yaw >= 315 || yaw < 45)
                plugin.utils.setBlockCheck(world, x, down3y, minusz, 35, grey, tid); // door is here if player facing south
                loc = world.getBlockAt(x, down3y, minusz).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + x + ":" + down2y + ":" + minusz;
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
                loc = world.getBlockAt(minusx, down3y, z).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + minusx + ":" + down2y + ":" + z;
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
                loc = world.getBlockAt(x, down3y, plusz).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + x + ":" + down2y + ":" + plusz;
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
                loc = world.getBlockAt(plusx, down3y, z).getLocation().toString();
                ps.put("location", loc);
                doorloc = world.getName() + ":" + plusx + ":" + down2y + ":" + z;
                sd = 5;
                signx = (plusx + 1);
                signz = z;
                west = 71;
                mdw = 8;
                bdw = 2;
                break;
        }
        ps.put("police_box", 1);
        qf.doInsert("blocks", ps);
        if (!loc.isEmpty()) {
            plugin.protectBlockMap.put(loc, tid);
        }
        // should insert the door when tardis is first made, and then update location there after!
        HashMap<String, Object> whered = new HashMap<String, Object>();
        whered.put("door_type", 0);
        whered.put("tardis_id", tid);
        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<String, Object>();
        setd.put("door_location", doorloc);
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<String, Object>();
            whereid.put("door_id", rsd.getDoor_id());
            qf.doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", tid);
            setd.put("door_type", 0);
            setd.put("door_direction", d.toString());
            qf.doInsert("doors", setd);
        }
        // bottom layer corners
        plugin.utils.setBlockAndRemember(world, plusx, down2y, plusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, minusx, down2y, plusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, minusx, down2y, minusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, plusx, down2y, minusz, mat, data, tid);
        // middle layer corners
        plugin.utils.setBlockAndRemember(world, plusx, minusy, plusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, minusx, minusy, plusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, minusx, minusy, minusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, plusx, minusy, minusz, mat, data, tid);
        // top layer
        switch (mat) {
            case 18:
                plugin.utils.setBlockAndRemember(world, x, y, z, 17, data, tid);
                break;
            case 46:
                plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 14, tid);
                break;
            case 79:
                plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 3, tid);
                break;
            case 89:
                plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 4, tid);
                break;
            default:
                plugin.utils.setBlockAndRemember(world, x, y, z, mat, data, tid);
                break;
        }
        plugin.utils.setBlockAndRemember(world, plusx, y, z, mat, data, tid); // east
        plugin.utils.setBlockAndRemember(world, plusx, y, plusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, x, y, plusz, mat, data, tid); // south
        plugin.utils.setBlockAndRemember(world, minusx, y, plusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, minusx, y, z, mat, data, tid); // west
        plugin.utils.setBlockAndRemember(world, minusx, y, minusz, mat, data, tid);
        plugin.utils.setBlockAndRemember(world, x, y, minusz, mat, data, tid); // north
        plugin.utils.setBlockAndRemember(world, plusx, y, minusz, mat, data, tid);
        if (!plain) {
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
                plugin.utils.setBlockAndRemember(world, x, plusy, z, 76, (byte) 5, tid);
            } else {
                plugin.utils.setBlockAndRemember(world, x, plusy, z, lamp, (byte) 5, tid);
            }
        }
        // bottom layer with door bottom
        plugin.utils.setBlockAndRemember(world, plusx, down2y, z, west, bdw, tid);
        plugin.utils.setBlockAndRemember(world, x, down2y, plusz, north, bdn, tid);
        plugin.utils.setBlockAndRemember(world, minusx, down2y, z, east, bde, tid);
        plugin.utils.setBlockAndRemember(world, x, down2y, minusz, south, bds, tid);
        // middle layer with door top
        plugin.utils.setBlockAndRemember(world, plusx, minusy, z, west, mdw, tid);
        plugin.utils.setBlockAndRemember(world, x, minusy, plusz, north, mdn, tid);
        plugin.utils.setBlockAndRemember(world, minusx, minusy, z, east, mde, tid);
        plugin.utils.setBlockAndRemember(world, x, minusy, minusz, south, mds, tid);
        // message travellers in tardis
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", tid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
        if (rst.resultSet()) {
            final List<String> travellers = rst.getData();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (String s : travellers) {
                        Player p = plugin.getServer().getPlayer(s);
                        if (p != null) {
                            String message = (mal) ? "There was a malfunction and the emergency handbrake was engaged! Scan location before exit!" : "LEFT-click the handbrake to exit!";
                            p.sendMessage(plugin.pluginName + message);
                        }
                    }
                }
            }, 30L);
        }
        plugin.tardisMaterialising.remove(Integer.valueOf(tid));
    }
}
