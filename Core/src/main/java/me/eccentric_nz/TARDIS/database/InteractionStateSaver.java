package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InteractionStateSaver {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public InteractionStateSaver(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void write(String which, int state, int id) {
        PreparedStatement statement = null;
        String query = "UPDATE interactions SET state = ? WHERE tardis_id = ? AND control = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, state);
            statement.setInt(2, id);
            statement.setString(3, which);
            statement.executeUpdate();
        }  catch (SQLException e) {
            plugin.debug("Update error for interactions state! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing interactions state! " + e.getMessage());
            }
        }
    }
}
