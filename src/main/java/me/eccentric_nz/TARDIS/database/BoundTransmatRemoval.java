package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class BoundTransmatRemoval {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final ArrayList<HashMap<String, String>> data;
    private final String prefix;

    public BoundTransmatRemoval(TARDIS plugin, ArrayList<HashMap<String, String>> data) {
        this.plugin = plugin;
        this.data = data;
        prefix = this.plugin.getPrefix();
    }

    public void unbind() {
        PreparedStatement statement = null;
        String query = "DELETE FROM " + prefix + "destinations WHERE dest_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            for (HashMap<String, String> map : data) {
                statement.setInt(1, Integer.parseInt(map.get("dest_id")));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            plugin.debug("Delete error for unbind transmats! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing unbind transmats! " + e.getMessage());
            }
        }
    }
}
