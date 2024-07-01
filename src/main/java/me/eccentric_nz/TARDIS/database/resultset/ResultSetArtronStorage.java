package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetArtronStorage {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int capacitors = 0;
    private int damaged = 0;
    private int task = -1;

    public ResultSetArtronStorage(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean fromID(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "eyes WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                capacitors = rs.getInt("capacitors");
                damaged = rs.getInt("damaged");
                task = rs.getInt("task");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for artron storage (eyes table)! " + e.getMessage());
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
                plugin.debug("Error closing artron storage (eyes table)! " + e.getMessage());
            }
        }
        return true;
    }

    public int getCapacitorCount() {
        return capacitors;
    }

    public int getDamageCount() {
        return damaged;
    }

    public int getTask() {
        return task;
    }
}
