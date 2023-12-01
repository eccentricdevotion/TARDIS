/*
 * Copyright (C) 2023 eccentric_nz
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarming;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisTimeLordName;
import me.eccentric_nz.TARDIS.enumeration.Room;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.schematic.setters.TARDISBannerSetter;
import me.eccentric_nz.TARDIS.schematic.setters.TARDISItemDisplaySetter;
import me.eccentric_nz.TARDIS.schematic.setters.TARDISItemFrameSetter;
import me.eccentric_nz.TARDIS.schematic.setters.TARDISSignSetter;
import me.eccentric_nz.TARDIS.utility.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

import java.util.*;

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
    private final JsonObject obj;
    private final int tardis_id;
    private final int progressLevel;
    private final int progressRow;
    private final int progressColumn;
    private final Material wall_type, floor_type;
    private final String room;
    private final Player player;
    private final UUID uuid;
    private final List<Chunk> chunkList = new ArrayList<>();
    private final List<Block> caneblocks = new ArrayList<>();
    private final List<Block> carrotblocks = new ArrayList<>();
    private final List<Block> farmlandblocks = new ArrayList<>();
    private final List<Block> iceblocks = new ArrayList<>();
    private final List<Block> lampblocks = new ArrayList<>();
    private final List<Block> melonblocks = new ArrayList<>();
    private final List<Block> potatoblocks = new ArrayList<>();
    private final List<Block> pumpkinblocks = new ArrayList<>();
    private final List<Block> wheatblocks = new ArrayList<>();
    private final List<Material> notThese = new ArrayList<>();
    private final List<BlockData> flora = new ArrayList<>();
    private final HashMap<Block, BlockData> cocoablocks = new HashMap<>();
    private final HashMap<Block, BlockData> doorblocks = new HashMap<>();
    private final HashMap<Block, BlockData> leverblocks = new HashMap<>();
    private final HashMap<Block, BlockData> propagules = new HashMap<>();
    private final HashMap<Block, BlockData> redstoneTorchblocks = new HashMap<>();
    private final HashMap<Block, BlockData> seagrass = new HashMap<>();
    private final HashMap<Block, BlockData> signblocks = new HashMap<>();
    private final HashMap<Block, BlockData> torchblocks = new HashMap<>();
    private final HashMap<Block, BlockData> trapdoorblocks = new HashMap<>();
    private final HashMap<Block, BlockFace> mushroomblocks = new HashMap<>();
    private final HashMap<Block, JsonObject> postSignBlocks = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> bannerblocks = new HashMap<>();
    private final BlockFace[] repeaterData = new BlockFace[6];
    private final HashMap<Integer, Integer> repeaterOrder = new HashMap<>();
    private final boolean wasResumed;
    private final List<String> postBlocks;
    private final boolean isLastTask;
    private int maze_count = 0;
    private int task, level, row, col, h, w, c, startx, starty, startz, resetx, resety, resetz;
    private boolean running;
    private World world;
    private JsonArray arr;
    private Location aqua_spawn;

    public TARDISRoomRunnable(TARDIS plugin, TARDISRoomData roomData, UUID uuid, boolean isLastTask) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.isLastTask = isLastTask;
        player = plugin.getServer().getPlayer(uuid);
        l = roomData.getLocation();
        obj = roomData.getSchematic();
        wall_type = roomData.getMiddleType();
        floor_type = roomData.getFloorType();
        room = roomData.getRoom();
        tardis_id = roomData.getTardis_id();
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
        notThese.add(Material.CARROTS);
        notThese.add(Material.COCOA);
        notThese.add(Material.IRON_TRAPDOOR);
        notThese.add(Material.LEVER);
        notThese.add(Material.MANGROVE_PROPAGULE);
        notThese.add(Material.MELON_STEM);
        notThese.add(Material.OAK_DOOR);
        notThese.add(Material.PISTON_HEAD);
        notThese.add(Material.POTATOES);
        notThese.add(Material.PUMPKIN_STEM);
        notThese.add(Material.REDSTONE_TORCH);
        notThese.add(Material.SEAGRASS);
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
     * A runnable task that builds TARDIS rooms block by block.
     */
    @Override
    public void run() {
        try {
            // initialise
            if (!running) {
                level = progressLevel;
                row = progressRow;
                col = progressColumn;
                JsonObject dim = obj.get("dimensions").getAsJsonObject();
                arr = obj.get("input").getAsJsonArray();
                h = dim.get("height").getAsInt() - 1;
                w = dim.get("width").getAsInt() - 1;
                c = dim.get("length").getAsInt();
                startx = l.getBlockX() + progressRow;
                starty = l.getBlockY() + progressLevel;
                startz = l.getBlockZ() + progressColumn;
                resetx = l.getBlockX();
                resety = l.getBlockY();
                resetz = l.getBlockZ();
                world = l.getWorld();
                if (wasResumed) {
                    // process post block list
                    for (String s : postBlocks) {
                        String[] split = s.split("~");
                        Location location = TARDISStaticLocationGetters.getLocationFromDB(split[0]);
                        if (location != null) {
                            Block postBlock = world.getBlockAt(location);
                            BlockData postData = plugin.getServer().createBlockData(split[1]);
                            switch (postData.getMaterial()) {
                                case ICE -> iceblocks.add(postBlock);
                                case REDSTONE_LAMP -> lampblocks.add(postBlock);
                                case TORCH -> torchblocks.put(postBlock, postData);
                                case REDSTONE_TORCH -> redstoneTorchblocks.put(postBlock, postData);
                                case COCOA -> cocoablocks.put(postBlock, postData);
                                case SUGAR_CANE -> caneblocks.add(postBlock);
                                case MANGROVE_PROPAGULE -> propagules.put(postBlock, postData);
                                case SEAGRASS -> seagrass.put(postBlock, postData);
                                case MELON_STEM -> melonblocks.add(postBlock);
                                case POTATOES -> potatoblocks.add(postBlock);
                                case CARROTS -> carrotblocks.add(postBlock);
                                case PUMPKIN_STEM -> pumpkinblocks.add(postBlock);
                                case WHEAT -> wheatblocks.add(postBlock);
                                case FARMLAND -> farmlandblocks.add(postBlock);
                                case OAK_DOOR -> doorblocks.put(postBlock, postData);
                                case LEVER -> leverblocks.put(postBlock, postData);
                                case IRON_TRAPDOOR -> trapdoorblocks.put(postBlock, postData);
                                default -> {
                                    if (Tag.ALL_SIGNS.isTagged(postData.getMaterial())) {
                                       signblocks.put(postBlock, postData);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    TARDISRoomData trd = plugin.getTrackerKeeper().getRoomTasks().get(task);
                    trd.setPostBlocks(new ArrayList<>());
                    plugin.getTrackerKeeper().getRoomTasks().put(task, trd);
                }
                plugin.getBuildKeeper().getRoomProgress().put(uuid, 0);
                running = true;
                String grammar = (TARDISConstants.VOWELS.contains(room.substring(0, 1))) ? "an " + room : "a " + room;
                if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                    grammar += " WELL";
                }
                if (player != null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_START", grammar);
                }
            }
            if (level == h && row == w && col == (c - 1)) {
                // the entire schematic has been read :)
                if (!iceblocks.isEmpty()) {
                    if (player != null) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ICE");
                    }
                    // set all the ice to water
                    iceblocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                    iceblocks.clear();
                }
                if (!postSignBlocks.isEmpty()) {
                    TARDISSignSetter.setSigns(postSignBlocks, plugin, 0);
                }
                if (!propagules.isEmpty()) {
                    for (Map.Entry<Block, BlockData> prop : propagules.entrySet()) {
                        prop.getKey().setBlockData(prop.getValue());
                    }
                }
                if (!signblocks.isEmpty()) {
                    for (Map.Entry<Block, BlockData> sign : signblocks.entrySet()) {
                        sign.getKey().setBlockData(sign.getValue());
                    }
                }
                if (!trapdoorblocks.isEmpty()) {
                    for (Map.Entry<Block, BlockData> trap : trapdoorblocks.entrySet()) {
                        trap.getKey().setBlockData(trap.getValue());
                    }
                }
                if (!seagrass.isEmpty()) {
                    for (Map.Entry<Block, BlockData> grass : seagrass.entrySet()) {
                        grass.getKey().setBlockData(grass.getValue());
                    }
                }
                if (room.equals("AQUARIUM")) {
                    if (aqua_spawn == null) {
                        ResultSetFarming rsf = new ResultSetFarming(plugin, tardis_id);
                        if (rsf.resultSet()) {
                            // resuming room growing...
                            aqua_spawn = TARDISStaticLocationGetters.getSpawnLocationFromDB(rsf.getFarming().getAquarium());
                        }
                    }
                    // add some underwater flora
                    int plusx = aqua_spawn.getBlockX() + 2;
                    int minusx = aqua_spawn.getBlockX() - 2;
                    int plusz = aqua_spawn.getBlockZ() + 2;
                    int minusz = aqua_spawn.getBlockZ() - 2;
                    int y = aqua_spawn.getBlockY();
                    int minusy = aqua_spawn.getBlockY() - 1;
                    for (int ax = plusx; ax < plusx + 5; ax++) {
                        for (int az = plusz; az < plusz + 5; az++) {
                            if (world.getBlockAt(ax, minusy, az).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
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
                    for (int bx = minusx; bx > minusx - 5; bx--) {
                        for (int bz = plusz; bz < plusz + 5; bz++) {
                            if (world.getBlockAt(bx, minusy, bz).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
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
                    for (int cx = minusx; cx > minusx - 5; cx--) {
                        for (int cz = minusz; cz > minusz - 5; cz--) {
                            if (world.getBlockAt(cx, minusy, cz).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
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
                    for (int dx = plusx; dx < plusx + 5; dx++) {
                        for (int dz = minusz; dz > minusz - 5; dz--) {
                            if (world.getBlockAt(dx, minusy, dz).getType().equals(Material.SAND) && TARDISConstants.RANDOM.nextInt(100) < 66) {
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
                if (obj.has("paintings")) {
                    // place paintings
                    JsonArray paintings = (JsonArray) obj.get("paintings");
                    for (int i = 0; i < paintings.size(); i++) {
                        JsonObject painting = paintings.get(i).getAsJsonObject();
                        JsonObject rel = painting.get("rel_location").getAsJsonObject();
                        int px = rel.get("x").getAsInt();
                        int py = rel.get("y").getAsInt();
                        int pz = rel.get("z").getAsInt();
                        Art art = Art.valueOf(painting.get("art").getAsString());
                        BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
                        Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, resetx + px, resety + py, resetz + pz));
                        try {
                            Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                            ent.teleport(pl);
                            ent.setFacingDirection(facing, true);
                            ent.setArt(art, true);
                        } catch (IllegalArgumentException e) {
                            plugin.debug("Invalid painting location!" + pl);
                        }
                    }
                }
                Location start = new Location(world, resetx, resety, resetz);
                if (obj.has("item_frames")) {
                    JsonArray frames = obj.get("item_frames").getAsJsonArray();
                    for (int i = 0; i < frames.size(); i++) {
                        TARDISItemFrameSetter.curate(frames.get(i).getAsJsonObject(), start, tardis_id);
                    }
                }
                if (obj.has("item_displays")) {
                    JsonArray displays = obj.get("item_displays").getAsJsonArray();
                    for (int i = 0; i < displays.size(); i++) {
                        TARDISItemDisplaySetter.fakeBlock(displays.get(i).getAsJsonObject(), start, tardis_id);
                    }
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
                    caneblocks.forEach((cane) -> cane.setBlockData(Material.SUGAR_CANE.createBlockData()));
                    caneblocks.clear();
                    // attach the cocoa
                    cocoablocks.forEach((key, value) -> key.setBlockData(value, true));
                    cocoablocks.clear();
                    // plant the melon
                    melonblocks.forEach((melon) -> melon.setBlockData(Material.MELON_STEM.createBlockData()));
                    melonblocks.clear();
                    // plant the pumpkin
                    pumpkinblocks.forEach((pumpkin) -> pumpkin.setBlockData(Material.PUMPKIN_STEM.createBlockData()));
                    pumpkinblocks.clear();
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // plant the wheat
                        wheatblocks.forEach((wheat) -> wheat.setBlockData(Material.WHEAT.createBlockData()));
                        wheatblocks.clear();
                        // plant the carrot
                        carrotblocks.forEach((carrot) -> carrot.setBlockData(Material.CARROTS.createBlockData()));
                        carrotblocks.clear();
                        // plant the potato
                        potatoblocks.forEach((potato) -> potato.setBlockData(Material.POTATOES.createBlockData()));
                        potatoblocks.clear();
                    }, 5L);
                }
                if (room.equals("VILLAGE") || room.equals("SHELL")) {
                    // put doors on
                    doorblocks.forEach((key, value) -> key.setBlockData(value, true));
                    doorblocks.clear();
                }
                // water farmland
                farmlandblocks.forEach((fl) -> {
                    BlockData farmData = Material.FARMLAND.createBlockData();
                    Farmland farmland = (Farmland) farmData;
                    farmland.setMoisture(farmland.getMaximumMoisture());
                    fl.setBlockData(farmland);
                });
                // put levers on
                leverblocks.forEach((key, value) -> key.setBlockData(value, true));
                leverblocks.clear();
                // update lamp block states
                if (player != null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_POWER");
                }
                lampblocks.forEach((lamp) -> lamp.setBlockData(TARDISConstants.LAMP));
                lampblocks.clear();
                // put torches on
                torchblocks.forEach((key, value) -> key.setBlockData(value, true));
                torchblocks.clear();
                // put redstone torches on
                redstoneTorchblocks.forEach((key, value) -> key.setBlockData(value, true));
                torchblocks.clear();
                // set banners
                TARDISBannerSetter.setBanners(bannerblocks);
                // remove the chunks, so they can unload as normal again
                if (!chunkList.isEmpty()) {
                    chunkList.forEach((ch) -> ch.setForceLoaded(false));
                }
                plugin.getTrackerKeeper().getRoomTasks().remove(task);
                // cancel the task
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                String rname = (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) ? room + " WELL" : room;
                if (player != null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_FINISHED", rname);
                }
                plugin.getBuildKeeper().getRoomProgress().remove(uuid);
                plugin.getTrackerKeeper().getIsGrowingRooms().remove(tardis_id);
            } else {
                TARDISRoomData rd = plugin.getTrackerKeeper().getRoomTasks().get(task);
                // place one block
                JsonObject v = arr.get(level).getAsJsonArray().get(row).getAsJsonArray().get(col).getAsJsonObject();
                String jData = v.get("data").getAsString();
                // check for pre-1.17 levelled cauldrons
                if (jData.contains("minecraft:cauldron[level=")) {
                    jData = jData.replace("cauldron[level=0]", "cauldron").replace("cauldron[level=3]", "water_cauldron[level=3]");
                }
                BlockData data = plugin.getServer().createBlockData(jData);
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
                    if (wall_type.equals(Material.ORANGE_WOOL) || ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && (wall_type.equals(Material.LIME_WOOL) || wall_type.equals(Material.PINK_WOOL)))) {
                        if (ow.equals(Material.ORANGE_WOOL)) {
                            if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                                data = TARDISConstants.BARRIER;
                                TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, startx, starty, startz);
                            }
                        } else {
                            data = ow.createBlockData();
                        }
                    } else {
                        data = wall_type.createBlockData();
                    }
                }
                if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                    if (floor_type.equals(Material.LIGHT_GRAY_WOOL) || ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && (floor_type.equals(Material.LIME_WOOL) || floor_type.equals(Material.PINK_WOOL)))) {
                        data = lgw.createBlockData();
                    } else {
                        data = floor_type.createBlockData();
                    }
                }
                if (Tag.ALL_SIGNS.isTagged(type)) {
                    Block sign = world.getBlockAt(startx, starty, startz);
                    postSignBlocks.put(sign, v);
                    signblocks.put(sign, data);
                }
                if (type.equals(Material.BEEHIVE) && room.equals("APIARY")) {
                    HashMap<String, Object> seta = new HashMap<>();
                    seta.put("apiary", world.getName() + ":" + startx + ":" + (starty + 1) + ":" + startz);
                    ResultSetFarming rsa = new ResultSetFarming(plugin, tardis_id);
                    if (rsa.resultSet()) {
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("tardis_id", tardis_id);
                        plugin.getQueryFactory().doUpdate("farming", seta, wherea);
                    } else {
                        seta.put("tardis_id", tardis_id);
                        plugin.getQueryFactory().doInsert("farming", seta);
                    }
                }
                // set condenser
                if (type.equals(Material.CHEST) && room.equals("HARMONY")) {
                    plugin.getQueryFactory().insertControl(tardis_id, 34, new Location(world, startx, starty, startz).toString(), 1);
                }
                // set drop chest
                if (type.equals(Material.TRAPPED_CHEST) && room.equals("VAULT") && player != null && TARDISPermission.hasPermission(player, "tardis.vault")) {
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
                    plugin.getQueryFactory().doInsert("vaults", setv);
                }
                // set farm
                if (type.equals(Material.SPAWNER) && room.equals("FARM")) {
                    // do they have a farm record?
                    HashMap<String, Object> setf = new HashMap<>();
                    setf.put("farm", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                    ResultSetFarming rsf = new ResultSetFarming(plugin, tardis_id);
                    if (rsf.resultSet()) {
                        HashMap<String, Object> wheref = new HashMap<>();
                        wheref.put("tardis_id", tardis_id);
                        plugin.getQueryFactory().doUpdate("farming", setf, wheref);
                    } else {
                        setf.put("tardis_id", tardis_id);
                        plugin.getQueryFactory().doInsert("farming", setf);
                    }
                    // replace with floor material
                    data = (floor_type.equals(Material.LIGHT_GRAY_WOOL)) ? lgw.createBlockData() : floor_type.createBlockData();
                    // update player prefs - turn on mob farming
                    if (player != null) {
                        turnOnFarming(player);
                    }
                }
                // set lazarus
                if (type.equals(Material.OAK_PRESSURE_PLATE) && room.equals("LAZARUS")) {
                    String plate = (new Location(world, startx, starty, startz)).toString();
                    plugin.getQueryFactory().insertControl(tardis_id, 19, plate, 0);
                }
                // set stable
                if (type.equals(Material.SOUL_SAND) &&
                        (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("RENDERER") ||
                         room.equals("ZERO") || room.equals("GEODE") || room.equals("HUTCH") ||
                         room.equals("IGLOO") || room.equals("MANGROVE") || room.equals("STALL") ||
                         room.equals("BAMBOO") || room.equals("BIRDCAGE") || room.equals("MAZE") || room.equals("GARDEN"))) {
                    HashMap<String, Object> sets = new HashMap<>();
                    sets.put(room.toLowerCase(Locale.ENGLISH), world.getName() + ":" + startx + ":" + starty + ":" + startz);
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("tardis_id", tardis_id);
                    switch (room) {
                        case "GARDEN" -> {
                            // do nothing here
                        }
                        case "RENDERER", "ZERO" -> plugin.getQueryFactory().doUpdate("tardis", sets, wheres);
                        case "MAZE" -> {
                            String loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startx, starty + 1, startz);
                            plugin.getQueryFactory().insertControl(tardis_id, 44, loc_str, 0);
                        }
                        default -> {
                            ResultSetFarming rsf = new ResultSetFarming(plugin, tardis_id);
                            if (rsf.resultSet()) {
                                // update
                                plugin.getQueryFactory().doUpdate("farming", sets, wheres);
                            } else {
                                sets.put("tardis_id", tardis_id);
                                plugin.getQueryFactory().doInsert("farming", sets);
                            }
                        }
                    }
                    // replace with correct block
                    switch (Room.valueOf(room)) {
                        case VILLAGE -> data = Material.COBBLESTONE.createBlockData();
                        case HUTCH, STABLE, STALL, MAZE -> data = Material.GRASS_BLOCK.createBlockData();
                        case BAMBOO, BIRDCAGE -> data = Material.PODZOL.createBlockData();
                        case GEODE -> data = Material.CLAY.createBlockData();
                        case IGLOO -> data = Material.PACKED_ICE.createBlockData();
                        case MANGROVE -> data = TARDISConstants.WATER;
                        case ZERO -> data = Material.PINK_CARPET.createBlockData();
                        case GARDEN -> {
                            data = Material.GRASS_BLOCK.createBlockData();
                            // save garden coords
                            HashMap<String, Object> setG = new HashMap<>();
                            setG.put("tardis_id", tardis_id);
                            setG.put("world", world.getName());
                            setG.put("minx", startx - 6);
                            setG.put("maxx", startx + 6);
                            setG.put("y", starty);
                            setG.put("minz", startz - 6);
                            setG.put("maxz", startz + 6);
                            plugin.getQueryFactory().doInsert("gardens", setG);
                        }
                        default -> {
                            data = TARDISConstants.BLACK;
                            // add WorldGuard region
                            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                                String name = "";
                                if (player == null) {
                                    ResultSetTardisTimeLordName rsn = new ResultSetTardisTimeLordName(plugin);
                                    if (rsn.fromID(tardis_id)) {
                                        name = rsn.getOwner();
                                    }
                                } else {
                                    name = player.getName();
                                }
                                if (!name.isEmpty()) {
                                    Location one = new Location(world, startx - 6, starty, startz - 6);
                                    Location two = new Location(world, startx + 6, starty + 8, startz + 6);
                                    plugin.getWorldGuardUtils().addRendererProtection(name, one, two);
                                }
                            }
                        }
                    }
                    if (!room.equals("ZERO") && !room.equals("RENDERER") && !room.equals("MAZE") && !room.equals("GARDEN")) {
                        // update player prefs - turn on mob farming
                        if (player != null) {
                            turnOnFarming(player);
                        }
                    }
                }
                if ((type.equals(Material.SOUL_SAND) || type.equals(Material.CARVED_PUMPKIN)) && room.equals("SMELTER")) {
                    String pos = new Location(world, startx, starty, startz).toString();
                    HashMap<String, Object> setsm = new HashMap<>();
                    setsm.put("tardis_id", tardis_id);
                    setsm.put("location", pos);
                    setsm.put("chest_type", type.equals(Material.CARVED_PUMPKIN) ? "SMELT" : "FUEL");
                    plugin.getQueryFactory().doInsert("vaults", setsm);
                    data = Material.CHEST.createBlockData();
                }
                if (type.equals(Material.DEAD_HORN_CORAL_BLOCK) && room.equals("AQUARIUM")) {
                    HashMap<String, Object> setaqua = new HashMap<>();
                    setaqua.put("aquarium", world.getName() + ":" + startx + ":" + starty + ":" + startz);
                    ResultSetFarming rsf = new ResultSetFarming(plugin, tardis_id);
                    if (rsf.resultSet()) {
                        HashMap<String, Object> wheres = new HashMap<>();
                        wheres.put("tardis_id", tardis_id);
                        // update
                        plugin.getQueryFactory().doUpdate("farming", setaqua, wheres);
                    } else {
                        setaqua.put("tardis_id", tardis_id);
                        plugin.getQueryFactory().doInsert("farming", setaqua);
                    }
                    data = (floor_type.equals(Material.LIGHT_GRAY_WOOL)) ? lgw.createBlockData() : floor_type.createBlockData();
                    if (player != null) {
                        turnOnFarming(player);
                    }
                    aqua_spawn = new Location(world, startx, starty + 1, startz);
                }
                // remember shell room button
                if (type.equals(Material.STONE_BUTTON) && room.equals("SHELL")) {
                    String loc_str = new Location(world, startx, starty, startz).toString();
                    plugin.getQueryFactory().insertControl(tardis_id, 25, loc_str, 0);
                }
                // remember village doors
                if (type.equals(Material.OAK_DOOR) && (room.equals("VILLAGE") || room.equals("SHELL"))) {
                    Block door = world.getBlockAt(startx, starty, startz);
                    doorblocks.put(door, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember torches
                if (type.equals(Material.TORCH)) {
                    Block torch = world.getBlockAt(startx, starty, startz);
                    torchblocks.put(torch, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember torches
                if (type.equals(Material.LEVER)) {
                    Block lever = world.getBlockAt(startx, starty, startz);
                    leverblocks.put(lever, data);
                }
                // remember iron trap doors if SURGERY room
                if (type.equals(Material.IRON_TRAPDOOR) && room.equals("SURGERY")) {
                    Block trap = world.getBlockAt(startx, starty, startz);
                    trapdoorblocks.put(trap, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember redstone torches
                if (type.equals(Material.REDSTONE_TORCH)) {
                    Block torch = world.getBlockAt(startx, starty, startz);
                    redstoneTorchblocks.put(torch, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember banners
                if (TARDISStaticUtils.isBanner(type)) {
                    Block banner = world.getBlockAt(startx, starty, startz);
                    JsonObject state = v.has("banner") ? v.get("banner").getAsJsonObject() : null;
                    if (state != null) {
                        TARDISBannerData tbd = new TARDISBannerData(data, state);
                        bannerblocks.put(banner, tbd);
                    }
                }
                if (type.equals(Material.FARMLAND)) {
                    Block farmland = world.getBlockAt(startx, starty, startz);
                    farmlandblocks.add(farmland);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.FARMLAND.createBlockData().getAsString());
                }
                if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
                    // remember sugar cane
                    if (type.equals(Material.SUGAR_CANE)) {
                        Block cane = world.getBlockAt(startx, starty, startz);
                        caneblocks.add(cane);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.SUGAR_CANE.createBlockData().getAsString());
                    }
                    // remember cocoa
                    if (type.equals(Material.COCOA)) {
                        Block cocoa = world.getBlockAt(startx, starty, startz);
                        cocoablocks.put(cocoa, data);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.COCOA.createBlockData().getAsString());
                    }
                    // remember wheat
                    if (type.equals(Material.WHEAT)) {
                        Block crops = world.getBlockAt(startx, starty, startz);
                        wheatblocks.add(crops);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.WHEAT.createBlockData().getAsString());
                    }
                    // remember melon
                    if (type.equals(Material.MELON_STEM)) {
                        Block melon = world.getBlockAt(startx, starty, startz);
                        melonblocks.add(melon);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.MELON_STEM.createBlockData().getAsString());
                    }
                    // remember pumpkin
                    if (type.equals(Material.PUMPKIN_STEM)) {
                        Block pumpkin = world.getBlockAt(startx, starty, startz);
                        pumpkinblocks.add(pumpkin);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.PUMPKIN_STEM.createBlockData().getAsString());
                    }
                    // remember carrots
                    if (type.equals(Material.CARROTS)) {
                        Block carrot = world.getBlockAt(startx, starty, startz);
                        carrotblocks.add(carrot);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.CARROTS.createBlockData().getAsString());
                    }
                    // remember potatoes
                    if (type.equals(Material.POTATOES)) {
                        Block potato = world.getBlockAt(startx, starty, startz);
                        potatoblocks.add(potato);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.POTATOES.createBlockData().getAsString());
                    }
                    if (level == 4 && room.equals("GREENHOUSE")) {
                        // set all the ice to water
                        iceblocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                        iceblocks.clear();
                    }
                }
                if (type.equals(Material.MANGROVE_PROPAGULE)) {
                    propagules.put(world.getBlockAt(startx, starty, startz), data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                if (type.equals(Material.SEAGRASS) || type.equals(Material.TALL_SEAGRASS)) {
                    seagrass.put(world.getBlockAt(startx, starty, startz), data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                if (room.equals("RAIL") && type.equals(Material.OAK_FENCE)) {
                    // remember fence location so we can teleport the storage minecart
                    String loc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("rail", loc);
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", tardis_id);
                    plugin.getQueryFactory().doUpdate("tardis", set, where);
                }
                // always replace bedrock (the door space in ARS rooms)
                if ((type.equals(Material.BEDROCK) && !room.equals("SHELL")) || (type.equals(Material.SOUL_SAND) && room.equals("SHELL"))) {
                    if (checkRoomNextDoor(world.getBlockAt(startx, starty, startz))) {
                        data = TARDISConstants.AIR;
                    } else {
                        if (ow.equals(Material.ORANGE_WOOL) && wall_type.equals(Material.ORANGE_WOOL)) {
                            if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                                data = TARDISConstants.BARRIER;
                                // set hexagon item display
                                TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, startx, starty, startz);
                            }
                        } else {
                            data = (wall_type.equals(Material.ORANGE_WOOL)) ? ow.createBlockData() : wall_type.createBlockData();
                        }
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
                            bottomdoorblock.setBlockData(TARDISConstants.AIR);
                            topdoorblock.setBlockData(TARDISConstants.AIR);
                        }
                    }
                }
                // always remove sponge
                if (type.equals(Material.SPONGE)) {
                    data = TARDISConstants.AIR;
                } else {
                    Block existing = world.getBlockAt(startx, starty, startz);
                    if (!existing.getType().isAir() && !(room.equals("BAMBOO") && existing.getType().equals(Material.BAMBOO))) {
                        if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                            switch (type) {
                                case AIR, GRAY_WOOL, LIGHT_GRAY_WOOL, ORANGE_WOOL, STONE_BRICKS -> {
                                }
                                default -> data = existing.getBlockData();
                            }
                        } else {
                            data = existing.getBlockData();
                        }
                    }
                }
                Chunk thisChunk = world.getChunkAt(world.getBlockAt(startx, starty, startz));
                thisChunk.setForceLoaded(true);
                chunkList.add(thisChunk);
                if (!notThese.contains(type) && !type.equals(Material.MUSHROOM_STEM)) {
                    if (type.equals(Material.WATER)) {
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, TARDISConstants.ICE);
                    } else {
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, data);
                    }
                }
                // remember ice blocks
                if ((type.equals(Material.WATER) || type.equals(Material.ICE)) && !room.equals("IGLOO")) {
                    Block icy = world.getBlockAt(startx, starty, startz);
                    iceblocks.add(icy);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + TARDISConstants.ICE.getAsString());
                }
                // remember lamp blocks
                if (type.equals(Material.REDSTONE_LAMP)) {
                    Block lamp = world.getBlockAt(startx, starty, startz);
                    lampblocks.add(lamp);
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
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + TARDISConstants.LAMP.getAsString());
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
                        plugin.getQueryFactory().doInsert("gravity_well", setd);
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
                        plugin.getQueryFactory().doInsert("gravity_well", setu);
                        Double[] values = {1D, 16D, 0.5D};
                        plugin.getGeneralKeeper().getGravityUpList().put(loc, values);
                    }
                }
                if (room.equals("MAZE") && type.equals(Material.STONE_PRESSURE_PLATE)) {
                    String loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startx, starty, startz);
                    int maze = 40 + maze_count;
                    plugin.getQueryFactory().insertControl(tardis_id, maze, loc_str, 0);
                    maze_count++;
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
                                loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startx, starty, startz);
                            }
                            case MUSHROOM_STEM -> { // repeater
                                control_type = repeaterOrder.get(r);
                                loc_str = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                Block rb = world.getBlockAt(startx, starty, startz);
                                mushroomblocks.put(rb, repeaterData[r]);
                                r++;
                            }
                            case OAK_BUTTON -> { // oak button - artron
                                control_type = 6;
                                loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startx, starty, startz);
                            }
                            default -> { // cake - handbrake
                                control_type = 0;
                                loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startx, starty, startz);
                            }
                        }
                        plugin.getQueryFactory().insertControl(tardis_id, control_type, loc_str, secondary);
                    }
                }
                if (room.equals("ZERO")) {
                    // remember the button
                    String loc_str;
                    if (type.equals(Material.OAK_BUTTON)) {
                        loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startx, starty, startz);
                        plugin.getQueryFactory().insertControl(tardis_id, 17, loc_str, 0);
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
                        plugin.getBuildKeeper().getRoomProgress().put(uuid, percent);
                        if (player != null) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_PERCENT", room, String.format("%d", percent));
                        }
                    }
                    level++;
                }
                rd.setRow(row);
                rd.setColumn(col);
                rd.setLevel(level);
                plugin.getTrackerKeeper().getRoomTasks().put(task, rd);
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            // abort room growing task
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            String message = "Resumption of room growing was aborted due to: " + e.getMessage();
            if (player != null) {
                plugin.getMessenger().messageWithColour(player, message, "#FF5555");
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
        plugin.getMessenger().send(p, TardisModule.TARDIS, "PREF_WAS_ON", "Mob farming");
    }

    private boolean checkRoomNextDoor(Block b) {
        if (b.getLocation().getBlockZ() < (resetz + 10) && !b.getRelative(BlockFace.EAST).getType().isAir()) {
            return true;
        } else {
            return !b.getRelative(BlockFace.SOUTH).getType().isAir();
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
