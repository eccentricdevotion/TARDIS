/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.ROOM;
import me.eccentric_nz.TARDIS.enumeration.USE_CLAY;
import me.eccentric_nz.TARDIS.utility.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.Player;

import java.util.*;

import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;

/**
 * The TARDIS had a swimming pool. After the TARDIS' crash following the Doctor's tenth regeneration, the pool's water -
 * or perhaps the pool itself - fell into the library. After the TARDIS had fixed itself, the swimming pool was restored
 * but the Doctor did not know where it was.
 *
 * @author eccentric_nz
 */
public class TARDISRoomRunnable implements Runnable {

    private final TARDIS plugin;
    private final Location l;
    private final JSONObject s;
    private int task, level, row, col, h, w, c, startx, starty, startz, resetx, resety, resetz;
    private final int tardis_id;
    private final Material wall_type, floor_type;
    private Material type;
    private BlockData data;
    private final String room;
    private String grammar;
    private boolean running;
    private final Player p;
    private World world;
    private final List<Chunk> chunkList = new ArrayList<>();
    private final List<Block> iceblocks = new ArrayList<>();
    private final List<Block> lampblocks = new ArrayList<>();
    private final List<Block> caneblocks = new ArrayList<>();
    private final List<Block> melonblocks = new ArrayList<>();
    private final List<Block> potatoblocks = new ArrayList<>();
    private final List<Block> carrotblocks = new ArrayList<>();
    private final List<Block> pumpkinblocks = new ArrayList<>();
    private final List<Block> wheatblocks = new ArrayList<>();
    private final List<Block> farmlandblocks = new ArrayList<>();
    private final List<Material> notThese = new ArrayList<>();
    private final HashMap<Block, BlockData> cocoablocks = new HashMap<>();
    private final HashMap<Block, BlockData> doorblocks = new HashMap<>();
    private final HashMap<Block, BlockData> leverblocks = new HashMap<>();
    private final HashMap<Block, BlockData> torchblocks = new HashMap<>();
    private final HashMap<Block, BlockData> redstoneTorchblocks = new HashMap<>();
    private final HashMap<Block, BlockFace> mushroomblocks = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> bannerblocks = new HashMap<>();
    private final BlockFace[] repeaterData = new BlockFace[6];
    private final HashMap<Integer, Integer> repeaterOrder = new HashMap<>();
    private JSONArray arr;

