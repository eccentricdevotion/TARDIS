/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS
 * controls.
 *
 * @author eccentric_nz
 */
public class ResultSetLightLevel {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private Location location;
    private int c_id;
    private int tardis_id;
    private int type;
    private int level;
    private boolean powered;
    private boolean lightsOn;
    private boolean policeBox;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the controls table.
     *
     * @param plugin   an instance of the main class.
     */
    public ResultSetLightLevel(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the controls table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean fromLocation(String loc) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "controls.*, " + prefix + "tardis.powered_on, " + prefix + "tardis.lights_on, " + prefix + "tardis.chameleon_preset FROM " + prefix + "controls, " + prefix + "tardis WHERE `type` IN (49, 50, 57) AND location = ? AND " + prefix + "controls.tardis_id = " + prefix + "tardis.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, loc);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                c_id = rs.getInt("c_id");
                tardis_id = rs.getInt("tardis_id");
                type = rs.getInt("type");
                level = rs.getInt("secondary");
                powered = rs.getBoolean("powered_on");
                lightsOn = rs.getBoolean("lights_on");
                String[] split = rs.getString("chameleon_preset").split(":");
                ChameleonPreset preset;
                try {
                    preset = ChameleonPreset.valueOf(split[0]);
                } catch (IllegalArgumentException e) {
                    preset = ChameleonPreset.FACTORY;
                }
                policeBox = preset.usesArmourStand();
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for light level control table! " + e.getMessage());
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
                plugin.debug("Error closing light level control table! " + e.getMessage());
            }
        }
        return true;
    }

    public boolean fromTypeAndID(int which, int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT " + prefix + "controls.*, " + prefix + "tardis.powered_on, " + prefix + "tardis.lights_on, " + prefix + "tardis.chameleon_preset FROM " + prefix + "controls, " + prefix + "tardis WHERE `type` = ? AND " + prefix + "controls.tardis_id = ? AND " + prefix + "controls.tardis_id = " + prefix + "tardis.tardis_id";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, which);
            statement.setInt(2, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                c_id = rs.getInt("c_id");
                tardis_id = rs.getInt("tardis_id");
                type = rs.getInt("type");
                location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location"));
                level = rs.getInt("secondary");
                powered = rs.getBoolean("powered_on");
                lightsOn = rs.getBoolean("lights_on");
                String[] split = rs.getString("chameleon_preset").split(":");
                ChameleonPreset preset;
                try {
                    preset = ChameleonPreset.valueOf(split[0]);
                } catch (IllegalArgumentException e) {
                    preset = ChameleonPreset.FACTORY;
                }
                policeBox = preset.usesArmourStand();
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for light level control [type,id] table! " + e.getMessage());
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
                plugin.debug("Error closing light level control [type,id] table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getControlId() {
        return c_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public int getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public boolean isPowered() {
        return powered;
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public boolean isPoliceBox() {
        return policeBox;
    }
}
