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

/**
 *
 * @author eccentric_nz
 */
public class ResultSetBlocks {

    private TARDISDatabase service = TARDISDatabase.getInstance();
    private Connection connection = service.getConnection();
    private TARDIS plugin;
    private HashMap<String, Object> where;
    private boolean multiple;
    private int id;
    private int tardis_id;
    private String location;
    private int blockId;
    private byte blockData;
    private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the blocks table.
     *
     * @param plugin an instance of the main class.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     * @param multiple a boolean indicating whether multiple rows should be
     * fetched
     */
    public ResultSetBlocks(TARDIS plugin, HashMap<String, Object> where, boolean multiple) {
        this.plugin = plugin;
        this.where = where;
        this.multiple = multiple;
    }

    /**
     * Retrieves an SQL ResultSet from the blocks table. This method builds an
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
                    sbw.append("'").append(entry.getValue()).append("',");
                } else {
                    sbw.append(entry.getValue()).append(",");
                }
            }
            wheres = " WHERE " + sbw.toString().substring(0, sbw.length() - 1);
            where.clear();
        }
        String query = "SELECT * FROM blocks" + wheres;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                plugin.debug(query);
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
                    this.id = rs.getInt("block_id");
                    this.tardis_id = rs.getInt("tardis_id");
                    this.location = rs.getString("location");
                    this.blockId = rs.getInt("block");
                    this.blockData = rs.getByte("data");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for destinations table! " + e.getMessage());
            return false;
        } finally {
            try {
                rs.close();
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing destinations table! " + e.getMessage());
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

    public String getLocation() {
        return location;
    }

    public int getBlockId() {
        return blockId;
    }

    public byte getBlockData() {
        return blockData;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }
}