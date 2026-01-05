/*
 * Copyright (C) 2026 eccentric_nz
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

import com.destroystokyo.paper.MaterialTags;
import com.google.gson.*;
import me.eccentric_nz.TARDIS.ARS.Jettison;
import me.eccentric_nz.TARDIS.ARS.ARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TARDISTIPSData;
import me.eccentric_nz.TARDIS.builders.utility.FractalFence;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.console.ConsoleDestroyer;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetScreenInteraction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.schematic.archive.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveByUse;
import me.eccentric_nz.TARDIS.schematic.setters.*;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.protection.TARDISProtectionRemover;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;

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
    private final HashMap<Block, BlockData> sidratFenceBlocks = new HashMap<>();
    private final HashMap<Block, JsonObject> postSignBlocks = new HashMap<>();
    private final HashMap<Block, TARDISBannerData> postBannerBlocks = new HashMap<>();
    private final List<Block> fractalBlocks = new ArrayList<>();
    private final List<Block> iceBlocks = new ArrayList<>();
    private final List<Block> postLightBlocks = new ArrayList<>();
    private boolean running;
    private int id, slot, c, height, previousHeight, width, level = 0, row = 0, startX, startY, startZ, resetX, resetZ, j = 2;
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
                ResultSetArchiveByUse rs = new ResultSetArchiveByUse(plugin, uuid.toString(), 1);
                if (rs.resultSet()) {
                    archive_next = rs.getArchive();
                } else {
                    // abort
                    Player cp = plugin.getServer().getPlayer(uuid);
                    plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
                    // reset next archive back to 0
                    new ArchiveReset(plugin, uuid.toString(), 1, 0).resetUse();
                    if (tud.getPrevious().getPermission().equals("archive")) {
                        // reset previous back to 1
                        new ArchiveReset(plugin, uuid.toString(), 2, 1).resetUse();
                    }
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            if (tud.getPrevious().getPermission().equals("archive")) {
                ResultSetArchiveByUse rs = new ResultSetArchiveByUse(plugin, uuid.toString(), 2);
                if (rs.resultSet()) {
                    archive_prev = rs.getArchive();
                } else {
                    // abort
                    Player cp = plugin.getServer().getPlayer(uuid);
                    plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
                    if (tud.getSchematic().getPermission().equals("archive")) {
                        // reset next back to 0
                        new ArchiveReset(plugin, uuid.toString(), 1, 0).resetUse();
                    }
                    // reset previous archive back to 1
                    new ArchiveReset(plugin, uuid.toString(), 2, 1).resetUse();
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
            int previousWidth;
            if (archive_prev == null) {
                // get JSON
                JsonObject prevObj = TARDISSchematicGZip.getObject(plugin, "consoles", tud.getPrevious().getPermission(), tud.getPrevious().isCustom());
                if (prevObj == null) {
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
                JsonObject prevDimensions = prevObj.get("dimensions").getAsJsonObject();
                previousHeight = prevDimensions.get("height").getAsInt();
                previousWidth = prevDimensions.get("width").getAsInt();
                size_prev = tud.getPrevious().getConsoleSize();
            } else {
                JsonObject dimensions = archive_prev.getJSON().get("dimensions").getAsJsonObject();
                previousHeight = dimensions.get("height").getAsInt();
                previousWidth = dimensions.get("width").getAsInt();
                size_prev = archive_prev.getConsoleSize();
            }
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            height = dimensions.get("height").getAsInt();
            width = dimensions.get("width").getAsInt();
            c = dimensions.get("length").getAsInt();
            // calculate startX, startY, startZ
            HashMap<String, Object> whereT = new HashMap<>();
            whereT.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, whereT, "", false);
            if (!rs.resultSet()) {
                // abort and return energy
                HashMap<String, Object> whereA = new HashMap<>();
                whereA.put("uuid", uuid.toString());
                int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission());
                plugin.getQueryFactory().alterEnergyLevel("tardis", amount, whereA, player);
                if (tud.getSchematic().getPermission().equals("archive")) {
                    // reset next archive back to 0
                    new ArchiveReset(plugin, uuid.toString(), 1, 0).resetUse();
                }
                if (tud.getPrevious().getPermission().equals("archive")) {
                    // reset previous back to 1
                    new ArchiveReset(plugin, uuid.toString(), 2, 1).resetUse();
                }
            }
            Tardis tardis = rs.getTardis();
            slot = tardis.getTIPS();
            id = tardis.getTardisId();
            chunk = TARDISStaticLocationGetters.getChunk(tardis.getChunk());
            if (tud.getPrevious().getPermission().equals("ender")) {
                // remove ender crystal
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getType().equals(EntityType.END_CRYSTAL)) {
                        entity.remove();
                    }
                }
            }
            // check for relativity differentiator
            HashMap<String, Object> whereRelDiff = new HashMap<>();
            whereRelDiff.put("tardis_id", id);
            whereRelDiff.put("type", 47);
            plugin.getQueryFactory().doDelete("controls", whereRelDiff);
            if (tardis.getRotor() != null) {
                // remove item frame and delete UUID in db
                ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                // only if entity still exists
                if (itemFrame != null) {
                    if (tud.getPrevious().getPermission().equals("mechanical")) {
                        // remove the engine item frame
                        ItemFrame engine = ItemFrameSetter.getItemFrameFromLocation(itemFrame.getLocation().add(0, -4, 0));
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
            // remove console if present
            ResultSetScreenInteraction rssi = new ResultSetScreenInteraction(plugin, id);
            if (rssi.resultSet() && rssi.getUuid() != null) {
                Entity screen = plugin.getServer().getEntity(rssi.getUuid());
                if (screen != null && screen.getPersistentDataContainer().has(plugin.getUnaryKey(), PersistentDataType.STRING)) {
                    String uuids = screen.getPersistentDataContainer().get(plugin.getUnaryKey(), PersistentDataType.STRING);
                    if (uuids != null) {
                        // remove the console
                        new ConsoleDestroyer(plugin).returnStack(uuids, id);
                    }
                }
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
                TARDISInteriorPostioning tintPos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintPos.getTIPSData(slot);
                startX = pos.getCentreX();
                resetX = pos.getCentreX();
                startZ = pos.getCentreZ();
                resetZ = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
                startX = gsl[0];
                resetX = gsl[1];
                startZ = gsl[2];
                resetZ = gsl[3];
            }
            if (tud.getSchematic().getPermission().equals("archive")) {
                startY = archive_next.getY();
            } else {
                startY = tud.getSchematic().getStartY();
            }
            downgrade = (height < previousHeight || width < previousWidth);
            world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            if (tud.getPrevious().getPermission().equals("cave")) {
                // remove water source block
                Block water = world.getBlockAt(startX + 1, startY + 9, startZ + 10);
                Block water2 = world.getBlockAt(startX + 8, startY + 14, startZ + 8);
                Block water3 = world.getBlockAt(startX + 8, startY + 14, startZ + 5);
                Block water4 = world.getBlockAt(startX + 8, startY + 13, startZ + 5);
                water.setType(Material.AIR);
                water2.setType(Material.AIR);
                water3.setType(Material.AIR);
                water4.setType(Material.AIR);
            }
            own_world = plugin.getConfig().getBoolean("creation.create_worlds");
            wg1 = new Location(world, startX, startY, startZ);
            wg2 = new Location(world, startX + (width - 1), startY + (height - 1), startZ + (c - 1));
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
            HashMap<String, Object> whereV = new HashMap<>();
            whereV.put("tardis_id", id);
            ResultSetTravellers rsv = new ResultSetTravellers(plugin, whereV, true);
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
            HashMap<String, Object> whereL = new HashMap<>();
            whereL.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("lamps", whereL);
            previousChunks.forEach((c) -> {
                // remove any item display or interactions
                TARDISDisplayItemUtils.removeEntitiesInChunk(c, 62, 64 + previousHeight);
            });
            plugin.getPM().callEvent(new TARDISDesktopThemeEvent(player, tardis, tud));
        }
        if (level == (height - 1) && row == (width - 1)) {
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
            postStickyPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonBaseBlocks.forEach(Block::setBlockData);
            postPistonExtensionBlocks.forEach(Block::setBlockData);
            SignSetter.setSigns(postSignBlocks, plugin, id);
            BannerSetter.setBanners(postBannerBlocks);
            SIDRATFenceSetter.update(sidratFenceBlocks);
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
                Entity ender_crystal = world.spawnEntity(ender, EntityType.END_CRYSTAL);
                ((EnderCrystal) ender_crystal).setShowingBottom(false);
            }
            // mannequins
            if (obj.has("mannequins")) {
                JsonArray mannequins = obj.get("mannequins").getAsJsonArray();
                MannequinSetter.setMannequins(mannequins, world, resetX, startY, resetZ);
            }
            // armour stands
            if (obj.has("armour_stands")) {
                JsonArray stands = obj.get("armour_stands").getAsJsonArray();
                ArmourStandSetter.setStands(stands, world, resetX, startY, resetZ);
            }
            // paintings
            if (obj.has("paintings")) {
                JsonArray paintings = (JsonArray) obj.get("paintings");
                PaintingSetter.setArt(paintings, world, resetX, startY, resetZ);
            }
            // item frames
            if (obj.has("item_frames")) {
                JsonArray frames = obj.get("item_frames").getAsJsonArray();
                for (int i = 0; i < frames.size(); i++) {
                    ItemFrameSetter.curate(frames.get(i).getAsJsonObject(), wg1, id);
                }
            }
            // item displays
            if (obj.has("item_displays")) {
                JsonArray displays = obj.get("item_displays").getAsJsonArray();
                ItemDisplaySetter.process(displays, player, wg1, id);
            }
            // finished processing - update tardis table!
            if (!set.isEmpty()) {
                where.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("tardis", set, where);
            }
            // reset archive use back to 0
            if (tud.getPrevious().getPermission().equals("archive") && !tud.getSchematic().getPermission().equals("archive")) {
                new ArchiveReset(plugin, uuid.toString(), 1, 0).resetUse();
            }
            // jettison blocks if downgrading to smaller size
            if (downgrade) {
                if (tud.getPrevious().getPermission().equals("mechanical") || tud.getPrevious().getPermission().equals("cursed")) {
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
                int minusY = (tud.getPrevious().getPermission().equals("mechanical")) ? 2 : 0;
                List<Jettison> jettisons = getJettisons(size_next, size_prev, chunk);
                jettisons.forEach((jet) -> {
                    // remove the room
                    setAir(jet.getX(), jet.getLevel(), jet.getZ(), jet.getChunk().getWorld(), minusY, 16);
                });
                // also tidy up the space directly above the ARS centre slot
                int tidy = startY + height;
                int plus = 16 - height;
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
                HashMap<String, Object> setR = new HashMap<>();
                setR.put("tardis_id", id);
                plugin.getQueryFactory().alterEnergyLevel("tardis", refund, setR, null);
                if (player.isOnline()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_RECOVERED", String.format("%d", refund));
                }
            } else if (tud.getSchematic().getPermission().equals("coral") && tud.getPrevious().getConsoleSize().equals(ConsoleSize.TALL)) {
                // clean up space above coral console
                int tidy = startY + height;
                int plus = 32 - height;
                chunks.forEach((chk) -> setAir(chk.getBlock(0, 64, 0).getX(), tidy, chk.getBlock(0, 64, 0).getZ(), chk.getWorld(), 0, plus));
            }
            // add / remove chunks from the chunks table
            HashMap<String, Object> whereC = new HashMap<>();
            whereC.put("tardis_id", id);
            plugin.getQueryFactory().doDelete("chunks", whereC);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // update chunks list in DB
                chunks.forEach((hunk) -> {
                    HashMap<String, Object> setC = new HashMap<>();
                    setC.put("tardis_id", id);
                    setC.put("world", world.getName());
                    setC.put("x", hunk.getX());
                    setC.put("z", hunk.getZ());
                    plugin.getQueryFactory().doInsert("chunks", setC);
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
                int x = startX + row;
                int y = startY + level;
                int z = startZ + col;
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
                    String upper = split2[1].toUpperCase(Locale.ROOT);
                    type = Material.valueOf(upper);
                    data = plugin.getServer().createBlockData(type);
                }
                if (type.equals(Material.BEDROCK)) {
                    // remember bedrock location to block off the beacon light
                    String bedrockLoc = world.getName() + ":" + x + ":" + y + ":" + z;
                    set.put("beacon", bedrockLoc);
                    postBedrock = b;
                }
                if (type.equals(Material.LIGHT_GRAY_CONCRETE) && (tud.getSchematic().getPermission().equals("bone") || tud.getSchematic().getPermission().equals("rustic"))) {
                    // get the block
                    Block block = new Location(world, x, y, z).getBlock();
                    // build a console
                    String ct = (tud.getSchematic().getPermission().equals("bone")) ? "console_light_gray" : "console_rustic";
                    new ConsoleBuilder(plugin).create(block, ct, id, uuid.toString());
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
                    // set block data to correct BARRIER + Display Item
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, world, x, y, z);
                }
                if (type.equals(Material.ORANGE_WOOL)) {
                    if (wall_type == Material.ORANGE_WOOL) {
                        // set regular blocks for bedrock players
                        if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                            data = TARDISConstants.BARRIER;
                            TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.HEXAGON, world, x, y, z);
                        }
                    } else {
                        data = wall_type.createBlockData();
                    }
                }
                if (type.equals(Material.BLUE_WOOL)) {
                    if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        data = TARDISConstants.BARRIER;
                        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.BLUE_BOX, world, x, y, z);
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
                        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ROUNDEL, world, x, y, z);
                    }
                }
                if (type.equals(Material.WHITE_TERRACOTTA) && tud.getSchematic().getPermission().equals("war")) {
                    if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        data = TARDISConstants.BARRIER;
                        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ROUNDEL_OFFSET, world, x, y, z);
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
                        HashMap<String, Object> setD = new HashMap<>();
                        String doorLoc = world.getName() + ":" + x + ":" + y + ":" + z;
                        setD.put("door_location", doorLoc);
                        HashMap<String, Object> whereD = new HashMap<>();
                        whereD.put("tardis_id", id);
                        whereD.put("door_type", 1);
                        plugin.getQueryFactory().doUpdate("doors", setD, whereD);
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
                if (type.equals(Material.JUKEBOX) && !(tud.getSchematic().getPermission().equals("eighth") && world.getBlockAt(x,y,z).getRelative(BlockFace.DOWN).getType() == Material.ANDESITE)) {
                    // remember the location of this Advanced Console
                    String advanced = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 15, advanced, 0);
                    // set block data to correct BARRIER + Item Display
                    data = TARDISConstants.BARRIER;
                    TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ADVANCED_CONSOLE, world, x, y, z);
                }
                if (type.equals(Material.CAKE)) {
                    /*
                     * This block will be converted to a lever by setBlock(),
                     * but remember it so we can use it as the handbrake!
                     * Bone and Rustic have modelled consoles, not a lever handbrake,
                     * so remove it to avoid unintentional bugs.
                     */
                    if (tud.getSchematic().getPermission().equals("rustic") || tud.getSchematic().getPermission().equals("bone")) {
                        // delete handbrake record
                        HashMap<String, Object> whereD = new HashMap<>();
                        whereD.put("tardis_id", id);
                        whereD.put("type", 0);
                        whereD.put("secondary", 0);
                        plugin.getQueryFactory().doDelete("controls", whereD);
                    } else {
                        // upsert handbrake record
                        String handbrakeLoc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                        plugin.getQueryFactory().insertSyncControl(id, 0, handbrakeLoc, 0);
                    }
                    if (tud.getSchematic().getPermission().equals("sidrat")) {
                        // cake -> handbrake
                        BlockData blockData = Material.LEVER.createBlockData();
                        Switch lever = (Switch) blockData;
                        lever.setAttachedFace(FaceAttachable.AttachedFace.WALL);
                        lever.setFacing(BlockFace.WEST);
                        data = lever;
                    }
                    // get current ARS json
                    HashMap<String, Object> whereR = new HashMap<>();
                    whereR.put("tardis_id", id);
                    ResultSetARS rsa = new ResultSetARS(plugin, whereR);
                    if (rsa.resultSet()) {
                        String[][][] existing = ARSMethods.getGridFromJSON(rsa.getJson());
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
                    HashMap<String, Object> setLB = new HashMap<>();
                    String lightLoc = world.getName() + ":" + x + ":" + y + ":" + z;
                    setLB.put("tardis_id", id);
                    setLB.put("location", lightLoc);
                    plugin.getQueryFactory().doInsert("lamps", setLB);
                }
                if (type.equals(Material.ICE) && tud.getSchematic().getPermission().equals("cave")) {
                    iceBlocks.add(b);
                }
                if (type.equals(Material.PALE_OAK_FENCE) && tud.getSchematic().getPermission().equals("sidrat")) {
                    sidratFenceBlocks.put(b, data);
                }
                if (type.equals(Material.COMMAND_BLOCK) || ((tud.getSchematic().getPermission().equals("bigger") || tud.getSchematic().getPermission().equals("coral") || tud.getSchematic().getPermission().equals("deluxe") || tud.getSchematic().getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                    /*
                     * command block - remember it to spawn the creeper on.
                     * could also be a beacon block, as the creeper sits over
                     * the beacon in the deluxe and bigger consoles.
                     */
                    String creepLoc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                    set.put("creeper", creepLoc);
                    if (type.equals(Material.COMMAND_BLOCK)) {
                        data = switch (tud.getSchematic().getPermission()) {
                            case "ender" -> Material.END_STONE_BRICKS.createBlockData();
                            case "delta", "cursed" -> Material.BLACKSTONE.createBlockData();
                            case "ancient", "bone", "fugitive" -> Material.GRAY_WOOL.createBlockData();
                            case "hospital" -> Material.LIGHT_GRAY_WOOL.createBlockData();
                            case "sidrat" -> Material.RED_CONCRETE.createBlockData();
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
                    String woodButtonLoc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 6, woodButtonLoc, 0);
                }
                if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                    /*
                     * remember the telepathic circuit.
                     */
                    String telepathicLoc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    plugin.getQueryFactory().insertSyncControl(id, 23, telepathicLoc, 0);
                }
                if (type.equals(Material.BEACON) && tud.getSchematic().getPermission().equals("ender")) {
                    /*
                     * get the ender crystal location
                     */
                    ender = b.getLocation().add(0.5d, 4d, 0.5d);
                }
                // if it's an iron/gold/diamond/emerald/beacon/redstone/bedrock/conduit/netherite block put it in the blocks table
                if (TARDISBuilderInstanceKeeper.getPrecious().contains(type)) {
                    HashMap<String, Object> setPB = new HashMap<>();
                    String loc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    setPB.put("tardis_id", id);
                    setPB.put("location", loc);
                    setPB.put("data", "minecraft:air");
                    setPB.put("police_box", 0);
                    plugin.getQueryFactory().doInsert("blocks", setPB);
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
                            try {
                                UUID uuid = UUID.fromString(head.get("uuid").getAsString());
                                HeadSetter.textureSkull(plugin, uuid, head, b);
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                    }
                } else if (type.equals(Material.DECORATED_POT)) {
                    TARDISBlockSetters.setBlock(world, x, y, z, data);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (bb.has("pot")) {
                            JsonObject pot = bb.get("pot").getAsJsonObject();
                            PotSetter.decorate(plugin, pot, b);
                        }
                    }, 1L);
                } else if (MaterialTags.INFESTED_BLOCKS.isTagged(type)) {
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
            if (row < width) {
                row++;
            }
            if (row == width && level < height) {
                row = 0;
                level++;
            }
        }
    }

    private List<Jettison> getJettisons(ConsoleSize next, ConsoleSize prev, Chunk chunk) {
        List<Jettison> list = new ArrayList<>();
        switch (prev) {
            case MASSIVE -> {
                switch (next) {
                    case TALL -> {
                        // the 5 chunks on the same level &
                        list.add(new Jettison(chunk, 1, 4, 6));
                        list.add(new Jettison(chunk, 1, 5, 6));
                        list.add(new Jettison(chunk, 1, 6, 4));
                        list.add(new Jettison(chunk, 1, 6, 5));
                        list.add(new Jettison(chunk, 1, 6, 6));
                        // the 5 chunks on the level above
                        list.add(new Jettison(chunk, 2, 4, 6));
                        list.add(new Jettison(chunk, 2, 5, 6));
                        list.add(new Jettison(chunk, 2, 6, 4));
                        list.add(new Jettison(chunk, 2, 6, 5));
                        list.add(new Jettison(chunk, 2, 6, 6));
                    }
                    case MEDIUM -> {
                        // the 5 chunks on the same level &
                        list.add(new Jettison(chunk, 1, 4, 6));
                        list.add(new Jettison(chunk, 1, 5, 6));
                        list.add(new Jettison(chunk, 1, 6, 4));
                        list.add(new Jettison(chunk, 1, 6, 5));
                        list.add(new Jettison(chunk, 1, 6, 6));
                        // the 9 chunks on the level above
                        list.add(new Jettison(chunk, 2, 4, 4));
                        list.add(new Jettison(chunk, 2, 4, 5));
                        list.add(new Jettison(chunk, 2, 4, 6));
                        list.add(new Jettison(chunk, 2, 5, 4));
                        list.add(new Jettison(chunk, 2, 5, 5));
                        list.add(new Jettison(chunk, 2, 5, 6));
                        list.add(new Jettison(chunk, 2, 6, 4));
                        list.add(new Jettison(chunk, 2, 6, 5));
                        list.add(new Jettison(chunk, 2, 6, 6));
                    }
                    case SMALL -> {
                        // the 8 chunks on the same level &
                        list.add(new Jettison(chunk, 1, 4, 5));
                        list.add(new Jettison(chunk, 1, 4, 6));
                        list.add(new Jettison(chunk, 1, 5, 4));
                        list.add(new Jettison(chunk, 1, 5, 5));
                        list.add(new Jettison(chunk, 1, 5, 6));
                        list.add(new Jettison(chunk, 1, 6, 4));
                        list.add(new Jettison(chunk, 1, 6, 5));
                        list.add(new Jettison(chunk, 1, 6, 6));
                        // the 9 chunks on the level above
                        list.add(new Jettison(chunk, 2, 4, 4));
                        list.add(new Jettison(chunk, 2, 4, 5));
                        list.add(new Jettison(chunk, 2, 4, 6));
                        list.add(new Jettison(chunk, 2, 5, 4));
                        list.add(new Jettison(chunk, 2, 5, 5));
                        list.add(new Jettison(chunk, 2, 5, 6));
                        list.add(new Jettison(chunk, 2, 6, 4));
                        list.add(new Jettison(chunk, 2, 6, 5));
                        list.add(new Jettison(chunk, 2, 6, 6));
                    }
                    case WIDE -> {
                        // the 9 chunks on the level above
                        list.add(new Jettison(chunk, 2, 4, 4));
                        list.add(new Jettison(chunk, 2, 4, 5));
                        list.add(new Jettison(chunk, 2, 4, 6));
                        list.add(new Jettison(chunk, 2, 5, 4));
                        list.add(new Jettison(chunk, 2, 5, 5));
                        list.add(new Jettison(chunk, 2, 5, 6));
                        list.add(new Jettison(chunk, 2, 6, 4));
                        list.add(new Jettison(chunk, 2, 6, 5));
                        list.add(new Jettison(chunk, 2, 6, 6));
                    }
                    default -> {
                    } // same size do nothing
                }
            }
            case TALL -> {
                switch (next) {
                    case MEDIUM, WIDE -> {
                        // the 4 chunks on the level above
                        list.add(new Jettison(chunk, 2, 4, 4));
                        list.add(new Jettison(chunk, 2, 4, 5));
                        list.add(new Jettison(chunk, 2, 5, 4));
                        list.add(new Jettison(chunk, 2, 5, 5));
                    }
                    case SMALL -> {
                        // the 3 chunks on the same level &
                        list.add(new Jettison(chunk, 1, 4, 5));
                        list.add(new Jettison(chunk, 1, 5, 4));
                        list.add(new Jettison(chunk, 1, 5, 5));
                        // the 4 chunks on the level above
                        list.add(new Jettison(chunk, 2, 4, 4));
                        list.add(new Jettison(chunk, 2, 4, 5));
                        list.add(new Jettison(chunk, 2, 5, 4));
                        list.add(new Jettison(chunk, 2, 5, 5));
                    }
                    default -> {
                    } // same size or bigger do nothing
                }
            }
            case WIDE -> {
                switch (next) {
                    case TALL, MEDIUM -> {
                        // the 5 chunks on the same level
                        list.add(new Jettison(chunk, 1, 4, 6));
                        list.add(new Jettison(chunk, 1, 5, 6));
                        list.add(new Jettison(chunk, 1, 6, 4));
                        list.add(new Jettison(chunk, 1, 6, 5));
                        list.add(new Jettison(chunk, 1, 6, 6));
                    }
                    case SMALL -> {
                        // the 8 chunks on the same level
                        list.add(new Jettison(chunk, 1, 4, 5));
                        list.add(new Jettison(chunk, 1, 4, 6));
                        list.add(new Jettison(chunk, 1, 5, 4));
                        list.add(new Jettison(chunk, 1, 5, 5));
                        list.add(new Jettison(chunk, 1, 5, 6));
                        list.add(new Jettison(chunk, 1, 6, 4));
                        list.add(new Jettison(chunk, 1, 6, 5));
                        list.add(new Jettison(chunk, 1, 6, 6));
                    }
                    default -> {
                    } // same size or bigger do nothing
                }
            }
            case MEDIUM -> {
                // the 3 chunks on the same level
                if (next == ConsoleSize.SMALL) {// the 3 chunks on the same level
                    list.add(new Jettison(chunk, 1, 4, 5));
                    list.add(new Jettison(chunk, 1, 5, 4));
                    list.add(new Jettison(chunk, 1, 5, 5));
                }
            }
            default -> {
            } // same size or bigger do nothing
        }
        return list;
    }

    private void setAir(int jx, int jy, int jz, World jw, int minusY, int plusY) {
        for (int yy = jy - minusY; yy < (jy + plusY); yy++) {
            for (int xx = jx; xx < (jx + 16); xx++) {
                for (int zz = jz; zz < (jz + 16); zz++) {
                    Block b = jw.getBlockAt(xx, yy, zz);
                    // reset the biome in case it was an EYE room
                    world.setBiome(xx, yy, zz, Biome.THE_VOID);
                    BlockState state = b.getState();
                    plugin.getTardisHelper().removeTileEntity(state);
                    b.setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }
}
