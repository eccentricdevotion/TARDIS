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
package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.custombiome.BiomeUtilities;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TARDISWorldNameConverter {

    private final HashMap<String, String> worldTables = new HashMap<>();
    private final HashMap<String, String> locationTables = new HashMap<>();
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String world;
    private final String legacy;
    private final String prefix;

    public TARDISWorldNameConverter(TARDIS plugin, String world) {
        this.plugin = plugin;
        this.world = world;
        legacy = BiomeUtilities.getLevelName() + "_tardis_" + world;
        prefix = this.plugin.getPrefix();
        worldTables.put("areas", "area_id");
        worldTables.put("back", "back_id");
        worldTables.put("current", "current_id");
        worldTables.put("destinations", "dest_id");
        worldTables.put("dispersed", "d_id");
        worldTables.put("homes", "home_id");
        worldTables.put("next", "next_id");
        locationTables.put("blocks", "b_id");
        locationTables.put("doors", "door_id");
        locationTables.put("forcefield", "uuid");
        locationTables.put("portals", "portal_id");
    }

    public void update() {
        Statement statement = null;
        PreparedStatement ps = null;
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // update world names in `areas/back/current/destinations/dispersed/homes/next/` tables
            for (Map.Entry<String, String> entry : worldTables.entrySet()) {
                String worldQuery = "SELECT " + entry.getValue() + ", world FROM " + prefix + entry.getKey() + "tardis WHERE world LIKE '%" + world + "'";
                String worldUpdate = "UPDATE " + prefix + entry.getKey() + "SET world = '" + world + "' WHERE " + entry.getValue() + " = ?";
                ps = connection.prepareStatement(worldUpdate);
                connection.setAutoCommit(false);
                // get records
                ResultSet rsw = statement.executeQuery(worldQuery);
                if (rsw.isBeforeFirst()) {
                    while (rsw.next()) {
                        // if there is a record
                        ps.setInt(1, rsw.getInt(entry.getValue()));
                        ps.addBatch();
                        i++;
                    }
                }
            }
            // update locations in `blocks/doors/forcefield/portals?/` tables
            // Location{world=CraftWorld{name=TARDIS_TimeVortex},x=499.0,y=66.0,z=504.0,pitch=0.0,yaw=0.0}
            // Earth:-73:66:-27
            for (Map.Entry<String, String> entry : locationTables.entrySet()) {
                String locationQuery = "SELECT " + entry.getValue() + ", location FROM " + prefix + entry.getKey() + "tardis WHERE location LIKE '%" + world + "%'";
                String locationUpdate = "UPDATE " + prefix + entry.getKey() + "SET location = ? WHERE " + entry.getValue() + " = ?";
                ps = connection.prepareStatement(locationUpdate);
                connection.setAutoCommit(false);
                // get records
                ResultSet rsl = statement.executeQuery(locationQuery);
                if (rsl.isBeforeFirst()) {
                    while (rsl.next()) {
                        String oldLocation = rsl.getString("location");
                        String newLocation = oldLocation.replace(legacy, world);
                        // if there is a record
                        ps.setString(1, newLocation);
                        ps.setInt(2, rsl.getInt(entry.getValue()));
                        ps.addBatch();
                        i++;
                    }
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " old " + world + " world name records");
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            plugin.debug("Conversion error for legacy world names! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing tables (converting legacy world names)! " + e.getMessage());
            }
        }
    }
}

