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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.builders.TARDISZeroRoomBuilder;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISSeedData;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls.Pair;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        String room = args[1].toUpperCase(Locale.ENGLISH);
        if (room.equals("HELP") || !plugin.getGeneralKeeper().getRoomArgs().contains(room)) {
            new TARDISRoomLister(plugin, player).list();
            return true;
        }
        String perm = "tardis.room." + args[1].toLowerCase(Locale.ENGLISH);
        if (!player.hasPermission(perm) && !player.hasPermission("tardis.room")) {
            TARDISMessage.send(player, "NO_PERM_ROOM_TYPE");
            return true;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        Tardis tardis = rs.getTardis();
        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
            TARDISMessage.send(player, "POWER_DOWN");
            return true;
        }
        if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
            TARDISMessage.send(player, "ROOM_OWN_WORLD");
            return true;
        }
        if (!tardis.getRenderer().isEmpty() && room.equals("RENDERER")) {
            TARDISMessage.send(player, "RENDER_EXISTS");
            return true;
        }
        int id = tardis.getTardis_id();
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasARS()) {
            TARDISMessage.send(player, "ARS_MISSING");
            return true;
        }
        int level = tardis.getArtron_level();
        String chunk = tardis.getChunk();
        SCHEMATIC schm = tardis.getSchematic();
        int tips = tardis.getTIPS();
        // check they are in the tardis
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        wheret.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TARDISMessage.send(player, "NOT_IN_TARDIS");
            return true;
        }
        // check they have enough artron energy
        if (level < plugin.getRoomsConfig().getInt("rooms." + room + ".cost")) {
            TARDISMessage.send(player, "ENERGY_NO_ROOM");
            return true;
        }
        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
            boolean hasRequired = true;
            HashMap<String, Integer> roomBlocks = plugin.getBuildKeeper().getRoomBlockCounts().get(room);
            String wall = "ORANGE_WOOL";
            String floor = "LIGHT_GREY_WOOL";
            HashMap<String, Object> wherepp = new HashMap<>();
            boolean hasPrefs = false;
            wherepp.put("uuid", player.getUniqueId().toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                hasPrefs = true;
                wall = rsp.getWall();
                floor = rsp.getFloor();
            }
            HashMap<String, Integer> item_counts = new HashMap<>();
            for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
                String[] block_data = entry.getKey().split(":");
                String bid = block_data[0];
                String mat;
                String bkey;
                String block_id;
                if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                    mat = (block_data[1].equals("1")) ? wall : floor;
                    Pair iddata = plugin.getTardisWalls().blocks.get(mat);
                    bkey = iddata.getType().toString();
                    block_id = iddata.getType().toString();
                } else {
                    bkey = bid;
                    block_id = bid;
                }
                int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                int required = (tmp > 0) ? tmp : 1;
                if (item_counts.containsKey(block_id)) {
                    item_counts.put(block_id, item_counts.get(block_id) + required);
                } else {
                    item_counts.put(block_id, required);
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
                        TARDISMessage.send(player, "CONDENSE_MORE", String.format("%d", diff), Material.getMaterial(map.getKey()).toString());
                    }
                } else {
                    hasRequired = false;
                    TARDISMessage.send(player, "CONDENSE_MIN", String.format("%d", map.getValue()), Material.getMaterial(map.getKey()).toString());
                }
            }
            if (hasRequired == false) {
                player.sendMessage("-----------------------------");
                return true;
            }
            TARDISCondenserData c_data = new TARDISCondenserData();
            c_data.setBlockIDCount(item_counts);
            c_data.setTardis_id(id);
            plugin.getGeneralKeeper().getRoomCondenserData().put(player.getUniqueId(), c_data);
        }
        if (room.equals("ZERO")) {
            return new TARDISZeroRoomBuilder(plugin).build(player, tips, id);
        }
        TARDISSeedData sd = new TARDISSeedData();
        sd.setId(id);
        sd.setRoom(room);
        sd.setSchematic(schm);
        sd.setChunkMinMax(chunk);
        // check whether they have an ARS sign
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("tardis_id", id);
        wherea.put("type", 10);
        ResultSetControls rsc = new ResultSetControls(plugin, wherea, false);
        sd.setARS(rsc.resultSet());
        plugin.getTrackerKeeper().getRoomSeed().put(player.getUniqueId(), sd);
        TARDISMessage.send(player, "ROOM_SEED_INFO", room, plugin.getRoomsConfig().getString("rooms." + room + ".seed"));
        return true;
    }
}
