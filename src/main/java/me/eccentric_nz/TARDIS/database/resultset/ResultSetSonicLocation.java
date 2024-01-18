package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ResultSetSonicLocation {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final UUID uuid;
    private final String prefix;

    private Location location;

    public ResultSetSonicLocation(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    public boolean resultset() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT last_scanned, scan_type FROM " + prefix + "sonic WHERE sonic_uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                if (rs.getInt("scan_type") == 1) {
                    Player player = plugin.getServer().getPlayer(UUID.fromString(rs.getString("last_scanned")));
                    if (player.isOnline()) {
                        location = player.getLocation();
                    }
                } else {
                    location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("last_scanned"));
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for sonic last scanned! " + e.getMessage());
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
                plugin.debug("Error closing sonic last scanned! " + e.getMessage());
            }
        }
        return true;
    }

    public Location getLocation() {
        return location;
    }
}
