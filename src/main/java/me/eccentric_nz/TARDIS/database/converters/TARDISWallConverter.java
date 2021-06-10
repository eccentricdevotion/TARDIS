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
package me.eccentric_nz.tardis.database.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TARDISWallConverter {

    private final String[] find = new String[]{"east=false", "east=true", "north=false", "north=true", "south=false", "south=true", "west=false", "west=true"};
    private final String[] replace = new String[]{"east=none", "east=low", "north=none", "north=low", "south=none", "south=low", "west=none", "west=low"};
    private final List<String> walls = new ArrayList<>();
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDISPlugin plugin;
    private final String prefix;

    public TARDISWallConverter(TARDISPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        walls.add("cobblestone_wall");
        walls.add("mossy_cobblestone_wall");
        walls.add("stone_brick_wall");
        walls.add("mossy_stone_brick_wall");
        walls.add("andesite_wall");
        walls.add("diorite_wall");
        walls.add("granite_wall");
        walls.add("sandstone_wall");
        walls.add("red_sandstone_wall");
        walls.add("brick_wall");
        walls.add("prismarine_wall");
        walls.add("nether_brick_wall");
        walls.add("red_nether_brick_wall");
        walls.add("end_stone_brick_wall");
    }

    private boolean isWall(String inputStr) {
        return walls.stream().anyMatch(inputStr::contains);
    }

    private String fixWallData(String text) {
        // "minecraft:cobblestone_wall[east=true,north=false,south=false,up=true,waterlogged=false,west=false]"
        // changes to ==>
        // "minecraft:cobblestone_wall[east=low,north=none,south=none,up=true,waterlogged=false,west=none]"
        return StringUtils.replaceEach(text, find, replace);
    }

    public void processArchives() {
        int i = 0;
        ResultSet rs = null;
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM " + prefix + "archive";
        String update = "UPDATE " + prefix + "archive SET data = ? WHERE archive_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            ps = connection.prepareStatement(update);
            rs = statement.executeQuery();
            connection.setAutoCommit(false);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    boolean updateRecord = false;
                    JsonObject json = JsonParser.parseString(rs.getString("data")).getAsJsonObject();
                    // get dimensions
                    JsonObject dimensions = json.get("dimensions").getAsJsonObject();
                    int h = dimensions.get("height").getAsInt();
                    int w = dimensions.get("width").getAsInt();
                    int c = dimensions.get("length").getAsInt();
                    JsonArray arr = json.get("input").getAsJsonArray();
                    // loop through json array
                    for (int level = 0; level < h; level++) {
                        JsonArray floor = arr.get(level).getAsJsonArray();
                        for (int row = 0; row < w; row++) {
                            JsonArray r = (JsonArray) floor.get(row);
                            for (int col = 0; col < c; col++) {
                                JsonObject block = r.get(col).getAsJsonObject();
                                String data = block.get("data").getAsString();
                                if (isWall(data)) {
                                    String fixed = fixWallData(data);
                                    block.remove("data");
                                    block.addProperty("data", fixed);
                                    updateRecord = true;
                                }
                            }
                        }
                    }
                    if (updateRecord) {
                        // write json back to database
                        ps.setString(1, json.toString());
                        ps.setInt(2, rs.getInt("archive_id"));
                        ps.addBatch();
                        i++;
                    }
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " archive wall block data records");
            }
            plugin.getConfig().set("conversions.archive_wall_data", true);
            plugin.saveConfig();
        } catch (SQLException e) {
            plugin.debug("ResultSet error for archive wall conversion! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing archive wall conversion! " + e.getMessage());
            }
        }
    }
}
