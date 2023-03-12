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
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author eccentric_nz
 */
public class TVMQueryFactory {

    private final TARDISVortexManipulator plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private final String prefix;
    Connection connection = service.getConnection();

    public TVMQueryFactory(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Inserts data into an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param data  a HashMap<String, Object> of table fields and values to insert.
     */
    public void doInsert(String table, HashMap<String, Object> data) {
        TVMSQLInsert insert = new TVMSQLInsert(plugin, table, data);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, insert);
    }

    /**
     * Inserts data into an SQLite database table. This method builds a prepared SQL statement from the parameters
     * supplied and then executes the insert.
     *
     * @param table the database table name to insert the data into.
     * @param data  a HashMap<String, Object> of table fields and values to insert.
     * @return the primary key of the record that was inserted
     */
    public int doSyncInsert(String table, HashMap<String, Object> data) {
        PreparedStatement ps = null;
        ResultSet idRS = null;
        String fields;
        String questions;
        StringBuilder sbf = new StringBuilder();
        StringBuilder sbq = new StringBuilder();
        data.entrySet().forEach((entry) -> {
            sbf.append(entry.getKey()).append(",");
            sbq.append("?,");
        });
        fields = sbf.toString().substring(0, sbf.length() - 1);
        questions = sbq.toString().substring(0, sbq.length() - 1);
        try {
            service.testConnection(connection);
            ps = connection.prepareStatement("INSERT INTO " + prefix + table + " (" + fields + ") VALUES (" + questions + ")", PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                    ps.setString(i, entry.getValue().toString());
                } else if (entry.getValue().getClass().getName().contains("Double")) {
                    ps.setDouble(i, TARDISNumberParsers.parseDouble(entry.getValue().toString()));
                } else if (entry.getValue().getClass().getName().contains("Float")) {
                    ps.setFloat(i, TARDISNumberParsers.parseFloat(entry.getValue().toString()));
                } else if (entry.getValue().getClass().getName().contains("Long")) {
                    ps.setLong(i, TARDISNumberParsers.parseLong(entry.getValue().toString()));
                } else {
                    ps.setInt(i, TARDISNumberParsers.parseInt(entry.getValue().toString()));
                }
                i++;
            }
            data.clear();
            ps.executeUpdate();
            idRS = ps.getGeneratedKeys();
            return (idRS.next()) ? idRS.getInt(1) : -1;
        } catch (SQLException e) {
            plugin.debug("Insert error for " + table + "! " + e.getMessage());
            return -1;
        } finally {
            try {
                if (idRS != null) {
                    idRS.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Updates data in an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to update.
     * @param data  a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to select the records to update.
     */
    public void doUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        TVMSQLUpdate update = new TVMSQLUpdate(plugin, table, data, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, update);
    }

    /**
     * Deletes rows from an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to select the records to delete.
     */
    public void doDelete(String table, HashMap<String, Object> where) {
        TVMSQLDelete delete = new TVMSQLDelete(plugin, table, where);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, delete);
    }

    /**
     * Deletes rows from an SQLite database table. This method executes the SQL in a separate thread.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to select the records to delete.
     * @return true or false depending on whether the data was deleted successfully
     */
    public boolean doSyncDelete(String table, HashMap<String, Object> where) {
        Statement statement = null;
        String values;
        StringBuilder sbw = new StringBuilder();
        where.entrySet().forEach((entry) -> {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        });
        where.clear();
        values = sbw.toString().substring(0, sbw.length() - 5);
        String query = "DELETE FROM " + prefix + table + " WHERE " + values;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.debug("Delete error for " + table + "! " + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }

    /**
     * Save a beacon block.
     *
     * @param uuid the uuid of the player who has set the beacon
     * @param b    the block to save
     */
    public void saveBeaconBlock(String uuid, Block b) {
        Location loc = b.getLocation();
        plugin.getBlocks().add(loc);
        String data = b.getBlockData().getAsString();
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", uuid);
        set.put("location", loc.toString());
        set.put("block_type", data);
        doSyncInsert("beacons", set);
    }

    /**
     * Alter tachyon levels. This method executes the SQL in a separate thread.
     *
     * @param uuid   the player's string UUID
     * @param amount the amount add tachyons to add or remove
     */
    public void alterTachyons(String uuid, int amount) {
        TVMAlterTachyon alter = new TVMAlterTachyon(plugin, amount, uuid);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, alter);
    }

    /**
     * Update message read status. This method executes the SQL in a separate thread.
     *
     * @param id the message_id to alter
     */
    public void setReadStatus(int id) {
        TVMSetReadStatus set = new TVMSetReadStatus(plugin, id);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, set);
    }
}
