/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISBiomeReaderListener implements Listener {

    private final TARDIS plugin;

    public TARDISBiomeReaderListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static boolean hasBiomeDisk(ItemStack[] stack, String biome) {
        boolean found = false;
        for (int s = 27; s < stack.length; s++) {
            ItemStack disk = stack[s];
            if (disk != null && disk.hasItemMeta()) {
                ItemMeta diskim = disk.getItemMeta();
                if (diskim.hasLore()) {
                    List<String> lore = diskim.getLore();
                    if (lore.contains(biome.toUpperCase(Locale.ROOT))) {
                        found = true;
                        break;
                    }
                }
            }
        }
        return found;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (event.getClickedBlock().getType().isInteractable()) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(Material.BRICK) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName() && im.getDisplayName().endsWith("TARDIS Biome Reader")) {
                UUID uuid = player.getUniqueId();
                Biome biome = event.getClickedBlock().getBiome();
                if (biome.equals(Biome.THE_VOID)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_READER_NOT_VALID");
                    return;
                }
                // check if they have this biome disk yet
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
                if (rs.resultSet()) {
                    try {
                        ItemStack[] disks1;
                        if (!rs.getBiomesOne().isEmpty()) {
                            disks1 = TARDISSerializeInventory.itemStacksFromString(rs.getBiomesOne());
                        } else {
                            disks1 = TARDISSerializeInventory.itemStacksFromString(Storage.BIOME_1.getEmpty());
                        }
                        String biomeKey = plugin.getFromRegistry().getKeysKey(biome);
                        if (!hasBiomeDisk(disks1, biomeKey)) {
                            ItemStack[] disks2;
                            if (!rs.getBiomesOne().isEmpty()) {
                                disks2 = TARDISSerializeInventory.itemStacksFromString(rs.getBiomesTwo());
                            } else {
                                disks2 = TARDISSerializeInventory.itemStacksFromString(Storage.BIOME_2.getEmpty());
                            }
                            if (!hasBiomeDisk(disks2, biomeKey)) {
                                ItemStack bd = new ItemStack(Material.MUSIC_DISC_CAT, 1);
                                ItemMeta dim = bd.getItemMeta();
                                dim.setDisplayName("Biome Storage Disk");
                                List<String> disk_lore = new ArrayList<>();
                                disk_lore.add(biomeKey);
                                dim.setLore(disk_lore);
                                bd.setItemMeta(dim);
                                int slot = getNextFreeSlot(disks1);
                                if (slot != -1) {
                                    disks1[slot] = bd;
                                    String serialized = TARDISSerializeInventory.itemStacksToString(disks1);
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("biomes_one", serialized);
                                    HashMap<String, Object> whereu = new HashMap<>();
                                    whereu.put("uuid", uuid.toString());
                                    plugin.getQueryFactory().doUpdate("storage", set, whereu);
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_READER_ADDED", biomeKey, "1");
                                } else {
                                    slot = getNextFreeSlot(disks2);
                                    if (slot != -1) {
                                        disks2[slot] = bd;
                                        String serialized = TARDISSerializeInventory.itemStacksToString(disks2);
                                        HashMap<String, Object> set = new HashMap<>();
                                        set.put("biomes_two", serialized);
                                        HashMap<String, Object> whereu = new HashMap<>();
                                        whereu.put("uuid", uuid.toString());
                                        plugin.getQueryFactory().doUpdate("storage", set, whereu);
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_READER_ADDED", biomeKey, "2");
                                    } else {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_READER_FULL");
                                    }
                                }
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_READER_FOUND", biomeKey, "2");
                            }
                        } else {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_READER_FOUND", biomeKey, "1");
                        }
                    } catch (IOException ex) {
                        plugin.debug("Could not get biome disks: " + ex);
                    }
                }
            }
        }
    }

    private int getNextFreeSlot(ItemStack[] stack) {
        int slot = -1;
        for (int s = 27; s < stack.length; s++) {
            ItemStack disk = stack[s];
            if (disk == null) {
                slot = s;
                break;
            }
        }
        return slot;
    }
}
