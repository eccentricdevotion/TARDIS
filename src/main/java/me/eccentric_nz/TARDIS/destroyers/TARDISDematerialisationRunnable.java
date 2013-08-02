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
package me.eccentric_nz.TARDIS.destroyers;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

/**
 * A dematerialisation circuit was an essential part of a Type 40 TARDIS which
 * enabled it to dematerialise from normal space into the Time Vortex and
 * rematerialise back from it.
 *
 * @author eccentric_nz
 */
public class TARDISDematerialisationRunnable implements Runnable {

    private TARDIS plugin;
    private COMPASS d;
    private int loops;
    private Location location;
    private int tid;
    public int task;
    private int i;
    private int lamp;
    private int mat;
    private byte data;
    private Player player;

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
     * @param player a player
     * @param mal a boolean determining whether there has been a TARDIS
     * malfunction
     */
    public TARDISDematerialisationRunnable(TARDIS plugin, Location location, int lamp, int mat, byte data, int tid, COMPASS d, Player player) {
        this.plugin = plugin;
        this.d = d;
        this.loops = 12;
        this.location = location;
        this.i = 0;
        this.tid = tid;
        this.lamp = lamp;
        this.mat = mat;
        this.data = data;
        this.player = player;
    }

    @Override
    public void run() {
        int id;
        byte b;
        // get relative locations
        int x = location.getBlockX(), plusx = location.getBlockX() + 1, minusx = location.getBlockX() - 1;
        int y = location.getBlockY() + 2, minusy = location.getBlockY() + 1;
        int down2y = location.getBlockY();
        int z = location.getBlockZ(), plusz = location.getBlockZ() + 1, minusz = location.getBlockZ() - 1;
        World world = location.getWorld();
        if (i < loops) {
            i++;
            // expand placed blocks to a police box
            switch (i % 3) {
                case 2:
                    id = 20;
                    b = 0;
                    break;
                case 1:
                    id = 79;
                    b = 0;
                    break;
                default:
                    id = mat;
                    b = data;
                    break;
            }
            int south = id, west = id, north = id, east = id;
            byte mds = b, mdw = b, mdn = b, mde = b, bds = b, bdw = b, bdn = b, bde = b;
            // first run - play sound
            if (i == 1) {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", tid);
                if (plugin.pm.getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                    SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropboxusercontent.com/u/53758864/tardis_takeoff.mp3", false, location, 9, 75);
                } else {
                    try {
                        Class.forName("org.bukkit.Sound");
                        world.playSound(location, Sound.MINECART_INSIDE, 1, 0);
                    } catch (ClassNotFoundException e) {
                        world.playEffect(location, Effect.BLAZE_SHOOT, 0);
                    }
                }
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
                    case 20:
                        plugin.utils.setBlock(world, x, y, z, 20, (byte) 0);
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
                        if (lamp == 123 && plugin.bukkitversion.compareTo(plugin.precomparatorversion) >= 0) {
                            plugin.utils.setBlock(world, x, y, z, 152, b);
                        } else {
                            plugin.utils.setBlock(world, x, y, z, id, b);
                        }
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
            new TARDISDeinstaPoliceBox(plugin).instaDestroyPB(location, d, tid, false);
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            plugin.tardisChunkList.remove(location.getChunk());
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
