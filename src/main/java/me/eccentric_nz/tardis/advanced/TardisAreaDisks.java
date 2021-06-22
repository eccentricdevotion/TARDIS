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
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.data.Area;
import me.eccentric_nz.tardis.database.resultset.ResultSetAreas;
import me.eccentric_nz.tardis.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.tardis.enumeration.Storage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * The visual stabiliser circuit controlled the TARDIS's outward appearance. Its removal rendered the ship invisible.
 *
 * @author eccentric_nz
 */
class TardisAreaDisks {

    private final TardisPlugin plugin;

    TardisAreaDisks(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Makes an array of item stacks containing the default Storage GUI top and any area storage disks the player has
     * permission for.
     *
     * @param player the player to create the array for
     * @return an array of item stacks
     */
    ItemStack[] makeDisks(Player player) {

        List<ItemStack> areas = new ArrayList<>();
        // get the areas this player has access to
        ResultSetAreas resultSetAreas = new ResultSetAreas(plugin, null, false, false);
        if (resultSetAreas.resultSet()) {
            // cycle through areas
            resultSetAreas.getData().forEach((area) -> {
                String name = area.getAreaName();
                if (TardisPermission.hasPermission(player, "tardis.area." + name) || TardisPermission.hasPermission(player, "tardis.area.*")) {
                    ItemStack itemStack = new ItemStack(Material.MUSIC_DISC_BLOCKS, 1);
                    ItemMeta itemMeta = itemStack.getItemMeta(); // fixme
                    assert itemMeta != null;
                    itemMeta.setDisplayName("Area Storage Disk");
                    List<String> lore = new ArrayList<>();
                    lore.add(name);
                    lore.add(area.getWorld());
                    itemMeta.setLore(lore);
                    itemMeta.setCustomModelData(10000001);
                    itemStack.setItemMeta(itemMeta);
                    areas.add(itemStack);
                }
            });
        }
        ItemStack[] itemStacks = new ItemStack[54];
        // set default top slots
        try {
            itemStacks = TardisInventorySerializer.itemStacksFromString(Storage.AREA.getEmpty());
        } catch (IOException ioException) {
            plugin.debug("Could not get make Area Disk Inventory: " + ioException);
        }
        // set saved slots
        int i = 27;
        for (ItemStack area : areas) {
            itemStacks[i] = area;
            i++;
        }
        return itemStacks;
    }

    /**
     * Checks the player's current area disks and adds any new ones they have permission for.
     *
     * @param player the player to check for
     * @return a serialized String
     */
    String checkDisksForNewAreas(Player player) {
        String serialized = "";
        // get the player's storage record
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetDiskStorage resultSetDiskStorage = new ResultSetDiskStorage(plugin, where);
        if (resultSetDiskStorage.resultSet()) {
            List<String> playerHas = new ArrayList<>();
            String serializedAreas = resultSetDiskStorage.getAreas();
            try {
                // check storage inventory
                ItemStack[] areas = TardisInventorySerializer.itemStacksFromString(serializedAreas);
                for (ItemStack area : areas) {
                    if (area != null && area.getType().equals(Material.MUSIC_DISC_BLOCKS) && area.hasItemMeta()) {
                        ItemMeta areaItemMeta = area.getItemMeta();
                        assert areaItemMeta != null;
                        if (areaItemMeta.hasLore()) {
                            playerHas.add(Objects.requireNonNull(areaItemMeta.getLore()).get(0));
                        }
                    }
                }
                // check console inventory
                ItemStack[] consoleItems = TardisInventorySerializer.itemStacksFromString(resultSetDiskStorage.getConsole());
                for (ItemStack consoleItem : consoleItems) {
                    if (consoleItem != null && consoleItem.getType().equals(Material.MUSIC_DISC_BLOCKS) && consoleItem.hasItemMeta()) {
                        ItemMeta consoleItemMeta = consoleItem.getItemMeta();
                        assert consoleItemMeta != null;
                        if (consoleItemMeta.hasLore()) {
                            playerHas.add(Objects.requireNonNull(consoleItemMeta.getLore()).get(0));
                        }
                    }
                }
                // check player inventory
                ItemStack[] playerItems = player.getInventory().getContents();
                for (ItemStack playerItem : playerItems) {
                    if (playerItem != null && playerItem.getType().equals(Material.MUSIC_DISC_BLOCKS) && playerItem.hasItemMeta()) {
                        ItemMeta playerItemMeta = playerItem.getItemMeta();
                        assert playerItemMeta != null;
                        if (playerItemMeta.hasLore()) {
                            playerHas.add(Objects.requireNonNull(playerItemMeta.getLore()).get(0));
                        }
                    }
                }
                Inventory inventory = plugin.getServer().createInventory(player, 54);
                inventory.setContents(areas);
                ResultSetAreas resultSetAreas = new ResultSetAreas(plugin, null, true, false);
                int count = 0;
                if (resultSetAreas.resultSet()) {
                    // cycle through areas
                    for (Area map : resultSetAreas.getData()) {
                        String name = map.getAreaName();
                        if ((!playerHas.contains(name) && TardisPermission.hasPermission(player, "tardis.area." + name)) || (!playerHas.contains(name) && TardisPermission.hasPermission(player, "tardis.area.*"))) {
                            // add new area if there is room
                            int nextEmptySlot = getNextEmptySlot(inventory);
                            if (nextEmptySlot != -1) {
                                ItemStack itemStack = new ItemStack(Material.MUSIC_DISC_BLOCKS, 1);
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                assert itemMeta != null;
                                itemMeta.setDisplayName("Area Storage Disk");
                                List<String> lore = new ArrayList<>();
                                lore.add(name);
                                lore.add(map.getWorld());
                                itemMeta.setLore(lore);
                                itemMeta.setCustomModelData(10000001);
                                itemStack.setItemMeta(itemMeta);
                                inventory.setItem(nextEmptySlot, itemStack);
                                count++;
                            }
                        }
                    }
                }
                // return the serialized string
                if (count > 0) {
                    return TardisInventorySerializer.itemStacksToString(inventory.getContents());
                } else {
                    return serializedAreas;
                }
            } catch (IOException ioException) {
                plugin.debug("Could not get NEW Area Disk Inventory: " + ioException);
            }
        }
        return serialized;
    }

    /**
     * Finds the first empty slot greater than 27.
     *
     * @param inventory the inventory to search
     * @return the empty slot number or -1 if not found
     */
    int getNextEmptySlot(Inventory inventory) {
        for (int i = 27; i < 54; i++) {
            if (inventory.getItem(i) == null || Objects.requireNonNull(inventory.getItem(i)).getType().isAir()) {
                return i;
            }
        }
        return -1;
    }
}
