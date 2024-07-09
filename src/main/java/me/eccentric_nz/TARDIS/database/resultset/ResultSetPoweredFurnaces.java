package me.eccentric_nz.TARDIS.database.resultset;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ResultSetPoweredFurnaces {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private final List<Pair<String, Integer>> data = new ArrayList<>();

    public ResultSetPoweredFurnaces(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void fetchAsync(final FurnaceCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean hasResult = resultSet();
            // go back to the tick loop
            Bukkit.getScheduler().runTask(plugin, () -> {
                // call the callback with the result
                callback.onDone(hasResult, this);
            });
        });
    }

    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "artron_powered";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    data.add(new Pair<>(rs.getString("location"), rs.getInt("tardis_id")));
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for artron_powered table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing artron_powered table! " + e.getMessage());
            }
        }
        return true;
    }

    public List<Pair<String, Integer>> getData() {
        return data;
    }

    public interface FurnaceCallback {

        void onDone(boolean hasResult, ResultSetPoweredFurnaces furnaces);
    }
}
