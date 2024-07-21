package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearEyeControls {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public ClearEyeControls(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void removeRecords(int id) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM " + prefix + "controls WHERE tardis_id = ? AND `type` IN (53, 54)");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps = connection.prepareStatement("UPDATE " + prefix + "eyes SET capacitors = 1, damaged = 0 WHERE tardis_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Error clearing eye controls [" + id + "] table: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error clearing eye controls [" + id + "] statement: " + ex.getMessage());
            }
        }
    }
}
