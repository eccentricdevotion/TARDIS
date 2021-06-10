/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.rooms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.tardis.database.resultset.ResultSetFarming;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisTimeLordName;
import me.eccentric_nz.tardis.enumeration.Room;
import me.eccentric_nz.tardis.enumeration.UseClay;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

import java.util.*;

import static me.eccentric_nz.tardis.schematic.TARDISBannerSetter.setBanners;

/**
 * The tardis had a swimming pool. After the tardis' crash following the Doctor's tenth regeneration, the pool's water -
 * or perhaps the pool itself - fell into the library. After the tardis had fixed itself, the swimming pool was restored
 * but the Doctor did not know where it was.
 *
 * @author eccentric_nz
 */
public class TARDISRoomRunnable implements Runnable {

    private final TARDISPlugin plugin;
    private final Location location;
    private final JsonObject schematic;
    private final int tardisId;
    private final int progressLevel;
    private final int progressRow;
    private final int progressColumn;
    private final Material wallType, floorType;
    private final String room;
    private final Player player;
    private final List<Chunk> chunkList = new ArrayList<>();
    private final List<Block> iceBlocks = new ArrayList<>();
    private final List<Block> lampBlocks = new ArrayList<>();
    private final List<Block> caneBlocks = new ArrayList<>();
    private final List<Block> melonBlocks = new ArrayList<>();
    private final List<Block> potatoBlocks = new ArrayList<>();
    private final List<Block> carrotBlocks = new ArrayList<>();
    private final List<Block> pumpkinBlocks = new ArrayList<>();
    private final List<Block> wheatBlocks = new ArrayList<>();
    private final List<Block> farmlandBlocks = new ArrayList<>();
    private final List<Block> signBlocks = new ArrayList<>();
    private final List<Material> notThese = new ArrayList<>();
    private final List<BlockData> flora = new ArrayList<>();
    private final HashMap<Block, BlockData> cocoaBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> doorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> leverBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> torchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> redstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockFace> mushroomBlocks = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> bannerBlocks = new HashMap<>();
    private final BlockFace[] repeaterData = new BlockFace[6];
    private final HashMap<Integer, Integer> repeaterOrder = new HashMap<>();
    private final boolean wasResumed;
    private final List<String> postBlocks;
    private int mazeCount = 0;
    private int task, level, row, col, h, w, c, startX, startY, startZ, resetX, resetY, resetZ;
    private boolean running;
    private World world;
    private JsonArray arr;
    private Location aquaSpawn;

    public TARDISRoomRunnable(TARDISPlugin plugin, TARDISRoomData roomData, Player player) {
        this.plugin = plugin;
        this.player = player;
        location = roomData.getLocation();
        schematic = roomData.getSchematic();
        wallType = roomData.getMiddleType();
        floorType = roomData.getFloorType();
        room = roomData.getRoom();
        tardisId = roomData.getTardisId();
        progressLevel = roomData.getLevel();
        progressRow = roomData.getRow();
        progressColumn = roomData.getColumn();
        postBlocks = roomData.getPostBlocks();
        wasResumed = (roomData.getLevel() > 0 || roomData.getRow() > 0 || roomData.getColumn() > 0);
        running = false;
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
        notThese.add(Material.CARROTS);
        notThese.add(Material.COCOA);
        notThese.add(Material.DARK_OAK_DOOR);
        notThese.add(Material.JUNGLE_DOOR);
        notThese.add(Material.LEVER);
        notThese.add(Material.MELON_STEM);
        notThese.add(Material.OAK_DOOR);
        notThese.add(Material.PISTON_HEAD);
        notThese.add(Material.POTATOES);
        notThese.add(Material.PUMPKIN_STEM);
        notThese.add(Material.REDSTONE_TORCH);
        notThese.add(Material.SPRUCE_DOOR);
        notThese.add(Material.SUGAR_CANE);
        notThese.add(Material.TORCH);
        notThese.add(Material.WHEAT);
        flora.add(Material.BRAIN_CORAL.createBlockData());
        flora.add(Material.BUBBLE_CORAL.createBlockData());
        flora.add(Material.FIRE_CORAL.createBlockData());
        flora.add(Material.HORN_CORAL.createBlockData());
        flora.add(Material.KELP_PLANT.createBlockData());
        flora.add(Material.SEA_PICKLE.createBlockData());
        flora.add(Material.SEAGRASS.createBlockData());
        flora.add(Material.TALL_SEAGRASS.createBlockData());
        flora.add(Material.TUBE_CORAL.createBlockData());
    }

