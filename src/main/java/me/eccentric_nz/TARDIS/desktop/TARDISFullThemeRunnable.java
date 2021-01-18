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
package me.eccentric_nz.TARDIS.desktop;

import com.google.gson.*;
import me.eccentric_nz.TARDIS.ARS.TARDISARSJettison;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.TARDIS.builders.FractalFence;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.builders.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.schematic.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;

import java.io.File;
import java.util.*;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
 *
 * @author eccentric_nz
 */
public class TARDISFullThemeRunnable extends TARDISThemeRunnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private final List<Block> lampBlocks = new ArrayList<>();
    private final List<Block> fractalBlocks = new ArrayList<>();
    private final HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postSignBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
    private boolean running;
    private int id;
    private int slot;
    private int level = 0;
    private int row = 0;
    private int h;
    private int w;
    private int c;
    private int ph;
    private int pw;
    private int startx;
    private int starty;
    private int startz;
    private int j = 2;
    private World world;
    private List<Chunk> chunks;
    private Block postBedrock;
    private JsonArray arr;
    private Material wall_type;
    private Material floor_type;
    private HashMap<String, Object> set;
    private HashMap<String, Object> where;
    private boolean own_world;
    private Location wg1;
    private Location wg2;
    private boolean downgrade = false;
    private Chunk chunk;
    private Player player;
    private Location ender = null;
    private Archive archive_next;
    private Archive archive_prev;
    private ConsoleSize size_next;
    private ConsoleSize size_prev;

    public TARDISFullThemeRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
    }

    @Override

    public void run() {
        // initialise
        if (!running) {
            // get Archive if necessary
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
            JsonObject obj;
            if (archive_next == null) {
                String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
                File file = new File(path);
                if (!file.exists()) {
                    plugin.debug("Could not find a schematic with that name!");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
                // get JSON
                obj = TARDISSchematicGZip.unzip(path);
                size_next = tud.getSchematic().getConsoleSize();
            } else {
                obj = archive_next.getJSON();
                size_next = archive_next.getConsoleSize();
            }
            // get previous schematic dimensions
            if (archive_prev == null) {
                String prevDirirectory = (tud.getPrevious().isCustom()) ? "user_schematics" : "schematics";
                String prevPath = plugin.getDataFolder() + File.separator + prevDirirectory + File.separator + tud.getPrevious().getPermission() + ".tschm";
                File prevFile = new File(prevPath);
                if (!prevFile.exists()) {
                    plugin.debug("Could not find a schematic with that name!");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
                // get JSON
                JsonObject prevObj = TARDISSchematicGZip.unzip(prevPath);
                JsonObject prevDimensions = prevObj.get("dimensions").getAsJsonObject();
                ph = prevDimensions.get("height").getAsInt();
                pw = prevDimensions.get("width").getAsInt();
                size_prev = tud.getPrevious().getConsoleSize();
            } else {
                JsonObject dimensions = archive_next.getJSON().get("dimensions").getAsJsonObject();
                ph = dimensions.get("height").getAsInt();
                pw = dimensions.get("width").getAsInt();
                size_prev = archive_prev.getConsoleSize();
            }
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            h = dimensions.get("height").getAsInt();
            w = dimensions.get("width").getAsInt();
            c = dimensions.get("length").getAsInt();
            // calculate startx, starty, startz
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
            if (!rs.resultSet()) {
                // abort and return energy
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("uuid", uuid.toString());
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wherea, player);
            }
            Tardis tardis = rs.getTardis();
            slot = tardis.getTIPS();
            id = tardis.getTardis_id();
            chunk = TARDISStaticLocationGetters.getChunk(tardis.getChunk());
            if (tud.getPrevious().getPermission().equals("ender")) {
                // remove ender crystal
                for (Entity end : chunk.getEntities()) {
                    if (end.getType().equals(EntityType.ENDER_CRYSTAL)) {
                        end.remove();
                    }
                }
            }
            if (tardis.getRotor() != null) {
                // remove item frame and delete UUID in db
                ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                // only if entity still exists
                if (itemFrame != null) {
                    itemFrame.setItem(null, false);
                    itemFrame.remove();
                }
                TARDISTimeRotor.updateRotorRecord(id, "");
            }
            chunks = getChunks(chunk, tud.getSchematic());
            if (!tardis.getCreeper().isEmpty()) {
                // remove the charged creeper
                Location creeper = TARDISStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
                Entity ent = creeper.getWorld().spawnEntity(creeper, EntityType.EGG);
                ent.getNearbyEntities(1.5d, 1.5d, 1.5d).forEach((e) -> {
                    if (e instanceof Creeper) {
                        e.remove();
                    }
                });
                ent.remove();
            }
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
            if (tud.getSchematic().getPermission().equals("archive")) {
                starty = archive_next.getY();
            } else {
                starty = TARDISConstants.HIGHER.contains(tud.getSchematic().getPermission()) ? 65 : 64;
            }
            downgrade = (h < ph || w < pw);
            world = TARDISStaticLocationGetters.getWorld(tardis.getChunk());
            own_world = plugin.getConfig().getBoolean("creation.create_worlds");
            wg1 = new Location(world, startx, starty, startz);
            wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (c - 1));
            // wall/floor block prefs
            String wall[] = tud.getWall().split(":");
            String floor[] = tud.getFloor().split(":");
            wall_type = Material.valueOf(wall[0]);
            floor_type = Material.valueOf(floor[0]);
            // get input array
            arr = obj.get("input").getAsJsonArray();
            // clear existing precious blocks
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("tardis_id", id);
            wherep.put("police_box", 0);
            plugin.getQueryFactory().doDelete("blocks", wherep);
            // set running
            running = true;
            player = plugin.getServer().getPlayer(uuid);
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
            // teleport all players to safe location
            Location loc;
            if (tud.getSchematic().getPermission().equals("twelfth") || tud.getPrevious().getPermission().equals("twelfth")) {
                loc = chunk.getBlock(9, 69, 3).getLocation();
            } else {
                int floorLevel = tud.getSchematic().getPermission().equals("bigger") ? 70 : 69;
                loc = chunk.getBlock(8, floorLevel, 4).getLocation();
            }
            loc.getBlock().getRelative(BlockFace.DOWN).setType(Material.BARRIER);
            // get players in TARDIS
            HashMap<String, Object> wherev = new HashMap<>();
            wherev.put("tardis_id", id);
            ResultSetTravellers rsv = new ResultSetTravellers(plugin, wherev, true);
            if (rsv.resultSet()) {
                rsv.getData().forEach((u) -> {
                    Player pv = plugin.getServer().getPlayer(u);
                    if (pv != null) { // may have gone offline!
                        pv.teleport(loc);
                        TARDISMessage.send(pv, "UPGRADE_TELEPORT");
                    }
                });
            }
            // clear lamps table as we'll be adding the new lamp positions later
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("lamps", wherel);
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
            postDoorBlocks.forEach(Block::setBlockData);
            postRedstoneTorchBlocks.forEach(Block::setBlockData);
            postLeverBlocks.forEach(Block::setBlockData);
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
            int s = 0;
            for (Map.Entry<Block, BlockData> entry : postSignBlocks.entrySet()) {
                Block psb = entry.getKey();
                psb.setBlockData(entry.getValue());
                // always make the control centre the first oak wall sign
                if (s == 0 && psb.getType().equals(Material.OAK_WALL_SIGN)) {
                    if (Tag.WALL_SIGNS.isTagged(psb.getType())) {
                        Sign cs = (Sign) psb.getState();
                        cs.setLine(0, "");
                        cs.setLine(1, plugin.getSigns().getStringList("control").get(0));
                        cs.setLine(2, plugin.getSigns().getStringList("control").get(1));
                        cs.setLine(3, "");
                        cs.update();
                        String controlloc = psb.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(id, 22, controlloc, 0);
                    }
                    s++;
                }
            }
            lampBlocks.forEach((lamp) -> {
                BlockData l = (tud.getSchematic().hasLanterns() || (archive_next != null && archive_next.isLanterns())) ? TARDISConstants.LANTERN : TARDISConstants.LAMP;
                lamp.setBlockData(l);
            });
            lampBlocks.clear();
            for (int f = 0; f < fractalBlocks.size(); f++) {
                FractalFence.grow(fractalBlocks.get(f), f);
            }
            if (postBedrock != null) {
                postBedrock.setBlockData(TARDISConstants.GLASS);
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
                plugin.getQueryFactory().doUpdate("tardis", set, where);
            }
            if (!tud.getSchematic().getPermission().equals("archive")) {
                // reset archive use back to 0
                new ArchiveReset(plugin, uuid.toString(), 0).resetUse();
            }
            // jettison blocks if downgrading to smaller size
            if (downgrade) {
                List<TARDISARSJettison> jettisons = getJettisons(size_next, size_prev, chunk);
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
                plugin.getQueryFactory().alterEnergyLevel("tardis", refund, setr, null);
                if (player.isOnline()) {
                    TARDISMessage.send(player, "ENERGY_RECOVERED", String.format("%d", refund));
                }
            } else if (tud.getSchematic().getPermission().equals("coral") && tud.getPrevious().getConsoleSize().equals(ConsoleSize.TALL)) {
                // clean up space above coral console
                int tidy = starty + h;
                int plus = 32 - h;
                chunks.forEach((chk) -> setAir(chk.getBlock(0, 64, 0).getX(), tidy, chk.getBlock(0, 64, 0).getZ(), chk.getWorld(), plus));
            }
            // add / remove chunks from the chunks table
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("chunks", wherec);
            List<Chunk> chunkList = TARDISStaticUtils.getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, c);
            // update chunks list in DB
            chunkList.stream().map((hunk) -> {
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("tardis_id", id);
                setc.put("world", world.getName());
                setc.put("x", hunk.getX());
                setc.put("z", hunk.getZ());
                return setc;
            }).forEachOrdered((setc) -> plugin.getQueryFactory().doInsert("chunks", setc));
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            TARDISMessage.send(player, "UPGRADE_FINISHED");
        } else {
            JsonArray floor = arr.get(level).getAsJsonArray();
            JsonArray r = (JsonArray) floor.get(row);
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                JsonObject bb = r.get(col).getAsJsonObject();
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                BlockData data;
                Material type;
                try {
                    data = plugin.getServer().createBlockData(bb.get("data").getAsString());
                    type = data.getMaterial();
                } catch (IllegalArgumentException e) {
                    // probably an archived console with legacy block data for wall blocks
                    // eg.
                    // minecraft:cobblestone_wall[east=false,north=false,south=false,up=true,waterlogged=false,west=false]
                    String[] split1 = bb.get("data").getAsString().split("\\[");
                    String[] split2 = split1[0].split(":");
                    String upper = split2[1].toUpperCase();
                    type = Material.valueOf(upper);
                    data = plugin.getServer().createBlockData(type);
                }
                if (type.equals(Material.BEDROCK)) {
                    // remember bedrock location to block off the beacon light
                    String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    set.put("beacon", bedrocloc);
                    postBedrock = world.getBlockAt(x, y, z);
                }
                if (type.equals(Material.NOTE_BLOCK)) {
                    // remember the location of this Disk Storage
                    String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 14, storage, 0);
                    // set block data to correct MUSHROOM_STEM
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
                }
                if (type.equals(Material.ORANGE_WOOL)) {
                    if (wall_type == Material.ORANGE_WOOL) {
                        data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(46));
                    } else {
                        data = wall_type.createBlockData();
                    }
                }
                if (type.equals(Material.BLUE_WOOL)) {
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(54));
                }
                if ((type.equals(Material.WARPED_FENCE) || type.equals(Material.CRIMSON_FENCE)) && tud.getSchematic().getPermission().equals("delta")) {
                    fractalBlocks.add(world.getBlockAt(x, y, z));
                }
                if (type.equals(Material.WHITE_STAINED_GLASS) && tud.getSchematic().getPermission().equals("war")) {
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(47));
                }
                if (type.equals(Material.WHITE_TERRACOTTA) && tud.getSchematic().getPermission().equals("war")) {
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(48));
                }
                if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                    data = floor_type.createBlockData();
                }
                if (type.equals(Material.SPAWNER)) { // scanner button
                    /*
                     * mob spawner will be converted to the correct id by
                     * setBlock(), but remember it for the scanner.
                     */
                    String scanner = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 33, scanner, 0);
                }
                if (type.equals(Material.CHEST)) {
                    // remember the location of the condenser chest
                    String condenser = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 34, condenser, 0);
                }
                if (type.equals(Material.IRON_DOOR)) {
                    Bisected bisected = (Bisected) data;
                    if (bisected.getHalf().equals(Bisected.Half.BOTTOM)) { // iron door bottom
                        HashMap<String, Object> setd = new HashMap<>();
                        String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        setd.put("door_location", doorloc);
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        plugin.getQueryFactory().doUpdate("doors", setd, whered);
                        // if create_worlds is true, set the world spawn
                        if (own_world) {
                            world.setSpawnLocation(x, y, (z + 1));
                        }
                    }
                }
                if (type.equals(Material.STONE_BUTTON)) { // random button
                    // remember the location of this button
                    String button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 1, button, 0);
                }
                if (type.equals(Material.JUKEBOX)) {
                    // remember the location of this Advanced Console
                    String advanced = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 15, advanced, 0);
                    // set block data to correct MUSHROOM_STEM
                    data = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(50));
                }
                if (type.equals(Material.CAKE)) {
                    /*
                     * This block will be converted to a lever by setBlock(),
                     * but remember it so we can use it as the handbrake!
                     */
                    String handbrakeloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 0, handbrakeloc, 0);
                    // get current ARS json
                    HashMap<String, Object> wherer = new HashMap<>();
                    wherer.put("tardis_id", id);
                    ResultSetARS rsa = new ResultSetARS(plugin, wherer);
                    if (rsa.resultSet()) {
                        String[][][] existing = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                        if (downgrade) {
                            // reset slots to stone
                            switch (size_prev) {
                                case MASSIVE:
                                    // the 8 slots on the same level &
                                    existing[1][4][5] = "STONE";
                                    existing[1][4][6] = "STONE";
                                    existing[1][5][4] = "STONE";
                                    existing[1][5][5] = "STONE";
                                    existing[1][5][6] = "STONE";
                                    existing[1][6][4] = "STONE";
                                    existing[1][6][5] = "STONE";
                                    existing[1][6][6] = "STONE";
                                    // the 9 slots on the level above
                                    existing[2][4][4] = "STONE";
                                    existing[2][4][5] = "STONE";
                                    existing[2][4][6] = "STONE";
                                    existing[2][5][4] = "STONE";
                                    existing[2][5][5] = "STONE";
                                    existing[2][5][6] = "STONE";
                                    existing[2][6][4] = "STONE";
                                    existing[2][6][5] = "STONE";
                                    existing[2][6][6] = "STONE";
                                    break;
                                case TALL:
                                    // the 3 slots on the same level &
                                    existing[1][4][5] = "STONE";
                                    existing[1][5][4] = "STONE";
                                    existing[1][5][5] = "STONE";
                                    // the 4 slots on the level above
                                    existing[2][4][4] = "STONE";
                                    existing[2][4][5] = "STONE";
                                    existing[2][5][4] = "STONE";
                                    existing[2][5][5] = "STONE";
                                    break;
                                case MEDIUM:
                                    // the 3 slots on the same level
                                    existing[1][4][5] = "STONE";
                                    existing[1][5][4] = "STONE";
                                    existing[1][5][5] = "STONE";
                                    break;
                                default:
                                    // SMALL size do nothing
                                    break;
                            }
                        }
                        // add control blocks
                        String control = tud.getSchematic().getSeedMaterial().toString();
                        existing[1][4][4] = control;
                        switch (size_next) {
                            case MASSIVE:
                                // the 8 slots on the same level &
                                existing[1][4][5] = control;
                                existing[1][4][6] = control;
                                existing[1][5][4] = control;
                                existing[1][5][5] = control;
                                existing[1][5][6] = control;
                                existing[1][6][4] = control;
                                existing[1][6][5] = control;
                                existing[1][6][6] = control;
                                // the 9 slots on the level above
                                existing[2][4][4] = control;
                                existing[2][4][5] = control;
                                existing[2][4][6] = control;
                                existing[2][5][4] = control;
                                existing[2][5][5] = control;
                                existing[2][5][6] = control;
                                existing[2][6][4] = control;
                                existing[2][6][5] = control;
                                existing[2][6][6] = control;
                                break;
                            case TALL:
                                // the 3 slots on the same level &
                                existing[1][4][5] = control;
                                existing[1][5][4] = control;
                                existing[1][5][5] = control;
                                // the 4 slots on the level above
                                existing[2][4][4] = control;
                                existing[2][4][5] = control;
                                existing[2][5][4] = control;
                                existing[2][5][5] = control;
                                break;
                            case MEDIUM:
                                // the 3 slots on the same level
                                existing[1][4][5] = control;
                                existing[1][5][4] = control;
                                existing[1][5][5] = control;
                                break;
                            default:
                                // SMALL size do nothing
                                break;
                        }
                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        JsonArray json = new JsonParser().parse(gson.toJson(existing)).getAsJsonArray();
                        HashMap<String, Object> seta = new HashMap<>();
                        seta.put("json", json.toString());
                        HashMap<String, Object> wheres = new HashMap<>();
                        wheres.put("tardis_id", id);
                        plugin.getQueryFactory().doUpdate("ars", seta, wheres);
                    }
                }
                if (type.equals(Material.REDSTONE_LAMP) || type.equals(Material.SEA_LANTERN)) {
                    // remember lamp blocks
                    Block lamp = world.getBlockAt(x, y, z);
                    lampBlocks.add(lamp);
                    // remember lamp block locations for malfunction and light switch
                    HashMap<String, Object> setlb = new HashMap<>();
                    String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    setlb.put("tardis_id", id);
                    setlb.put("location", lloc);
                    plugin.getQueryFactory().doInsert("lamps", setlb);
                }
                if (type.equals(Material.COMMAND_BLOCK) || ((tud.getSchematic().getPermission().equals("bigger") || tud.getSchematic().getPermission().equals("coral") || tud.getSchematic().getPermission().equals("deluxe") || tud.getSchematic().getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                    /*
                     * command block - remember it to spawn the creeper on.
                     * could also be a beacon block, as the creeper sits over
                     * the beacon in the deluxe and bigger consoles.
                     */
                    String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                    set.put("creeper", creeploc);
                    if (type.equals(Material.COMMAND_BLOCK)) {
                        if (tud.getSchematic().getPermission().equals("ender")) {
                            data = Material.END_STONE_BRICKS.createBlockData();
                        } else if (tud.getSchematic().getPermission().equals("delta")) {
                            data = Material.BLACKSTONE.createBlockData();
                        } else {
                            data = Material.STONE_BRICKS.createBlockData();
                        }
                    }
                }
                if (type.equals(Material.OAK_BUTTON)) {
                    /*
                     * wood button will be converted to the correct id by
                     * setBlock(), but remember it for the Artron Energy
                     * Capacitor.
                     */
                    String woodbuttonloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 6, woodbuttonloc, 0);
                }
                if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                    /*
                     * remember the telepathic circuit.
                     */
                    String telepathicloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 23, telepathicloc, 0);
                }
                if (type.equals(Material.BEACON) && tud.getSchematic().getPermission().equals("ender")) {
                    /*
                     * get the ender crystal location
                     */
                    ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
                }
                // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                if (TARDISBuilderInstanceKeeper.getPrecious().contains(type) || type.equals(Material.BEDROCK)) {
                    HashMap<String, Object> setpb = new HashMap<>();
                    String loc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    setpb.put("tardis_id", id);
                    setpb.put("location", loc);
                    setpb.put("data", "minecraft:air");
                    setpb.put("police_box", 0);
                    plugin.getQueryFactory().doInsert("blocks", setpb);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(loc, id);
                }
                // if it's the door, don't set it just remember its block then do it at the end
                if (type.equals(Material.HONEYCOMB_BLOCK) && (tud.getSchematic().getPermission().equals("delta") || tud.getSchematic().getPermission().equals("rotor"))) {
                    /*
                     * spawn an item frame and place the time rotor in it
                     */
                    TARDISBlockSetters.setBlock(world, x, y, z, (tud.getSchematic().getPermission().equals("delta")) ? Material.POLISHED_BLACKSTONE_BRICKS : Material.STONE_BRICKS);
                    TARDISTimeRotor.setItemFrame(tud.getSchematic().getPermission(), new Location(world, x, y + 1, z), id);
                } else if (type.equals(Material.IRON_DOOR)) { // doors
                    postDoorBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.LEVER)) {
                    postLeverBlocks.put(world.getBlockAt(x, y, z), data);
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
                } else if (Tag.WALL_SIGNS.isTagged(type)) {
                    postSignBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (TARDISMaterials.infested.contains(type)) {
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
                                directional.setFacing(BlockFace.WEST);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                plugin.getQueryFactory().insertSyncControl(id, 3, repeater, 0);
                                break;
                            case 3:
                                directional.setFacing(BlockFace.NORTH);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                plugin.getQueryFactory().insertSyncControl(id, 2, repeater, 0);
                                break;
                            case 4:
                                directional.setFacing(BlockFace.SOUTH);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                plugin.getQueryFactory().insertSyncControl(id, 5, repeater, 0);
                                break;
                            default:
                                directional.setFacing(BlockFace.EAST);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                plugin.getQueryFactory().insertSyncControl(id, 4, repeater, 0);
                                break;
                        }
                        j++;
                    }
                } else if (type.equals(Material.BROWN_MUSHROOM) && tud.getSchematic().getPermission().equals("master")) {
                    // spawn locations for two villagers
                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                    plugin.setTardisSpawn(true);
                    world.spawnEntity(new Location(world, x + 0.5, y + 0.25, z + 0.5), EntityType.VILLAGER);
                } else if (type.equals(Material.SPONGE)) {
                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                } else {
                    BlockState state = world.getBlockAt(x, y, z).getState();
                    if (state instanceof BlockState) {
                        plugin.getTardisHelper().removeTileEntity(state);
                    }
                    TARDISBlockSetters.setBlock(world, x, y, z, data);
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

    private List<TARDISARSJettison> getJettisons(ConsoleSize next, ConsoleSize prev, Chunk chunk) {
        List<TARDISARSJettison> list = new ArrayList<>();
        switch (prev) {
            case MASSIVE:
                switch (next) {
                    case TALL:
                        // the 5 chunks on the same level &
                        list.add(new TARDISARSJettison(chunk, 1, 4, 6));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 6));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 4));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 5));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 6));
                        // the 5 chunks on the level above
                        list.add(new TARDISARSJettison(chunk, 2, 4, 6));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 6));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 6));

                        break;
                    case MEDIUM:
                        // the 5 chunks on the same level &
                        list.add(new TARDISARSJettison(chunk, 1, 4, 6));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 6));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 4));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 5));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 6));
                        // the 9 chunks on the level above
                        list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 6));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 6));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 6));
                        break;
                    case SMALL:
                        // the 8 chunks on the same level &
                        list.add(new TARDISARSJettison(chunk, 1, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 1, 4, 6));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 5));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 6));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 4));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 5));
                        list.add(new TARDISARSJettison(chunk, 1, 6, 6));
                        // the 9 chunks on the level above
                        list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 6));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 6));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 6, 6));
                        break;
                    default:
                        // same size do nothing
                        break;
                }
                break;
            case TALL:
                switch (next) {
                    case MEDIUM:
                        // the 4 chunks on the level above
                        list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 5));
                        break;
                    case SMALL:
                        // the 3 chunks on the same level &
                        list.add(new TARDISARSJettison(chunk, 1, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 5));
                        // the 4 chunks on the level above
                        list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 5));
                        break;
                    default:
                        // same size or bigger do nothing
                        break;
                }
                break;
            case MEDIUM:
                // same size or bigger do nothing
                if (next == ConsoleSize.SMALL) {// the 3 chunks on the same level
                    list.add(new TARDISARSJettison(chunk, 1, 4, 5));
                    list.add(new TARDISARSJettison(chunk, 1, 5, 4));
                    list.add(new TARDISARSJettison(chunk, 1, 5, 5));
                }
                break;
            default: // SMALL size do nothing
                break;
        }
        return list;
    }

    private void setAir(int jx, int jy, int jz, World jw, int plusy) {
        for (int yy = jy; yy < (jy + plusy); yy++) {
            for (int xx = jx; xx < (jx + 16); xx++) {
                for (int zz = jz; zz < (jz + 16); zz++) {
                    Block b = jw.getBlockAt(xx, yy, zz);
                    BlockState state = b.getState();
                    if (state instanceof BlockState) {
                        plugin.getTardisHelper().removeTileEntity(state);
                    }
                    b.setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }

    private List<Chunk> getChunks(Chunk c, Schematic s) {
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
