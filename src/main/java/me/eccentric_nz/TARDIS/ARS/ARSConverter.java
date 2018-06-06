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
package me.eccentric_nz.TARDIS.ARS;

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.TARDISMaterialIDConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author eccentric_nz
 */
public class ARSConverter {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();

    public ARSConverter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void convertARS() {
        TARDISMaterialIDConverter tmic = new TARDISMaterialIDConverter(plugin);
        PreparedStatement query = null;
        PreparedStatement update = null;
        ResultSet rs = null;
        try {
            service.testConnection(connection);
            query = connection.prepareStatement("SELECT ars_id, json FROM ars");
            update = connection.prepareStatement("UPDATE ars set json = ? WHERE ars_id = ?");
            rs = query.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("ars_id");
                    String js = rs.getString("json");
                    if (rs.wasNull()) {
                        continue;
                    }
                    String[][][] grid = new String[3][9][9];
                    JSONArray json = new JSONArray(js);
                    for (int y = 0; y < 3; y++) {
                        JSONArray jsonx = json.getJSONArray(y);
                        for (int x = 0; x < 9; x++) {
                            JSONArray jsonz = jsonx.getJSONArray(x);
                            for (int z = 0; z < 9; z++) {
                                if (jsonz.getInt(z) == 46) {
                                    grid[y][x][z] = "STONE";
                                } else {
                                    grid[y][x][z] = tmic.LEGACY_ID_LOOKUP.get(jsonz.getInt(z)).toString();
                                }
                            }
                        }
                    }
                    JSONArray arr = new JSONArray(grid);
                    update.setString(1, arr.toString());
                    update.setInt(2, id);
                    update.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for ars table! " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (query != null) {
                    query.close();
                }
                if (update != null) {
                    update.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing ars table! " + e.getMessage());
            }
        }
    }
}
