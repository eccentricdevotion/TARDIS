/*
 * Copyright (C) 2024 eccentric_nz
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.TARDIS.builders.FractalFence;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.schematic.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.schematic.setters.*;
import me.eccentric_nz.TARDIS.utility.*;
import me.eccentric_nz.TARDIS.utility.protection.TARDISProtectionRemover;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;

import java.util.*;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
 *
 * @author eccentric_nz
 */
public class TARDISThemeRepairRunnable extends TARDISThemeRunnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
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
    private final boolean clean;
    private boolean running;
    private int id, slot, c, h, w, level = 0, row = 0, startx, starty, startz, resetx, resetz, j = 2;
    private World world;
    private List<Chunk> chunks;
    private Block postBedrock;
    private Location postOod;
    private JsonObject obj;
    private JsonArray arr;
    private Material wall_type;
    private Material floor_type;
    private HashMap<String, Object> set;
    private HashMap<String, Object> where;
    private boolean own_world;
    private Location wg1;
    private Location wg2;
    private Player player;
    private Location ender = null;
    private Archive archive;

    TARDISThemeRepairRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud, boolean clean) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        this.clean = clean;
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
                    archive = rs.getArchive();
                } else {
                    // abort
                    Player cp = plugin.getServer().getPlayer(uuid);
                    plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            set = new HashMap<>();
            where = new HashMap<>();
            if (archive == null) {
                // get JSON
                obj = TARDISSchematicGZip.getObject(plugin, "consoles", tud.getSchematic().getPermission(), tud.getSchematic().isCustom());
                if (obj == null) {
                    return;
                }
            } else {
                obj = archive.getJSON();
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
                // ?
                return;
            }
            Tardis tardis = rs.getTardis();
            slot = tardis.getTIPS();
            id = tardis.getTardisId();
            Chunk chunk = TARDISStaticLocationGetters.getChunk(tardis.getChunk());
            if (tud.getPrevious().getPermission().equals("ender")) {
                // remove ender crystal
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getType().equals(EntityType.END_CRYSTAL)) {
                        entity.remove();
                    }
                }
            }
            if (tardis.getRotor() != null) {
                // remove item frame and delete UUID in db
                ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                // only if entity still exists
                if (itemFrame != null) {
                    if (tud.getPrevious().getPermission().equals("mechanical")) {
                        // remove the engine item frame
                        ItemFrame engine = TARDISItemFrameSetter.getItemFrameFromLocation(itemFrame.getLocation().add(0, -4, 0));
                        if (engine != null) {
                            engine.setItem(null, false);
                            engine.remove();
                        }
                    }
                    itemFrame.setItem(null, false);
                    itemFrame.remove();
                    TARDISTimeRotor.updateRotorRecord(id, "");
                    plugin.getGeneralKeeper().getTimeRotors().add(tardis.getRotor());
                }
            }
            chunks = TARDISChunkUtils.getConsoleChunks(chunk, tud.getSchematic());
            if (!tardis.getCreeper().isEmpty()) {
                Location creeper = TARDISStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
                if (tud.getPrevious().getPermission().equals("division") || tud.getPrevious().getPermission().equals("hospital")) {
                    // remove ood
                    new TARDISFollowerSpawner(plugin).removeDivisionOod(creeper);
                }
                // remove the charged creeper
                Entity ent = creeper.getWorld().spawnEntity(creeper, EntityType.EGG);
                ent.getNearbyEntities(1.5d, 1.5d, 1.5d).forEach((e) -> {
                    if (e instanceof Creeper) {
                        e.remove();
                    }
                });
                ent.remove();
            }
            if (slot != -1000001) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                resetx = pos.getCentreX();
                startz = pos.getCentreZ();
                resetz = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
                startx = gsl[0];
                resetx = gsl[1];
                startz = gsl[2];
                resetz = gsl[3];
            }
            if (tud.getSchematic().getPermission().equals("mechanical")) {
                starty = 62;
            } else if (TARDISConstants.HIGHER.contains(tud.getSchematic().getPermission())) {
                starty = 65;
            } else {
                starty = 64;
            }
            world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            own_world = plugin.getConfig().getBoolean("creation.create_worlds");
            wg1 = new Location(world, startx, starty, startz);
            wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (c - 1));
            // wall/floor block prefs
            String[] wall = tud.getWall().split(":");
            String[] floor = tud.getFloor().split(":");
            wall_type = Material.valueOf(wall[0]);
            floor_type = Material.valueOf(floor[0]);
            // get input array
            arr = obj.get("input").getAsJsonArray();
            // clear existing precious blocks and remove the entries from the protection map
            new TARDISProtectionRemover(plugin).cleanInteriorBlocks(id);
            // set running
            running = true;
            player = plugin.getServer().getPlayer(uuid);
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
            // clear lamps table as we'll be adding the new lamp positions later
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("lamps", wherel);
            chunks.forEach((c) -> {
                // remove any display items lamps
                TARDISDisplayItemUtils.removeDisplaysInChunk(c, starty, starty + h);
            });
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
            // put on the door, redstone torches, signs, beds, and the repeaters
            postBedBlocks.forEach(Block::setBlockData);
            postDoorBlocks.forEach(Block::setBlockData);
            postRedstoneTorchBlocks.forEach(Block::setBlockData);
            postLeverBlocks.forEach(Block::setBlockData);
            postTorchBlocks.forEach(Block::setBlockData);
            postLanternBlocks.forEach(Block::setBlockData);
            postRepeaterBlocks.forEach(Block::setBlockData);
            postDripstoneBlocks.forEach(Block::setBlockData);
            postLichenBlocks.forEach(Block::setBlockData);
            postSculkVeinBlocks.forEach(Block::setBlockData);
            postStickyPistonBaseBlocks.forEach((pspb, value) -> {
                plugin.getGeneralKeeper().getDoorPistons().add(pspb);
                pspb.setBlockData(value);
            });
            postPistonBaseBlocks.forEach((ppb, value) -> {
                plugin.getGeneralKeeper().getDoorPistons().add(ppb);
                ppb.setBlockData(value);
            });
            postPistonExtensionBlocks.forEach(Block::setBlockData);
            TARDISSignSetter.setSigns(postSignBlocks, plugin, id);
            TARDISBannerSetter.setBanners(postBannerBlocks);
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
            if (tud.getSchematic().getPermission().equals("cave")) {
                iceBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                iceBlocks.clear();
            }
            if (postBedrock != null) {
                postBedrock.setBlockData(TARDISConstants.GLASS);
            }
            if (postOod != null) {
                // spawn Ood
                new TARDISFollowerSpawner(plugin).spawnDivisionOod(postOod);
            }
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                if (slot == -1) {
                    plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
                }
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
                    Art art = Art.valueOf(painting.get("art").getAsString());
                    BlockFace facing = BlockFace.valueOf(painting.get("facing").getAsString());
                    Location pl = TARDISPainting.calculatePosition(art, facing, new Location(world, resetx + px, starty + py, resetz + pz));
                    try {
                        Painting ent = (Painting) world.spawnEntity(pl, EntityType.PAINTING);
                        ent.teleport(pl);
                        ent.setFacingDirection(facing, true);
                        ent.setArt(art, true);
                    } catch (IllegalArgumentException e) {
                        plugin.debug("Invalid painting location!" + pl);
                    }
                }
            }
            if (obj.has("item_frames")) {
                JsonArray frames = obj.get("item_frames").getAsJsonArray();
                for (int i = 0; i < frames.size(); i++) {
                    TARDISItemFrameSetter.curate(frames.get(i).getAsJsonObject(), wg1, id);
                }
            }
            if (obj.has("item_displays")) {
                JsonArray displays = obj.get("item_displays").getAsJsonArray();
                for (int i = 0; i < displays.size(); i++) {
                    TARDISItemDisplaySetter.fakeBlock(displays.get(i).getAsJsonObject(), wg1, id);
                }
            }
            // finished processing - update tardis table!
            if (!set.isEmpty()) {
                where.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("tardis", set, where);
            }
            if (!tud.getSchematic().getPermission().equals("archive")) {
                // reset archive use back to 0
                new ArchiveReset(plugin, uuid.toString(), 0).resetUse();
            }
            if (tud.getSchematic().getPermission().equals("coral") && tud.getPrevious().getConsoleSize().equals(ConsoleSize.TALL)) {
                // clean up space above coral console
                int tidy = starty + h;
                int plus = 32 - h;
                chunks.forEach((chk) -> setAir(chk.getBlock(0, 64, 0).getX(), tidy, chk.getBlock(0, 64, 0).getZ(), chk.getWorld(), plus));
            }
            // add / remove chunks from the chunks table
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("chunks", wherec);
            // update chunks list in DB
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                chunks.forEach((hunk) -> {
                    HashMap<String, Object> setc = new HashMap<>();
                    setc.put("tardis_id", id);
                    setc.put("world", world.getName());
                    setc.put("x", hunk.getX());
                    setc.put("z", hunk.getZ());
                    plugin.getQueryFactory().doInsert("chunks", setc);
                });
            }, 20L);
            // remove blocks from condenser table if necessary
            if (!clean && plugin.getGeneralKeeper().getRoomCondenserData().containsKey(uuid)) {
                TARDISCondenserData c_data = plugin.getGeneralKeeper().getRoomCondenserData().get(uuid);
                int amount = 0;
                for (Map.Entry<String, Integer> entry : c_data.getBlockIDCount().entrySet()) {
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", c_data.getTardis_id());
                    whered.put("block_data", entry.getKey());
                    plugin.getQueryFactory().alterCondenserBlockCount(entry.getValue(), whered);
                    amount += entry.getValue() * plugin.getCondensables().get(entry.getKey());
                }
                plugin.getGeneralKeeper().getRoomCondenserData().remove(uuid);
                if (amount > 0) {
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wheret, player);
                }
            }
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            String message = (clean) ? "REPAIR_CLEAN" : "REPAIR_DONE";
            plugin.getMessenger().send(player, TardisModule.TARDIS, message);
        } else {
            JsonArray floor = arr.get(level).getAsJsonArray();
            JsonArray r = (JsonArray) floor.get(row);
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                JsonObject bb = r.get(col).getAsJsonObject();
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                BlockData data = plugin.getServer().createBlockData(bb.get("data").getAsString());
                Material type = data.getMaterial();
                if (type.equals(Material.BEDROCK)) {
                    // remember bedrock location to block off the beacon light
                    String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    set.put("beacon", bedrocloc);
                    postBedrock = world.getBlockAt(x, y, z);
                }
                if (type.equals(Material.LIGHT_GRAY_CONCRETE) && tud.getSchematic().getPermission().equals("bone")) {
                    // get the block
                    Block block = new Location(world, x, y, z).getBlock();
                    // build a console
                    new ConsoleBuilder(plugin).create(block, 1, id, uuid.toString());
                }
                if (type.equals(Material.SCULK_SHRIEKER)) {
                    // remember the location, so we can make it shriek when flying
                    String shrieker = new Location(world, x, y, z).toString();
                    TARDISTimeRotor.updateRotorRecord(id, shrieker);
                }
                if (type.equals(Material.NOTE_BLOCK)) {
                    // remember the location of this Disk Storage
                    String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 14, storage, 0);
                    // set block data to correct BARRIER + Item Display
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.DISK_STORAGE, world, x, y, z);
                }
                if (type.equals(Material.ORANGE_WOOL)) {
                    if (wall_type == Material.ORANGE_WOOL) {
                        data = TARDISConstants.BARRIER;
                        TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, x, y, z);
                    } else {
                        data = wall_type.createBlockData();
                    }
                }
                if (type.equals(Material.BLUE_WOOL)) {
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.BLUE_BOX, world, x, y, z);
                }
                if ((type.equals(Material.WARPED_FENCE) || type.equals(Material.CRIMSON_FENCE)) && tud.getSchematic().getPermission().equals("delta")) {
                    fractalBlocks.add(world.getBlockAt(x, y, z));
                }
                if (type.equals(Material.DEEPSLATE_REDSTONE_ORE) && (tud.getSchematic().getPermission().equals("division") || tud.getSchematic().getPermission().equals("hospital"))) {
                    // replace with gray concrete
                    data = tud.getSchematic().getPermission().equals("division") ? Material.GRAY_CONCRETE.createBlockData() : Material.LIGHT_GRAY_CONCRETE.createBlockData();
                    if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                        // remember the block to spawn an Ood on
                        postOod = new Location(world, x, y + 1, z);
                    }
                }
                if (level == 0 && type.equals(Material.PINK_STAINED_GLASS) && tud.getSchematic().getPermission().equals("division")) {
                    postLightBlocks.add(world.getBlockAt(x, y - 1, z));
                }
                if (type.equals(Material.WHITE_STAINED_GLASS) && tud.getSchematic().getPermission().equals("war")) {
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL, world, x, y, z);
                }
                if (type.equals(Material.WHITE_TERRACOTTA) && tud.getSchematic().getPermission().equals("war")) {
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL_OFFSET, world, x, y, z);
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
                    // set block data to correct BARRIER + Item Display
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.ADVANCED_CONSOLE, world, x, y, z);
                }
                if (type.equals(Material.CAKE)) {
                    /*
                     * This block will be converted to a lever by setBlock(),
                     * but remember it so we can use it as the handbrake!
                     */
                    String handbrakeloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 0, handbrakeloc, 0);
                }
                if (type.equals(Material.LIGHT)) {
                    // remember light block locations for malfunction and light switch
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
                        data = switch (tud.getSchematic().getPermission()) {
                            case "ender" -> Material.END_STONE_BRICKS.createBlockData();
                            case "delta", "cursed" -> Material.BLACKSTONE.createBlockData();
                            case "ancient", "bone", "fugitive" -> Material.GRAY_WOOL.createBlockData();
                            case "hospital" -> Material.LIGHT_GRAY_WOOL.createBlockData();
                            default -> Material.STONE_BRICKS.createBlockData();
                        };
                    }
                }
                if (type.equals(Material.OAK_BUTTON)) {
                    /*
                     * Remember it for the Artron Energy Capacitor.
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
                // if it's an iron/gold/diamond/emerald/beacon/redstone/bedrock/conduit/netherite block put it in the blocks table
                if (TARDISBuilderInstanceKeeper.getPrecious().contains(type)) {
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
                if (type.equals(Material.IRON_DOOR)) { // doors
                    postDoorBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (Tag.BEDS.isTagged(type)) {
                    postBedBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.LEVER)) {
                    postLeverBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.LANTERN) || type.equals(Material.SOUL_LANTERN)) {
                    postLanternBlocks.put(world.getBlockAt(x, y, z), data);
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
                } else if (Tag.WALL_SIGNS.isTagged(type) || (tud.getSchematic().getPermission().equals("cave") && type.equals(Material.OAK_SIGN))) {
                    postSignBlocks.put(world.getBlockAt(x, y, z), bb);
                } else if (type.equals(Material.POINTED_DRIPSTONE)) {
                    postDripstoneBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.GLOW_LICHEN)) {
                    postLichenBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.SCULK_VEIN)) {
                    postSculkVeinBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (TARDISStaticUtils.isBanner(type)) {
                    JsonObject state = bb.has("banner") ? bb.getAsJsonObject("banner") : null;
                    if (state != null) {
                        TARDISBannerData tbd = new TARDISBannerData(data, state);
                        postBannerBlocks.put(world.getBlockAt(x, y, z), tbd);
                    }
                } else if (type.equals(Material.PLAYER_HEAD) || type.equals(Material.PLAYER_WALL_HEAD)) {
                    TARDISBlockSetters.setBlock(world, x, y, z, data);
                    if (bb.has("head")) {
                        JsonObject head = bb.get("head").getAsJsonObject();
                        if (head.has("uuid")) {
                            try {
                                UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                                TARDISHeadSetter.textureSkull(plugin, uuid, head, world.getBlockAt(x, y, z));
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                    }
                } else if (TARDISMaterials.infested.contains(type)) {
                    // legacy monster egg stone for controls
                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                } else if (type.equals(Material.ICE) && tud.getSchematic().getPermission().equals("cave")) {
                    iceBlocks.add(world.getBlockAt(x, y, z));
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
                                plugin.getQueryFactory().insertSyncControl(id, 3, repeater, 0);
                            }
                            case 3 -> {
                                directional.setFacing(BlockFace.NORTH);
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                                plugin.getQueryFactory().insertSyncControl(id, 2, repeater, 0);
                            }
                            case 4 -> {
                                directional.setFacing(BlockFace.SOUTH);
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                                plugin.getQueryFactory().insertSyncControl(id, 5, repeater, 0);
                            }
                            default -> {
                                directional.setFacing(BlockFace.EAST);
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), directional);
                                plugin.getQueryFactory().insertSyncControl(id, 4, repeater, 0);
                            }
                        }
                        j++;
                    }
                } else if (type.equals(Material.SPONGE)) {
                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                } else {
                    BlockState state = world.getBlockAt(x, y, z).getState();
                    if (state instanceof BlockState) {
                        plugin.getTardisHelper().removeTileEntity(state);
                        TARDISBlockSetters.setBlock(world, x, y, z, data);
                    } else {
                        Block tmp = world.getBlockAt(x, y, z);
                        if (clean && !tmp.getType().equals(type) && !tmp.getType().isAir()) {
                            TARDISBlockSetters.setBlock(world, x, y, z, data);
                        } else if (!tmp.getType().equals(type)) {
                            TARDISBlockSetters.setBlock(world, x, y, z, data);
                        }
                    }
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

    private void setAir(int jx, int jy, int jz, World jw, int plusy) {
        for (int yy = jy; yy < (jy + plusy); yy++) {
            for (int xx = jx; xx < (jx + 16); xx++) {
                for (int zz = jz; zz < (jz + 16); zz++) {
                    Block b = jw.getBlockAt(xx, yy, zz);
                    b.setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }
}