    public TARDISRoomRunnable(TARDIS plugin, TARDISRoomData roomData, Player p) {
        this.plugin = plugin;
        l = roomData.getLocation();
        s = roomData.getSchematic();
        wall_type = roomData.getMiddleType();
        floor_type = roomData.getFloorType();
        room = roomData.getRoom();
        tardis_id = roomData.getTardis_id();
        running = false;
        this.p = p;
        repeaterData[0] = BlockFace.NORTH;
        repeaterData[1] = BlockFace.NORTH;
        repeaterData[2] = BlockFace.EAST;
        repeaterData[3] = BlockFace.SOUTH;
        repeaterData[4] = BlockFace.NORTH;
        repeaterData[5] = BlockFace.EAST;
        repeaterOrder.put(2, 3);
        repeaterOrder.put(3, 2);
        repeaterOrder.put(4, 5);
        repeaterOrder.put(5, 4);
        notThese.add(Material.ACACIA_DOOR);
        notThese.add(Material.BIRCH_DOOR);
        notThese.add(Material.CARROT);
        notThese.add(Material.COCOA);
        notThese.add(Material.DARK_OAK_DOOR);
        notThese.add(Material.JUNGLE_DOOR);
        notThese.add(Material.LEVER);
        notThese.add(Material.MELON_STEM);
        notThese.add(Material.OAK_DOOR);
        notThese.add(Material.PISTON_HEAD);
        notThese.add(Material.POTATO);
        notThese.add(Material.PUMPKIN_STEM);
        notThese.add(Material.REDSTONE_TORCH);
        notThese.add(Material.SPRUCE_DOOR);
        notThese.add(Material.SUGAR_CANE);
        notThese.add(Material.TORCH);
        notThese.add(Material.WHEAT);
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
                iceblocks.forEach((ice) -> ice.setType(Material.WATER));
                iceblocks.clear();
            }
            if (room.equals("BAKER") || room.equals("WOOD")) {
                // set the repeaters
                mushroomblocks.forEach((key, value) -> {
                    BlockData repeater = Material.REPEATER.createBlockData();
                    Directional directional = (Directional) repeater;
                    directional.setFacing(value);
                    key.setBlockData(directional, true);
                });
                mushroomblocks.clear();
            }
            if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
                // plant the sugar cane
                caneblocks.forEach((cane) -> cane.setType(Material.SUGAR_CANE));
                caneblocks.clear();
                // attach the cocoa
                cocoablocks.forEach((key, value) -> {
                    key.setType(Material.COCOA);
                    key.setBlockData(value, true);
                });
                cocoablocks.clear();
                // plant the melon
                melonblocks.forEach((melon) -> melon.setType(Material.MELON_STEM));
                melonblocks.clear();
                // plant the pumpkin
                pumpkinblocks.forEach((pumpkin) -> pumpkin.setType(Material.PUMPKIN_STEM));
                pumpkinblocks.clear();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // plant the wheat
                    wheatblocks.forEach((wheat) -> wheat.setType(Material.WHEAT));
                    wheatblocks.clear();
                    // plant the carrot
                    carrotblocks.forEach((carrot) -> carrot.setType(Material.CARROT));
                    carrotblocks.clear();
                    // plant the potato
                    potatoblocks.forEach((potato) -> potato.setType(Material.POTATO));
                    potatoblocks.clear();
                }, 5L);
            }
            if (room.equals("VILLAGE")) {
                // put doors on
                doorblocks.forEach((key, value) -> {
                    key.setType(Material.OAK_DOOR);
                    key.setBlockData(value, true);
                });
                doorblocks.clear();
            }
            // water farmland
            farmlandblocks.forEach((fl) -> {
                Farmland farmland = (Farmland) fl;
                farmland.setMoisture(farmland.getMaximumMoisture());
            });
            // put levers on
            leverblocks.forEach((key, value) -> key.setBlockData(value, true));
            leverblocks.clear();
            // update lamp block states
            TARDISMessage.send(p, "ROOM_POWER");
            lampblocks.forEach((lamp) -> lamp.setType(Material.REDSTONE_LAMP));
            lampblocks.clear();
            // put torches on
            torchblocks.forEach((key, value) -> key.setBlockData(value, true));
            torchblocks.clear();
            // put redstone torches on
            redstoneTorchblocks.forEach((key, value) -> key.setBlockData(value, true));
            torchblocks.clear();
            // set banners
            setBanners(bannerblocks);
            // remove the chunks, so they can unload as normal again
            if (chunkList.size() > 0) {
                chunkList.forEach((ch) -> {
                    if (plugin.getConfig().getBoolean("creation.sky_biome")) {
                        // refresh the chunk so ctm textures show
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
            data = plugin.getServer().createBlockData(v.getString("data"));
            type = data.getMaterial();
//            if (type.equals(Material.DROPPER)) {
//                byte bit = data;
//                switch (bit) {
//                    case 8:
//                        data = 0;
//                        break;
//                    case 9:
//                        data = 1;
//                        break;
//                    case 10:
//                        data = 2;
//                        break;
//                    case 11:
//                        data = 3;
//                        break;
//                    case 12:
//                        data = 4;
//                        break;
//                    case 13:
//                        data = 5;
//                        break;
//                    default:
//                        break;
//                }
//            }
            // determine 'use_clay' material
            USE_CLAY use_clay;
            try {
                use_clay = USE_CLAY.valueOf(plugin.getConfig().getString("creation.use_clay"));
            } catch (IllegalArgumentException e) {
                use_clay = USE_CLAY.WOOL;
            }
            Material ow;
            Material lgw;
            Material gw;
            switch (use_clay) {
                case TERRACOTTA:
                    ow = Material.ORANGE_TERRACOTTA;
                    lgw = Material.LIGHT_GRAY_TERRACOTTA;
                    gw = Material.GRAY_TERRACOTTA;
                    break;
                case CONCRETE:
                    ow = Material.ORANGE_CONCRETE;
                    lgw = Material.LIGHT_GRAY_CONCRETE;
                    gw = Material.GRAY_CONCRETE;
                    break;
                default:
                    ow = Material.ORANGE_WOOL;
                    lgw = Material.LIGHT_GRAY_WOOL;
                    gw = Material.GRAY_WOOL;
                    break;
            }
            if (type.equals(Material.GRAY_WOOL)) {
                type = gw;
            }
            if (type.equals(Material.ORANGE_WOOL)) {
                if (wall_type.equals(Material.ORANGE_WOOL)) {
                    type = ow;
                } else {
                    type = wall_type;
                }
                type = ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && (wall_type.equals(Material.LIME_WOOL) || wall_type.equals(Material.PINK_WOOL))) ? ow : wall_type;
            }
            if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                if (floor_type.equals(Material.LIGHT_GRAY_WOOL)) {
                    type = lgw;
                } else {
                    type = floor_type;
                }
                type = ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && (floor_type.equals(Material.LIME_WOOL) || floor_type.equals(Material.PINK_WOOL))) ? lgw : floor_type;
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
            if (type.equals(Material.SPAWNER) && room.equals("FARM")) {
                HashMap<String, Object> setf = new HashMap<>();
                setf.put("farm", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheref = new HashMap<>();
                wheref.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", setf, wheref);
                // replace with floor material
                type = (floor_type.equals(Material.LIGHT_GRAY_WOOL)) ? lgw : floor_type;
                data = type.createBlockData();
                // update player prefs - turn on mob farming
                turnOnFarming(p, qf);
            }
            // set lazarus
            if (type.equals(Material.OAK_PRESSURE_PLATE) && room.equals("LAZARUS")) {
                String plate = (new Location(world, startx, starty, startz)).toString();
                qf.insertControl(tardis_id, 19, plate, 0);
            }
            // set stable
            if (type.equals(Material.SOUL_SAND) && (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("RENDERER") || room.equals("ZERO") || room.equals("HUTCH") || room.equals("IGLOO") || room.equals("STALL") || room.equals("BIRDCAGE") || room.equals("AQUARIUM"))) {
                HashMap<String, Object> sets = new HashMap<>();
                sets.put(room.toLowerCase(Locale.ENGLISH), world.getName() + ":" + startx + ":" + starty + ":" + startz);
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("tardis_id", tardis_id);
                qf.doUpdate("farming", sets, wheres);
                // replace with correct block
                switch (ROOM.valueOf(room)) {
                    case AQUARIUM:
                        type = (floor_type.equals(Material.LIGHT_GRAY_WOOL)) ? lgw : floor_type;
                        data = type.createBlockData();
                        break;
                    case VILLAGE:
                        type = Material.COBBLESTONE;
                        break;
                    case HUTCH:
                    case STABLE:
                    case STALL:
                        type = Material.GRASS_BLOCK;
                        break;
                    case BIRDCAGE:
                        type = Material.PODZOL;
                        break;
                    case IGLOO:
                        type = Material.PACKED_ICE;
                        break;
                    case ZERO:
                        type = Material.PINK_CARPET;
                        break;
                    default:
                        type = Material.BLACK_WOOL;
                        // add WorldGuard region
                        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                            Location one = new Location(world, startx - 6, starty, startz - 6);
                            Location two = new Location(world, startx + 6, starty + 8, startz + 6);
                            plugin.getWorldGuardUtils().addRendererProtection(p.getName(), one, two);
                        }
                        break;
                }
                if (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("HUTCH") || room.equals("IGLOO") || room.equals("STALL") || room.equals("BIRDCAGE") || room.equals("AQUARIUM")) {
                    // update player prefs - turn on mob farming
                    turnOnFarming(p, qf);
                }
            }
            if (type.equals(Material.SOUL_SAND) && room.equals("SMELTER")) {
                String pos = new Location(world, startx, starty, startz).toString();
                HashMap<String, Object> setsm = new HashMap<>();
                setsm.put("tardis_id", tardis_id);
                setsm.put("location", pos);
                qf.doInsert("vaults", setsm);
                type = Material.CHEST;
                data = type.createBlockData();
            }
            // remember shell room button
            if (type.equals(Material.STONE_BUTTON) && room.equals("SHELL")) {
                String loc_str = new Location(world, startx, starty, startz).toString();
                qf.insertControl(tardis_id, 25, loc_str, 0);
            }
            // remember village doors
            if (type.equals(Material.OAK_DOOR) && room.equals("VILLAGE")) {
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
            if (type.equals(Material.REDSTONE_TORCH)) {
                Block torch = world.getBlockAt(startx, starty, startz);
                redstoneTorchblocks.put(torch, data);
            }
            // remember banners
            if (TARDISStaticUtils.isBanner(type)) {
                Block banner = world.getBlockAt(startx, starty, startz);
                JSONObject state = v.optJSONObject("banner");
                if (state != null) {
                    TARDISBannerData tbd = new TARDISBannerData(data, state);
                    bannerblocks.put(banner, tbd);
                }
            }
            if (type.equals(Material.FARMLAND)) {
                Block farmland = world.getBlockAt(startx, starty, startz);
                farmlandblocks.add(farmland);
            }
            if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
                // remember sugar cane
                if (type.equals(Material.SUGAR_CANE)) {
                    Block cane = world.getBlockAt(startx, starty, startz);
                    caneblocks.add(cane);
                }
                // remember cocoa
                if (type.equals(Material.COCOA)) {
                    Block cocoa = world.getBlockAt(startx, starty, startz);
                    cocoablocks.put(cocoa, data);
                }
                // remember wheat
                if (type.equals(Material.WHEAT)) {
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
                    iceblocks.forEach((ice) -> ice.setType(Material.WATER));
                    iceblocks.clear();
                }
            }
            if (room.equals("RAIL") && type.equals(Material.OAK_FENCE)) {
                // remember fence location so we can teleport the storage minecart
                String loc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                HashMap<String, Object> set = new HashMap<>();
                set.put("rail", loc);
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", tardis_id);
                qf.doUpdate("tardis", set, where);
            }
            // always replace bedrock (the door space in ARS rooms)
            if ((type.equals(Material.BEDROCK) && !room.equals("shell")) || (type.equals(Material.SOUL_SAND) && room.equals("shell"))) {
                if (checkRoomNextDoor(world.getBlockAt(startx, starty, startz))) {
                    type = Material.AIR;
                    data = type.createBlockData();
                } else {
                    type = (wall_type.equals(Material.ORANGE_WOOL)) ? ow : wall_type;
                    data = type.createBlockData();
                }
            }
            // always clear the door blocks on the north and west sides of adjacent spaces
            if (type.equals(Material.STICKY_PISTON)) {
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
                data = type.createBlockData();
            } else {
                Block existing = world.getBlockAt(startx, starty, startz);
                if (!existing.getType().equals(Material.AIR)) {
                    if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                        switch (type) {
                            case AIR:
                            case GRAY_WOOL:
                            case LIGHT_GRAY_WOOL:
                            case ORANGE_WOOL:
                            case STONE_BRICKS:
                                break;
                            default:
                                type = existing.getType();
                                data = existing.getBlockData();
                                break;
                        }
                    } else {
                        type = existing.getType();
                        data = existing.getBlockData();
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
                world.setBiome(startx, startz, Biome.THE_VOID);
            }
            if (!notThese.contains(type) && !type.equals(Material.MUSHROOM_STEM)) {
                if (type.equals(Material.WATER)) {
                    TARDISBlockSetters.setBlock(world, startx, starty, startz, Material.ICE);
                } else {
                    TARDISBlockSetters.setBlock(world, startx, starty, startz, data);
                }
            }
            // remember ice blocks
            if ((type.equals(Material.WATER) || type.equals(Material.ICE)) && !room.equals("IGLOO")) {
                Block icy = world.getBlockAt(startx, starty, startz);
                iceblocks.add(icy);
            }
            // remember lamp blocks
            if (type.equals(Material.REDSTONE_LAMP)) {
                Block lamp = world.getBlockAt(startx, starty, startz);
                lampblocks.add(lamp);
            }
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                String loc;
                if (type.equals(Material.PINK_WOOL)) {
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
                if (type.equals(Material.LIME_WOOL)) {
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
                List<Material> controls = Arrays.asList(Material.CAKE, Material.STONE_BUTTON, Material.MUSHROOM_STEM, Material.OAK_BUTTON);
                if (controls.contains(type)) {
                    switch (type) {
                        case STONE_BUTTON: // stone button - random
                            ctype = 1;
                            loc_str = TARDISLocationGetters.makeLocationStr(world, startx, starty, startz);
                            break;
                        case MUSHROOM_STEM: // repeater
                            ctype = repeaterOrder.get(r);
                            loc_str = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                            Block rb = world.getBlockAt(startx, starty, startz);
                            mushroomblocks.put(rb, repeaterData[r]);
                            r++;
                            break;
                        case OAK_BUTTON: // oak button - artron
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
                if (type.equals(Material.OAK_BUTTON)) {
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

    private void turnOnFarming(Player p, QueryFactory qf) {
        // update player prefs - turn on mob farming
        HashMap<String, Object> setpp = new HashMap<>();
        setpp.put("farm_on", 1);
        HashMap<String, Object> wherepp = new HashMap<>();
        wherepp.put("uuid", p.getUniqueId().toString());
        qf.doUpdate("player_prefs", setpp, wherepp);
        TARDISMessage.send(p, "PREF_WAS_ON", "Mob farming");
    }

    private boolean checkRoomNextDoor(Block b) {
        if (b.getLocation().getBlockZ() < (resetz + 10) && !b.getRelative(BlockFace.EAST).getType().equals(Material.AIR)) {
            return true;
        } else {
            return !b.getRelative(BlockFace.SOUTH).getType().equals(Material.AIR);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
