/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.destroyers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.command.CommandSender;

/**
 * Reesha was a rosebush-like plant who needed to keep herself pruned to prevent
 * her consciousness from fading and dying. She was invited by the Eighth Doctor
 * to the founding of the Institute of Time.
 *
 * @author eccentric_nz
 */
public class TARDISPruner {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;

    public TARDISPruner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list(CommandSender sender, int days) {
        long millis = getTime(days);
        Timestamp prune = getTimestamp(millis);
        String query = "SELECT * FROM tardis WHERE lastuse < " + millis;
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            String file = plugin.getDataFolder() + File.separator + "TARDIS_Prune_List.txt";
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                if (rs.isBeforeFirst()) {
                    sender.sendMessage(plugin.pluginName + "Prune List:");
                }
                while (rs.next()) {
                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                    wherecl.put("tardis_id", rs.getInt("tardis_id"));
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (rsc.resultSet()) {

                        // double check that this is an unused TARDIS
                        Timestamp lastuse = new Timestamp(rs.getLong("lastuse"));
                        if (lastuse.before(prune)) {
                            String line = "Timelord: " + rs.getString("owner") + ", Location: " + rsc.getWorld().getName() + ":" + rsc.getX() + ":" + rsc.getY() + ":" + rsc.getZ();
                            // write line to file
                            bw.write(line);
                            bw.newLine();
                            // display the TARDIS prune list
                            sender.sendMessage(line);
                        }
                    } else {
                        plugin.debug("Could not get current TARDIS location!");
                    }
                }
                bw.close();
            } catch (IOException e) {
                plugin.debug("Could not create and write to TARDIS_Prune_List.txt! " + e.getMessage());
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error trying to display prune list! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug(e.getMessage());
            }
        }
    }

    public void prune(CommandSender sender, int days) {
        long millis = getTime(days);
        Timestamp prune = getTimestamp(millis);
        String query = "SELECT * FROM tardis WHERE lastuse < " + millis;
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            TARDISExterminator te = new TARDISExterminator(plugin);
            while (rs.next()) {
                // double check that this is an unused TARDIS
                Timestamp lastuse = new Timestamp(rs.getLong("lastuse"));
                if (lastuse.before(prune)) {
                    // remove the TARDIS
                    if (te.exterminate(rs.getInt("tardis_id"))) {
                        sender.sendMessage("Pruned " + rs.getString("owner") + "'s TARDIS");
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error trying to prune! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug(e.getMessage());
            }
        }
    }

    private long getTime(int day) {
        long period = (long) (day * 86400000L);
        long now = System.currentTimeMillis();
        long prune = now - period;
        return prune;
    }

    private Timestamp getTimestamp(long l) {
        return new Timestamp(l);
    }
}
