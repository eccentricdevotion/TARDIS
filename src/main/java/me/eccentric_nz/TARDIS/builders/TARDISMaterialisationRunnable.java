/*
 *  Copyright 2013 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which
 * enabled it to dematerialise from normal space into the Time Vortex and
 * rematerialise back from it.
 *
 * @author eccentric_nz
 */
public class TARDISMaterialisationRunnable implements Runnable {

    private TARDIS plugin;
    private COMPASS d;
    private int loops;
    private Location location;
    private int tid;
    public int task;
    private int i;
    private int mat;
    private byte data;
    private boolean rebuild;

    /**
     * Runnable method to materialise the TARDIS Police Box. Tries to mimic the
     * transparency of materialisation by building the Police Box first with
     * GLASS, then ICE, then the normall wall block (BLUE WOOL or the chameleon
     * material).
     *
     * @param plugin instance of the TARDIS plugin
     * @param location the location to build the Police Box at
     * @param mat the material ID to construct the final walls
     * @param data the material data to construct the final walls
     * @param tid the tardis_id this Police Box belongs to
     * @param d the COMPASS direction the Police Box is facing
     * @param rebuild boolean, if false, save the block locations for this build
     */
    public TARDISMaterialisationRunnable(TARDIS plugin, Location location, int mat, byte data, int tid, COMPASS d, boolean rebuild) {
        this.plugin = plugin;
        this.d = d;
        this.loops = 8;
        this.location = location;
        this.i = 0;
        this.tid = tid;
        this.mat = mat;
        this.data = data;
        this.rebuild = rebuild;
    }

