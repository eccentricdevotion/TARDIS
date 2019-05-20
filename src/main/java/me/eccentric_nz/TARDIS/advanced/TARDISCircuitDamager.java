/*
 * Copyright (C) 2019 eccentric_nz
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
 */
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISCircuitDamager {

    private final TARDIS plugin;
    private final DISK_CIRCUIT circuit;
    private int uses_left;
    private final int id;
    private final Player p;

    public TARDISCircuitDamager(TARDIS plugin, DISK_CIRCUIT circuit, int uses_left, int id, Player p) {
        this.plugin = plugin;
        this.circuit = circuit;
        this.uses_left = uses_left;
        this.id = id;
        this.p = p;
    }

    public void damage() {
        if (uses_left == 0) {
            uses_left = plugin.getConfig().getInt("circuits.uses." + circuit.toString().toLowerCase(Locale.ENGLISH));
        }
        if ((uses_left - 1) <= 0) {
            // destroy
            setCircuitDamage(circuit.getName(), 0, true);
            TARDISMessage.send(p, "CIRCUIT_VAPOUR", circuit.getName());
        } else {
            // decrement
            int decremented = uses_left - 1;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                setCircuitDamage(circuit.getName(), decremented, false);
                TARDISMessage.send(p, "CIRCUIT_USES", circuit.getName(), String.format("%d", decremented));
            }, 5L);
        }
    }

    private void setCircuitDamage(String c, int decremented, boolean destroy) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            ItemStack[] items;
            ItemStack[] clone = new ItemStack[9];
            int i = 0;
            try {
                items = TARDISSerializeInventory.itemStacksFromString(rs.getConsole());
                for (ItemStack is : items) {
                    if (is != null && is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName()) {
                            String dn = im.getDisplayName();
                            if (dn.equals(c)) {
                                if (destroy) {
                                    clone[i] = null;
                                } else {
                                    // set uses
                                    List<String> lore = im.getLore();
                                    if (lore == null) {
                                        lore = Arrays.asList("Uses left", "");
                                    }
                                    String stripped = ChatColor.YELLOW + "" + decremented;
                                    lore.set(1, stripped);
                                    im.setLore(lore);
                                    is.setItemMeta(im);
                                    clone[i] = is;
                                }
                            } else {
                                clone[i] = is;
                            }
                        }
                    }
                    i++;
                }
                String serialized = TARDISSerializeInventory.itemStacksToString(clone);
                HashMap<String, Object> set = new HashMap<>();
                set.put("console", serialized);
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                new QueryFactory(plugin).doUpdate("storage", set, wheret);
            } catch (IOException ex) {
                plugin.debug("Could not get console items: " + ex);
            }
        }
    }
}
