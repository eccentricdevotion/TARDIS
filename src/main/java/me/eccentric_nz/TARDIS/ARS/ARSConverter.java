/*
 * Copyright (C) 2023 eccentric_nz
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.converters.TARDISMaterialIDConverter;

/**
 * @author eccentric_nz
 */
public class ARSConverter {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public ARSConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void convertARS() {
        TARDISMaterialIDConverter tmic = new TARDISMaterialIDConverter(plugin);
        PreparedStatement query = null;
        PreparedStatement update = null;
        ResultSet rs = null;
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            query = connection.prepareStatement("SELECT ars_id, json FROM " + prefix + "ars");
            update = connection.prepareStatement("UPDATE " + prefix + "ars set json = ? WHERE ars_id = ?");
            rs = query.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("ars_id");
                    String js = rs.getString("json");
                    if (rs.wasNull()) {
                        continue;
                    }
                    if (!js.contains("1")) { // default empty slot is stone - id: 1
                        // no IDs found, already converted
                        continue;
                    }
                    String[][][] grid = new String[3][9][9];
                    JsonArray json = JsonParser.parseString(js).getAsJsonArray();
                    for (int y = 0; y < 3; y++) {
                        JsonArray jsonx = json.get(y).getAsJsonArray();
                        for (int x = 0; x < 9; x++) {
                            JsonArray jsonz = jsonx.get(x).getAsJsonArray();
                            for (int z = 0; z < 9; z++) {
                                if (jsonz.get(z).getAsInt() == 46) {
                                    grid[y][x][z] = "STONE";
                                } else {
                                    grid[y][x][z] = tmic.LEGACY_ID_LOOKUP.get(jsonz.get(z).getAsInt()).toString();
                                }
                            }
                        }
                    }
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    JsonArray arr = JsonParser.parseString(gson.toJson(grid)).getAsJsonArray();
                    update.setString(1, arr.toString());
                    update.setInt(2, id);
                    update.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                update.executeBatch();
                connection.commit();
                plugin.getLogger().log(Level.INFO, "Converted " + i + " ARS records");
            }
            plugin.getConfig().set("conversions.ars_materials", true);
            plugin.saveConfig();
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
