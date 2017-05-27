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
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.ROOM;
import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
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
    String room;
    String grammar;
    private boolean running;
    Player p;
    World world;
    List<Chunk> chunkList = new ArrayList<>();
    List<Block> iceblocks = new ArrayList<>();
    List<Block> lampblocks = new ArrayList<>();
    List<Block> caneblocks = new ArrayList<>();
    List<Block> melonblocks = new ArrayList<>();
    List<Block> potatoblocks = new ArrayList<>();
    List<Block> carrotblocks = new ArrayList<>();
    List<Block> pumpkinblocks = new ArrayList<>();
    List<Block> wheatblocks = new ArrayList<>();
    List<Material> notThese = new ArrayList<>();
    HashMap<Block, Byte> cocoablocks = new HashMap<>();
    HashMap<Block, Byte> doorblocks = new HashMap<>();
    HashMap<Block, Byte> leverblocks = new HashMap<>();
    HashMap<Block, Byte> torchblocks = new HashMap<>();
    HashMap<Block, Byte> redstoneTorchblocks = new HashMap<>();
    HashMap<Block, Byte> mushroomblocks = new HashMap<>();
    HashMap<Block, JSONObject> standingBanners = new HashMap<>();
    HashMap<Block, JSONObject> wallBanners = new HashMap<>();
    byte[] repeaterData = new byte[6];
    HashMap<Integer, Integer> repeaterOrder = new HashMap<>();
    JSONArray arr;

    public TARDISRoomRunnable(TARDIS plugin, TARDISRoomData roomData, Player p) {
        this.plugin = plugin;
        this.l = roomData.getLocation();
        this.s = roomData.getSchematic();
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
        this.notThese.add(Material.CARROT);
        this.notThese.add(Material.COCOA);
        this.notThese.add(Material.CROPS);
        this.notThese.add(Material.LEVER);
        this.notThese.add(Material.MELON_STEM);
        this.notThese.add(Material.PISTON_EXTENSION);
        this.notThese.add(Material.POTATO);
        this.notThese.add(Material.PUMPKIN_STEM);
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
            grammar = (TARDISConstants.VOWELS.contains(room.substring(0, 1))) ? "an " + room : "a " + room;
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
                iceblocks.forEach((ice) -> {
                    ice.setType(Material.STATIONARY_WATER);
                });
                iceblocks.clear();
            }
            if (room.equals("BAKER") || room.equals("WOOD")) {
                // set the repeaters
                mushroomblocks.entrySet().forEach((entry) -> {
                    entry.getKey().setType(Material.DIODE_BLOCK_OFF);
                    entry.getKey().setData(entry.getValue(), true);
                });
                mushroomblocks.clear();
            }
            if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
                // plant the sugar cane
                caneblocks.forEach((cane) -> {
                    cane.setType(Material.SUGAR_CANE_BLOCK);
                });
                caneblocks.clear();
                // attach the cocoa
                cocoablocks.entrySet().forEach((entry) -> {
                    entry.getKey().setType(Material.COCOA);
                    entry.getKey().setData(entry.getValue(), true);
                });
                cocoablocks.clear();
                // plant the melon
                melonblocks.forEach((melon) -> {
                    melon.setType(Material.MELON_STEM);
                });
                melonblocks.clear();
                // plant the pumpkin
                pumpkinblocks.forEach((pumpkin) -> {
                    pumpkin.setType(Material.PUMPKIN_STEM);
                });
                pumpkinblocks.clear();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // plant the wheat
                    wheatblocks.forEach((wheat) -> {
                        wheat.setType(Material.CROPS);
                    });
                    wheatblocks.clear();
                    // plant the carrot
                    carrotblocks.forEach((carrot) -> {
                        carrot.setType(Material.CARROT);
                    });
                    carrotblocks.clear();
                    // plant the potato
                    potatoblocks.forEach((potato) -> {
                        potato.setType(Material.POTATO);
                    });
                    potatoblocks.clear();
                }, 5L);
            }
            if (room.equals("VILLAGE")) {
                // put doors on
                doorblocks.entrySet().forEach((entry) -> {
                    entry.getKey().setType(Material.WOODEN_DOOR);
                    entry.getKey().setData(entry.getValue(), true);
                });
                doorblocks.clear();
            }
            // put levers on
            leverblocks.entrySet().forEach((entry) -> {
                entry.getKey().setTypeIdAndData(69, entry.getValue(), true);
            });
            leverblocks.clear();
            // update lamp block states
            TARDISMessage.send(p, "ROOM_POWER");
            lampblocks.forEach((lamp) -> {
                lamp.setType(Material.REDSTONE_LAMP_ON);
            });
            lampblocks.clear();
            // put torches on
            torchblocks.entrySet().forEach((entry) -> {
                entry.getKey().setTypeIdAndData(50, entry.getValue(), true);
            });
            torchblocks.clear();
            // put redstone torches on
            redstoneTorchblocks.entrySet().forEach((entry) -> {
                entry.getKey().setTypeIdAndData(76, entry.getValue(), true);
            });
            torchblocks.clear();
            // set banners
            setBanners(176, standingBanners);
            setBanners(177, wallBanners);
            // remove the chunks, so they can unload as normal again
            if (chunkList.size() > 0) {
                chunkList.forEach((ch) -> {
                    if (plugin.getConfig().getBoolean("creation.sky_biome")) {
                        // refesh the cunk so ctm textures show
                        //world.refreshChunk(ch.getX(), ch.getZ());
                        plugin.getTardisHelper().refreshChunk(ch);
                    }
                    plugin.getGeneralKeeper().getRoomChunkList().remove(ch);
                });
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
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("condenser", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wherec = new HashMap<>();
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
                HashMap<String, Object> setv = new HashMap<>();
                setv.put("tardis_id", tardis_id);
                setv.put("location", pos);
                setv.put("x", x);
                setv.put("y", y);
                setv.put("z", z);
                qf.doInsert("vaults", setv);
            }
            // set farm
            if (type.equals(Material.MOB_SPAWNER) && room.equals("FARM")) {
                HashMap<String, Object> setf = new HashMap<>();
                setf.put("farm", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheref = new HashMap<>();
                wheref.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", setf, wheref);
                // replace with floor material
                type = (floor_type.equals(Material.WOOL) && floor_data == 8 && plugin.getConfig().getBoolean("creation.use_clay")) ? Material.STAINED_CLAY : floor_type;
                data = floor_data;
                // update player prefs - turn on mob farming
                HashMap<String, Object> setpp = new HashMap<>();
                setpp.put("farm_on", 1);
                HashMap<String, Object> wherepp = new HashMap<>();
                wherepp.put("uuid", p.getUniqueId().toString());
                qf.doUpdate("player_prefs", setpp, wherepp);
                TARDISMessage.send(p, "PREF_WAS_ON", "Mob farming");
            }
            // set lazarus
            if (type.equals(Material.WOOD_PLATE) && room.equals("LAZARUS")) {
                String plate = (new Location(world, startx, starty, startz)).toString();
                qf.insertControl(tardis_id, 19, plate, 0);
            }
            // set stable
            if (type.equals(Material.SOUL_SAND) && (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("RENDERER") || room.equals("ZERO") || room.equals("HUTCH") || room.equals("IGLOO") || room.equals("STALL") || room.equals("BIRDCAGE"))) {
                HashMap<String, Object> sets = new HashMap<>();
                sets.put(room.toLowerCase(Locale.ENGLISH), world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", sets, wheres);
                // replace with correct block
                switch (ROOM.valueOf(room)) {
                    case VILLAGE:
                        type = Material.COBBLESTONE;
                        data = 0;
                        break;
                    case HUTCH:
                    case STABLE:
                    case STALL:
                        type = Material.GRASS;
                        data = 0;
                        break;
                    case BIRDCAGE:
                        type = Material.DIRT;
                        data = 2;
                        break;
                    case IGLOO:
                        type = Material.PACKED_ICE;
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
            if (type.equals(Material.SOUL_SAND) && room.equals("SMELTER")) {
                String pos = new Location(world, startx, starty, startz).toString();
                HashMap<String, Object> setsm = new HashMap<>();
                setsm.put("tardis_id", tardis_id);
                setsm.put("location", pos);
                qf.doInsert("vaults", setsm);
                type = Material.CHEST;
                data = 0;
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
            // remember torches
            if (type.equals(Material.LEVER)) {
                Block lever = world.getBlockAt(startx, starty, startz);
                leverblocks.put(lever, data);
            }
            // remember redstone torches
            if (type.equals(Material.REDSTONE_TORCH_ON)) {
                Block torch = world.getBlockAt(startx, starty, startz);
                redstoneTorchblocks.put(torch, data);
            }
            // remember banners
            if (type.equals(Material.STANDING_BANNER) || type.equals(Material.WALL_BANNER)) {
                Block banner = world.getBlockAt(startx, starty, startz);
                JSONObject state = v.optJSONObject("banner");
                if (state != null) {
                    if (type.equals(Material.STANDING_BANNER)) {
                        standingBanners.put(banner, state);
                    } else {
                        wallBanners.put(banner, state);
                    }
                }
            }
            // set farmland hydrated
            if (type.equals(Material.SOIL) && data == 0) {
                data = (byte) 4;
            }
            if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
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
                // remember wheat
                if (type.equals(Material.CROPS)) {
                    Block crops = world.getBlockAt(startx, starty, startz);
                    wheatblocks.add(crops);
                }
                // remember melon
                if (type.equals(Material.MELON_STEM)) {
                    Block melon = world.getBlockAt(startx, starty, startz);
                    melonblocks.add(melon);
                }
                // remember pumpkin
                if (type.equals(Material.PUMPKIN_STEM)) {
                    Block pumpkin = world.getBlockAt(startx, starty, startz);
                    pumpkinblocks.add(pumpkin);
                }
                // remember carrot
                if (type.equals(Material.CARROT)) {
                    Block carrot = world.getBlockAt(startx, starty, startz);
                    carrotblocks.add(carrot);
                }
                // remember potato
                if (type.equals(Material.POTATO)) {
                    Block potato = world.getBlockAt(startx, starty, startz);
                    potatoblocks.add(potato);
                }
                if (level == 4 && room.equals("GREENHOUSE")) {
                    // set all the ice to water
                    iceblocks.forEach((ice) -> {
                        ice.setType(Material.STATIONARY_WATER);
                    });
                    iceblocks.clear();
                }
            }
            if (room.equals("RAIL") && type.equals(Material.FENCE)) {
                // remember fence location so we can teleport the storage minecart
                String loc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                HashMap<String, Object> set = new HashMap<>();
                set.put("rail", loc);
                HashMap<String, Object> where = new HashMap<>();
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
                if (!existing.getType().equals(Material.AIR)) {
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
                world.setBiome(startx, startz, Biome.VOID);
            }
            if (!notThese.contains(type) && !(type.equals(Material.HUGE_MUSHROOM_2) && data == (byte) 15)) {
                if (type.equals(Material.STATIONARY_WATER)) {
                    TARDISBlockSetters.setBlock(world, startx, starty, startz, 79, (byte) 0);
                } else {
                    TARDISBlockSetters.setBlock(world, startx, starty, startz, type, data);
                }
            }
            // remember ice blocks
            if ((type.equals(Material.STATIONARY_WATER) || type.equals(Material.ICE)) && !room.equals("IGLOO")) {
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
                    HashMap<String, Object> setd = new HashMap<>();
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
                    HashMap<String, Object> setu = new HashMap<>();
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
                List<Material> controls = Arrays.asList(Material.CAKE_BLOCK, Material.STONE_BUTTON, Material.HUGE_MUSHROOM_2, Material.WOOD_BUTTON);
                if (controls.contains(this.type)) {
                    switch (this.type) {
                        case STONE_BUTTON: // stone button - random
                            ctype = 1;
                            loc_str = TARDISLocationGetters.makeLocationStr(world, startx, starty, startz);
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
                            loc_str = TARDISLocationGetters.makeLocationStr(world, startx, starty, startz);
                            break;
                        default: // cake - handbrake
                            ctype = 0;
                            loc_str = TARDISLocationGetters.makeLocationStr(world, startx, starty, startz);
                    }
                    qf.insertControl(tardis_id, ctype, loc_str, secondary);
                }
            }
            if (room.equals("ZERO")) {
                // remember the button
                String loc_str;
                if (type.equals(Material.WOOD_BUTTON)) {
                    loc_str = TARDISLocationGetters.makeLocationStr(world, startx, starty, startz);
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
                int percent = TARDISNumberParsers.roundUp(level * 100, h);
                if (percent > 0) {
                    TARDISMessage.send(p, "ROOM_PERCENT", room, String.format("%d", percent));
                }
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
