package me.eccentric_nz.TARDIS.database.converters;

import java.io.File;
import java.sql.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

public class TARDISShopTransfer {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    ;
    private final TARDIS plugin;
    private final String prefix;

    public TARDISShopTransfer(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean transferData() {
        Connection shop = getSQLiteConnection();
        if (shop != null) {
            int i = 0;
            // transfer data
            PreparedStatement statement = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String queryItem = "SELECT * FROM items";
            String insertItem = "INSERT INTO " + prefix + "items (`item`, `location`, `cost`) VALUES (?, ?, ?)";
            try {
                connection.setAutoCommit(false);
                statement = shop.prepareStatement(queryItem);
                ps = connection.prepareStatement(insertItem);
                rs = statement.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        ps.setString(1, rs.getString("item"));
                        ps.setString(2, rs.getString("location"));
                        ps.setFloat(3, rs.getFloat("cost"));
                        ps.addBatch();
                        i++;
                    }
                }
                if (i > 0) {
                    ps.executeBatch();
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Transferred " + i + " shop items to database");
                }
                connection.commit();
                return true;
            } catch (SQLException e) {
                plugin.debug("Transfer error for items table! " + e.getMessage());
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
                    plugin.debug("Error closing items transfer! " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    public Connection getSQLiteConnection() {
        Connection sqlite = null;
        String path = plugin.getServer().getWorldContainer() + File.separator + "plugins" + File.separator + "TARDISShop" + File.separator + "TARDISShop.db";
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
}
