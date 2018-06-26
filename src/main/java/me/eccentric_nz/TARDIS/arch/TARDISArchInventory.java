/*
 * Copyright (C) 2018 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Some parts written by:
 * Kristian S. Stangeland aadnk
 * Norway
 * kristian@comphenix.net
 * thtp://www.comphenix.net/
 */
package me.eccentric_nz.TARDIS.arch;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.arch.attributes.*;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISArchInventory {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final String prefix = TARDIS.plugin.getPrefix();

    public void switchInventories(Player p, int arch) {
        String uuid = p.getUniqueId().toString();
        String name = p.getName();
        String inv = TARDISArchSerialization.toDatabase(p.getInventory().getContents());
        String attr = TARDISAttributeSerialization.toDatabase(getAttributeMap(p.getInventory().getContents()));
        String arm_attr = TARDISAttributeSerialization.toDatabase(getAttributeMap(p.getInventory().getArmorContents()));
        String arm = TARDISArchSerialization.toDatabase(p.getInventory().getArmorContents());
        Statement statement = null;
        PreparedStatement ps = null;
        ResultSet rsInv = null;
        ResultSet rsToInv = null;
        try {
            Connection connection = service.getConnection();
            service.testConnection(connection);
            statement = connection.createStatement();
            // get their current inventory from database
            String getQuery = "SELECT id FROM " + prefix + "inventories WHERE uuid = '" + uuid + "' AND arch = '" + arch + "'";
            rsInv = statement.executeQuery(getQuery);
            if (rsInv.next()) {
                // update it with their current inventory
                int id = rsInv.getInt("id");
                String updateQuery = "UPDATE " + prefix + "inventories SET inventory = ?, armour = ?, attributes = ?, armour_attributes = ? WHERE id = ?";
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
                String insertQuery = "INSERT INTO " + prefix + "inventories (uuid, player, arch, inventory, armour, attributes, armour_attributes) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
            String getToQuery = "SELECT * FROM " + prefix + "inventories WHERE uuid = '" + uuid + "' AND arch = '" + to + "'";
            rsToInv = statement.executeQuery(getToQuery);
            if (rsToInv.next()) {
                // set their inventory to the saved one
                try {
                    ItemStack[] i = TARDISArchSerialization.fromDatabase(rsToInv.getString("inventory"));
                    ItemStack[] a = TARDISArchSerialization.fromDatabase(rsToInv.getString("armour"));
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
                    TARDIS.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                        ShapedRecipe recipe = TARDIS.plugin.getFigura().getShapedRecipes().get("Fob Watch");
                        ItemStack result = recipe.getResult();
                        result.setAmount(1);
                        p.getInventory().addItem(result);
                        p.updateInventory();
                    }, 5L);
                }
            }
            rsToInv.close();
            statement.close();
            p.updateInventory();
        } catch (SQLException e) {
            System.err.println("Could not save inventory on Chameleon Arch change, " + e);
        } finally {
            try {
                if (rsToInv != null) {
                    rsToInv.close();
                }
                if (rsInv != null) {
                    rsInv.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                System.err.println("Could not close resources: " + ex.getMessage());
            }
        }
    }

    public void clear(UUID uuid) {
        Connection connection = service.getConnection();
        PreparedStatement ps = null;
        try {
            service.testConnection(connection);
            String clearQuery = "DELETE FROM " + prefix + "inventories WHERE uuid = ?";
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
        HashMap<Integer, List<TARDISAttributeData>> map = new HashMap<>();
        int add = (stacks.length == 4) ? 36 : 0;
        for (int s = 0; s < stacks.length; s++) {
            ItemStack i = stacks[s];
            if (i != null && !i.getType().equals(Material.AIR)) {
                TARDISAttributes attributes = new TARDISAttributes(i);
                if (attributes.size() > 0) {
                    List<TARDISAttributeData> ist = new ArrayList<>();
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
            cus.forEach((key, value) -> {
                int slot = key;
                if (slot != -1) {
                    ItemStack is = p.getInventory().getItem(slot);
                    TARDISAttributes attributes = new TARDISAttributes(is);
                    value.forEach((ad) -> {
                        attributes.add(TARDISAttribute.newBuilder().name(ad.getAttribute()).type(TARDISAttributeType.fromId(ad.getAttributeID())).operation(ad.getOperation()).amount(ad.getValue()).build());
                        p.getInventory().setItem(key, attributes.getStack());
                    });
                }
            });
        } catch (IOException e) {
            System.err.println("Could not reapply custom attributes, " + e);
        }
    }
}
