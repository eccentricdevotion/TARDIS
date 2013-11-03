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
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
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
            player.sendMessage(plugin.pluginName + "Too few command arguments!");
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
            player.sendMessage(plugin.pluginName + "You are not a Timelord. You need to create a TARDIS first before using this command!");
            return true;
        }
        if (!plugin.utils.canGrowRooms(rs.getChunk())) {
            player.sendMessage(plugin.pluginName + "You cannot grow rooms unless your TARDIS was created in its own world!");
            return true;
        }
        int id = rs.getTardis_id();
        int level = rs.getArtron_level();
        String chunk = rs.getChunk();
        TARDISConstants.SCHEMATIC schm = rs.getSchematic();
        // check they are in the tardis
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("player", player.getName());
        wheret.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            player.sendMessage(plugin.pluginName + "You are not inside your TARDIS. You need to be to run this command!");
            return true;
        }
        // check they have enough artron energy
        if (level < plugin.getRoomsConfig().getInt("rooms." + room + ".cost")) {
            player.sendMessage(plugin.pluginName + "The TARDIS does not have enough Artron Energy to grow this room!");
            return true;
        }
        if (plugin.getConfig().getBoolean("rooms_require_blocks")) {
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
            for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
                String[] block_data = entry.getKey().split(":");
                int bid = plugin.utils.parseNum(block_data[0]);
                String mat;
                String bdata;
                if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                    mat = (block_data[1].equals("1")) ? wall : floor;
                    int[] iddata = plugin.tw.blocks.get(mat);
                    bdata = String.format("%d", iddata[0]);
                } else {
                    mat = Material.getMaterial(bid).toString();
                    bdata = String.format("%d", bid);
                }
                int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("rooms_condenser_percent"));
                int required = (tmp > 0) ? tmp : 1;
                blockIDCount.put(bdata, required);
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("block_data", bdata);
                ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
                if (rsc.resultSet()) {
                    if (rsc.getBlock_count() < required) {
                        hasRequired = false;
                        int diff = required - rsc.getBlock_count();
                        player.sendMessage(plugin.pluginName + "You need to condense " + diff + " more " + mat + "!");
                    }
                } else {
                    hasRequired = false;
                    player.sendMessage(plugin.pluginName + "You need to condense a minimum of " + required + " " + mat);
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
        TARDISSeedData sd = new TARDISSeedData(plugin);
        sd.setId(id);
        sd.setRoom(room);
        sd.setSchematic(schm);
        sd.setChunkMinMax(chunk);
        String message = "Place the " + room + " seed block (" + plugin.getRoomsConfig().getString("rooms." + room + ".seed") + ") in front of the pressure plate, then hit it with the TARDIS key to start growing your room!";
        plugin.trackRoomSeed.put(player.getName(), sd);
        player.sendMessage(plugin.pluginName + message);
        return true;
    }
}
