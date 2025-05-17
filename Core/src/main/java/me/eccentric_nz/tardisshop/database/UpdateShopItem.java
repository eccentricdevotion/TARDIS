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
package me.eccentric_nz.tardisshop.database;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateShopItem {

    private final TARDIS plugin;
    private final String prefix;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();

    public UpdateShopItem(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Updates data in the items table. This method builds an SQL query string
     * from the parameters supplied and then executes the update.
     *
     * @param data a HashMap&lt;String, Object&gt; of table fields and values update.
     * @param where a HashMap&lt;String, Object&gt; of table fields and values to
     * select the records to update.
     */
    public void alterRecord(HashMap<String, Object> data, HashMap<String, Object> where) {
        PreparedStatement ps = null;
        String updates;
        String wheres;
        StringBuilder sbu = new StringBuilder();
        StringBuilder sbw = new StringBuilder();
        data.forEach((key, value) -> sbu.append(key).append(" = ?,"));
        where.forEach((key, value) -> {
            sbw.append(key).append(" = ");
            if (value instanceof String || value instanceof UUID) {
                sbw.append("'").append(value).append("' AND ");
            } else {
                sbw.append(value).append(" AND ");
            }
        });
        where.clear();
        updates = sbu.substring(0, sbu.length() - 1);
        wheres = sbw.substring(0, sbw.length() - 5);
        String query = "UPDATE " + prefix + "items SET " + updates + " WHERE " + wheres;
        try {
            ps = connection.prepareStatement(query);
            int s = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() instanceof String || entry.getValue() instanceof UUID) {
                    ps.setString(s, entry.getValue().toString());
                } else if (entry.getValue() instanceof Double) {
                    ps.setDouble(s, (Double) entry.getValue());
                }
                s++;
            }
            data.clear();
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for items table! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing items table! " + e.getMessage());
            }
        }
    }

    public void addLocation(String location, int id) {
        PreparedStatement ps = null;
        final String query = "UPDATE " + prefix + "items SET location = ? WHERE item_id = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, location);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for items table! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing items table! " + e.getMessage());
            }
        }
    }

    public void updateCost(double cost, int id) {
        PreparedStatement ps = null;
        final String query = "UPDATE " + prefix + "items SET cost = ? WHERE item_id = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setDouble(1, cost);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for items table! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing items table! " + e.getMessage());
            }
        }
    }
}
