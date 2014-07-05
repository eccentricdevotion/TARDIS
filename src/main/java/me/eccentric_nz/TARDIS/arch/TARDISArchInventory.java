/*
 * Kristian S. Stangeland aadnk
 * Norway
 * kristian@comphenix.net
 * thtp://www.comphenix.net/
 */
package me.eccentric_nz.TARDIS.arch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class TARDISArchInventory {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();

    @SuppressWarnings("deprecation")
    public void switchInventories(Player p, int arch) {
        String uuid = p.getUniqueId().toString();
        String name = p.getName();
        String inv = TARDISInventorySerialization.toString(p.getInventory().getContents());
        String arm = TARDISInventorySerialization.toString(p.getInventory().getArmorContents());
        try {
            Connection connection = service.getConnection();
            service.testConnection(connection);
            Statement statement = connection.createStatement();
            PreparedStatement ps;
            // get their current gamemode inventory from database
            String getQuery = "SELECT id FROM inventories WHERE uuid = '" + uuid + "' AND arch = '" + arch + "'";
            ResultSet rsInv = statement.executeQuery(getQuery);
            if (rsInv.next()) {
                // update it with their current inventory
                int id = rsInv.getInt("id");
                String updateQuery = "UPDATE inventories SET inventory = ?, armour = ? WHERE id = ?";
                ps = connection.prepareStatement(updateQuery);
                ps.setString(1, inv);
                ps.setString(2, arm);
                ps.setInt(3, id);
                ps.executeUpdate();
                ps.close();
            } else {
                // they haven't got an inventory saved yet so make one with their current inventory
                String insertQuery = "INSERT INTO inventories (uuid, player, arch, inventory, armour) VALUES (?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, uuid);
                ps.setString(2, name);
                ps.setInt(3, arch);
                ps.setString(4, inv);
                ps.setString(5, arm);
                ps.executeUpdate();
                ps.close();
                // give a fob watch if it is the Chameleon Arch inventory
                if (arch == 0) {
                    TARDIS.plugin.debug("Giving a fob watch...");
                    ShapedRecipe recipe = TARDIS.plugin.getFigura().getShapedRecipes().get("Fob Watch");
                    ItemStack result = recipe.getResult();
                    TARDIS.plugin.debug("Result: " + result.toString());
                    result.setAmount(1);
                    p.getInventory().addItem(result);
                    p.updateInventory();
                }
            }
            rsInv.close();
            // check if they have an inventory for the apposing chameleon arch state
            int to = (arch == 0) ? 1 : 0;
            String getNewQuery = "SELECT inventory, armour FROM inventories WHERE uuid = '" + uuid + "' AND arch = '" + to + "'";
            ResultSet rsNewInv = statement.executeQuery(getNewQuery);
            if (rsNewInv.next()) {
                // set their inventory to the saved one
                ItemStack[] i = TARDISInventorySerialization.toItemStacks(rsNewInv.getString("inventory"));
                p.getInventory().setContents(i);
                ItemStack[] a = TARDISInventorySerialization.toItemStacks(rsNewInv.getString("armour"));
                setArmour(p, a);
            } else {
                // start with an empty inventory and armour
                p.getInventory().clear();
                p.getInventory().setBoots(null);
                p.getInventory().setChestplate(null);
                p.getInventory().setLeggings(null);
                p.getInventory().setHelmet(null);
            }
            rsNewInv.close();
            statement.close();
            p.updateInventory();
        } catch (SQLException e) {
            System.err.println("Could not save inventory on Chameleon Arch change, " + e);
        }
    }

    public void restoreOnSpawn(Player p) {
        String uuid = p.getUniqueId().toString();
        String gm = p.getGameMode().name();
        // restore their inventory
        try {
            Connection connection = service.getConnection();
            service.testConnection(connection);
            Statement statement = connection.createStatement();
            // get their current gamemode inventory from database
            String getQuery = "SELECT inventory, armour FROM inventories WHERE uuid = '" + uuid + "' AND gamemode = '" + gm + "'";
            ResultSet rsInv = statement.executeQuery(getQuery);
            if (rsInv.next()) {
                // set their inventory to the saved one
                String base64 = rsInv.getString("inventory");
                ItemStack[] i = TARDISInventorySerialization.toItemStacks(base64);
                p.getInventory().setContents(i);
                String savedarmour = rsInv.getString("armour");
                ItemStack[] a = TARDISInventorySerialization.toItemStacks(savedarmour);
                setArmour(p, a);
            }
            rsInv.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Could not restore inventories on respawn, " + e);
        }
    }

    public void setArmour(Player p, ItemStack[] is) {
        p.getInventory().setArmorContents(is);
    }

    public boolean isInstanceOf(Entity e) {
        return e instanceof PoweredMinecart || e instanceof StorageMinecart || e instanceof HopperMinecart || e instanceof ItemFrame;
    }

    public boolean isInstanceOf(InventoryHolder h) {
        return h instanceof Horse;
    }
}
