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
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the name of the custom exterior
 * model.
 *
 * @author eccentric_nz
 */
public class ResultSetTardisModel {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private String itemPreset = "";
    private String itemDemat = "";

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the tardis table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTardisModel(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Gets to the chameleon preset and demat keys of a TARDIS to determine the material to use for the associated
     * custom exterior model. This method builds an SQL query string from the parameters supplied and then executes the
     * query.
     *
     * @param id the TARDIS id to check
     * @return true or false depending on whether the TARDIS is powered on
     */
    public boolean fromID(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT uuid, chameleon_preset, chameleon_demat FROM " + prefix + "tardis WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                if (rs.getString("chameleon_preset").startsWith("ITEM:")) {
                    String[] split = rs.getString("chameleon_preset").split(":");
                    if (split.length > 1) {
                        itemPreset = split[1];
                    } else {
                        itemPreset = "Bad Wolf";
                        TARDISStaticUtils.warnPreset(uuid);
                    }
                }
                if (rs.getString("chameleon_demat").startsWith("ITEM:")) {
                    String[] split = rs.getString("chameleon_demat").split(":");
                    if (split.length > 1) {
                        itemDemat = split[1];
                    } else {
                        itemDemat = "Bad Wolf";
                        TARDISStaticUtils.warnPreset(uuid);
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [custom model keys fromID] table! " + e.getMessage());
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
                plugin.debug("Error closing tardis [custom model keys fromID] table! " + e.getMessage());
            }
        }
    }

    public String getItemPreset() {
        return itemPreset;
    }

    public String getItemDemat() {
        return itemDemat;
    }
}
