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
package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSystemUpgrades;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;

import java.util.HashMap;

public class SystemUpgradeRecord {

    private final TARDIS plugin;

    public SystemUpgradeRecord(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void make(String uuid) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(uuid)) {
            return;
        }
        // get player's artron energy level
        ResultSetSystemUpgrades rsp = new ResultSetSystemUpgrades(plugin, rs.getTardisId(), uuid);
        if (!rsp.resultset()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", rs.getTardisId());
            set.put("uuid", uuid);
            plugin.getQueryFactory().doInsert("system_upgrades", set);
        }
    }
}
