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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... everything about the construction of the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class TARDISLocationsConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final List<String> directions = Arrays.asList(new String[]{"NORTH", "SOUTH", "EAST", "WEST"});

    ;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis table.
     *
     * @param plugin an instance of the main class.
     */
    public TARDISLocationsConverter(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves records from the tardis table. This method transfers old save
     * location strings to the new location style class formats.
     */
    public void convert() {
        QueryFactory qf = new QueryFactory(this.plugin);
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, direction, home, save, current, fast_return FROM tardis";
        int i = 0;
        try {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("tardis_id");
                    String d = rs.getString("direction");
                    HashMap<String, Object> seth = buildMap(id, rs.getString("home").split(":"), d);
                    qf.doInsert("homes", seth);
                    HashMap<String, Object> setn = buildMap(id, rs.getString("save").split(":"), d);
                    qf.doInsert("next", setn);
                    HashMap<String, Object> setc = buildMap(id, rs.getString("current").split(":"), d);
                    qf.doInsert("current", setc);
                    HashMap<String, Object> setb;
                    if (!rs.getString("fast_return").isEmpty()) {
                        setb = buildMap(id, rs.getString("fast_return").split(":"), d);
                    } else {
                        setb = buildMap(id, rs.getString("current").split(":"), d);
                    }
                    qf.doInsert("back", setb);
                    i++;
                }
            }
        } catch (SQLException e) {
            plugin.debug("Conversion error for saved locations! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing current table (loading Police Box chunks)! " + e.getMessage());
            }
        }
        if (i > 0) {
            plugin.console.sendMessage(plugin.pluginName + "Converted " + i + " TARDIS locations to new format");
            plugin.getConfig().set("location_conversion_done", true);
            plugin.saveConfig();
        }
    }

    private HashMap<String, Object> buildMap(int id, String[] data, String d) {
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("tardis_id", id);
        set.put("world", data[0]);
        set.put("x", Integer.parseInt(data[1]));
        set.put("y", Integer.parseInt(data[2]));
        set.put("z", Integer.parseInt(data[3]));
        int l = data.length;
        set.put("direction", (l > 4 && directions.contains(data[4])) ? data[4] : d);
        set.put("submarine", (l > 5 && data[5].equals("true")) ? 1 : 0);
        return set;
    }
}
