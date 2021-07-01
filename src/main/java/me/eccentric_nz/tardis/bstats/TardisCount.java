package me.eccentric_nz.tardis.bstats;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TardisCount {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the chameleon table.
     *
     * @param plugin an instance of the main class.
     */
    TardisCount(TardisPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the TARDIS table and counts the number of records.
     *
     * @return the number of records
     */
    public int getCount(int abandoned) {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(*) as count FROM " + prefix + "tardis WHERE abandoned = " + abandoned;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                rs.next();
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis getting count! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis table for counts! " + e.getMessage());
            }
        }
        return 0;
    }
}