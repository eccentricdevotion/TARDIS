package me.eccentric_nz.tardisshop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.tardisshop.TARDISShopItem;

public class InsertShopItem {

    private final TARDIS plugin;
    private final String prefix;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();

    /**
     * Inserts data into an SQLite database table. This method builds a prepared
     * SQL statement from the parameters supplied and then executes the insert.
     *
     * @param plugin an instance of the main plugin class
     */
    public InsertShopItem(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public TARDISShopItem addNamedItem(String item, double cost) {
        PreparedStatement ps = null;
        ResultSet idRS = null;
        try {
            ps = connection.prepareStatement("INSERT INTO " + prefix + "items (item, cost) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
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
