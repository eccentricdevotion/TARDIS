package me.eccentric_nz.tardis.bstats;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class CondenserCounts {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the chameleon table.
     *
     * @param plugin an instance of the main class.
     */
    CondenserCounts(TardisPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the TARDIS table and counts the number of records.
     *
     * @return the number of records
     */
    public HashMap<String, Integer> getCounts() {
        HashMap<String, Integer> data = new HashMap<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT block_data, block_count FROM " + prefix + "condenser";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        String block = rs.getString("block_data");
                        int amount = rs.getInt("block_count");
                        int count = (data.containsKey(block)) ? data.get(block) + amount : amount;
                        data.put(block, count);
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for condenser getting block counts! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing condenser table getting block counts! " + e.getMessage());
            }
        }
        return data;
    }
}