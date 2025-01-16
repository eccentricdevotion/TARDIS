package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearTable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public ClearTable(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void removeRecords(String table) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM " + prefix + table);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Error clearing " + table + " table: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error clearing " + table + " statement: " + ex.getMessage());
            }
        }
    }
}
