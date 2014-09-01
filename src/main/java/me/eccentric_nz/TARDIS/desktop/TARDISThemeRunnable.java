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
package me.eccentric_nz.TARDIS.desktop;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author eccentric_nz
 */
public class TARDISThemeRunnable implements Runnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private boolean running;
    int taskID, id, slot, level = 0, row = 0, col = 0, h, w, c, startx, starty, startz, resetx, resetz, j = 2;
    World world;
    private final List<Block> lampblocks = new ArrayList<Block>();
    private final List<Material> precious = new ArrayList<Material>();
    private final HashMap<Block, Byte> postDoorBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postTorchBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postSignBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postRepeaterBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postPistonBaseBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postStickyPistonBaseBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postPistonExtensionBlocks = new HashMap<Block, Byte>();
    Block postSaveSignBlock = null;
    Block postTerminalBlock = null;
    Block postARSBlock = null;
    Block postTISBlock = null;
    Block postTemporalBlock = null;
    Block postKeyboardBlock = null;
    JSONArray arr;
    Material wall_type, floor_type;
    byte wall_data, floor_data;
    QueryFactory qf;
    private HashMap<String, Object> set;
    private HashMap<String, Object> where;
    boolean own_world;
    Location wg1;
    Location wg2;

    public TARDISThemeRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        this.precious.add(Material.BEACON);
        this.precious.add(Material.DIAMOND_BLOCK);
        this.precious.add(Material.EMERALD_BLOCK);
        this.precious.add(Material.GOLD_BLOCK);
        this.precious.add(Material.IRON_BLOCK);
        this.precious.add(Material.REDSTONE_BLOCK);
        this.qf = new QueryFactory(this.plugin);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
        plugin.debug("running");
        // initialise
        if (!running) {
            set = new HashMap<String, Object>();
            where = new HashMap<String, Object>();
            String directory = (tud.getSchematic().equals(SCHEMATIC.CUSTOM)) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getFile();
            File file = new File(path);
            if (!file.exists()) {
                plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
                return;
            }
            // get JSON
            JSONObject obj = TARDISSchematicGZip.unzip(path);
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            h = dimensions.getInt("height");
            w = dimensions.getInt("width");
            c = dimensions.getInt("length");
            // calculate startx, starty, startz
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
            if (!rs.resultSet()) {
                // abort and return energy
                HashMap<String, Object> wherea = new HashMap<String, Object>();
                wherea.put("uuid", uuid.toString());
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().name().toLowerCase());
                qf.alterEnergyLevel("tardis", amount, wherea, plugin.getServer().getPlayer(uuid));
            }
            slot = rs.getTIPS();
            id = rs.getTardis_id();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                resetx = pos.getCentreX();
                startz = pos.getCentreZ();
                resetz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getUtils().getStartLocation(rs.getTardis_id());
                startx = gsl[0];
                resetx = gsl[1];
                startz = gsl[2];
                resetz = gsl[3];
            }
            switch (tud.getSchematic()) {
                case REDSTONE:
                    starty = 65;
                    break;
                default:
                    starty = 64;
                    break;
            }
            String[] split = rs.getChunk().split(":");
            world = plugin.getServer().getWorld(split[0]);
            own_world = plugin.getConfig().getBoolean("creation.create_worlds");
            wg1 = new Location(world, startx, starty, startz);
            wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (c - 1));
            // wall/floor block prefs
            String wall[] = tud.getWall().split(":");
            String floor[] = tud.getFloor().split(":");
            wall_type = Material.valueOf(wall[0]);
            floor_type = Material.valueOf(floor[0]);
            wall_data = plugin.getUtils().parseByte(wall[1]);
            floor_data = plugin.getUtils().parseByte(floor[1]);
            // get input array
            arr = (JSONArray) obj.get("input");
            // clear existing lamp blocks
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("tardis_id", id);
            qf.doDelete("lamps", whered);
            // clear existing precious blocks
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("tardis_id", id);
            wherep.put("police_box", 0);
            qf.doDelete("blocks", wherep);
            // set running
            running = true;
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            plugin.debug("finished placing blocks");
            // put on the door, redstone torches, signs, and the repeaters
            for (Map.Entry<Block, Byte> entry : postDoorBlocks.entrySet()) {
                Block pdb = entry.getKey();
                byte pddata = entry.getValue();
                pdb.setType(Material.IRON_DOOR_BLOCK);
                pdb.setData(pddata, true);
            }
            for (Map.Entry<Block, Byte> entry : postTorchBlocks.entrySet()) {
                Block ptb = entry.getKey();
                byte ptdata = entry.getValue();
                ptb.setType(Material.REDSTONE_TORCH_ON);
                ptb.setData(ptdata, true);
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
            for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
                final Block psb = entry.getKey();
                byte psdata = entry.getValue();
                psb.setType(Material.WALL_SIGN);
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
                postSaveSignBlock.setType(Material.WALL_SIGN);
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
                postTerminalBlock.setType(Material.WALL_SIGN);
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
                postARSBlock.setType(Material.WALL_SIGN);
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
                postTISBlock.setType(Material.WALL_SIGN);
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
                postTemporalBlock.setType(Material.WALL_SIGN);
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
                postKeyboardBlock.setType(Material.WALL_SIGN);
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
            Player player = plugin.getServer().getPlayer(uuid);
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                if (slot == -1) {
                    plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
                }
            }
            // finished processing - update tardis table!
            where.put("tardis_id", id);
            qf.doUpdate("tardis", set, where);
            // jettison blocks if downgrading to smaller size
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            TARDISMessage.send(player, "UPGRADE_FINISHED");
        } else {
            JSONArray floor = (JSONArray) arr.get(level);
            JSONArray r = (JSONArray) floor.get(row);
            // place a row of blocks
            for (int zz = startz; zz < startz + c; zz++) {
                JSONObject bb = (JSONObject) r.get(zz);
                int x = startx + row;
                int y = starty + level;
                //int z = startz + col;
                // if we're setting the biome to sky, do it now
                if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                    world.setBiome(x, zz, Biome.SKY);
                }
                Material type = Material.valueOf((String) bb.get("type"));
                plugin.debug(world.getName() + ":" + x + ":" + y + ":" + zz + " - " + type.toString());
                byte data = bb.getByte("data");
                if (type.equals(Material.BEDROCK)) {
                    // remember bedrock location to block off the beacon light
                    String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + zz;
                    set.put("beacon", bedrocloc);
                }
                if (type.equals(Material.NOTE_BLOCK)) {
                    // remember the location of this Disk Storage
                    String storage = plugin.getUtils().makeLocationStr(world, x, y, zz);
                    qf.insertSyncControl(id, 14, storage, 0);
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
                            if (!tud.getSchematic().equals(SCHEMATIC.ELEVENTH)) {
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
                            } else {
                                if (plugin.getConfig().getBoolean("creation.use_clay")) {
                                    type = Material.STAINED_CLAY;
                                }
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
                    String scanloc = world.getName() + ":" + x + ":" + y + ":" + zz;
                    set.put("scanner", scanloc);
                }
                if (type.equals(Material.CHEST)) {
                    // remember the location of the condenser chest
                    String chest = world.getName() + ":" + x + ":" + y + ":" + zz;
                    set.put("condenser", chest);
                }
                if (type.equals(Material.WALL_SIGN)) { // chameleon circuit sign
                    String chameleonloc = world.getName() + ":" + x + ":" + y + ":" + zz;
                    set.put("chameleon", chameleonloc);
                    set.put("chamele_on", 0);
                }
                if (type.equals(Material.IRON_DOOR_BLOCK)) {
                    if (data < (byte) 8) { // iron door bottom
                        HashMap<String, Object> setd = new HashMap<String, Object>();
                        String doorloc = world.getName() + ":" + x + ":" + y + ":" + zz;
                        setd.put("door_location", doorloc);
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        qf.doUpdate("doors", setd, whered);
                        // if create_worlds is true, set the world spawn
                        if (own_world) {
                            if (plugin.getPM().isPluginEnabled("Multiverse-Core")) {
                                Plugin mvplugin = plugin.getPM().getPlugin("Multiverse-Core");
                                if (mvplugin instanceof MultiverseCore) {
                                    MultiverseCore mvc = (MultiverseCore) mvplugin;
                                    MultiverseWorld foundWorld = mvc.getMVWorldManager().getMVWorld(world.getName());
                                    Location spawn = new Location(world, (x + 0.5), y, (zz + 1.5), 0, 0);
                                    foundWorld.setSpawnLocation(spawn);
                                }
                            } else {
                                world.setSpawnLocation(x, y, (zz + 1));
                            }
                        }
                    } else {
                        // iron door top
                        data = (byte) 9;
                    }
                }
                if (type.equals(Material.STONE_BUTTON)) { // random button
                    // remember the location of this button
                    String button = plugin.getUtils().makeLocationStr(world, x, y, zz);
                    qf.insertSyncControl(id, 1, button, 0);
                }
                if (type.equals(Material.JUKEBOX)) {
                    // remember the location of this Advanced Console
                    String advanced = plugin.getUtils().makeLocationStr(world, x, y, zz);
                    qf.insertSyncControl(id, 15, advanced, 0);
                }
                if (type.equals(Material.CAKE_BLOCK)) {
                    /*
                     * This block will be converted to a lever by
                     * setBlock(), but remember it so we can use it as the handbrake!
                     */
                    String handbrakeloc = plugin.getUtils().makeLocationStr(world, x, y, zz);
                    qf.insertSyncControl(id, 0, handbrakeloc, 0);
                }
                if (type.equals(Material.MONSTER_EGGS)) { // silverfish stone
                    String blockLocStr = (new Location(world, x, y, zz)).toString();
                    switch (data) {
                        case 0: // Save Sign
                            String save_loc = world.getName() + ":" + x + ":" + y + ":" + zz;
                            set.put("save_sign", save_loc);
                            break;
                        case 1: // Destination Terminal
                            qf.insertSyncControl(id, 9, blockLocStr, 0);
                            break;
                        case 2: // Architectural Reconfiguration System
                            qf.insertSyncControl(id, 10, blockLocStr, 0);
                            // get current ARS json
                            HashMap<String, Object> wherer = new HashMap<String, Object>();
                            wherer.put("tardis_id", id);
                            ResultSetARS rsa = new ResultSetARS(plugin, wherer);
                            if (rsa.resultSet()) {
                                int[][][] existing = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                                int control = 42;
                                switch (tud.getSchematic()) {
                                    case DELUXE:
                                        control = 57;
                                        break;
                                    case ELEVENTH:
                                        control = 133;
                                        break;
                                    case BIGGER:
                                        control = 41;
                                        break;
                                    case REDSTONE:
                                        control = 152;
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
                                    case WAR:
                                        control = 159;
                                        break;
                                    case CUSTOM:
                                        control = 122;
                                        break;
                                    default:
                                        break;
                                }
                                existing[1][4][4] = control;
                                if (w > 16) {
                                    existing[1][4][5] = control;
                                    existing[1][5][4] = control;
                                    existing[1][5][5] = control;
                                    if (h > 16) {
                                        existing[2][4][4] = control;
                                        existing[2][4][5] = control;
                                        existing[2][5][4] = control;
                                        existing[2][5][5] = control;
                                    }
                                } else if (h > 16) {
                                    existing[2][4][4] = control;
                                }
                                JSONArray json = new JSONArray(existing);
                                HashMap<String, Object> seta = new HashMap<String, Object>();
                                seta.put("json", json.toString());
                                HashMap<String, Object> wheres = new HashMap<String, Object>();
                                wheres.put("tardis_id", id);
                                qf.doUpdate("ars", seta, wheres);
                            }
                            break;
                        case 3: // TARDIS Information System
                            qf.insertSyncControl(id, 13, blockLocStr, 0);
                            break;
                        case 4: // Temporal Circuit
                            qf.insertSyncControl(id, 11, blockLocStr, 0);
                            break;
                        case 5: // Keyboard
                            qf.insertSyncControl(id, 7, blockLocStr, 0);
                            break;
                        default:
                            break;
                    }
                }
                if (type.equals(Material.REDSTONE_LAMP_ON)) {
                    // remember lamp blocks
                    Block lamp = world.getBlockAt(x, y, zz);
                    lampblocks.add(lamp);
                    if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
                        // remember lamp block locations for malfunction
                        HashMap<String, Object> setlb = new HashMap<String, Object>();
                        String lloc = world.getName() + ":" + x + ":" + y + ":" + zz;
                        setlb.put("tardis_id", id);
                        setlb.put("location", lloc);
                        qf.doInsert("lamps", setlb);
                    }
                }
                if (type.equals(Material.COMMAND) || ((tud.getSchematic().equals(SCHEMATIC.BIGGER) || tud.getSchematic().equals(SCHEMATIC.DELUXE)) && type.equals(Material.BEACON))) {
                    /*
                     * command block - remember it to spawn the creeper on.
                     * could also be a beacon block, as the creeper sits
                     * over the beacon in the deluxe and bigger consoles.
                     */
                    String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (zz + 0.5);
                    set.put("creeper", creeploc);
                    switch (tud.getSchematic()) {
                        case CUSTOM:
                            type = Material.valueOf(plugin.getConfig().getString("creation.custom_creeper_id"));
                            break;
                        case BIGGER:
                        case DELUXE:
                            type = Material.BEACON;
                            break;
                        default:
                            type = Material.SMOOTH_BRICK;
                            break;
                    }
                }
                if (type.equals(Material.WOOD_BUTTON)) {
                    /*
                     * wood button will be coverted to the correct id by
                     * setBlock(), but remember it for the Artron Energy Capacitor.
                     */
                    String woodbuttonloc = plugin.getUtils().makeLocationStr(world, x, y, zz);
                    qf.insertSyncControl(id, 6, woodbuttonloc, 0);
                }
                // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                if (precious.contains(type)) {
                    HashMap<String, Object> setpb = new HashMap<String, Object>();
                    String loc = plugin.getUtils().makeLocationStr(world, x, y, zz);
                    setpb.put("tardis_id", id);
                    setpb.put("location", loc);
                    setpb.put("police_box", 0);
                    qf.doInsert("blocks", setpb);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(loc, id);
                }
                // if it's the door, don't set it just remember its block then do it at the end
                if (type.equals(Material.IRON_DOOR_BLOCK)) { // doors
                    postDoorBlocks.put(world.getBlockAt(x, y, zz), data);
                } else if (type.equals(Material.REDSTONE_TORCH_ON)) {
                    postTorchBlocks.put(world.getBlockAt(x, y, zz), data);
                } else if (type.equals(Material.PISTON_STICKY_BASE)) {
                    postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, zz), data);
                } else if (type.equals(Material.PISTON_BASE)) {
                    postPistonBaseBlocks.put(world.getBlockAt(x, y, zz), data);
                } else if (type.equals(Material.PISTON_EXTENSION)) {
                    postPistonExtensionBlocks.put(world.getBlockAt(x, y, zz), data);
                } else if (type.equals(Material.WALL_SIGN)) {
                    postSignBlocks.put(world.getBlockAt(x, y, zz), data);
                } else if (type.equals(Material.MONSTER_EGGS)) { // monster egg stone for controls
                    switch (data) {
                        case 0:
                            postSaveSignBlock = world.getBlockAt(x, y, zz);
                            break;
                        case 1:
                            postTerminalBlock = world.getBlockAt(x, y, zz);
                            break;
                        case 2:
                            postARSBlock = world.getBlockAt(x, y, zz);
                            break;
                        case 3:
                            postTISBlock = world.getBlockAt(x, y, zz);
                            break;
                        case 4:
                            postTemporalBlock = world.getBlockAt(x, y, zz);
                            break;
                        case 5:
                            postKeyboardBlock = world.getBlockAt(x, y, zz);
                            break;
                        default:
                            break;
                    }
                } else if (type.equals(Material.HUGE_MUSHROOM_2) && data == 15) { // mushroom stem for repeaters
                    // save repeater location
                    if (j < 6) {
                        String repeater = world.getName() + ":" + x + ":" + y + ":" + zz;
                        switch (j) {
                            case 2:
                                postRepeaterBlocks.put(world.getBlockAt(x, y, zz), (byte) 1);
                                qf.insertSyncControl(id, 3, repeater, 0);
                                break;
                            case 3:
                                postRepeaterBlocks.put(world.getBlockAt(x, y, zz), (byte) 2);
                                qf.insertSyncControl(id, 2, repeater, 0);
                                break;
                            case 4:
                                postRepeaterBlocks.put(world.getBlockAt(x, y, zz), (byte) 0);
                                qf.insertSyncControl(id, 5, repeater, 0);
                                break;
                            default:
                                postRepeaterBlocks.put(world.getBlockAt(x, y, zz), (byte) 3);
                                qf.insertSyncControl(id, 4, repeater, 0);
                                break;
                        }
                        j++;
                    }
                } else if (type.equals(Material.SPONGE)) {
                    plugin.getUtils().setBlock(world, x, y, zz, Material.AIR, (byte) 0);
                } else {
                    plugin.getUtils().setBlock(world, x, y, zz, type, data);
                }
            }
            if (row < w) {
                row++;
                plugin.debug("incrementing row");
            }
            if (row == w && level < h) {
                row = 0;
                level++;
                plugin.debug("resetting row, incrementing level");
            }
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
