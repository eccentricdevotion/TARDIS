/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMResultSetBlock {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private final String uuid;
    private final List<TVMBlock> blocks = new ArrayList<>();
    private final String prefix;

    public TVMResultSetBlock(TARDISVortexManipulator plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the beacons table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "beacons WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    TVMBlock tvmb = new TVMBlock();
                    Location l = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location"));
                    BlockData blockData = plugin.getServer().createBlockData(rs.getString("block_type"));
                    Block b = l.getBlock();
                    tvmb.setBlock(b);
                    tvmb.setBlockData(blockData);
                    blocks.add(tvmb);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("Block error for beacons table! " + e.getMessage());
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
                plugin.debug("Error closing beacons table! " + e.getMessage());
            }
        }
        return true;
    }

    public List<TVMBlock> getBlocks() {
        return blocks;
    }
}
