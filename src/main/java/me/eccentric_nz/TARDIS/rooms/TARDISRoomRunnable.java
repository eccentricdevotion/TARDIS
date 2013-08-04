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
package me.eccentric_nz.TARDIS.rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private int id, task, level, row, col, h, w, c, middle_id, floor_id, startx, starty, startz, resetx, resetz, x, z, tardis_id;
    byte data, middle_data, floor_data;
    Block b;
    COMPASS d;
    String room;
    String grammar;
    private boolean running;
    Player p;
    World world;
    List<Chunk> chunkList = new ArrayList<Chunk>();
    List<Block> iceblocks = new ArrayList<Block>();
    List<Block> lampblocks = new ArrayList<Block>();
    List<Block> caneblocks = new ArrayList<Block>();
    HashMap<Block, Byte> cocoablocks = new HashMap<Block, Byte>();

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
        this.floor_id = roomData.getFloor_id();
        this.floor_data = roomData.getFloor_data();
        this.room = roomData.getRoom();
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
            world = l.getWorld();
            running = true;
            grammar = (TARDISConstants.vowels.contains(room.substring(0, 1))) ? "an " + room : "a " + room;
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                grammar += " WELL";
            }
            p.sendMessage(plugin.pluginName + "Started growing " + grammar + "...");
        }
        String tmp;
        if (level == h && row == w && col == (c - 1)) {
            // the entire schematic has been read :)
            if (!room.equals("GRAVITY") && !room.equals("ANTIGRAVITY")) {
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
            if (iceblocks.size() > 0) {
                p.sendMessage(plugin.pluginName + "Melting the ice!");
                // set all the ice to water
                for (Block ice : iceblocks) {
                    ice.setTypeId(9);
                }
                iceblocks.clear();
            }
            if (room.equals("GREENHOUSE")) {
                // plant the sugar cane
                for (Block cane : caneblocks) {
                    cane.setTypeId(83);
                }
                caneblocks.clear();
                // attach the cocoa
                for (Map.Entry<Block, Byte> entry : cocoablocks.entrySet()) {
                    entry.getKey().setTypeIdAndData(127, entry.getValue(), true);
                }
                cocoablocks.clear();
            }
            // update lamp block states
            p.sendMessage(plugin.pluginName + "Turning on the lights!");
            for (Block lamp : lampblocks) {
                lamp.setType(Material.REDSTONE_LAMP_ON);
            }
            lampblocks.clear();
            // remove the chunks, so they can unload as normal again
            if (chunkList.size() > 0) {
                for (Chunk ch : chunkList) {
                    plugin.roomChunkList.remove(ch);
                }
            }
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            String rname = (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) ? room + " WELL" : room;
            p.sendMessage(plugin.pluginName + "Finished growing the " + rname + "!");
        } else {
            // place one block
            tmp = s[level][row][col];
            String[] iddata = tmp.split(":");
            id = plugin.utils.parseNum(iddata[0]);
            if (TARDISConstants.PROBLEM_BLOCKS.contains(Integer.valueOf(id)) && (d.equals(COMPASS.NORTH) || d.equals(COMPASS.WEST))) {
                data = TARDISDataRecalculator.calculateData(id, Byte.parseByte(iddata[1]));
            } else {
                data = Byte.parseByte(iddata[1]);
            }
            if (id == 35 && data == 1) {
                if (middle_id == 35 && middle_data == 1 && plugin.getConfig().getBoolean("use_clay")) {
                    id = 159;
                } else {
                    id = middle_id;
                }
                data = middle_data;
            }
            if (id == 35 && data == 8) {
                if (floor_id == 35 && floor_data == 8 && plugin.getConfig().getBoolean("use_clay")) {
                    id = 159;
                } else {
                    id = floor_id;
                }
                data = floor_data;
            }
            QueryFactory qf = new QueryFactory(plugin);
            // set condenser
            if (id == 54 && room.equals("HARMONY")) {
                HashMap<String, Object> setc = new HashMap<String, Object>();
                setc.put("condenser", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", setc, wherec);
            }
            // set farm
            if (id == 52 && room.equals("FARM")) {
                HashMap<String, Object> setf = new HashMap<String, Object>();
                setf.put("farm", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheref = new HashMap<String, Object>();
                wheref.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", setf, wheref);
                // replace with floor material
                id = (floor_id == 35 && floor_data == 8 && plugin.getConfig().getBoolean("use_clay")) ? 159 : floor_id;
                data = floor_data;
            }
            // set stable
            if (id == 88 && room.equals("STABLE")) {
                HashMap<String, Object> sets = new HashMap<String, Object>();
                sets.put("stable", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheres = new HashMap<String, Object>();
                wheres.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", sets, wheres);
                // replace with grass
                id = 2;
                data = 0;
            }
            // set farmland hydrated
            if (id == 60 && data == 0) {
                data = (byte) 4;
            }
            if (room.equals("GREENHOUSE")) {
                // remember sugar cane
                if (id == 83) {
                    Block cane = world.getBlockAt(startx, starty, startz);
                    caneblocks.add(cane);
                }
                // remember cocoa
                if (id == 127) {
                    Block cocoa = world.getBlockAt(startx, starty, startz);
                    cocoablocks.put(cocoa, data);
                }
            }
            if (room.equals("RAIL") && id == 85) {
                // remember fence location so we can teleport the storage minecart
                String loc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("rail", loc);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", set, where);
            }
            // always remove sponge
            if (id == 19) {
                id = 0;
                data = (byte) 0;
            } else {
                Block existing = world.getBlockAt(startx, starty, startz);
                if (existing.getTypeId() != 0) {
                    if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                        switch (id) {
                            case 20:
                            case 35:
                            case 98:
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
            if (id != 83 && id != 127) {
                plugin.utils.setBlock(world, startx, starty, startz, id, data);
            }
            // remember ice blocks
            if (id == 79) {
                Block icy = world.getBlockAt(startx, starty, startz);
                iceblocks.add(icy);
            }
            // remember lamp blocks
            if (id == 124) {
                Block lamp = world.getBlockAt(startx, starty, startz);
                lampblocks.add(lamp);
            }
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                String loc;
                if (id == 35 && data == 6) {
                    // pink wool - gravity well down
                    loc = new Location(world, startx, starty, startz).toString();
                    HashMap<String, Object> setd = new HashMap<String, Object>();
                    setd.put("tardis_id", tardis_id);
                    setd.put("location", loc);
                    setd.put("direction", 0);
                    setd.put("distance", 0);
                    setd.put("velocity", 0);
                    qf.doInsert("gravity_well", setd);
                    plugin.gravityDownList.add(loc);
                }
                if (id == 35 && data == 5) {
                    // light green wool - gravity well up
                    loc = new Location(world, startx, starty, startz).toString();
                    HashMap<String, Object> setu = new HashMap<String, Object>();
                    setu.put("tardis_id", tardis_id);
                    setu.put("location", loc);
                    setu.put("direction", 1);
                    setu.put("distance", 11);
                    setu.put("velocity", 0.5);
                    qf.doInsert("gravity_well", setu);
                    Double[] values = new Double[]{1D, 11D, 0.5D};
                    plugin.gravityUpList.put(loc, values);
                }
            }
            if (room.equals("BAKER") || room.equals("WOOD")) {
                // remember the controls
                int secondary = (room.equals("BAKER")) ? 1 : 2;
                int r = 2;
                int type;
                String loc_str;
                List<Integer> controls = Arrays.asList(new Integer[]{69, 77, 92, 143, -113});
                if (controls.contains(Integer.valueOf(id))) {
                    switch (id) {
                        case 77: // stone button - random
                            type = 1;
                            loc_str = plugin.utils.makeLocationStr(world, startx, starty, startz);
                            break;
                        case 93: // repeater
                            type = r;
                            loc_str = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                            r++;
                            break;
                        case -113: // wood button - artron
                        case 143:
                            type = 6;
                            loc_str = plugin.utils.makeLocationStr(world, startx, starty, startz);
                            break;
                        default: // cake - handbrake
                            type = 0;
                            loc_str = plugin.utils.makeLocationStr(world, startx, starty, startz);
                    }
                    qf.insertControl(tardis_id, type, loc_str, secondary);
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
    }

    public void setTask(int task) {
        this.task = task;
    }
}
