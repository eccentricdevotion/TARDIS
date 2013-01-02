/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.TARDISConstants.ROOM;

/**
 *
 * @author eccentric_nz
 */
public class ResultSetRooms {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private boolean multiple;
    private int room_id;
    private int tardis_id;
    private String world;
    private int startx;
    private int starty;
    private int startz;
    private int endx;
    private int endy;
    private int endz;
    private ROOM room_type;
    private COMPASS room_direction;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the doors table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public ResultSetRooms(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the rooms table. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query. Use the getters to retrieve the results.
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String wheres = "";
        if (where != null) {
            StringBuilder sbw = new StringBuilder();
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sbw.append(entry.getKey()).append(" = ");
                if (entry.getValue().getClass().equals(String.class)) {
                    sbw.append("'").append(entry.getValue()).append("' AND ");
                } else {
                    sbw.append(entry.getValue()).append(" AND ");
                }
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 5);
            where.clear();
        }
        String query = "SELECT * FROM rooms" + wheres;
        //plugin.debug(query);
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (multiple) {
                        HashMap<String, String> row = new HashMap<String, String>();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columns = rsmd.getColumnCount();
                        for (int i = 1; i < columns + 1; i++) {
                            row.put(rsmd.getColumnName(i).toLowerCase(), rs.getString(i));
                        }
                        data.add(row);
                    }
                    this.room_id = rs.getInt("room_id");
                    this.tardis_id = rs.getInt("tardis_id");
                    this.world = rs.getString("world");
                    this.startx = rs.getInt("startx");
                    this.starty = rs.getInt("starty");
                    this.startz = rs.getInt("startz");
                    this.endx = rs.getInt("endx");
                    this.endy = rs.getInt("endy");
                    this.endz = rs.getInt("endz");
                    this.room_type = ROOM.valueOf(rs.getString("room_type"));
                    this.room_direction = COMPASS.valueOf(rs.getString("room_direction"));
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for rooms table! " + e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing rooms table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getRoom_id() {
        return room_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getWorld() {
        return world;
    }

    public int getStartx() {
        return startx;
    }

    public int getStarty() {
        return starty;
    }

    public int getStartz() {
        return startz;
    }

    public int getEndx() {
        return endx;
    }

    public int getEndy() {
        return endy;
    }

    public int getEndz() {
        return endz;
    }

    public ROOM getRoom_type() {
        return room_type;
    }

    public COMPASS getRoom_direction() {
        return room_direction;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}