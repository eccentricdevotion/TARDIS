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
import org.bukkit.World;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... a list of locations the TARDIS can travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetHomeLocation {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final HashMap<String, Object> where;
    private int home_id;
    private int tardis_id;
    private World world;
    private int x;
    private int y;
    private int z;
    private TARDISConstants.COMPASS direction;
    private boolean submarine;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the next locations table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public ResultSetHomeLocation(TARDIS plugin, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.where = where;
    }

    /**
     * Retrieves an SQL ResultSet from the destinations table. This method
     * builds an SQL query string from the parameters supplied and then executes
     * the query. Use the getters to retrieve the results.
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
        String query = "SELECT * FROM homes" + wheres;
        //plugin.debug(query);
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
                    this.home_id = rs.getInt("home_id");
                    this.tardis_id = rs.getInt("tardis_id");
                    this.world = plugin.getServer().getWorld(rs.getString("world"));
                    this.x = rs.getInt("x");
                    this.y = rs.getInt("y");
                    this.z = rs.getInt("z");
                    this.direction = TARDISConstants.COMPASS.valueOf(rs.getString("direction"));
                    this.submarine = rs.getBoolean("submarine");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for destinations table! " + e.getMessage());
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
                plugin.debug("Error closing destinations table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getHome_id() {
        return home_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public TARDISConstants.COMPASS getDirection() {
        return direction;
    }

    public boolean isSubmarine() {
        return submarine;
    }
}
