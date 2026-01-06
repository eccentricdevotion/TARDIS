/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class CircuitDamager {

    private final TARDIS plugin;
    private final DiskCircuit circuit;
    private final int id;
    private final Player p;
    private int uses_left;

    public CircuitDamager(TARDIS plugin, DiskCircuit circuit, int uses_left, int id, Player p) {
        this.plugin = plugin;
        this.circuit = circuit;
        this.uses_left = uses_left;
        this.id = id;
        this.p = p;
    }

    public void damage() {
        if (uses_left == 0) {
            uses_left = plugin.getConfig().getInt("circuits.uses." + circuit.toString().toLowerCase(Locale.ROOT));
        }
        int decremented = uses_left - 1;
        if (decremented <= 0) {
            // destroy
            setCircuitDamage(circuit.getName(), 0, true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "CIRCUIT_VAPOUR", circuit.getName());
        } else {
            // decrement
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                setCircuitDamage(circuit.getName(), decremented, false);
                plugin.getMessenger().send(p, TardisModule.TARDIS, "CIRCUIT_USES", circuit.getName(), String.format("%d", decremented));
            }, 5L);
        }
    }

    private void setCircuitDamage(String c, int decremented, boolean destroy) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            ItemStack[] items;
            ItemStack[] clone = new ItemStack[18];
            int i = 0;
            try {
                items = SerializeInventory.itemStacksFromString(rs.getConsole());
                for (ItemStack is : items) {
                    if (is != null && is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName()) {
                            String dn = ComponentUtils.stripColour(im.displayName());
                            if (dn.endsWith(c)) {
                                if (destroy) {
                                    clone[i] = null;
                                } else {
                                    // set uses
                                    List<Component> lore = im.lore();
                                    if (lore == null) {
                                        lore = List.of(Component.text("Uses left"), Component.text(""));
                                    }
                                    Component yellow = Component.text(decremented, NamedTextColor.YELLOW);
                                    lore.set(1, yellow);
                                    im.lore(lore);
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
                String serialized = SerializeInventory.itemStacksToString(clone);
                HashMap<String, Object> set = new HashMap<>();
                set.put("console", serialized);
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("storage", set, wheret);
            } catch (IOException ex) {
                plugin.debug("Could not get console items: " + ex);
            }
        }
    }
}
