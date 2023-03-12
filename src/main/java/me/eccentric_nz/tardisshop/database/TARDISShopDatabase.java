package me.eccentric_nz.tardisshop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TARDISShopDatabase {

    private static final TARDISShopDatabase instance = new TARDISShopDatabase();
    public Connection connection = null;
    public Statement statement = null;

    public static synchronized TARDISShopDatabase getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(String path) throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }

    public void createTables() {
        try {
            statement = connection.createStatement();
            // Table structure for table 'items'
            final String queryItems = "CREATE TABLE IF NOT EXISTS items (item_id INTEGER PRIMARY KEY NOT NULL, item TEXT DEFAULT '', location TEXT DEFAULT '', cost REAL DEFAULT 0)";
            statement.executeUpdate(queryItems);
        } catch (SQLException e) {
            System.err.println("[TARDISShop] Error creating tables: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.err.println("[TARDISShop] Error closing SQL statement: " + e.getMessage());
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}
