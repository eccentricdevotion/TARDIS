/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
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
                    PRESET preset;
                    try {
                        preset = PRESET.valueOf(rs.getString("chameleon_preset"));
                    } catch (IllegalArgumentException e) {
                        preset = PRESET.FACTORY;
                    }
                    switch (rs.getString("size")) {
                        case "JUNK":
                            sd = new StandbyData(Integer.MAX_VALUE, UUID.fromString(rs.getString("uuid")), false, false, PRESET.JUNK, false);
                            break;
                        case "ARCHIVE":
                            HashMap<String, Object> wherea = new HashMap<>();
                            wherea.put("uuid", rs.getString("uuid"));
                            wherea.put("use", 1);
                            ResultSetArchive rsa = new ResultSetArchive(plugin, wherea);
                            boolean lanterns = false;
                            if (rsa.resultSet()) {
                                lanterns = rsa.getArchive().isLanterns();
                            }
                            sd = new StandbyData(Integer.MAX_VALUE, UUID.fromString(rs.getString("uuid")), rs.getBoolean("hidden"), rs.getBoolean("lights_on"), preset, lanterns);
                            break;
                        default:
                            sd = new StandbyData(rs.getInt("artron_level"), UUID.fromString(rs.getString("uuid")), rs.getBoolean("hidden"), rs.getBoolean("lights_on"), preset, CONSOLES.getBY_NAMES().get(rs.getString("size")).hasLanterns());
                            break;
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

    public static class StandbyData {

        final int level;
        final UUID uuid;
        final boolean hidden;
        final boolean lights;
        final PRESET preset;
        final boolean lanterns;

        StandbyData(int level, UUID uuid, boolean hidden, boolean lights, PRESET preset, boolean lanterns) {
            this.level = level;
            this.uuid = uuid;
            this.hidden = hidden;
            this.lights = lights;
            this.preset = preset;
            this.lanterns = lanterns;
        }

        public int getLevel() {
            return level;
        }

        public UUID getUuid() {
            return uuid;
        }

        public boolean isHidden() {
            return hidden;
        }

        public boolean isLights() {
            return lights;
        }

        public PRESET getPreset() {
            return preset;
        }

        public boolean isLanterns() {
            return lanterns;
        }
    }
}
