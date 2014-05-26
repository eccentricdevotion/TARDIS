/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetStandby;

/**
 *
 * @author eccentric_nz
 */
public class TARDISStandbyMode implements Runnable {

    private final TARDIS plugin;
    private final int amount;

    public TARDISStandbyMode(TARDIS plugin) {
        this.plugin = plugin;
        this.amount = this.plugin.getArtronConfig().getInt("standby");
    }

    @Override
    public void run() {
        // get TARDISes that are powered on
        List<Integer> ids = new ResultSetStandby(plugin).onStandby();
        QueryFactory qf = new QueryFactory(plugin);
        for (Integer id : ids) {
            // not while travelling
            if (!plugin.getTrackerKeeper().getInVortex().contains(id)) {
                // remove some energy
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                qf.alterEnergyLevel("tardis", -amount, where, null);
            }
        }
    }
}
