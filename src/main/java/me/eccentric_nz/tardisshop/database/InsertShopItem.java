package me.eccentric_nz.tardisshop.database;

import me.eccentric_nz.tardisshop.TARDISShop;
import me.eccentric_nz.tardisshop.TARDISShopItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertShopItem {

    private final TARDISShop plugin;
    private final TARDISShopDatabase service = TARDISShopDatabase.getInstance();
    private final Connection connection = service.getConnection();

    /**
     * Inserts data into an SQLite database table. This method builds a prepared SQL statement from the parameters
     * supplied and then executes the insert.
     *
     * @param plugin an instance of the main plugin class
     */
    public InsertShopItem(TARDISShop plugin) {
        this.plugin = plugin;
    }

    public TARDISShopItem addNamedItem(String item, double cost) {
        PreparedStatement ps = null;
        ResultSet idRS = null;
        try {
            ps = connection.prepareStatement("INSERT INTO items (item, cost) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, item);
            ps.setDouble(2, cost);
            ps.executeUpdate();
            idRS = ps.getGeneratedKeys();
            int id = (idRS.next()) ? idRS.getInt(1) : -1;
            return new TARDISShopItem(id, item, null, cost);
        } catch (SQLException e) {
            plugin.debug("Insert error for items! " + e.getMessage());
        } finally {
            try {
                if (idRS != null) {
                    idRS.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing items! " + e.getMessage());
            }
        }
        return null;
    }
}
