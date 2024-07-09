package me.eccentric_nz.TARDIS.database.resultset;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetArtronPowered {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public ResultSetArtronPowered(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }


    public Pair<Boolean, Integer> fromLocation(String location) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT location FROM " + prefix + "artron_powered WHERE location = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, location);
            rs = statement.executeQuery();

            if (rs.isBeforeFirst()) {
                rs.next();
                return new Pair<>(true, rs.getInt("tardis_id"));
            }
            return new Pair<>(false, -1);
        } catch (SQLException e) {
            plugin.debug("ResultSet error for artron_powered table! " + e.getMessage());
            return new Pair<>(false, -1);
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
    }
}
