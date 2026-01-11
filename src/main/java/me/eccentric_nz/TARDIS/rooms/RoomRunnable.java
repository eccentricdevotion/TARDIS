/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarming;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHappy;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisTimeLordName;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.rooms.architectural.tree.TreeBuilder;
import me.eccentric_nz.TARDIS.rooms.eye.EyeOfHarmonyParticles;
import me.eccentric_nz.TARDIS.rooms.library.LibraryCatalogue;
import me.eccentric_nz.TARDIS.schematic.setters.*;
import me.eccentric_nz.TARDIS.utility.*;
import me.eccentric_nz.tardischunkgenerator.custombiome.BiomeHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The TARDIS had a swimming pool. After the TARDIS' crash following the Doctor's tenth regeneration, the pool's water -
 * or perhaps the pool itself - fell into the library. After the TARDIS had fixed itself, the swimming pool was restored
 * but the Doctor did not know where it was.
 *
 * @author eccentric_nz
 */
public class RoomRunnable implements Runnable {

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
    private final List<Block> caneBlocks = new ArrayList<>();
    private final List<Block> carrotBlocks = new ArrayList<>();
    private final List<Block> farmlandBlocks = new ArrayList<>();
    private final List<Block> iceBlocks = new ArrayList<>();
    private final List<Block> lavaBlocks = new ArrayList<>();
    private final List<Block> lampBlocks = new ArrayList<>();
    private final List<Block> melonBlocks = new ArrayList<>();
    private final List<Block> potatoBlocks = new ArrayList<>();
    private final List<Block> pumpkinBlocks = new ArrayList<>();
    private final List<Block> wheatBlocks = new ArrayList<>();
    private final List<Material> notThese = new ArrayList<>();
    private final List<BlockData> flora = new ArrayList<>();
    private final HashMap<Block, BlockData> cocoaBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> doorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> dripLeafBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> leverBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> propagules = new HashMap<>();
    private final HashMap<Block, BlockData> redstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> seagrass = new HashMap<>();
    private final HashMap<Block, BlockData> signBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> torchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> trapdoorBlocks = new HashMap<>();
    private final HashMap<Block, BlockFace> mushroomBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> magmaBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> eyeBlocks = new HashMap<>();
    private final HashMap<Block, JsonObject> postSignBlocks = new HashMap<>();
    private final HashMap<Block, JsonObject> pots = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> bannerBlocks = new HashMap<>();
    private final BlockFace[] repeaterData = new BlockFace[6];
    private final HashMap<Integer, Integer> repeaterOrder = new HashMap<>();
    private final boolean wasResumed;
    private final List<String> postBlocks;
    private Location library;
    private int maze_count = 0;
    private int task, level, row, col, h, w, c, startx, starty, startz, resetx, resety, resetz;
    private boolean running;
    private World world;
    private JsonArray arr;
    private Block architectural;
    private Location aqua_spawn;
    private Chunk thisChunk;
    private int r = 2;

