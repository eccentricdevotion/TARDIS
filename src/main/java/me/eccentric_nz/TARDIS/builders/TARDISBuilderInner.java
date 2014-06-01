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

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
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
import org.bukkit.plugin.Plugin;

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

    public TARDISBuilderInner(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds the inside of the TARDIS.
     *
     * @param schm the name of the schematic file to use can be DEFAULT, BIGGER,
     * ELEVENTH, REDSTONE, COAL or DELUXE.
     * @param world the world where the TARDIS is to be built.
     * @param dbID the unique key of the record for this TARDIS in the database.
     * @param p an instance of the player who owns the TARDIS.
     * @param middle_id a material type ID determined from the TARDIS seed
     * block, or the middle block in the TARDIS creation stack, this material
     * determines the makeup of the TARDIS walls.
     * @param middle_data the data bit associated with the middle_id parameter.
     * @param floor_id a material type ID determined from the TARDIS seed block,
     * or 35 (if TARDIS was made via the creation stack), this material
     * determines the makeup of the TARDIS floors.
     * @param floor_data the data bit associated with the floor_id parameter.
     * @param tips a boolean determining where this TARDIS will be built
     * -------- false:own world, underground - true:default world--------
     */
    @SuppressWarnings("deprecation")
    public void buildInner(SCHEMATIC schm, World world, int dbID, Player p, int middle_id, byte middle_data, int floor_id, byte floor_data, boolean tips) {
        String[][][] s;
        short[] d;
        int level, row, col, id, startx, startz, resetx, resetz, j = 2;
        boolean below = (!plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world"));
        int starty;
        if (below) {
            starty = 15;
        } else {
            switch (schm) {
                case REDSTONE:
                    starty = 65;
                    break;
                default:
                    starty = 64;
                    break;
            }
        }
        switch (schm) {
            // TARDIS schematics supplied by Lord_Rahl and killeratnight at mcnovus.net
            case BIGGER:
                s = plugin.getBuildKeeper().getBiggerSchematic();
                d = plugin.getBuildKeeper().getBiggerDimensions();
                break;
            case DELUXE:
                s = plugin.getBuildKeeper().getDeluxeSchematic();
                d = plugin.getBuildKeeper().getDeluxeDimensions();
                break;
            case ELEVENTH:
                s = plugin.getBuildKeeper().getEleventhSchematic();
                d = plugin.getBuildKeeper().getEleventhDimensions();
                break;
            case REDSTONE:
                s = plugin.getBuildKeeper().getRedstoneSchematic();
                d = plugin.getBuildKeeper().getRedstoneDimensions();
                break;
            case STEAMPUNK:
                s = plugin.getBuildKeeper().getSteampunkSchematic();
                d = plugin.getBuildKeeper().getSteampunkDimensions();
                break;
            case PLANK:
                s = plugin.getBuildKeeper().getPlankSchematic();
                d = plugin.getBuildKeeper().getPlankDimensions();
                break;
            case TOM:
                s = plugin.getBuildKeeper().getTomSchematic();
                d = plugin.getBuildKeeper().getTomDimensions();
                break;
            case ARS:
                s = plugin.getBuildKeeper().getARSSchematic();
                d = plugin.getBuildKeeper().getARSDimensions();
                break;
            case WAR:
                s = plugin.getBuildKeeper().getWarSchematic();
                d = plugin.getBuildKeeper().getWarDimensions();
                break;
            case CUSTOM:
                s = plugin.getBuildKeeper().getCustomSchematic();
                d = plugin.getBuildKeeper().getCustomDimensions();
                break;
            default:
                s = plugin.getBuildKeeper().getBudgetSchematic();
                d = plugin.getBuildKeeper().getBudgetDimensions();
                break;
        }
        short h = d[0];
        short w = d[1];
        short l = d[2];
        byte data;
        String tmp;
        String playerUUID = p.getUniqueId().toString();
        HashMap<Block, Byte> postDoorBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postTorchBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postSignBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postRepeaterBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postPistonBaseBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postStickyPistonBaseBlocks = new HashMap<Block, Byte>();
        HashMap<Block, Byte> postPistonExtensionBlocks = new HashMap<Block, Byte>();
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
            int slot = tintpos.getFreeSlot();
            // save the slot
            set.put("tips", slot);
            pos = tintpos.getTIPSData(slot);
            startx = pos.getCentreX();
            resetx = pos.getCentreX();
            startz = pos.getCentreZ();
            plugin.debug("startx: " + startx + ", startz: " + startz);
            resetz = pos.getCentreZ();
            // get the correct chunk for ARS
            Chunk c = world.getChunkAt(new Location(world, startx, starty, startz));
            String chun = world.getName() + ":" + c.getX() + ":" + c.getZ();
            set.put("chunk", chun);
        } else {
            int gsl[] = plugin.getUtils().getStartLocation(dbID);
            startx = gsl[0];
            resetx = gsl[1];
            startz = gsl[2];
            resetz = gsl[3];
        }
        boolean own_world = plugin.getConfig().getBoolean("creation.create_worlds");
        Location wg1 = new Location(world, startx, starty, startz);
        Location wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (l - 1));
        // get list of used chunks
        List<Chunk> chunkList = getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), d);
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
                        plugin.getUtils().setBlock(world, startx, starty, startz, 0, (byte) 0);
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
        for (level = 0; level < h; level++) {
            for (row = 0; row < w; row++) {
                for (col = 0; col < l; col++) {
                    tmp = s[level][row][col];
                    // if we're setting the biome to sky, do it now
                    if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0 && !below) {
                        world.setBiome(startx, startz, Biome.SKY);
                    }
                    if (!tmp.equals("-")) {
                        if (tmp.contains(":")) {
                            String[] iddata = tmp.split(":");
                            id = plugin.getUtils().parseInt(iddata[0]);
                            data = plugin.getUtils().parseByte(iddata[1]);
                            if (id == 7) {
                                // remember bedrock location to block off the beacon light
                                String bedrocloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                set.put("beacon", bedrocloc);
                            }
                            if (id == 25) { // noteblock
                                // remember the location of this Disk Storage
                                String storage = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                                qf.insertSyncControl(dbID, 14, storage, 0);
                            }
                            if (id == 35) { // wool
                                switch (data) {
                                    case 1:
                                        switch (middle_id) {
                                            case 22: // if using the default Lapis Block - then use Orange Wool / Stained Clay
                                                if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                                    id = 159;
                                                }
                                                break;
                                            default:
                                                id = middle_id;
                                                data = middle_data;
                                        }
                                        break;
                                    case 8:
                                        if (!schm.equals(SCHEMATIC.ELEVENTH)) {
                                            switch (floor_id) {
                                                case 22: // if using the default Lapis Block - then use Light Grey Wool / Stained Clay
                                                    if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                                        id = 159;
                                                    }
                                                    break;
                                                default:
                                                    id = floor_id;
                                                    data = floor_data;
                                            }
                                        } else {
                                            if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                                id = 159;
                                            }
                                        }
                                        break;
                                    default:
                                        if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                            id = 159;
                                        }
                                        break;
                                }
                            }
                            if (id == 52) { // scanner button
                                /*
                                 * mob spawner will be converted to the correct id by
                                 * setBlock(), but remember it for the scanner.
                                 */
                                String scanloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                set.put("scanner", scanloc);
                            }
                            if (id == 54) { // chest
                                // remember the location of the condenser chest
                                String chest = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                set.put("condenser", chest);
                            }
                            if (id == 68) { // chameleon circuit sign
                                String chameleonloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                set.put("chameleon", chameleonloc);
                                set.put("chamele_on", 0);
                            }
                            if (id == 71) {
                                if (data < (byte) 8) { // iron door bottom
                                    HashMap<String, Object> setd = new HashMap<String, Object>();
                                    String doorloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                    setd.put("tardis_id", dbID);
                                    setd.put("door_type", 1);
                                    setd.put("door_location", doorloc);
                                    setd.put("door_direction", "SOUTH");
                                    qf.doInsert("doors", setd);
                                    // if create_worlds is true, set the world spawn
                                    if (own_world) {
                                        if (plugin.getPM().isPluginEnabled("Multiverse-Core")) {
                                            Plugin mvplugin = plugin.getPM().getPlugin("Multiverse-Core");
                                            if (mvplugin instanceof MultiverseCore) {
                                                MultiverseCore mvc = (MultiverseCore) mvplugin;
                                                MultiverseWorld foundWorld = mvc.getMVWorldManager().getMVWorld(world.getName());
                                                Location spawn = new Location(world, (startx + 0.5), starty, (startz + 1.5), 0, 0);
                                                foundWorld.setSpawnLocation(spawn);
                                            }
                                        } else {
                                            world.setSpawnLocation(startx, starty, (startz + 1));
                                        }
                                    }
                                } else {
                                    // iron door top
                                    data = (byte) 9;
                                }
                            }
                            if (id == 77) { // stone button
                                // remember the location of this button
                                String button = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                                qf.insertSyncControl(dbID, 1, button, 0);
                            }
                            if (id == 84) { // jukebox
                                // remember the location of this Advanced Console
                                String advanced = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                                qf.insertSyncControl(dbID, 15, advanced, 0);
                                // check if player has storage record, and update the tardis_id field
                                plugin.getUtils().updateStorageId(playerUUID, dbID, qf);
                            }
                            if (id == 92) {
                                /*
                                 * This block will be converted to a lever by
                                 * setBlock(), but remember it so we can use it as the handbrake!
                                 */
                                String handbrakeloc = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                                qf.insertSyncControl(dbID, 0, handbrakeloc, 0);
                            }
                            if (id == 97) { // silverfish stone
                                String blockLocStr = (new Location(world, startx, starty, startz)).toString();
                                switch (data) {
                                    case 0: // Save Sign
                                        String save_loc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                        set.put("save_sign", save_loc);
                                        break;
                                    case 1: // Destination Terminal
                                        qf.insertSyncControl(dbID, 9, blockLocStr, 0);
                                        break;
                                    case 2: // Architectural Reconfiguration System
                                        qf.insertSyncControl(dbID, 10, blockLocStr, 0);
                                        // create default json
                                        int[][][] empty = new int[3][9][9];
                                        for (int y = 0; y < 3; y++) {
                                            for (int ars_x = 0; ars_x < 9; ars_x++) {
                                                for (int ars_z = 0; ars_z < 9; ars_z++) {
                                                    empty[y][ars_x][ars_z] = 1;
                                                }
                                            }
                                        }
                                        int control = 42;
                                        switch (schm) {
                                            case DELUXE:
                                                control = 57;
                                                empty[2][4][4] = control;
                                                empty[2][4][5] = control;
                                                empty[2][5][4] = control;
                                                empty[2][5][5] = control;
                                                empty[1][4][5] = control;
                                                empty[1][5][4] = control;
                                                empty[1][5][5] = control;
                                                break;
                                            case ELEVENTH:
                                                control = 133;
                                                empty[2][4][4] = control;
                                                empty[2][4][5] = control;
                                                empty[2][5][4] = control;
                                                empty[2][5][5] = control;
                                                empty[1][4][5] = control;
                                                empty[1][5][4] = control;
                                                empty[1][5][5] = control;
                                                break;
                                            case BIGGER:
                                                control = 41;
                                                empty[1][4][5] = control;
                                                empty[1][5][4] = control;
                                                empty[1][5][5] = control;
                                                break;
                                            case REDSTONE:
                                                control = 152;
                                                empty[1][4][5] = control;
                                                empty[1][5][4] = control;
                                                empty[1][5][5] = control;
                                                break;
                                            case STEAMPUNK:
                                                control = 173;
                                                break;
                                            case ARS:
                                                control = 155;
                                                break;
                                            case PLANK:
                                                control = 47;
                                                break;
                                            case TOM:
                                                control = 22;
                                                break;
                                            default:
                                                break;
                                        }
                                        empty[1][4][4] = control;
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
                            if (id == 124) {
                                // remember lamp blocks
                                Block lamp = world.getBlockAt(startx, starty, startz);
                                lampblocks.add(lamp);
                                if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
                                    // remember lamp block locations for malfunction
                                    HashMap<String, Object> setlb = new HashMap<String, Object>();
                                    String lloc = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                    setlb.put("tardis_id", dbID);
                                    setlb.put("location", lloc);
                                    qf.doInsert("lamps", setlb);
                                }
                            }
                            if (id == 137 || id == -119 || ((schm.equals(SCHEMATIC.BIGGER) || schm.equals(SCHEMATIC.DELUXE)) && (id == 138 || id == -118))) {
                                /*
                                 * command block - remember it to spawn the creeper on.
                                 * could also be a beacon block, as the creeper sits
                                 * over the beacon in the deluxe and bigger consoles.
                                 */
                                String creeploc = world.getName() + ":" + (startx + 0.5) + ":" + starty + ":" + (startz + 0.5);
                                set.put("creeper", creeploc);
                                switch (schm) {
                                    case CUSTOM:
                                        id = plugin.getConfig().getInt("creation.custom_creeper_id");
                                        break;
                                    case BIGGER:
                                    case DELUXE:
                                        id = 138;
                                        break;
                                    default:
                                        id = 98;
                                        break;
                                }
                            }
                            if (id == 143 || id == -113) {
                                /*
                                 * wood button will be coverted to the correct id by
                                 * setBlock(), but remember it for the Artron Energy Capacitor.
                                 */
                                String woodbuttonloc = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                                qf.insertSyncControl(dbID, 6, woodbuttonloc, 0);
                            }
                        } else {
                            id = plugin.getUtils().parseInt(tmp);
                            data = 0;
                        }
                        // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                        if (id == 41 || id == 42 || id == 57 || id == 133 || id == -123 || id == 138 || id == -118 || id == 152 || id == -104) {
                            HashMap<String, Object> setpb = new HashMap<String, Object>();
                            String loc = plugin.getUtils().makeLocationStr(world, startx, starty, startz);
                            setpb.put("tardis_id", dbID);
                            setpb.put("location", loc);
                            setpb.put("police_box", 0);
                            qf.doInsert("blocks", setpb);
                            plugin.getGeneralKeeper().getProtectBlockMap().put(loc, dbID);
                        }
                        // if it's the door, don't set it just remember its block then do it at the end
                        if (id == 71) { // doors
                            postDoorBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 76) { // redstone torches
                            postTorchBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 29) { // wall signs
                            postStickyPistonBaseBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 33) { // wall signs
                            postPistonBaseBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 34) { // wall signs
                            postPistonExtensionBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 68) { // wall signs
                            postSignBlocks.put(world.getBlockAt(startx, starty, startz), data);
                        } else if (id == 97) { // monster egg stone for controls
                            switch (data) {
                                case 0:
                                    postSaveSignBlock = world.getBlockAt(startx, starty, startz);
                                    break;
                                case 1:
                                    postTerminalBlock = world.getBlockAt(startx, starty, startz);
                                    break;
                                case 2:
                                    postARSBlock = world.getBlockAt(startx, starty, startz);
                                    break;
                                case 3:
                                    postTISBlock = world.getBlockAt(startx, starty, startz);
                                    break;
                                case 4:
                                    postTemporalBlock = world.getBlockAt(startx, starty, startz);
                                    break;
                                case 5:
                                    postKeyboardBlock = world.getBlockAt(startx, starty, startz);
                                    break;
                                default:
                                    break;
                            }
                        } else if (id == 100 && data == 15) { // mushroom stem for repeaters
                            // save repeater location
                            if (j < 6) {
                                String repeater = world.getName() + ":" + startx + ":" + starty + ":" + startz;
                                qf.insertSyncControl(dbID, j, repeater, 0);
                                switch (j) {
                                    case 2:
                                        postRepeaterBlocks.put(world.getBlockAt(startx, starty, startz), (byte) 2);
                                        break;
                                    case 3:
                                        postRepeaterBlocks.put(world.getBlockAt(startx, starty, startz), (byte) 1);
                                        break;
                                    case 4:
                                        postRepeaterBlocks.put(world.getBlockAt(startx, starty, startz), (byte) 3);
                                        break;
                                    default:
                                        postRepeaterBlocks.put(world.getBlockAt(startx, starty, startz), (byte) 0);
                                        break;
                                }
                                j++;
                            }
                        } else if (id == 19) { // sponge
                            int swap;
                            if (world.getWorldType().equals(WorldType.FLAT) || own_world || world.getName().equals("TARDIS_TimeVortex") || world.getGenerator() instanceof TARDISChunkGenerator) {
                                swap = 0;
                            } else {
                                swap = 1;
                            }
                            plugin.getUtils().setBlock(world, startx, starty, startz, swap, data);
                        } else {
                            plugin.getUtils().setBlock(world, startx, starty, startz, id, data);
                        }
                    }
                    startx += 1;
                }
                startx = resetx;
                startz += 1;
            }
            startz = resetz;
            starty += 1;
        }
        // put on the door, redstone torches, signs, and the repeaters
        for (Map.Entry<Block, Byte> entry : postDoorBlocks.entrySet()) {
            Block pdb = entry.getKey();
            byte pddata = entry.getValue();
            pdb.setTypeId(71);
            pdb.setData(pddata, true);
        }
        for (Map.Entry<Block, Byte> entry : postTorchBlocks.entrySet()) {
            Block ptb = entry.getKey();
            byte ptdata = entry.getValue();
            ptb.setTypeId(76);
            ptb.setData(ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postRepeaterBlocks.entrySet()) {
            Block prb = entry.getKey();
            byte ptdata = entry.getValue();
            prb.setTypeId(93);
            prb.setData(ptdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postStickyPistonBaseBlocks.entrySet()) {
            Block pspb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(pspb);
            byte pspdata = entry.getValue();
            pspb.setTypeId(29);
            pspb.setData(pspdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postPistonBaseBlocks.entrySet()) {
            Block ppb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(ppb);
            byte ppbdata = entry.getValue();
            ppb.setTypeId(33);
            ppb.setData(ppbdata, true);
        }
        for (Map.Entry<Block, Byte> entry : postPistonExtensionBlocks.entrySet()) {
            Block ppeb = entry.getKey();
            byte ppedata = entry.getValue();
            ppeb.setTypeId(34);
            ppeb.setData(ppedata, true);
        }
        for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
            final Block psb = entry.getKey();
            byte psdata = entry.getValue();
            psb.setTypeId(68);
            psb.setData(psdata, true);
            if (psb.getType().equals(Material.WALL_SIGN)) {
                Sign cs = (Sign) psb.getState();
                cs.setLine(0, "Chameleon");
                cs.setLine(1, "Circuit");
                cs.setLine(2, ChatColor.RED + "OFF");
                cs.setLine(3, "NEW");
                cs.update();
            }
        }
        if (postSaveSignBlock != null) {
            postSaveSignBlock.setTypeId(68);
            postSaveSignBlock.setData((byte) 3, true);
            if (postSaveSignBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ss = (Sign) postSaveSignBlock.getState();
                ss.setLine(0, "TARDIS");
                ss.setLine(1, "Saved");
                ss.setLine(2, "Locations");
                ss.setLine(3, "");
                ss.update();
            }
        }
        if (postTerminalBlock != null) {
            postTerminalBlock.setTypeId(68);
            postTerminalBlock.setData((byte) 3, true);
            if (postTerminalBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ts = (Sign) postTerminalBlock.getState();
                ts.setLine(0, "");
                ts.setLine(1, "Destination");
                ts.setLine(2, "Terminal");
                ts.setLine(3, "");
                ts.update();
            }
        }
        if (postARSBlock != null) {
            postARSBlock.setTypeId(68);
            postARSBlock.setData((byte) 3, true);
            if (postARSBlock.getType().equals(Material.WALL_SIGN)) {
                Sign as = (Sign) postARSBlock.getState();
                as.setLine(0, "TARDIS");
                as.setLine(1, "Architectural");
                as.setLine(2, "Reconfiguration");
                as.setLine(3, "System");
                as.update();
            }
        }
        if (postTISBlock != null) {
            postTISBlock.setTypeId(68);
            postTISBlock.setData((byte) 3, true);
            if (postTISBlock.getType().equals(Material.WALL_SIGN)) {
                Sign is = (Sign) postTISBlock.getState();
                is.setLine(0, "-----");
                is.setLine(1, "TARDIS");
                is.setLine(2, "Information");
                is.setLine(3, "System");
                is.update();
            }
        }
        if (postTemporalBlock != null) {
            postTemporalBlock.setTypeId(68);
            postTemporalBlock.setData((byte) 3, true);
            if (postTemporalBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ms = (Sign) postTemporalBlock.getState();
                ms.setLine(0, "");
                ms.setLine(1, "Temporal");
                ms.setLine(2, "Locator");
                ms.setLine(3, "");
                ms.update();
            }
        }
        if (postKeyboardBlock != null) {
            postKeyboardBlock.setTypeId(68);
            postKeyboardBlock.setData((byte) 3, true);
            if (postKeyboardBlock.getType().equals(Material.WALL_SIGN)) {
                Sign ks = (Sign) postKeyboardBlock.getState();
                ks.setLine(0, "Keyboard");
                for (int i = 1; i < 4; i++) {
                    ks.setLine(i, "");
                }
                ks.update();
            }
        }
        for (Block lamp : lampblocks) {
            lamp.setType(Material.REDSTONE_LAMP_ON);
        }
        lampblocks.clear();
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            if (tips) {
                if (pos != null) {
                    plugin.getWorldGuardUtils().addWGProtection(p, pos, world);
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
     * @param d an array of the schematic dimensions
     * @return a list of Chunks.
     */
    public List<Chunk> getChunks(World w, int x, int z, short[] d) {
        List<Chunk> chunks = new ArrayList<Chunk>();
        int cw = plugin.getUtils().roundUp(d[1], 16);
        int cl = plugin.getUtils().roundUp(d[2], 16);
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