    /**
     * A runnable task that builds tardis rooms block by block.
     */
    @Override
    public void run() {
        try {
            // initialise
            if (!running) {
                level = progressLevel;
                row = progressRow;
                col = progressColumn;
                JsonObject dim = schematic.get("dimensions").getAsJsonObject();
                arr = schematic.get("input").getAsJsonArray();
                h = dim.get("height").getAsInt() - 1;
                w = dim.get("width").getAsInt() - 1;
                c = dim.get("length").getAsInt();
                startX = location.getBlockX() + progressRow;
                startY = location.getBlockY() + progressLevel;
                startZ = location.getBlockZ() + progressColumn;
                resetX = location.getBlockX();
                resetY = location.getBlockY();
                resetZ = location.getBlockZ();
                world = location.getWorld();
                if (wasResumed) {
                    // process post block list
                    for (String s : postBlocks) {
                        String[] split = s.split("~");
                        Location location = TARDISStaticLocationGetters.getLocationFromDB(split[0]);
                        if (location != null) {
                            Block postBlock = world.getBlockAt(location);
                            BlockData postData = plugin.getServer().createBlockData(split[1]);
                            switch (postData.getMaterial()) {
                                case ICE:
                                    iceBlocks.add(postBlock);
                                    break;
                                case REDSTONE_LAMP:
                                    lampBlocks.add(postBlock);
                                    break;
                                case TORCH:
                                    torchBlocks.put(postBlock, postData);
                                    break;
                                case REDSTONE_TORCH:
                                    redstoneTorchBlocks.put(postBlock, postData);
                                    break;
                                case COCOA:
                                    cocoaBlocks.put(postBlock, postData);
                                    break;
                                case SUGAR_CANE:
                                    caneBlocks.add(postBlock);
                                    break;
                                case MELON_STEM:
                                    melonBlocks.add(postBlock);
                                    break;
                                case POTATOES:
                                    potatoBlocks.add(postBlock);
                                    break;
                                case CARROTS:
                                    carrotBlocks.add(postBlock);
                                    break;
                                case PUMPKIN_STEM:
                                    pumpkinBlocks.add(postBlock);
                                    break;
                                case WHEAT:
                                    wheatBlocks.add(postBlock);
                                    break;
                                case FARMLAND:
                                    farmlandBlocks.add(postBlock);
                                    break;
                                case SPRUCE_SIGN:
                                    signBlocks.add(postBlock);
                                    break;
                                case OAK_DOOR:
                                    doorBlocks.put(postBlock, postData);
                                    break;
                                case LEVER:
                                    leverBlocks.put(postBlock, postData);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } else {
                    TARDISRoomData trd = plugin.getTrackerKeeper().getRoomTasks().get(task);
                    trd.setPostBlocks(new ArrayList<>());
                    plugin.getTrackerKeeper().getRoomTasks().put(task, trd);
                }
                plugin.getBuildKeeper().getRoomProgress().put(player.getUniqueId(), 0);
                running = true;
                String grammar = (TARDISConstants.VOWELS.contains(room.substring(0, 1))) ? "an " + room : "a " + room;
                if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                    grammar += " WELL";
                }
                if (player != null) {
                    TARDISMessage.send(player, "ROOM_START", grammar);
                }
            }
            if (level == h && row == w && col == (c - 1)) {
                // the entire schematic has been read :)
                if (iceBlocks.size() > 0) {
                    if (player != null) {
                        TARDISMessage.send(player, "ICE");
                    }
                    // set all the ice to water
                    iceBlocks.forEach((ice) -> ice.setBlockData(Material.WATER.createBlockData()));
                    iceBlocks.clear();
                }
                if (room.equals("CHEMISTRY") && signBlocks.size() > 0) {
                    boolean first = true;
                    for (Block b : signBlocks) {
                        Sign sign = (Sign) b.getState();
                        if (first) {
                            sign.setLine(1, "Chemistry");
                            sign.setLine(2, "Lab");
                            first = false;
                        } else {
                            sign.setLine(1, "Science");
                            sign.setLine(2, "is fun!");
                        }
                        sign.update();
                    }
                }
                if (room.equals("AQUARIUM")) {
                    if (aquaSpawn == null) {
                        ResultSetFarming rsf = new ResultSetFarming(plugin, tardisId);
                        if (rsf.resultSet()) {
                            // resuming room growing...
                            aquaSpawn = TARDISStaticLocationGetters.getSpawnLocationFromDB(rsf.getFarming().getAquarium());
                        }
                    }
                    // add some underwater flora
                    int plusX = aquaSpawn.getBlockX() + 2;
                    int minusX = aquaSpawn.getBlockX() - 2;
                    int plusZ = aquaSpawn.getBlockZ() + 2;
                    int minusZ = aquaSpawn.getBlockZ() - 2;
                    int y = aquaSpawn.getBlockY();
                    int minusY = aquaSpawn.getBlockY() - 1;
                    for (int ax = plusX; ax < plusX + 5; ax++) {
                        for (int az = plusZ; az < plusZ + 5; az++) {
                            if (world.getBlockAt(ax, minusY, az).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
                                BlockData f = flora.get(TARDISConstants.RANDOM.nextInt(flora.size()));
                                switch (f.getMaterial()) {
                                    case KELP -> {
                                        world.getBlockAt(ax, y + 1, az).setBlockData(f);
                                        world.getBlockAt(ax, y + 2, az).setBlockData(f);
                                    }
                                    case TALL_SEAGRASS -> {
                                        ((Bisected) f).setHalf(Bisected.Half.BOTTOM);
                                        world.getBlockAt(ax, y, az).setBlockData(f);
                                        ((Bisected) f).setHalf(Bisected.Half.TOP);
                                        world.getBlockAt(ax, y + 1, az).setBlockData(f);
                                    }
                                    case SEA_PICKLE -> {
                                        ((SeaPickle) f).setPickles(TARDISConstants.RANDOM.nextInt(4) + 1);
                                        world.getBlockAt(ax, y, az).setBlockData(f);
                                    }
                                    default -> world.getBlockAt(ax, y, az).setBlockData(f);
                                }
                            }
                        }
                    }
                    for (int bx = minusX; bx > minusX - 5; bx--) {
                        for (int bz = plusZ; bz < plusZ + 5; bz++) {
                            if (world.getBlockAt(bx, minusY, bz).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
                                BlockData f = flora.get(TARDISConstants.RANDOM.nextInt(flora.size()));
                                switch (f.getMaterial()) {
                                    case KELP -> {
                                        world.getBlockAt(bx, y + 1, bz).setBlockData(f);
                                        world.getBlockAt(bx, y + 2, bz).setBlockData(f);
                                    }
                                    case TALL_SEAGRASS -> {
                                        ((Bisected) f).setHalf(Bisected.Half.BOTTOM);
                                        world.getBlockAt(bx, y, bz).setBlockData(f);
                                        ((Bisected) f).setHalf(Bisected.Half.TOP);
                                        world.getBlockAt(bx, y + 1, bz).setBlockData(f);
                                    }
                                    case SEA_PICKLE -> {
                                        ((SeaPickle) f).setPickles(TARDISConstants.RANDOM.nextInt(4) + 1);
                                        world.getBlockAt(bx, y, bz).setBlockData(f);
                                    }
                                    default -> world.getBlockAt(bx, y, bz).setBlockData(f);
                                }
                            }
                        }
                    }
                    for (int cx = minusX; cx > minusX - 5; cx--) {
                        for (int cz = minusZ; cz > minusZ - 5; cz--) {
                            if (world.getBlockAt(cx, minusY, cz).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
                                BlockData f = flora.get(TARDISConstants.RANDOM.nextInt(flora.size()));
                                switch (f.getMaterial()) {
                                    case KELP -> {
                                        world.getBlockAt(cx, y + 1, cz).setBlockData(f);
                                        world.getBlockAt(cx, y + 2, cz).setBlockData(f);
                                    }
                                    case TALL_SEAGRASS -> {
                                        ((Bisected) f).setHalf(Bisected.Half.BOTTOM);
                                        world.getBlockAt(cx, y, cz).setBlockData(f);
                                        ((Bisected) f).setHalf(Bisected.Half.TOP);
                                        world.getBlockAt(cx, y + 1, cz).setBlockData(f);
                                    }
                                    case SEA_PICKLE -> {
                                        ((SeaPickle) f).setPickles(TARDISConstants.RANDOM.nextInt(4) + 1);
                                        world.getBlockAt(cx, y, cz).setBlockData(f);
                                    }
                                    default -> world.getBlockAt(cx, y, cz).setBlockData(f);
                                }
                            }
                        }
                    }
                    for (int dx = plusX; dx < plusX + 5; dx++) {
                        for (int dz = minusZ; dz > minusZ - 5; dz--) {
                            if (world.getBlockAt(dx, minusY, dz).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
                                BlockData f = flora.get(TARDISConstants.RANDOM.nextInt(flora.size()));
                                switch (f.getMaterial()) {
                                    case KELP -> {
                                        world.getBlockAt(dx, y + 1, dz).setBlockData(f);
                                        world.getBlockAt(dx, y + 2, dz).setBlockData(f);
                                    }
                                    case TALL_SEAGRASS -> {
                                        ((Bisected) f).setHalf(Bisected.Half.BOTTOM);
                                        world.getBlockAt(dx, y, dz).setBlockData(f);
                                        ((Bisected) f).setHalf(Bisected.Half.TOP);
                                        world.getBlockAt(dx, y + 1, dz).setBlockData(f);
                                    }
                                    case SEA_PICKLE -> {
                                        ((SeaPickle) f).setPickles(TARDISConstants.RANDOM.nextInt(4) + 1);
                                        world.getBlockAt(dx, y, dz).setBlockData(f);
                                    }
                                    default -> world.getBlockAt(dx, y, dz).setBlockData(f);
                                }
                            }
                        }
                    }
                }
                if (schematic.has("paintings")) {
                    // place paintings
                    JsonArray paintings = (JsonArray) schematic.get("paintings");
                    for (int i = 0; i < paintings.size(); i++) {
                        JsonObject painting = paintings.get(i).getAsJsonObject();
                        JsonObject rel = painting.get("rel_location").getAsJsonObject();
                        int px = rel.get("x").getAsInt();
                        int py = rel.get("y").getAsInt();
                        int pz = rel.get("z").getAsInt();
                        Art art = Art.valueOf(painting.get("art").getAsString());
                        BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
                        Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, resetX + px, resetY + py, resetZ + pz));
                        try {
                            Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                            ent.teleport(pl);
                            ent.setFacingDirection(facing, true);
                            ent.setArt(art, true);
                        } catch (IllegalArgumentException e) {
                            plugin.debug("Could not spawn painting! " + e.getMessage());
                        }
                    }
                }
                if (room.equals("BAKER") || room.equals("WOOD")) {
                    // set the repeaters
                    mushroomBlocks.forEach((key, value) -> {
                        BlockData repeater = Material.REPEATER.createBlockData();
                        Directional directional = (Directional) repeater;
                        directional.setFacing(value);
                        key.setBlockData(directional, true);
                    });
                    mushroomBlocks.clear();
                }
                if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
                    // plant the sugar cane
                    caneBlocks.forEach((cane) -> cane.setBlockData(Material.SUGAR_CANE.createBlockData()));
                    caneBlocks.clear();
                    // attach the cocoa
                    cocoaBlocks.forEach((key, value) -> key.setBlockData(value, true));
                    cocoaBlocks.clear();
                    // plant the melon
                    melonBlocks.forEach((melon) -> melon.setBlockData(Material.MELON_STEM.createBlockData()));
                    melonBlocks.clear();
                    // plant the pumpkin
                    pumpkinBlocks.forEach((pumpkin) -> pumpkin.setBlockData(Material.PUMPKIN_STEM.createBlockData()));
                    pumpkinBlocks.clear();
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // plant the wheat
                        wheatBlocks.forEach((wheat) -> wheat.setBlockData(Material.WHEAT.createBlockData()));
                        wheatBlocks.clear();
                        // plant the carrot
                        carrotBlocks.forEach((carrot) -> carrot.setBlockData(Material.CARROTS.createBlockData()));
                        carrotBlocks.clear();
                        // plant the potato
                        potatoBlocks.forEach((potato) -> potato.setBlockData(Material.POTATOES.createBlockData()));
                        potatoBlocks.clear();
                    }, 5L);
                }
                if (room.equals("VILLAGE") || room.equals("SHELL")) {
                    // put doors on
                    doorBlocks.forEach((key, value) -> key.setBlockData(value, true));
                    doorBlocks.clear();
                }
                // water farmland
                farmlandBlocks.forEach((fl) -> {
                    BlockData farmData = Material.FARMLAND.createBlockData();
                    Farmland farmland = (Farmland) farmData;
                    farmland.setMoisture(farmland.getMaximumMoisture());
                    fl.setBlockData(farmland);
                });
                // put levers on
                leverBlocks.forEach((key, value) -> key.setBlockData(value, true));
                leverBlocks.clear();
                // update lamp block states
                if (player != null) {
                    TARDISMessage.send(player, "ROOM_POWER");
                }
                lampBlocks.forEach((lamp) -> lamp.setBlockData(TARDISConstants.LAMP));
                lampBlocks.clear();
                // put torches on
                torchBlocks.forEach((key, value) -> key.setBlockData(value, true));
                torchBlocks.clear();
                // put redstone torches on
                redstoneTorchBlocks.forEach((key, value) -> key.setBlockData(value, true));
                torchBlocks.clear();
                // set banners
                setBanners(bannerBlocks);
                // remove the chunks, so they can unload as normal again
                if (chunkList.size() > 0) {
                    chunkList.forEach((ch) -> ch.setForceLoaded(false));
                }
                plugin.getTrackerKeeper().getRoomTasks().remove(task);
                // cancel the task
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                String rname = (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) ? room + " WELL" : room;
                if (player != null) {
                    TARDISMessage.send(player, "ROOM_FINISHED", rname);
                }
                plugin.getBuildKeeper().getRoomProgress().remove(player.getUniqueId());
            } else {
                TARDISRoomData rd = plugin.getTrackerKeeper().getRoomTasks().get(task);
                // place one block
                JsonObject v = arr.get(level).getAsJsonArray().get(row).getAsJsonArray().get(col).getAsJsonObject();
                BlockData data = plugin.getServer().createBlockData(v.get("data").getAsString());
                Material type = data.getMaterial();
                // determine 'use_clay' material
                UseClay use_clay;
                try {
                    use_clay = UseClay.valueOf(plugin.getConfig().getString("creation.use_clay"));
                } catch (IllegalArgumentException e) {
                    use_clay = UseClay.WOOL;
                }
                Material ow;
                Material lgw;
                Material gw;
                switch (use_clay) {
                    case TERRACOTTA -> {
                        ow = Material.ORANGE_TERRACOTTA;
                        lgw = Material.LIGHT_GRAY_TERRACOTTA;
                        gw = Material.GRAY_TERRACOTTA;
                    }
                    case CONCRETE -> {
                        ow = Material.ORANGE_CONCRETE;
                        lgw = Material.LIGHT_GRAY_CONCRETE;
                        gw = Material.GRAY_CONCRETE;
                    }
                    default -> {
                        ow = Material.ORANGE_WOOL;
                        lgw = Material.LIGHT_GRAY_WOOL;
                        gw = Material.GRAY_WOOL;
                    }
                }
                if (type.equals(Material.GRAY_WOOL)) {
                    data = gw.createBlockData();
                }
                if (type.equals(Material.ORANGE_WOOL)) {
                    if (wallType.equals(Material.ORANGE_WOOL) || ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && (wallType.equals(Material.LIME_WOOL) || wallType.equals(Material.PINK_WOOL)))) {
                        if (ow.equals(Material.ORANGE_WOOL)) {
                            data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(46));
                        } else {
                            data = ow.createBlockData();
                        }
                    } else {
                        data = wallType.createBlockData();
                    }
                }
                if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                    if (floorType.equals(Material.LIGHT_GRAY_WOOL) || ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && (floorType.equals(Material.LIME_WOOL) || floorType.equals(Material.PINK_WOOL)))) {
                        data = lgw.createBlockData();
                    } else {
                        data = floorType.createBlockData();
                    }
                }
                if (type.equals(Material.SPRUCE_SIGN) && room.equals("CHEMISTRY")) {
                    Block sign = world.getBlockAt(startX, startY, startZ);
                    signBlocks.add(sign);
                }
                if (type.equals(Material.BEEHIVE) && room.equals("APIARY")) {
                    HashMap<String, Object> seta = new HashMap<>();
                    seta.put("apiary", world.getName() + ":" + startX + ":" + (startY + 1) + ":" + startZ);
                    ResultSetFarming rsa = new ResultSetFarming(plugin, tardisId);
                    if (rsa.resultSet()) {
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("tardis_id", tardisId);
                        plugin.getQueryFactory().doUpdate("farming", seta, wherea);
                    } else {
                        seta.put("tardis_id", tardisId);
                        plugin.getQueryFactory().doInsert("farming", seta);
                    }
                }
                // set condenser
                if (type.equals(Material.CHEST) && room.equals("HARMONY")) {
                    plugin.getQueryFactory().insertControl(tardisId, 34, new Location(world, startX, startY, startZ).toString(), 1);
                }
                // set drop chest
                if (type.equals(Material.TRAPPED_CHEST) && room.equals("VAULT") && player != null && TARDISPermission.hasPermission(player, "tardis.vault")) {
                    // determine the min x, y, z coords
                    int mx = startX % 16;
                    if (mx < 0) {
                        mx += 16;
                    }
                    int mz = startZ % 16;
                    if (mz < 0) {
                        mz += 16;
                    }
                    int x = startX - mx;
                    int y = startY - (startY % 16);
                    int z = startZ - mz;
                    String pos = new Location(world, startX, startY, startZ).toString();
                    HashMap<String, Object> setv = new HashMap<>();
                    setv.put("tardis_id", tardisId);
                    setv.put("location", pos);
                    setv.put("x", x);
                    setv.put("y", y);
                    setv.put("z", z);
                    plugin.getQueryFactory().doInsert("vaults", setv);
                }
                // set farm
                if (type.equals(Material.SPAWNER) && room.equals("FARM")) {
                    // do they have a farm record?
                    HashMap<String, Object> setf = new HashMap<>();
                    setf.put("farm", world.getName() + ":" + startX + ":" + startY + ":" + startZ);
                    ResultSetFarming rsf = new ResultSetFarming(plugin, tardisId);
                    if (rsf.resultSet()) {
                        HashMap<String, Object> wheref = new HashMap<>();
                        wheref.put("tardis_id", tardisId);
                        plugin.getQueryFactory().doUpdate("farming", setf, wheref);
                    } else {
                        setf.put("tardis_id", tardisId);
                        plugin.getQueryFactory().doInsert("farming", setf);
                    }
                    // replace with floor material
                    data = (floorType.equals(Material.LIGHT_GRAY_WOOL)) ? lgw.createBlockData() : floorType.createBlockData();
                    // update player prefs - turn on mob farming
                    if (player != null) {
                        turnOnFarming(player);
                    }
                }
                // set lazarus
                if (type.equals(Material.OAK_PRESSURE_PLATE) && room.equals("LAZARUS")) {
                    String plate = (new Location(world, startX, startY, startZ)).toString();
                    plugin.getQueryFactory().insertControl(tardisId, 19, plate, 0);
                }
                // set stable
                if (type.equals(Material.SOUL_SAND) && (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("RENDERER") || room.equals("ZERO") || room.equals("HUTCH") || room.equals("IGLOO") || room.equals("STALL") || room.equals("BAMBOO") || room.equals("BIRDCAGE") || room.equals("MAZE"))) {
                    HashMap<String, Object> sets = new HashMap<>();
                    sets.put(room.toLowerCase(Locale.ENGLISH), world.getName() + ":" + startX + ":" + startY + ":" + startZ);
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("tardis_id", tardisId);
                    if (room.equals("RENDERER") || room.equals("ZERO")) {
                        plugin.getQueryFactory().doUpdate("tardis", sets, wheres);
                    } else if (room.equals("MAZE")) {
                        String loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startX, startY + 1, startZ);
                        plugin.getQueryFactory().insertControl(tardisId, 44, loc_str, 0);
                    } else {
                        ResultSetFarming rsf = new ResultSetFarming(plugin, tardisId);
                        if (rsf.resultSet()) {
                            // update
                            plugin.getQueryFactory().doUpdate("farming", sets, wheres);
                        } else {
                            sets.put("tardis_id", tardisId);
                            plugin.getQueryFactory().doInsert("farming", sets);
                        }
                    }
                    // replace with correct block
                    switch (Room.valueOf(room)) {
                        case VILLAGE -> data = Material.COBBLESTONE.createBlockData();
                        case HUTCH, STABLE, STALL, MAZE -> data = Material.GRASS_BLOCK.createBlockData();
                        case BAMBOO, BIRDCAGE -> data = Material.PODZOL.createBlockData();
                        case IGLOO -> data = Material.PACKED_ICE.createBlockData();
                        case ZERO -> data = Material.PINK_CARPET.createBlockData();
                        default -> {
                            data = TARDISConstants.BLACK;
                            // add WorldGuard region
                            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                                String name = "";
                                if (player == null) {
                                    ResultSetTardisTimeLordName rsn = new ResultSetTardisTimeLordName(plugin);
                                    if (rsn.fromID(tardisId)) {
                                        name = rsn.getOwner();
                                    }
                                } else {
                                    name = player.getName();
                                }
                                if (!name.isEmpty()) {
                                    Location one = new Location(world, startX - 6, startY, startZ - 6);
                                    Location two = new Location(world, startX + 6, startY + 8, startZ + 6);
                                    plugin.getWorldGuardUtils().addRendererProtection(name, one, two);
                                }
                            }
                        }
                    }
                    if (!room.equals("ZERO") && !room.equals("RENDERER") && !room.equals("MAZE")) {
                        // update player prefs - turn on mob farming
                        if (player != null) {
                            turnOnFarming(player);
                        }
                    }
                }
                if ((type.equals(Material.SOUL_SAND) || type.equals(Material.CARVED_PUMPKIN)) && room.equals("SMELTER")) {
                    String pos = new Location(world, startX, startY, startZ).toString();
                    HashMap<String, Object> setsm = new HashMap<>();
                    setsm.put("tardis_id", tardisId);
                    setsm.put("location", pos);
                    setsm.put("chest_type", type.equals(Material.CARVED_PUMPKIN) ? "SMELT" : "FUEL");
                    plugin.getQueryFactory().doInsert("vaults", setsm);
                    data = Material.CHEST.createBlockData();
                }
                if (type.equals(Material.DEAD_HORN_CORAL_BLOCK) && room.equals("AQUARIUM")) {
                    HashMap<String, Object> setaqua = new HashMap<>();
                    setaqua.put("aquarium", world.getName() + ":" + startX + ":" + startY + ":" + startZ);
                    ResultSetFarming rsf = new ResultSetFarming(plugin, tardisId);
                    if (rsf.resultSet()) {
                        HashMap<String, Object> wheres = new HashMap<>();
                        wheres.put("tardis_id", tardisId);
                        // update
                        plugin.getQueryFactory().doUpdate("farming", setaqua, wheres);
                    } else {
                        setaqua.put("tardis_id", tardisId);
                        plugin.getQueryFactory().doInsert("farming", setaqua);
                    }
                    data = (floorType.equals(Material.LIGHT_GRAY_WOOL)) ? lgw.createBlockData() : floorType.createBlockData();
                    if (player != null) {
                        turnOnFarming(player);
                    }
                    aquaSpawn = new Location(world, startX, startY + 1, startZ);
                }
                // remember shell room button
                if (type.equals(Material.STONE_BUTTON) && room.equals("SHELL")) {
                    String loc_str = new Location(world, startX, startY, startZ).toString();
                    plugin.getQueryFactory().insertControl(tardisId, 25, loc_str, 0);
                }
                // remember village doors
                if (type.equals(Material.OAK_DOOR) && (room.equals("VILLAGE") || room.equals("SHELL"))) {
                    Block door = world.getBlockAt(startX, startY, startZ);
                    doorBlocks.put(door, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + data.getAsString());
                }
                // remember torches
                if (type.equals(Material.TORCH)) {
                    Block torch = world.getBlockAt(startX, startY, startZ);
                    torchBlocks.put(torch, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + data.getAsString());
                }
                // remember torches
                if (type.equals(Material.LEVER)) {
                    Block lever = world.getBlockAt(startX, startY, startZ);
                    leverBlocks.put(lever, data);
                }
                // remember redstone torches
                if (type.equals(Material.REDSTONE_TORCH)) {
                    Block torch = world.getBlockAt(startX, startY, startZ);
                    redstoneTorchBlocks.put(torch, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + data.getAsString());
                }
                // remember banners
                if (TARDISStaticUtils.isBanner(type)) {
                    Block banner = world.getBlockAt(startX, startY, startZ);
                    JsonObject state = v.has("banner") ? v.get("banner").getAsJsonObject() : null;
                    if (state != null) {
                        TARDISBannerData tbd = new TARDISBannerData(data, state);
                        bannerBlocks.put(banner, tbd);
                    }
                }
                if (type.equals(Material.FARMLAND)) {
                    Block farmland = world.getBlockAt(startX, startY, startZ);
                    farmlandBlocks.add(farmland);
                    rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.FARMLAND.createBlockData().getAsString());
                }
                if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
                    // remember sugar cane
                    if (type.equals(Material.SUGAR_CANE)) {
                        Block cane = world.getBlockAt(startX, startY, startZ);
                        caneBlocks.add(cane);
                        rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.SUGAR_CANE.createBlockData().getAsString());
                    }
                    // remember cocoa
                    if (type.equals(Material.COCOA)) {
                        Block cocoa = world.getBlockAt(startX, startY, startZ);
                        cocoaBlocks.put(cocoa, data);
                        rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.COCOA.createBlockData().getAsString());
                    }
                    // remember wheat
                    if (type.equals(Material.WHEAT)) {
                        Block crops = world.getBlockAt(startX, startY, startZ);
                        wheatBlocks.add(crops);
                        rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.WHEAT.createBlockData().getAsString());
                    }
                    // remember melon
                    if (type.equals(Material.MELON_STEM)) {
                        Block melon = world.getBlockAt(startX, startY, startZ);
                        melonBlocks.add(melon);
                        rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.MELON_STEM.createBlockData().getAsString());
                    }
                    // remember pumpkin
                    if (type.equals(Material.PUMPKIN_STEM)) {
                        Block pumpkin = world.getBlockAt(startX, startY, startZ);
                        pumpkinBlocks.add(pumpkin);
                        rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.PUMPKIN_STEM.createBlockData().getAsString());
                    }
                    // remember carrots
                    if (type.equals(Material.CARROTS)) {
                        Block carrot = world.getBlockAt(startX, startY, startZ);
                        carrotBlocks.add(carrot);
                        rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.CARROTS.createBlockData().getAsString());
                    }
                    // remember potatoes
                    if (type.equals(Material.POTATOES)) {
                        Block potato = world.getBlockAt(startX, startY, startZ);
                        potatoBlocks.add(potato);
                        rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.POTATOES.createBlockData().getAsString());
                    }
                    if (level == 4 && room.equals("GREENHOUSE")) {
                        // set all the ice to water
                        iceBlocks.forEach((ice) -> ice.setBlockData(Material.WATER.createBlockData()));
                        iceBlocks.clear();
                    }
                }
                if (room.equals("RAIL") && type.equals(Material.OAK_FENCE)) {
                    // remember fence location so we can teleport the storage minecart
                    String loc = world.getName() + ":" + startX + ":" + startY + ":" + startZ;
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("rail", loc);
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", tardisId);
                    plugin.getQueryFactory().doUpdate("tardis", set, where);
                }
                // always replace bedrock (the door space in ars rooms)
                if ((type.equals(Material.BEDROCK) && !room.equals("SHELL")) || (type.equals(Material.SOUL_SAND) && room.equals("SHELL"))) {
                    if (checkRoomNextDoor(world.getBlockAt(startX, startY, startZ))) {
                        data = TARDISConstants.AIR;
                    } else {
                        BlockData wall = (ow.equals(Material.ORANGE_WOOL)) ? plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(46)) : ow.createBlockData();
                        data = (wallType.equals(Material.ORANGE_WOOL)) ? wall : wallType.createBlockData();
                    }
                }
                // always clear the door blocks on the north and west sides of adjacent spaces
                if (type.equals(Material.STICKY_PISTON)) {
                    // only the bottom pistons
                    if (startY == (resetY + 2)) {
                        Block bottomdoorblock = null;
                        // north
                        if (startX == (resetX + 8) && startZ == resetZ) {
                            bottomdoorblock = world.getBlockAt(startX, (startY + 2), (startZ - 1));
                        }
                        // west
                        if (startX == resetX && startZ == (resetZ + 8)) {
                            bottomdoorblock = world.getBlockAt((startX - 1), (startY + 2), startZ);
                        }
                        if (bottomdoorblock != null) {
                            Block topdoorblock = bottomdoorblock.getRelative(BlockFace.UP);
                            bottomdoorblock.setBlockData(TARDISConstants.AIR);
                            topdoorblock.setBlockData(TARDISConstants.AIR);
                        }
                    }
                }
                // always remove sponge
                if (type.equals(Material.SPONGE)) {
                    data = TARDISConstants.AIR;
                } else {
                    Block existing = world.getBlockAt(startX, startY, startZ);
                    if (!existing.getType().isAir() && !(room.equals("BAMBOO") && existing.getType().equals(Material.BAMBOO))) {
                        if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                            switch (type) {
                                case AIR:
                                case GRAY_WOOL:
                                case LIGHT_GRAY_WOOL:
                                case ORANGE_WOOL:
                                case STONE_BRICKS:
                                    break;
                                default:
                                    data = existing.getBlockData();
                                    break;
                            }
                        } else {
                            data = existing.getBlockData();
                        }
                    }
                }
                Chunk thisChunk = world.getChunkAt(world.getBlockAt(startX, startY, startZ));
                thisChunk.setForceLoaded(true);
                chunkList.add(thisChunk);
                if (!notThese.contains(type) && !type.equals(Material.MUSHROOM_STEM)) {
                    if (type.equals(Material.WATER)) {
                        TARDISBlockSetters.setBlock(world, startX, startY, startZ, Material.ICE.createBlockData());
                    } else {
                        TARDISBlockSetters.setBlock(world, startX, startY, startZ, data);
                    }
                }
                // remember ice blocks
                if ((type.equals(Material.WATER) || type.equals(Material.ICE)) && !room.equals("IGLOO")) {
                    Block icy = world.getBlockAt(startX, startY, startZ);
                    iceBlocks.add(icy);
                    rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + Material.ICE.createBlockData().getAsString());
                }
                // remember lamp blocks
                if (type.equals(Material.REDSTONE_LAMP)) {
                    Block lamp = world.getBlockAt(startX, startY, startZ);
                    lampBlocks.add(lamp);
                    if (rd == null) {
                        plugin.debug("Room Data NULL");
                        plugin.getServer().getScheduler().cancelTask(task);
                        task = 0;
                        return;
                    }
                    if (rd.getPostBlocks() == null) {
                        plugin.debug("Post Blocks NULL");
                        plugin.getServer().getScheduler().cancelTask(task);
                        task = 0;
                        return;
                    }
                    rd.getPostBlocks().add(world.getName() + ":" + startX + ":" + startY + ":" + startZ + "~" + TARDISConstants.LAMP.getAsString());
                }
                if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                    String loc;
                    if (type.equals(Material.PINK_WOOL)) {
                        // pink wool - gravity well down
                        loc = new Location(world, startX, startY, startZ).toString();
                        HashMap<String, Object> setd = new HashMap<>();
                        setd.put("tardis_id", tardisId);
                        setd.put("location", loc);
                        setd.put("direction", 0);
                        setd.put("distance", 0);
                        setd.put("velocity", 0);
                        plugin.getQueryFactory().doInsert("gravity_well", setd);
                        plugin.getGeneralKeeper().getGravityDownList().add(loc);
                    }
                    if (type.equals(Material.LIME_WOOL)) {
                        // light green wool - gravity well up
                        loc = new Location(world, startX, startY, startZ).toString();
                        HashMap<String, Object> setu = new HashMap<>();
                        setu.put("tardis_id", tardisId);
                        setu.put("location", loc);
                        setu.put("direction", 1);
                        setu.put("distance", 16);
                        setu.put("velocity", 0.5);
                        plugin.getQueryFactory().doInsert("gravity_well", setu);
                        Double[] values = {1D, 16D, 0.5D};
                        plugin.getGeneralKeeper().getGravityUpList().put(loc, values);
                    }
                }
                if (room.equals("MAZE") && type.equals(Material.STONE_PRESSURE_PLATE)) {
                    String loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startX, startY, startZ);
                    int maze = 40 + mazeCount;
                    plugin.getQueryFactory().insertControl(tardisId, maze, loc_str, 0);
                    mazeCount++;
                }
                if (room.equals("BAKER") || room.equals("WOOD")) {
                    // remember the controls
                    int secondary = (room.equals("BAKER")) ? 1 : 2;
                    int r = 2;
                    int control_type;
                    String loc_str;
                    List<Material> controls = Arrays.asList(Material.CAKE, Material.STONE_BUTTON, Material.MUSHROOM_STEM, Material.OAK_BUTTON);
                    if (controls.contains(type)) {
                        switch (type) {
                            case STONE_BUTTON -> { // stone button - TARDISConstants.RANDOM
                                control_type = 1;
                                loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startX, startY, startZ);
                            }
                            case MUSHROOM_STEM -> { // repeater
                                control_type = repeaterOrder.get(r);
                                loc_str = world.getName() + ":" + startX + ":" + startY + ":" + startZ;
                                Block rb = world.getBlockAt(startX, startY, startZ);
                                mushroomBlocks.put(rb, repeaterData[r]);
                                r++;
                            }
                            case OAK_BUTTON -> { // oak button - artron
                                control_type = 6;
                                loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startX, startY, startZ);
                            }
                            default -> { // cake - handbrake
                                control_type = 0;
                                loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startX, startY, startZ);
                            }
                        }
                        plugin.getQueryFactory().insertControl(tardisId, control_type, loc_str, secondary);
                    }
                }
                if (room.equals("ZERO")) {
                    // remember the button
                    String loc_str;
                    if (type.equals(Material.OAK_BUTTON)) {
                        loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startX, startY, startZ);
                        plugin.getQueryFactory().insertControl(tardisId, 17, loc_str, 0);
                    }
                }
                startZ += 1;
                col++;
                if (col == c && row < w) {
                    col = 0;
                    startZ = resetZ;
                    startX += 1;
                    row++;
                }
                if (col == c && row == w && level < h) {
                    col = 0;
                    row = 0;
                    startX = resetX;
                    startZ = resetZ;
                    startY += 1;
                    int percent = TARDISNumberParsers.roundUp(level * 100, h);
                    if (percent > 0 && player != null) {
                        plugin.getBuildKeeper().getRoomProgress().put(player.getUniqueId(), percent);
                        TARDISMessage.send(player, "ROOM_PERCENT", room, String.format("%d", percent));
                    }
                    level++;
                }
                rd.setRow(row);
                rd.setColumn(col);
                rd.setLevel(level);
                plugin.getTrackerKeeper().getRoomTasks().put(task, rd);
            }
        } catch (Exception e) {
            // abort room growing task
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            String message = ChatColor.RED + "Resumption of room growing was aborted due to: " + ChatColor.RESET + e.getMessage();
            if (player != null) {
                TARDISMessage.message(player, message);
            }
            plugin.debug(message);
        }
    }

    private void turnOnFarming(Player p) {
        // update player prefs - turn on mob farming
        HashMap<String, Object> setpp = new HashMap<>();
        setpp.put("farm_on", 1);
        HashMap<String, Object> wherepp = new HashMap<>();
        wherepp.put("uuid", p.getUniqueId().toString());
        plugin.getQueryFactory().doUpdate("player_prefs", setpp, wherepp);
        TARDISMessage.send(p, "PREF_WAS_ON", "Mob farming");
    }

    private boolean checkRoomNextDoor(Block b) {
        if (b.getLocation().getBlockZ() < (resetZ + 10) && !b.getRelative(BlockFace.EAST).getType().isAir()) {
            return true;
        } else {
            return !b.getRelative(BlockFace.SOUTH).getType().isAir();
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
