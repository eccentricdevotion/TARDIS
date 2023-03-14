package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.*;

public class TARDISVortexManipulatorTransfer {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String basePath;
    private final TARDIS plugin;
    private final String prefix;
    private final String vm_prefix;
    private boolean isMySQL;
    private FileConfiguration configuration;

    public TARDISVortexManipulatorTransfer(TARDIS plugin) {
        this.plugin = plugin;
        basePath = this.plugin.getServer().getWorldContainer() + File.separator + "plugins" + File.separator + "TARDISVortexManipulator" + File.separator;
        prefix = this.plugin.getPrefix();
        vm_prefix = getVMPrefix();
    }

    public boolean transferData() {
        Connection vm = getVMConnection();
        if (vm != null) {
            // transfer data
            PreparedStatement statement = null;
            ResultSet rs = null;
            String query = "SELECT * FROM " + vm_prefix + "beacons";
            try {
                statement = vm.prepareStatement(query);
                rs = statement.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        String l = rs.getString("location");
                        String blockData = rs.getString("block_type");

                    }
                } else {
                    return false;
                }
            } catch (SQLException e) {
                plugin.debug("Block error for beacons table! " + e.getMessage());
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
                    plugin.debug("Error closing beacons table! " + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Get Vortex Manipulator database connection
     */
    private Connection getVMConnection() {
        if (isMySQL) {
            return getMySQLConnection();
        } else {
            return getSQLiteConnection();
        }
    }

    public Connection getMySQLConnection() {
        Connection mysql = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbc = "jdbc:mysql://" + configuration.getString("storage.mysql.host") + ":" + configuration.getString("storage.mysql.port") + "/" + configuration.getString("storage.mysql.database") + "?autoReconnect=true";
            if (!configuration.getBoolean("storage.mysql.useSSL")) {
                jdbc += "&useSSL=false";
            }
            String user = configuration.getString("storage.mysql.user");
            String pass = configuration.getString("storage.mysql.password");
            mysql = DriverManager.getConnection(jdbc, user, pass);
            mysql.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            plugin.debug("Cannot connect the Vortex Manipulator database! " + e.getMessage());
        }
        return mysql;
    }

    public Connection getSQLiteConnection() {
        Connection sqlite = null;
        try {
            String path = basePath + "TVM.db";
            Class.forName("org.sqlite.JDBC");
            sqlite = DriverManager.getConnection("jdbc:sqlite:" + path);
            sqlite.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            plugin.debug("Cannot connect the Vortex Manipulator database! " + e.getMessage());
        }
        return sqlite;
    }

    /**
     * Get Vortex Manipulator config options from plugins/TARDISVortexManipulator/config.yml
     */
    private String getVMPrefix() {
        // get the TARDISVortexManipulator config
        configuration = YamlConfiguration.loadConfiguration(new File(basePath + "config.yml"));
        isMySQL = configuration.getString("storage.database").equals("mysql");
        return (isMySQL) ? configuration.getString("storage.mysql.prefix") : "";
    }
}
