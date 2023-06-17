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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

/**
 * @author eccentric_nz
 */
public class TARDISWorldRemover {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private Statement statement;

    public TARDISWorldRemover(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void cleanWorld(String w) {
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            // blocks
            String blocksQuery = "DELETE FROM " + prefix + "blocks WHERE location LIKE 'Location{world=CraftWorld{name=" + w + "}%'";
            int numBlocks = statement.executeUpdate(blocksQuery);
            if (numBlocks > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Removed " + numBlocks + " block record for non-existent world ->" + w);
            }
            // portals
            String portalsQuery = "DELETE FROM " + prefix + "portals WHERE portal LIKE 'Location{world=CraftWorld{name=" + w + "}%' OR teleport LIKE 'Location{world=CraftWorld{name=" + w + "}%'";
            int numPortals = statement.executeUpdate(portalsQuery);
            if (numPortals > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Removed " + numPortals + " portal record for non-existent world ->" + w);
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for blocks/portals table! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing statement! " + e.getMessage());
            }
        }
    }
}
