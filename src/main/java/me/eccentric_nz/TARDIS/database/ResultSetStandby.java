/*
 * Copyright (C) 2014 eccentric_nz
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.PRESET;

/**
 * Gets a list of TARDIS ids whose power is on.
 *
 * @author eccentric_nz
 */
public class ResultSetStandby {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public ResultSetStandby(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    public HashMap<Integer, StandbyData> onStandby() {
        HashMap<Integer, StandbyData> ids = new HashMap<Integer, StandbyData>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, artron_level, chameleon_preset, size, hidden, lights_on, uuid FROM " + prefix + "tardis WHERE powered_on = 1";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    StandbyData sd;
                    if (rs.getString("size").equals("JUNK")) {
                        sd = new StandbyData(Integer.MAX_VALUE, UUID.fromString(rs.getString("uuid")), false, false, PRESET.JUNK, false);
                    } else {
                        sd = new StandbyData(rs.getInt("artron_level"), UUID.fromString(rs.getString("uuid")), rs.getBoolean("hidden"), rs.getBoolean("lights_on"), PRESET.valueOf(rs.getString("chameleon_preset")), CONSOLES.getByNames().get(rs.getString("size")).hasLanterns());
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

    public class StandbyData {

        int level;
        UUID uuid;
        boolean hidden;
        boolean lights;
        PRESET preset;
        boolean lanterns;

        public StandbyData(int level, UUID uuid, boolean hidden, boolean lights, PRESET preset, boolean lanterns) {
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
