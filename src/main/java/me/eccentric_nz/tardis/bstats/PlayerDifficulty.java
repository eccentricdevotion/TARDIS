package me.eccentric_nz.tardis.bstats;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class PlayerDifficulty {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the chameleon table.
     *
     * @param plugin an instance of the main class.
     */
    PlayerDifficulty(TardisPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the TARDIS table and maps a player's difficulty preference to the number of times they are used.
     *
     * @return a map of chameleon keys and count values
     */
    public HashMap<String, Integer> getModes() {
        HashMap<String, Integer> data = new HashMap<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT difficulty, count(difficulty) AS count_of FROM " + prefix + "player_prefs GROUP BY difficulty";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    data.put(rs.getInt("difficulty") == 0 ? "hard" : "easy", rs.getInt("count_of"));
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for player_prefs table getting player difficulty! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing player_prefs table for player difficulty! " + e.getMessage());
            }
        }
        return data;
    }
}