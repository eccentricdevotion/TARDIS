package me.eccentric_nz.tardisshop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardisshop.TARDISShopItem;

public class ResultSetUpdateShop {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    private List<TARDISShopItem> shopItems;

    public ResultSetUpdateShop(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean getAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        final String query = "SELECT * FROM " + prefix + "items";
        try {
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                shopItems = new ArrayList<>();
                while (rs.next()) {
                    shopItems.add(new TARDISShopItem(rs.getInt("item_id"), rs.getString("item"), TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location")), rs.getDouble("cost")));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for items table! " + e.getMessage());
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
                plugin.debug("Error closing items table! " + e.getMessage());
            }
        }
    }

    public List<TARDISShopItem> getShopItems() {
        return shopItems;
    }
}
