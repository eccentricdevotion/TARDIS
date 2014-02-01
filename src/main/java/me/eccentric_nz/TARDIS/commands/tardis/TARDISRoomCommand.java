/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.builders.TARDISZeroRoomBuilder;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISSeedData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRoomCommand {

    private final TARDIS plugin;

    public TARDISRoomCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startRoom(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(plugin.pluginName + MESSAGE.TOO_FEW_ARGS.getText());
            return false;
        }
        String room = args[1].toUpperCase(Locale.ENGLISH);
        StringBuilder buf = new StringBuilder();
        for (String rl : plugin.tardisCommand.roomArgs) {
            buf.append(rl).append(", ");
        }
        String roomlist = buf.toString().substring(0, buf.length() - 2);
        if (room.equals("HELP")) {
            player.sendMessage(plugin.pluginName + "There are currently " + plugin.tardisCommand.roomArgs.size() + " room types! They are: " + roomlist + ".");
            player.sendMessage("View a TARDIS room gallery at http://eccentricdevotion.github.com/TARDIS/room-gallery.html");
            return true;
        }
        if (!plugin.tardisCommand.roomArgs.contains(room)) {
            player.sendMessage(plugin.pluginName + "That is not a valid room type! Try one of: " + roomlist + ".");
            return true;
        }
        String perm = "tardis.room." + args[1].toLowerCase(Locale.ENGLISH);
        if (!player.hasPermission(perm) && !player.hasPermission("tardis.room")) {
            String grammar = (TARDISConstants.vowels.contains(room.substring(0, 1))) ? "an" : "a";
            player.sendMessage(plugin.pluginName + "You do not have permission to grow " + grammar + " " + room);
            return true;
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", player.getName());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            player.sendMessage(plugin.pluginName + MESSAGE.NOT_A_TIMELORD.getText());
            return true;
        }
        if (!plugin.utils.canGrowRooms(rs.getChunk())) {
            player.sendMessage(plugin.pluginName + "You cannot grow rooms unless your TARDIS was created in its own world!");
            return true;
        }
        if (!rs.getRenderer().isEmpty() && room.equals("RENDERER")) {
            player.sendMessage(plugin.pluginName + "You already have an exterior rendering room! Please jettison the existing room first.");
            return true;
        }
        int id = rs.getTardis_id();
        TARDISCircuitChecker tcc = null;
        if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasARS()) {
            player.sendMessage(plugin.pluginName + "The ARS Circuit is missing from the console!");
            return true;
        }
        int level = rs.getArtron_level();
        String chunk = rs.getChunk();
        SCHEMATIC schm = rs.getSchematic();
        int tips = rs.getTIPS();
        // check they are in the tardis
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("player", player.getName());
        wheret.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            player.sendMessage(plugin.pluginName + MESSAGE.NOT_IN_TARDIS.getText());
            return true;
        }
        // check they have enough artron energy
        if (level < plugin.getRoomsConfig().getInt("rooms." + room + ".cost")) {
            player.sendMessage(plugin.pluginName + "The TARDIS does not have enough Artron Energy to grow this room!");
            return true;
        }
        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
            HashMap<String, Integer> blockIDCount = new HashMap<String, Integer>();
            boolean hasRequired = true;
            HashMap<String, Integer> roomBlocks = plugin.roomBlockCounts.get(room);
            String wall = "ORANGE_WOOL";
            String floor = "LIGHT_GREY_WOOL";
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            boolean hasPrefs = false;
            wherepp.put("player", player.getName());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                hasPrefs = true;
                wall = rsp.getWall();
                floor = rsp.getFloor();
            }
            HashMap<Integer, Integer> item_counts = new HashMap<Integer, Integer>();
            for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
                String[] block_data = entry.getKey().split(":");
                int bid = plugin.utils.parseInt(block_data[0]);
                String mat;
                String bdata;
                int block_id;
                if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                    mat = (block_data[1].equals("1")) ? wall : floor;
                    int[] iddata = plugin.tw.blocks.get(mat);
                    bdata = String.format("%d", iddata[0]);
                    block_id = iddata[0];
                } else {
                    bdata = String.format("%d", bid);
                    block_id = bid;
                }
                int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                int required = (tmp > 0) ? tmp : 1;
                if (blockIDCount.containsKey(bdata)) {
                    blockIDCount.put(bdata, blockIDCount.get(bdata) + required);
                } else {
                    blockIDCount.put(bdata, required);
                }
                if (item_counts.containsKey(block_id)) {
                    item_counts.put(block_id, item_counts.get(block_id) + required);
                } else {
                    item_counts.put(block_id, required);
                }
            }
            for (Map.Entry<Integer, Integer> map : item_counts.entrySet()) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("block_data", map.getKey());
                ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
                if (rsc.resultSet()) {
                    if (rsc.getBlock_count() < map.getValue()) {
                        hasRequired = false;
                        int diff = map.getValue() - rsc.getBlock_count();
                        player.sendMessage(plugin.pluginName + "You need to condense " + diff + " more " + Material.getMaterial(map.getKey()).toString() + "!");
                    }
                } else {
                    hasRequired = false;
                    player.sendMessage(plugin.pluginName + "You need to condense a minimum of " + map.getValue() + " " + Material.getMaterial(map.getKey()).toString());
                }
            }
            if (hasRequired == false) {
                player.sendMessage("-----------------------------");
                return true;
            }
            TARDISCondenserData c_data = new TARDISCondenserData();
            c_data.setBlockIDCount(blockIDCount);
            c_data.setTardis_id(id);
            plugin.roomCondenserData.put(player.getName(), c_data);
        }
        if (room.equals("ZERO")) {
            return new TARDISZeroRoomBuilder(plugin).build(player, tips, id);
        }
        TARDISSeedData sd = new TARDISSeedData(plugin);
        sd.setId(id);
        sd.setRoom(room);
        sd.setSchematic(schm);
        sd.setChunkMinMax(chunk);
        // check whether they have an ARS sign
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("tardis_id", id);
        wherea.put("type", 10);
        ResultSetControls rsc = new ResultSetControls(plugin, wherea, false);
        sd.setARS(rsc.resultSet());
        String message = "Place the " + room + " seed block (" + plugin.getRoomsConfig().getString("rooms." + room + ".seed") + ") in front of the pressure plate, then hit it with the TARDIS key to start growing your room!";
        plugin.trackRoomSeed.put(player.getName(), sd);
        player.sendMessage(plugin.pluginName + message);
        return true;
    }
}
