/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISRepair {

    private final TARDIS plugin;
    private final Player player;

    public TARDISRepair(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void restore(boolean clean) {
        UUID uuid = player.getUniqueId();
        TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            String perm = tardis.getSchematic().getPermission();
            boolean hasLava = tud.getPrevious().getPermission().equals("master") || tud.getPrevious().getPermission().equals("delta");
            if (hasLava) {
                new TARDISDelavafier(plugin, uuid).swap();
            }
            String wall = "ORANGE_WOOL";
            String floor = "LIGHT_GRAY_WOOL";
            if (perm.equals("archive")) {
                new ArchiveUpdate(plugin, uuid.toString(), getArchiveName()).setInUse();
                tud.setSchematic(Consoles.schematicFor("archive"));
            } else {
                tud.setSchematic(tud.getPrevious());
                // get player prefs
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                if (rsp.resultSet()) {
                    wall = rsp.getWall();
                    floor = rsp.getFloor();
                }
            }
            tud.setWall(wall);
            tud.setFloor(floor);
            plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
            TARDISThemeRunnable ttr = new TARDISThemeRepairRunnable(plugin, uuid, tud, clean);
            // start the rebuild
            long initial_delay = (hasLava) ? 45L : 5L;
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, initial_delay, delay);
            ttr.setTaskID(task);
        }
    }

    boolean hasCondensedMissingBlocks() {
        String uuid = player.getUniqueId().toString();
        HashMap<String, Integer> blockIDs = new HashMap<>();
        JsonObject obj = getConsole();
        if (obj.has("dimensions")) {
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            int h = dimensions.get("height").getAsInt();
            int w = dimensions.get("width").getAsInt();
            int l = dimensions.get("length").getAsInt();
            // get input array
            JsonArray arr = obj.get("input").getAsJsonArray();
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int slot = tardis.getTIPS();
                int id = tardis.getTardisId();
                int startx, startz;
                if (slot != -1000001) { // default world - use TIPS
                    TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                    TARDISTIPSData pos = tintpos.getTIPSData(slot);
                    startx = pos.getCentreX();
                    startz = pos.getCentreZ();
                } else {
                    int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
                    startx = gsl[0];
                    startz = gsl[2];
                }
                int starty = tardis.getSchematic().getStartY();
                World world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
                String wall = "ORANGE_WOOL";
                String floor = "LIGHT_GRAY_WOOL";
                Material wall_type = Material.ORANGE_WOOL;
                Material floor_type = Material.LIGHT_GRAY_WOOL;
                boolean hasPrefs = false;
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                if (rsp.resultSet()) {
                    hasPrefs = true;
                    wall_type = Material.getMaterial(rsp.getWall());
                    floor_type = Material.getMaterial(rsp.getFloor());
                }
                // loop like crazy
                for (int level = 0; level < h; level++) {
                    JsonArray plane = arr.get(level).getAsJsonArray();
                    for (int row = 0; row < w; row++) {
                        JsonArray r = (JsonArray) plane.get(row);
                        for (int col = 0; col < l; col++) {
                            int x = startx + row;
                            int y = starty + level;
                            int z = startz + col;
                            JsonObject c = r.get(col).getAsJsonObject();
                            BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
                            String bid = data.getMaterial().toString();
                            if (plugin.getBuildKeeper().getIgnoreBlocks().contains(bid)) {
                                continue;
                            }
                            if (plugin.getBuildKeeper().getBlockConversion().containsKey(bid)) {
                                if (!world.getBlockAt(x, y, z).getType().toString().equals(bid)) {
                                    bid = plugin.getBuildKeeper().getBlockConversion().get(bid);
                                } else {
                                    continue;
                                }
                            }
                            if (bid.equals("ORANGE_WOOL")) {
                                if (!world.getBlockAt(x, y, z).getType().equals(wall_type)) {
                                    if (blockIDs.containsKey(bid)) {
                                        Integer count = blockIDs.get(bid) + 1;
                                        blockIDs.put(bid, count);
                                    } else {
                                        blockIDs.put(bid, 1);
                                    }
                                }
                            } else if (bid.equals("LIGHT_GRAY_WOOL")) {
                                if (!world.getBlockAt(x, y, z).getType().equals(floor_type)) {
                                    if (blockIDs.containsKey(bid)) {
                                        Integer count = blockIDs.get(bid) + 1;
                                        blockIDs.put(bid, count);
                                    } else {
                                        blockIDs.put(bid, 1);
                                    }
                                }
                            } else if (!world.getBlockAt(x, y, z).getType().toString().equals(bid)) {
                                if (blockIDs.containsKey(bid)) {
                                    Integer count = blockIDs.get(bid) + 1;
                                    blockIDs.put(bid, count);
                                } else {
                                    blockIDs.put(bid, 1);
                                }
                            }
                        }
                    }
                }
                HashMap<String, Integer> blockTypeCount = new HashMap<>();
                boolean hasRequired = true;
                HashMap<String, Integer> item_counts = new HashMap<>();
                for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                    String bid = entry.getKey();
                    String bkey;
                    String block_id;
                    if (hasPrefs && (entry.getKey().equals("ORANGE_WOOL") || entry.getKey().equals("LIGHT_GRAY_WOOL"))) {
                        bkey = (entry.getKey().equals("ORANGE_WOOL")) ? wall : floor;
                        block_id = (entry.getKey().equals("ORANGE_WOOL")) ? wall : floor;
                    } else {
                        bkey = bid;
                        block_id = bid;
                    }
                    if (blockTypeCount.containsKey(bkey)) {
                        blockTypeCount.put(bkey, blockTypeCount.get(bkey) + entry.getValue());
                    } else {
                        blockTypeCount.put(bkey, entry.getValue());
                    }
                    if (item_counts.containsKey(block_id)) {
                        item_counts.put(block_id, item_counts.get(block_id) + entry.getValue());
                    } else {
                        item_counts.put(block_id, entry.getValue());
                    }
                }
                for (Map.Entry<String, Integer> map : item_counts.entrySet()) {
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    wherec.put("block_data", map.getKey());
                    ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
                    if (rsc.resultSet()) {
                        if (rsc.getBlock_count() < map.getValue()) {
                            hasRequired = false;
                            int diff = map.getValue() - rsc.getBlock_count();
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "CONDENSE_MORE", String.format("%d", diff), Material.getMaterial(map.getKey()).toString());
                        }
                    } else {
                        hasRequired = false;
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CONDENSE_MIN", String.format("%d", map.getValue()), Material.getMaterial(map.getKey()).toString());
                    }
                }
                if (!hasRequired) {
                    player.sendMessage("-----------------------------");
                    return false;
                }
                TARDISCondenserData c_data = new TARDISCondenserData();
                c_data.setBlockIDCount(blockTypeCount);
                c_data.setTardis_id(id);
                plugin.getGeneralKeeper().getRoomCondenserData().put(player.getUniqueId(), c_data);
                return true;
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "REPAIR_FAIL", "No JSON data");
        }
        return false;
    }

    private JsonObject getConsole() {
        JsonObject obj = new JsonObject();
        String uuid = player.getUniqueId().toString();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            String perm = tardis.getSchematic().getPermission();
            if (perm.equals("archive")) {
                // try 1
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("uuid", uuid);
                wherea.put("use", 1);
                ResultSetArchive rsa = new ResultSetArchive(plugin, wherea);
                if (rsa.resultSet()) {
                    obj = rsa.getArchive().getJSON();
                } else {
                    // try 2
                    HashMap<String, Object> wherea2 = new HashMap<>();
                    wherea2.put("uuid", uuid);
                    wherea2.put("use", 2);
                    ResultSetArchive rsa2 = new ResultSetArchive(plugin, wherea2);
                    if (rsa2.resultSet()) {
                        obj = rsa2.getArchive().getJSON();
                        // set as active
                        new ArchiveUpdate(plugin, uuid, rsa2.getArchive().getName()).setInUse();
                    } else {
                        // try 3
                        HashMap<String, Object> wherea3 = new HashMap<>();
                        wherea3.put("uuid", uuid);
                        wherea3.put("use", 0);
                        ResultSetArchive rsa3 = new ResultSetArchive(plugin, wherea3);
                        if (rsa3.resultSet()) {
                            obj = rsa2.getArchive().getJSON();
                            // set as active
                            new ArchiveUpdate(plugin, uuid, rsa3.getArchive().getName()).setInUse();
                        }
                    }
                }
            } else {
                // get JSON
                obj = TARDISSchematicGZip.getObject(plugin, "consoles", tardis.getSchematic().getPermission(), tardis.getSchematic().isCustom());
            }
        }
        return obj;
    }

    private String getArchiveName() {
        String uuid = player.getUniqueId().toString();
        // try 1
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("uuid", uuid);
        wherea.put("use", 1);
        ResultSetArchive rsa = new ResultSetArchive(plugin, wherea);
        if (rsa.resultSet()) {
            return rsa.getArchive().getName();
        } else {
            // try 2
            HashMap<String, Object> wherea2 = new HashMap<>();
            wherea2.put("uuid", uuid);
            wherea2.put("use", 2);
            ResultSetArchive rsa2 = new ResultSetArchive(plugin, wherea2);
            if (rsa2.resultSet()) {
                return rsa2.getArchive().getName();
            } else {
                // try 3
                HashMap<String, Object> wherea3 = new HashMap<>();
                wherea3.put("uuid", uuid);
                wherea3.put("use", 0);
                ResultSetArchive rsa3 = new ResultSetArchive(plugin, wherea3);
                if (rsa3.resultSet()) {
                    return rsa3.getArchive().getName();
                }
            }
        }
        return "";
    }
}
