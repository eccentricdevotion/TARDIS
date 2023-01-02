/*
 * Copyright (C) 2023 eccentric_nz
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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetCompanions {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final List<UUID> companions = new ArrayList<>();
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the current locations table.
     *
     * @param plugin an instance of the main class.
     * @param id     the TARDIS id to get the companions for.
     */
    public ResultSetCompanions(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves list of companion UUIDs from the tardis table.
     *
     * @return a list of companion UUIDs or if none an empty list.
     */
    public List<UUID> getCompanions() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT uuid, companions FROM " + prefix + "tardis WHERE tardis_id =" + id;
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                // always add the Time Lord of the TARDIS
                companions.add(UUID.fromString(rs.getString("uuid")));
                String comps = rs.getString("companions");
                if (!rs.wasNull() && !comps.isEmpty()) {
                    // add companions
                    String compStr = rs.getString("companions");
                    if (compStr.equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            companions.add(p.getUniqueId());
                        }
                    } else {
                        for (String c : rs.getString("companions").split(":")) {
                            companions.add(UUID.fromString(c));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for companions! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing companions! " + e.getMessage());
            }
        }
        return companions;
    }
}
