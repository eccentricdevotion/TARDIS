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
package me.eccentric_nz.TARDIS.schematic;

import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class ResultSetArchiveByName {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final String uuid;
    private final String name;
    private Archive archive;

    public ResultSetArchiveByName(TARDIS plugin, String uuid, String name) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        this.uuid = uuid;
        this.name = name;
    }

    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "archive.*, " + prefix + "player_prefs.lights FROM "
                + prefix + "archive, " + prefix + "player_prefs WHERE archive.uuid = ? AND name = ?"
                + " AND " + prefix + "archive.uuid = " + prefix + "player_prefs.uuid";
        plugin.debug(query);
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setString(2, name);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    TardisLight light;
                    try {
                        light = TardisLight.valueOf(rs.getString("lights"));
                    } catch (IllegalArgumentException e) {
                        light = TardisLight.LAMP;
                    }
                    archive = new Archive(
                            UUID.fromString(rs.getString("uuid")),
                            rs.getString("name"),
                            rs.getString("console_size"),
                            rs.getBoolean("beacon"),
                            light,
                            rs.getInt("use"),
                            rs.getInt("y"),
                            JsonParser.parseString(rs.getString("data")).getAsJsonObject(),
                            rs.getString("description")
                    );
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for archive! " + e.getMessage());
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
                plugin.debug("Error closing archive! " + e.getMessage());
            }
        }
        return true;
    }

    public Archive getArchive() {
        return archive;
    }
}
