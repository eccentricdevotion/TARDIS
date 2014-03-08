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
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ROOM;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
    private final Location l;
    String[][][] s;
    short[] dim;
    private int id, task, level, row, col, h, w, c, startx, starty, startz, resetx, resety, resetz;
    private final int middle_id, floor_id, x, z, tardis_id;
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
    HashMap<Block, Byte> doorblocks = new HashMap<Block, Byte>();
    HashMap<Block, Byte> torchblocks = new HashMap<Block, Byte>();
    HashMap<Block, Byte> redstoneTorchblocks = new HashMap<Block, Byte>();
    HashMap<Block, Byte> mushroomblocks = new HashMap<Block, Byte>();
    byte[] repeaterData = new byte[6];

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
        this.repeaterData[0] = (byte) 0;
        this.repeaterData[1] = (byte) 0;
        this.repeaterData[2] = (byte) 2;
        this.repeaterData[3] = (byte) 3;
        this.repeaterData[4] = (byte) 1;
        this.repeaterData[5] = (byte) 0;
    }

    /**
     * A runnable task that builds TARDIS rooms block by block.
     */
    @SuppressWarnings("deprecation")
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
            resety = starty;
            resetz = startz;
            world = l.getWorld();
            running = true;
            grammar = (TARDISConstants.vowels.contains(room.substring(0, 1))) ? "an " + room : "a " + room;
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                grammar += " WELL";
            }
            TARDISMessage.send(p, plugin.getPluginName() + "Started growing " + grammar + "...");
        }
        String tmp;
        if (level == h && row == w && col == (c - 1)) {
            // the entire schematic has been read :)
            if (iceblocks.size() > 0) {
                TARDISMessage.send(p, plugin.getPluginName() + "Melting the ice!");
                // set all the ice to water
                for (Block ice : iceblocks) {
                    ice.setTypeId(9);
                }
                iceblocks.clear();
            }
            if (room.equals("BAKER") || room.equals("WOOD")) {
                // set the repeaters
                for (Map.Entry<Block, Byte> entry : mushroomblocks.entrySet()) {
                    entry.getKey().setTypeId(93);
                    entry.getKey().setData(entry.getValue(), true);
                }
                mushroomblocks.clear();
            }
            if (room.equals("GREENHOUSE")) {
                // plant the sugar cane
                for (Block cane : caneblocks) {
                    cane.setTypeId(83);
                }
                caneblocks.clear();
                // attach the cocoa
                for (Map.Entry<Block, Byte> entry : cocoablocks.entrySet()) {
                    entry.getKey().setTypeId(127);
                    entry.getKey().setData(entry.getValue(), true);
                }
                cocoablocks.clear();
            }
            if (room.equals("VILLAGE")) {
                // put doors on
                for (Map.Entry<Block, Byte> entry : doorblocks.entrySet()) {
                    entry.getKey().setTypeId(64);
                    entry.getKey().setData(entry.getValue(), true);
                }
                doorblocks.clear();
            }
            // update lamp block states
            TARDISMessage.send(p, plugin.getPluginName() + "Turning on the power!");
            for (Block lamp : lampblocks) {
                lamp.setType(Material.REDSTONE_LAMP_ON);
            }
            lampblocks.clear();
            // put torches on
            for (Map.Entry<Block, Byte> entry : torchblocks.entrySet()) {
                entry.getKey().setTypeId(50);
                entry.getKey().setData(entry.getValue(), true);
            }
            torchblocks.clear();
            // put redstone torches on
            for (Map.Entry<Block, Byte> entry : redstoneTorchblocks.entrySet()) {
                entry.getKey().setTypeId(76);
                entry.getKey().setData(entry.getValue(), true);
            }
            torchblocks.clear();
            // remove the chunks, so they can unload as normal again
            if (chunkList.size() > 0) {
                for (Chunk ch : chunkList) {
                    plugin.getGeneralKeeper().getRoomChunkList().remove(ch);
                }
            }
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            String rname = (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) ? room + " WELL" : room;
            TARDISMessage.send(p, plugin.getPluginName() + "Finished growing the " + rname + "!");
        } else {
            // place one block
            tmp = s[level][row][col];
            String[] iddata = tmp.split(":");
            id = plugin.getUtils().parseInt(iddata[0]);
            data = plugin.getUtils().parseByte(iddata[1]);
            if (id == 158 || id == -98) {
                byte bit = data;
                switch (bit) {
                    case 8:
                        data = 0;
                        break;
                    case 9:
                        data = 1;
                        break;
                    case 10:
                        data = 2;
                        break;
                    case 11:
                        data = 3;
                        break;
                    case 12:
                        data = 4;
                        break;
                    case 13:
                        data = 5;
                        break;
                    default:
                        break;
                }
            }
            if (id == 55) {
                data = 0;
            }
            if (id == 35 && data == 7 && plugin.getConfig().getBoolean("creation.use_clay")) {
                id = 159;
            }
            if (id == 35 && data == 1) {
                if (middle_id == 35 && middle_data == 1 && plugin.getConfig().getBoolean("creation.use_clay")) {
                    id = 159;
                } else {
                    id = middle_id;
                }
                data = ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && middle_id == 35 && (middle_data == 5 || middle_data == 6)) ? 1 : middle_data;
            }
            if (id == 35 && data == 8) {
                if (floor_id == 35 && floor_data == 8 && plugin.getConfig().getBoolean("creation.use_clay")) {
                    id = 159;
                } else {
                    id = floor_id;
                }
                data = ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && floor_id == 35 && (floor_data == 5 || floor_data == 6)) ? 8 : floor_data;
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
                id = (floor_id == 35 && floor_data == 8 && plugin.getConfig().getBoolean("creation.use_clay")) ? 159 : floor_id;
                data = floor_data;
            }
            // set lazarus
            if (id == 72 && room.equals("LAZARUS")) {
                String plate = (new Location(world, startx, starty, startz)).toString();
                qf.insertControl(tardis_id, 19, plate, 0);
            }
            // set stable
            if (id == 88 && (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("RENDERER") || room.equals("ZERO"))) {
                HashMap<String, Object> sets = new HashMap<String, Object>();
                sets.put(room.toLowerCase(Locale.ENGLISH), world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheres = new HashMap<String, Object>();
                wheres.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", sets, wheres);
                // replace with correct block
                switch (ROOM.valueOf(room)) {
                    case VILLAGE:
                        id = 4;
                        data = 0;
                        break;
                    case STABLE:
                        id = 2;
                        data = 0;
                        break;
                    case ZERO:
                        id = 171;
                        data = 6;
                        break;
                    default:
                        id = 35;
                        data = 15;
                        // add WorldGuard region
                        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                            Location one = new Location(world, startx - 6, starty, startz - 6);
                            Location two = new Location(world, startx + 6, starty + 8, startz + 6);
                            plugin.getWorldGuardUtils().addRendererProtection(p.getName(), one, two);
                        }
                        break;
                }
            }
            // remember village doors
            if (id == 64 && room.equals("VILLAGE")) {
                Block door = world.getBlockAt(startx, starty, startz);
                doorblocks.put(door, data);
            }
            // remember torches
            if (id == 50) {
                Block torch = world.getBlockAt(startx, starty, startz);
                torchblocks.put(torch, data);
            }
            // remember redstone torches
            if (id == 76) {
                Block torch = world.getBlockAt(startx, starty, startz);
                redstoneTorchblocks.put(torch, data);
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
            // always replace bedrock (the door space in ARS rooms)
            if (id == 7) {
                if (checkRoomNextDoor(world.getBlockAt(startx, starty, startz))) {
                    id = 0;
                    data = (byte) 0;
                } else {
                    id = (middle_id == 35 && middle_data == 1 && plugin.getConfig().getBoolean("creation.use_clay")) ? 159 : middle_id;
                    data = middle_data;
                }
            }
            // always clear the door blocks on the north and west sides of adjacent spaces
            if (id == 29) {
                // only the bottom pistons
                if (starty == (resety + 2)) {
                    Block bottomdoorblock = null;
                    // north
                    if (startx == (resetx + 8) && startz == resetz) {
                        bottomdoorblock = world.getBlockAt(startx, (starty + 2), (startz - 1));
                    }
                    // west
                    if (startx == resetx && startz == (resetz + 8)) {
                        bottomdoorblock = world.getBlockAt((startx - 1), (starty + 2), startz);
                    }
                    if (bottomdoorblock != null) {
                        Block topdoorblock = bottomdoorblock.getRelative(BlockFace.UP);
                        bottomdoorblock.setType(Material.AIR);
                        topdoorblock.setType(Material.AIR);
                    }
                }
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
            if (!plugin.getGeneralKeeper().getRoomChunkList().contains(thisChunk)) {
                plugin.getGeneralKeeper().getRoomChunkList().add(thisChunk);
                chunkList.add(thisChunk);
            }
            if (id != 83 && id != 127 && id != 64 && id != 50 && id != 76 && id != 34 && !(id == 100 && data == (byte) 15)) {
                if (id == 9) {
                    plugin.getUtils().setBlock(world, startx, starty, startz, 79, (byte) 0);
                } else {
                    plugin.getUtils().setBlock(world, startx, starty, startz, id, data);
                }
            }
            // remember ice blocks
            if (id == 9 || id == 79) {
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
                    plugin.getGeneralKeeper().getGravityDownList().add(loc);
                }
                if (id == 35 && data == 5) {
                    // light green wool - gravity well up
                    loc = new Location(world, startx, starty, startz).toString();
                    HashMap<String, Object> setu = new HashMap<String, Object>();
                    setu.put("tardis_id", tardis_id);
                    setu.put("location", loc);
                    setu.put("direction", 1);
                    setu.put("distance", 16);
                    setu.put("velocity", 0.5);
                    qf.doInsert("gravity_well", setu);
                    Double[] values = new Double[]{1D, 16D, 0.5D};
                    plugin.getGeneralKeeper().getGravityUpList().put(loc, values);
                }
            }
            if (room.equals("BAKER") || room.equals("WOOD")) {
                // remember the controls
                int secondary = (room.equals("BAKER")) ? 1 : 2;
                int r = 2;
                int type;
                String loc_str;
                List<Integer> controls = Arrays.asList(new Integer[]{69, 77, 100, 143, -113});
                if (controls.contains(Integer.valueOf(id))) {
                    switch (id) {
                        case 77: // stone button - random
                            type = 1;
                            loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                            break;
                        case 100: // repeater
                            type = r;
                            loc_str = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                            Block rb = world.getBlockAt(startx, starty, startz);
                            mushroomblocks.put(rb, repeaterData[r]);
                            r++;
                            break;
                        case -113: // wood button - artron
                        case 143:
                            type = 6;
                            loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                            break;
                        default: // cake - handbrake
                            type = 0;
                            loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                    }
                    qf.insertControl(tardis_id, type, loc_str, secondary);
                }
            }
            if (room.equals("ZERO")) {
                // remember the button
                String loc_str;
                if (id == -113 || id == 143) {
                    loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                    qf.insertControl(tardis_id, 17, loc_str, 0);
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

    @SuppressWarnings("deprecation")
    private boolean checkRoomNextDoor(Block b) {
        if (b.getLocation().getBlockX() > (resetx + 10) && b.getRelative(BlockFace.EAST).getTypeId() != 0) {
            return true;
        } else if (b.getRelative(BlockFace.SOUTH).getTypeId() != 0) {
            return true;
        }
        return false;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
