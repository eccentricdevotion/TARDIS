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
import me.eccentric_nz.tardis.enumeration.GlowstoneCircuit;
import me.eccentric_nz.tardis.enumeration.Storage;
import me.eccentric_nz.tardis.listeners.TardisMenuListener;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

/**
 * Banshee Circuits were components of TARDISes, emergency defence mechanisms used as a last resort when all other
 * systems had failed. They allowed a TARDIS to use whatever resources were available to ensure the survival of the ship
 * and its crew.
 *
 * @author eccentric_nz
 */
public class TardisStorageListener extends TardisMenuListener implements Listener {

    private final TardisPlugin plugin;
    private final List<String> invTitles = new ArrayList<>();
    private final List<Material> onlyThese = new ArrayList<>();

    public TardisStorageListener(TardisPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        for (Storage storage : Storage.values()) {
            invTitles.add(storage.getTitle());
        }
        for (DiskCircuit diskCircuit : DiskCircuit.values()) {
            if (!onlyThese.contains(diskCircuit.getMaterial())) {
                onlyThese.add(diskCircuit.getMaterial());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDiskStorageClose(InventoryCloseEvent event) {
        InventoryView inventoryView = event.getView();
        String title = inventoryView.getTitle();
        if (invTitles.contains(title)) {
            // which inventory screen is it?
            String[] split = title.split(" ");
            String temp = split[0].toUpperCase(Locale.ENGLISH);
            if (split.length > 2) {
                temp = temp + "_" + split[2];
            }
            Storage store = Storage.valueOf(temp);
            saveCurrentStorage(event.getInventory(), store.getTable(), (Player) event.getPlayer());
        } else if (!title.equals(ChatColor.DARK_RED + "TARDOS Console") && !title.equals(ChatColor.DARK_RED + "Handles Program")) {
            // scan the inventory for area disks and spit them out
            for (int i = 0; i < event.getInventory().getSize(); i++) {
                ItemStack itemStack = inventoryView.getItem(i);
                if (itemStack != null && itemStack.getType().equals(Material.MUSIC_DISC_BLOCKS) && itemStack.hasItemMeta()) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    assert itemMeta != null;
                    if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Area Storage Disk")) {
                        Player player = (Player) event.getPlayer();
                        Location location = player.getLocation();
                        Objects.requireNonNull(location.getWorld()).dropItemNaturally(location, itemStack);
                        inventoryView.setItem(i, new ItemStack(Material.AIR));
                        TardisMessage.send(player, "ADV_NO_STORE");
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDiskStorageInteract(InventoryClickEvent event) {
        InventoryView inventoryView = event.getView();
        String title = inventoryView.getTitle();
        if (!invTitles.contains(title)) {
            return;
        }
        int slot = event.getRawSlot();
        if ((slot >= 0 && slot < 27) || event.isShiftClick()) {
            event.setCancelled(true);
        }
        Player player = (Player) event.getWhoClicked();
        // get the storage record
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetDiskStorage resultSetDiskStorage = new ResultSetDiskStorage(plugin, where);
        if (resultSetDiskStorage.resultSet()) {
            // which inventory screen is it?
            String[] split = title.split(" ");
            String temp = split[0].toUpperCase(Locale.ENGLISH);
            if (split.length > 2) {
                temp = temp + "_" + split[2];
            }
            Storage storage = Storage.valueOf(temp);
            if (slot < 6 || slot == 18 || slot == 26) {
                saveCurrentStorage(event.getClickedInventory(), storage.getTable(), player);
            }
            switch (slot) {
                case 0:
                    if (!storage.equals(Storage.SAVE_1)) {
                        // switch to saves
                        loadInventory(resultSetDiskStorage.getSavesOne(), player, Storage.SAVE_1);
                    }
                    break;
                case 1:
                    if (!storage.equals(Storage.AREA)) {
                        // switch to areas
                        loadInventory(resultSetDiskStorage.getAreas(), player, Storage.AREA);
                    }
                    break;
                case 2:
                    if (!storage.equals(Storage.PLAYER)) {
                        // switch to players
                        loadInventory(resultSetDiskStorage.getPlayers(), player, Storage.PLAYER);
                    }
                    break;
                case 3:
                    if (!storage.equals(Storage.BIOME_1)) {
                        // switch to biomes
                        loadInventory(resultSetDiskStorage.getBiomesOne(), player, Storage.BIOME_1);
                    }
                    break;
                case 4:
                    if (!storage.equals(Storage.PRESET_1)) {
                        // switch to presets
                        loadInventory(resultSetDiskStorage.getPresetsOne(), player, Storage.PRESET_1);
                    }
                    break;
                case 5:
                    if (!storage.equals(Storage.CIRCUIT)) {
                        // switch to circuits
                        loadInventory(resultSetDiskStorage.getCircuits(), player, Storage.CIRCUIT);
                    }
                    break;
                default:
                    break;
            }
            switch (storage) {
                case BIOME_1:
                    if (slot == 26) {// switch to biome 2
                        loadInventory(resultSetDiskStorage.getBiomesTwo(), player, Storage.BIOME_2);
                    }
                    break;
                case BIOME_2:
                    if (slot == 18) {// switch to biome 1
                        loadInventory(resultSetDiskStorage.getBiomesOne(), player, Storage.BIOME_1);
                    }
                    break;
                case PRESET_1:
                    if (slot == 26) {// switch to preset 2
                        loadInventory(resultSetDiskStorage.getPresetsTwo(), player, Storage.PRESET_2);
                    }
                    break;
                case PRESET_2:
                    if (slot == 18) {// switch to preset 1
                        loadInventory(resultSetDiskStorage.getPresetsOne(), player, Storage.PRESET_1);
                    }
                    break;
                case SAVE_1:
                    if (slot == 26) {// switch to save 2
                        loadInventory(resultSetDiskStorage.getSavesTwo(), player, Storage.SAVE_2);
                    }
                    break;
                case SAVE_2:
                    if (slot == 18) {// switch to save 1
                        loadInventory(resultSetDiskStorage.getSavesOne(), player, Storage.SAVE_1);
                    }
                    break;
                default: // no extra pages
                    break;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropAreaDisk(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (itemStack.getType().equals(Material.MUSIC_DISC_BLOCKS) && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Area Storage Disk")) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                TardisMessage.send(player, "ADV_NO_DROP");
            }
        }
    }

    private void saveCurrentStorage(Inventory inventory, String column, Player player) {
        // loop through inventory contents and remove any items that are not disks or circuits
        for (int i = 27; i < 54; i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null) {
                if (!onlyThese.contains(itemStack.getType())) {
                    Objects.requireNonNull(player.getLocation().getWorld()).dropItemNaturally(player.getLocation(), itemStack);
                    inventory.setItem(i, new ItemStack(Material.AIR));
                }
            }
        }
        String serialized = TardisInventorySerializer.itemStacksToString(inventory.getContents());
        HashMap<String, Object> set = new HashMap<>();
        set.put(column, serialized);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().doUpdate("storage", set, where);
    }

    private void loadInventory(String serialized, Player player, Storage storage) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ItemStack[] itemStacks = null;
            try {
                if (!serialized.isEmpty()) {
                    if (storage.equals(Storage.AREA)) {
                        itemStacks = TardisInventorySerializer.itemStacksFromString(new TardisAreaDisks(plugin).checkDisksForNewAreas(player));
                    } else {
                        itemStacks = TardisInventorySerializer.itemStacksFromString(serialized);
                    }
                } else if (storage.equals(Storage.AREA)) {
                    itemStacks = new TardisAreaDisks(plugin).makeDisks(player);
                } else {
                    itemStacks = TardisInventorySerializer.itemStacksFromString(storage.getEmpty());
                }
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack != null && itemStack.hasItemMeta()) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        assert itemMeta != null;
                        if (itemMeta.hasDisplayName()) {
                            if (itemStack.getType().equals(Material.FILLED_MAP)) {
                                GlowstoneCircuit glowstoneCircuit = GlowstoneCircuit.getByName().get(itemMeta.getDisplayName());
                                if (glowstoneCircuit != null) {
                                    itemMeta.setCustomModelData(glowstoneCircuit.getCustomModelData());
                                    itemStack.setType(Material.GLOWSTONE_DUST);
                                    itemStack.setItemMeta(itemMeta);
                                }
                            } else {
                                if (TardisStaticUtils.isMusicDisk(itemStack)) {
                                    itemMeta.setCustomModelData(10000001);
                                } else if (itemStack.getType().equals(Material.LIME_WOOL)) {
                                    itemMeta.setCustomModelData(86);
                                    itemStack.setType(Material.BOWL);
                                    itemStack.setItemMeta(itemMeta);
                                } else if (itemStack.getType().equals(Material.RED_WOOL)) {
                                    itemMeta.setCustomModelData(87);
                                    itemStack.setType(Material.BOWL);
                                    itemStack.setItemMeta(itemMeta);
                                } else if (itemStack.getType().equals(Material.GLOWSTONE_DUST) && !itemMeta.hasCustomModelData() && itemMeta.getDisplayName().equals("Circuits")) {
                                    itemMeta.setCustomModelData(10001985);
                                }
                                itemStack.setItemMeta(itemMeta);
                            }
                        }
                    }
                }
            } catch (IOException ioException) {
                plugin.debug("Could not get inventory from database! " + ioException);
            }
            // close inventory
            player.closeInventory();
            if (itemStacks != null) {
                // open new inventory
                Inventory inventory = plugin.getServer().createInventory(player, 54, storage.getTitle());
                inventory.setContents(itemStacks);
                player.openInventory(inventory);
            }
        }, 1L);
    }
}
