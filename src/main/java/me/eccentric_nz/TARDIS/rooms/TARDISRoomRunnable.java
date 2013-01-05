/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * @author eccentric_nz
 */
public class TARDISRoomRunnable implements Runnable {

    private final TARDIS plugin;
    private Location l;
    String[][][] s;
    short[] dim;
    private int id, task, level, row, col, h, w, c, middle_id, startx, starty, startz, resetx, resetz, x, z, tardis_id;
    byte data, middle_data;
    Block b;
    COMPASS d;
    String room;
    QueryFactory qf;
    private boolean running;
    HashMap<String, Object> set;

    public TARDISRoomRunnable(TARDIS plugin, TARDISRoomData roomData) {
        this.plugin = plugin;
        this.l = roomData.getLocation();
        this.s = roomData.getSchematic();
        this.dim = roomData.getDimensions();
        this.x = roomData.getX();
        this.z = roomData.getZ();
        this.b = roomData.getBlock();
        this.d = roomData.getDirection();
        this.middle_id = roomData.getMiddle_id();
        this.middle_data = roomData.getMiddle_data();
        this.room = roomData.getRoom().toString();
        this.tardis_id = roomData.getTardis_id();
        this.qf = new QueryFactory(plugin);
        this.running = false;
    }

    /**
     * A runnable task that builds TARDIS rooms block by block.
     */
    @Override
    public void run() {
        // initialise
        if (!running) {
            set = new HashMap<String, Object>();
            plugin.debug("Initialising variables...");
            level = 0;
            row = 0;
            col = 0;
            h = dim[0] - 1;
            w = dim[1] - 1;
            c = dim[2];
            startx = l.getBlockX();
            starty = l.getBlockY();
            startz = l.getBlockZ();
            resetx = startx;
            resetz = startz;
            set.put("startx", startx);
            set.put("starty", starty);
            set.put("startz", startz);
            running = true;
        }
        String tmp;
        if (level == h && row == w && col == (c - 1)) {
            // the entire schematic has been read :)
            byte door_data;
            switch (d) {
                case NORTH:
                    door_data = 1;
                    break;
                case WEST:
                    door_data = 0;
                    break;
                case SOUTH:
                    door_data = 3;
                    break;
                default:
                    door_data = 2;
                    break;
            }
            // put door on
            b.setTypeIdAndData(64, door_data, true);
            b.getRelative(BlockFace.UP).setTypeIdAndData(64, (byte) 8, true);
            plugin.debug("Finished building");
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
        }
        // place one block
        tmp = s[level][row][col];
        String[] iddata = tmp.split(":");
        id = plugin.utils.parseNum(iddata[0]);
        data = Byte.parseByte(iddata[1]);
        if (id == 35 && data == 1) {
            id = middle_id;
            data = middle_data;
        }
        // remove sponge
        if (id == 19) {
            id = 0;
            data = (byte) 0;
        }
        Block existing = l.getWorld().getBlockAt(startx, starty, startz);
        if (existing.getTypeId() != 0) {
            id = existing.getTypeId();
            data = existing.getData();
        }
        plugin.utils.setBlock(l.getWorld(), startx, starty, startz, id, data);
        startx += x;
        col++;
        if (col == c && row < w) {
            col = 0;
            startx = resetx;
            startz += z;
            row++;
        }
        if (col == c && row == w && level < h) {
            col = 0;
            row = 0;
            startx = resetx;
            startz = resetz;
            starty += 1;
            level++;
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
