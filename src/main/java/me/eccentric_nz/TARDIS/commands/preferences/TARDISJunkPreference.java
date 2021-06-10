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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetJunk;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISJunkPreference {

    private final TARDISPlugin plugin;

    TARDISJunkPreference(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(Player player, String arg) {
        UUID uuid = player.getUniqueId();
        String ustr = uuid.toString();
        // get tardis
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", ustr);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            TARDIS tardis = rs.getTardis();
            int id = tardis.getTardisId();
            // get current preset
            String current = tardis.getPreset().toString();
            // must be outside of the tardis
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", ustr);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (rst.resultSet()) {
                TARDISMessage.send(player, "JUNK_PRESET_OUTSIDE");
                return true;
            }
            if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
                long now = System.currentTimeMillis();
                long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
                long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
                if (now < then) {
                    TARDISMessage.send(player.getPlayer(), "COOLDOWN", String.format("%d", cooldown / 1000));
                    return true;
                }
            }
            // make sure is opposite
            if (current.equals("JUNK_MODE") && arg.equalsIgnoreCase("on")) {
                TARDISMessage.send(player, "JUNK_ALREADY_ON");
                return true;
            }
            if (!current.equals("JUNK_MODE") && arg.equalsIgnoreCase("off")) {
                TARDISMessage.send(player, "JUNK_ALREADY_OFF");
                return true;
            }
            // check if they have a junk record
            HashMap<String, Object> wherej = new HashMap<>();
            wherej.put("uuid", ustr);
            ResultSetJunk rsj = new ResultSetJunk(plugin, wherej);
            boolean has = rsj.resultSet();
            HashMap<String, Object> sett = new HashMap<>();
            String cham_set = "";
            if (arg.equalsIgnoreCase("on")) {
                HashMap<String, Object> set = new HashMap<>();
                if (has) {
                    // update record
                    HashMap<String, Object> whereu = new HashMap<>();
                    whereu.put("uuid", ustr);
                    set.put("preset", current);
                    plugin.getQueryFactory().doSyncUpdate("junk", set, whereu);
                } else {
                    // insert record
                    set.put("uuid", ustr);
                    set.put("tardis_id", id);
                    set.put("preset", current);
                    plugin.getQueryFactory().doSyncInsert("junk", set);
                }
                // save JUNK_MODE preset
                sett.put("chameleon_preset", "JUNK_MODE");
                sett.put("chameleon_demat", current);
                // set chameleon adaption to OFF
                sett.put("adapti_on", 0);
                TARDISMessage.send(player, "JUNK_PRESET_ON");
                cham_set = "JUNK_MODE";
            }
            if (arg.equalsIgnoreCase("off")) {
                // restore saved preset
                String preset = (has) ? rsj.getPreset().toString() : current;
                sett.put("chameleon_preset", preset);
                sett.put("chameleon_demat", "JUNK_MODE");
                TARDISMessage.send(player, "JUNK_PRESET_OFF");
                cham_set = preset;
            }
            // update tardis table
            HashMap<String, Object> whereu = new HashMap<>();
            whereu.put("uuid", ustr);
            plugin.getQueryFactory().doSyncUpdate("tardis", sett, whereu);
            // set the Chameleon Circuit sign
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            whereh.put("type", 31);
            ResultSetControls rsc = new ResultSetControls(plugin, whereh, true);
            if (rsc.resultSet()) {
                for (HashMap<String, String> map : rsc.getData()) {
                    TARDISStaticUtils.setSign(map.get("location"), 3, cham_set, player);
                }
            }
            // rebuild
            player.performCommand("tardis rebuild");
            return true;
        }
        return true;
    }
}
