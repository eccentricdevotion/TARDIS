/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.block.Block;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetDoorBlocks {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private Block innerBlock;
    private Block outerBlock;
    private COMPASS innerDirection;
    private COMPASS outerDirection;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the current locations table.
     *
     * @param plugin an instance of the main class.
     * @param id     the TARDIS id to get the doors for.
     */
    public ResultSetDoorBlocks(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves door from the doors table.
     *
     * @return the door block.
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "doors WHERE door_type IN (0,1) AND tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    // get block
                    String door = rs.getString("door_location");
                    if (rs.getInt("door_type") == 0) {
                        outerBlock = TARDISStaticLocationGetters.getLocationFromDB(door).getBlock();
                        outerDirection = COMPASS.valueOf(rs.getString("door_direction"));
                    } else {
                        innerBlock = TARDISStaticLocationGetters.getLocationFromDB(door).getBlock();
                        innerDirection = COMPASS.valueOf(rs.getString("door_direction"));
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for door! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing door! " + e.getMessage());
            }
        }
        return true;
    }

    public Block getInnerBlock() {
        return innerBlock;
    }

    public Block getOuterBlock() {
        return outerBlock;
    }

    public COMPASS getInnerDirection() {
        return innerDirection;
    }

    public COMPASS getOuterDirection() {
        return outerDirection;
    }
}
