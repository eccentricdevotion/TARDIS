package me.eccentric_nz.tardis.database.resultset;

import me.eccentric_nz.tardis.TardisBuilderInstanceKeeper;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetTips {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final String prefix;

    public ResultSetTips(TardisPlugin plugin) {
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
                    TardisBuilderInstanceKeeper.getTipsSlots().add(slot);
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