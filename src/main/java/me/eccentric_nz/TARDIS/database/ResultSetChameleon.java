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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... the chameleon location of the TARDIS interior.
 *
 * @author eccentric_nz
 */
public class ResultSetChameleon {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
//    private int chameleon_id;
//    private int tardis_id;
//    private String blueprintID;
//    private String blueprintData;
//    private String stainID;
//    private String stainData;
//    private String glassID;
//    private String glassData;
    private final HashMap<String, String> data = new HashMap<String, String>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the chameleon table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public ResultSetChameleon(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
        this.prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the chameleon table. This method builds
     * an SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ? AND ");
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM " + prefix + "chameleon" + wheres;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue().getClass().equals(String.class)) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        statement.setInt(s, TARDISNumberParsers.parseInt(entry.getValue().toString()));
                    }
                    s++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columns = rsmd.getColumnCount();
                    for (int i = 1; i <= columns; i++) {
                        data.put(rsmd.getColumnName(i), rs.getString(i));
                    }
//                    this.chameleon_id = rs.getInt("chameleon_id");
//                    this.tardis_id = rs.getInt("tardis_id");
//                    this.blueprintID = rs.getString("blueprintID");
//                    this.blueprintData = rs.getString("blueprintData");
//                    this.stainID = rs.getString("stainID");
//                    this.stainData = rs.getString("stainData");
//                    this.glassID = rs.getString("glassID");
//                    this.glassData = rs.getString("glassData");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for chameleon table! " + e.getMessage());
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
                plugin.debug("Error closing chameleon table! " + e.getMessage());
            }
        }
        return true;
    }

//    public int getChameleon_id() {
//        return chameleon_id;
//    }
//
//    public int getTardis_id() {
//        return tardis_id;
//    }
//
//    public String getBlueprintID() {
//        return blueprintID;
//    }
//
//    public String getBlueprintData() {
//        return blueprintData;
//    }
//
//    public String getStainID() {
//        return stainID;
//    }
//
//    public String getStainData() {
//        return stainData;
//    }
//
//    public String getGlassID() {
//        return glassID;
//    }
//
//    public String getGlassData() {
//        return glassData;
//    }
    public HashMap<String, String> getData() {
        return data;
    }
}
