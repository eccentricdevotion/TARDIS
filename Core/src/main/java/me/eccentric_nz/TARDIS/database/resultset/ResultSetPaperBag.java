/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ResultSetPaperBag {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final UUID uuid;
    private final List<Integer> flavours = new ArrayList<>(List.of(1, 2, 3, 4));
    private final String prefix;
    private int paperBagID;
    private String flavour1;
    private int amount1;
    private String flavour2;
    private int amount2;
    private String flavour3;
    private int amount3;
    private String flavour4;
    private int amount4;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the count table.
     *
     * @param plugin an instance of the main class.
     * @param uuid   a player's UUID to refine the search.
     */
    public ResultSetPaperBag(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the lamps table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "paper_bag WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                paperBagID = rs.getInt("paper_bag_id");
                flavour1 = rs.getString("flavour_1");
                amount1 = rs.getInt("amount_1");
                flavour2 = rs.getString("flavour_2");
                amount2 = rs.getInt("amount_2");
                flavour3 = rs.getString("flavour_3");
                amount3 = rs.getInt("amount_3");
                flavour4 = rs.getString("flavour_4");
                amount4 = rs.getInt("amount_4");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for paper_bag table! " + e.getMessage());
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
                plugin.debug("Error closing paper_bag table! " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * Retrieves a paper bag record and selects a random jelly baby
     *
     * @return a jelly baby flavour
     */
    public String getJellyBaby() {
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "paper_bag WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                Collections.shuffle(flavours);
                for (int f : flavours) {
                    int amount = rs.getInt("amount_" + f);
                    if (amount > 0) {
                        String update = "UPDATE " + prefix + "paper_bag set amount_" + f + " = ? WHERE uuid = ?";
                        ps = connection.prepareStatement(update);
                        ps.setInt(1, amount - 1);
                        ps.setString(2, uuid.toString());
                        ps.executeUpdate();
                        return rs.getString("flavour_" + f);
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for paper_bag table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing paper_bag table! " + e.getMessage());
            }
        }
        return null;
    }

    public int getPaperBagID() {
        return paperBagID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFlavour1() {
        return flavour1;
    }

    public int getAmount1() {
        return amount1;
    }

    public String getFlavour2() {
        return flavour2;
    }

    public int getAmount2() {
        return amount2;
    }

    public String getFlavour3() {
        return flavour3;
    }

    public int getAmount3() {
        return amount3;
    }

    public String getFlavour4() {
        return flavour4;
    }

    public int getAmount4() {
        return amount4;
    }
}
