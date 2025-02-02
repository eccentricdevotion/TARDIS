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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
class TARDISPortalCommand {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private Statement statement = null;

    TARDISPortalCommand(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    boolean clearAll(CommandSender sender) {
        // clear all portals on the server
        plugin.getTrackerKeeper().getPortals().clear();
        // stop tracking players
        plugin.getTrackerKeeper().getMovers().clear();
        // clear the portals table
        try {
            statement = connection.createStatement();
            String query = "DELETE FROM " + prefix + "portals";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Error deleting from portals table! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing portals table! " + e.getMessage());
            }
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "PURGE_PORTAL");
        return true;
    }
}
