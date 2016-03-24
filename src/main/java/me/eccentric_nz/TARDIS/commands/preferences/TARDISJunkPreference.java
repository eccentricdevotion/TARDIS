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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetJunk;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkPreference {

    private final TARDIS plugin;

    public TARDISJunkPreference(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(Player player, String arg, QueryFactory qf) {
        String uuid = player.getUniqueId().toString();
        // get TARDIS
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int id = rs.getTardis_id();
            // get current preset
            String current = rs.getPreset().toString();
            // must be outside of the TARDIS
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("uuid", uuid);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (rst.resultSet()) {
                TARDISMessage.send(player, "JUNK_PRESET_OUTSIDE");
                return true;
            }
            // check if they have junk record
            HashMap<String, Object> wherej = new HashMap<String, Object>();
            wherej.put("uuid", uuid);
            ResultSetJunk rsj = new ResultSetJunk(plugin, wherej);
            boolean has = rsj.resultSet();
            HashMap<String, Object> sett = new HashMap<String, Object>();
            if (arg.equalsIgnoreCase("on")) {
                HashMap<String, Object> set = new HashMap<String, Object>();
                if (has) {
                    // update record
                    HashMap<String, Object> whereu = new HashMap<String, Object>();
                    whereu.put("uuid", uuid);
                    set.put("preset", current);
                    qf.doSyncUpdate("junk", set, whereu);
                } else {
                    // insert record
                    set.put("uuid", uuid);
                    set.put("tardis_id", id);
                    set.put("preset", current);
                    qf.doSyncInsert("junk", set);
                }
                // save JUNK_MODE preset
                sett.put("chameleon_preset", "JUNK_MODE");
                sett.put("chameleon_demat", current);
                TARDISMessage.send(player, "JUNK_PRESET_ON");
            }
            if (arg.equalsIgnoreCase("off")) {
                // restore saved preset
                String preset = (has) ? rsj.getPreset().toString() : current;
                sett.put("chameleon_preset", preset);
                sett.put("chameleon_demat", "JUNK_MODE");
                TARDISMessage.send(player, "JUNK_PRESET_OFF");
            }
            // update tardis table
            HashMap<String, Object> whereu = new HashMap<String, Object>();
            whereu.put("uuid", uuid);
            qf.doSyncUpdate("tardis", sett, whereu);
            // rebuild
            player.performCommand("tardis rebuild");
            return true;
        }
        return true;
    }
}
