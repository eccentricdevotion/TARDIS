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
package me.eccentric_nz.TARDIS.rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
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
import org.bukkit.block.Biome;
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
    JSONObject s;
    private int task, level, row, col, h, w, c, startx, starty, startz, resetx, resety, resetz;
    private final int tardis_id;
    private final Material wall_type, floor_type;
    Material type;
    byte data, wall_data, floor_data;
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
    List<Material> notThese = new ArrayList<Material>();
    HashMap<Block, Byte> cocoablocks = new HashMap<Block, Byte>();
    HashMap<Block, Byte> doorblocks = new HashMap<Block, Byte>();
    HashMap<Block, Byte> torchblocks = new HashMap<Block, Byte>();
    HashMap<Block, Byte> redstoneTorchblocks = new HashMap<Block, Byte>();
    HashMap<Block, Byte> mushroomblocks = new HashMap<Block, Byte>();
    byte[] repeaterData = new byte[6];
    HashMap<Integer, Integer> repeaterOrder = new HashMap<Integer, Integer>();
    JSONArray arr;

    public TARDISRoomRunnable(TARDIS plugin, TARDISRoomData roomData, Player p) {
        this.plugin = plugin;
        this.l = roomData.getLocation();
        this.s = roomData.getSchematic();
        this.b = roomData.getBlock();
        this.d = roomData.getDirection();
        this.wall_type = roomData.getMiddleType();
        this.wall_data = roomData.getMiddleData();
        this.floor_type = roomData.getFloorType();
        this.floor_data = roomData.getFloorData();
        this.room = roomData.getRoom();
        this.tardis_id = roomData.getTardis_id();
        this.running = false;
        this.p = p;
        this.repeaterData[0] = (byte) 0;
        this.repeaterData[1] = (byte) 0;
        this.repeaterData[2] = (byte) 1;
        this.repeaterData[3] = (byte) 2;
        this.repeaterData[4] = (byte) 0;
        this.repeaterData[5] = (byte) 3;
        this.repeaterOrder.put(2, 3);
        this.repeaterOrder.put(3, 2);
        this.repeaterOrder.put(4, 5);
        this.repeaterOrder.put(5, 4);
        this.notThese.add(Material.COCOA);
        this.notThese.add(Material.PISTON_EXTENSION);
        this.notThese.add(Material.REDSTONE_TORCH_ON);
        this.notThese.add(Material.SUGAR_CANE_BLOCK);
        this.notThese.add(Material.TORCH);
        this.notThese.add(Material.WOODEN_DOOR);
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
            JSONObject dim = s.getJSONObject("dimensions");
            arr = s.getJSONArray("input");
            h = dim.getInt("height") - 1;
            w = dim.getInt("width") - 1;
            c = dim.getInt("length");
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
            TARDISMessage.send(p, "ROOM_START", grammar);
        }
        if (level == h && row == w && col == (c - 1)) {
            // the entire schematic has been read :)
            if (iceblocks.size() > 0) {
                TARDISMessage.send(p, "ICE");
                // set all the ice to water
                for (Block ice : iceblocks) {
                    ice.setType(Material.ICE);
                }
                iceblocks.clear();
            }
            if (room.equals("BAKER") || room.equals("WOOD")) {
                // set the repeaters
                for (Map.Entry<Block, Byte> entry : mushroomblocks.entrySet()) {
                    entry.getKey().setType(Material.DIODE_BLOCK_OFF);
                    entry.getKey().setData(entry.getValue(), true);
                }
                mushroomblocks.clear();
            }
            if (room.equals("GREENHOUSE")) {
                // plant the sugar cane
                for (Block cane : caneblocks) {
                    cane.setType(Material.SUGAR_CANE_BLOCK);
                }
                caneblocks.clear();
                // attach the cocoa
                for (Map.Entry<Block, Byte> entry : cocoablocks.entrySet()) {
                    entry.getKey().setType(Material.COCOA);
                    entry.getKey().setData(entry.getValue(), true);
                }
                cocoablocks.clear();
            }
            if (room.equals("VILLAGE")) {
                // put doors on
                for (Map.Entry<Block, Byte> entry : doorblocks.entrySet()) {
                    entry.getKey().setType(Material.WOODEN_DOOR);
                    entry.getKey().setData(entry.getValue(), true);
                }
                doorblocks.clear();
            }
            // update lamp block states
            TARDISMessage.send(p, "ROOM_POWER");
            for (Block lamp : lampblocks) {
                lamp.setType(Material.REDSTONE_LAMP_ON);
            }
            lampblocks.clear();
            // put torches on
            for (Map.Entry<Block, Byte> entry : torchblocks.entrySet()) {
                entry.getKey().setType(Material.TORCH);
                entry.getKey().setData(entry.getValue(), true);
            }
            torchblocks.clear();
            // put redstone torches on
            for (Map.Entry<Block, Byte> entry : redstoneTorchblocks.entrySet()) {
                entry.getKey().setType(Material.REDSTONE_TORCH_ON);
                entry.getKey().setData(entry.getValue(), true);
            }
            torchblocks.clear();
            // remove the chunks, so they can unload as normal again
            if (chunkList.size() > 0) {
                for (Chunk ch : chunkList) {
                    if (plugin.getConfig().getBoolean("creation.sky_biome")) {
                        // refesh the cunk so ctm textures show
                        world.refreshChunk(ch.getX(), ch.getZ());
                    }
                    plugin.getGeneralKeeper().getRoomChunkList().remove(ch);
                }
            }
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            String rname = (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) ? room + " WELL" : room;
            TARDISMessage.send(p, "ROOM_FINISHED", rname);
        } else {
            // place one block
            JSONObject v = arr.getJSONArray(level).getJSONArray(row).getJSONObject(col);
            type = Material.valueOf(v.getString("type"));
            data = v.getByte("data");
            if (type.equals(Material.DROPPER)) {
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
            if (type.equals(Material.REDSTONE_WIRE)) {
                data = 0;
            }
            if (type.equals(Material.WOOL) && data == 7 && plugin.getConfig().getBoolean("creation.use_clay")) {
                type = Material.STAINED_CLAY;
            }
            if (type.equals(Material.WOOL) && data == 1) {
                if (wall_type.equals(Material.WOOL) && wall_data == 1 && plugin.getConfig().getBoolean("creation.use_clay")) {
                    type = Material.STAINED_CLAY;
                } else {
                    type = wall_type;
                }
                data = ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && wall_type.equals(Material.WOOL) && (wall_data == 5 || wall_data == 6)) ? 1 : wall_data;
            }
            if (type.equals(Material.WOOL) && data == 8) {
                if (floor_type.equals(Material.WOOL) && floor_data == 8 && plugin.getConfig().getBoolean("creation.use_clay")) {
                    type = Material.STAINED_CLAY;
                } else {
                    type = floor_type;
                }
                data = ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && floor_type.equals(Material.WOOL) && (floor_data == 5 || floor_data == 6)) ? 8 : floor_data;
            }
            QueryFactory qf = new QueryFactory(plugin);
            // set condenser
            if (type.equals(Material.CHEST) && room.equals("HARMONY")) {
                HashMap<String, Object> setc = new HashMap<String, Object>();
                setc.put("condenser", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", setc, wherec);
            }
            // set drop chest
            if (type.equals(Material.TRAPPED_CHEST) && room.equals("VAULT") && p.hasPermission("tardis.vault")) {
                // determine the min x, y, z coords
                int mx = startx % 16;
                if (mx < 0) {
                    mx += 16;
                }
                int mz = startz % 16;
                if (mz < 0) {
                    mz += 16;
                }
                int x = startx - mx;
                int y = starty - (starty % 16);
                int z = startz - mz;
                String pos = new Location(world, startx, starty, startz).toString();
                HashMap<String, Object> setv = new HashMap<String, Object>();
                setv.put("tardis_id", tardis_id);
                setv.put("location", pos);
                setv.put("x", x);
                setv.put("y", y);
                setv.put("z", z);
                qf.doInsert("vaults", setv);
            }
            // set farm
            if (type.equals(Material.MOB_SPAWNER) && room.equals("FARM")) {
                HashMap<String, Object> setf = new HashMap<String, Object>();
                setf.put("farm", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheref = new HashMap<String, Object>();
                wheref.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", setf, wheref);
                // replace with floor material
                type = (floor_type.equals(Material.WOOL) && floor_data == 8 && plugin.getConfig().getBoolean("creation.use_clay")) ? Material.STAINED_CLAY : floor_type;
                data = floor_data;
            }
            // set lazarus
            if (type.equals(Material.WOOD_PLATE) && room.equals("LAZARUS")) {
                String plate = (new Location(world, startx, starty, startz)).toString();
                qf.insertControl(tardis_id, 19, plate, 0);
            }
            // set stable
            if (type.equals(Material.SOUL_SAND) && (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("RENDERER") || room.equals("ZERO"))) {
                HashMap<String, Object> sets = new HashMap<String, Object>();
                sets.put(room.toLowerCase(Locale.ENGLISH), world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheres = new HashMap<String, Object>();
                wheres.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", sets, wheres);
                // replace with correct block
                switch (ROOM.valueOf(room)) {
                    case VILLAGE:
                        type = Material.COBBLESTONE;
                        data = 0;
                        break;
                    case STABLE:
                        type = Material.GRASS;
                        data = 0;
                        break;
                    case ZERO:
                        type = Material.CARPET;
                        data = 6;
                        break;
                    default:
                        type = Material.WOOL;
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
            if (type.equals(Material.WOODEN_DOOR) && room.equals("VILLAGE")) {
                Block door = world.getBlockAt(startx, starty, startz);
                doorblocks.put(door, data);
            }
            // remember torches
            if (type.equals(Material.TORCH)) {
                Block torch = world.getBlockAt(startx, starty, startz);
                torchblocks.put(torch, data);
            }
            // remember redstone torches
            if (type.equals(Material.REDSTONE_TORCH_ON)) {
                Block torch = world.getBlockAt(startx, starty, startz);
                redstoneTorchblocks.put(torch, data);
            }
            // set farmland hydrated
            if (type.equals(Material.SOIL) && data == 0) {
                data = (byte) 4;
            }
            if (room.equals("GREENHOUSE")) {
                // remember sugar cane
                if (type.equals(Material.SUGAR_CANE_BLOCK)) {
                    Block cane = world.getBlockAt(startx, starty, startz);
                    caneblocks.add(cane);
                }
                // remember cocoa
                if (type.equals(Material.COCOA)) {
                    Block cocoa = world.getBlockAt(startx, starty, startz);
                    cocoablocks.put(cocoa, data);
                }
            }
            if (room.equals("RAIL") && type.equals(Material.FENCE)) {
                // remember fence location so we can teleport the storage minecart
                String loc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("rail", loc);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", set, where);
            }
            // always replace bedrock (the door space in ARS rooms)
            if (type.equals(Material.BEDROCK)) {
                if (checkRoomNextDoor(world.getBlockAt(startx, starty, startz))) {
                    type = Material.AIR;
                    data = (byte) 0;
                } else {
                    type = (wall_type.equals(Material.WOOL) && wall_data == 1 && plugin.getConfig().getBoolean("creation.use_clay")) ? Material.STAINED_CLAY : wall_type;
                    data = wall_data;
                }
            }
            // always clear the door blocks on the north and west sides of adjacent spaces
            if (type.equals(Material.PISTON_STICKY_BASE)) {
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
            if (type.equals(Material.SPONGE)) {
                type = Material.AIR;
                data = (byte) 0;
            } else {
                Block existing = world.getBlockAt(startx, starty, startz);
                if (existing.getTypeId() != 0) {
                    if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                        switch (type) {
                            case AIR:
                            case WOOL:
                            case SMOOTH_BRICK:
                                break;
                            default:
                                type = existing.getType();
                                data = existing.getData();
                                break;
                        }
                    } else {
                        type = existing.getType();
                        data = existing.getData();
                    }
                }
            }

            Chunk thisChunk = world.getChunkAt(world.getBlockAt(startx, starty, startz));
            if (!plugin.getGeneralKeeper().getRoomChunkList().contains(thisChunk)) {
                plugin.getGeneralKeeper().getRoomChunkList().add(thisChunk);
                chunkList.add(thisChunk);
            }
            // if we're setting the biome to sky, do it now
            if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                world.setBiome(startx, startz, Biome.SKY);
            }
            if (!notThese.contains(type) && !(type.equals(Material.HUGE_MUSHROOM_2) && data == (byte) 15)) {
                if (type.equals(Material.STATIONARY_WATER)) {
                    plugin.getUtils().setBlock(world, startx, starty, startz, 79, (byte) 0);
                } else {
                    plugin.getUtils().setBlock(world, startx, starty, startz, type, data);
                }
            }
            // remember ice blocks
            if (type.equals(Material.STATIONARY_WATER) || type.equals(Material.ICE)) {
                Block icy = world.getBlockAt(startx, starty, startz);
                iceblocks.add(icy);
            }
            // remember lamp blocks
            if (type.equals(Material.REDSTONE_LAMP_ON)) {
                Block lamp = world.getBlockAt(startx, starty, startz);
                lampblocks.add(lamp);
            }
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                String loc;
                if (type.equals(Material.WOOL) && data == 6) {
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
                if (type.equals(Material.WOOL) && data == 5) {
                    // light green wool - gravity well up
                    loc = new Location(world, startx, starty, startz).toString();
                    HashMap<String, Object> setu = new HashMap<String, Object>();
                    setu.put("tardis_id", tardis_id);
                    setu.put("location", loc);
                    setu.put("direction", 1);
                    setu.put("distance", 16);
                    setu.put("velocity", 0.5);
                    qf.doInsert("gravity_well", setu);
                    Double[] values = {1D, 16D, 0.5D};
                    plugin.getGeneralKeeper().getGravityUpList().put(loc, values);
                }
            }
            if (room.equals("BAKER") || room.equals("WOOD")) {
                // remember the controls
                int secondary = (room.equals("BAKER")) ? 1 : 2;
                int r = 2;
                int ctype;
                String loc_str;
                List<Material> controls = Arrays.asList(Material.LEVER, Material.STONE_BUTTON, Material.HUGE_MUSHROOM_2, Material.WOOD_BUTTON);
                if (controls.contains(this.type)) {
                    switch (this.type) {
                        case STONE_BUTTON: // stone button - random
                            ctype = 1;
                            loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                            break;
                        case HUGE_MUSHROOM_2: // repeater
                            ctype = repeaterOrder.get(r);
                            loc_str = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                            Block rb = world.getBlockAt(startx, starty, startz);
                            mushroomblocks.put(rb, repeaterData[r]);
                            r++;
                            break;
                        case WOOD_BUTTON: // wood button - artron
                            ctype = 6;
                            loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                            break;
                        default: // cake - handbrake
                            ctype = 0;
                            loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                    }
                    qf.insertControl(tardis_id, ctype, loc_str, secondary);
                }
            }
            if (room.equals("ZERO")) {
                // remember the button
                String loc_str;
                if (type.equals(Material.WOOD_BUTTON)) {
                    loc_str = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                    qf.insertControl(tardis_id, 17, loc_str, 0);
                }
            }
            startz += 1;
            col++;
            if (col == c && row < w) {
                col = 0;
                startz = resetz;
                startx += 1;
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

    private boolean checkRoomNextDoor(Block b) {
        if (b.getLocation().getBlockZ() < (resetz + 10) && !b.getRelative(BlockFace.EAST).getType().equals(Material.AIR)) {
            return true;
        } else if (!b.getRelative(BlockFace.SOUTH).getType().equals(Material.AIR)) {
            return true;
        }
        return false;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
