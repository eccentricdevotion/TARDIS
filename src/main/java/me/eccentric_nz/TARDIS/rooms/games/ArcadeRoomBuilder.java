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
package me.eccentric_nz.TARDIS.rooms.games;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TIPSData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.CondenserData;
import me.eccentric_nz.TARDIS.rooms.RoomBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class ArcadeRoomBuilder {

    private final TARDIS plugin;

    public ArcadeRoomBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void build(Player p, int tips, int id) {
        // check if they have a games record already
        ResultSetGames rsg = new ResultSetGames(plugin);
        if (rsg.fromId(id)) {
            return;
        }
        if (!plugin.getConfig().getBoolean("allow.zero_room")) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "ZERO_DISABLED");
            return;
        }
        TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
        int slot = tips;
        if (tips == -1) {
            slot = tintpos.getFreeSlot();
            // update TARDIS table with new slot number
            HashMap<String, Object> set = new HashMap<>();
            set.put("tips", slot);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", set, where);
        }
        TIPSData pos = tintpos.getTIPSData(slot);
        int x = pos.getCentreX();
        int y = 16;
        int z = pos.getCentreZ();
        World w = plugin.getServer().getWorld("TARDIS_Zero_room");
        if (w == null) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "ZERO_NOT_FOUND");
            return;
        }
        Location l = new Location(w, x, y, z);
        RoomBuilder builder = new RoomBuilder(plugin, "ARCADE", l, COMPASS.SOUTH, p);
        if (builder.build()) {
            UUID uuid = p.getUniqueId();
            // ok, room growing was successful, so take their energy!
            int amount = plugin.getRoomsConfig().getInt("rooms.ARCADE.cost");
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", p.getUniqueId().toString());
            plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, set, p);
            // remove blocks from condenser table if rooms_require_blocks is true
            if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                CondenserData c_data = plugin.getGeneralKeeper().getRoomCondenserData().get(uuid);
                c_data.getBlockIDCount().forEach((key, value) -> {
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", c_data.getTardis_id());
                    wherec.put("block_data", key);
                    plugin.getQueryFactory().alterCondenserBlockCount(value, wherec);
                });
                plugin.getGeneralKeeper().getRoomCondenserData().remove(uuid);
            }
            // are we doing an achievement?
            if (plugin.getAchievementConfig().getBoolean("rooms.enabled")) {
                TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, p, Advancement.ROOMS, plugin.getBuildKeeper().getRoomSeeds().size());
                taf.doAchievement("ARCADE");
            }
        }
    }
}
