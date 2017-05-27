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
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomBuilder;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISZeroRoomBuilder {

    private final TARDIS plugin;

    public TARDISZeroRoomBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean build(Player p, int tips, int id) {
        if (!plugin.getConfig().getBoolean("allow.zero_room")) {
            TARDISMessage.send(p, "ZERO_DISABLED");
            return true;
        }
        TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
        int slot = tips;
        if (tips == -1) {
            slot = tintpos.getFreeSlot();
            // uodate TARDIS table with new slot number
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<>();
            set.put("tips", slot);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            qf.doUpdate("tardis", set, where);
        }
        TARDISTIPSData pos = tintpos.getTIPSData(slot);
        int x = pos.getCentreX();
        int y = 64;
        int z = pos.getCentreZ();
        World w = plugin.getServer().getWorld("TARDIS_Zero_room");
        if (w == null) {
            TARDISMessage.send(p, "ZERO_NOT_FOUND");
            return true;
        }
        Location l = new Location(w, x, y, z);
        TARDISRoomBuilder builder = new TARDISRoomBuilder(plugin, "ZERO", l, COMPASS.SOUTH, p);
        if (builder.build()) {
            UUID uuid = p.getUniqueId();
            // ok, room growing was successful, so take their energy!
            int amount = plugin.getRoomsConfig().getInt("rooms.ZERO.cost");
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", p.getUniqueId().toString());
            qf.alterEnergyLevel("tardis", -amount, set, p);
            // remove blocks from condenser table if rooms_require_blocks is true
            if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                TARDISCondenserData c_data = plugin.getGeneralKeeper().getRoomCondenserData().get(uuid);
                c_data.getBlockIDCount().entrySet().forEach((entry) -> {
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", c_data.getTardis_id());
                    wherec.put("block_data", entry.getKey());
                    qf.alterCondenserBlockCount(entry.getValue(), wherec);
                });
                plugin.getGeneralKeeper().getRoomCondenserData().remove(uuid);
            }
            // are we doing an achievement?
            if (plugin.getAchievementConfig().getBoolean("rooms.enabled")) {
                TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, p, "rooms", plugin.getBuildKeeper().getSeeds().size());
                taf.doAchievement("ZERO");
            }
        }
        return true;
    }
}
