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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author eccentric_nz
 */
public class FlightEnd {

    private final TARDIS plugin;

    public FlightEnd(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, Player player, boolean malfunction, boolean rebuild) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
        if (rst.resultSet()) {
            List<UUID> travellers = rst.getData();
            travellers.forEach((s) -> {
                Player p = plugin.getServer().getPlayer(s);
                if (p != null && !rebuild) {
                    String message = malfunction ? "MALFUNCTION" : "HANDBRAKE_LEFT_CLICK";
                    plugin.getMessenger().sendStatus(p, message);
                    // TARDIS has travelled so add players to list, so they can receive Artron on exit
                    plugin.getTrackerKeeper().getHasTravelled().add(s);
                }
            });
        } else if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(player.getUniqueId())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_HANDBRAKE_LEFT_CLICK");
        }
        // restore beacon up block if present
        HashMap<String, Object> whereb = new HashMap<>();
        whereb.put("tardis_id", id);
        whereb.put("police_box", 2);
        ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, false);
        rs.resultSetAsync((hasResult, resultSetBlocks) -> {
            if (hasResult) {
                ReplacedBlock rb = resultSetBlocks.getReplacedBlock();
                TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData());
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("tardis_id", id);
                whered.put("police_box", 2);
                plugin.getQueryFactory().doDelete("blocks", whered);
            }
        });
        // tardis has moved so remove HADS damage count
        plugin.getTrackerKeeper().getHadsDamage().remove(id);
    }
}
