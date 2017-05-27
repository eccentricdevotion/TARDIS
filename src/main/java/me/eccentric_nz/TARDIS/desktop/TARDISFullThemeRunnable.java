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
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
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
public class TARDISFullThemeRunnable extends TARDISThemeRunnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private boolean running;
    int id, slot, level = 0, row = 0, h, w, c, startx, starty, startz, j = 2;
    World world;
    private final List<Block> lampblocks = new ArrayList<>();
    private List<Chunk> chunks;
    private final HashMap<Block, Byte> postDoorBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postRedstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postTorchBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postLeverBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postSignBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postRepeaterBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postStickyPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, Byte> postPistonExtensionBlocks = new HashMap<>();
    private Block postBedrock;
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
    Location ender = null;
    private Archive archive_next;
    private Archive archive_prev;

    public TARDISFullThemeRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        this.qf = new QueryFactory(this.plugin);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
        // initialise
        if (!running) {
            // get Archive if nescessary
            if (tud.getSchematic().getPermission().equals("archive")) {
                HashMap<String, Object> wherean = new HashMap<>();
                wherean.put("uuid", uuid.toString());
                wherean.put("use", 1);
                ResultSetArchive rs = new ResultSetArchive(plugin, wherean);
                if (rs.resultSet()) {
                    archive_next = rs.getArchive();
                } else {
                    // abort
                    Player cp = plugin.getServer().getPlayer(uuid);
                    TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            if (tud.getPrevious().getPermission().equals("archive")) {
                HashMap<String, Object> whereap = new HashMap<>();
                whereap.put("uuid", uuid.toString());
                whereap.put("use", 2);
                ResultSetArchive rs = new ResultSetArchive(plugin, whereap);
                if (rs.resultSet()) {
                    archive_prev = rs.getArchive();
                } else {
                    // abort
                    Player cp = plugin.getServer().getPlayer(uuid);
                    TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            set = new HashMap<>();
            where = new HashMap<>();
            JSONObject obj;
            if (archive_next == null) {
                String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
                File file = new File(path);
                if (!file.exists()) {
                    plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
                // get JSON
                obj = TARDISSchematicGZip.unzip(path);
            } else {
                obj = archive_next.getJSON();
            }
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            h = dimensions.getInt("height");
            w = dimensions.getInt("width");
            c = dimensions.getInt("length");
            // calculate startx, starty, startz
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
            if (!rs.resultSet()) {
                // abort and return energy
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("uuid", uuid.toString());
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                qf.alterEnergyLevel("tardis", amount, wherea, player);
            }
            Tardis tardis = rs.getTardis();
            slot = tardis.getTIPS();
            id = tardis.getTardis_id();
            chunk = getChunk(tardis.getChunk());
            if (tud.getPrevious().getPermission().equals("ender")) {
                // remove ender crystal
                for (Entity end : chunk.getEntities()) {
                    if (end.getType().equals(EntityType.ENDER_CRYSTAL)) {
                        end.remove();
                    }
                }
            }
            chunks = getChunks(chunk, tud.getSchematic());
            // remove the charged creeper
            Location creeper = getCreeperLocation(tardis.getCreeper());
            Entity ent = creeper.getWorld().spawnEntity(creeper, EntityType.EGG);
            ent.getNearbyEntities(1.5d, 1.5d, 1.5d).forEach((e) -> {
                if (e instanceof Creeper) {
                    e.remove();
                }
            });
            ent.remove();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                startx = gsl[0];
                startz = gsl[2];
            }
            starty = (tud.getSchematic().getPermission().equals("redstone")) ? 65 : 64;
            downgrade = compare(tud.getPrevious(), tud.getSchematic());
            String[] split = tardis.getChunk().split(":");
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
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", id);
            qf.doDelete("lamps", whered);
            // clear existing precious blocks
            HashMap<String, Object> wherep = new HashMap<>();
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
            // clear lamps table as we'll be adding the new lamp positions later
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            qf.doDelete("lamps", wherel);
            plugin.getPM().callEvent(new TARDISDesktopThemeEvent(player, tardis, tud));
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            // remove items
            chunks.forEach((chink) -> {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
            // put on the door, redstone torches, signs, and the repeaters
            postDoorBlocks.entrySet().forEach((entry) -> {
                Block pdb = entry.getKey();
                byte pddata = entry.getValue();
                pdb.setType(Material.IRON_DOOR_BLOCK);
                pdb.setData(pddata, true);
            });
            // TODO fix redstone blocks falling off
            postRedstoneTorchBlocks.entrySet().forEach((entry) -> {
                Block prtb = entry.getKey();
                byte ptdata = entry.getValue();
                prtb.setTypeIdAndData(76, ptdata, true);
            });
            postLeverBlocks.entrySet().forEach((entry) -> {
                Block plb = entry.getKey();
                byte pldata = entry.getValue();
                plb.setType(Material.LEVER);
                plb.setData(pldata, true);
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
            int s = 0;
            for (Map.Entry<Block, Byte> entry : postSignBlocks.entrySet()) {
                if (s == 0) {
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
                        qf.insertSyncControl(id, 22, controlloc, 0);
                    }
                }
                s++;
            }
            lampblocks.forEach((lamp) -> {
                Material l = (tud.getSchematic().hasLanterns() || (archive_next != null && archive_next.isLanterns())) ? Material.SEA_LANTERN : Material.REDSTONE_LAMP_ON;
                lamp.setType(l);
            });
            lampblocks.clear();
            if (postBedrock != null) {
                postBedrock.setType(Material.GLASS);
            }
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                if (slot == -1) {
                    plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
                }
            }
            if (ender != null) {
                Entity ender_crystal = world.spawnEntity(ender, EntityType.ENDER_CRYSTAL);
                ((EnderCrystal) ender_crystal).setShowingBottom(false);
            }
            // finished processing - update tardis table!
            if (set.size() > 0) {
                where.put("tardis_id", id);
                qf.doUpdate("tardis", set, where);
            }
            if (!tud.getSchematic().getPermission().equals("archive")) {
                // reset archive use back to 0
                new ArchiveReset(plugin, uuid.toString(), 0).resetUse();
            }
            // jettison blocks if downgrading to smaller size
            if (downgrade) {
                List<TARDISARSJettison> jettisons = getJettisons(tud.getPrevious(), tud.getSchematic(), chunk);
                jettisons.forEach((jet) -> {
                    // remove the room
                    setAir(jet.getX(), jet.getY(), jet.getZ(), jet.getChunk().getWorld(), 16);
                });
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
                HashMap<String, Object> setr = new HashMap<>();
                setr.put("tardis_id", id);
                qf.alterEnergyLevel("tardis", refund, setr, null);
                if (player.isOnline()) {
                    TARDISMessage.send(player, "ENERGY_RECOVERED", String.format("%d", refund));
                }
            } else if (tud.getSchematic().getPermission().equals("coral") && tud.getPrevious().getConsoleSize().equals(ConsoleSize.TALL)) {
                // clean up space above coral console
                int tidy = starty + h;
                int plus = 32 - h;
                chunks.forEach((chk) -> {
                    setAir(chk.getBlock(0, 64, 0).getX(), tidy, chk.getBlock(0, 64, 0).getZ(), chk.getWorld(), plus);
                });
            }
            // add / remove chunks from the chunks table
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            qf.doDelete("chunks", wherec);
            List<Chunk> chunkList = plugin.getInteriorBuilder().getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, c);
            // update chunks list in DB
            chunkList.stream().map((hunk) -> {
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("tardis_id", id);
                setc.put("world", world.getName());
                setc.put("x", hunk.getX());
                setc.put("z", hunk.getZ());
                return setc;
            }).forEachOrdered((setc) -> {
                qf.doInsert("chunks", setc);
            });
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
                    world.setBiome(x, z, Biome.VOID);
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
                        HashMap<String, Object> setd = new HashMap<>();
                        String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        setd.put("door_location", doorloc);
                        HashMap<String, Object> whered = new HashMap<>();
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
                if (type.equals(Material.MONSTER_EGGS)) {
                    // silverfish stone
                    if (data == 2) {
                        // get current ARS json
                        HashMap<String, Object> wherer = new HashMap<>();
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
                            HashMap<String, Object> seta = new HashMap<>();
                            seta.put("json", json.toString());
                            HashMap<String, Object> wheres = new HashMap<>();
                            wheres.put("tardis_id", id);
                            qf.doUpdate("ars", seta, wheres);
                        }
                    }
                }
                if (type.equals(Material.REDSTONE_LAMP_ON) || type.equals(Material.SEA_LANTERN)) {
                    // remember lamp blocks
                    Block lamp = world.getBlockAt(x, y, z);
                    lampblocks.add(lamp);
                    // remember lamp block locations for malfunction and light switch
                    HashMap<String, Object> setlb = new HashMap<>();
                    String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    setlb.put("tardis_id", id);
                    setlb.put("location", lloc);
                    qf.doInsert("lamps", setlb);
                }
                if (type.equals(Material.COMMAND) || ((tud.getSchematic().getPermission().equals("bigger") || tud.getSchematic().getPermission().equals("coral") || tud.getSchematic().getPermission().equals("deluxe") || tud.getSchematic().getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                    /*
                     * command block - remember it to spawn the creeper on.
                     * could also be a beacon block, as the creeper sits over
                     * the beacon in the deluxe and bigger consoles.
                     */
                    String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                    set.put("creeper", creeploc);
                    if (tud.getSchematic().getPermission().equals("bigger") || tud.getSchematic().getPermission().equals("coral") || tud.getSchematic().getPermission().equals("deluxe") || tud.getSchematic().getPermission().equals("twelfth")) {
                        type = Material.BEACON;
                    } else {
                        type = Material.SMOOTH_BRICK;
                        if (tud.getSchematic().getPermission().equals("ender")) {
                            data = (byte) 3;
                        }
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
                if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                    /*
                     * remember the telepathic circuit.
                     */
                    String telepathicloc = TARDISLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 23, telepathicloc, 0);
                }
                if (type.equals(Material.BEACON) && tud.getSchematic().getPermission().equals("ender")) {
                    /*
                        * get the ender crytal location
                     */
                    ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
                }
                // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                if (TARDISBuilderInstanceKeeper.getPrecious().contains(type) || type.equals(Material.BEDROCK)) {
                    HashMap<String, Object> setpb = new HashMap<>();
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
            chunks.forEach((chink) -> {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
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
        // special case for archives
        if (archive_next != null && archive_prev == null) {
            return (!prev.getPermission().equals(archive_next.getName()) && ((!prev.getConsoleSize().equals(ConsoleSize.SMALL) && archive_next.getConsoleSize().equals(ConsoleSize.SMALL)) || (prev.getConsoleSize().equals(ConsoleSize.TALL) && !archive_next.getConsoleSize().equals(ConsoleSize.TALL))));
        } else if (archive_next == null && archive_prev != null) {
            return (!archive_prev.getName().equals(next.getPermission()) && ((!archive_prev.getConsoleSize().equals(ConsoleSize.SMALL) && next.getConsoleSize().equals(ConsoleSize.SMALL)) || (archive_prev.getConsoleSize().equals(ConsoleSize.TALL) && !next.getConsoleSize().equals(ConsoleSize.TALL))));
        } else if (archive_next != null && archive_prev != null) {
            return (!archive_prev.getName().equals(archive_next.getName()) && ((!archive_prev.getConsoleSize().equals(ConsoleSize.SMALL) && archive_next.getConsoleSize().equals(ConsoleSize.SMALL)) || (archive_prev.getConsoleSize().equals(ConsoleSize.TALL) && !archive_next.getConsoleSize().equals(ConsoleSize.TALL))));
        } else {
            return (!prev.equals(next) && ((!prev.getConsoleSize().equals(ConsoleSize.SMALL) && next.getConsoleSize().equals(ConsoleSize.SMALL)) || (prev.getConsoleSize().equals(ConsoleSize.TALL) && !next.getConsoleSize().equals(ConsoleSize.TALL))));
        }
    }

    private List<TARDISARSJettison> getJettisons(SCHEMATIC prev, SCHEMATIC next, Chunk chunk) {
        List<TARDISARSJettison> list = new ArrayList<>();
        if (prev.getConsoleSize().equals(ConsoleSize.MEDIUM) || (archive_prev != null && archive_prev.getConsoleSize().equals(ConsoleSize.MEDIUM))) {
            // the 3 chunks on the same level
            list.add(new TARDISARSJettison(chunk, 1, 4, 5));
            list.add(new TARDISARSJettison(chunk, 1, 5, 4));
            list.add(new TARDISARSJettison(chunk, 1, 5, 5));
        } else if (prev.getConsoleSize().equals(ConsoleSize.TALL) || (archive_prev != null && archive_prev.getConsoleSize().equals(ConsoleSize.TALL))) {
            if (next.getConsoleSize().equals(ConsoleSize.MEDIUM) || (archive_next != null && archive_next.getConsoleSize().equals(ConsoleSize.MEDIUM))) {
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
        List<Chunk> chinks = new ArrayList<>();
        chinks.add(c);
        if (!s.getConsoleSize().equals(ConsoleSize.SMALL)) {
            chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ()));
            chinks.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 1));
            chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ() + 1));
        }
        return chinks;
    }
}
