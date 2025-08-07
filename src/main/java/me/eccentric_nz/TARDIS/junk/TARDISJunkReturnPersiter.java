package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TARDISJunkReturnPersiter {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public TARDISJunkReturnPersiter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public long getJunkTime() {
        long time = System.currentTimeMillis();
        try {
            ps = connection.prepareStatement("SELECT lastuse FROM " + prefix + "tardis WHERE uuid = '00000000-aaaa-bbbb-cccc-000000000000'");
            rs = ps.executeQuery();
            time = rs.getLong("lastuse");
        } catch (SQLException e) {
            plugin.debug("Resultset error for junk return persistence: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing junk return statement: " + e.getMessage());
            }
        }
        return time;
    }

    public void setJunkTime() {
        try {
            ps = connection.prepareStatement("UPDATE " + prefix + "tardis SET lastuse =  ? WHERE uuid = '00000000-aaaa-bbbb-cccc-000000000000'");
            ps.setLong(1, System.currentTimeMillis());
            ps.executeQuery();
        } catch (SQLException e) {
            plugin.debug("Update error for junk return persistence: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing junk return statement: " + e.getMessage());
            }
        }
    }
}
