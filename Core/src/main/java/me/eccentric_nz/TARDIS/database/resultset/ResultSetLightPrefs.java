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
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 *
 * @author eccentric_nz
 */
public class ResultSetLightPrefs {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int tardisId;
    private TardisLight light;
    private Material material;
    private String sequence;
    private String delays;
    private String levels;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetLightPrefs(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Attempts to see whether the supplied TARDIS id is in the tardis table. This method builds an SQL query string
     * from the parameters supplied and then executes the query.
     *
     * @param id the TARDIS id to get light preferences for
     * @return true or false depending on whether the TARDIS id exists in the table
     */
    public boolean fromID(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "light_prefs WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                tardisId = rs.getInt("tardis_id");
                light = TardisLight.valueOf(rs.getString("light"));
                try {
                    material = Material.valueOf(rs.getString("material"));
                } catch (IllegalArgumentException e) {
                    material = Material.BONE_BLOCK;
                }
                sequence = rs.getString("pattern");
                delays = rs.getString("delays");
                levels = rs.getString("levels");
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for light_prefs table! " + e.getMessage());
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
                plugin.debug("Error closing light_prefs table! " + e.getMessage());
            }
        }
    }

    public int getTardisId() {
        return tardisId;
    }

    public TardisLight getLight() {
        return light;
    }

    public Material getMaterial() {
        return material;
    }

    public String getSequence() {
        return sequence;
    }

    public String getDelays() {
        return delays;
    }

    public String getLevels() {
        return levels;
    }
}