    @Override
    public void run() {
        int id;
        byte b;
        final World world;
        if (i <= loops) {
            i++;
             // try loading chunk
            world = l.getWorld();
            Chunk chunk = world.getChunkAt(l);
            if (!world.isChunkLoaded(chunk)) {
                world.loadChunk(chunk);
            }
            world.refreshChunk(chunk.getX(), chunk.getZ());
            // expand placed blocks to a police box
            switch (i % 3) {
                case 2:
                    id = 79;
                    b = 0;
                    break;
                case 1:
                    id = 20;
                    b = 0;
                    break;
                default:
                    id = mat;
                    b = data;
                    break;
            }
            byte mds = b, mdw = b, mdn = b, mde = b, bds = b, bdw = b, bdn = b, bde = b, sd = 0, norm = 0, grey = 8;
            int plusx, minusx, x, plusz, minusz, z;
            // get relative locations
            x = location.getBlockX();
            plusx = (location.getBlockX() + 1);
            minusx = (location.getBlockX() - 1);
            int y = location.getBlockY() + 2;
            int plusy = (location.getBlockY() + 3), minusy = (location.getBlockY() + 1), down2y = location.getBlockY(), down3y = (location.getBlockY() - 1);
            z = (location.getBlockZ());
            plusz = (location.getBlockZ() + 1);
            minusz = (location.getBlockZ() - 1);
            world = location.getWorld();
            int south = id, west = id, north = id, east = id, signx = 0, signz = 0;
            String doorloc = "";
            // first run - remember blocks
            if (i == 1) {
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> ps = new HashMap<String, Object>();
                ps.put("tardis_id", tid);
                // get direction player is facing from yaw place block under door if block is in list of blocks an iron door cannot go on
                switch (d) {
                    case SOUTH:
                        //if (yaw >= 315 || yaw < 45)
                        plugin.utils.setBlockCheck(world, x, down3y, minusz, 35, grey, tid); // door is here if player facing south
                        ps.put("location", world.getBlockAt(x, down3y, minusz).getLocation().toString());
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
                        ps.put("location", world.getBlockAt(minusx, down3y, z).getLocation().toString());
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
                        ps.put("location", world.getBlockAt(x, down3y, plusz).getLocation().toString());
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
                        ps.put("location", world.getBlockAt(plusx, down3y, z).getLocation().toString());
                        doorloc = world.getName() + ":" + plusx + ":" + down2y + ":" + z;
                        sd = 5;
                        signx = (plusx + 1);
                        signz = z;
                        west = 71;
                        mdw = 8;
                        bdw = 2;
                        break;
                }
                qf.doInsert("blocks", ps);
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
                    qf.doInsert("doors", setd);
                }
                // bottom layer corners
                plugin.utils.setBlockAndRemember(world, plusx, down2y, plusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, minusx, down2y, plusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, minusx, down2y, minusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, plusx, down2y, minusz, id, b, tid, rebuild);
                // middle layer corners
                plugin.utils.setBlockAndRemember(world, plusx, minusy, plusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, minusx, minusy, plusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, minusx, minusy, minusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, plusx, minusy, minusz, id, b, tid, rebuild);
                // top layer
                switch (id) {
                    case 18:
                        plugin.utils.setBlockAndRemember(world, x, y, z, 17, b, tid, rebuild);
                        break;
                    case 46:
                        plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 14, tid, rebuild);
                        break;
                    case 79:
                        plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 3, tid, rebuild);
                        break;
                    case 89:
                        plugin.utils.setBlockAndRemember(world, x, y, z, 35, (byte) 4, tid, rebuild);
                        break;
                    default:
                        plugin.utils.setBlockAndRemember(world, x, y, z, id, b, tid, rebuild);
                        break;
                }
                plugin.utils.setBlockAndRemember(world, plusx, y, z, id, b, tid, rebuild); // east
                plugin.utils.setBlockAndRemember(world, plusx, y, plusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, x, y, plusz, id, b, tid, rebuild); // south
                plugin.utils.setBlockAndRemember(world, minusx, y, plusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, minusx, y, z, id, b, tid, rebuild); // west
                plugin.utils.setBlockAndRemember(world, minusx, y, minusz, id, b, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, x, y, minusz, id, b, tid, rebuild); // north
                plugin.utils.setBlockAndRemember(world, plusx, y, minusz, id, b, tid, rebuild);
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
                if (id == 79) {
                    plugin.utils.setBlockAndRemember(world, x, plusy, z, 76, (byte) 5, tid, rebuild);
                } else {
                    plugin.utils.setBlockAndRemember(world, x, plusy, z, 50, (byte) 5, tid, rebuild);
                }
                // bottom layer with door bottom
                plugin.utils.setBlockAndRemember(world, plusx, down2y, z, west, bdw, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, x, down2y, plusz, north, bdn, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, minusx, down2y, z, east, bde, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, x, down2y, minusz, south, bds, tid, rebuild);
                // middle layer with door top
                plugin.utils.setBlockAndRemember(world, plusx, minusy, z, west, mdw, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, x, minusy, plusz, north, mdn, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, minusx, minusy, z, east, mde, tid, rebuild);
                plugin.utils.setBlockAndRemember(world, x, minusy, minusz, south, mds, tid, rebuild);
            } else {
                // just change the walls
                // bottom layer corners
                plugin.utils.setBlock(world, plusx, down2y, plusz, id, b);
                plugin.utils.setBlock(world, minusx, down2y, plusz, id, b);
                plugin.utils.setBlock(world, minusx, down2y, minusz, id, b);
                plugin.utils.setBlock(world, plusx, down2y, minusz, id, b);
                // middle layer corners
                plugin.utils.setBlock(world, plusx, minusy, plusz, id, b);
                plugin.utils.setBlock(world, minusx, minusy, plusz, id, b);
                plugin.utils.setBlock(world, minusx, minusy, minusz, id, b);
                plugin.utils.setBlock(world, plusx, minusy, minusz, id, b);
                // top layer
                switch (id) {
                    case 18:
                        plugin.utils.setBlock(world, x, y, z, 17, b);
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
                        plugin.utils.setBlock(world, x, y, z, id, b);
                        break;
                }
                plugin.utils.setBlock(world, plusx, y, z, id, b); // east
                plugin.utils.setBlock(world, plusx, y, plusz, id, b);
                plugin.utils.setBlock(world, x, y, plusz, id, b); // south
                plugin.utils.setBlock(world, minusx, y, plusz, id, b);
                plugin.utils.setBlock(world, minusx, y, z, id, b); // west
                plugin.utils.setBlock(world, minusx, y, minusz, id, b);
                plugin.utils.setBlock(world, x, y, minusz, id, b); // north
                plugin.utils.setBlock(world, plusx, y, minusz, id, b);
                switch (d) {
                    case SOUTH:
                        south = 71;
                        mds = 8;
                        bds = 1;
                        break;
                    case EAST:
                        east = 71;
                        mde = 8;
                        bde = 0;
                        break;
                    case NORTH:
                        north = 71;
                        mdn = 8;
                        bdn = 3;
                        break;
                    case WEST:
                        west = 71;
                        mdw = 8;
                        bdw = 2;
                        break;
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
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            plugin.tardisMaterilising.remove(Integer.valueOf(tid));
            task = 0;
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
