package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISystemTree;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SystemUpgradeUpdate {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public SystemUpgradeUpdate(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    public void set(String uuid, int id, GUISystemTree clicked) {
        PreparedStatement ps = null;
        String query = "UPDATE " + prefix + "system_upgrades SET " + clicked.getDatabaseName() + " = 1 WHERE uuid = ? AND tardis_id = ?";
        try {
            service.testConnection(connection);
            ps = connection.prepareStatement(query);
            int s = 1;
            ps.setString(1, uuid);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for system upgrades table! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing system upgrades table! " + e.getMessage());
            }
        }
    }
}
