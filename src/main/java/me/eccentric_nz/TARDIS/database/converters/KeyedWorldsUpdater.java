package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class KeyedWorldsUpdater {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final Set<SQLTable> locationTables = new HashSet<>();

    public KeyedWorldsUpdater(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        locationTables.add(new SQLTable("artron_powered", "a_id"));
        locationTables.add(new SQLTable("bind", "bind_id"));
        locationTables.add(new SQLTable("blocks", "b_id"));
        locationTables.add(new SQLTable("camera", "c_id"));
        locationTables.add(new SQLTable("controls", "c_id"));
        locationTables.add(new SQLTable("flight", "f_id"));
        locationTables.add(new SQLTable("forcefield", "uuid"));
        locationTables.add(new SQLTable("games", "game_id", "player_location"));
        locationTables.add(new SQLTable("gravity_well", "g_id"));
        locationTables.add(new SQLTable("portals", "portal_id", "teleport"));
        locationTables.add(new SQLTable("seeds", "seed_id"));
        locationTables.add(new SQLTable("vaults", "v_id"));
        // vortex manipulator
        locationTables.add(new SQLTable("beacons", "beacon_id"));
        // shop
        locationTables.add(new SQLTable("items", "item_id"));
    }

    public boolean setKeys() {
        Statement statement = null;
        PreparedStatement ps = null;
        try {
            service.testConnection(connection);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // Regex: finds "name=" followed by any character that isn't a closing brace or comma
            Pattern pattern = Pattern.compile("name=([^},]+)");
            // update locations from
            // Location{world=CraftWorld{name=TARDIS_TimeVortex},x=499.0,y=66.0,z=504.0,pitch=0.0,yaw=0.0}
            // to
            // Location{world=CraftWorld{key=minecraft:tardis_timevortex},x=503.0,y=70.0,z=505.0,pitch=0.0,yaw=0.0}
            for (SQLTable entry : locationTables) {
                int i = 0;
                // only records that haven't been converted
                String locationQuery = "SELECT " + entry.id() + ", " + entry.column() + " FROM " + prefix + entry.table() + " WHERE " + entry.column() + " LIKE '%name=%';";
                String locationUpdate = "UPDATE " + prefix + entry.table() + " SET " + entry.column() + " = ? WHERE " + entry.id() + " = ?";
                ps = connection.prepareStatement(locationUpdate);
                connection.setAutoCommit(false);
                // get records
                ResultSet rsl = statement.executeQuery(locationQuery);
                if (rsl.isBeforeFirst()) {
                    while (rsl.next()) {
                        String original = rsl.getString(entry.column());
                        // find the world name, lowercase it, and swap the prefix
                        String updated = pattern.matcher(original).replaceAll(match ->
                                "key=minecraft:" + match.group(1).toLowerCase()
                        );
                        // set the new data
                        ps.setString(1, updated);
                        ps.setInt(2, rsl.getInt(entry.id()));
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Converted " + i + " location records to use world keys.");
                }
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            plugin.debug("Conversion error for non-world-keyed locations! " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // reset auto commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                plugin.debug("Error closing tables (converting non-keyed world names)! " + e.getMessage());
            }
        }
        return true;
    }
}
