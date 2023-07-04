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
package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.converters.lookup.LegacyColourTable;
import me.eccentric_nz.TARDIS.database.converters.lookup.LegacyIdTable;
import me.eccentric_nz.TARDIS.database.converters.lookup.LegacyTypeTable;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISMaterialIDConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final List<Integer> COLOURED = Arrays.asList(35, 95, 159, 160, 171, 251, 252);

    public TARDISMaterialIDConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void checkCondenserData() {
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT c_id, block_data FROM " + prefix + "condenser";
        String update = "UPDATE " + prefix + "condenser SET block_data = ? WHERE c_id = ?";
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            // do condenser data
            statement = connection.prepareStatement(query);
            ps = connection.prepareStatement(update);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int c_id = rs.getInt("c_id");
                    String blockData = rs.getString("block_data");
                    try {
                        Material.valueOf(blockData);
                        // if it's a valid material skip it
                        continue;
                    } catch (IllegalArgumentException e) {
                        plugin.debug("Condenser data was not a valid Material");
                        // look up blockData to get the correct material...
                        String mat = LegacyTypeTable.LOOKUP.get(blockData);
                        if (mat != null) {
                            // update the record
                            ps.setString(1, mat);
                            ps.setInt(2, c_id);
                            ps.addBatch();
                        }
                    }
                    i++;
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " condenser IDs to material names");
                }
            }
            plugin.getConfig().set("conversions.condenser_materials", true);
            plugin.saveConfig();
        } catch (SQLException e) {
            plugin.debug("Conversion error for condenser materials! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing condenser table (converting IDs)! " + e.getMessage());
            }
        }
    }

    public void checkPlayerPrefsData() {
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT pp_id, wall, floor, siege_wall, siege_floor FROM " + prefix + "player_prefs";
        String update = "UPDATE " + prefix + "player_prefs SET wall = ?, floor = ?, siege_wall = ?, siege_floor = ? WHERE pp_id = ?";
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            // do condenser data
            statement = connection.prepareStatement(query);
            ps = connection.prepareStatement(update);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String wall = rs.getString("wall");
                    String floor = rs.getString("floor");
                    String siegeWall = rs.getString("siege_wall");
                    String siegeFloor = rs.getString("siege_floor");
                    String newWall = rs.getString("wall");
                    String newFloor = rs.getString("floor");
                    String newSiegeWall = rs.getString("siege_wall");
                    String newSiegeFloor = rs.getString("siege_floor");
                    Material material;
                    try {
                        material = Material.valueOf(wall);
                    } catch (IllegalArgumentException e) {
                        // look up blockData to get the correct material...
                        String mat = LegacyTypeTable.LOOKUP.get(wall);
                        if (mat != null) {
                            newWall = mat;
                        }
                    }
                    try {
                        material = Material.valueOf(floor);
                    } catch (IllegalArgumentException e) {
                        // look up blockData to get the correct material...
                        String mat = LegacyTypeTable.LOOKUP.get(floor);
                        if (mat != null) {
                            newFloor = mat;
                        }
                    }
                    try {
                        material = Material.valueOf(siegeWall);
                    } catch (IllegalArgumentException e) {
                        // look up blockData to get the correct material...
                        String mat = LegacyTypeTable.LOOKUP.get(siegeWall);
                        if (mat != null) {
                            newSiegeWall = mat;
                        }
                    }
                    try {
                        material = Material.valueOf(siegeFloor);
                    } catch (IllegalArgumentException e) {
                        // look up blockData to get the correct material...
                        String mat = LegacyTypeTable.LOOKUP.get(siegeFloor);
                        if (mat != null) {
                            newSiegeFloor = mat;
                        }
                    }
                    if (!wall.equals(newWall) || !floor.equals(newFloor) || !siegeWall.equals(newSiegeWall) || !siegeFloor.equals(newSiegeFloor)) {
                        int pp_id = rs.getInt("pp_id");
                        // update the record
                        ps.setString(1, newWall);
                        ps.setString(2, newFloor);
                        ps.setString(3, newSiegeWall);
                        ps.setString(4, newSiegeFloor);
                        ps.setInt(5, pp_id);
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " player_prefs IDs to material names");
                }
            }
            plugin.getConfig().set("conversions.player_prefs_materials", true);
            plugin.saveConfig();
        } catch (SQLException e) {
            plugin.debug("Conversion error for player_prefs materials! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing player_prefs table (converting IDs)! " + e.getMessage());
            }
        }
    }

    public void checkBlockData() {
        Statement checker = null;
        PreparedStatement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String check = (plugin.getConfig().getString("storage.database").equals("sqlite")) ? "SELECT sql FROM sqlite_master WHERE tbl_name = '" + prefix + "blocks' AND sql LIKE '%block INTEGER DEFAULT 0%'" : "SHOW COLUMNS FROM " + prefix + "blocks LIKE 'block'";
        String query = "SELECT b_id, block, data FROM " + prefix + "blocks";
        String update = "UPDATE " + prefix + "blocks SET data = ? WHERE b_id = ?";
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            checker = connection.createStatement();
            ResultSet rsfc = checker.executeQuery(check);
            if (rsfc.next()) {
                // do blocks data
                statement = connection.prepareStatement(query);
                ps = connection.prepareStatement(update);
                rs = statement.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        int b_id = rs.getInt("b_id");
                        int block = rs.getInt("block");
                        int data = rs.getInt("data");
                        Material material = LegacyIdTable.LOOKUP.get(block);
                        if (material != null) {
                            if (data != 0) {
                                if (COLOURED.contains(block)) {
                                    String white = material.toString();
                                    String[] tmp = white.split("_");
                                    String colour = white.replace(tmp[0], LegacyColourTable.LOOKUP.get(data));
                                    material = Material.valueOf(colour);
                                }
                            }
                            // update the record
                            ps.setString(1, material.createBlockData().getAsString());
                        } else {
                            plugin.debug("Could not convert legacy material, defaulting to AIR!");
                            // update the record
                            ps.setString(1, TARDISConstants.AIR.getAsString());
                        }
                        ps.setInt(2, b_id);
                        ps.addBatch();
                        i++;
                    }
                    if (i > 0) {
                        ps.executeBatch();
                        connection.commit();
                        plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " blocks IDs to material names");
                    }
                }
            }
            plugin.getConfig().set("conversions.block_materials", true);
            plugin.saveConfig();
        } catch (SQLException e) {
            plugin.debug("Conversion error for blocks materials! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (checker != null) {
                    checker.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing blocks table (converting ID & data)! " + e.getMessage());
            }
        }
    }
}
