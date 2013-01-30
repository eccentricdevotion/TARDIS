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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * The TARDIS had a swimming pool. After the TARDIS' crash following the
 * Doctor's tenth regeneration, the pool's water - or perhaps the pool itself -
 * fell into the library. After the TARDIS had fixed itself, the swimming pool
 * was restored but the Doctor did not know where it was.
 *
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
    private boolean running;
    HashMap<String, Object> set;
    Player p;
    World world;
    List<Chunk> chunkList = new ArrayList<Chunk>();

    public TARDISRoomRunnable(TARDIS plugin, TARDISRoomData roomData, Player p) {
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
        this.running = false;
        this.p = p;
    }

    /**
     * A runnable task that builds TARDIS rooms block by block.
     */
    @Override
    public void run() {
        // initialise
        if (!running) {
            set = new HashMap<String, Object>();
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
            world = l.getWorld();
            running = true;
            p.sendMessage(plugin.pluginName + "Started growing a " + room + "...");
        }
        String tmp;
        if (level == h && row == w && col == (c - 1)) {
            // the entire schematic has been read :)
            if (!room.equals("GRAVITY")) {
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
            }
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            p.sendMessage(plugin.pluginName + "Finished growing the " + room + "!");
            if (chunkList.size() > 0) {
                for (Chunk c : chunkList) {
                    plugin.roomChunkList.remove(c);
                }
            }
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
        // always remove sponge
        if (id == 19) {
            id = 0;
            data = (byte) 0;
        } else {
            Block existing = world.getBlockAt(startx, starty, startz);
            if (existing.getTypeId() != 0) {
                if (room.equals("GRAVITY")) {
                    switch (id) {
                        case 35:
                            break;
                        default:
                            id = existing.getTypeId();
                            data = existing.getData();
                            break;
                    }
                } else {
                    id = existing.getTypeId();
                    data = existing.getData();
                }
            }
        }

        Chunk thisChunk = world.getChunkAt(world.getBlockAt(startx, starty, startz));
        if (!plugin.roomChunkList.contains(thisChunk)) {
            plugin.roomChunkList.add(thisChunk);
            chunkList.add(thisChunk);
        }
        plugin.utils.setBlock(l.getWorld(), startx, starty, startz, id, data);
        QueryFactory qf = new QueryFactory(plugin);
        if (room.equals("GRAVITY")) {
            if (id == 35 && data == 6) {
                // pink wool - gravity well down
                HashMap<String, Object> setd = new HashMap<String, Object>();
                setd.put("tardis_id", tardis_id);
                setd.put("world", l.getWorld().toString());
                setd.put("downx", startx);
                setd.put("downz", startz);
                qf.doInsert("gravity", setd);
            }
            if (id == 35 && data == 5) {
                // pink wool - gravity well down
                HashMap<String, Object> setu = new HashMap<String, Object>();
                HashMap<String, Object> where = new HashMap<String, Object>();
                setu.put("upx", startx);
                setu.put("upz", startz);
                where.put("tardis_id", tardis_id);
                qf.doUpdate("gravity", setu, where);
            }
        }
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
