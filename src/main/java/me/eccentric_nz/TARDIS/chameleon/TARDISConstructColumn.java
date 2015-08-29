/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetChameleon;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConstructColumn {

    private final TARDIS plugin;
    private final int id;
    private final String field;
    private final COMPASS d;

    public TARDISConstructColumn(TARDIS plugin, int id, String field, COMPASS d) {
        this.plugin = plugin;
        this.id = id;
        this.field = field;
        this.d = d;
    }

    public TARDISChameleonColumn getColumn() {
        // get the json data
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetChameleon rs = new ResultSetChameleon(plugin, where);
        if (rs.resultSet()) {
            String jsonid = rs.getData().get(field + "ID");
            String jsondata = rs.getData().get(field + "Data");
            TARDISChameleonColumn col = TARDISChameleonPreset.buildTARDISChameleonColumn(d, jsonid, jsondata, false, false, false);
            return col;
        }
        return null;
    }
}
