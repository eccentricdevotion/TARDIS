package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearInteractions {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public ClearInteractions(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void removeRecords(int id) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM " + prefix + "interactions WHERE tardis_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Error clearing interactions [" + id + "] table: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error clearing interactions [" + id + "] statement: " + ex.getMessage());
            }
        }
    }
}
