/*
 * Copyright (C) 2013 eccentric_nz
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
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... everything about the construction of the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetPoliceBox {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetPoliceBox(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves records from the tardis table. This method builds a list of
     * Chunks around Police Box locations to stop them unloading.
     *
     */
    public void loadChunks() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM current";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    World w = plugin.getServer().getWorld(rs.getString("world"));
                    if (w != null) {
                        Chunk c = w.getChunkAt(new Location(w, rs.getInt("x"), rs.getInt("y"), rs.getInt("z")));
                        if (!plugin.tardisChunkList.contains(c)) {
                            plugin.tardisChunkList.add(c);
                            c.load();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for current table (loading Police Box chunks)! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing current table (loading Police Box chunks)! " + e.getMessage());
            }
        }
    }
}
