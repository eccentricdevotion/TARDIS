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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import static me.eccentric_nz.TARDIS.files.TARDISConfigConverter.copyFile;
import me.eccentric_nz.TARDIS.utility.TARDISUUIDFetcher;
import org.bukkit.ChatColor;

/**
 *
 * @author eccentric_nz
 */
public class TARDISUUIDConverter {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final List<String> timelords = new ArrayList<String>();

    public TARDISUUIDConverter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean convert() {
        // get if server is online-mode=true (as required to retrieve correct player UUIDs)
        if (!getOnlineMode()) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + "UUID conversion requires the server online-mode to be TRUE!");
            return false;
        }
        // backup database
        plugin.getConsole().sendMessage(plugin.getPluginName() + "Backing up TARDIS database...");
        File oldFile = new File(plugin.getDataFolder() + File.separator + "TARDIS.db");
        File newFile = new File(plugin.getDataFolder() + File.separator + "TARDIS_" + System.currentTimeMillis() + ".db");
        try {
            copyFile(oldFile, newFile);
        } catch (IOException ex) {
            plugin.debug("Could not backup TARDIS.db! " + ex.getMessage());
            return false;
        }
        // get all TARDIS owners from database
        Statement statement = null;
        PreparedStatement ps_ach = null;
        PreparedStatement ps_ars = null;
        PreparedStatement ps_pla = null;
        PreparedStatement ps_sto = null;
        PreparedStatement ps_tag = null;
        PreparedStatement ps_tco = null;
        PreparedStatement ps_tar = null;
        PreparedStatement ps_tra = null;
        ResultSet rs = null;
        String query = "SELECT owner FROM tardis";
        String achievements_update = "UPDATE achievements SET uuid = ? WHERE player = ?";
        String ars_update = "UPDATE ars SET uuid = ? WHERE player = ?";
        String playerprefs_update = "UPDATE player_prefs SET uuid = ? WHERE player = ?";
        String storage_update = "UPDATE storage SET uuid = ? WHERE owner = ?";
        String tag_update = "UPDATE tag SET uuid = ? WHERE player = ?";
        String tcount_update = "UPDATE t_count SET uuid = ? WHERE player = ?";
        String tardis_update = "UPDATE tardis SET uuid = ? WHERE owner = ?";
        String travellers_update = "UPDATE travellers SET uuid = ? WHERE player = ?";
        int count = 0;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    timelords.add(rs.getString("owner"));
                }
                TARDISUUIDFetcher fetcher = new TARDISUUIDFetcher(timelords);
                // get UUIDs
                Map<String, UUID> response = null;
                try {
                    response = fetcher.call();
                } catch (Exception e) {
                    plugin.debug("Exception while running TARDISUUIDFetcher: " + e.getMessage());
                    return false;
                }
                if (response != null) {
                    // update all TARDIS owners to UUIDs
                    ps_ach = connection.prepareStatement(achievements_update);
                    ps_ars = connection.prepareStatement(ars_update);
                    ps_sto = connection.prepareStatement(playerprefs_update);
                    ps_pla = connection.prepareStatement(storage_update);
                    ps_tag = connection.prepareStatement(tag_update);
                    ps_tco = connection.prepareStatement(tcount_update);
                    ps_tar = connection.prepareStatement(tardis_update);
                    ps_tra = connection.prepareStatement(travellers_update);
                    for (Map.Entry<String, UUID> map : response.entrySet()) {
                        ps_ach.setString(1, map.getValue().toString());
                        ps_ach.setString(2, map.getKey());
                        count += ps_ach.executeUpdate();
                        ps_ars.setString(1, map.getValue().toString());
                        ps_ars.setString(2, map.getKey());
                        count += ps_ars.executeUpdate();
                        ps_pla.setString(1, map.getValue().toString());
                        ps_pla.setString(2, map.getKey());
                        count += ps_pla.executeUpdate();
                        ps_sto.setString(1, map.getValue().toString());
                        ps_sto.setString(2, map.getKey());
                        count += ps_sto.executeUpdate();
                        ps_tag.setString(1, map.getValue().toString());
                        ps_tag.setString(2, map.getKey());
                        count += ps_tag.executeUpdate();
                        ps_tco.setString(1, map.getValue().toString());
                        ps_tco.setString(2, map.getKey());
                        count += ps_tco.executeUpdate();
                        ps_tar.setString(1, map.getValue().toString());
                        ps_tar.setString(2, map.getKey());
                        count += ps_tar.executeUpdate();
                        ps_tra.setString(1, map.getValue().toString());
                        ps_tra.setString(2, map.getKey());
                        count += ps_tra.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for UUID updating! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (ps_ach != null) {
                    ps_ach.close();
                }
                if (ps_ars != null) {
                    ps_ars.close();
                }
                if (ps_pla != null) {
                    ps_pla.close();
                }
                if (ps_sto != null) {
                    ps_sto.close();
                }
                if (ps_tag != null) {
                    ps_tag.close();
                }
                if (ps_tco != null) {
                    ps_tco.close();
                }
                if (ps_tar != null) {
                    ps_tar.close();
                }
                if (ps_tra != null) {
                    ps_tra.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + count + " Time Lord names to UUIDs.");
        return true;
    }

    /**
     * Gets the server default resource pack. Will use the Minecraft default
     * pack if none is specified. Until Minecraft/Bukkit lets us set the RP back
     * to Default, we'll have to host it on DropBox
     *
     * @return The server specified texture pack.
     */
    public boolean getOnlineMode() {
        FileInputStream in = null;
        try {
            Properties properties = new Properties();
            String path = "server.properties";
            in = new FileInputStream(path);
            properties.load(in);
            String online = properties.getProperty("online-mode");
            return (online != null && online.equalsIgnoreCase("true"));
        } catch (FileNotFoundException ex) {
            plugin.debug("Could not find server.properties!");
            return false;
        } catch (IOException ex) {
            plugin.debug("Could not read server.properties!");
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                plugin.debug("Could not close server.properties!");
            }
        }
    }
}
