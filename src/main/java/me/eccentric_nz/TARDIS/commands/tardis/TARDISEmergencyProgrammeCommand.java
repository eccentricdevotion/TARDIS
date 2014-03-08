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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISEPSRunnable;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISEmergencyProgrammeCommand {

    private final TARDIS plugin;

    public TARDISEmergencyProgrammeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean showEP1(Player p) {
        if (plugin.getPM().isPluginEnabled("Citizens") && plugin.getConfig().getBoolean("allow.emergency_npc")) {
            if (!plugin.getUtils().inTARDISWorld(p)) {
                TARDISMessage.send(p, plugin.getPluginName() + "You must be in a TARDIS world to run this command!");
                return true;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", p.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(p, plugin.getPluginName() + MESSAGE.NOT_A_TIMELORD.getText());
                return true;
            }
            int id = rs.getTardis_id();
            String eps = rs.getEps();
            String creeper = rs.getCreeper();
            HashMap<String, Object> wherem = new HashMap<String, Object>();
            wherem.put("player", p.getName());
            ResultSetTravellers rsm = new ResultSetTravellers(plugin, wherem, true);
            if (!rsm.resultSet()) {
                TARDISMessage.send(p, plugin.getPluginName() + MESSAGE.NOT_IN_TARDIS.getText());
                return true;
            }
            if (rsm.getTardis_id() != id) {
                TARDISMessage.send(p, plugin.getPluginName() + MESSAGE.NOT_IN_TARDIS.getText());
                return true;
            }
            // get player prefs
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", p.getName());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
            if (rsp.resultSet()) {
                // schedule the NPC to appear
                String message = "This is Emergency Programme One. Now listen, this is important. If this message is activated, then it can only mean one thing: we must be in danger, and I mean fatal. You're about to die any second with no chance of escape.";
                HashMap<String, Object> wherev = new HashMap<String, Object>();
                wherev.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                List<String> players;
                if (rst.resultSet()) {
                    players = rst.getData();
                } else {
                    players = new ArrayList<String>();
                    players.add(p.getName());
                }
                TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, message, p, players, id, eps, creeper);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 20L);
                return true;
            }
        } else {
            TARDISMessage.send(p, plugin.getPluginName() + "Emergency Programme One is not available on this server.");
            return true;
        }
        return false;
    }
}
