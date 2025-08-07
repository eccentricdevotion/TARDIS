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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISEPSRunnable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISEmergencyProgrammeCommand {

    private final TARDIS plugin;

    TARDISEmergencyProgrammeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean showEP1(Player p) {
        if (!plugin.getConfig().getBoolean("allow.emergency_npc")) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "EP1_DISABLED");
            return true;
        }
        if (!plugin.getUtils().inTARDISWorld(p)) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "CMD_IN_WORLD");
            return true;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "NOT_A_TIMELORD");
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
            plugin.getMessenger().send(p, TardisModule.TARDIS, "NOT_IN_TARDIS");
            return true;
        }
        if (rsm.getTardis_id() != id) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "NOT_IN_TARDIS");
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
        List<UUID> playerUUIDs;
        if (rst.resultSet()) {
            playerUUIDs = rst.getData();
        } else {
            playerUUIDs = new ArrayList<>();
            playerUUIDs.add(p.getUniqueId());
        }
        TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, message, p, playerUUIDs, id, eps, creeper);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 20L);
        return true;
    }
}
