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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;

/**
 * The sonic screwdriver is a highly versatile tool used by many, but not all,
 * incarnations of the Doctor. The Doctor modified and ostensibly upgraded it
 * over the years, giving it an increasing number of applications.
 *
 * Control types: 2 = environment-repeater 3 = x-repeater 4 = z-repeater 5 =
 * y-repeater
 *
 * @author eccentric_nz
 */
public class ResultSetRepeaters {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final int secondary;
    private final byte[] diodes = new byte[4];
    List<String> locations = new ArrayList<>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the controls table.
     *
     * @param plugin an instance of the main class.
     * @param id the TARDIS id to search for.
     * @param secondary the level of control to look for.
     */
    public ResultSetRepeaters(TARDIS plugin, int id, int secondary) {
        this.plugin = plugin;
        this.id = id;
        this.secondary = secondary;
        this.prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the controls table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        int i = 0;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT DISTINCT location FROM " + prefix + "controls WHERE tardis_id = ? AND type IN (2,3,4,5) AND secondary = ? ORDER BY type";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setInt(2, secondary);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    locations.add(rs.getString("location"));
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for controls table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing controls table! " + e.getMessage());
            }
        }
        return true;
    }

    
    public byte[] getRepeaters() {
        if (locations.size() == 4) {
            // get repeater settings
            diodes[0] = TARDISLocationGetters.getLocationFromDB(locations.get(0), 0, 0).getBlock().getData();
            diodes[1] = TARDISLocationGetters.getLocationFromDB(locations.get(1), 0, 0).getBlock().getData();
            diodes[2] = TARDISLocationGetters.getLocationFromDB(locations.get(2), 0, 0).getBlock().getData();
            diodes[3] = TARDISLocationGetters.getLocationFromDB(locations.get(3), 0, 0).getBlock().getData();
        } else {
            diodes[0] = (byte) -1;
        }
        return diodes;
    }

    public List<String> getLocations() {
        return locations;
    }
}
