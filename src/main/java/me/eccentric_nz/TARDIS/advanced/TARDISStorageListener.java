/*
 * Copyright (C) 2013 eccentric_nz
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.STORAGE;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISStorageListener implements Listener {

    private final TARDIS plugin;
    List<String> titles = new ArrayList<String>();

    public TARDISStorageListener(TARDIS plugin) {
        this.plugin = plugin;
        for (STORAGE s : STORAGE.values()) {
            this.titles.add(s.getTitle());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        if (!titles.contains(title) || event.isCancelled()) {
            return;
        }
        int slot = event.getRawSlot();
        if (slot < 27) {
            event.setCancelled(true);
        }
        final Player player = (Player) event.getWhoClicked();
        String playerNameStr = player.getName();
        // get the storage record
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", playerNameStr);
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            // which inventory screen is it?
            String[] split = title.split(" ");
            STORAGE store = STORAGE.valueOf(split[0].toUpperCase(Locale.ENGLISH));
            if (slot == 18 || slot == 26) {
                saveCurrentStorage(inv, store.getTable(), playerNameStr);
            }
            switch (store) {
                case AREA:
                    switch (slot) {
                        case 18:
                            // switch to second
                            loadInventory(rs.getSavesTwo(), player, STORAGE.SECOND);
                            break;
                        case 26:
                            // switch to players
                            loadInventory(rs.getPlayers(), player, STORAGE.PLAYER);
                            break;
                        default:
                            break;
                    }
                    break;
                case BIOME:
                    switch (slot) {
                        case 18:
                            // switch to players
                            loadInventory(rs.getPlayers(), player, STORAGE.PLAYER);
                            break;
                        case 26:
                            // switch to presets
                            loadInventory(rs.getPresets(), player, STORAGE.PRESET);
                            break;
                        default:
                            break;
                    }
                    break;
                case CIRCUIT:
                    switch (slot) {
                        case 18:
                            // switch to presets
                            loadInventory(rs.getPresets(), player, STORAGE.PRESET);
                            break;
                        case 26:
                            // switch to first
                            loadInventory(rs.getSavesOne(), player, STORAGE.FIRST);
                            break;
                        default:
                            break;
                    }
                    break;
                case FIRST:
                    switch (slot) {
                        case 18:
                            // switch to circuits
                            loadInventory(rs.getCircuits(), player, STORAGE.CIRCUIT);
                            break;
                        case 26:
                            // switch to second
                            loadInventory(rs.getSavesTwo(), player, STORAGE.SECOND);
                            break;
                        default:
                            break;
                    }
                    break;
                case PLAYER:
                    switch (slot) {
                        case 18:
                            // switch to areas
                            loadInventory(rs.getAreas(), player, STORAGE.AREA);
                            break;
                        case 26:
                            // switch to biomes
                            loadInventory(rs.getBiomes(), player, STORAGE.BIOME);
                            break;
                        default:
                            break;
                    }
                    break;
                case PRESET:
                    switch (slot) {
                        case 18:
                            // switch to biomes
                            loadInventory(rs.getBiomes(), player, STORAGE.BIOME);
                            break;
                        case 26:
                            // switch to circuits
                            loadInventory(rs.getCircuits(), player, STORAGE.CIRCUIT);
                            break;
                        default:
                            break;
                    }
                    break;
                default: // second
                    switch (slot) {
                        case 18:
                            // switch to first
                            loadInventory(rs.getSavesOne(), player, STORAGE.FIRST);
                            break;
                        case 26:
                            // switch to areas
                            loadInventory(rs.getAreas(), player, STORAGE.AREA);
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }
    }

    private void saveCurrentStorage(Inventory inv, String column, String p) {
        String serialized = TARDISSerializeInventory.itemStacksToString(inv.getContents());
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put(column, serialized);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", p);
        new QueryFactory(plugin).doUpdate("storage", set, where);
    }

    private void loadInventory(final String serialized, final Player p, final STORAGE s) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                ItemStack[] stack = null;
                try {
                    if (!serialized.isEmpty()) {
                        stack = TARDISSerializeInventory.itemStacksFromString(serialized);
                    } else {
                        if (s.equals(STORAGE.AREA)) {
                            stack = new TARDISAreaDisks(plugin).makeDisks(p);
                        } else {
                            stack = TARDISSerializeInventory.itemStacksFromString(s.getEmpty());
                        }
                    }
                } catch (IOException ex) {
                    plugin.debug("Could not get inventory from database! " + ex);
                }
                // close inventory
                p.closeInventory();
                // open new inventory
                Inventory inv = plugin.getServer().createInventory(p, 54, s.getTitle());
                inv.setContents(stack);
                p.openInventory(inv);
            }
        }, 1L);
    }
}
