package me.eccentric_nz.tardisshop.database;

import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardisshop.TARDISShop;
import me.eccentric_nz.tardisshop.TARDISShopItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetShopItem {

    private final TARDISShopDatabase service = TARDISShopDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISShop plugin;

    private TARDISShopItem shopItem;

    public ResultSetShopItem(TARDISShop plugin) {
        this.plugin = plugin;
    }

    public boolean itemFromBlock(String location) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        final String query = "SELECT * FROM items WHERE location = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, location);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                shopItem = new TARDISShopItem(rs.getInt("item_id"), rs.getString("item"), TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getString("location")), rs.getDouble("cost"));
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

    public TARDISShopItem getShopItem() {
        return shopItem;
    }
}
