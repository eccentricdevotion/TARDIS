package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BoundTransmatRemoval {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final String name;
    private final String prefix;

    public BoundTransmatRemoval(TARDIS plugin, int id, String name) {
        this.plugin = plugin;
        this.id = id;
        this.name = name;
        prefix = this.plugin.getPrefix();
    }

    public void unbind() {
        PreparedStatement statement = null;
        String query = "DELETE FROM " + prefix + "bind WHERE tardis_id = ? AND type = 6 AND name = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.executeUpdate();
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
