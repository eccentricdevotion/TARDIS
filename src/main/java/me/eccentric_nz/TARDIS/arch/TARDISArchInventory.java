/*
 * Kristian S. Stangeland aadnk
 * Norway
 * kristian@comphenix.net
 * thtp://www.comphenix.net/
 */
package me.eccentric_nz.TARDIS.arch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.arch.attributes.TARDISAttribute;
import me.eccentric_nz.TARDIS.arch.attributes.TARDISAttributeData;
import me.eccentric_nz.TARDIS.arch.attributes.TARDISAttributeSerialization;
import me.eccentric_nz.TARDIS.arch.attributes.TARDISAttributeType;
import me.eccentric_nz.TARDIS.arch.attributes.TARDISAttributes;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class TARDISArchInventory {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();

    @SuppressWarnings("deprecation")
    public void switchInventories(final Player p, int arch) {
        String uuid = p.getUniqueId().toString();
        String name = p.getName();
        String inv = TARDISArchSerialization.toDatabase(p.getInventory().getContents());
        String attr = TARDISAttributeSerialization.toDatabase(getAttributeMap(p.getInventory().getContents()));
        String arm_attr = TARDISAttributeSerialization.toDatabase(getAttributeMap(p.getInventory().getArmorContents()));
        String arm = TARDISArchSerialization.toDatabase(p.getInventory().getArmorContents());
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
                String updateQuery = "UPDATE inventories SET inventory = ?, armour = ?, attributes = ?, armour_attributes = ? WHERE id = ?";
                ps = connection.prepareStatement(updateQuery);
                ps.setString(1, inv);
                ps.setString(2, arm);
                ps.setString(3, attr);
                ps.setString(4, arm_attr);
                ps.setInt(5, id);
                ps.executeUpdate();
                ps.close();
            } else {
                // they haven't got an inventory saved yet so make one with their current inventory
                String insertQuery = "INSERT INTO inventories (uuid, player, arch, inventory, armour, attributes, armour_attributes) VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, uuid);
                ps.setString(2, name);
                ps.setInt(3, arch);
                ps.setString(4, inv);
                ps.setString(5, arm);
                ps.setString(6, attr);
                ps.setString(7, arm_attr);
                ps.executeUpdate();
                ps.close();
            }
            rsInv.close();
            // check if they have an inventory for the apposing chameleon arch state
            int to = (arch == 0) ? 1 : 0;
            String getToQuery = "SELECT * FROM inventories WHERE uuid = '" + uuid + "' AND arch = '" + to + "'";
            ResultSet rsToInv = statement.executeQuery(getToQuery);
            if (rsToInv.next()) {
                // set their inventory to the saved one
                try {
                    String to_inv = rsToInv.getString("inventory");
                    ItemStack[] i;
                    ItemStack[] a;
                    if (to_inv.startsWith("[")) {
                        // old data format
                        i = TARDISInventorySerialization.toItemStacks(to_inv);
                        a = TARDISInventorySerialization.toItemStacks(rsToInv.getString("armour"));
                    } else {
                        // new data format - supports Fireworks meta
                        i = TARDISArchSerialization.fromDatabase(to_inv);
                        a = TARDISArchSerialization.fromDatabase(rsToInv.getString("armour"));
                    }
                    p.getInventory().setContents(i);
                    p.getInventory().setArmorContents(a);
                    reapplyCustomAttributes(p, rsToInv.getString("attributes"));
                    reapplyCustomAttributes(p, rsToInv.getString("armour_attributes"));
                } catch (IOException ex) {
                    System.err.println("Could not restore inventory on Chameleon Arch change, " + ex);
                }
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

    private HashMap<Integer, List<TARDISAttributeData>> getAttributeMap(ItemStack[] stacks) {
        HashMap<Integer, List<TARDISAttributeData>> map = new HashMap<Integer, List<TARDISAttributeData>>();
        int add = (stacks.length == 4) ? 36 : 0;
        for (int s = 0; s < stacks.length; s++) {
            ItemStack i = stacks[s];
            if (i != null && !i.getType().equals(Material.AIR)) {
                TARDISAttributes attributes = new TARDISAttributes(i);
                if (attributes.size() > 0) {
                    List<TARDISAttributeData> ist = new ArrayList<TARDISAttributeData>();
                    for (TARDISAttribute a : attributes.values()) {
                        TARDISAttributeData data = new TARDISAttributeData(a.getName(), a.getAttributeType().getMinecraftId(), a.getAmount(), a.getOperation());
                        ist.add(data);
                    }
                    map.put(s + add, ist);
                }
            }
        }
        return map;
    }

    private void reapplyCustomAttributes(Player p, String data) {
        try {
            HashMap<Integer, List<TARDISAttributeData>> cus = TARDISAttributeSerialization.fromDatabase(data);
            for (Map.Entry<Integer, List<TARDISAttributeData>> m : cus.entrySet()) {
                int slot = m.getKey();
                if (slot != -1) {
                    ItemStack is = p.getInventory().getItem(slot);
                    TARDISAttributes attributes = new TARDISAttributes(is);
                    for (TARDISAttributeData ad : m.getValue()) {
                        attributes.add(TARDISAttribute.newBuilder().name(ad.getAttribute()).type(TARDISAttributeType.fromId(ad.getAttributeID())).operation(ad.getOperation()).amount(ad.getValue()).build());
                        p.getInventory().setItem(m.getKey(), attributes.getStack());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not reapply custom attributes, " + e);
        }
    }

}
