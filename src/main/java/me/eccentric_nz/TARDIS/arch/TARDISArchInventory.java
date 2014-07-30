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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class TARDISArchInventory {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();

    @SuppressWarnings("deprecation")
    public void switchInventories(final Player p, int arch) {
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
            }
            rsInv.close();
            // check if they have an inventory for the apposing chameleon arch state
            int to = (arch == 0) ? 1 : 0;
            String getToQuery = "SELECT inventory, armour FROM inventories WHERE uuid = '" + uuid + "' AND arch = '" + to + "'";
            ResultSet rsToInv = statement.executeQuery(getToQuery);
            if (rsToInv.next()) {
                // set their inventory to the saved one
                ItemStack[] i = TARDISInventorySerialization.toItemStacks(rsToInv.getString("inventory"));
                p.getInventory().setContents(i);
                ItemStack[] a = TARDISInventorySerialization.toItemStacks(rsToInv.getString("armour"));
                setArmour(p, a);
            } else {
                // start with an empty inventory and armour
                p.getInventory().clear();
                p.getInventory().setBoots(null);
                p.getInventory().setChestplate(null);
                p.getInventory().setLeggings(null);
                p.getInventory().setHelmet(null);
                // give a fob watch if it is the Chameleon Arch inventory
                if (arch == 0) {
                    TARDIS.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, new Runnable() {
                        @Override
                        public void run() {
                            ShapedRecipe recipe = TARDIS.plugin.getFigura().getShapedRecipes().get("Fob Watch");
                            ItemStack result = recipe.getResult();
                            result.setAmount(1);
                            p.getInventory().addItem(result);
                            p.updateInventory();
                        }
                    }, 5L);
                }
            }
            rsToInv.close();
            statement.close();
            p.updateInventory();
        } catch (SQLException e) {
            System.err.println("Could not save inventory on Chameleon Arch change, " + e);
        }
    }

    public void clear(UUID uuid) {
        Connection connection = service.getConnection();
        PreparedStatement ps = null;
        try {
            service.testConnection(connection);
            String clearQuery = "DELETE FROM inventories WHERE uuid = ?";
            ps = connection.prepareStatement(clearQuery);
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not save inventory on Chameleon Arch change, " + e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Could not clear inventory on Chameleon Arch death, " + e);
                }
            }
        }
    }

    public void setArmour(Player p, ItemStack[] is) {
        p.getInventory().setArmorContents(is);
    }
}
