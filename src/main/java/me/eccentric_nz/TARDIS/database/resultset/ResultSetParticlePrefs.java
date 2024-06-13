package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.ParticlePrefs;
import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetParticlePrefs {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private ParticlePrefs data;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the player_prefs table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetParticlePrefs(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean fromUUID(String uuid) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "particle_prefs WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                try {
                    ParticleEffect effect = ParticleEffect.valueOf(rs.getString("effect"));
                    ParticleShape shape = ParticleShape.valueOf(rs.getString("shape"));
                    data = new ParticlePrefs(effect, shape, rs.getDouble("speed"), rs.getInt("density"), rs.getBoolean("particles_on"));
                    return true;
                } catch (IllegalArgumentException ignored) {
                }
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for particle prefs fromUUID! " + e.getMessage());
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
                plugin.debug("Error closing particle prefs fromUUID! " + e.getMessage());
            }
        }
    }

    public ParticlePrefs getData() {
        return data;
    }
}
