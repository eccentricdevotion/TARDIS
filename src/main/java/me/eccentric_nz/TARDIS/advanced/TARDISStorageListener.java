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
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.STORAGE;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISStorageListener implements Listener {

    private final TARDIS plugin;
    List<String> titles = new ArrayList<String>();
    private final List<Material> onlythese = new ArrayList<Material>();

    public TARDISStorageListener(TARDIS plugin) {
        this.plugin = plugin;
        for (STORAGE s : STORAGE.values()) {
            this.titles.add(s.getTitle());
        }
        for (DISK_CIRCUIT dc : DISK_CIRCUIT.values()) {
            if (!onlythese.contains(dc.getMaterial())) {
                onlythese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDiskStorageClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        if (titles.contains(title)) {
            // which inventory screen is it?
            String[] split = title.split(" ");
            String tmp = split[0].toUpperCase(Locale.ENGLISH);
            if (split.length > 2) {
                tmp = tmp + "_" + split[2];
            }
            STORAGE store = STORAGE.valueOf(tmp);
            saveCurrentStorage(inv, store.getTable(), (Player) event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDiskStorageInteract(InventoryClickEvent event) {
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
            String tmp = split[0].toUpperCase(Locale.ENGLISH);
            if (split.length > 2) {
                tmp = tmp + "_" + split[2];
            }
            STORAGE store = STORAGE.valueOf(tmp);
            if (slot < 6 || slot == 18 || slot == 26) {
                saveCurrentStorage(inv, store.getTable(), player);
            }
            switch (slot) {
                case 0:
                    // switch to saves
                    loadInventory(rs.getSavesOne(), player, STORAGE.SAVE_1);
                    break;
                case 1:
                    // switch to areas
                    loadInventory(rs.getAreas(), player, STORAGE.AREA);
                    break;
                case 2:
                    // switch to players
                    loadInventory(rs.getPlayers(), player, STORAGE.PLAYER);
                    break;
                case 3:
                    // switch to biomes
                    loadInventory(rs.getBiomesOne(), player, STORAGE.BIOME_1);
                    break;
                case 4:
                    // switch to presets
                    loadInventory(rs.getPresetsOne(), player, STORAGE.PRESET_1);
                    break;
                case 5:
                    // switch to circuits
                    loadInventory(rs.getCircuits(), player, STORAGE.CIRCUIT);
                    break;
            }
            switch (store) {
                case BIOME_1:
                    switch (slot) {
                        case 26:
                            // switch to biome 2
                            loadInventory(rs.getBiomesTwo(), player, STORAGE.BIOME_2);
                            break;
                        default:
                            break;
                    }
                    break;
                case BIOME_2:
                    switch (slot) {
                        case 18:
                            // switch to biome 1
                            loadInventory(rs.getBiomesOne(), player, STORAGE.BIOME_1);
                            break;
                        default:
                            break;
                    }
                    break;
                case PRESET_1:
                    switch (slot) {
                        case 26:
                            // switch to preset 2
                            loadInventory(rs.getPresetsTwo(), player, STORAGE.PRESET_2);
                            break;
                        default:
                            break;
                    }
                    break;
                case PRESET_2:
                    switch (slot) {
                        case 18:
                            // switch to preset 1
                            loadInventory(rs.getPresetsOne(), player, STORAGE.PRESET_1);
                            break;
                        default:
                            break;
                    }
                    break;
                case SAVE_1:
                    switch (slot) {
                        case 26:
                            // switch to save 2
                            loadInventory(rs.getSavesTwo(), player, STORAGE.SAVE_2);
                            break;
                        default:
                            break;
                    }
                    break;
                case SAVE_2:
                    switch (slot) {
                        case 18:
                            // switch to save 1
                            loadInventory(rs.getSavesOne(), player, STORAGE.SAVE_1);
                            break;
                        default:
                            break;
                    }
                    break;
                default: // no extra pages
                    break;
            }
        }
    }

    private void saveCurrentStorage(Inventory inv, String column, Player p) {
        // loop through inventory contents and remove any items that are not disks or circuits
        for (int i = 27; i < 54; i++) {
            ItemStack is = inv.getItem(i);
            if (is != null) {
                if (!onlythese.contains(is.getType())) {
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), is);
                    inv.setItem(i, new ItemStack(Material.AIR));
                }
            }
        }
        String serialized = TARDISSerializeInventory.itemStacksToString(inv.getContents());
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put(column, serialized);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", p.getName());
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
