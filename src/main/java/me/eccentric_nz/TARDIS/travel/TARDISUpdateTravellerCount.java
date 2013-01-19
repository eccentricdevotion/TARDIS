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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.travel;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;

/**
 * Companions were the Doctor's closest friends. Such people knew the Doctor's
 * "secret": that he was someone non-human who travelled in space and time in a
 * police box-shaped craft called the TARDIS.
 *
 * @author eccentric_nz
 */
public class TARDISUpdateTravellerCount {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISUpdateTravellerCount(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Populate the trackTravellers HashMap with tarveller counts from the
     * database. Called onEnable(), so that we don't get NPEs later when the
     * travellers try to exit, and they haven't clicked any buttons!
     */
    public void getTravellers() {
        ResultSetTardis rs = new ResultSetTardis(plugin, null, "", true);
        if (rs.resultSet()) {
            ArrayList<HashMap<String, String>> data = rs.getData();
            for (HashMap<String, String> map : data) {
                int id = plugin.utils.parseNum(map.get("tardis_id"));
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
                if (rst.resultSet()) {
                    int count = rs.getData().size();
                    plugin.trackTravellers.put(id, count);
                }
            }
        }
    }
}