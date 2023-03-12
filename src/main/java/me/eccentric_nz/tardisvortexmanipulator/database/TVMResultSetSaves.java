/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMSave;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMResultSetSaves {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private final String uuid;
    private final int start, limit;
    private final List<TVMSave> saves = new ArrayList<>();
    private final String prefix;

    public TVMResultSetSaves(TARDISVortexManipulator plugin, String uuid, int start, int limit) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.start = start;
        this.limit = limit;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the messages table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = String.format("SELECT * FROM " + prefix + "saves WHERE uuid = ? ORDER BY save_name LIMIT %d, %d", start, start + limit);
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    TVMSave tvms = new TVMSave();
                    tvms.setId(rs.getInt("save_id"));
                    tvms.setName(rs.getString("save_name"));
                    String w = rs.getString("world");
                    tvms.setWorld(w);
                    tvms.setX(rs.getFloat("x"));
                    tvms.setY(rs.getFloat("y"));
                    tvms.setZ(rs.getFloat("z"));
                    World world = plugin.getServer().getWorld(w);
                    tvms.setEnv((world != null) ? world.getEnvironment().toString() : "NORMAL");
                    saves.add(tvms);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for saves table! " + e.getMessage());
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
                plugin.debug("Error closing saves table! " + e.getMessage());
            }
        }
        return true;
    }

    public List<TVMSave> getSaves() {
        return saves;
    }
}
