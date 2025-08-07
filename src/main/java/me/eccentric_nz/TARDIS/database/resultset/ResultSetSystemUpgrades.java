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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetSystemUpgrades {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final String uuid;
    private final String prefix;
    private SystemUpgrade data;

    public ResultSetSystemUpgrades(TARDIS plugin, int id, String uuid) {
        this.plugin = plugin;
        this.id = id;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    public boolean resultset() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "player_prefs.artron_level, " + prefix + "system_upgrades.* " +
                "FROM " + prefix + "player_prefs, " + prefix + "system_upgrades " +
                "WHERE " + prefix + "system_upgrades.uuid = ? " +
                "AND " + prefix + "system_upgrades.tardis_id = ? " +
                "AND " + prefix + "system_upgrades.uuid = " + prefix + "player_prefs.uuid";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setInt(2, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                data = new SystemUpgrade(
                    rs.getInt("artron_level"),
                    rs.getBoolean("architecture"),
                    rs.getBoolean("chameleon"),
                    rs.getBoolean("rooms"),
                    rs.getBoolean("desktop"),
                    rs.getBoolean("feature"),
                    rs.getBoolean("saves"),
                    rs.getBoolean("monitor"),
                    rs.getBoolean("force_field"),
                    rs.getBoolean("tools"),
                    rs.getBoolean("locator"),
                    rs.getBoolean("telepathic"),
                    rs.getBoolean("stattenheim_remote"),
                    rs.getBoolean("navigation"),
                    rs.getBoolean("distance_1"),
                    rs.getBoolean("distance_2"),
                    rs.getBoolean("distance_3"),
                    rs.getBoolean("inter_dimension"),
                    rs.getBoolean("throttle"),
                    rs.getBoolean("faster"),
                    rs.getBoolean("rapid"),
                    rs.getBoolean("warp"),
                    rs.getBoolean("flight")
                );
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for player_prefs (system upgrades from UUID) table! " + e.getMessage());
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
                plugin.debug("Error closing player_prefs (system upgrades from UUID) statement! " + e.getMessage());
            }
        }
        return true;
    }

    public SystemUpgrade getData() {
        return data;
    }
}
