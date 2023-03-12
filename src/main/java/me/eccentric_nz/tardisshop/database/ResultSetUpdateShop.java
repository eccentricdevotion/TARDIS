package me.eccentric_nz.tardisshop.database;

import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardisshop.TARDISShop;
import me.eccentric_nz.tardisshop.TARDISShopItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetUpdateShop {

    private final TARDISShopDatabase service = TARDISShopDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISShop plugin;

    private List<TARDISShopItem> shopItems;

    public ResultSetUpdateShop(TARDISShop plugin) {
        this.plugin = plugin;
    }

    public boolean getAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        final String query = "SELECT * FROM items";
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
