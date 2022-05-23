/*
 * Copyright (C) 2022 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.Schematic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... non-abandoned TARDISes.
 *
 * @author eccentric_nz
 */
public class ResultSetTardisAbandoned {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int tardis_id;
    private int artron_level;
    private Schematic schematic;
    private PRESET preset;
    private boolean handbrake_on;
    private boolean hidden;
    private boolean tardis_init;
    private boolean powered_on;
    private boolean lights_on;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTardisAbandoned(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Gets to the TARDIS data required to claim abandon a TARDIS. This method builds an SQL query string from the
     * parameters supplied and then executes the query.
     *
     * @param uuid the Time Lord uuid to check
     * @return true if the TARDIS is not yet abandoned
     */
    public boolean fromUUID(String uuid) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, artron_level, size, chameleon_preset, handbrake_on, hidden, tardis_init, powered_on, lights_on FROM " + prefix + "tardis WHERE uuid = ? AND abandoned = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                tardis_id = rs.getInt("tardis_id");
                artron_level = rs.getInt("artron_level");
                schematic = Consoles.schematicFor(rs.getString("size").toLowerCase(Locale.ENGLISH));
                try {
                    preset = PRESET.valueOf(rs.getString("chameleon_preset"));
                } catch (IllegalArgumentException e) {
                    preset = PRESET.FACTORY;
                }
                handbrake_on = rs.getBoolean("handbrake_on");
                hidden = rs.getBoolean("hidden");
                tardis_init = rs.getBoolean("tardis_init");
                powered_on = rs.getBoolean("powered_on");
                lights_on = rs.getBoolean("lights_on");
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [not abandoned] table! " + e.getMessage());
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
                plugin.debug("Error closing tardis [not abandoned] table! " + e.getMessage());
            }
        }
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public int getArtron_level() {
        return artron_level;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public PRESET getPreset() {
        return preset;
    }

    public boolean isHandbrake_on() {
        return handbrake_on;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isTardis_init() {
        return tardis_init;
    }

    public boolean isPowered_on() {
        return powered_on;
    }

    public boolean isLights_on() {
        return lights_on;
    }
}
