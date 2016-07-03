/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.block.Block;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... the location of the TARDIS Police Box blocks.
 *
 * @author eccentric_nz
 */
public class ResultSetConsole {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int tardis_id;
    private final String prefix;
    private Block sign;
    private String preset;
    private String world;
    private String location;
    private String biome;
    private int artronLevel;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the blocks table.
     *
     * @param plugin - an instance of the main class
     * @param tardis_id - the tardis_id to get the data for
     */
    public ResultSetConsole(TARDIS plugin, int tardis_id) {
        this.plugin = plugin;
        this.tardis_id = tardis_id;
        this.prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the tardis, current and controls tables.
     * This method builds an SQL query string from the parameters supplied and
     * then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean locationData() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "tardis.chameleon_preset, " + prefix + "current.*, " + prefix + "controls.location "
                + "FROM " + prefix + "tardis, " + prefix + "current, " + prefix + "controls "
                + "WHERE " + prefix + "controls.type = 22 AND " + prefix + "tardis.tardis_id = ? "
                + "AND " + prefix + "tardis.tardis_id = " + prefix + "current.tardis_id "
                + "AND " + prefix + "tardis.tardis_id = " + prefix + "controls.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, tardis_id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                this.sign = plugin.getLocationUtils().getLocationFromBukkitString(rs.getString("location")).getBlock();
                this.preset = rs.getString("chameleon_preset");
                this.world = rs.getString("world");
                this.location = rs.getString("x") + ", " + rs.getString("y") + ", " + rs.getString("z");
                this.biome = rs.getString("biome");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for blocks table! " + e.getMessage());
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
                plugin.debug("Error closing blocks table! " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis, current and controls tables.
     * This method builds an SQL query string from the parameters supplied and
     * then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean artronData() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "tardis.artron_level, " + prefix + "controls.location "
                + "FROM " + prefix + "tardis, " + prefix + "controls "
                + "WHERE " + prefix + "controls.type = 22 AND " + prefix + "tardis.tardis_id = ? "
                + "AND " + prefix + "tardis.tardis_id = " + prefix + "controls.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, tardis_id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                this.sign = plugin.getLocationUtils().getLocationFromBukkitString(rs.getString("location")).getBlock();
                this.artronLevel = rs.getInt("artron_level");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for blocks table! " + e.getMessage());
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
                plugin.debug("Error closing blocks table! " + e.getMessage());
            }
        }
        return true;
    }

    public Block getSign() {
        return sign;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getPreset() {
        return preset;
    }

    public String getWorld() {
        return world;
    }

    public String getLocation() {
        return location;
    }

    public String getBiome() {
        return biome;
    }

    public int getArtronLevel() {
        return artronLevel;
    }
}
