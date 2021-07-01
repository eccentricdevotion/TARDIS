package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetTIPS {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public ResultSetTIPS(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Make a list of the currently used TIPS slots.
     */
    public void fillUsedSlotList() {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT tips FROM " + prefix + "tardis";
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int slot = rs.getInt("tips");
                    TARDISBuilderInstanceKeeper.getTipsSlots().add(slot);
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table (getting TIPS slots)! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis table (getting TIPS slots)! " + e.getMessage());
            }
        }
    }
}
