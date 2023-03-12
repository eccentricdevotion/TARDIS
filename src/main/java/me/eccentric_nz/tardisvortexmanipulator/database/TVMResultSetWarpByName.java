/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author eccentric_nz
 */
public class TVMResultSetWarpByName {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private final String uuid;
    private final String name;
    private final String prefix;
    private int id;
    private Location warp;

    public TVMResultSetWarpByName(TARDISVortexManipulator plugin, String uuid, String name) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
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
        String query = "SELECT * FROM " + prefix + "saves WHERE uuid = ? AND save_name = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setString(2, name);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                id = rs.getInt("save_id");
                World world = plugin.getServer().getWorld(rs.getString("world"));
                float x = rs.getFloat("x");
                float y = rs.getFloat("y");
                float z = rs.getFloat("z");
                float yaw = rs.getFloat("yaw");
                float pitch = rs.getFloat("pitch");
                warp = new Location(world, x, y, z, yaw, pitch);
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("Warp error for saves table! " + e.getMessage());
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
                plugin.debug("Error closing saves table for warp! " + e.getMessage());
            }
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public Location getWarp() {
        return warp;
    }
}
