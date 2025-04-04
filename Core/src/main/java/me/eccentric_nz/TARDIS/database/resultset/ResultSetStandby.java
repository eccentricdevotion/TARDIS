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
import me.eccentric_nz.TARDIS.database.data.StandbyData;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Gets a list of TARDIS ids whose power is on.
 *
 * @author eccentric_nz
 */
public class ResultSetStandby {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public ResultSetStandby(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public HashMap<Integer, StandbyData> onStandby() {
        HashMap<Integer, StandbyData> ids = new HashMap<>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, artron_level, chameleon_preset, size, hidden, lights_on, uuid FROM " + prefix + "tardis WHERE powered_on = 1 AND abandoned = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    StandbyData sd;
                    ChameleonPreset preset;
                    try {
                        if (rs.getString("chameleon_preset").startsWith("ITEM")) {
                            preset = ChameleonPreset.ITEM;
                        } else {
                            preset = ChameleonPreset.valueOf(rs.getString("chameleon_preset"));
                        }
                    } catch (IllegalArgumentException e) {
                        preset = ChameleonPreset.FACTORY;
                    }
                    switch (rs.getString("size")) {
                        case "JUNK" -> sd = new StandbyData(Integer.MAX_VALUE, UUID.fromString(rs.getString("uuid")), false, false, ChameleonPreset.JUNK, TardisLight.TENTH);
                        case "ARCHIVE" -> {
                            HashMap<String, Object> wherea = new HashMap<>();
                            wherea.put("uuid", rs.getString("uuid"));
                            wherea.put("use", 1);
                            ResultSetArchive rsa = new ResultSetArchive(plugin, wherea);
                            TardisLight lightType = TardisLight.LAMP;
                            if (rsa.resultSet()) {
                                lightType = rsa.getArchive().getLight();
                            }
                            sd = new StandbyData(Integer.MAX_VALUE, UUID.fromString(rs.getString("uuid")), rs.getBoolean("hidden"), rs.getBoolean("lights_on"), preset, lightType);
                        }
                        default -> {
                            // get the TARDIS/player's light preference
                            TardisLight light;
                            ResultSetLightPrefs rsp = new ResultSetLightPrefs(plugin);
                            if (rsp.fromID(rs.getInt("tardis_id"))) {
                                light = rsp.getLight();
                            } else {
                                light = Consoles.getBY_NAMES().get(rs.getString("size")).getLights();
                            }
                            sd = new StandbyData(rs.getInt("artron_level"), UUID.fromString(rs.getString("uuid")), rs.getBoolean("hidden"), rs.getBoolean("lights_on"), preset, light);
                        }
                    }
                    ids.put(rs.getInt("tardis_id"), sd);
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for standby! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing standby! " + e.getMessage());
            }
        }
        return ids;
    }
}
