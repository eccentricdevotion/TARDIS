/*
 * Copyright (C) 2020 eccentric_nz
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

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetChameleon;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;

import java.util.HashMap;

/**
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
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetChameleon rs = new ResultSetChameleon(plugin, where);
        if (rs.resultSet()) {
            // convert to String[][] array
            String data = rs.getData().get(field);
            if (data != null) {
                JsonArray json = new JsonParser().parse(data).getAsJsonArray();
                String[][] strings = new String[10][4];
                for (int i = 0; i < 10; i++) {
                    JsonArray inner = json.get(i).getAsJsonArray();
                    for (int j = 0; j < 4; j++) {
                        String block = inner.get(j).getAsString();
                        if (!block.startsWith("minecraft")) {
                            return null;
                        }
                        strings[i][j] = block;
                    }
                }
                return TARDISChameleonPreset.buildTARDISChameleonColumn(d, strings, rs.getData().get("asymmetric").equals("1"), false);
            }
        }
        return null;
    }
}
