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
package me.eccentric_nz.tardis.builders;

import com.google.gson.*;
import me.eccentric_nz.tardis.TardisBuilderInstanceKeeper;
import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.custommodeldata.TardisMushroomBlockData;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.enumeration.UseClay;
import me.eccentric_nz.tardis.schematic.TardisBannerSetter;
import me.eccentric_nz.tardis.schematic.TardisSchematicGZip;
import me.eccentric_nz.tardis.utility.TardisBannerData;
import me.eccentric_nz.tardis.utility.TardisBlockSetters;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

/**
 * The tardis was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the tardis from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
class TardisBuildAbandoned implements Runnable {

    private final TardisPlugin plugin;
    private final List<Block> lampBlocks = new ArrayList<>();
    private final Schematic schematic;
    private final World world;
    private final int dbId;
    private final Player player;
    private final Material wall_type = Material.ORANGE_WOOL;
    private final Material floor_type = Material.LIGHT_GRAY_WOOL;
    private final HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postSignBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
    private final HashMap<Block, TardisBannerData> postBannerBlocks = new HashMap<>();
    private final HashMap<String, Object> set = new HashMap<>();
    private final HashMap<String, Object> where = new HashMap<>();
    private Block postBedrock = null;
    private boolean running;
    private TardisTipsData pos;
    private JsonObject obj;
    private JsonArray arr;
    private int task, h, w, d, level = 0, row = 0, startX, startY, startZ, j = 2;
    private Location ender = null;
    private Material type;
    private BlockData data;
    private UseClay use_clay;
    private int counter = 0;
    private double div = 1.0d;
    private BossBar bossBar;

    /**
     * Builds the interior of an abandoned tardis.
     *
     * @param plugin    an instance of the tardis plugin main class.
     * @param schematic the name of the schematic file to use can be ars, BIGGER, BUDGET, CORAL, CUSTOM, DELUXE, ELEVENTH,
     *                  ENDER, MASTER, PYRAMID, REDSTONE, STEAMPUNK, THIRTEENTH, TOM, TWELFTH, WAR, WOOD, LEGACY_BUDGET,
     *                  LEGACY_BIGGER, LEGACY_DELUXE, LEGACY_ELEVENTH, LEGACY_REDSTONE or a CUSTOM name.
     * @param world     the world where the tardis is to be built.
     * @param dbId      the unique key of the record for this tardis in the database.
     * @param player    the player to show the progress bar to, may be null.
     */
    TardisBuildAbandoned(TardisPlugin plugin, Schematic schematic, World world, int dbId, Player player) {
        this.plugin = plugin;
        this.schematic = schematic;
        this.world = world;
        this.dbId = dbId;
        this.player = player;
    }

    @Override
    public void run() {
        if (!running) {
            if (schematic.getPermission().equals("redstone")) {
                startY = 65;
            } else {
                startY = 64;
            }
            String directory = (schematic.isCustom()) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + schematic.getPermission() + ".tschm";
            File file = new File(path);
            if (!file.exists()) {
                plugin.debug("Could not find a schematic with that name!");
                return;
            }
            // get JSON
            obj = TardisSchematicGZip.unzip(path);
            // get dimensions
            assert obj != null;
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            h = dimensions.get("height").getAsInt() - 1;
            w = dimensions.get("width").getAsInt();
            d = dimensions.get("length").getAsInt() - 1;
            div = (h + 1.0d) * w * (d + 1.0d);
            // calculate startX, startY, startZ
            TardisInteriorPositioning tintpos = new TardisInteriorPositioning(plugin);
            int slot = tintpos.getFreeSlot();
            pos = tintpos.getTipsData(slot);
            // save the slot
            set.put("tips", slot);
            startX = pos.getCentreX();
            startZ = pos.getCentreZ();
            // get the correct chunk for ARS
            Location cl = new Location(world, startX, startY, startZ);
            Chunk cars = world.getChunkAt(cl);
            String chun = world.getName() + ":" + cars.getX() + ":" + cars.getZ();
            set.put("chunk", chun);
            Location wg1 = new Location(world, startX, startY, startZ);
            // get list of used chunks
            List<Chunk> chunkList = TardisStaticUtils.getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, d);
            // update chunks list in DB
            chunkList.forEach((ch) -> {
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("tardis_id", dbId);
                setc.put("world", world.getName());
                setc.put("x", ch.getX());
                setc.put("z", ch.getZ());
                plugin.getQueryFactory().doInsert("chunks", setc);
            });
            where.put("tardis_id", dbId);
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
                bossBar = Bukkit.createBossBar(TardisConstants.GROWTH_STATES.get(0), BarColor.WHITE, BarStyle.SOLID, TardisConstants.EMPTY_ARRAY);
                bossBar.setProgress(0);
                bossBar.addPlayer(player);
                bossBar.setVisible(true);
            }
            running = true;
        }
        if (level == h && row == w - 1) {
            // put on the door, redstone torches, signs, and the repeaters
            postDoorBlocks.forEach(Block::setBlockData);
            postRedstoneTorchBlocks.forEach(Block::setBlockData);
            postTorchBlocks.forEach(Block::setBlockData);
            postRepeaterBlocks.forEach(Block::setBlockData);
            postStickyPistonBaseBlocks.forEach((pspb, value) -> {
                plugin.getGeneralKeeper().getDoorPistons().add(pspb);
                pspb.setBlockData(value);
            });
            postPistonBaseBlocks.forEach((ppb, value) -> {
                plugin.getGeneralKeeper().getDoorPistons().add(ppb);
                ppb.setBlockData(value);
            });
            postPistonExtensionBlocks.forEach(Block::setBlockData);
            postLeverBlocks.forEach(Block::setBlockData);
            int s = 0;
            for (Map.Entry<Block, BlockData> entry : postSignBlocks.entrySet()) {
                if (s == 0) {
                    // always make the control centre the first sign
                    Block psb = entry.getKey();
                    psb.setBlockData(entry.getValue());
                    if (Tag.WALL_SIGNS.isTagged(entry.getValue().getMaterial())) {
                        Sign cs = (Sign) psb.getState();
                        cs.setLine(0, "");
                        cs.setLine(1, plugin.getSigns().getStringList("control").get(0));
                        cs.setLine(2, plugin.getSigns().getStringList("control").get(1));
                        cs.setLine(3, "");
                        cs.update();
                        String controlloc = psb.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbId, 22, controlloc, 0);
                    }
                }
                s++;
            }
            if (postBedrock != null) {
                postBedrock.setBlockData(TardisConstants.POWER);
            }
            lampBlocks.forEach((lamp) -> {
                BlockData lantern = (schematic.hasLanterns()) ? TardisConstants.LANTERN : TardisConstants.LAMP;
                lamp.setBlockData(lantern);
            });
            lampBlocks.clear();
            TardisBannerSetter.setBanners(postBannerBlocks);
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                plugin.getWorldGuardUtils().addWGProtection(UUID.randomUUID().toString(), pos, world);
            }
            if (ender != null) {
                Entity ender_crystal = world.spawnEntity(ender, EntityType.ENDER_CRYSTAL);
                ((EnderCrystal) ender_crystal).setShowingBottom(false);
            }
            // finished processing - update tardis table!
            plugin.getQueryFactory().doUpdate("tardis", set, where);
            plugin.getServer().getScheduler().cancelTask(task);
            task = -1;
            if (player != null) {
                bossBar.setProgress(1);
                bossBar.setVisible(false);
                bossBar.removeAll();
            }
        }
        JsonArray floor = arr.get(level).getAsJsonArray();
        JsonArray r = (JsonArray) floor.get(row);
        // paste a column
        for (int col = 0; col <= d; col++) {
            counter++;
            JsonObject c = r.get(col).getAsJsonObject();
            int x = startX + row;
            int y = startY + level;
            int z = startZ + col;
            data = plugin.getServer().createBlockData(c.get("data").getAsString());
            type = data.getMaterial();
            if (type.equals(Material.NOTE_BLOCK)) {
                // remember the location of this Disk Storage
                String storage = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 14, storage, 0);
                // set block data to correct MUSHROOM_STEM
                data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
            }
            if (Tag.WOOL.isTagged(type)) {
                switch (type) {
                    case ORANGE_WOOL:
                        if (wall_type == Material.LAPIS_BLOCK) { // if using the default Lapis Block - then use Orange Wool / Terracotta
                            switch (use_clay) {
                                case TERRACOTTA -> data = Material.ORANGE_TERRACOTTA.createBlockData();
                                case CONCRETE -> data = Material.ORANGE_CONCRETE.createBlockData();
                                default -> data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(46));
                            }
                        } else {
                            data = wall_type.createBlockData();
                        }
                        break;
                    case LIGHT_GRAY_WOOL:
                        if (!schematic.getPermission().equals("eleventh")) {
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
                            data = Objects.requireNonNull(Material.getMaterial(m)).createBlockData();
                        }
                        break;
                    case BLUE_WOOL:
                        switch (use_clay) {
                            case TERRACOTTA -> data = Material.BLUE_TERRACOTTA.createBlockData();
                            case CONCRETE -> data = Material.BLUE_CONCRETE.createBlockData();
                            default -> data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(54));
                        }
                        break;
                    default:
                        String[] split = type.toString().split("_");
                        String m;
                        if (split.length > 2) {
                            m = split[0] + "_" + split[1] + "_" + use_clay.toString();
                        } else {
                            m = split[0] + "_" + use_clay.toString();
                        }
                        data = Objects.requireNonNull(Material.getMaterial(m)).createBlockData();
                }
            }
            if (type.equals(Material.WHITE_STAINED_GLASS) && schematic.getPermission().equals("war")) {
                data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(47));
            }
            if (type.equals(Material.WHITE_TERRACOTTA) && schematic.getPermission().equals("war")) {
                data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(48));
            }
            if (type.equals(Material.SPAWNER)) { // scanner button
                /*
                 * mob spawner will be converted to the correct id by
                 * setBlock(), but remember it for the scanner.
                 */
                String scanner = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 33, scanner, 0);
            }
            if (type.equals(Material.CHEST)) {
                // remember the location of the condenser chest
                String condenser = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 34, condenser, 0);
            }
            if (type.equals(Material.IRON_DOOR)) {
                Bisected bisected = (Bisected) data;
                if (bisected.getHalf().equals(Half.BOTTOM)) { // iron door bottom
                    HashMap<String, Object> setd = new HashMap<>();
                    String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    setd.put("tardis_id", dbId);
                    setd.put("door_type", 1);
                    setd.put("door_location", doorloc);
                    setd.put("door_direction", "SOUTH");
                    plugin.getQueryFactory().doInsert("doors", setd);
                }
            }
            if (type.equals(Material.STONE_BUTTON) && !schematic.getPermission().equals("junk")) { // random button
                // remember the location of this button
                String button = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 1, button, 0);
            }
            if (type.equals(Material.JUKEBOX)) {
                // remember the location of this Advanced Console
                String advanced = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 15, advanced, 0);
                // set block data to correct MUSHROOM_STEM
                data = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(50));
            }
            if (type.equals(Material.CAKE) && !schematic.getPermission().equals("junk")) {
                /*
                 * This block will be converted to a lever by
                 * setBlock(), but remember it so we can use it as the
                 * handbrake!
                 */
                String handbrakeloc = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 0, handbrakeloc, 0);
                // create default json for ARS
                String[][][] empty = new String[3][9][9];
                for (int ars_y = 0; ars_y < 3; ars_y++) {
                    for (int ars_x = 0; ars_x < 9; ars_x++) {
                        for (int ars_z = 0; ars_z < 9; ars_z++) {
                            empty[ars_y][ars_x][ars_z] = "STONE";
                        }
                    }
                }
                String control = schematic.getSeedMaterial().toString();
                empty[1][4][4] = control;
                switch (schematic.getConsoleSize()) {
                    case MASSIVE:
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
                        break;
                    case TALL:
                        // the 3 slots on the same level &
                        empty[1][4][5] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        // the 4 slots on the level above
                        empty[2][4][4] = control;
                        empty[2][4][5] = control;
                        empty[2][5][4] = control;
                        empty[2][5][5] = control;
                        break;
                    case MEDIUM:
                        // the 3 slots on the same level
                        empty[1][4][5] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        break;
                    default:
                        // SMALL size do nothing
                        break;
                }
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                JsonArray json = JsonParser.parseString(gson.toJson(empty)).getAsJsonArray();
                HashMap<String, Object> seta = new HashMap<>();
                seta.put("tardis_id", dbId);
                seta.put("json", json.toString());
                plugin.getQueryFactory().doInsert("ars", seta);
            }
            if (type.equals(Material.REDSTONE_LAMP) || type.equals(Material.SEA_LANTERN)) {
                // remember lamp blocks
                Block lamp = world.getBlockAt(x, y, z);
                lampBlocks.add(lamp);
                // remember lamp block locations for malfunction and light switch
                HashMap<String, Object> setlb = new HashMap<>();
                String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                setlb.put("tardis_id", dbId);
                setlb.put("location", lloc);
                plugin.getQueryFactory().doInsert("lamps", setlb);
            }
            if (type.equals(Material.COMMAND_BLOCK) || ((schematic.getPermission().equals("bigger") || schematic.getPermission().equals("coral") || schematic.getPermission().equals("deluxe") || schematic.getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                /*
                 * command block - remember it to spawn the creeper on.
                 * could also be a beacon block, as the creeper sits
                 * over the beacon in the deluxe and bigger consoles.
                 */
                String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                set.put("creeper", creeploc);
                if (type.equals(Material.COMMAND_BLOCK)) {
                    if (schematic.getPermission().equals("ender")) {
                        data = Material.END_STONE_BRICKS.createBlockData();
                    } else if (schematic.getPermission().equals("delta")) {
                        data = Material.BLACKSTONE.createBlockData();
                    } else {
                        data = Material.STONE_BRICKS.createBlockData();
                    }
                }
            }
            if (Tag.BUTTONS.isTagged(type) && !schematic.getPermission().equals("junk")) {
                /*
                 * wood button - remember it for the Artron Energy
                 * Capacitor.
                 */
                String woodbuttonloc = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 6, woodbuttonloc, 0);
            }
            if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                /*
                 * remember the telepathic circuit.
                 */
                String telepathicloc = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                plugin.getQueryFactory().insertSyncControl(dbId, 23, telepathicloc, 0);
            }
            if (type.equals(Material.BEACON) && schematic.getPermission().equals("ender")) {
                /*
                 * get the ender crystal location
                 */
                ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
            }
            // if it's an iron/gold/diamond/emerald/beacon/redstone/bedrock/conduit/netherite block put it in the blocks table
            if (TardisBuilderInstanceKeeper.getPrecious().contains(type)) {
                HashMap<String, Object> setpb = new HashMap<>();
                String loc = TardisStaticLocationGetters.makeLocationStr(world, x, y, z);
                setpb.put("tardis_id", dbId);
                setpb.put("location", loc);
                setpb.put("data", "minecraft:air");
                setpb.put("police_box", 0);
                plugin.getQueryFactory().doInsert("blocks", setpb);
                plugin.getGeneralKeeper().getProtectBlockMap().put(loc, dbId);
            }
            // if it's the door, don't set it just remember its block then do it at the end
            if (type.equals(Material.HONEYCOMB_BLOCK) && (schematic.getPermission().equals("delta") || schematic.getPermission().equals("rotor"))) {
                /*
                 * spawn an item frame and place the time rotor in it
                 */
                TardisBlockSetters.setBlock(world, x, y, z, (schematic.getPermission().equals("delta")) ? Material.POLISHED_BLACKSTONE_BRICKS : Material.STONE_BRICKS);
                TardisTimeRotor.setItemFrame(schematic.getPermission(), new Location(world, x, y + 1, z), dbId);
            } else if (type.equals(Material.IRON_DOOR)) { // doors
                postDoorBlocks.put(world.getBlockAt(x, y, z), data);
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
            } else if (type.equals(Material.OAK_WALL_SIGN)) {
                postSignBlocks.put(world.getBlockAt(x, y, z), data);
            } else if (TardisStaticUtils.isBanner(type)) {
                JsonObject state = c.has("banner") ? c.getAsJsonObject("banner") : null;
                if (state != null) {
                    TardisBannerData tbd = new TardisBannerData(data, state);
                    postBannerBlocks.put(world.getBlockAt(x, y, z), tbd);
                }
            } else if (TardisStaticUtils.isInfested(type)) {
                // legacy monster egg stone for controls
                TardisBlockSetters.setBlock(world, x, y, z, Material.AIR);
            } else if (type.equals(Material.MUSHROOM_STEM)) { // mushroom stem for repeaters
                // save repeater location
                if (j < 6) {
                    String repeater = world.getName() + ":" + x + ":" + y + ":" + z;
                    data = Material.REPEATER.createBlockData();
                    Directional directional = (Directional) data;
                    switch (j) {
                        case 2 -> {
                            directional.setFacing(BlockFace.WEST);
                            data = directional;
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                            plugin.getQueryFactory().insertSyncControl(dbId, 3, repeater, 0);
                        }
                        case 3 -> {
                            directional.setFacing(BlockFace.NORTH);
                            data = directional;
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                            plugin.getQueryFactory().insertSyncControl(dbId, 2, repeater, 0);
                        }
                        case 4 -> {
                            directional.setFacing(BlockFace.SOUTH);
                            data = directional;
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                            plugin.getQueryFactory().insertSyncControl(dbId, 5, repeater, 0);
                        }
                        default -> {
                            directional.setFacing(BlockFace.EAST);
                            data = directional;
                            postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                            plugin.getQueryFactory().insertSyncControl(dbId, 4, repeater, 0);
                        }
                    }
                    j++;
                }
            } else if (type.equals(Material.SPONGE)) {
                TardisBlockSetters.setBlock(world, x, y, z, Material.AIR);
            } else if (type.equals(Material.BEDROCK)) {
                // remember bedrock location to block off the beacon light
                String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
                set.put("beacon", bedrocloc);
                postBedrock = world.getBlockAt(x, y, z);
            } else if (type.equals(Material.BROWN_MUSHROOM) && schematic.getPermission().equals("master")) {
                // spawn locations for two villagers
                TardisBlockSetters.setBlock(world, x, y, z, Material.AIR);
                plugin.setTardisSpawn(true);
                world.spawnEntity(new Location(world, +0.5, y + 0.25, z + 0.5), EntityType.VILLAGER);
            } else {
                TardisBlockSetters.setBlock(world, x, y, z, data);
            }
            if (player != null) {
                double progress = counter / div;
                bossBar.setProgress(progress);
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
                        bossBar.setTitle(TardisConstants.GROWTH_STATES.get(31));
                    } else {
                        bossBar.setTitle(TardisConstants.GROWTH_STATES.get(level));
                    }
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
