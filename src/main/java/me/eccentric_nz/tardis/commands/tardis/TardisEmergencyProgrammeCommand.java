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
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisEpsRunnable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisEmergencyProgrammeCommand {

    private final TardisPlugin plugin;

    TardisEmergencyProgrammeCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean showEp1(Player p) {
        if (plugin.getConfig().getBoolean("allow.emergency_npc")) {
            if (!plugin.getUtils().inTardisWorld(p)) {
                TardisMessage.send(p, "CMD_IN_WORLD");
                return true;
            }
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", p.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TardisMessage.send(p, "NOT_A_TIMELORD");
                return true;
            }
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            String eps = tardis.getEps();
            String creeper = tardis.getCreeper();
            HashMap<String, Object> wherem = new HashMap<>();
            wherem.put("uuid", p.getUniqueId().toString());
            ResultSetTravellers rsm = new ResultSetTravellers(plugin, wherem, true);
            if (!rsm.resultSet()) {
                TardisMessage.send(p, "NOT_IN_TARDIS");
                return true;
            }
            if (rsm.getTardisId() != id) {
                TardisMessage.send(p, "NOT_IN_TARDIS");
                return true;
            }
            // get player prefs
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
            String message = "This is Emergency Programme One. Now listen, this is important. If this message is activated, then it can only mean one thing: we must be in danger, and I mean fatal. You're about to die any second with no chance of escape.";
            if (rsp.resultSet() && !rsp.getEpsMessage().isEmpty()) {
                message = rsp.getEpsMessage();
            }
            // schedule the NPC to appear
            HashMap<String, Object> wherev = new HashMap<>();
            wherev.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
            List<UUID> playerUuids;
            if (rst.resultSet()) {
                playerUuids = rst.getData();
            } else {
                playerUuids = new ArrayList<>();
                playerUuids.add(p.getUniqueId());
            }
            TardisEpsRunnable tardisEpsRunnable = new TardisEpsRunnable(plugin, message, p, playerUuids, id, eps, creeper);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, tardisEpsRunnable, 20L);
        } else {
            TardisMessage.send(p, "EP1_DISABLED");
        }
        return true;
    }
}
