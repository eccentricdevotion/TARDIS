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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class QueryFactory {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    Connection connection = service.getConnection();

    public QueryFactory(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves an SQL ResultSet from an SQLite database. This method builds an
     * SQL query string from the parameters supplied and then executes the
     * query.
     *
     * @param table the database table name to query.
     * @param data a List<string> of table fields to retrieve.
     * @param where a HashMap<String, Object> of table fields and values to
     * refine the search.
     */
    public ResultSet getResults(String table, List<String> data, HashMap<String, Object> where) {
        Statement statement = null;
        ResultSet rs = null;
        String fields;
        String wheres;
        StringBuilder sbf = new StringBuilder();
        for (String f : data) {
            sbf.append(f).append(",");
        }
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("',");
            } else {
                sbw.append(entry.getValue()).append(",");
            }
        }
        data.clear();
        where.clear();
        fields = sbf.toString().substring(0, sbf.length() - 1);
        wheres = sbw.toString().substring(0, sbw.length() - 1);
        String query = "SELECT " + fields + " FROM " + table + " WHERE " + wheres + "";
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            plugin.debug("ResultSet error for " + table + "! " + e.getMessage());
        } finally {
            try {
                rs.close();
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
        return rs;
    }

    /**
     * Inserts data into an SQLite database table. This method builds an SQL
     * query string from the parameters supplied and then executes the insert.
     *
     * @param table the database table name to insert the data into.
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     */
    public boolean doInsert(String table, HashMap<String, Object> data) {
        Statement statement = null;
        String fields;
        String values;
        StringBuilder sbf = new StringBuilder();
        StringBuilder sbv = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbf.append(entry.getKey()).append(",");
            if (entry.getValue().getClass().equals(String.class)) {
                sbv.append("'").append(entry.getValue()).append("',");
            } else {
                sbv.append(entry.getValue()).append(",");
            }
        }
        data.clear();
        fields = sbf.toString().substring(0, sbf.length() - 1);
        values = sbv.toString().substring(0, sbv.length() - 1);
        String query = "INSERT INTO " + table + " (" + fields + ") VALUES (" + values + ")";
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.debug("Insert error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Inserts data into an SQLite database table. This method builds a prepared
     * SQL statement from the parameters supplied and then executes the
     * insert. You will need to supply data for all table fields.
     *
     * @param table the database table name to insert the data into.
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     */
    public boolean doPreparedInsert(String table, HashMap<String, Object> data) {
        PreparedStatement ps = null;
        String fields;
        String questions;
        StringBuilder sbf = new StringBuilder();
        StringBuilder sbq = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbf.append(entry.getKey()).append(",");
            sbq.append("?").append(",");
        }
        fields = sbf.toString().substring(0, sbf.length() - 1);
        questions = sbq.toString().substring(0, sbq.length() - 1);
        try {
            ps = connection.prepareStatement("INSERT INTO " + table + " (" + fields + ") VALUES (" + questions + ")");
            int i = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class)) {
                    ps.setString(i, entry.getValue().toString());
                } else {
                    ps.setInt(i, plugin.utils.parseNum(entry.getValue().toString()));
                }
                i++;
            }
            data.clear();
            return (ps.executeUpdate() > 0);
        } catch (SQLException e) {
            plugin.debug("Update error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Updates data in an SQLite database table. This method builds an SQL query
     * string from the parameters supplied and then executes the update.
     *
     * @param table the database table name to update.
     * @param data a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to update.
     */
    public boolean doUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        Statement statement = null;
        String updates;
        String wheres;
        StringBuilder sbu = new StringBuilder();
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbu.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbu.append("'").append(entry.getValue()).append("',");
            } else {
                sbu.append(entry.getValue()).append(",");
            }
        }
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("',");
            } else {
                sbw.append(entry.getValue()).append(",");
            }
        }
        data.clear();
        where.clear();
        updates = sbu.toString().substring(0, sbu.length() - 1);
        wheres = sbw.toString().substring(0, sbw.length() - 1);
        String query = "UPDATE " + table + " SET " + updates + " WHERE " + wheres;
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.debug("Update error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Deletes rows from an SQLite database table. This method builds an SQL
     * query string from the parameters supplied and then executes the delete.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to delete.
     */
    public boolean doDelete(String table, HashMap<String, Object> where) {
        Statement statement = null;
        String values;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("',");
            } else {
                sbw.append(entry.getValue()).append(",");
            }
        }
        where.clear();
        values = sbw.toString().substring(0, sbw.length() - 1);
        String query = "DELETE FROM " + table + " WHERE " + values;
        plugin.debug(query);
        try {
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.debug("Delete error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }
}