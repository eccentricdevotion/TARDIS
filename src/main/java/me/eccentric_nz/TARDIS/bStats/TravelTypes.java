package me.eccentric_nz.TARDIS.bStats;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class TravelTypes {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the chameleon table.
     *
     * @param plugin an instance of the main class.
     */
    TravelTypes(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the travel_stats table and maps travel types to the number of times they are used.
     *
     * @return a map of travel types and count values
     */
    public HashMap<String, Integer> getMap() {
        HashMap<String, Integer> data = new HashMap<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT travel_type, count(travel_type) AS count_of FROM " + prefix + "travel_stats GROUP BY travel_type";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    data.put(rs.getString("travel_type"), rs.getInt("count_of"));
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for travel_stats table getting travel types! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing travel_stats table for travel types! " + e.getMessage());
            }
        }
        return data;
    }
}
