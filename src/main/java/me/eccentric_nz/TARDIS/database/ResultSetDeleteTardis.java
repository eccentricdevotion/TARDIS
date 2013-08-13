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
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... everything about the construction of the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetDeleteTardis {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private int tardis_id;
    private String owner;
    private String chunk;
    private TARDISConstants.COMPASS direction;
    private TARDISConstants.SCHEMATIC schematic;
    private String current;
    private boolean hidden;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     * @param limit
     * @param multiple a boolean indicating whether multiple rows should be
     * fetched
     */
    public ResultSetDeleteTardis(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis table. This method builds an
     * SQL query string from the parameters supplied and then executes the
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
                if (entry.getKey().equals("current")) {
                    sbw.append(entry.getKey()).append(" LIKE ? AND ");
                } else {
                    sbw.append(entry.getKey()).append(" = ? AND ");
                }
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
        }
        String query = "SELECT * FROM tardis" + wheres;
        try {
            statement = connection.prepareStatement(query);
            if (where != null) {
                int s = 1;
                for (Map.Entry<String, Object> entry : where.entrySet()) {
                    if (entry.getValue().getClass().equals(String.class)) {
                        statement.setString(s, entry.getValue().toString());
                    } else {
                        statement.setInt(s, plugin.utils.parseNum(entry.getValue().toString()));
                    }
                    s++;
                }
                where.clear();
            }
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    this.tardis_id = rs.getInt("tardis_id");
                    this.owner = rs.getString("owner");
                    this.chunk = rs.getString("chunk");
                    this.direction = (!rs.getString("direction").isEmpty()) ? TARDISConstants.COMPASS.valueOf(rs.getString("direction")) : TARDISConstants.COMPASS.EAST;
                    this.schematic = TARDISConstants.SCHEMATIC.valueOf(rs.getString("size"));
                    this.current = rs.getString("current");
                    this.hidden = rs.getBoolean("hidden");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis (delete) table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                plugin.debug("Error closing tardis (delete) table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getOwner() {
        return owner;
    }

    public String getChunk() {
        return chunk;
    }

    public TARDISConstants.COMPASS getDirection() {
        return direction;
    }

    public TARDISConstants.SCHEMATIC getSchematic() {
        return schematic;
    }

    public String getCurrent() {
        return current;
    }

    public boolean isHidden() {
        return hidden;
    }
}
