/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.messaging.TardisMessage;
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
public class TardisCircuitDamager {

    private final TardisPlugin plugin;
    private final DiskCircuit diskCircuit;
    private final int id;
    private final Player player;
    private int usesLeft;

    public TardisCircuitDamager(TardisPlugin plugin, DiskCircuit diskCircuit, int usesLeft, int id, Player player) {
        this.plugin = plugin;
        this.diskCircuit = diskCircuit;
        this.usesLeft = usesLeft;
        this.id = id;
        this.player = player;
    }

    public void damage() {
        if (usesLeft == 0) {
            usesLeft = plugin.getConfig().getInt("circuits.uses." + diskCircuit.toString().toLowerCase(Locale.ENGLISH));
        }
        if ((usesLeft - 1) <= 0) {
            // destroy
            setCircuitDamage(diskCircuit.getName(), 0, true);
            TardisMessage.send(player, "CIRCUIT_VAPOUR", diskCircuit.getName());
        } else {
            // decrement
            int decremented = usesLeft - 1;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                setCircuitDamage(diskCircuit.getName(), decremented, false);
                TardisMessage.send(player, "CIRCUIT_USES", diskCircuit.getName(), String.format("%d", decremented));
            }, 5L);
        }
    }

    private void setCircuitDamage(String circuitName, int decremented, boolean destroy) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetDiskStorage resultSetDiskStorage = new ResultSetDiskStorage(plugin, where);
        if (resultSetDiskStorage.resultSet()) {
            ItemStack[] itemStacks;
            ItemStack[] clone = new ItemStack[9];
            int i = 0;
            try {
                itemStacks = TardisInventorySerializer.itemStacksFromString(resultSetDiskStorage.getConsole());
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack != null && itemStack.hasItemMeta()) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        assert itemMeta != null;
                        if (itemMeta.hasDisplayName()) {
                            String displayName = itemMeta.getDisplayName();
                            if (displayName.equals(circuitName)) {
                                if (destroy) {
                                    clone[i] = null;
                                } else {
                                    // set uses
                                    List<String> lore = itemMeta.getLore();
                                    if (lore == null) {
                                        lore = Arrays.asList("Uses left", "");
                                    }
                                    String stripped = ChatColor.YELLOW + "" + decremented;
                                    lore.set(1, stripped);
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    clone[i] = itemStack;
                                }
                            } else {
                                clone[i] = itemStack;
                            }
                        }
                    }
                    i++;
                }
                String serialized = TardisInventorySerializer.itemStacksToString(clone);
                HashMap<String, Object> set = new HashMap<>();
                set.put("console", serialized);
                HashMap<String, Object> wheret = new HashMap<>(); // TODO Figure out how ED names these HashMap variables.
                wheret.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("storage", set, wheret);
            } catch (IOException ioException) {
                plugin.debug("Could not get console items: " + ioException);
            }
        }
    }
}
