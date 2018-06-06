/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.Smelter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 * <p>
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater 4 = multiplier-repeater 5 =
 * environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetSmelter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String where;
    private int smelter_id;
    private int tardis_id;
    private String location;
    private List<Chest> fuelChests;
    private List<Chest> oreChests;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     * @param where  the location of the smelter chest.
     */
    public ResultSetSmelter(TARDIS plugin, String where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the vaults table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "vaults WHERE location = ? AND x = 0 AND y = 0 AND z = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    smelter_id = rs.getInt("v_id");
                    tardis_id = rs.getInt("tardis_id");
                    location = rs.getString("location");
                    fuelChests = getChests(location, true);
                    oreChests = getChests(location, false);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for smelter table! " + e.getMessage());
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
                plugin.debug("Error closing smelter table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getSmelter_id() {
        return smelter_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getLocation() {
        return location;
    }

    public List<Chest> getFuelChests() {
        return fuelChests;
    }

    public List<Chest> getOreChests() {
        return oreChests;
    }

    private List<Chest> getChests(String location, boolean fuel) {
        Location l = plugin.getLocationUtils().getLocationFromBukkitString(location);
        List<Chest> chests = new ArrayList<>();
        List<Vector> vectors = (fuel) ? Smelter.getFuelVectors() : Smelter.getOreVectors();
        vectors.forEach((v) -> {
            Block b = l.clone().add(v).getBlock();
            if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                Chest chest = (Chest) b.getState();
                chests.add(chest);
            }
        });
        return chests;
    }
}