    public RoomRunnable(TARDIS plugin, RoomData roomData, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
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
        notThese.add(Material.BIG_DRIPLEAF);
        notThese.add(Material.BIG_DRIPLEAF_STEM);
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
                                case ICE -> iceBlocks.add(postBlock);
                                case LAVA -> lavaBlocks.add(postBlock);
                                case BIG_DRIPLEAF, BIG_DRIPLEAF_STEM -> dripLeafBlocks.put(postBlock, postData);
                                case REDSTONE_LAMP -> lampBlocks.add(postBlock);
                                case TORCH -> torchBlocks.put(postBlock, postData);
                                case REDSTONE_TORCH -> redstoneTorchBlocks.put(postBlock, postData);
                                case COCOA -> cocoaBlocks.put(postBlock, postData);
                                case SUGAR_CANE -> caneBlocks.add(postBlock);
                                case MANGROVE_PROPAGULE -> propagules.put(postBlock, postData);
                                case SEAGRASS -> seagrass.put(postBlock, postData);
                                case MELON_STEM -> melonBlocks.add(postBlock);
                                case POTATOES -> potatoBlocks.add(postBlock);
                                case CARROTS -> carrotBlocks.add(postBlock);
                                case PUMPKIN_STEM -> pumpkinBlocks.add(postBlock);
                                case WHEAT -> wheatBlocks.add(postBlock);
                                case FARMLAND -> farmlandBlocks.add(postBlock);
                                case OAK_DOOR -> doorBlocks.put(postBlock, postData);
                                case LEVER -> leverBlocks.put(postBlock, postData);
                                case IRON_TRAPDOOR -> trapdoorBlocks.put(postBlock, postData);
                                default -> {
                                    if (Tag.ALL_SIGNS.isTagged(postData.getMaterial())) {
                                        signBlocks.put(postBlock, postData);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    RoomData trd = plugin.getTrackerKeeper().getRoomTasks().get(task);
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
                if (room.equals("EYE")) {
                    new BiomeHelper().setCustomBiome("eye_of_harmony", l.getChunk(), starty);
                }
            }
            if (level == h && row == w && col == (c - 1)) {
                // the entire schematic has been read :)
                if (!iceBlocks.isEmpty()) {
                    if (player != null) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ICE");
                    }
                    // set all the ice to water
                    iceBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                    iceBlocks.clear();
                }
                if (!lavaBlocks.isEmpty()) {
                    if (player != null) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "LAVA");
                    }
                    // set all the blocks to lava
                    lavaBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.LAVA));
                    lavaBlocks.clear();
                }
                if (!postSignBlocks.isEmpty()) {
                    SignSetter.setSigns(postSignBlocks, plugin, 0);
                }
                if (!pots.isEmpty()) {
                    for (Map.Entry<Block, JsonObject> pot : pots.entrySet()) {
                        PotSetter.decorate(plugin, pot.getValue(), pot.getKey());
                    }
                }
                if (!propagules.isEmpty()) {
                    for (Map.Entry<Block, BlockData> prop : propagules.entrySet()) {
                        prop.getKey().setBlockData(prop.getValue());
                    }
                }
                if (!dripLeafBlocks.isEmpty()) {
                    for (Map.Entry<Block, BlockData> prop : dripLeafBlocks.entrySet()) {
                        prop.getKey().setBlockData(prop.getValue());
                    }
                }
                if (!signBlocks.isEmpty()) {
                    for (Map.Entry<Block, BlockData> sign : signBlocks.entrySet()) {
                        sign.getKey().setBlockData(sign.getValue());
                    }
                }
                if (!trapdoorBlocks.isEmpty()) {
                    for (Map.Entry<Block, BlockData> trap : trapdoorBlocks.entrySet()) {
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
                            aqua_spawn = TARDISStaticLocationGetters.getSpawnLocationFromDB(rsf.getFarming().aquarium());
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
                // mannequins
                if (obj.has("mannequins")) {
                    JsonArray mannequins = obj.get("mannequins").getAsJsonArray();
                    MannequinSetter.setMannequins(mannequins, world, resetx, resety, resetz);
                }
                // armour stands
                if (obj.has("armour_stands")) {
                    JsonArray stands = obj.get("armour_stands").getAsJsonArray();
                    ArmourStandSetter.setStands(stands, world, resetx, resety, resetz);
                }
                // paintings
                if (obj.has("paintings")) {
                    JsonArray paintings = (JsonArray) obj.get("paintings");
                    PaintingSetter.setArt(paintings, world, resetx, resety, resetz);
                }
                Location start = new Location(world, resetx, resety, resetz);
                if (obj.has("item_frames")) {
                    JsonArray frames = obj.get("item_frames").getAsJsonArray();
                    ItemFrameSetter.curate(frames, start, tardis_id);
                }
                if (obj.has("item_displays")) {
                    JsonArray displays = obj.get("item_displays").getAsJsonArray();
                    ItemDisplaySetter.process(displays, player, start, tardis_id);
                }
                if (room.equals("NAUTILUS")) {
                    magmaBlocks.forEach((key, value) -> {
                        key.setBlockData(value, true);
                        // also add some random seagrass
                        Block meal = key.getRelative(BlockFace.NORTH);
                        meal.applyBoneMeal(BlockFace.UP);
                        world.spawnEntity(meal.getLocation().add(0.5d, 1d, 0.5d), EntityType.PUFFERFISH);
                    });
                    magmaBlocks.clear();
                }
                if (room.equals("ARCHITECTURAL") && architectural != null) {
                    // generate a fractal tree
                    new TreeBuilder().place(architectural);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> architectural.setType(Material.GRAY_SHULKER_BOX), 30L);
                }
                if (room.equals("EYE")) {
                    eyeBlocks.forEach((key, value) -> key.setBlockData(value, true));
                    eyeBlocks.clear();
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
                if (room.equals("LIBRARY") && library != null) {
                    // add shelf labels
                    new LibraryCatalogue().label(library);
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
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_POWER");
                }
                if (plugin.getConfig().getBoolean("allow.dynamic_lamps")) {
                    // get lamp preference
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                    if (rsp.resultSet() && rsp.isDynamicLightsOn()) {
                        TardisLight light = rsp.getLights();
                        lampBlocks.forEach((lamp) -> TARDISDisplayItemUtils.set(light.getOn(), lamp, tardis_id));
                    } else {
                        lampBlocks.forEach((lamp) -> lamp.setBlockData(TARDISConstants.LAMP));
                    }
                } else {
                    lampBlocks.forEach((lamp) -> lamp.setBlockData(TARDISConstants.LAMP));
                }
                lampBlocks.clear();
                // put torches on
                torchBlocks.forEach((key, value) -> key.setBlockData(value, true));
                torchBlocks.clear();
                // put redstone torches on
                redstoneTorchBlocks.forEach((key, value) -> key.setBlockData(value, true));
                torchBlocks.clear();
                // set banners
                BannerSetter.setBanners(bannerBlocks);
                // remove staircase floor/ceiling if necessary
                if (plugin.getTrackerKeeper().getIsStackedStaircase().containsKey(tardis_id) && room.equals("STAIRCASE")) {
                    boolean above = plugin.getTrackerKeeper().getIsStackedStaircase().get(tardis_id);
                    int sy = above ? resety - 1 : starty;
                    for (int y = sy; y < sy + 2; y++) {
                        for (int x = resetx; x < resetx + 15; x++) {
                            for (int z = resetz; z < resetz + 15; z++) {
                                Block block = world.getBlockAt(x, y, z);
                                if (block.getType() == Material.WARPED_SLAB) {
                                    block.setType(Material.AIR);
                                }
                            }
                        }
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_STAIRCASE");
                }
                // remove the chunks, so they can unload as normal again
                if (!chunkList.isEmpty()) {
                    chunkList.forEach((ch) -> ch.removePluginChunkTicket(plugin));
                }
                plugin.getTrackerKeeper().getRoomTasks().remove(task);
                // cancel the task
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                String rname = (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) ? room + " WELL" : room;
                if (player != null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_FINISHED", rname);
                    if (plugin.getUtils().inTARDISWorld(player) && thisChunk != null) {
                        // get room coords and send message to transmat player to room
                        Transmat transmat = getTransmat(thisChunk);
                        plugin.getMessenger().sendRoomTransmat(player, transmat);
                    }
                }
                plugin.getBuildKeeper().getRoomProgress().remove(uuid);
                plugin.getTrackerKeeper().getIsGrowingRooms().remove(tardis_id);
                plugin.getTrackerKeeper().getIsStackedStaircase().remove(tardis_id);
            } else {
                RoomData rd = plugin.getTrackerKeeper().getRoomTasks().get(task);
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
                        ow = room.equals("STAIRCASE") ? Material.ORANGE_TERRACOTTA : Material.ORANGE_WOOL;
                        lgw = Material.LIGHT_GRAY_WOOL;
                        gw = Material.GRAY_WOOL;
                    }
                }
                if (type.equals(Material.GRAY_WOOL)) {
                    data = gw.createBlockData();
                }
                if (type.equals(Material.ORANGE_WOOL)) {
                    if (wall_type.equals(Material.ORANGE_WOOL) || ((room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) && (wall_type.equals(Material.LIME_WOOL) || wall_type.equals(Material.PINK_WOOL)))) {
                        data = ow.createBlockData();
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
                    signBlocks.put(sign, data);
                }
                if (type.equals(Material.DECORATED_POT)) {
                    if (v.has("pot")) {
                        Block pot = world.getBlockAt(startx, starty, startz);
                        pots.put(pot, v.get("pot").getAsJsonObject());
                    }
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
                // set library
                if (type.equals(Material.CHEST) && room.equals("LIBRARY")) {
                    Location pos = new Location(world, startx, starty, startz);
                    HashMap<String, Object> setl = new HashMap<>();
                    setl.put("tardis_id", tardis_id);
                    setl.put("location", pos.toString());
                    setl.put("chest_type", "LIBRARY");
                    plugin.getQueryFactory().doInsert("vaults", setl);
                    library = pos.clone().add(-8, -4, -8);
                }
                // nautilus magma blocks
                if (type.equals(Material.RED_SAND) && room.equals("NAUTILUS")) {
                    Block magma = world.getBlockAt(startx, starty, startz);
                    magmaBlocks.put(magma, TARDISConstants.MAGMA);
                }
                // nautilus water blocks
                if (type.equals(Material.DEAD_BUBBLE_CORAL_BLOCK) && room.equals("NAUTILUS")) {
                    Block water = world.getBlockAt(startx, starty, startz);
                    iceBlocks.add(water);
                }
                // eye of harmony wall
                if (type.equals(Material.RED_SANDSTONE_WALL) && room.equals("EYE")) {
                    Block wall = world.getBlockAt(startx, starty, startz);
                    eyeBlocks.put(wall, data);
                }
                // eye of harmony item display
                if (type.equals(Material.SHROOMLIGHT) && room.equals("EYE")) {
                    double sx = startx + 0.5d;
                    double sy = starty + 0.5d;
                    double sz = startz + 0.5d;
                    Location item = new Location(world, sx, sy, sz);
                    ItemDisplay display = (ItemDisplay) world.spawnEntity(item, EntityType.ITEM_DISPLAY);
                    ItemStack is = ItemStack.of(Material.MAGMA_BLOCK);
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.text("Sphere Normal"));
                    is.setItemMeta(im);
                    display.setItemStack(is);
                    display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
                    // save location to controls
                    plugin.getQueryFactory().insertSyncControl(tardis_id, 53, item.toString(), 0);
                    data = TARDISConstants.BARRIER;
                    // start a particle runnable
                    if (plugin.getConfig().getBoolean("eye_of_harmony.particles")) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            int task = new EyeOfHarmonyParticles(plugin).start(tardis_id, 1, uuid);
                            if (task != -1) {
                                // update eyes record
                                HashMap<String, Object> set = new HashMap<>();
                                set.put("task", task);
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("tardis_id", tardis_id);
                                plugin.getQueryFactory().doSyncUpdate("eyes", set, where);
                            }
                        }, 600L);
                    }
                }
                // eye of harmony storage
                if (type.equals(Material.BARRIER) && room.equals("EYE")) {
                    String dbStr = new Location(world, startx, starty, startz).toString();
                    // save location to controls
                    plugin.getQueryFactory().insertControl(tardis_id, 54, dbStr, 0);
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
                if (type.equals(Material.SOUL_SAND) && (room.equals("ARCHITECTURAL") || room.equals("STABLE") || room.equals("VILLAGE") || room.equals("RENDERER") || room.equals("LAVA") || room.equals("ALLAY") || room.equals("ZERO") || room.equals("GEODE") || room.equals("HUTCH") || room.equals("IGLOO") || room.equals("IISTUBIL") || room.equals("MANGROVE") || room.equals("PEN") || room.equals("STALL") || room.equals("BAMBOO") || room.equals("BIRDCAGE") || room.equals("MAZE") || room.equals("GARDEN") || room.equals("HAPPY") || room.equals("NAUTILUS"))) {
                    HashMap<String, Object> sets = new HashMap<>();
                    sets.put(room.toLowerCase(Locale.ROOT), world.getName() + ":" + startx + ":" + starty + ":" + startz);
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("tardis_id", tardis_id);
                    switch (room) {
                        case "GARDEN" -> {
                            // do nothing here
                        }
                        case "RENDERER", "ZERO" -> plugin.getQueryFactory().doUpdate("tardis", sets, wheres);
                        case "ARCHITECTURAL", "MAZE" -> {
                            String loc_str = TARDISStaticLocationGetters.makeLocationStr(world, startx, starty + 1, startz);
                            int control = room.equals("ARCHITECTURAL") ? 59 : 44;
                            plugin.getQueryFactory().insertControl(tardis_id, control, loc_str, 0);
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
                    switch (room) {
                        case "ALLAY" -> data = Material.LIGHT_GRAY_WOOL.createBlockData();
                        case "ARCHITECTURAL" -> {
                            data = Material.LIGHT_GRAY_WOOL.createBlockData();
                            architectural = world.getBlockAt(startx, starty + 1, startz);
                        }
                        case "VILLAGE" -> data = Material.COBBLESTONE.createBlockData();
                        case "HUTCH", "STABLE", "STALL", "MAZE" -> data = Material.GRASS_BLOCK.createBlockData();
                        case "BAMBOO", "BIRDCAGE" -> data = Material.PODZOL.createBlockData();
                        case "GEODE" -> data = Material.CLAY.createBlockData();
                        case "HAPPY" -> data = Material.BLUE_ICE.createBlockData();
                        case "IGLOO" -> data = Material.PACKED_ICE.createBlockData();
                        case "IISTUBIL" -> data = Material.TERRACOTTA.createBlockData();
                        case "LAVA" -> data = Material.NETHERRACK.createBlockData();
                        case "MANGROVE" -> data = TARDISConstants.WATER;
                        case "NAUTILUS" -> data = TARDISConstants.GLASS;
                        case "PEN" -> data = Material.MOSS_BLOCK.createBlockData();
                        case "ZERO" -> data = Material.PINK_CARPET.createBlockData();
                        case "GARDEN" -> {
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
                if (type.equals(Material.CRIMSON_HYPHAE) && room.equals("HAPPY")) {
                    // remember happy control
                    String loc_str = new Location(world, startx, starty, startz).toString();
                    plugin.getQueryFactory().insertControl(tardis_id, 58, loc_str, 0);
                    // should we add a happy table record?
                    ResultSetHappy rsh = new ResultSetHappy(plugin);
                    HashMap<String, Object> set = new HashMap<>();
                    if (rsh.fromId(tardis_id)) {
                        // update record
                        set.put("slots", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", tardis_id);
                        plugin.getQueryFactory().doUpdate("happy", set, where);
                    } else {
                        // add new record
                        set.put("tardis_id", tardis_id);
                        plugin.getQueryFactory().doInsert("happy", set);
                    }
                    // add block to levers
                    Block lever = world.getBlockAt(startx, starty, startz);
                    Switch happyLever = (Switch) Material.LEVER.createBlockData();
                    happyLever.setAttachedFace(FaceAttachable.AttachedFace.WALL);
                    happyLever.setFacing(BlockFace.WEST);
                    happyLever.setPowered(true);
                    leverBlocks.put(lever, happyLever);
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
                    doorBlocks.put(door, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember torches
                if (type.equals(Material.TORCH)) {
                    Block torch = world.getBlockAt(startx, starty, startz);
                    torchBlocks.put(torch, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember levers
                if (type.equals(Material.LEVER)) {
                    Block lever = world.getBlockAt(startx, starty, startz);
                    leverBlocks.put(lever, data);
                }
                // remember iron trap doors
                if (type.equals(Material.IRON_TRAPDOOR)) {
                    Block trap = world.getBlockAt(startx, starty, startz);
                    trapdoorBlocks.put(trap, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember redstone torches
                if (type.equals(Material.REDSTONE_TORCH)) {
                    Block torch = world.getBlockAt(startx, starty, startz);
                    redstoneTorchBlocks.put(torch, data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                // remember banners
                if (TARDISStaticUtils.isBanner(type)) {
                    Block banner = world.getBlockAt(startx, starty, startz);
                    JsonObject state = v.has("banner") ? v.get("banner").getAsJsonObject() : null;
                    if (state != null) {
                        TARDISBannerData tbd = new TARDISBannerData(data, state);
                        bannerBlocks.put(banner, tbd);
                    }
                }
                if (type.equals(Material.FARMLAND)) {
                    Block farmland = world.getBlockAt(startx, starty, startz);
                    farmlandBlocks.add(farmland);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.FARMLAND.createBlockData().getAsString());
                }
                // remember lava
                if (type.equals(Material.LAVA)) {
                    Block lava = world.getBlockAt(startx, starty, startz);
                    lavaBlocks.add(lava);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + TARDISConstants.LAVA.getAsString());
                }
                if (room.equals("ARBORETUM") || room.equals("GREENHOUSE")) {
                    // remember sugar cane
                    if (type.equals(Material.SUGAR_CANE)) {
                        Block cane = world.getBlockAt(startx, starty, startz);
                        caneBlocks.add(cane);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.SUGAR_CANE.createBlockData().getAsString());
                    }
                    // remember cocoa
                    if (type.equals(Material.COCOA)) {
                        Block cocoa = world.getBlockAt(startx, starty, startz);
                        cocoaBlocks.put(cocoa, data);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.COCOA.createBlockData().getAsString());
                    }
                    // remember wheat
                    if (type.equals(Material.WHEAT)) {
                        Block crops = world.getBlockAt(startx, starty, startz);
                        wheatBlocks.add(crops);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.WHEAT.createBlockData().getAsString());
                    }
                    // remember melon
                    if (type.equals(Material.MELON_STEM)) {
                        Block melon = world.getBlockAt(startx, starty, startz);
                        melonBlocks.add(melon);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.MELON_STEM.createBlockData().getAsString());
                    }
                    // remember pumpkin
                    if (type.equals(Material.PUMPKIN_STEM)) {
                        Block pumpkin = world.getBlockAt(startx, starty, startz);
                        pumpkinBlocks.add(pumpkin);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.PUMPKIN_STEM.createBlockData().getAsString());
                    }
                    // remember carrots
                    if (type.equals(Material.CARROTS)) {
                        Block carrot = world.getBlockAt(startx, starty, startz);
                        carrotBlocks.add(carrot);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.CARROTS.createBlockData().getAsString());
                    }
                    // remember potatoes
                    if (type.equals(Material.POTATOES)) {
                        Block potato = world.getBlockAt(startx, starty, startz);
                        potatoBlocks.add(potato);
                        rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + Material.POTATOES.createBlockData().getAsString());
                    }
                    if (level == 4 && room.equals("GREENHOUSE")) {
                        // set all the ice to water
                        iceBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                        iceBlocks.clear();
                    }
                }
                if (type.equals(Material.MANGROVE_PROPAGULE)) {
                    propagules.put(world.getBlockAt(startx, starty, startz), data);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + data.getAsString());
                }
                if (type.equals(Material.BIG_DRIPLEAF) || type.equals(Material.BIG_DRIPLEAF_STEM)) {
                    dripLeafBlocks.put(world.getBlockAt(startx, starty, startz), data);
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
                    } else if (room.equals("ARCHITECTURAL")) {
                        data = gw.createBlockData();
                    } else {
                        data = (wall_type.equals(Material.ORANGE_WOOL)) ? ow.createBlockData() : wall_type.createBlockData();
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
                            // remove display entities
                            TARDISDisplayItemUtils.remove(bottomdoorblock);
                            TARDISDisplayItemUtils.remove(topdoorblock);
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
                thisChunk = world.getChunkAt(world.getBlockAt(startx, starty, startz));
                thisChunk.addPluginChunkTicket(plugin);
                chunkList.add(thisChunk);
                if (!notThese.contains(type) && !type.equals(Material.MUSHROOM_STEM)) {
                    if (type.equals(Material.WATER)) {
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, TARDISConstants.ICE);
                    } else if (type.equals(Material.LAVA)) {
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, Material.ORANGE_STAINED_GLASS);
                    } else {
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, data);
                    }
                }
                // remember ice blocks
                if ((type.equals(Material.WATER) || type.equals(Material.ICE)) && !room.equals("IGLOO")) {
                    Block icy = world.getBlockAt(startx, starty, startz);
                    iceBlocks.add(icy);
                    rd.getPostBlocks().add(world.getName() + ":" + startx + ":" + starty + ":" + startz + "~" + TARDISConstants.ICE.getAsString());
                }
                // remember lamp blocks
                if (type.equals(Material.REDSTONE_LAMP)) {
                    Block lamp = world.getBlockAt(startx, starty, startz);
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
                    int control_type;
                    String loc_str;
                    List<Material> controls = List.of(Material.CAKE, Material.STONE_BUTTON, Material.MUSHROOM_STEM, Material.OAK_BUTTON);
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
                                mushroomBlocks.put(rb, repeaterData[r]);
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

    private Transmat getTransmat(Chunk chunk) {
        Location location = new Location(chunk.getWorld(), resetx, resety, resetz).add(3.5d, 5.0d, 8.5d);
        return new Transmat(room, location.getWorld().getName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0.0f);
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
