/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import com.google.gson.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.desktop.TARDISChunkUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.schematic.getters.DataPackPainting;
import me.eccentric_nz.TARDIS.schematic.setters.*;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
class TARDISBuildAbandoned implements Runnable {

    private final TARDIS plugin;
    private final Schematic schm;
    private final World world;
    private final int dbID;
    private final Player player;
    private final Material wall_type = Material.ORANGE_WOOL;
    private final Material floor_type = Material.LIGHT_GRAY_WOOL;
    private final HashMap<Block, BlockData> postBedBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postDripstoneBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLanternBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLichenBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postSculkVeinBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
    private final HashMap<Block, JsonObject> postSignBlocks = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> postBannerBlocks = new HashMap<>();
    private final List<Block> fractalBlocks = new ArrayList<>();
    private final List<Block> iceBlocks = new ArrayList<>();
    private final List<Block> postLightBlocks = new ArrayList<>();
    private final HashMap<String, Object> set = new HashMap<>();
    private final HashMap<String, Object> where = new HashMap<>();
    private Location postOod = null;
    private Block postBedrock = null;
    private boolean running;
    private TARDISTIPSData pos;
    private JsonObject obj;
    private JsonArray arr;
    private int task, h, w, d, level = 0, row = 0, startx, starty, startz, resetx, resetz, j = 2;
    private Location cl;
    private Location ender = null;
    private UseClay use_clay;
    private int counter = 0;
    private double div = 1.0d;
    private BossBar bb;

    /**
     * Builds the interior of an abandoned TARDIS.
     *
     * @param plugin an instance of the TARDIS plugin main class.
     * @param schm   the name of the schematic file to use can be ANCIENT, ARS, BIGGER, BONE, BUDGET, CAVE, COPPER,
     *               CORAL, CURSED, CUSTOM, DELTA, DELUXE, DIVISION, ELEVENTH, ENDER, FACTORY, FIFTEENTH, FUGITIVE,
     *               HOSPITAL, MASTER, MECHANICAL, ORIGINAL, PLANK, PYRAMID, REDSTONE, ROTOR, RUSTIC, STEAMPUNK,
     *               THIRTEENTH, TOM, TWELFTH, WAR, WEATHERED, WOOD, LEGACY_BIGGER, LEGACY_DELUXE, LEGACY_ELEVENTH,
     *               LEGACY_REDSTONE or a CUSTOM name.
     * @param world  the world where the TARDIS is to be built.
     * @param dbID   the unique key of the record for this TARDIS in the database.
     * @param player the player to show the progress bar to, may be null.
     */
    TARDISBuildAbandoned(TARDIS plugin, Schematic schm, World world, int dbID, Player player) {
        this.plugin = plugin;
        this.schm = schm;
        this.world = world;
        this.dbID = dbID;
        this.player = player;
    }

