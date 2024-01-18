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
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgateDisplaySetter;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.rooms.TARDISPainting;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    private boolean running;
    private int id, slot, c, h, ph, w, level = 0, row = 0, startx, starty, startz, resetx, resetz, j = 2;
    private World world;
    private List<Chunk> chunks;
    private List<Chunk> previousChunks;
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
    private boolean downgrade = false;
    private Chunk chunk;
    private Player player;
    private Location ender = null;
    private Archive archive_next;
    private Archive archive_prev;
    private ConsoleSize size_next;
    private ConsoleSize size_prev;

    TARDISFullThemeRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud) {
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
                    plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
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
                    plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            set = new HashMap<>();
            where = new HashMap<>();
            if (archive_next == null) {
                // get JSON
                obj = TARDISSchematicGZip.getObject(plugin, "consoles", tud.getSchematic().getPermission(), tud.getSchematic().isCustom());
                if (obj == null) {
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
                size_next = tud.getSchematic().getConsoleSize();
            } else {
                obj = archive_next.getJSON();
                size_next = archive_next.getConsoleSize();
            }
            // get previous schematic dimensions
            int pw;
            if (archive_prev == null) {
                // get JSON
                JsonObject prevObj = TARDISSchematicGZip.getObject(plugin, "consoles", tud.getPrevious().getPermission(), tud.getPrevious().isCustom());
                if (prevObj == null) {
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
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
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getType().equals(EntityType.ENDER_CRYSTAL)) {
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
                }
                TARDISTimeRotor.updateRotorRecord(id, "");
                plugin.getGeneralKeeper().getTimeRotors().add(tardis.getRotor());
            }
            chunks = TARDISChunkUtils.getConsoleChunks(chunk, tud.getSchematic());
            previousChunks = TARDISChunkUtils.getConsoleChunks(chunk, tud.getPrevious());
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
                int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                startx = gsl[0];
                resetx = gsl[1];
                startz = gsl[2];
                resetz = gsl[3];
            }
            if (tud.getSchematic().getPermission().equals("archive")) {
                starty = archive_next.getY();
            } else if (tud.getSchematic().getPermission().equals("mechanical")) {
                starty = 62;
            } else if (TARDISConstants.HIGHER.contains(tud.getSchematic().getPermission())) {
                starty = 65;
            } else {
                starty = 64;
            }
            downgrade = (h < ph || w < pw);
            world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            if (tud.getPrevious().getPermission().equals("cave")) {
                // remove water source block
                Block water = world.getBlockAt(startx + 1, starty + 9, startz + 10);
                Block water2 = world.getBlockAt(startx + 8, starty + 14, startz + 8);
                Block water3 = world.getBlockAt(startx + 8, starty + 14, startz + 5);
                Block water4 = world.getBlockAt(startx + 8, starty + 13, startz + 5);
                water.setType(Material.AIR);
                water2.setType(Material.AIR);
                water3.setType(Material.AIR);
                water4.setType(Material.AIR);
            }
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
                        plugin.getMessenger().send(pv, TardisModule.TARDIS, "UPGRADE_TELEPORT");
                    }
                });
            }
            // clear lamps table as we'll be adding the new lamp positions later
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("lamps", wherel);
            previousChunks.forEach((c) -> {
                // remove any item display or interactions
                TARDISDisplayItemUtils.removeDisplaysInChunk(c, 62, 64 + ph);

            });
            plugin.getPM().callEvent(new TARDISDesktopThemeEvent(player, tardis, tud));
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            // remove items
            previousChunks.forEach((chink) -> {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
            // put on the door, redstone torches, signs, beds and the repeaters
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
            if (tud.getSchematic().getPermission().equals("cave")) {
                iceBlocks.forEach((ice) -> ice.setBlockData(TARDISConstants.WATER));
                iceBlocks.clear();
            }
            for (int f = 0; f < fractalBlocks.size(); f++) {
                FractalFence.grow(fractalBlocks.get(f), f);
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
                Entity ender_crystal = world.spawnEntity(ender, EntityType.ENDER_CRYSTAL);
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
                        plugin.debug("Invalid painting location! " + pl);
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
                    // set regular blocks for bedrock players
                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        TARDISFloodgateDisplaySetter.regularBlock(displays.get(i).getAsJsonObject(), wg1, id);
                    } else {
                        TARDISItemDisplaySetter.fakeBlock(displays.get(i).getAsJsonObject(), wg1, id);
                    }
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
            // jettison blocks if downgrading to smaller size
            if (downgrade) {
                if (tud.getPrevious().getPermission().equals("mechanical")) {
                    // remove the blocks at y = 62 & y = 63 under the console
                    for (Chunk u : previousChunks) {
                        // remove lower console blocks
                        int cx = u.getX() * 16;
                        int cz = u.getZ() * 16;
                        for (int y = 63; y > 61; y--) {
                            for (int col = cx; col < cx + 16; col++) {
                                for (int row = cz; row < cz + 16; row++) {
                                    u.getWorld().getBlockAt(row, y, col).setBlockData(TARDISConstants.AIR);
                                }
                            }
                        }
                    }
                }
                int minusy = (tud.getPrevious().getPermission().equals("mechanical")) ? 2 : 0;
                List<TARDISARSJettison> jettisons = getJettisons(size_next, size_prev, chunk);
                jettisons.forEach((jet) -> {
                    // remove the room
                    setAir(jet.getX(), jet.getY(), jet.getZ(), jet.getChunk().getWorld(), minusy, 16);
                });
                // also tidy up the space directly above the ARS centre slot
                int tidy = starty + h;
                int plus = 16 - h;
                setAir(chunk.getBlock(0, 64, 0).getX(), tidy, chunk.getBlock(0, 64, 0).getZ(), chunk.getWorld(), 0, plus);
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
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_RECOVERED", String.format("%d", refund));
                }
            } else if (tud.getSchematic().getPermission().equals("coral") && tud.getPrevious().getConsoleSize().equals(ConsoleSize.TALL)) {
                // clean up space above coral console
                int tidy = starty + h;
                int plus = 32 - h;
                chunks.forEach((chk) -> setAir(chk.getBlock(0, 64, 0).getX(), tidy, chk.getBlock(0, 64, 0).getZ(), chk.getWorld(), 0, plus));
            }
            // add / remove chunks from the chunks table
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("chunks", wherec);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // update chunks list in DB
                chunks.forEach((hunk) -> {
                    HashMap<String, Object> setc = new HashMap<>();
                    setc.put("tardis_id", id);
                    setc.put("world", world.getName());
                    setc.put("x", hunk.getX());
                    setc.put("z", hunk.getZ());
                    plugin.getQueryFactory().doInsert("chunks", setc);
                });
            }, 20L);
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPGRADE_FINISHED");
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
                Block b = world.getBlockAt(x, y, z);
                if (b.getType().equals(Material.BARRIER) || b.getType().equals(Material.LIGHT)) {
                    TARDISDisplayItemUtils.remove(b);
                }
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
                    postBedrock = b;
                }
                if (type.equals(Material.SCULK_SHRIEKER)) {
                    // remember the location so we can make it shriek when flying
                    String shrieker = new Location(world, x, y, z).toString();
                    TARDISTimeRotor.updateRotorRecord(id, shrieker);
                }
                if (type.equals(Material.NOTE_BLOCK)) {
                    // remember the location of this Disk Storage
                    String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 14, storage, 0);
                    // set block data to correct BARRIER + Display Item
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.DISK_STORAGE, world, x, y, z);
                }
                if (type.equals(Material.ORANGE_WOOL)) {
                    if (wall_type == Material.ORANGE_WOOL) {
                        // set regular blocks for bedrock players
                        if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                            data = TARDISConstants.BARRIER;
                            TARDISDisplayItemUtils.set(TARDISDisplayItem.HEXAGON, world, x, y, z);
                        }
                    } else {
                        data = wall_type.createBlockData();
                    }
                }
                if (type.equals(Material.BLUE_WOOL)) {
                    if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        data = TARDISConstants.BARRIER;
                        TARDISDisplayItemUtils.set(TARDISDisplayItem.BLUE_BOX, world, x, y, z);
                    }
                }
                if ((type.equals(Material.WARPED_FENCE) || type.equals(Material.CRIMSON_FENCE)) && tud.getSchematic().getPermission().equals("delta")) {
                    fractalBlocks.add(b);
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
                    if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        data = TARDISConstants.BARRIER;
                        TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL, world, x, y, z);
                    }
                }
                if (type.equals(Material.WHITE_TERRACOTTA) && tud.getSchematic().getPermission().equals("war")) {
                    if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        data = TARDISConstants.BARRIER;
                        TARDISDisplayItemUtils.set(TARDISDisplayItem.ROUNDEL_OFFSET, world, x, y, z);
                    }
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
                    // get current ARS json
                    HashMap<String, Object> wherer = new HashMap<>();
                    wherer.put("tardis_id", id);
                    ResultSetARS rsa = new ResultSetARS(plugin, wherer);
                    if (rsa.resultSet()) {
                        String[][][] existing = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                        if (downgrade) {
                            // reset slots to stone
                            switch (size_prev) {
                                case MASSIVE -> {
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
                                }
                                case TALL -> {
                                    // the 3 slots on the same level &
                                    existing[1][4][5] = "STONE";
                                    existing[1][5][4] = "STONE";
                                    existing[1][5][5] = "STONE";
                                    // the 4 slots on the level above
                                    existing[2][4][4] = "STONE";
                                    existing[2][4][5] = "STONE";
                                    existing[2][5][4] = "STONE";
                                    existing[2][5][5] = "STONE";
                                }
                                case MEDIUM -> {
                                    // the 3 slots on the same level
                                    existing[1][4][5] = "STONE";
                                    existing[1][5][4] = "STONE";
                                    existing[1][5][5] = "STONE";
                                }
                                default -> {
                                }
                                // SMALL size do nothing
                            }
                        }
                        // add control blocks
                        String control = tud.getSchematic().getSeedMaterial().toString();
                        existing[1][4][4] = control;
                        switch (size_next) {
                            case MASSIVE -> {
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
                            }
                            case TALL -> {
                                // the 3 slots on the same level &
                                existing[1][4][5] = control;
                                existing[1][5][4] = control;
                                existing[1][5][5] = control;
                                // the 4 slots on the level above
                                existing[2][4][4] = control;
                                existing[2][4][5] = control;
                                existing[2][5][4] = control;
                                existing[2][5][5] = control;
                            }
                            case MEDIUM -> {
                                // the 3 slots on the same level
                                existing[1][4][5] = control;
                                existing[1][5][4] = control;
                                existing[1][5][5] = control;
                            }
                            default -> {
                            }
                            // SMALL size do nothing
                        }
                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        JsonArray json = JsonParser.parseString(gson.toJson(existing)).getAsJsonArray();
                        HashMap<String, Object> seta = new HashMap<>();
                        seta.put("json", json.toString());
                        HashMap<String, Object> wheres = new HashMap<>();
                        wheres.put("tardis_id", id);
                        plugin.getQueryFactory().doUpdate("ars", seta, wheres);
                    }
                }
                if (type.equals(Material.LIGHT)) {
                    // remember light block locations for malfunction and light switch
                    HashMap<String, Object> setlb = new HashMap<>();
                    String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    setlb.put("tardis_id", id);
                    setlb.put("location", lloc);
                    plugin.getQueryFactory().doInsert("lamps", setlb);
                }
                if (type.equals(Material.ICE) && tud.getSchematic().getPermission().equals("cave")) {
                    iceBlocks.add(b);
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
                            case "ancient", "fugitive" -> Material.GRAY_WOOL.createBlockData();
                            case "hospital" -> Material.LIGHT_GRAY_WOOL.createBlockData();
                            default -> Material.STONE_BRICKS.createBlockData();
                        };
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
                    ender = b.getLocation().add(0.5d, 4d, 0.5d);
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
                    postDoorBlocks.put(b, data);
                } else if (Tag.BEDS.isTagged(type)) {
                    postBedBlocks.put(b, data);
                } else if (type.equals(Material.LEVER)) {
                    postLeverBlocks.put(b, data);
                } else if (type.equals(Material.REDSTONE_TORCH) || type.equals(Material.REDSTONE_WALL_TORCH)) {
                    postRedstoneTorchBlocks.put(b, data);
                } else if (type.equals(Material.TORCH) || type.equals(Material.WALL_TORCH) || type.equals(Material.SOUL_TORCH) || type.equals(Material.SOUL_WALL_TORCH)) {
                    postTorchBlocks.put(b, data);
                } else if (type.equals(Material.STICKY_PISTON)) {
                    postStickyPistonBaseBlocks.put(b, data);
                } else if (type.equals(Material.PISTON)) {
                    postPistonBaseBlocks.put(b, data);
                } else if (type.equals(Material.PISTON_HEAD)) {
                    postPistonExtensionBlocks.put(b, data);
                } else if (Tag.ALL_SIGNS.isTagged(type)) {
                    postSignBlocks.put(b, bb);
                } else if (type.equals(Material.POINTED_DRIPSTONE)) {
                    postDripstoneBlocks.put(b, data);
                } else if (type.equals(Material.GLOW_LICHEN)) {
                    postLichenBlocks.put(b, data);
                } else if (type.equals(Material.LANTERN) || type.equals(Material.SOUL_LANTERN)) {
                    postLanternBlocks.put(b, data);
                } else if (type.equals(Material.SCULK_VEIN)) {
                    postSculkVeinBlocks.put(b, data);
                } else if (TARDISStaticUtils.isBanner(type)) {
                    JsonObject state = bb.has("banner") ? bb.getAsJsonObject("banner") : null;
                    if (state != null) {
                        TARDISBannerData tbd = new TARDISBannerData(data, state);
                        postBannerBlocks.put(b, tbd);
                    }
                } else if (type.equals(Material.PLAYER_HEAD) || type.equals(Material.PLAYER_WALL_HEAD)) {
                    TARDISBlockSetters.setBlock(world, x, y, z, data);
                    if (bb.has("head")) {
                        JsonObject head = bb.get("head").getAsJsonObject();
                        if (head.has("uuid")) {
                            UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                            if (uuid != null) {
                                TARDISHeadSetter.textureSkull(plugin, uuid, head, b);
                            }
                        }
                    }
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
                            case 2 -> {
                                directional.setFacing(BlockFace.WEST);
                                postRepeaterBlocks.put(b, directional);
                                plugin.getQueryFactory().insertSyncControl(id, 3, repeater, 0);
                            }
                            case 3 -> {
                                directional.setFacing(BlockFace.NORTH);
                                postRepeaterBlocks.put(b, directional);
                                plugin.getQueryFactory().insertSyncControl(id, 2, repeater, 0);
                            }
                            case 4 -> {
                                directional.setFacing(BlockFace.SOUTH);
                                postRepeaterBlocks.put(b, directional);
                                plugin.getQueryFactory().insertSyncControl(id, 5, repeater, 0);
                            }
                            default -> {
                                directional.setFacing(BlockFace.EAST);
                                postRepeaterBlocks.put(b, directional);
                                plugin.getQueryFactory().insertSyncControl(id, 4, repeater, 0);
                            }
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
                    BlockState state = b.getState();
                    plugin.getTardisHelper().removeTileEntity(state);
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
            case MASSIVE -> {
                switch (next) {
                    case TALL -> {
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
                    }
                    case MEDIUM -> {
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
                    }
                    case SMALL -> {
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
                    }
                    default -> {
                    }
                    // same size do nothing
                }
            }
            case TALL -> {
                switch (next) {
                    case MEDIUM -> {
                        // the 4 chunks on the level above
                        list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 5));
                    }
                    case SMALL -> {
                        // the 3 chunks on the same level &
                        list.add(new TARDISARSJettison(chunk, 1, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 1, 5, 5));
                        // the 4 chunks on the level above
                        list.add(new TARDISARSJettison(chunk, 2, 4, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 4, 5));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 4));
                        list.add(new TARDISARSJettison(chunk, 2, 5, 5));
                    }
                    default -> {
                    }
                    // same size or bigger do nothing
                }
            }
            case MEDIUM -> {
                // same size or bigger do nothing
                if (next == ConsoleSize.SMALL) {// the 3 chunks on the same level
                    list.add(new TARDISARSJettison(chunk, 1, 4, 5));
                    list.add(new TARDISARSJettison(chunk, 1, 5, 4));
                    list.add(new TARDISARSJettison(chunk, 1, 5, 5));
                }
            }
            default -> {
            } // SMALL size do nothing
        }
        return list;
    }

    private void setAir(int jx, int jy, int jz, World jw, int minusy, int plusy) {
        for (int yy = jy - minusy; yy < (jy + plusy); yy++) {
            for (int xx = jx; xx < (jx + 16); xx++) {
                for (int zz = jz; zz < (jz + 16); zz++) {
                    Block b = jw.getBlockAt(xx, yy, zz);
                    BlockState state = b.getState();
                    plugin.getTardisHelper().removeTileEntity(state);
                    b.setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }
}
