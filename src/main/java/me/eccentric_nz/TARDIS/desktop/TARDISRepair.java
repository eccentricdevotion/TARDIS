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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
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
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            String perm = tardis.getSchematic().getPermission();
            boolean master = tud.getPrevious().getPermission().equals("master");
            if (master) {
                new TARDISDelavafier(plugin, uuid).swap();
            }
            String wall = "WOOL:1";
            String floor = "WOOL:8";
            if (perm.equals("archive")) {
                new ArchiveUpdate(plugin, uuid.toString(), getArchiveName()).setInUse();
                tud.setSchematic(CONSOLES.SCHEMATICFor("archive"));
            } else {
                tud.setSchematic(tud.getPrevious());
                // get player prefs
                HashMap<String, Object> wherep = new HashMap<String, Object>();
                wherep.put("uuid", uuid.toString());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                if (rsp.resultSet()) {
                    TARDISWalls.Pair w = plugin.getTardisWalls().blocks.get(rsp.getWall());
                    TARDISWalls.Pair f = plugin.getTardisWalls().blocks.get(rsp.getFloor());
                    wall = w.getType().toString() + ":" + w.getData();
                    floor = f.getType().toString() + ":" + f.getData();
                }
            }
            tud.setWall(wall);
            tud.setFloor(floor);
            plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
            TARDISThemeRunnable ttr = new TARDISThemeRepairRunnable(plugin, uuid, tud, clean);
            // start the rebuild
            long initial_delay = (master) ? 45L : 5L;
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, initial_delay, delay);
            ttr.setTaskID(task);
        }
    }

    public boolean hasCondensedMissingBlocks() {
        String uuid = player.getUniqueId().toString();
        HashMap<String, Integer> blockIDs = new HashMap<String, Integer>();
        JSONObject obj = getConsole();
        if (obj.has("dimensions")) {
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            int h = dimensions.getInt("height");
            int w = dimensions.getInt("width");
            int l = dimensions.getInt("length");
            // get input array
            JSONArray arr = (JSONArray) obj.get("input");
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int slot = tardis.getTIPS();
                int id = tardis.getTardis_id();
                int startx, startz;
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
                int starty = (tardis.getSchematic().getPermission().equals("redstone")) ? 65 : 64;
                String[] split = tardis.getChunk().split(":");
                World world = plugin.getServer().getWorld(split[0]);
                String wall = "ORANGE_WOOL";
                String floor = "LIGHT_GREY_WOOL";
                Material wall_type = Material.WOOL;
                Material floor_type = Material.WOOL;
                HashMap<String, Object> wherepp = new HashMap<String, Object>();
                boolean hasPrefs = false;
                wherepp.put("uuid", uuid);
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                if (rsp.resultSet()) {
                    hasPrefs = true;
                    wall = rsp.getWall();
                    floor = rsp.getFloor();
                    TARDISWalls.Pair wp = plugin.getTardisWalls().blocks.get(rsp.getWall());
                    wall_type = wp.getType();
                    TARDISWalls.Pair fp = plugin.getTardisWalls().blocks.get(rsp.getFloor());
                    floor_type = fp.getType();
                }
                // loop like crazy
                for (int level = 0; level < h; level++) {
                    JSONArray plane = (JSONArray) arr.get(level);
                    for (int row = 0; row < w; row++) {
                        JSONArray r = (JSONArray) plane.get(row);
                        for (int col = 0; col < l; col++) {
                            int x = startx + row;
                            int y = starty + level;
                            int z = startz + col;
                            JSONObject c = (JSONObject) r.get(col);
                            String bid = c.getString("type");
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
                            if (bid.equals("WOOL")) {
                                switch (c.getByte("data")) {
                                    case 1:
                                        if (!world.getBlockAt(x, y, z).getType().equals(wall_type)) {
                                            String bstr = bid + ":" + c.getByte("data");
                                            if (blockIDs.containsKey(bstr)) {
                                                Integer count = blockIDs.get(bstr) + 1;
                                                blockIDs.put(bstr, count);
                                            } else {
                                                blockIDs.put(bstr, 1);
                                            }
                                        }
                                        break;
                                    case 8:
                                        if (!world.getBlockAt(x, y, z).getType().equals(floor_type)) {
                                            String bstr = bid + ":" + c.getByte("data");
                                            if (blockIDs.containsKey(bstr)) {
                                                Integer count = blockIDs.get(bstr) + 1;
                                                blockIDs.put(bstr, count);
                                            } else {
                                                blockIDs.put(bstr, 1);
                                            }
                                        }
                                        break;
                                    default:
                                        if (!world.getBlockAt(x, y, z).getType().toString().equals(bid)) {
                                            if (blockIDs.containsKey(bid)) {
                                                Integer count = blockIDs.get(bid) + 1;
                                                blockIDs.put(bid, count);
                                            } else {
                                                blockIDs.put(bid, 1);
                                            }
                                        }
                                        break;
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
                HashMap<String, Integer> blockTypeCount = new HashMap<String, Integer>();
                boolean hasRequired = true;
                HashMap<String, Integer> item_counts = new HashMap<String, Integer>();
                for (Map.Entry<String, Integer> entry : blockIDs.entrySet()) {
                    String[] block_data = entry.getKey().split(":");
                    String bid = block_data[0];
                    String mat;
                    String bkey;
                    String block_id;
                    if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                        mat = (block_data[1].equals("1")) ? wall : floor;
                        TARDISWalls.Pair iddata = plugin.getTardisWalls().blocks.get(mat);
                        bkey = iddata.getType().toString();
                        block_id = iddata.getType().toString();
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
                    HashMap<String, Object> wherec = new HashMap<String, Object>();
                    wherec.put("tardis_id", id);
                    wherec.put("block_data", map.getKey());
                    ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
                    if (rsc.resultSet()) {
                        if (rsc.getBlock_count() < map.getValue()) {
                            hasRequired = false;
                            int diff = map.getValue() - rsc.getBlock_count();
                            TARDISMessage.send(player, "CONDENSE_MORE", String.format("%d", diff), Material.getMaterial(map.getKey()).toString());
                        }
                    } else {
                        hasRequired = false;
                        TARDISMessage.send(player, "CONDENSE_MIN", String.format("%d", map.getValue()), Material.getMaterial(map.getKey()).toString());
                    }
                }
                if (hasRequired == false) {
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
            TARDISMessage.send(player, "REPAIR_FAIL", "No JSON data");
        }
        return false;
    }

    private JSONObject getConsole() {
        JSONObject obj = new JSONObject();
        String uuid = player.getUniqueId().toString();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            String perm = tardis.getSchematic().getPermission();
            if (perm.equals("archive")) {
                // try 1
                HashMap<String, Object> wherea = new HashMap<String, Object>();
                wherea.put("uuid", uuid);
                wherea.put("use", 1);
                ResultSetArchive rsa = new ResultSetArchive(plugin, wherea);
                if (rsa.resultSet()) {
                    obj = rsa.getArchive().getJSON();
                } else {
                    // try 2
                    HashMap<String, Object> wherea2 = new HashMap<String, Object>();
                    wherea2.put("uuid", uuid);
                    wherea2.put("use", 2);
                    ResultSetArchive rsa2 = new ResultSetArchive(plugin, wherea2);
                    if (rsa2.resultSet()) {
                        obj = rsa2.getArchive().getJSON();
                        // set as active
                        new ArchiveUpdate(plugin, uuid, rsa2.getArchive().getName()).setInUse();
                    } else {
                        // try 3
                        HashMap<String, Object> wherea3 = new HashMap<String, Object>();
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
                String directory = (tardis.getSchematic().isCustom()) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + perm + ".tschm";
                // get JSON
                obj = TARDISSchematicGZip.unzip(path);
            }
        }
        return obj;
    }

    private String getArchiveName() {
        String uuid = player.getUniqueId().toString();
        // try 1
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("uuid", uuid);
        wherea.put("use", 1);
        ResultSetArchive rsa = new ResultSetArchive(plugin, wherea);
        if (rsa.resultSet()) {
            return rsa.getArchive().getName();
        } else {
            // try 2
            HashMap<String, Object> wherea2 = new HashMap<String, Object>();
            wherea2.put("uuid", uuid);
            wherea2.put("use", 2);
            ResultSetArchive rsa2 = new ResultSetArchive(plugin, wherea2);
            if (rsa2.resultSet()) {
                return rsa2.getArchive().getName();
            } else {
                // try 3
                HashMap<String, Object> wherea3 = new HashMap<String, Object>();
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
