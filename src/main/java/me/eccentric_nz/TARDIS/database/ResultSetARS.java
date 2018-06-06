/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tricky van Baalen was the youngest and the smartest of the van Baalen brothers. Tricky worked for his brothers Gregor
 * and Bram, finding and processing salvaged spaceships. Although made captain by their father, he suffered an accident
 * in which he lost his eyes, his voice, and his memory. He was fitted with synthetic eyes and a partially
 * electronic-sounding voice box; unable to replace his memory, his brothers instead convinced him that he was in fact
 * an android.
 *
 * @author eccentric_nz
 */
public class ResultSetARS {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private int id;
    private int tardis_id;
    private UUID uuid;
    private int east;
    private int south;
    private int layer;
    private String json;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the ars table.
     *
     * @param plugin an instance of the main class.
     * @param where  a HashMap<String, Object> of table fields and values to refine the search.
     */
    public ResultSetARS(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the lamps table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            where.entrySet().forEach((entry) -> {
                sbw.append(entry.getKey()).append(" = ? AND ");
            });
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM " + prefix + "ars" + wheres;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
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
                    id = rs.getInt("ars_id");
                    tardis_id = rs.getInt("tardis_id");
                    uuid = UUID.fromString(rs.getString("uuid"));
                    east = rs.getInt("ars_x_east");
                    south = rs.getInt("ars_z_south");
                    layer = rs.getInt("ars_y_layer");
                    json = rs.getString("json");
                    if (rs.wasNull()) {
                        json = "";
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for ars table! " + e.getMessage());
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
                plugin.debug("Error closing ars table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getEast() {
        return east;
    }

    public int getSouth() {
        return south;
    }

    public int getLayer() {
        return layer;
    }

    public String getJson() {
        return json;
    }
}
