/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author rob
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
            }
            if (entry.getValue().getClass().equals(Integer.TYPE)) {
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
            }
            if (entry.getValue().getClass().equals(Integer.TYPE)) {
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
            }
            if (entry.getValue().getClass().equals(Integer.TYPE)) {
                sbu.append(entry.getValue()).append(",");
            }
        }
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("',");
            }
            if (entry.getValue().getClass().equals(Integer.TYPE)) {
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
}