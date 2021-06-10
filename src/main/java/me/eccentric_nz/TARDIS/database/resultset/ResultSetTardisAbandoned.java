/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.database.resultset;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.enumeration.Schematic;

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
    private final TARDISPlugin plugin;
    private final String prefix;
    private int tardisId;
    private int artronLevel;
    private Schematic schematic;
    private PRESET preset;
    private boolean handbrakeOn;
    private boolean hidden;
    private boolean tardisInit;
    private boolean powered;
    private boolean lightsOn;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTardisAbandoned(TARDISPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Gets to the tardis data required to claim abandon a tardis. This method builds an SQL query string from the
     * parameters supplied and then executes the query.
     *
     * @param uuid the Time Lord uuid to check
     * @return true if the tardis is not yet abandoned
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
                tardisId = rs.getInt("tardis_id");
                artronLevel = rs.getInt("artron_level");
                schematic = Consoles.schematicFor(rs.getString("size").toLowerCase(Locale.ENGLISH));
                try {
                    preset = PRESET.valueOf(rs.getString("chameleon_preset"));
                } catch (IllegalArgumentException e) {
                    preset = PRESET.FACTORY;
                }
                handbrakeOn = rs.getBoolean("handbrake_on");
                hidden = rs.getBoolean("hidden");
                tardisInit = rs.getBoolean("tardis_init");
                powered = rs.getBoolean("powered_on");
                lightsOn = rs.getBoolean("lights_on");
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

    public int getTardisId() {
        return tardisId;
    }

    public int getArtronLevel() {
        return artronLevel;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public PRESET getPreset() {
        return preset;
    }

    public boolean isHandbrakeOn() {
        return handbrakeOn;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isTardisInit() {
        return tardisInit;
    }

    public boolean isPowered() {
        return powered;
    }

    public boolean isLightsOn() {
        return lightsOn;
    }
}
