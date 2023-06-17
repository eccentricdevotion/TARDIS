package me.eccentric_nz.TARDIS.database.converters;

import java.io.File;
import java.sql.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
            int i = 0;
            // transfer data
            PreparedStatement statement = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String queryBeacon = "SELECT * FROM " + vm_prefix + "beacons";
            String insertBeacon = "INSERT INTO " + prefix + "beacons (`uuid`, `location`, `block_data`) VALUES (?, ?, ?)";
            String querySave = "SELECT * FROM " + vm_prefix + "saves";
            String insertSave = "INSERT INTO " + prefix + "saves (`uuid`, `save_name`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            String queryManipulator = "SELECT * FROM " + vm_prefix + "manipulator";
            String insertManipulator = "INSERT INTO " + prefix + "manipulator (`uuid`, `tachyon_level`) VALUES (?, ?)";
            String queryMessage = "SELECT * FROM " + vm_prefix + "messages";
            String insertMessage = "INSERT INTO " + prefix + "messages (`uuid_to`, `uuid_from`, `message`, `date`, `read`) VALUES (?, ?, ?, ?, ?)";
            try {
                connection.setAutoCommit(false);
                statement = vm.prepareStatement(queryBeacon);
                ps = connection.prepareStatement(insertBeacon);
                rs = statement.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        ps.setString(1, rs.getString("uuid"));
                        ps.setString(2, rs.getString("location"));
                        ps.setString(3, rs.getString("block_type"));
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Transferred " + i + " vortex beacons to database");
                    i = 0;
                }
                statement = vm.prepareStatement(querySave);
                ps = connection.prepareStatement(insertSave);
                rs = statement.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        ps.setString(1, rs.getString("uuid"));
                        ps.setString(2, rs.getString("save_name"));
                        ps.setString(3, rs.getString("world"));
                        ps.setInt(4, rs.getInt("x"));
                        ps.setInt(5, rs.getInt("y"));
                        ps.setInt(6, rs.getInt("z"));
                        ps.setFloat(7, rs.getFloat("yaw"));
                        ps.setFloat(8, rs.getFloat("pitch"));
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Transferred " + i + " vortex saves to database");
                    i = 0;
                }
                statement = vm.prepareStatement(queryManipulator);
                ps = connection.prepareStatement(insertManipulator);
                rs = statement.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        ps.setString(1, rs.getString("uuid"));
                        ps.setInt(2, rs.getInt("tachyon_level"));
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Transferred " + i + " vortex manipulators to database");
                    i = 0;
                }
                statement = vm.prepareStatement(queryMessage);
                ps = connection.prepareStatement(insertMessage);
                rs = statement.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        ps.setString(1, rs.getString("uuid_to"));
                        ps.setString(2, rs.getString("uuid_from"));
                        ps.setString(3, rs.getString("message"));
                        ps.setLong(4, rs.getLong("date"));
                        ps.setInt(5, rs.getInt("read"));
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    connection.commit();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Transferred " + i + " vortex messages to database");
                }
                return true;
            } catch (SQLException e) {
                plugin.debug("Transfer error for beacons table! " + e.getMessage());
                return false;
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (statement != null) {
                        statement.close();
                    }
                    // reset auto commit
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    plugin.debug("Error closing vortex manipulator transfer! " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
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
        if (!vm_prefix.equals("NOT_FOUND")) {
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
        }
        return mysql;
    }

    public Connection getSQLiteConnection() {
        Connection sqlite = null;
        String path = basePath + "TVM.db";
        File file = new File(path);
        if (file.exists()) {
            try {
                Class.forName("org.sqlite.JDBC");
                sqlite = DriverManager.getConnection("jdbc:sqlite:" + path);
                sqlite.setAutoCommit(true);
            } catch (ClassNotFoundException | SQLException e) {
                plugin.debug("Cannot connect the Vortex Manipulator database! " + e.getMessage());
            }
        }
        return sqlite;
    }

    /**
     * Get Vortex Manipulator config options from
     * plugins/TARDISVortexManipulator/config.yml
     */
    private String getVMPrefix() {
        File file = new File(basePath + "config.yml");
        if (file.exists()) {
            // get the TARDISVortexManipulator config
            configuration = YamlConfiguration.loadConfiguration(file);
            isMySQL = configuration.getString("storage.database").equals("mysql");
            return (isMySQL) ? configuration.getString("storage.mysql.prefix") : "";
        } else {
            return "NOT_FOUND";
        }
    }
}
