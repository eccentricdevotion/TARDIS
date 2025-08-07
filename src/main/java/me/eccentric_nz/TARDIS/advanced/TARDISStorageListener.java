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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProgramInventory;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Banshee Circuits were components of TARDISes, emergency defence mechanisms used as a last resort when all other
 * systems had failed. They allowed a TARDIS to use whatever resources were available to ensure the survival of the ship
 * and its crew.
 *
 * @author eccentric_nz
 */
public class TARDISStorageListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<Material> onlythese = new ArrayList<>();

    public TARDISStorageListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        for (DiskCircuit dc : DiskCircuit.values()) {
            if (!onlythese.contains(dc.getMaterial())) {
                onlythese.add(dc.getMaterial());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDiskStorageClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        if (event.getInventory().getHolder(false) instanceof TARDISStorageInventory) {
            // which inventory screen is it?
            String title = ComponentUtils.stripColour(view.title());
            String[] split = title.split(" ");
            String tmp = split[0].toUpperCase(Locale.ROOT);
            if (split.length > 2) {
                tmp = tmp + "_" + split[2];
            }
            Storage store = Storage.valueOf(tmp);
            saveCurrentStorage(event.getInventory(), store.getColumn(), (Player) event.getPlayer());
        } else if (!(event.getInventory().getHolder(false) instanceof TARDISAdvancedConsoleInventory)
                && !(event.getInventory().getHolder(false) instanceof TARDISHandlesProgramInventory)) {
            // scan the inventory for area disks and spit them out
            for (int i = 0; i < event.getInventory().getSize(); i++) {
                ItemStack stack = view.getItem(i);
                if (stack == null || !stack.getType().equals(Material.MUSIC_DISC_BLOCKS) || !stack.hasItemMeta()) {
                    return;
                }
                ItemMeta ims = stack.getItemMeta();
                if (!ims.hasDisplayName() || !ComponentUtils.endsWith(ims.displayName(), "Area Storage Disk")) {
                    return;
                }
                Player p = (Player) event.getPlayer();
                Location loc = p.getLocation();
                loc.getWorld().dropItemNaturally(loc, stack);
                view.setItem(i, ItemStack.of(Material.AIR));
                plugin.getMessenger().send(p, TardisModule.TARDIS, "ADV_NO_STORE");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDiskStorageInteract(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISStorageInventory)) {
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
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (!rs.resultSet()) {
            return;
        }
        // which inventory screen is it?
        String title = ComponentUtils.stripColour(event.getView().title());
        String[] split = title.split(" ");
        String tmp = split[0].toUpperCase(Locale.ROOT);
        if (split.length > 2) {
            tmp = tmp + "_" + split[2];
        }
        Storage store = Storage.valueOf(tmp);
        if ((slot >= 0 && slot < 6) || slot == 18 || slot == 26) {
            saveCurrentStorage(event.getClickedInventory(), store.getColumn(), player);
        }
        String[] versions = rs.getVersions().split(",");
        switch (slot) {
            case 0 -> {
                if (!store.equals(Storage.SAVE_1)) {
                    // switch to saves
                    loadInventory(rs.getSavesOne(), player, Storage.SAVE_1, versions);
                }
            }
            case 1 -> {
                if (!store.equals(Storage.AREA)) {
                    // switch to areas
                    loadInventory(rs.getAreas(), player, Storage.AREA, versions);
                }
            }
            case 2 -> {
                if (!store.equals(Storage.PLAYER)) {
                    // switch to players
                    loadInventory(rs.getPlayers(), player, Storage.PLAYER, versions);
                }
            }
            case 3 -> {
                if (!store.equals(Storage.BIOME_1)) {
                    // switch to biomes
                    loadInventory(rs.getBiomesOne(), player, Storage.BIOME_1, versions);
                }
            }
            case 4 -> {
                if (!store.equals(Storage.PRESET_1)) {
                    // switch to presets
                    loadInventory(rs.getPresetsOne(), player, Storage.PRESET_1, versions);
                }
            }
            case 5 -> {
                if (!store.equals(Storage.CIRCUIT)) {
                    // switch to circuits
                    loadInventory(rs.getCircuits(), player, Storage.CIRCUIT, versions);
                }
            }
            default -> {
            }
        }
        switch (store) {
            case BIOME_1 -> {
                if (slot == 26) { // switch to biome 2
                    loadInventory(rs.getBiomesTwo(), player, Storage.BIOME_2, versions);
                }
            }
            case BIOME_2 -> {
                if (slot == 18) { // switch to biome 1
                    loadInventory(rs.getBiomesOne(), player, Storage.BIOME_1, versions);
                }
            }
            case PRESET_1 -> {
                if (slot == 26) { // switch to preset 2
                    loadInventory(rs.getPresetsTwo(), player, Storage.PRESET_2, versions);
                }
            }
            case PRESET_2 -> {
                if (slot == 18) { // switch to preset 1
                    loadInventory(rs.getPresetsOne(), player, Storage.PRESET_1, versions);
                }
            }
            case SAVE_1 -> {
                if (slot == 26) { // switch to save 2
                    loadInventory(rs.getSavesTwo(), player, Storage.SAVE_2, versions);
                }
            }
            case SAVE_2 -> {
                if (slot == 18) { // switch to save 1
                    loadInventory(rs.getSavesOne(), player, Storage.SAVE_1, versions);
                }
            }
            default -> { // no extra pages
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropAreaDisk(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack is = event.getItemDrop().getItemStack();
        if (!is.getType().equals(Material.MUSIC_DISC_BLOCKS) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im == null || !im.hasDisplayName() || !ComponentUtils.endsWith(im.displayName(), "Area Storage Disk")) {
            return;
        }
        event.setCancelled(true);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ADV_NO_DROP");
    }

    private void saveCurrentStorage(Inventory inv, String column, Player p) {
        // loop through inventory contents and remove any items that are not disks or circuits
        for (int i = 27; i < 54; i++) {
            ItemStack is = inv.getItem(i);
            if (is != null) {
                if (!onlythese.contains(is.getType())) {
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), is);
                    inv.setItem(i, ItemStack.of(Material.AIR));
                }
            }
        }
        String serialized = TARDISSerializeInventory.itemStacksToString(inv.getContents());
        HashMap<String, Object> set = new HashMap<>();
        set.put(column, serialized);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        plugin.getQueryFactory().doUpdate("storage", set, where);
    }

    private void loadInventory(String serialized, Player p, Storage s, String[] versions) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ItemStack[] stack = null;
            try {
                if (!serialized.isEmpty()) {
                    if (s == Storage.AREA) {
                        if (versions[0].equals("0")) {
                            stack = TARDISStorageConverter.updateDisks(new TARDISAreaDisks(plugin).checkDisksForNewAreas(p));
                            versions[0] = "1";
                            updateVersions(versions, p.getUniqueId().toString());
                        } else {
                            stack = TARDISSerializeInventory.itemStacksFromString(new TARDISAreaDisks(plugin).checkDisksForNewAreas(p));
                        }
                    } else if (s == Storage.CIRCUIT) {
                        int version = TARDISNumberParsers.parseInt(versions[3]);
                        if (version < 2) {
                            stack = TARDISStorageConverter.updateCircuits(serialized);
                            versions[3] = version == 0 ? "1" : "2";
                            updateVersions(versions, p.getUniqueId().toString());
                        } else {
                            stack = TARDISSerializeInventory.itemStacksFromString(serialized);
                        }
                    } else {
                        if (versions[s.ordinal()].equals("0")) {
                            stack = TARDISStorageConverter.updateDisks(serialized);
                            versions[s.ordinal()] = "1";
                            updateVersions(versions, p.getUniqueId().toString());
                        } else {
                            stack = TARDISSerializeInventory.itemStacksFromString(serialized);
                        }
                    }
                } else if (s.equals(Storage.AREA)) {
                    stack = new TARDISAreaDisks(plugin).makeDisks(p);
                } else {
                    stack = TARDISSerializeInventory.itemStacksFromString(s.getEmpty());
                }
            } catch (IOException ex) {
                plugin.debug("Could not get inventory from database! " + ex);
            }
            // close inventory
            p.closeInventory();
            if (stack != null) {
                // open new inventory
                p.openInventory(new TARDISStorageInventory(plugin, s.getTitle(), stack).getInventory());
            }
        }, 1L);
    }

    private void updateVersions(String[] versions, String uuid) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("versions", String.join(",", versions));
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        plugin.getQueryFactory().doSyncUpdate("storage", set, where);
    }
}
