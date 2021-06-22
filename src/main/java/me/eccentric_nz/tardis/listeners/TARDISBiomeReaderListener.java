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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisInventorySerializer;
import me.eccentric_nz.tardis.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.tardis.enumeration.Storage;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisBiome;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.Material;
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
public class TardisBiomeReaderListener implements Listener {

    private final TardisPlugin plugin;

    public TardisBiomeReaderListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public static boolean hasBiomeDisk(ItemStack[] stack, String biome) {
        boolean found = false;
        for (int s = 27; s < stack.length; s++) {
            ItemStack disk = stack[s];
            if (disk != null && disk.hasItemMeta()) {
                ItemMeta diskim = disk.getItemMeta();
                assert diskim != null;
                if (diskim.hasLore()) {
                    List<String> lore = diskim.getLore();
                    assert lore != null;
                    if (lore.contains(biome)) {
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
        if (Objects.requireNonNull(event.getClickedBlock()).getType().isInteractable()) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(Material.BRICK) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            assert im != null;
            if (im.hasDisplayName() && im.getDisplayName().equals("TARDIS Biome Reader")) {
                TardisBiome biome = TardisStaticUtils.getBiomeAt(event.getClickedBlock().getLocation());
                if (biome.equals(TardisBiome.THE_VOID)) {
                    TardisMessage.send(player, "BIOME_READER_NOT_VALID");
                    return;
                }
                UUID uuid = player.getUniqueId();
                // check if they have this biome disk yet
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
                if (rs.resultSet()) {
                    try {
                        ItemStack[] disks1;
                        if (!rs.getBiomesOne().isEmpty()) {
                            disks1 = TardisInventorySerializer.itemStacksFromString(rs.getBiomesOne());
                        } else {
                            disks1 = TardisInventorySerializer.itemStacksFromString(Storage.BIOME_1.getEmpty());
                        }
                        if (!hasBiomeDisk(disks1, biome.name())) {
                            ItemStack[] disks2;
                            if (!rs.getBiomesOne().isEmpty()) {
                                disks2 = TardisInventorySerializer.itemStacksFromString(rs.getBiomesTwo());
                            } else {
                                disks2 = TardisInventorySerializer.itemStacksFromString(Storage.BIOME_2.getEmpty());
                            }
                            if (!hasBiomeDisk(disks2, biome.name())) {
                                ItemStack bd = new ItemStack(Material.MUSIC_DISC_CAT, 1);
                                ItemMeta dim = bd.getItemMeta();
                                assert dim != null;
                                dim.setDisplayName("Biome Storage Disk");
                                List<String> disk_lore = new ArrayList<>();
                                disk_lore.add(biome.name());
                                dim.setLore(disk_lore);
                                dim.setCustomModelData(10000001);
                                bd.setItemMeta(dim);
                                int slot = getNextFreeSlot(disks1);
                                if (slot != -1) {
                                    disks1[slot] = bd;
                                    String serialized = TardisInventorySerializer.itemStacksToString(disks1);
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("biomes_one", serialized);
                                    HashMap<String, Object> whereu = new HashMap<>();
                                    whereu.put("uuid", uuid.toString());
                                    plugin.getQueryFactory().doUpdate("storage", set, whereu);
                                    TardisMessage.send(player, "BIOME_READER_ADDED", biome.name(), "1");
                                } else {
                                    slot = getNextFreeSlot(disks2);
                                    if (slot != -1) {
                                        disks2[slot] = bd;
                                        String serialized = TardisInventorySerializer.itemStacksToString(disks2);
                                        HashMap<String, Object> set = new HashMap<>();
                                        set.put("biomes_two", serialized);
                                        HashMap<String, Object> whereu = new HashMap<>();
                                        whereu.put("uuid", uuid.toString());
                                        plugin.getQueryFactory().doUpdate("storage", set, whereu);
                                        TardisMessage.send(player, "BIOME_READER_ADDED", biome.name(), "2");
                                    } else {
                                        TardisMessage.send(player, "BIOME_READER_FULL");
                                    }
                                }
                            } else {
                                TardisMessage.send(player, "BIOME_READER_FOUND", biome.name(), "2");
                            }
                        } else {
                            TardisMessage.send(player, "BIOME_READER_FOUND", biome.name(), "1");
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