    @Override
    public void run() {
        if (!running) {
            starty = schm.getStartY();
            // get JSON
            obj = TARDISSchematicGZip.getObject(plugin, "consoles", schm.getPermission(), schm.isCustom());
            if (obj != null) {
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                h = dimensions.get("height").getAsInt() - 1;
                w = dimensions.get("width").getAsInt();
                d = dimensions.get("length").getAsInt() - 1;
                div = (h + 1.0d) * w * (d + 1.0d);
                // calculate startx, starty, startz
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                int slot = tintpos.getFreeSlot();
                pos = tintpos.getTIPSData(slot);
                // save the slot
                set.put("tips", slot);
                startx = pos.getCentreX();
                resetx = pos.getCentreX();
                startz = pos.getCentreZ();
                resetz = pos.getCentreZ();
                // get the correct chunk for ARS
                cl = new Location(world, startx, starty, startz);
                Chunk cars = world.getChunkAt(cl);
                String chun = world.getName() + ":" + cars.getX() + ":" + cars.getZ();
                set.put("chunk", chun);
                // get list of used chunks
                List<Chunk> chunkList = TARDISChunkUtils.getConsoleChunks(world, cl.getChunk().getX(), cl.getChunk().getZ(), w, d);
                // update chunks list in DB
                chunkList.forEach((ch) -> {
                    HashMap<String, Object> setc = new HashMap<>();
                    setc.put("tardis_id", dbID);
                    setc.put("world", world.getName());
                    setc.put("x", ch.getX());
                    setc.put("z", ch.getZ());
                    plugin.getQueryFactory().doInsert("chunks", setc);
                });
                where.put("tardis_id", dbID);
                // determine 'use_clay' material
                try {
                    use_clay = UseClay.valueOf(plugin.getConfig().getString("creation.use_clay"));
                } catch (IllegalArgumentException e) {
                    use_clay = UseClay.WOOL;
                }
                // get input array
                arr = obj.get("input").getAsJsonArray();
                if (player != null) {
                    // start progress bar
                    bb = Bukkit.createBossBar(TARDISConstants.GROWTH_STATES.getFirst(), BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
                    bb.setProgress(0);
                    bb.addPlayer(player);
                    bb.setVisible(true);
                }
                running = true;
            }
        }
        if (level == h && row == w - 1) {
            // put on the door, redstone torches, signs, beds, and the repeaters
            postBedBlocks.forEach(Block::setBlockData);
            postDoorBlocks.forEach(Block::setBlockData);
            postRedstoneTorchBlocks.forEach(Block::setBlockData);
            postTorchBlocks.forEach(Block::setBlockData);
            postLanternBlocks.forEach(Block::setBlockData);
            postRepeaterBlocks.forEach(Block::setBlockData);
            postStickyPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonExtensionBlocks.forEach(Block::setBlockData);
            postLeverBlocks.forEach(Block::setBlockData);
            postDripstoneBlocks.forEach(Block::setBlockData);
            postLichenBlocks.forEach(Block::setBlockData);
            postSculkVeinBlocks.forEach(Block::setBlockData);
            if (schm.getPermission().equals("cave")) {
                iceBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                iceBlocks.clear();
            }
            TARDISSignSetter.setSigns(postSignBlocks, plugin, dbID);
            if (postBedrock != null) {
                postBedrock.setBlockData(TARDISConstants.POWER);
            }
            if (postOod != null) {
                // spawn Ood
                TARDISFollowerSpawner spawner = new TARDISFollowerSpawner(plugin);
                spawner.spawnDivisionOod(postOod);
            }
            postLightBlocks.forEach((block) -> {
                if (block.getType().isAir()) {
                    Levelled levelled = TARDISConstants.LIGHT;
                    levelled.setLevel(15);
                    block.setBlockData(levelled);
                }
            });
            for (int f = 0; f < fractalBlocks.size(); f++) {
                FractalFence.grow(fractalBlocks.get(f), f);
            }
            TARDISBannerSetter.setBanners(postBannerBlocks);
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                UUID randomUUID = UUID.randomUUID();
                plugin.getWorldGuardUtils().addWGProtection(randomUUID, randomUUID.toString(), pos, world);
            }
            if (ender != null) {
                Entity ender_crystal = world.spawnEntity(ender, EntityType.END_CRYSTAL);
                ((EnderCrystal) ender_crystal).setShowingBottom(false);
            }
            if (obj.has("paintings")) {
                JsonArray paintings = (JsonArray) obj.get("paintings");
                for (int i = 0; i < paintings.size(); i++) {
                    JsonObject painting = paintings.get(i).getAsJsonObject();
                    JsonObject rel = painting.get("rel_location").getAsJsonObject();
                    int px = rel.get("x").getAsInt();
                    int py = rel.get("y").getAsInt();
                    int pz = rel.get("z").getAsInt();
                    BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
                    Location pl;
                    String which = painting.get("art").getAsString();
                    Art art = null;
                    if (which.contains(":")) {
                        // custom datapack painting
                        pl = TARDISPainting.calculatePosition(which.split(":")[1], facing, new Location(world, resetx + px, starty + py, resetz + pz));
                    } else {
                        art = Art.valueOf(which);
                        pl = TARDISPainting.calculatePosition(art, facing, new Location(world, resetx + px, starty + py, resetz + pz));
                    }
                    try {
                        Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                        ent.teleport(pl);
                        ent.setFacingDirection(facing, true);
                        if (art != null) {
                            ent.setArt(art, true);
                        } else {
                            DataPackPainting.setCustomVariant(ent, which);
                        }
                    } catch (IllegalArgumentException e) {
                        plugin.debug("Invalid painting location!" + pl);
                    }
                }
            }
            if (obj.has("item_frames")) {
                JsonArray frames = obj.get("item_frames").getAsJsonArray();
                for (int i = 0; i < frames.size(); i++) {
                    TARDISItemFrameSetter.curate(frames.get(i).getAsJsonObject(), cl, dbID);
                }
            }
            if (obj.has("item_displays")) {
                JsonArray displays = obj.get("item_displays").getAsJsonArray();
                for (int i = 0; i < displays.size(); i++) {
                    TARDISItemDisplaySetter.fakeBlock(displays.get(i).getAsJsonObject(), cl, dbID);
                }
            }
            // finished processing - update tardis table!
            plugin.getQueryFactory().doUpdate("tardis", set, where);
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
            if (player != null) {
                bb.setProgress(1);
                bb.setVisible(false);
                bb.removeAll();
            }
        }
        JsonArray floor = arr.get(level).getAsJsonArray();
        JsonArray r = (JsonArray) floor.get(row);
        // paste a column
        for (int col = 0; col <= d; col++) {
            counter++;
            JsonObject c = r.get(col).getAsJsonObject();
            int x = startx + row;
            int y = starty + level;
            int z = startz + col;
            BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
            Material type = data.getMaterial();
            if (type.equals(Material.LIGHT_GRAY_CONCRETE) && (schm.getPermission().equals("bone") || schm.getPermission().equals("rustic"))) {
                // get the block
                Block block = new Location(world, x, y, z).getBlock();
                String ct = (schm.getPermission().equals("bone")) ? "console_light_gray" : "console_rustic";
                // build a console
                new ConsoleBuilder(plugin).create(block, ct, dbID, TARDISConstants.UUID_ZERO.toString());
            }
            if (type.equals(Material.SCULK_SHRIEKER)) {
                // remember the location, so we can make it shriek when flying
                String shrieker = new Location(world, x, y, z).toString();
                TARDISTimeRotor.updateRotorRecord(dbID, shrieker);
            }
            if (type.equals(Material.NOTE_BLOCK)) {
                // remember the location of this Disk Storage
                String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 14, storage, 0);
                // set block data to correct BARRIER
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISDisplayItem.DISK_STORAGE, world, x, y, z);
            }
            if (Tag.WOOL.isTagged(type)) {
                switch (type) {
                    case ORANGE_WOOL -> {
                        if (wall_type == Material.LAPIS_BLOCK) { // if using the default Lapis Block - then use Orange Wool / Terracotta
                            switch (use_clay) {
                                case TERRACOTTA -> data = Material.ORANGE_TERRACOTTA.createBlockData();
                                case CONCRETE -> data = Material.ORANGE_CONCRETE.createBlockData();
                                default -> {
                                    data = TARDISConstants.BARRIER;
                                    // spawn an item display entity
                                    TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, x, y, z);
                                }
                            }
                        } else {
                            data = wall_type.createBlockData();
                        }
                    }
                    case LIGHT_GRAY_WOOL -> {
                        if (!schm.getPermission().equals("eleventh")) {
                            if (floor_type == Material.LAPIS_BLOCK) { // if using the default Lapis Block - then use Light Grey Wool / Terracotta
                                switch (use_clay) {
                                    case TERRACOTTA -> data = Material.LIGHT_GRAY_TERRACOTTA.createBlockData();
                                    case CONCRETE -> data = Material.LIGHT_GRAY_CONCRETE.createBlockData();
                                    default -> data = Material.LIGHT_GRAY_WOOL.createBlockData();
                                }
                            } else {
                                data = floor_type.createBlockData();
                            }
                        } else {
                            String[] split = type.toString().split("_");
                            String m;
                            if (split.length > 2) {
                                m = split[0] + "_" + split[1] + "_" + use_clay.toString();
                            } else {
                                m = split[0] + "_" + use_clay.toString();
                            }
                            Material material = Material.getMaterial(m);
                            if (material != null) {
                                data = material.createBlockData();
                            } else {
                                data = TARDISConstants.AIR;
                            }
                        }
                    }
                    case BLUE_WOOL -> {
                        switch (use_clay) {
                            case TERRACOTTA -> data = Material.BLUE_TERRACOTTA.createBlockData();
                            case CONCRETE -> data = Material.BLUE_CONCRETE.createBlockData();
                            default -> {
                                data = TARDISConstants.BARRIER;
                                // spawn an item display entity
                                TARDISDisplayItemUtils.set(TARDISDisplayItem.BLUE_BOX, world, x, y, z);
                            }
                        }
                    }
                    default -> {
                        String[] split = type.toString().split("_");
                        String m;
                        if (split.length > 2) {
                            m = split[0] + "_" + split[1] + "_" + use_clay.toString();
                        } else {
                            m = split[0] + "_" + use_clay.toString();
                        }
                        Material material = Material.getMaterial(m);
                        if (material != null) {
                            data = material.createBlockData();
                        } else {
                            data = TARDISConstants.AIR;
                        }
                    }
                }
            }
            if ((type.equals(Material.WARPED_FENCE) || type.equals(Material.CRIMSON_FENCE)) && schm.getPermission().equals("delta")) {
                fractalBlocks.add(world.getBlockAt(x, y, z));
            }
            if (level == 0 && type.equals(Material.PINK_STAINED_GLASS) && schm.getPermission().equals("division")) {
                postLightBlocks.add(world.getBlockAt(x, y - 1, z));
            }
            if (type.equals(Material.DEEPSLATE_REDSTONE_ORE) && (schm.getPermission().equals("division") || schm.getPermission().equals("hospital"))) {
                // replace with gray concrete
                data = schm.getPermission().equals("division") ? Material.GRAY_CONCRETE.createBlockData() : Material.LIGHT_GRAY_CONCRETE.createBlockData();
                if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                    // remember the block to spawn an Ood on
                    postOod = new Location(world, x, y + 1, z);
                }
            }
            if (type.equals(Material.WHITE_STAINED_GLASS) && schm.getPermission().equals("war")) {
                data = TARDISConstants.BARRIER;
                TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL, world, x, y, z);
            }
            if (type.equals(Material.WHITE_TERRACOTTA) && schm.getPermission().equals("war")) {
                data = TARDISConstants.BARRIER;
                TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL_OFFSET, world, x, y, z);
            }
            if (type.equals(Material.SPAWNER)) { // scanner button
                /*
                 * mob spawner will be converted to the correct id by
                 * setBlock(), but remember it for the scanner.
                 */
                String scanner = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 33, scanner, 0);
            }
            if (type.equals(Material.CHEST)) {
                // remember the location of the condenser chest
                String condenser = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 34, condenser, 0);
            }
            if (type.equals(Material.IRON_DOOR)) {
                Bisected bisected = (Bisected) data;
                if (bisected.getHalf().equals(Half.BOTTOM)) { // iron door bottom
                    HashMap<String, Object> setd = new HashMap<>();
                    String doorLocation = world.getName() + ":" + x + ":" + y + ":" + z;
                    setd.put("tardis_id", dbID);
                    setd.put("door_type", 1);
                    setd.put("door_location", doorLocation);
                    setd.put("door_direction", "SOUTH");
                    plugin.getQueryFactory().doInsert("doors", setd);
                }
            }
            if (type.equals(Material.STONE_BUTTON) && !schm.getPermission().equals("junk")) { // random button
                // remember the location of this button
                String button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 1, button, 0);
            }
            if (type.equals(Material.JUKEBOX)) {
                // remember the location of this Advanced Console
                String advanced = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 15, advanced, 0);
                // set block data to correct BARRIER + Item Display
                data = TARDISConstants.BARRIER;
                // spawn an item display entity
                TARDISDisplayItemUtils.set(TARDISDisplayItem.ADVANCED_CONSOLE, world, x, y, z);
            }
            if (type.equals(Material.CAKE) && !schm.getPermission().equals("junk")) {
                /*
                 * This block will be converted to a lever by setBlock(),
                 * but remember it so we can use it as the handbrake!
                 * Bone and Rustic have modelled consoles, not a lever handbrake.
                 */
                if (!schm.getPermission().equals("rustic") && !schm.getPermission().equals("bone")) {
                    String handbrakeLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(dbID, 0, handbrakeLocation, 0);
                }
                // create default json for ARS
                String[][][] empty = new String[3][9][9];
                for (int ars_y = 0; ars_y < 3; ars_y++) {
                    for (int ars_x = 0; ars_x < 9; ars_x++) {
                        for (int ars_z = 0; ars_z < 9; ars_z++) {
                            empty[ars_y][ars_x][ars_z] = "STONE";
                        }
                    }
                }
                String control = schm.getSeedMaterial().toString();
                empty[1][4][4] = control;
                switch (schm.getConsoleSize()) {
                    case MASSIVE -> {
                        // the 8 slots on the same level &
                        empty[1][4][5] = control;
                        empty[1][4][6] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        empty[1][5][6] = control;
                        empty[1][6][4] = control;
                        empty[1][6][5] = control;
                        empty[1][6][6] = control;
                        // the 9 slots on the level above
                        empty[2][4][4] = control;
                        empty[2][4][5] = control;
                        empty[2][4][6] = control;
                        empty[2][5][4] = control;
                        empty[2][5][5] = control;
                        empty[2][5][6] = control;
                        empty[2][6][4] = control;
                        empty[2][6][5] = control;
                        empty[2][6][6] = control;
                    }
                    case WIDE -> {
                        // the 8 slots on the same level
                        empty[1][4][5] = control;
                        empty[1][4][6] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        empty[1][5][6] = control;
                        empty[1][6][4] = control;
                        empty[1][6][5] = control;
                        empty[1][6][6] = control;
                    }
                    case TALL -> {
                        // the 3 slots on the same level &
                        empty[1][4][5] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        // the 4 slots on the level above
                        empty[2][4][4] = control;
                        empty[2][4][5] = control;
                        empty[2][5][4] = control;
                        empty[2][5][5] = control;
                    }
                    case MEDIUM -> {
                        // the 3 slots on the same level
                        empty[1][4][5] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                    }
                    default -> {
                        // SMALL size do nothing
                    }
                }
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                JsonArray json = JsonParser.parseString(gson.toJson(empty)).getAsJsonArray();
                HashMap<String, Object> seta = new HashMap<>();
                seta.put("tardis_id", dbID);
                seta.put("json", json.toString());
                plugin.getQueryFactory().doInsert("ars", seta);
            }
            if (type.equals(Material.ICE) && schm.getPermission().equals("cave")) {
                iceBlocks.add(world.getBlockAt(x, y, z));
            }
            if (type.equals(Material.LIGHT)) {
                // remember light block locations for malfunction and light switch
                HashMap<String, Object> setlb = new HashMap<>();
                String lightLocation = world.getName() + ":" + x + ":" + y + ":" + z;
                setlb.put("tardis_id", dbID);
                setlb.put("location", lightLocation);
                plugin.getQueryFactory().doInsert("lamps", setlb);
            }
            if (type.equals(Material.COMMAND_BLOCK) || ((schm.getPermission().equals("bigger") || schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                /*
                 * command block - remember it to spawn the creeper on.
                 * could also be a beacon block, as the creeper sits
                 * over the beacon in the deluxe and bigger consoles.
                 */
                String creeperLocation = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                set.put("creeper", creeperLocation);
                if (type.equals(Material.COMMAND_BLOCK)) {
                    data = switch (schm.getPermission()) {
                        case "ender" -> Material.END_STONE_BRICKS.createBlockData();
                        case "delta", "cursed" -> Material.BLACKSTONE.createBlockData();
                        case "ancient", "bone", "fugitive" -> Material.GRAY_WOOL.createBlockData();
                        case "hospital" -> Material.LIGHT_GRAY_WOOL.createBlockData();
                        default -> Material.STONE_BRICKS.createBlockData();
                    };
                }
            }
            if (Tag.BUTTONS.isTagged(type) && !schm.getPermission().equals("junk")) {
                /*
                 * wood button - remember it for the Artron Energy
                 * Capacitor.
                 */
                String woodButtonLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 6, woodButtonLocation, 0);
            }
            if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                /*
                 * remember the telepathic circuit.
                 */
                String telepathicLocation = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbID, 23, telepathicLocation, 0);
            }
            if (type.equals(Material.BEACON) && schm.getPermission().equals("ender")) {
                /*
                 * get the ender crystal location
                 */
                ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
            }
            // if it's an iron/gold/diamond/emerald/beacon/redstone/bedrock/conduit/netherite block put it in the blocks table
            if (TARDISBuilderInstanceKeeper.getPrecious().contains(type)) {
                HashMap<String, Object> setpb = new HashMap<>();
                String loc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                setpb.put("tardis_id", dbID);
                setpb.put("location", loc);
                setpb.put("data", "minecraft:air");
                setpb.put("police_box", 0);
                plugin.getQueryFactory().doInsert("blocks", setpb);
                plugin.getGeneralKeeper().getProtectBlockMap().put(loc, dbID);
            }
            if (type.equals(Material.IRON_DOOR)) { // doors
                // if it's the door, don't set it just remember its block then do it at the end
                postDoorBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (Tag.BEDS.isTagged(type)) {
                postBedBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.REDSTONE_TORCH) || type.equals(Material.REDSTONE_WALL_TORCH)) {
                postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.TORCH) || type.equals(Material.WALL_TORCH) || type.equals(Material.SOUL_TORCH) || type.equals(Material.SOUL_WALL_TORCH)) {
                postTorchBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.STICKY_PISTON)) {
                postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.PISTON)) {
                postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.PISTON_HEAD)) {
                postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.LEVER)) {
                postLeverBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (Tag.ALL_SIGNS.isTagged(type)) {
                postSignBlocks.put(world.getBlockAt(x, y, z), c);
            } else if (type.equals(Material.POINTED_DRIPSTONE)) {
                postDripstoneBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.GLOW_LICHEN)) {
                postLichenBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.SCULK_VEIN)) {
                postSculkVeinBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (type.equals(Material.LANTERN) || type.equals(Material.SOUL_LANTERN)) {
                postLanternBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (TARDISStaticUtils.isBanner(type)) {
                JsonObject state = c.has("banner") ? c.getAsJsonObject("banner") : null;
                if (state != null) {
                    TARDISBannerData tbd = new TARDISBannerData(data, state);
                    postBannerBlocks.put(world.getBlockAt(x, y, z), tbd);
                }
            } else if (type.equals(Material.PLAYER_HEAD) || type.equals(Material.PLAYER_WALL_HEAD)) {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
                if (c.has("head")) {
                    JsonObject head = c.get("head").getAsJsonObject();
                    if (head.has("uuid")) {
                        try {
                            UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                            TARDISHeadSetter.textureSkull(plugin, uuid, head, world.getBlockAt(x, y, z));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                }
            } else if (TARDISStaticUtils.isInfested(type)) {
                // legacy monster egg stone for controls
                TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
            } else if (type.equals(Material.MUSHROOM_STEM)) { // mushroom stem for repeaters
                // save repeater location
                if (j < 6) {
                    String repeater = world.getName() + ":" + x + ":" + y + ":" + z;
                    data = Material.REPEATER.createBlockData();
                    Directional directional = (Directional) data;
                    switch (j) {
                        case 2 -> {
                            directional.setFacing(BlockFace.WEST);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 3, repeater, 0);
                        }
                        case 3 -> {
                            directional.setFacing(BlockFace.NORTH);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 2, repeater, 0);
                        }
                        case 4 -> {
                            directional.setFacing(BlockFace.SOUTH);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 5, repeater, 0);
                        }
                        default -> {
                            directional.setFacing(BlockFace.EAST);
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                            plugin.getQueryFactory().insertSyncControl(dbID, 4, repeater, 0);
                        }
                    }
                    j++;
                }
            } else if (type.equals(Material.SPONGE)) {
                TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
            } else if (type.equals(Material.BEDROCK)) {
                // remember bedrock location to block off the beacon light
                String bedrockLocation = world.getName() + ":" + x + ":" + y + ":" + z;
                set.put("beacon", bedrockLocation);
                postBedrock = world.getBlockAt(x, y, z);
            } else if (type.equals(Material.BROWN_MUSHROOM) && schm.getPermission().equals("master")) {
                // spawn locations for two villagers
                TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                plugin.setTardisSpawn(true);
                world.spawnEntity(new Location(world, +0.5, y + 0.25, z + 0.5), EntityType.VILLAGER);
            } else {
                TARDISBlockSetters.setBlock(world, x, y, z, data);
            }
            if (player != null) {
                double progress = counter / div;
                if (progress > 1.0) {
                    progress = 1.0;
                }
                bb.setProgress(progress);
            }
            if (col == d && row < w) {
                row++;
            }
            if (col == d && row == w && level < h) {
                row = 0;
                level++;
                if (player != null) {
                    // set progress bar title
                    if (level == h) {
                        bb.setTitle(TARDISConstants.GROWTH_STATES.get(31));
                    } else {
                        bb.setTitle(TARDISConstants.GROWTH_STATES.get(level));
                    }
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
