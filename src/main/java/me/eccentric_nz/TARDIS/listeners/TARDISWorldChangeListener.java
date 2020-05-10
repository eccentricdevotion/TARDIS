/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

/**
 * @author Technoguyfication
 */
public class TARDISWorldChangeListener implements Listener {
    private final TARDIS plugin;
    private final TARDISDatabaseConnection service;

    public TARDISWorldChangeListener(TARDIS plugin) {
        this.plugin = plugin;
        this.service = TARDISDatabaseConnection.getINSTANCE();
    }

    // lowest priority because we aren't affecting the world change
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        handleWorld(event.getPlayer());
    }

    // handle player joins too
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        handleWorld(event.getPlayer());
    }

    // add that the player has been to this world to the database
    private void handleWorld(Player p) {
        Environment env = p.getWorld().getEnvironment();
        String envName = env.toString().toLowerCase();

        // set up database connection
        ResultSet rs = null;
        PreparedStatement statement = null;
        try {
            Connection connection = service.getConnection();
            service.testConnection(connection);
            String prefix = this.plugin.getPrefix();

            // get their current entry from database
            String getQuery = "SELECT * FROM " + prefix + "traveled_to WHERE uuid = ?";
            PreparedStatement query = connection.prepareStatement(getQuery);
            query.setString(1, p.getUniqueId().toString());
            rs = query.executeQuery();

            // update entry if it exists, otherwise create a new one
            if (rs.next()) {
                // check if the player has visited this world before and update if needed
                if (rs.getInt(envName) != 1) {
                    String updateQuery = "UPDATE " + prefix + "traveled_to SET " + envName + " = 1 WHERE uuid = ?;";
                    statement = connection.prepareStatement(updateQuery);
                    statement.setString(1, p.getUniqueId().toString());
                    statement.executeUpdate();
                    statement.close();
                }
            } else {
                // they have no data, so just set their current world as visited
                String insertQuery = "INSERT INTO " + prefix + "traveled_to (uuid, " + envName + ") VALUES (?, 1);";
                statement = connection.prepareStatement(insertQuery);
                statement.setString(1, p.getUniqueId().toString());
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet or Statement error for traveled_to table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing traveled_to table! " + e.getMessage());
            }
        }
    }
}