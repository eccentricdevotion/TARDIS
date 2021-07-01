/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.TardisZeroRoomBuilder;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.messaging.TardisRoomLister;
import me.eccentric_nz.tardis.rooms.TardisCondenserData;
import me.eccentric_nz.tardis.rooms.TardisSeedData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author eccentric_nz
 */
class TardisRoomCommand {

    private final TardisPlugin plugin;

    TardisRoomCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean startRoom(Player player, String[] args) {
        if (args.length < 2) {
            TardisMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        String room = args[1].toUpperCase(Locale.ENGLISH);
        if (room.equals("HELP") || !plugin.getGeneralKeeper().getRoomArgs().contains(room)) {
            new TardisRoomLister(plugin, player).list();
            return true;
        }
        String perm = "tardis.room." + args[1].toLowerCase(Locale.ENGLISH);
        if (!TardisPermission.hasPermission(player, perm) && !TardisPermission.hasPermission(player, "tardis.room")) {
            TardisMessage.send(player, "NO_PERM_ROOM_TYPE");
            return true;
        }
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TardisMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        Tardis tardis = rs.getTardis();
        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered()) {
            TardisMessage.send(player, "POWER_DOWN");
            return true;
        }
        if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
            TardisMessage.send(player, "ROOM_OWN_WORLD");
            return true;
        }
        if (!tardis.getRenderer().isEmpty() && room.equals("RENDERER")) {
            TardisMessage.send(player, "RENDER_EXISTS");
            return true;
        }
        int id = tardis.getTardisId();
        TardisCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
            tcc = new TardisCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasArs()) {
            TardisMessage.send(player, "ARS_MISSING");
            return true;
        }
        int level = tardis.getArtronLevel();
        String chunk = tardis.getChunk();
        Schematic schm = tardis.getSchematic();
        int tips = tardis.getTips();
        // check they are in the TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        wheret.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TardisMessage.send(player, "NOT_IN_TARDIS");
            return true;
        }
        // check they have enough artron energy
        if (level < plugin.getRoomsConfig().getInt("rooms." + room + ".cost")) {
            TardisMessage.send(player, "ENERGY_NO_ROOM");
            return true;
        }
        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
            boolean hasRequired = true;
            HashMap<String, Integer> roomBlocks = plugin.getBuildKeeper().getRoomBlockCounts().get(room);
            String wall = "ORANGE_WOOL";
            String floor = "LIGHT_GRAY_WOOL";
            boolean hasPrefs = false;
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (rsp.resultSet()) {
                hasPrefs = true;
                wall = rsp.getWall();
                floor = rsp.getFloor();
            }
            HashMap<String, Integer> item_counts = new HashMap<>();
            for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
                String[] blockData = entry.getKey().split(":");
                String bid = blockData[0];
                String mat;
                String blockId;
                if (hasPrefs && blockData.length == 2 && (blockData[1].equals("1") || blockData[1].equals("8"))) {
                    mat = (blockData[1].equals("1")) ? wall : floor;
                    blockId = mat;
                } else {
                    blockId = bid;
                }
                int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                int required = (tmp > 0) ? tmp : 1;
                if (item_counts.containsKey(blockId)) {
                    item_counts.put(blockId, item_counts.get(blockId) + required);
                } else {
                    item_counts.put(blockId, required);
                }
            }
            for (Map.Entry<String, Integer> map : item_counts.entrySet()) {
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", id);
                wherec.put("block_data", map.getKey());
                ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
                if (rsc.resultSet()) {
                    if (rsc.getBlockCount() < map.getValue()) {
                        hasRequired = false;
                        int diff = map.getValue() - rsc.getBlockCount();
                        TardisMessage.send(player, "CONDENSE_MORE", String.format("%d", diff), Objects.requireNonNull(Material.getMaterial(map.getKey())).toString());
                    }
                } else {
                    hasRequired = false;
                    TardisMessage.send(player, "CONDENSE_MIN", String.format("%d", map.getValue()), Objects.requireNonNull(Material.getMaterial(map.getKey())).toString());
                }
            }
            if (!hasRequired) {
                player.sendMessage("-----------------------------");
                plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                return true;
            }
            TardisCondenserData c_data = new TardisCondenserData();
            c_data.setBlockIDCount(item_counts);
            c_data.setTardisId(id);
            plugin.getGeneralKeeper().getRoomCondenserData().put(uuid, c_data);
        }
        if (room.equals("ZERO")) {
            return new TardisZeroRoomBuilder(plugin).build(player, tips, id);
        }
        TardisSeedData sd = new TardisSeedData();
        sd.setId(id);
        sd.setRoom(room);
        sd.setSchematic(schm);
        sd.setChunkMinMax(chunk);
        // check whether they have an ars sign
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("tardis_id", id);
        wherea.put("type", 10);
        ResultSetControls rsc = new ResultSetControls(plugin, wherea, false);
        sd.setArs(rsc.resultSet());
        plugin.getTrackerKeeper().getRoomSeed().put(uuid, sd);
        TardisMessage.send(player, "ROOM_SEED_INFO", room, plugin.getRoomsConfig().getString("rooms." + room + ".seed"));
        return true;
    }
}
