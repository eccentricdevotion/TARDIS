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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSJettison;
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
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted,
 * automatically relocating any living beings in the deleted room, depositing
 * them in the control room.
 *
 * @author eccentric_nz
 */
public class TARDISFullThemeRunnable extends TARDISThemeRunnable implements Runnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private boolean running;
    int id, slot, level = 0, row = 0, h, w, c, startx, starty, startz, resetx, resetz, j = 2;
    World world;
    private final List<Block> lampblocks = new ArrayList<Block>();
    private final List<Material> precious = new ArrayList<Material>();
    private List<Chunk> chunks;
    private final HashMap<Block, Byte> postDoorBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postRedstoneTorchBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postTorchBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postLeverBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postSignBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postRepeaterBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postPistonBaseBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postStickyPistonBaseBlocks = new HashMap<Block, Byte>();
    private final HashMap<Block, Byte> postPistonExtensionBlocks = new HashMap<Block, Byte>();
    private Block postBedrock;
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
    boolean downgrade = false;
    Chunk chunk;
    Player player;

    public TARDISFullThemeRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud) {
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
        // initialise
        if (!running) {
            set = new HashMap<String, Object>();
            where = new HashMap<String, Object>();
            String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
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
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                qf.alterEnergyLevel("tardis", amount, wherea, player);
            }
            slot = rs.getTIPS();
            id = rs.getTardis_id();
            chunk = getChunk(rs.getChunk());
            chunks = getChunks(chunk, tud.getSchematic());
            // remove the charged creeper
            Location creeper = getCreeperLocation(rs.getCreeper());
            Entity ent = creeper.getWorld().spawnEntity(creeper, EntityType.EGG);
            for (Entity e : ent.getNearbyEntities(1.5d, 1.5d, 1.5d)) {
                if (e instanceof Creeper) {
                    e.remove();
                }
            }
            ent.remove();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                resetx = pos.getCentreX();
                startz = pos.getCentreZ();
                resetz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(rs.getTardis_id());
                startx = gsl[0];
                resetx = gsl[1];
                startz = gsl[2];
                resetz = gsl[3];
            }
            starty = (tud.getSchematic().getPermission().equals("redstone")) ? 65 : 64;
            downgrade = compare(tud.getPrevious(), tud.getSchematic());
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
            wall_data = TARDISNumberParsers.parseByte(wall[1]);
            floor_data = TARDISNumberParsers.parseByte(floor[1]);
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
            player = plugin.getServer().getPlayer(uuid);
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
            // teleport player to safe location
            TARDISMessage.send(player, "UPGRADE_TELEPORT");
            Location loc;
            if (tud.getSchematic().getPermission().equals("twelfth") || tud.getPrevious().getPermission().equals("twelfth")) {
                loc = chunk.getBlock(9, 69, 3).getLocation();
            } else {
                loc = chunk.getBlock(8, 69, 4).getLocation();
            }
            player.teleport(loc);
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            // remove items
            for (Chunk chink : chunks) {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
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
                prtb.setType(Material.REDSTONE_TORCH_ON);
                prtb.setData(ptdata, true);
            }
            for (Map.Entry<Block, Byte> entry : postLeverBlocks.entrySet()) {
                Block plb = entry.getKey();
                byte pldata = entry.getValue();
                plb.setType(Material.LEVER);
                plb.setData(pldata, true);
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
                        qf.insertSyncControl(id, 22, controlloc, 0);
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
                postTerminalBlock.setData((byte) 3, true);
                if (postTerminalBlock.getType().equals(Material.WALL_SIGN)) {
                    Sign ts = (Sign) postTerminalBlock.getState();
                    ts.setLine(0, "");
                    ts.setLine(1, plugin.getSigns().getStringList("terminal").get(0));
                    ts.setLine(2, plugin.getSigns().getStringList("terminal").get(1));
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
                Material l = (tud.getSchematic().getPermission().equals("eleventh") || tud.getSchematic().getPermission().equals("twelfth")) ? Material.SEA_LANTERN : Material.REDSTONE_LAMP_ON;
                lamp.setType(l);
            }
            lampblocks.clear();
            if (postBedrock != null) {
                postBedrock.setType(Material.GLASS);
            }
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                if (slot == -1) {
                    plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
                }
            }
            // finished processing - update tardis table!
            where.put("tardis_id", id);
            qf.doUpdate("tardis", set, where);
            // jettison blocks if downgrading to smaller size
            if (downgrade) {
                List<TARDISARSJettison> jettisons = getJettisons(tud.getPrevious(), tud.getSchematic(), chunk);
                for (TARDISARSJettison jet : jettisons) {
                    // remove the room
                    setAir(jet.getX(), jet.getY(), jet.getZ(), jet.getChunk().getWorld(), 16);
                }
                // also tidy up the space directly above the ARS centre slot
                int tidy = starty + h;
                int plus = 16 - h;
                setAir(chunk.getBlock(0, 64, 0).getX(), tidy, chunk.getBlock(0, 64, 0).getZ(), chunk.getWorld(), plus);
                // remove dropped items
                for (Entity e : chunk.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
                // give them back some energy (jettison % * difference in cost)
                int big = plugin.getArtronConfig().getInt("upgrades." + tud.getPrevious().getPermission());
                int small = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                int refund = Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * (big - small));
                HashMap<String, Object> setr = new HashMap<String, Object>();
                setr.put("tardis_id", id);
                qf.alterEnergyLevel("tardis", refund, setr, null);
                if (player.isOnline()) {
                    TARDISMessage.send(player, "ENERGY_RECOVERED", String.format("%d", refund));
                }
            }
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            TARDISMessage.send(player, "UPGRADE_FINISHED");
        } else {
            JSONArray floor = (JSONArray) arr.get(level);
            JSONArray r = (JSONArray) floor.get(row);
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                JSONObject bb = (JSONObject) r.get(col);
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                // if we're setting the biome to sky, do it now
                if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                    world.setBiome(x, z, Biome.SKY);
                }
                Material type = Material.valueOf((String) bb.get("type"));
                byte data = bb.getByte("data");
                if (type.equals(Material.BEDROCK)) {
                    // remember bedrock location to block off the beacon light
                    String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    set.put("beacon", bedrocloc);
                    postBedrock = world.getBlockAt(x, y, z);
                }
                if (type.equals(Material.NOTE_BLOCK)) {
                    // remember the location of this Disk Storage
                    String storage = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 14, storage, 0);
                }
                if (type.equals(Material.WOOL)) {
                    switch (data) {
                        case 1:
                            type = wall_type;
                            data = wall_data;
                            break;
                        case 8:
                            type = floor_type;
                            data = floor_data;
                            break;
                        default:
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
                        setd.put("door_location", doorloc);
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        qf.doUpdate("doors", setd, whered);
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
                if (type.equals(Material.STONE_BUTTON)) { // random button
                    // remember the location of this button
                    String button = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 1, button, 0);
                }
                if (type.equals(Material.JUKEBOX)) {
                    // remember the location of this Advanced Console
                    String advanced = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 15, advanced, 0);
                }
                if (type.equals(Material.CAKE_BLOCK)) {
                    /*
                     * This block will be converted to a lever by setBlock(),
                     * but remember it so we can use it as the handbrake!
                     */
                    String handbrakeloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 0, handbrakeloc, 0);
                }
                if (type.equals(Material.MONSTER_EGGS)) { // silverfish stone
                    String blockLocStr = (new Location(world, x, y, z)).toString();
                    switch (data) {
                        case 0: // Save Sign
                            String save_loc = world.getName() + ":" + x + ":" + y + ":" + z;
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
                                int control = tud.getSchematic().getSeedId();
                                existing[1][4][4] = control;
                                if (downgrade) {
                                    // reset slots to stone
                                    existing[2][4][4] = 1;
                                    existing[2][4][5] = 1;
                                    existing[2][5][4] = 1;
                                    existing[2][5][5] = 1;
                                    if (w <= 16) {
                                        existing[1][4][5] = 1;
                                        existing[1][5][4] = 1;
                                        existing[1][5][5] = 1;
                                    }
                                }
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
                if (type.equals(Material.REDSTONE_LAMP_ON) || type.equals(Material.SEA_LANTERN)) {
                    // remember lamp blocks
                    Block lamp = world.getBlockAt(x, y, z);
                    lampblocks.add(lamp);
                    if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
                        // remember lamp block locations for malfunction
                        HashMap<String, Object> setlb = new HashMap<String, Object>();
                        String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        setlb.put("tardis_id", id);
                        setlb.put("location", lloc);
                        qf.doInsert("lamps", setlb);
                    }
                }
                if (type.equals(Material.COMMAND) || ((tud.getSchematic().getPermission().equals("bigger") || tud.getSchematic().getPermission().equals("deluxe") || tud.getSchematic().getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                    /*
                     * command block - remember it to spawn the creeper on.
                     * could also be a beacon block, as the creeper sits over
                     * the beacon in the deluxe and bigger consoles.
                     */
                    String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                    set.put("creeper", creeploc);
                    if (tud.getSchematic().getPermission().equals("bigger") || tud.getSchematic().getPermission().equals("deluxe") || tud.getSchematic().getPermission().equals("twelfth")) {
                        type = Material.BEACON;
                    } else {
                        type = Material.SMOOTH_BRICK;
                    }
                }
                if (type.equals(Material.WOOD_BUTTON)) {
                    /*
                     * wood button will be coverted to the correct id by
                     * setBlock(), but remember it for the Artron Energy
                     * Capacitor.
                     */
                    String woodbuttonloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 6, woodbuttonloc, 0);
                }
                // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                if (precious.contains(type)) {
                    HashMap<String, Object> setpb = new HashMap<String, Object>();
                    String loc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                    setpb.put("tardis_id", id);
                    setpb.put("location", loc);
                    setpb.put("police_box", 0);
                    qf.doInsert("blocks", setpb);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(loc, id);
                }
                // if it's the door, don't set it just remember its block then do it at the end
                if (type.equals(Material.IRON_DOOR_BLOCK)) { // doors
                    postDoorBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.LEVER)) {
                    postLeverBlocks.put(world.getBlockAt(x, y, z), data);
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
                } else if (type.equals(Material.WALL_SIGN)) {
                    postSignBlocks.put(world.getBlockAt(x, y, z), data);
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
                                qf.insertSyncControl(id, 3, repeater, 0);
                                break;
                            case 3:
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), (byte) 2);
                                qf.insertSyncControl(id, 2, repeater, 0);
                                break;
                            case 4:
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), (byte) 0);
                                qf.insertSyncControl(id, 5, repeater, 0);
                                break;
                            default:
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), (byte) 3);
                                qf.insertSyncControl(id, 4, repeater, 0);
                                break;
                        }
                        j++;
                    }
                } else if (type.equals(Material.SPONGE)) {
                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR, (byte) 0);
                } else {
                    TARDISBlockSetters.setBlock(world, x, y, z, type, data);
                }
            }
            // remove items
            for (Chunk chink : chunks) {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            }
            if (row < w) {
                row++;
            }
            if (row == w && level < h) {
                row = 0;
                level++;
            }
        }
    }

    private Chunk getChunk(String str) {
        String[] split = str.split(":");
        World cw = plugin.getServer().getWorld(split[0]);
        int cx = TARDISNumberParsers.parseInt(split[1]);
        int cz = TARDISNumberParsers.parseInt(split[2]);
        return cw.getChunkAt(cx, cz);
    }

    private boolean compare(SCHEMATIC prev, SCHEMATIC next) {
        return (!prev.equals(next) && ((!prev.isSmall() && next.isSmall()) || (prev.isTall() && !next.isTall())));
    }

    private List<TARDISARSJettison> getJettisons(SCHEMATIC prev, SCHEMATIC next, Chunk chunk) {
        List<TARDISARSJettison> list = new ArrayList<TARDISARSJettison>();
        if (prev.getPermission().equals("bigger") || prev.getPermission().equals("redstone") || prev.getPermission().equals("twelfth")) {
            // the 3 chunks on the same level
            list.add(new TARDISARSJettison(chunk, 1, 4, 5));
            list.add(new TARDISARSJettison(chunk, 1, 5, 4));
            list.add(new TARDISARSJettison(chunk, 1, 5, 5));
        } else if (prev.getPermission().equals("deluxe") || prev.getPermission().equals("eleventh") || prev.getPermission().equals("master")) {
            if (next.getPermission().equals("bigger") || next.getPermission().equals("redstone") || next.getPermission().equals("twelfth")) {
                // the 4 chunks on the level above
                list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                list.add(new TARDISARSJettison(chunk, 2, 5, 5));
            } else {
                // the 3 chunks on the same level &
                // the 4 chunks on the level above
                list.add(new TARDISARSJettison(chunk, 1, 4, 5));
                list.add(new TARDISARSJettison(chunk, 1, 5, 4));
                list.add(new TARDISARSJettison(chunk, 1, 5, 5));
                list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                list.add(new TARDISARSJettison(chunk, 2, 5, 5));
            }
        }
        return list;
    }

    private void setAir(int jx, int jy, int jz, World jw, int plusy) {
        for (int yy = jy; yy < (jy + plusy); yy++) {
            for (int xx = jx; xx < (jx + 16); xx++) {
                for (int zz = jz; zz < (jz + 16); zz++) {
                    Block b = jw.getBlockAt(xx, yy, zz);
                    b.setType(Material.AIR);
                }
            }
        }
    }

    private Location getCreeperLocation(String str) {
        String[] creeperData = str.split(":");
        World cw = plugin.getServer().getWorld(creeperData[0]);
        float cx = TARDISNumberParsers.parseFloat(creeperData[1]);
        float cy = TARDISNumberParsers.parseFloat(creeperData[2]) + 1;
        float cz = TARDISNumberParsers.parseFloat(creeperData[3]);
        return new Location(cw, cx, cy, cz);
    }

    private List<Chunk> getChunks(Chunk c, SCHEMATIC s) {
        List<Chunk> chinks = new ArrayList<Chunk>();
        chinks.add(c);
        if (!s.isSmall()) {
            chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ()));
            chinks.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 1));
            chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ() + 1));
        }
        return chinks;
    }
}
