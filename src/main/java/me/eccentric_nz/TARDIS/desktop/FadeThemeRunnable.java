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

import com.google.gson.*;
import me.eccentric_nz.TARDIS.ARS.ARSMethods;
import me.eccentric_nz.TARDIS.ARS.JettisonSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.TARDIS.builders.interior.Fade;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPositioning;
import me.eccentric_nz.TARDIS.builders.interior.TIPSData;
import me.eccentric_nz.TARDIS.console.ConsoleDestroyer;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetScreenInteraction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.FollowerSpawner;
import me.eccentric_nz.TARDIS.rotors.TimeRotor;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import me.eccentric_nz.TARDIS.schematic.archive.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveByUse;
import me.eccentric_nz.TARDIS.schematic.setters.ItemFrameSetter;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.protection.TARDISProtectionRemover;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
 *
 * @author eccentric_nz
 */
public class FadeThemeRunnable extends DesktopThemeRunnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final UpgradeData tud;
    private int id;
    private int slot;
    private int height;
    private int previousHeight;
    private int startY;
    private World world;
    private List<Chunk> chunks;
    private List<Chunk> previousChunks;
    private HashMap<String, Object> set;
    private HashMap<String, Object> where;
    private Location wg1;
    private Location wg2;
    private Location location;
    private boolean downgrade = false;
    private Chunk chunk;
    private Player player;
    private Archive archive_prev;
    private ConsoleSize size_next;
    private ConsoleSize size_prev;
    int loop = 0;

    FadeThemeRunnable(TARDIS plugin, UUID uuid, UpgradeData tud) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
    }

    @Override
    public void run() {
        // initialise
        if (loop == 0) {
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
            size_next = ConsoleSize.MEDIUM;
            // get previous schematic dimensions
            int previousWidth;
            if (archive_prev == null) {
                // get JSON
                JsonObject prevObj = SchematicGZip.getObject(plugin, "consoles", tud.getPrevious().getPermission(), tud.getPrevious().isCustom());
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
            height = 16;
            int width = 32;
            int length = 32;
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
                ItemFrame itemFrame = TimeRotor.getItemFrame(tardis.getRotor());
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
                TimeRotor.updateRotorRecord(id, "");
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
            chunks = ChunkUtils.getConsoleChunks(chunk, tud.getSchematic());
            previousChunks = ChunkUtils.getConsoleChunks(chunk, tud.getPrevious());
            if (!tardis.getCreeper().isEmpty()) {
                Location creeper = TARDISStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
                if (creeper != null) {
                    if (tud.getPrevious().getPermission().equals("division") || tud.getPrevious().getPermission().equals("hospital")) {
                        // remove ood
                        new FollowerSpawner(plugin).removeDivisionOod(creeper);
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
            }
            int startX;
            int startZ;
            if (slot != -1000001) { // default world - use TIPS
                TARDISInteriorPositioning tintPos = new TARDISInteriorPositioning(plugin);
                TIPSData pos = tintPos.getTIPSData(slot);
                startX = pos.getCentreX();
                startZ = pos.getCentreZ();
            } else {
                int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
                startX = gsl[0];
                startZ = gsl[2];
            }
            world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            startY = tud.getSchematic().getStartY();
            location = new Location(world, startX, startY, startZ);
            downgrade = (height < previousHeight || width < previousWidth);
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
            wg1 = new Location(world, startX, startY, startZ);
            wg2 = new Location(world, startX + (width - 1), startY + (height - 1), startZ + (length - 1));
            // clear existing precious blocks and remove the entries from the protection map
            new TARDISProtectionRemover(plugin).cleanInteriorBlocks(id);
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
            previousChunks.forEach((chunk) -> {
                // remove any item display or interactions
                TARDISDisplayItemUtils.removeEntitiesInChunk(chunk, 62, 64 + previousHeight);
            });
            plugin.getPM().callEvent(new TARDISDesktopThemeEvent(player, tardis, tud));
            loop++;
        } else if (loop == 1) {
            // build
            new Fade(plugin, tud.getVariant(), location, id, player).build(false);
            // remove items
            chunks.forEach((chink) -> {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
            loop++;
        } else if (loop == 2) {
            // we're finished
            // get current ARS JSON
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
                        case WIDE -> {
                            // the 8 slots on the same level
                            existing[1][4][5] = "STONE";
                            existing[1][4][6] = "STONE";
                            existing[1][5][4] = "STONE";
                            existing[1][5][5] = "STONE";
                            existing[1][5][6] = "STONE";
                            existing[1][6][4] = "STONE";
                            existing[1][6][5] = "STONE";
                            existing[1][6][6] = "STONE";
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
                            // SMALL size do nothing
                        }
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
                    case WIDE -> {
                        // the 8 slots on the same level
                        existing[1][4][5] = control;
                        existing[1][4][6] = control;
                        existing[1][5][4] = control;
                        existing[1][5][5] = control;
                        existing[1][5][6] = control;
                        existing[1][6][4] = control;
                        existing[1][6][5] = control;
                        existing[1][6][6] = control;
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
                        // SMALL size do nothing
                    }
                }
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                JsonArray json = JsonParser.parseString(gson.toJson(existing)).getAsJsonArray();
                HashMap<String, Object> seta = new HashMap<>();
                seta.put("json", json.toString());
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("ars", seta, wheres);
            }
            // remove items
            previousChunks.forEach((chink) -> {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                if (slot == -1) {
                    plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
                }
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
                List<JettisonSlot> jettisons = getJettisons(size_next, size_prev, chunk);
                jettisons.forEach((jet) -> {
                    // remove the room
                    setAir(jet.getX(), jet.getY(), jet.getZ(), jet.getChunk().getWorld(), minusY, 16);
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
                    setC.put("world", world.getKey().asString());
                    setC.put("x", hunk.getX());
                    setC.put("z", hunk.getZ());
                    plugin.getQueryFactory().doInsert("chunks", setC);
                });
            }, 20L);
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPGRADE_FINISHED");
        }
    }

    private List<JettisonSlot> getJettisons(ConsoleSize next, ConsoleSize prev, Chunk chunk) {
        List<JettisonSlot> list = new ArrayList<>();
        switch (prev) {
            case MASSIVE -> {
                switch (next) {
                    case WIDE -> {
                        // the 9 chunks on the level above
                        list.add(new JettisonSlot(chunk, 2, 4, 4));
                        list.add(new JettisonSlot(chunk, 2, 4, 5));
                        list.add(new JettisonSlot(chunk, 2, 4, 6));
                        list.add(new JettisonSlot(chunk, 2, 5, 4));
                        list.add(new JettisonSlot(chunk, 2, 5, 5));
                        list.add(new JettisonSlot(chunk, 2, 5, 6));
                        list.add(new JettisonSlot(chunk, 2, 6, 4));
                        list.add(new JettisonSlot(chunk, 2, 6, 5));
                        list.add(new JettisonSlot(chunk, 2, 6, 6));
                    }
                    case TALL -> {
                        // the 5 chunks on the same level &
                        list.add(new JettisonSlot(chunk, 1, 4, 6));
                        list.add(new JettisonSlot(chunk, 1, 5, 6));
                        list.add(new JettisonSlot(chunk, 1, 6, 4));
                        list.add(new JettisonSlot(chunk, 1, 6, 5));
                        list.add(new JettisonSlot(chunk, 1, 6, 6));
                        // the 5 chunks on the level above
                        list.add(new JettisonSlot(chunk, 2, 4, 6));
                        list.add(new JettisonSlot(chunk, 2, 5, 6));
                        list.add(new JettisonSlot(chunk, 2, 6, 4));
                        list.add(new JettisonSlot(chunk, 2, 6, 5));
                        list.add(new JettisonSlot(chunk, 2, 6, 6));
                    }
                    case MEDIUM -> {
                        // the 5 chunks on the same level &
                        list.add(new JettisonSlot(chunk, 1, 4, 6));
                        list.add(new JettisonSlot(chunk, 1, 5, 6));
                        list.add(new JettisonSlot(chunk, 1, 6, 4));
                        list.add(new JettisonSlot(chunk, 1, 6, 5));
                        list.add(new JettisonSlot(chunk, 1, 6, 6));
                        // the 9 chunks on the level above
                        list.add(new JettisonSlot(chunk, 2, 4, 4));
                        list.add(new JettisonSlot(chunk, 2, 4, 5));
                        list.add(new JettisonSlot(chunk, 2, 4, 6));
                        list.add(new JettisonSlot(chunk, 2, 5, 4));
                        list.add(new JettisonSlot(chunk, 2, 5, 5));
                        list.add(new JettisonSlot(chunk, 2, 5, 6));
                        list.add(new JettisonSlot(chunk, 2, 6, 4));
                        list.add(new JettisonSlot(chunk, 2, 6, 5));
                        list.add(new JettisonSlot(chunk, 2, 6, 6));
                    }
                    case SMALL -> {
                        // the 8 chunks on the same level &
                        list.add(new JettisonSlot(chunk, 1, 4, 5));
                        list.add(new JettisonSlot(chunk, 1, 4, 6));
                        list.add(new JettisonSlot(chunk, 1, 5, 4));
                        list.add(new JettisonSlot(chunk, 1, 5, 5));
                        list.add(new JettisonSlot(chunk, 1, 5, 6));
                        list.add(new JettisonSlot(chunk, 1, 6, 4));
                        list.add(new JettisonSlot(chunk, 1, 6, 5));
                        list.add(new JettisonSlot(chunk, 1, 6, 6));
                        // the 9 chunks on the level above
                        list.add(new JettisonSlot(chunk, 2, 4, 4));
                        list.add(new JettisonSlot(chunk, 2, 4, 5));
                        list.add(new JettisonSlot(chunk, 2, 4, 6));
                        list.add(new JettisonSlot(chunk, 2, 5, 4));
                        list.add(new JettisonSlot(chunk, 2, 5, 5));
                        list.add(new JettisonSlot(chunk, 2, 5, 6));
                        list.add(new JettisonSlot(chunk, 2, 6, 4));
                        list.add(new JettisonSlot(chunk, 2, 6, 5));
                        list.add(new JettisonSlot(chunk, 2, 6, 6));
                    }
                    default -> {
                    } // same size do nothing
                }
            }
            case TALL -> {
                switch (next) {
                    case MEDIUM, WIDE -> {
                        // the 4 chunks on the level above
                        list.add(new JettisonSlot(chunk, 2, 4, 4));
                        list.add(new JettisonSlot(chunk, 2, 4, 5));
                        list.add(new JettisonSlot(chunk, 2, 5, 4));
                        list.add(new JettisonSlot(chunk, 2, 5, 5));
                    }
                    case SMALL -> {
                        // the 3 chunks on the same level &
                        list.add(new JettisonSlot(chunk, 1, 4, 5));
                        list.add(new JettisonSlot(chunk, 1, 5, 4));
                        list.add(new JettisonSlot(chunk, 1, 5, 5));
                        // the 4 chunks on the level above
                        list.add(new JettisonSlot(chunk, 2, 4, 4));
                        list.add(new JettisonSlot(chunk, 2, 4, 5));
                        list.add(new JettisonSlot(chunk, 2, 5, 4));
                        list.add(new JettisonSlot(chunk, 2, 5, 5));
                    }
                    default -> {
                    } // same size or bigger do nothing
                }
            }
            case WIDE -> {
                switch (next) {
                    case TALL, MEDIUM -> {
                        // the 5 chunks on the same level
                        list.add(new JettisonSlot(chunk, 1, 4, 6));
                        list.add(new JettisonSlot(chunk, 1, 5, 6));
                        list.add(new JettisonSlot(chunk, 1, 6, 4));
                        list.add(new JettisonSlot(chunk, 1, 6, 5));
                        list.add(new JettisonSlot(chunk, 1, 6, 6));
                    }
                    case SMALL -> {
                        // the 8 chunks on the same level
                        list.add(new JettisonSlot(chunk, 1, 4, 5));
                        list.add(new JettisonSlot(chunk, 1, 4, 6));
                        list.add(new JettisonSlot(chunk, 1, 5, 4));
                        list.add(new JettisonSlot(chunk, 1, 5, 5));
                        list.add(new JettisonSlot(chunk, 1, 5, 6));
                        list.add(new JettisonSlot(chunk, 1, 6, 4));
                        list.add(new JettisonSlot(chunk, 1, 6, 5));
                        list.add(new JettisonSlot(chunk, 1, 6, 6));
                    }
                    default -> {
                    } // same size or bigger do nothing
                }
            }
            case MEDIUM -> {
                // the 3 chunks on the same level
                if (next == ConsoleSize.SMALL) {
                    list.add(new JettisonSlot(chunk, 1, 4, 5));
                    list.add(new JettisonSlot(chunk, 1, 5, 4));
                    list.add(new JettisonSlot(chunk, 1, 5, 5));
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
