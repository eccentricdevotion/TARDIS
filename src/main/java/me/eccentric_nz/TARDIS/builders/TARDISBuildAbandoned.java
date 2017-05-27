/*
 * Copyright (C) 2016 eccentric_nz
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted
 * resources to malfunctioning controls to a simple inability to arrive at the
 * proper time or location. While the Doctor did not build the TARDIS from
 * scratch, he has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
public class TARDISBuildAbandoned {

    private final TARDIS plugin;
    List<Block> lampblocks = new ArrayList<>();
    private Block postBedrock = null;

    public TARDISBuildAbandoned(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void buildOuter(BuildData bd, PRESET preset) {
        plugin.getTrackerKeeper().getMaterialising().add(bd.getTardisID());
        TARDISMaterialisationPreset runnable = new TARDISMaterialisationPreset(plugin, bd, preset, 35, (byte) 11, 3);
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
        runnable.setTask(taskID);
        TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land_fast");
    }

    /**
     * Builds the inside of the TARDIS.
     *
     * @param schm the name of the schematic file to use can be BUDGET, BIGGER,
     * ELEVENTH, TWELFTH, REDSTONE, STEAMPUNK, DELUXE, PLANK, TOM, ARS, WAR,
     * PYRAMID, MASTER or a CUSTOM name.
     * @param world the world where the TARDIS is to be built.
     * @param dbID the unique key of the record for this TARDIS in the database.
     * @param wall_type a material type determined from the TARDIS seed block,
     * or the middle block in the TARDIS creation stack, this material
     * determines the makeup of the TARDIS walls.
     * @param wall_data the data bit associated with the wall_type parameter.
     * @param floor_type a material type determined from the TARDIS seed block,
     * or 35 (if TARDIS was made via the creation stack), this material
     * determines the makeup of the TARDIS floors.
     * @param floor_data the data bit associated with the floor_id parameter.
     */
    @SuppressWarnings("deprecation")
    public void buildInner(SCHEMATIC schm, World world, int dbID, Material wall_type, byte wall_data, Material floor_type, byte floor_data) {
        Material type;
        int level, row, col, startx, starty, startz, j = 2;
        if (schm.getPermission().equals("redstone")) {
            starty = 65;
        } else {
            starty = 64;
        }
        String directory = (schm.isCustom()) ? "user_schematics" : "schematics";
        String path = plugin.getDataFolder() + File.separator + directory + File.separator + schm.getPermission() + ".tschm";
        File file = new File(path);
        if (!file.exists()) {
            plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
            return;
        }
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int h = dimensions.getInt("height");
        int w = dimensions.getInt("width");
        int l = dimensions.getInt("length");
        byte data;
        HashMap<Block, Byte> postDoorBlocks = new HashMap<>();
        HashMap<Block, Byte> postRedstoneTorchBlocks = new HashMap<>();
        HashMap<Block, Byte> postTorchBlocks = new HashMap<>();
        HashMap<Block, Byte> postSignBlocks = new HashMap<>();
        HashMap<Block, Byte> postRepeaterBlocks = new HashMap<>();
        HashMap<Block, Byte> postPistonBaseBlocks = new HashMap<>();
        HashMap<Block, Byte> postStickyPistonBaseBlocks = new HashMap<>();
        HashMap<Block, Byte> postPistonExtensionBlocks = new HashMap<>();
        HashMap<Block, Byte> postLeverBlocks = new HashMap<>();
        HashMap<Block, JSONObject> postStandingBanners = new HashMap<>();
        HashMap<Block, JSONObject> postWallBanners = new HashMap<>();
        Location ender = null;
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<>();
        // calculate startx, starty, startz
        TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
        int slot = tintpos.getFreeSlot();
        TARDISTIPSData pos = tintpos.getTIPSData(slot);
        // save the slot
        set.put("tips", slot);
        startx = pos.getCentreX();
        startz = pos.getCentreZ();
        // get the correct chunk for ARS
        Location cl = new Location(world, startx, starty, startz);
        Chunk cars = world.getChunkAt(cl);
        String chun = world.getName() + ":" + cars.getX() + ":" + cars.getZ();
        set.put("chunk", chun);
        Location wg1 = new Location(world, startx, starty, startz);
        // get list of used chunks
        List<Chunk> chunkList = getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, l);
        // update chunks list in DB
        for (Chunk ch : chunkList) {
            HashMap<String, Object> setc = new HashMap<>();
            setc.put("tardis_id", dbID);
            setc.put("world", world.getName());
            setc.put("x", ch.getX());
            setc.put("z", ch.getZ());
            qf.doInsert("chunks", setc);
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
                    if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                        world.setBiome(x, z, Biome.VOID);
                    }
                    type = Material.valueOf((String) c.get("type"));
                    data = c.getByte("data");
                    if (type.equals(Material.NOTE_BLOCK)) {
                        // remember the location of this Disk Storage
                        String storage = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        qf.insertSyncControl(dbID, 14, storage, 0);
                    }
                    if (type.equals(Material.WOOL)) {
                        switch (data) {
                            case 1:
                                switch (wall_type) {
                                    case LAPIS_BLOCK: // if using the default Lapis Block - then use Orange Wool / Stained Clay
                                        if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                            type = Material.STAINED_CLAY;
                                        }
                                        break;
                                    default:
                                        type = wall_type;
                                        data = wall_data;
                                }
                                break;
                            case 8:
                                if (!schm.getPermission().equals("eleventh")) {
                                    switch (floor_type) {
                                        case LAPIS_BLOCK: // if using the default Lapis Block - then use Light Grey Wool / Stained Clay
                                            if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                                type = Material.STAINED_CLAY;
                                            }
                                            break;
                                        default:
                                            type = floor_type;
                                            data = floor_data;
                                    }
                                } else if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                    type = Material.STAINED_CLAY;
                                }
                                break;
                            default:
                                if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                    type = Material.STAINED_CLAY;
                                }
                                break;
                        }
                    }
                    if (type.equals(Material.MOB_SPAWNER)) { // scanner button
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
                    if (type.equals(Material.IRON_DOOR_BLOCK)) {
                        if (data < (byte) 8) { // iron door bottom
                            HashMap<String, Object> setd = new HashMap<>();
                            String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
                            setd.put("tardis_id", dbID);
                            setd.put("door_type", 1);
                            setd.put("door_location", doorloc);
                            setd.put("door_direction", "SOUTH");
                            qf.doInsert("doors", setd);
                        } else {
                            // iron door top
                            data = (byte) 9;
                        }
                    }
                    if (type.equals(Material.STONE_BUTTON) && !schm.getPermission().equals("junk")) { // random button
                        // remember the location of this button
                        String button = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        qf.insertSyncControl(dbID, 1, button, 0);
                    }
                    if (type.equals(Material.JUKEBOX)) {
                        // remember the location of this Advanced Console
                        String advanced = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        qf.insertSyncControl(dbID, 15, advanced, 0);
                    }
                    if (type.equals(Material.CAKE_BLOCK) && !schm.getPermission().equals("junk")) {
                        /*
                         * This block will be converted to a lever by
                         * setBlock(), but remember it so we can use it as the
                         * handbrake!
                         */
                        String handbrakeloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        qf.insertSyncControl(dbID, 0, handbrakeloc, 0);
                    }
                    if (type.equals(Material.MONSTER_EGGS) && !schm.getPermission().equals("junk")) {
                        // silverfish stone
                        if (data == 2) {
                            // create default json
                            int[][][] empty = new int[3][9][9];
                            for (int ars_y = 0; ars_y < 3; ars_y++) {
                                for (int ars_x = 0; ars_x < 9; ars_x++) {
                                    for (int ars_z = 0; ars_z < 9; ars_z++) {
                                        empty[ars_y][ars_x][ars_z] = 1;
                                    }
                                }
                            }
                            int control = schm.getSeedId();
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
                            seta.put("json", json.toString());
                            qf.doInsert("ars", seta);
                        }
                    }
                    if (type.equals(Material.REDSTONE_LAMP_ON) || type.equals(Material.SEA_LANTERN)) {
                        // remember lamp blocks
                        Block lamp = world.getBlockAt(x, y, z);
                        lampblocks.add(lamp);
                        // remember lamp block locations for malfunction and light switch
                        HashMap<String, Object> setlb = new HashMap<>();
                        String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        setlb.put("tardis_id", dbID);
                        setlb.put("location", lloc);
                        qf.doInsert("lamps", setlb);
                    }
                    if (type.equals(Material.COMMAND) || ((schm.getPermission().equals("bigger") || schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                        /*
                         * command block - remember it to spawn the creeper on.
                         * could also be a beacon block, as the creeper sits
                         * over the beacon in the deluxe and bigger consoles.
                         */
                        String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                        set.put("creeper", creeploc);
                        if (schm.getPermission().equals("bigger") || schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) {
                            type = Material.BEACON;
                        } else {
                            type = Material.SMOOTH_BRICK;
                            if (schm.getPermission().equals("ender")) {
                                data = (byte) 3;
                            }
                        }
                    }
                    if (type.equals(Material.WOOD_BUTTON) && !schm.getPermission().equals("junk")) {
                        /*
                         * wood button - remember it for the Artron Energy
                         * Capacitor.
                         */
                        String woodbuttonloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        qf.insertSyncControl(dbID, 6, woodbuttonloc, 0);
                    }
                    if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                        /*
                         * remember the telepathic circuit.
                         */
                        String telepathicloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        qf.insertSyncControl(dbID, 23, telepathicloc, 0);
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
                        String loc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        setpb.put("tardis_id", dbID);
                        setpb.put("location", loc);
                        setpb.put("police_box", 0);
                        qf.doInsert("blocks", setpb);
                        plugin.getGeneralKeeper().getProtectBlockMap().put(loc, dbID);
                    }
                    // if it's the door, don't set it just remember its block then do it at the end
                    if (type.equals(Material.IRON_DOOR_BLOCK)) { // doors
                        postDoorBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.REDSTONE_TORCH_ON)) {
                        postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.TORCH)) {
                        postTorchBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.PISTON_STICKY_BASE)) {
                        postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.PISTON_BASE)) {
                        postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.PISTON_EXTENSION)) {
                        postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.LEVER)) {
                        postLeverBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.WALL_SIGN)) {
                        postSignBlocks.put(world.getBlockAt(x, y, z), data);
                    } else if (type.equals(Material.STANDING_BANNER) || type.equals(Material.WALL_BANNER)) {
                        JSONObject state = c.optJSONObject("banner");
                        if (state != null) {
                            if (type.equals(Material.STANDING_BANNER)) {
                                postStandingBanners.put(world.getBlockAt(x, y, z), state);
                            } else {
                                postWallBanners.put(world.getBlockAt(x, y, z), state);
                            }
                        }
                    } else if (type.equals(Material.MONSTER_EGGS)) {
                        // legacy monster egg stone for controls
                        TARDISBlockSetters.setBlock(world, x, y, z, 0, (byte) 0);
                    } else if (type.equals(Material.HUGE_MUSHROOM_2) && data == 15) { // mushroom stem for repeaters
                        // save repeater location
                        if (j < 6) {
                            String repeater = world.getName() + ":" + x + ":" + y + ":" + z;
                            switch (j) {
                                case 2:
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), (byte) 1);
                                    qf.insertSyncControl(dbID, 3, repeater, 0);
                                    break;
                                case 3:
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), (byte) 2);
                                    qf.insertSyncControl(dbID, 2, repeater, 0);
                                    break;
                                case 4:
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), (byte) 0);
                                    qf.insertSyncControl(dbID, 5, repeater, 0);
                                    break;
                                default:
                                    postRepeaterBlocks.put(world.getBlockAt(x, y, z), (byte) 3);
                                    qf.insertSyncControl(dbID, 4, repeater, 0);
                                    break;
                            }
                            j++;
                        }
                    } else if (type.equals(Material.SPONGE)) {
                        TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR, data);
                    } else if (type.equals(Material.BEDROCK)) {
                        // remember bedrock location to block off the beacon light
                        String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        set.put("beacon", bedrocloc);
                        postBedrock = world.getBlockAt(x, y, z);
                    } else {
                        TARDISBlockSetters.setBlock(world, x, y, z, type, data);
                    }
                }
            }
        }
        // put on the door, redstone torches, signs, and the repeaters
        postDoorBlocks.entrySet().forEach((entry) -> {
            Block pdb = entry.getKey();
            byte pddata = entry.getValue();
            pdb.setType(Material.IRON_DOOR_BLOCK);
            pdb.setData(pddata, true);
        });
        postRedstoneTorchBlocks.entrySet().forEach((entry) -> {
            Block prtb = entry.getKey();
            byte ptdata = entry.getValue();
            prtb.setTypeIdAndData(76, ptdata, true);
        });
        postTorchBlocks.entrySet().forEach((entry) -> {
            Block ptb = entry.getKey();
            byte ptdata = entry.getValue();
            ptb.setTypeIdAndData(50, ptdata, true);
        });
        postRepeaterBlocks.entrySet().forEach((entry) -> {
            Block prb = entry.getKey();
            byte ptdata = entry.getValue();
            prb.setType(Material.DIODE_BLOCK_OFF);
            prb.setData(ptdata, true);
        });
        postStickyPistonBaseBlocks.entrySet().forEach((entry) -> {
            Block pspb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(pspb);
            byte pspdata = entry.getValue();
            pspb.setType(Material.PISTON_STICKY_BASE);
            pspb.setData(pspdata, true);
        });
        postPistonBaseBlocks.entrySet().forEach((entry) -> {
            Block ppb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(ppb);
            byte ppbdata = entry.getValue();
            ppb.setType(Material.PISTON_BASE);
            ppb.setData(ppbdata, true);
        });
        postPistonExtensionBlocks.entrySet().forEach((entry) -> {
            Block ppeb = entry.getKey();
            byte ppedata = entry.getValue();
            ppeb.setType(Material.PISTON_EXTENSION);
            ppeb.setData(ppedata, true);
        });
        postLeverBlocks.entrySet().forEach((entry) -> {
            Block plb = entry.getKey();
            byte pldata = entry.getValue();
            plb.setType(Material.LEVER);
            plb.setData(pldata, true);
        });
        int s = 0;
        for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
            if (s == 0) {
                // always make the control centre the first sign
                final Block psb = entry.getKey();
                byte psdata = entry.getValue();
                psb.setType(Material.WALL_SIGN);
                psb.setData(psdata, true);
                if (psb.getType().equals(Material.WALL_SIGN)) {
                    Sign cs = (Sign) psb.getState();
                    cs.setLine(0, "");
                    cs.setLine(1, plugin.getSigns().getStringList("control").get(0));
                    cs.setLine(2, plugin.getSigns().getStringList("control").get(1));
                    cs.setLine(3, "");
                    cs.update();
                    String controlloc = psb.getLocation().toString();
                    qf.insertSyncControl(dbID, 22, controlloc, 0);
                }
            }
            s++;
        }
        if (postBedrock != null) {
            postBedrock.setType(Material.REDSTONE_BLOCK);
        }
        lampblocks.forEach((lamp) -> {
            Material lantern = (schm.hasLanterns()) ? Material.SEA_LANTERN : Material.REDSTONE_LAMP_ON;
            lamp.setType(lantern);
        });
        lampblocks.clear();
        setBanners(176, postStandingBanners);
        setBanners(177, postWallBanners);
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            plugin.getWorldGuardUtils().addWGProtection(UUID.randomUUID().toString(), pos, world);
        }
        if (ender != null) {
            Entity ender_crystal = world.spawnEntity(ender, EntityType.ENDER_CRYSTAL);
            ((EnderCrystal) ender_crystal).setShowingBottom(false);
        }
        // finished processing - update tardis table!
        qf.doUpdate("tardis", set, where);
    }

    /**
     * Checks whether a chunk is available to build a TARDIS in.
     *
     * @param w the world the chunk is in.
     * @param x the x coordinate of the chunk.
     * @param z the z coordinate of the chunk.
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
