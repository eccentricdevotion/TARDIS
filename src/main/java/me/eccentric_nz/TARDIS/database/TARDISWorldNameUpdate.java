/*
 * Copyright (C) 2019 eccentric_nz
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

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISWorldNameUpdate {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISWorldNameUpdate(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void replaceNames() {
        Statement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String select;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            connection.setAutoCommit(false);
            select = "SELECT * FROM " + prefix + "tardis";
            rs = statement.executeQuery(select);
            // tardis table
            query = "UPDATE " + prefix + "tardis SET chunk = ?, chameleon = ?, creeper = ?, condenser = ?, scanner = ?, beacon = ?, eps = ?, rail = ?, renderer = ?, zero = ? WHERE tardis_id = ?";
            ps = connection.prepareStatement(query);
            int i = 0;
            while (rs.next()) {
                if (rs.getString("chunk").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("chunk").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(2, rs.getString("chameleon").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(3, rs.getString("creeper").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(4, rs.getString("condenser").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(5, rs.getString("scanner").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(6, rs.getString("beacon").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(7, rs.getString("eps").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(8, rs.getString("rail").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(9, rs.getString("renderer").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(10, rs.getString("zero").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(11, rs.getInt("tardis_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " tardis world name records");
            }
            // blocks
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "blocks SET location = ? WHERE b_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT b_id, location FROM " + prefix + "blocks";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("location").contains("tardis_time_vortex") || rs.getString("location").contains("gallifrey") || rs.getString("location").contains("siluria") || rs.getString("location").contains("skaro")) {
                    ps.setString(1, rs.getString("location").replace("tardis_time_vortex", "TARDIS_TimeVortex").replace("gallifrey", "Gallifrey").replace("siluria", "Siluria").replace("skaro", "Skaro"));
                    ps.setInt(2, rs.getInt("b_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " lamps world name records");
            }
            // controls
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "controls SET location = ? WHERE c_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT c_id, location FROM " + prefix + "controls";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("location").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("location").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(2, rs.getInt("c_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " controls world name records");
            }
            // doors
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "doors SET door_location = ? WHERE door_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT door_id, door_location FROM " + prefix + "doors";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("door_location").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("door_location").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(2, rs.getInt("door_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " doors world name records");
            }
            // farming
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "farming SET aquarium = ?, birdcage = ?, farm = ?, hutch = ?, igloo = ?, stable = ?, stall = ?, village = ? WHERE farm_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT * FROM " + prefix + "farming";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                boolean has = false;
                List<String> columns = Arrays.asList("farm", "aquarium", "birdcage", "hutch", "igloo", "stable", "stall", "village");
                for (String f : columns) {
                    if (rs.getString(f).contains("tardis_time_vortex")) {
                        has = true;
                        break;
                    }
                }
                if (has) {
                    ps.setString(1, rs.getString("aquarium").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(2, rs.getString("birdcage").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(3, rs.getString("farm").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(4, rs.getString("hutch").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(5, rs.getString("igloo").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(6, rs.getString("stable").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(7, rs.getString("stall").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(8, rs.getString("village").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(9, rs.getInt("farm_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " farming world name records");
            }
            // gravity_well
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "gravity_well SET location = ? WHERE g_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT g_id, location FROM " + prefix + "gravity_well";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("location").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("location").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(2, rs.getInt("g_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " gravity_well world name records");
            }
            // lamps
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "lamps SET location = ? WHERE l_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT l_id, location FROM " + prefix + "lamps";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("location").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("location").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(2, rs.getInt("l_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " lamps world name records");
            }
            // portals
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "portals SET portal = ?, teleport = ? WHERE portal_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT portal_id, portal, teleport FROM " + prefix + "portals";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("portal").contains("tardis_time_vortex") || rs.getString("teleport").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("portal").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setString(2, rs.getString("teleport").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(3, rs.getInt("portal_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " portals world name records");
            }
            // transmats
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "transmats SET world = ? WHERE transmat_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT transmat_id, world FROM " + prefix + "transmats";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("world").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("world").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(2, rs.getInt("transmat_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " transmats world name records");
            }
            // vaults
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "vaults SET location = ? WHERE v_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT v_id, location FROM " + prefix + "vaults";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("location").contains("tardis_time_vortex")) {
                    ps.setString(1, rs.getString("location").replace("tardis_time_vortex", "TARDIS_TimeVortex"));
                    ps.setInt(2, rs.getInt("v_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " lamps world name records");
            }
            // home, saves etc
            // back
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "back SET world = ? WHERE back_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT back_id, world FROM " + prefix + "back";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("world").contains("gallifrey") || rs.getString("world").contains("siluria") || rs.getString("world").contains("skaro")) {
                    ps.setString(1, rs.getString("world").replace("gallifrey", "Gallifrey").replace("siluria", "Siluria").replace("skaro", "Skaro"));
                    ps.setInt(2, rs.getInt("back_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " back world name records");
            }
            // current
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "current SET world = ? WHERE current_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT current_id, world FROM " + prefix + "current";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("world").contains("gallifrey") || rs.getString("world").contains("siluria") || rs.getString("world").contains("skaro")) {
                    ps.setString(1, rs.getString("world").replace("gallifrey", "Gallifrey").replace("siluria", "Siluria").replace("skaro", "Skaro"));
                    ps.setInt(2, rs.getInt("current_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " current world name records");
            }
            // dispersed
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "dispersed SET world = ? WHERE d_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT d_id, world FROM " + prefix + "dispersed";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("world").contains("gallifrey") || rs.getString("world").contains("siluria") || rs.getString("world").contains("skaro")) {
                    ps.setString(1, rs.getString("world").replace("gallifrey", "Gallifrey").replace("siluria", "Siluria").replace("skaro", "Skaro"));
                    ps.setInt(2, rs.getInt("d_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " dispersed world name records");
            }
            // homes
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "homes SET world = ? WHERE home_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT home_id, world FROM " + prefix + "homes";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("world").contains("gallifrey") || rs.getString("world").contains("siluria") || rs.getString("world").contains("skaro")) {
                    ps.setString(1, rs.getString("world").replace("gallifrey", "Gallifrey").replace("siluria", "Siluria").replace("skaro", "Skaro"));
                    ps.setInt(2, rs.getInt("home_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " homes world name records");
            }
            // next
            connection.setAutoCommit(false);
            query = "UPDATE " + prefix + "next SET world = ? WHERE next_id = ?";
            ps = connection.prepareStatement(query);
            select = "SELECT next_id, world FROM " + prefix + "next";
            rs = statement.executeQuery(select);
            i = 0;
            while (rs.next()) {
                if (rs.getString("world").contains("gallifrey") || rs.getString("world").contains("siluria") || rs.getString("world").contains("skaro")) {
                    ps.setString(1, rs.getString("world").replace("gallifrey", "Gallifrey").replace("siluria", "Siluria").replace("skaro", "Skaro"));
                    ps.setInt(2, rs.getInt("next_id"));
                    ps.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " next world name records");
            }
        } catch (SQLException e) {
            plugin.debug("Update error for lowercase world name update! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (rs != null) {
                    rs.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing lowercase world name update associated tables! " + e.getMessage());
            }
        }
    }
}
