package me.eccentric_nz.TARDIS.database.converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.ARS.ARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.sql.*;

public class HellBentUpdater {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public HellBentUpdater(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean convert() {
        Statement statement = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String hellBentQuery = "SELECT "
                    + prefix + "tardis.tardis_id, " + prefix + "tardis.size, "
                    + prefix + "ars.ars_id, " + prefix + "ars.json FROM "
                    + prefix + "tardis, " + prefix + "ars WHERE "
                    + prefix + "tardis.size = 'HELL_BENT' AND "
                    + prefix + "tardis.tardis_id = " + prefix + "ars.tardis_id";
            String hellBentUpdate = "UPDATE " + prefix + "ars SET json = ? WHERE ars_id = ?";
            ps = connection.prepareStatement(hellBentUpdate);
            rs = statement.executeQuery(hellBentQuery);
            if (rs.isBeforeFirst()) {
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                while (rs.next()) {
                    String[][][] data = ARSMethods.getGridFromJSON(rs.getString("json"));
                    if (data[1][4][4].equals("WHITE_GLAZED_TERRACOTTA")) {
                        data[1][4][4] = "CINNABAR";
                        JsonArray json = JsonParser.parseString(gson.toJson(data)).getAsJsonArray();
                        ps.setString(1, json.toString());
                        ps.setInt(2, rs.getInt("ars_id"));
                        ps.addBatch();
                        i++;
                    }
                }
            }
            if (i > 0) {
                ps.executeBatch();
                connection.commit();
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " Hell Bent ARS records");
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            plugin.debug("Conversion error for Hell Bent ARS! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing Hell Bent ARS! " + e.getMessage());
            }
        }
        return true;
    }
}
