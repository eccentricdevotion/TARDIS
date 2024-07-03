package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.particles.Sphere;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.*;
import java.util.UUID;

public class EyeStarter {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private Statement statement = null;
    private PreparedStatement ps = null;

    public EyeStarter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void goSuperNova() {
        ResultSet rs = null;
        try {
            String update = "UPDATE " + prefix + "eyes SET task = ? WHERE tardis_id = ?";
            ps = connection.prepareStatement(update);
            String query = "SELECT " + prefix + "controls.location, " + prefix + "eyes.capacitors, " + prefix + "tardis.uuid, " + prefix + "tardis.tardis_id " +
                    "FROM " + prefix + "controls, " + prefix + "eyes, " + prefix + "tardis " +
                    "WHERE " + prefix + "controls.type = 53 " +
                    "AND " + prefix + "controls.tardis_id = " + prefix + "eyes.tardis_id " +
                    "AND " + prefix + "eyes.tardis_id = " + prefix + "tardis.tardis_id";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                connection.setAutoCommit(false);
                while (rs.next()) {
                    Location s = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location"));
                    Capacitor capacitor = Capacitor.values()[rs.getInt("capacitors") - 1];
                    Sphere sphere = new Sphere(plugin, UUID.fromString(rs.getString("uuid")), s, capacitor);
                    int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sphere, 0, 10);
                    sphere.setTaskID(task);
                    ps.setInt(1, task);
                    ps.setInt(2, rs.getInt("tardis_id"));
                    ps.addBatch();
                }
                ps.executeBatch();
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            plugin.debug("Error loading eye of harmony stars: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing eye of harmony stars: " + ex.getMessage());
            }
        }
    }
}
