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
package me.eccentric_nz.TARDIS.builders;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted
 * resources to malfunctioning controls to a simple inability to arrive at the
 * proper time or location. While the Doctor did not build the TARDIS from
 * scratch, he has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInner {

    private final TARDIS plugin;
    List<Block> lampblocks = new ArrayList<Block>();
    List<Material> precious = new ArrayList<Material>();
    private Block postBedrock = null;

    public TARDISBuilderInner(TARDIS plugin) {
        this.plugin = plugin;
        this.precious.add(Material.BEACON);
        this.precious.add(Material.DIAMOND_BLOCK);
        this.precious.add(Material.EMERALD_BLOCK);
        this.precious.add(Material.GOLD_BLOCK);
        this.precious.add(Material.IRON_BLOCK);
        this.precious.add(Material.REDSTONE_BLOCK);
        this.precious.add(Material.BEDROCK);
    }

    /**
     * Builds the inside of the TARDIS.
     *
     * @param schm the name of the schematic file to use can be BUDGET, BIGGER,
     * ELEVENTH, TWELFTH, REDSTONE, STEAMPUNK, DELUXE, PLANK, TOM, ARS, WAR,
     * PYRAMID, MASTER or a CUSTOM name.
     * @param world the world where the TARDIS is to be built.
     * @param dbID the unique key of the record for this TARDIS in the database.
     * @param p an instance of the player who owns the TARDIS.
     * @param wall_type a material type determined from the TARDIS seed block,
     * or the middle block in the TARDIS creation stack, this material
     * determines the makeup of the TARDIS walls.
     * @param wall_data the data bit associated with the wall_type parameter.
     * @param floor_type a material type determined from the TARDIS seed block,
     * or 35 (if TARDIS was made via the creation stack), this material
     * determines the makeup of the TARDIS floors.
     * @param floor_data the data bit associated with the floor_id parameter.
     * @param tips a boolean determining where this TARDIS will be built
     * -------- false:own world, underground - true:default world--------
     */
    @SuppressWarnings("deprecation")
    public void buildInner(SCHEMATIC schm, World world, int dbID, Player p, Material wall_type, byte wall_data, Material floor_type, byte floor_data, boolean tips) {
        Material type;
        int level, row, col, startx, startz, resetx, resetz, j = 2;
        boolean below = (!plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world"));
        int starty;
        if (below) {
            starty = 15;
        } else if (schm.getPermission().equals("redstone")) {
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
        String playerUUID = p.getUniqueId().toString();
        HashMap<Block, Byte> postDoorBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postRedstoneTorchBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postTorchBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postSignBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postRepeaterBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postPistonBaseBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postStickyPistonBaseBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postPistonExtensionBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postLeverBlocks = new HashMap<Block, Byte>();
        HashMap<Block, JSONObject> postStandingBanners = new HashMap<Block, JSONObject>();
        HashMap<Block, JSONObject> postWallBanners = new HashMap<Block, JSONObject>();
        Block postSaveSignBlock = null;
        Block postTerminalBlock = null;
        Block postARSBlock = null;
        Block postTISBlock = null;
        Block postTemporalBlock = null;
        Block postKeyboardBlock = null;
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<String, Object>();
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
        for (Chunk c : chunkList) {
            HashMap<String, Object> setc = new HashMap<String, Object>();
            setc.put("tardis_id", dbID);
            setc.put("world", world.getName());
            setc.put("x", c.getX());
            setc.put("z", c.getZ());
            qf.doInsert("chunks", setc);
        }
        // if for some reason this is not a TARDIS world, set the blocks to air first
        if (below) {
            for (level = 0; level < h; level++) {
                for (row = 0; row < w; row++) {
                    for (col = 0; col < l; col++) {
                        TARDISBlockSetters.setBlock(world, startx, starty, startz, 0, (byte) 0);
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
        HashMap<String, Object> where = new HashMap<String, Object>();
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
                        world.setBiome(x, z, Biome.SKY);
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
                            HashMap<String, Object> setd = new HashMap<String, Object>();
                            String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
                            setd.put("tardis_id", dbID);
                            setd.put("door_type", 1);
                            setd.put("door_location", doorloc);
                            setd.put("door_direction", "SOUTH");
                            qf.doInsert("doors", setd);
                            // if create_worlds is true, set the world spawn
                            if (own_world) {
                                if (plugin.isMVOnServer()) {
                                    plugin.getMVHelper().setSpawnLocation(world, x, y, z);
                                } else {
                                    world.setSpawnLocation(x, y, (z + 1));
                                }
                            }
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
                        // check if player has storage record, and update the tardis_id field
                        plugin.getUtils().updateStorageId(playerUUID, dbID, qf);
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
                    if (type.equals(Material.MONSTER_EGGS) && !schm.getPermission().equals("junk")) { // silverfish stone
                        String blockLocStr = (new Location(world, x, y, z)).toString();
                        switch (data) {
                            case 0: // Save Sign
                                String save_loc = world.getName() + ":" + x + ":" + y + ":" + z;
                                set.put("save_sign", save_loc);
                                break;
                            case 1: // Destination Terminal
                                qf.insertSyncControl(dbID, 9, blockLocStr, 0);
                                break;
                            case 2: // Architectural Reconfiguration System
                                qf.insertSyncControl(dbID, 10, blockLocStr, 0);
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
                                HashMap<String, Object> seta = new HashMap<String, Object>();
                                seta.put("tardis_id", dbID);
                                seta.put("uuid", playerUUID);
                                seta.put("json", json.toString());
                                qf.doInsert("ars", seta);
                                break;
                            case 3: // TARDIS Information System
                                qf.insertSyncControl(dbID, 13, blockLocStr, 0);
                                break;
                            case 4: // Temporal Circuit
                                qf.insertSyncControl(dbID, 11, blockLocStr, 0);
                                break;
                            case 5: // Keyboard
                                qf.insertSyncControl(dbID, 7, blockLocStr, 0);
                                break;
                            default:
                                break;
                        }
                    }
                    if (type.equals(Material.REDSTONE_LAMP_ON) || type.equals(Material.SEA_LANTERN)) {
                        // remember lamp blocks
                        Block lamp = world.getBlockAt(x, y, z);
                        lampblocks.add(lamp);
                        if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
                            // remember lamp block locations for malfunction
                            HashMap<String, Object> setlb = new HashMap<String, Object>();
                            String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                            setlb.put("tardis_id", dbID);
                            setlb.put("location", lloc);
                            qf.doInsert("lamps", setlb);
                        }
                    }
                    if (type.equals(Material.COMMAND) || ((schm.getPermission().equals("bigger") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                        /*
                         * command block - remember it to spawn the creeper on.
                         * could also be a beacon block, as the creeper sits
                         * over the beacon in the deluxe and bigger consoles.
                         */
                        String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                        set.put("creeper", creeploc);
                        type = (schm.getPermission().equals("bigger") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("twelfth")) ? Material.BEACON : Material.SMOOTH_BRICK;
                    }
                    if (type.equals(Material.WOOD_BUTTON) && !schm.getPermission().equals("junk")) {
                        /*
                         * wood button - remember it for the Artron Energy
                         * Capacitor.
                         */
                        String woodbuttonloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                        qf.insertSyncControl(dbID, 6, woodbuttonloc, 0);
                    }
                    // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                    if (precious.contains(type)) {
                        HashMap<String, Object> setpb = new HashMap<String, Object>();
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
                    } else if (type.equals(Material.MONSTER_EGGS)) { // monster egg stone for controls
                        switch (data) {
                            case 0:
                                postSaveSignBlock = world.getBlockAt(x, y, z);
                                break;
                            case 1:
                                postTerminalBlock = world.getBlockAt(x, y, z);
                                break;
                            case 2:
                                postARSBlock = world.getBlockAt(x, y, z);
                                break;
                            case 3:
                                postTISBlock = world.getBlockAt(x, y, z);
                                break;
                            case 4:
                                postTemporalBlock = world.getBlockAt(x, y, z);
                                break;
                            case 5:
                                postKeyboardBlock = world.getBlockAt(x, y, z);
                                break;
                            default:
                                break;
                        }
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
                        Material swap;
                        if (world.getWorldType().equals(WorldType.FLAT) || own_world || world.getName().equals("TARDIS_TimeVortex") || world.getGenerator() instanceof TARDISChunkGenerator) {
                            swap = Material.AIR;
                        } else {
                            swap = Material.STONE;
                        }
                        TARDISBlockSetters.setBlock(world, x, y, z, swap, data);
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
        for (Map.Entry<Block, Byte> entry : postDoorBlocks.entrySet()) {
            Block pdb = entry.getKey();
            byte pddata = entry.getValue();
            pdb.setType(Material.IRON_DOOR_BLOCK);
            pdb.setData(pddata, true);
        }
        for (Map.Entry<Block, Byte> entry : postRedstoneTorchBlocks.entrySet()) {
            Block prtb = entry.getKey();
            byte ptdata = entry.getValue();
            prtb.setTypeIdAndData(76, ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postTorchBlocks.entrySet()) {
            Block ptb = entry.getKey();
            byte ptdata = entry.getValue();
            ptb.setTypeIdAndData(50, ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postRepeaterBlocks.entrySet()) {
            Block prb = entry.getKey();
            byte ptdata = entry.getValue();
            prb.setType(Material.DIODE_BLOCK_OFF);
            prb.setData(ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postStickyPistonBaseBlocks.entrySet()) {
            Block pspb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(pspb);
            byte pspdata = entry.getValue();
            pspb.setType(Material.PISTON_STICKY_BASE);
            pspb.setData(pspdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postPistonBaseBlocks.entrySet()) {
            Block ppb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(ppb);
            byte ppbdata = entry.getValue();
            ppb.setType(Material.PISTON_BASE);
            ppb.setData(ppbdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postPistonExtensionBlocks.entrySet()) {
            Block ppeb = entry.getKey();
            byte ppedata = entry.getValue();
            ppeb.setType(Material.PISTON_EXTENSION);
            ppeb.setData(ppedata, true);
        }
        for (Map.Entry<Block, Byte> entry : postLeverBlocks.entrySet()) {
            Block plb = entry.getKey();
            byte pldata = entry.getValue();
            plb.setType(Material.LEVER);
            plb.setData(pldata, true);
        }
        int s = 0;
        for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
            final Block psb = entry.getKey();
            byte psdata = entry.getValue();
            psb.setType(Material.WALL_SIGN);
            psb.setData(psdata, true);
            if (psb.getType().equals(Material.WALL_SIGN)) {
                Sign cs = (Sign) psb.getState();
                if (s > 0) {
                    cs.setLine(1, plugin.getSigns().getStringList("control").get(0));
                    cs.setLine(2, plugin.getSigns().getStringList("control").get(1));
                    String controlloc = psb.getLocation().toString();
                    qf.insertSyncControl(dbID, 22, controlloc, 0);
                } else {
                    cs.setLine(0, plugin.getSigns().getStringList("chameleon").get(0));
                    cs.setLine(1, plugin.getSigns().getStringList("chameleon").get(1));
                    cs.setLine(2, ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
                    cs.setLine(3, "NEW");
                    String chameleonloc = world.getName() + ":" + psb.getLocation().getBlockX() + ":" + psb.getLocation().getBlockY() + ":" + psb.getLocation().getBlockZ();
                    set.put("chameleon", chameleonloc);
                    set.put("chamele_on", 0);
                }
                cs.update();
            }
            s++;
        }
        if (postBedrock != null) {
            postBedrock.setType(Material.REDSTONE_BLOCK);
        }
        if (postSaveSignBlock != null) {
            postSaveSignBlock.setType(Material.WALL_SIGN);
            postSaveSignBlock.setData((byte) 3, true);
            if (postSaveSignBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ss = (Sign) postSaveSignBlock.getState();
                ss.setLine(0, "TARDIS");
                ss.setLine(1, plugin.getSigns().getStringList("saves").get(0));
                ss.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                ss.setLine(3, "");
                ss.update();
            }
        }
        if (postTerminalBlock != null) {
            postTerminalBlock.setType(Material.WALL_SIGN);
            byte ptb_data = (schm.getPermission().equals("junk")) ? (byte) 5 : 3;
            postTerminalBlock.setData(ptb_data, true);
            if (postTerminalBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ts = (Sign) postTerminalBlock.getState();
                ts.setLine(0, ((schm.getPermission().equals("junk")) ? plugin.getSigns().getStringList("junk").get(0) : ""));
                ts.setLine(1, ((!schm.getPermission().equals("junk")) ? plugin.getSigns().getStringList("terminal").get(0) : ""));
                ts.setLine(2, ((!schm.getPermission().equals("junk")) ? plugin.getSigns().getStringList("terminal").get(1) : ""));
                ts.setLine(3, "");
                ts.update();
            }
        }
        if (postARSBlock != null) {
            postARSBlock.setType(Material.WALL_SIGN);
            postARSBlock.setData((byte) 3, true);
            if (postARSBlock.getType().equals(Material.WALL_SIGN)) {
                Sign as = (Sign) postARSBlock.getState();
                as.setLine(0, "TARDIS");
                as.setLine(1, plugin.getSigns().getStringList("ars").get(0));
                as.setLine(2, plugin.getSigns().getStringList("ars").get(1));
                as.setLine(3, plugin.getSigns().getStringList("ars").get(2));
                as.update();
            }
        }
        if (postTISBlock != null) {
            postTISBlock.setType(Material.WALL_SIGN);
            postTISBlock.setData((byte) 3, true);
            if (postTISBlock.getType().equals(Material.WALL_SIGN)) {
                Sign is = (Sign) postTISBlock.getState();
                is.setLine(0, "-----");
                is.setLine(1, "TARDIS");
                is.setLine(2, plugin.getSigns().getStringList("info").get(0));
                is.setLine(3, plugin.getSigns().getStringList("info").get(1));
                is.update();
            }
        }
        if (postTemporalBlock != null) {
            postTemporalBlock.setType(Material.WALL_SIGN);
            postTemporalBlock.setData((byte) 3, true);
            if (postTemporalBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ms = (Sign) postTemporalBlock.getState();
                ms.setLine(0, "");
                ms.setLine(1, plugin.getSigns().getStringList("temporal").get(0));
                ms.setLine(2, plugin.getSigns().getStringList("temporal").get(1));
                ms.setLine(3, "");
                ms.update();
            }
        }
        if (postKeyboardBlock != null) {
            postKeyboardBlock.setType(Material.WALL_SIGN);
            postKeyboardBlock.setData((byte) 3, true);
            if (postKeyboardBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ks = (Sign) postKeyboardBlock.getState();
                ks.setLine(0, plugin.getSigns().getStringList("keyboard").get(0));
                for (int i = 1; i < 4; i++) {
                    ks.setLine(i, "");
                }
                ks.update();
            }
        }
        for (Block lamp : lampblocks) {
            Material lantern = (schm.getPermission().equals("eleventh") || schm.getPermission().equals("twelfth")) ? Material.SEA_LANTERN : Material.REDSTONE_LAMP_ON;
            lamp.setType(lantern);
        }
        lampblocks.clear();
        setBanners(176, postStandingBanners);
        setBanners(177, postWallBanners);
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
        // finished processing - update tardis table!
        qf.doUpdate("tardis", set, where);
        // give kit?
        if (plugin.getKitsConfig().getBoolean("give.create.enabled")) {
            if (p.hasPermission("tardis.kit.create")) {
                // check if they have the tardis kit
                HashMap<String, Object> wherek = new HashMap<String, Object>();
                wherek.put("uuid", playerUUID);
                wherek.put("name", "createkit");
                ResultSetAchievements rsa = new ResultSetAchievements(plugin, wherek, false);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> setk = new HashMap<String, Object>();
                    setk.put("uuid", playerUUID);
                    setk.put("name", "createkit");
                    qf.doInsert("achievements", setk);
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
     * @param w the world the chunk is in.
     * @param x the x coordinate of the chunk.
     * @param z the z coordinate of the chunk.
     * @param wid the width of the schematic.
     * @param len the length of the schematic.
     * @return a list of Chunks.
     */
    public List<Chunk> getChunks(World w, int x, int z, int wid, int len) {
        List<Chunk> chunks = new ArrayList<Chunk>();
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
