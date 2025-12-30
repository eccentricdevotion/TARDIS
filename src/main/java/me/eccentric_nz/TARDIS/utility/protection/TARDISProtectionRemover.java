/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility.protection;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;

import java.util.HashMap;

public class TARDISProtectionRemover {

    private final TARDIS plugin;

    public TARDISProtectionRemover(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void cleanInteriorBlocks(int id) {
        // get and remove block protection
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("police_box", 0);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, true);
        if (rsb.resultSet()) {
            for (ReplacedBlock b : rsb.getData()) {
                plugin.getGeneralKeeper().getProtectBlockMap().remove(b.getStrLocation());
            }
        }
        // remove database entries
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("tardis_id", id);
        wherep.put("police_box", 0);
        plugin.getQueryFactory().doDelete("blocks", wherep);
    }
}
