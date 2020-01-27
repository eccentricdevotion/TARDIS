/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.enumeration.USE_CLAY;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.*;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted resources to malfunctioning controls to a
 * simple inability to arrive at the proper time or location. While the Doctor did not build the TARDIS from scratch, he
 * has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInner {

    private final TARDIS plugin;
    private final List<Block> lampblocks = new ArrayList<>();
    private Block postBedrock = null;

    public TARDISBuilderInner(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds the inside of the TARDIS.
     *
     * @param schm       the name of the schematic file to use can be BUDGET, BIGGER, CORAL, ELEVENTH, TWELFTH,
     *                   THIRTEENTH, REDSTONE, STEAMPUNK, DELUXE, PLANK, TOM, ARS, WAR, PYRAMID, MASTER or a CUSTOM
     *                   name.
     * @param world      the world where the TARDIS is to be built.
     * @param dbID       the unique key of the record for this TARDIS in the database.
     * @param p          an instance of the player who owns the TARDIS.
     * @param wall_type  a material type determined from the TARDIS seed block, or the middle block in the TARDIS
     *                   creation stack, this material determines the makeup of the TARDIS walls.
     * @param floor_type a material type determined from the TARDIS seed block, or 35 (if TARDIS was made via the
     *                   creation stack), this material determines the makeup of the TARDIS floors.
     * @param tips       a boolean determining where this TARDIS will be built -------- false:own world, underground -
     *                   true:default world--------
     */
    public void buildInner(SCHEMATIC schm, World world, int dbID, Player p, Material wall_type, Material floor_type, boolean tips) {
        Material type;
        int level, row, col, startx, startz, resetx, resetz, j = 2;
        boolean below = (!plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world"));
        int starty;
        if (below) {
            starty = 15;
        } else if (TARDISConstants.HIGHER.contains(schm.getPermission())) {
            starty = 65;
        } else {
            starty = 64;
        }
        String directory = (schm.isCustom()) ? "user_schematics" : "schematics";
        String path = plugin.getDataFolder() + File.separator + directory + File.separator + schm.getPermission() + ".tschm";
        File file = new File(path);
        if (!file.exists()) {
            plugin.debug("Could not find a schematic with that name!");
            return;
        }
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int h = dimensions.getInt("height");
        int w = dimensions.getInt("width");
        int l = dimensions.getInt("length");
        BlockData data;
        String playerUUID = p.getUniqueId().toString();
        HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
        HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
        HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
        HashMap<Block, BlockData> postSignBlocks = new HashMap<>();
        HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
        HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
        HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
        HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
        HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
        List<MushroomBlock> postMushroomBlocks = new ArrayList<>();
        HashMap<Block, TARDISBannerData> postBannerBlocks = new HashMap<>();
        Location ender = null;
        HashMap<String, Object> set = new HashMap<>();
        // calculate startx, starty, startz
        TARDISTIPSData pos = null;
        if (tips) { // default world - use TIPS
            TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
            int slot;
            if (schm.getPermission().equals("junk")) {
                slot = -999;
                pos = tintpos.getTIPSJunkData();
            } else {
                slot = tintpos.getFreeSlot();
                pos = tintpos.getTIPSData(slot);
            }
            // save the slot
            set.put("tips", slot);
            startx = pos.getCentreX();
            resetx = pos.getCentreX();
            startz = pos.getCentreZ();
            resetz = pos.getCentreZ();
            // get the correct chunk for ARS
            Location cl = new Location(world, startx, starty, startz);
            Chunk c = world.getChunkAt(cl);
            String chun = world.getName() + ":" + c.getX() + ":" + c.getZ();
            set.put("chunk", chun);
            if (schm.getPermission().equals("junk")) {
                set.put("creeper", cl.toString());
            }
        } else {
            int gsl[] = plugin.getLocationUtils().getStartLocation(dbID);
            startx = gsl[0];
            resetx = gsl[1];
            startz = gsl[2];
            resetz = gsl[3];
        }
        boolean own_world = plugin.getConfig().getBoolean("creation.create_worlds");
        Location wg1 = new Location(world, startx, starty, startz);
        Location wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (l - 1));
        // get list of used chunks
        List<Chunk> chunkList = getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, l);
        // update chunks list in DB
        chunkList.forEach((c) -> {
            HashMap<String, Object> setc = new HashMap<>();
            setc.put("tardis_id", dbID);
            setc.put("world", world.getName());
            setc.put("x", c.getX());
            setc.put("z", c.getZ());
            plugin.getQueryFactory().doInsert("chunks", setc);
        });
        // if for some reason this is not a TARDIS world, set the blocks to air first
        if (below) {
            for (level = 0; level < h; level++) {
                for (row = 0; row < w; row++) {
                    for (col = 0; col < l; col++) {
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, Material.AIR);
                        startx += 1;
                    }
                    startx = resetx;
                    startz += 1;
                }
                startz = resetz;
                starty += 1;
            }
            // reset start positions
            startx = resetx;
            starty = 15;
            startz = resetz;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", dbID);
        // get input array
        JSONArray arr = (JSONArray) obj.get("input");
        // loop like crazy
        for (level = 0; level < h; level++) {
            JSONArray floor = (JSONArray) arr.get(level);
            for (row = 0; row < w; row++) {
                JSONArray r = (JSONArray) floor.get(row);
                for (col = 0; col < l; col++) {
                    JSONObject c = (JSONObject) r.get(col);
                    int x = startx + row;
                    int y = starty + level;
                    int z = startz + col;
                    // if we're setting the biome to sky, do it now
                    if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0 && !below) {
                        world.setBiome(x, z, Biome.THE_VOID);
                    }
                    data = plugin.getServer().createBlockData(c.getString("data"));
                    type = data.getMaterial();
                    if (type.equals(Material.NOTE_BLOCK)) {
                        // remember the location of this Disk Storage
                        String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                        plugin.getQueryFactory().insertSyncControl(dbID, 14, storage, 0);
                    }
                    // determine 'use_clay' material
                    USE_CLAY use_clay;
                    try {
                        use_clay = USE_CLAY.valueOf(plugin.getConfig().getString("creation.use_clay"));
                    } catch (IllegalArgumentException e) {
                        use_clay = USE_CLAY.WOOL;
                    }
                    if (Tag.WOOL.isTagged(type)) {
                        switch (type) {
                            case ORANGE_WOOL:
                                if (wall_type == Material.ORANGE_WOOL) {
                                    switch (use_clay) {
                                        case TERRACOTTA:
                                            data = Material.ORANGE_TERRACOTTA.createBlockData();
                                            break;
                                        case CONCRETE:
                                            data = Material.ORANGE_CONCRETE.createBlockData();
                                            break;
                                        default:
                                            data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(46));
                                            postMushroomBlocks.add(new MushroomBlock(world.getBlockAt(x, y, z), data));
                                            break;
                                    }
                                } else {
                                    data = wall_type.createBlockData();
                                }
                                break;
                            case LIGHT_GRAY_WOOL:
                                if (!schm.getPermission().equals("eleventh")) {
                                    if (floor_type == Material.LIGHT_GRAY_WOOL) {
                                        switch (use_clay) {
                                            case TERRACOTTA:
                                                data = Material.LIGHT_GRAY_TERRACOTTA.createBlockData();
                                                break;
                                            case CONCRETE:
                                                data = Material.LIGHT_GRAY_CONCRETE.createBlockData();
                                                break;
                                            default:
                                                data = Material.LIGHT_GRAY_WOOL.createBlockData();
                                                break;
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
                                    data = Material.getMaterial(m).createBlockData();
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
                                data = Material.getMaterial(m).createBlockData();
                        }
                    }
                    if (type.equals(Material.WHITE_STAINED_GLASS)) {
                        data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(47));
                        postMushroomBlocks.add(new MushroomBlock(world.getBlockAt(x, y, z), data));
                    }
                    if (type.equals(Material.WHITE_TERRACOTTA)) {
                        data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(48));
                        postMushroomBlocks.add(new MushroomBlock(world.getBlockAt(x, y, z), data));
                    }
                    if (type.equals(Material.SPAWNER)) { // scanner button
                        /*
                         * mob spawner will be converted to the correct id by
                         * setBlock(), but remember it for the scanner.
                         */
                        String scanloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        set.put("scanner", scanloc);
                    }
                    if (type.equals(Material.CHEST)) {
                        // remember the location of the condenser chest
                        String chest = world.getName() + ":" + x + ":" + y + ":" + z;
                        set.put("condenser", chest);
                    }
                    if (type.equals(Material.IRON_DOOR)) {
                        Bisected bisected = (Bisected) data;
                        if (bisected.getHalf().equals(Bisected.Half.BOTTOM)) { // iron door bottom
                            HashMap<String, Object> setd = new HashMap<>();
                            String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
                            setd.put("tardis_id", dbID);
                            setd.put("door_type", 1);
                            setd.put("door_location", doorloc);
                            setd.put("door_direction", "SOUTH");
                            plugin.getQueryFactory().doInsert("doors", setd);
                            // if create_worlds is true, set the world spawn
                            if (own_world) {
                                world.setSpawnLocation(x, y, (z + 1));
                            }
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
                        // check if player has storage record, and update the tardis_id field
                        plugin.getUtils().updateStorageId(playerUUID, dbID);
                    }
                    if (type.equals(Material.CAKE) && !schm.getPermission().equals("junk")) {
                        /*
                         * This block will be converted to a lever by
                         * setBlock(), but remember it so we can use it as the
                         * handbrake!
                         */
                        String handbrakeloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                        plugin.getQueryFactory().insertSyncControl(dbID, 0, handbrakeloc, 0);
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
                        if (w > 16) {
                            empty[1][4][5] = control;
                            empty[1][5][4] = control;
                            empty[1][5][5] = control;
                            if (h > 16) {
                                empty[2][4][4] = control;
                                empty[2][4][5] = control;
                                empty[2][5][4] = control;
                                empty[2][5][5] = control;
                            }
                        } else if (h > 16) {
                            empty[2][4][4] = control;
                        }
                        JSONArray json = new JSONArray(empty);
                        HashMap<String, Object> seta = new HashMap<>();
                        seta.put("tardis_id", dbID);
                        seta.put("uuid", playerUUID);
                        seta.put("json", json.toString());
                        plugin.getQueryFactory().doInsert("ars", seta);
                    }
                    if (type.equals(Material.REDSTONE_LAMP) || type.equals(Material.SEA_LANTERN)) {
                        // remember lamp blocks
                        Block lamp = world.getBlockAt(x, y, z);
                        lampblocks.add(lamp);
                        // remember lamp block locations for malfunction and light switch
                        HashMap<String, Object> setlb = new HashMap<>();
                        String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        setlb.put("tardis_id", dbID);
                        setlb.put("location", lloc);
                        plugin.getQueryFactory().doInsert("lamps", setlb);
                    }
                    if (type.equals(Material.COMMAND_BLOCK) || ((schm.getPermission().equals("bigger") || schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                        /*
                         * command block - remember it to spawn the creeper on.
                         * could also be a beacon block, as the creeper sits
                         * over the beacon in the deluxe and bigger consoles.
                         */
                        String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                        set.put("creeper", creeploc);
                        if (type.equals(Material.COMMAND_BLOCK)) {
                            if (schm.getPermission().equals("ender")) {
                                data = Material.END_STONE_BRICKS.createBlockData();
                            } else {
                                data = Material.STONE_BRICKS.createBlockData();
                            }
                        }
                    }
                    if (Tag.WOODEN_BUTTONS.isTagged(type) && !schm.getPermission().equals("junk")) {
                        /*
                         * wood button - remember it for the Artron Energy
                         * Capacitor.
                         */
                        String woodbuttonloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                        plugin.getQueryFactory().insertSyncControl(dbID, 6, woodbuttonloc, 0);
                    }
                    if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                        /*
                         * remember the telepathic circuit.
                         */
                        String telepathicloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                        plugin.getQueryFactory().insertSyncControl(dbID, 23, telepathicloc, 0);
                    }
                    if (type.equals(Material.BEACON) && schm.getPermission().equals("ender")) {
                        /*
                         * get the ender crystal location
                         */
                        ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
                    }
                    // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
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
                    // if it's the door, don't set it just remember its block then do it at the end
                    if (type.equals(Material.IRON_DOOR)) { // doors
                        postDoorBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.REDSTONE_TORCH)) {
                        postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.TORCH)) {
                        postTorchBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.STICKY_PISTON)) {
                        postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.PISTON)) {
                        postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.PISTON_HEAD)) {
                        postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.LEVER)) {
                        postLeverBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (Tag.SIGNS.isTagged(type)) {
                        postSignBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (TARDISStaticUtils.isBanner(type)) {
                        JSONObject state = c.optJSONObject("banner");
                        if (state != null) {
                            TARDISBannerData tbd = new TARDISBannerData(data, state);
                            postBannerBlocks.put(world.getBlockAt(x, y, z), tbd);
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
                                case 2:
                                    directional.setFacing(BlockFace.EAST);
                                    data = directional;
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                    plugin.getQueryFactory().insertSyncControl(dbID, 3, repeater, 0);
                                    break;
                                case 3:
                                    directional.setFacing(BlockFace.SOUTH);
                                    data = directional;
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                    plugin.getQueryFactory().insertSyncControl(dbID, 2, repeater, 0);
                                    break;
                                case 4:
                                    directional.setFacing(BlockFace.NORTH);
                                    data = directional;
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                    plugin.getQueryFactory().insertSyncControl(dbID, 5, repeater, 0);
                                    break;
                                default:
                                    directional.setFacing(BlockFace.WEST);
                                    data = directional;
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                    plugin.getQueryFactory().insertSyncControl(dbID, 4, repeater, 0);
                                    break;
                            }
                            j++;
                        }
                    } else if (type.equals(Material.SPONGE)) {
                        Material swap;
                        String dn = plugin.getConfig().getString("creation.default_world_name");
                        if (world.getWorldType().equals(WorldType.FLAT) || own_world || world.getName().equals(dn) || world.getGenerator() instanceof TARDISChunkGenerator) {
                            swap = Material.AIR;
                        } else {
                            swap = Material.STONE;
                        }
                        TARDISBlockSetters.setBlock(world, x, y, z, swap);
                    } else if (type.equals(Material.BEDROCK)) {
                        // remember bedrock location to block off the beacon light
                        String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        set.put("beacon", bedrocloc);
                        postBedrock = world.getBlockAt(x, y, z);
                    } else if (type.equals(Material.BROWN_MUSHROOM) && schm.getPermission().equals("master")) {
                        // spawn locations for two villagers
                        TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                        plugin.setTardisSpawn(true);
                        world.spawnEntity(new Location(world, x + 0.5, y + 0.25, z + 0.5), EntityType.VILLAGER);
                    } else {
                        TARDISBlockSetters.setBlock(world, x, y, z, data);
                    }
                }
            }
        }
        // put on the door, redstone torches, signs, and the repeaters
        postDoorBlocks.forEach((pdb, value) -> pdb.setBlockData(value));
        postRedstoneTorchBlocks.forEach((prtb, value) -> prtb.setBlockData(value));
        postTorchBlocks.forEach((ptb, value) -> ptb.setBlockData(value));
        postRepeaterBlocks.forEach((prb, value) -> {
            prb.setBlockData(value);
        });
        postStickyPistonBaseBlocks.forEach((pspb, value) -> {
            plugin.getGeneralKeeper().getDoorPistons().add(pspb);
            pspb.setBlockData(value);
        });
        postPistonBaseBlocks.forEach((ppb, value) -> {
            plugin.getGeneralKeeper().getDoorPistons().add(ppb);
            ppb.setBlockData(value);
        });
        postPistonExtensionBlocks.forEach((ppeb, value) -> {
            ppeb.setBlockData(value);
        });
        postLeverBlocks.forEach((plb, value) -> {
            plb.setBlockData(value);
        });
        int s = 0;
        for (Map.Entry<Block, BlockData> entry : postSignBlocks.entrySet()) {
            if (s == 0) {
                // always make the control centre the first sign
                Block psb = entry.getKey();
                psb.setBlockData(entry.getValue());
                if (entry.getValue().getMaterial().equals(Material.OAK_WALL_SIGN)) {
                    Sign cs = (Sign) psb.getState();
                    cs.setLine(0, "");
                    cs.setLine(1, plugin.getSigns().getStringList("control").get(0));
                    cs.setLine(2, plugin.getSigns().getStringList("control").get(1));
                    cs.setLine(3, "");
                    cs.update();
                    String controlloc = psb.getLocation().toString();
                    plugin.getQueryFactory().insertSyncControl(dbID, 22, controlloc, 0);
                }
            }
            s++;
        }
        if (postBedrock != null) {
            postBedrock.setBlockData(TARDISConstants.POWER);
        }
        lampblocks.forEach((lamp) -> {
            BlockData lantern;
            if (schm.hasLanterns()) {
                lantern = TARDISConstants.LANTERN;
            } else {
                lantern = TARDISConstants.LAMP;
                ((Lightable) lantern).setLit(true);
            }
            lamp.setBlockData(lantern);
        });
        lampblocks.clear();
        TARDISBannerSetter.setBanners(postBannerBlocks);
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            if (tips) {
                if (pos != null) {
                    String name = (schm.getPermission().equals("junk")) ? "junk" : p.getName();
                    plugin.getWorldGuardUtils().addWGProtection(name, pos, world);
                }
            } else {
                plugin.getWorldGuardUtils().addWGProtection(p, wg1, wg2);
            }
        }
        if (ender != null) {
            Entity ender_crystal = world.spawnEntity(ender, EntityType.ENDER_CRYSTAL);
            ((EnderCrystal) ender_crystal).setShowingBottom(false);
        }
        if (obj.has("paintings")) {
            JSONArray paintings = (JSONArray) obj.get("paintings");
            for (int i = 0; i < paintings.length(); i++) {
                JSONObject painting = paintings.getJSONObject(i);
                JSONObject rel = painting.getJSONObject("rel_location");
                int px = rel.getInt("x");
                int py = rel.getInt("y");
                int pz = rel.getInt("z");
                Art art = Art.valueOf(painting.getString("art"));
                BlockFace facing = BlockFace.valueOf(painting.getString("facing"));
                Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, resetx + px, starty + py, resetz + pz));
                Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                ent.teleport(pl);
                ent.setFacingDirection(facing, true);
                ent.setArt(art, true);
            }
        }
        // reset mushroom stem blocks
        if (postMushroomBlocks.size() > 0) {
            TARDISMushroomRunnable runnable = new TARDISMushroomRunnable(plugin, postMushroomBlocks);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 400L, 1L);
            runnable.setTask(taskID);
        }
        // remove dropped items
        chunkList.forEach((chink) -> {
            for (Entity e : chink.getEntities()) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
        });
        // finished processing - update tardis table!
        plugin.getQueryFactory().doUpdate("tardis", set, where);
        // give kit?
        if (plugin.getKitsConfig().getBoolean("give.create.enabled")) {
            if (p.hasPermission("tardis.kit.create")) {
                // check if they have the tardis kit
                HashMap<String, Object> wherek = new HashMap<>();
                wherek.put("uuid", playerUUID);
                wherek.put("name", "createkit");
                ResultSetAchievements rsa = new ResultSetAchievements(plugin, wherek, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> setk = new HashMap<>();
                    setk.put("uuid", playerUUID);
                    setk.put("name", "createkit");
                    plugin.getQueryFactory().doInsert("achievements", setk);
                    // give the join kit
                    String kit = plugin.getKitsConfig().getString("give.create.kit");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisgive " + p.getName() + " kit " + kit);
                }
            }
        }
    }

    /**
     * Checks whether a chunk is available to build a TARDIS in.
     *
     * @param w   the world the chunk is in.
     * @param x   the x coordinate of the chunk.
     * @param z   the z coordinate of the chunk.
     * @param wid the width of the schematic.
     * @param len the length of the schematic.
     * @return a list of Chunks.
     */
    public List<Chunk> getChunks(World w, int x, int z, int wid, int len) {
        List<Chunk> chunks = new ArrayList<>();
        int cw = TARDISNumberParsers.roundUp(wid, 16);
        int cl = TARDISNumberParsers.roundUp(len, 16);
        // check all the chunks that will be used by the schematic
        for (int cx = 0; cx < cw; cx++) {
            for (int cz = 0; cz < cl; cz++) {
                Chunk chunk = w.getChunkAt((x + cx), (z + cz));
                chunks.add(chunk);
            }
        }
        return chunks;
    }
}
