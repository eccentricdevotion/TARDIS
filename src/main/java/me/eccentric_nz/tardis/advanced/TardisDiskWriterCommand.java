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
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

/**
 * @author eccentric_nz
 */
public class TardisDiskWriterCommand {

    private final TardisPlugin plugin;
    private final List<String> disks = new ArrayList<>();

    public TardisDiskWriterCommand(TardisPlugin plugin) {
        this.plugin = plugin;
        disks.add("Save Storage Disk");
        disks.add("Player Storage Disk");
        disks.add("Biome Storage Disk");
        disks.add("Preset Storage Disk");
    }

    public boolean writeSave(Player player, String[] args) {
        ItemStack itemStack;
        if (plugin.getDifficulty().equals(Difficulty.MEDIUM)) {
            itemStack = new ItemStack(Material.MUSIC_DISC_CHIRP, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName("Save Storage Disk");
            itemMeta.setLore(Collections.singletonList("Blank"));
            itemMeta.setCustomModelData(10000001);
            itemStack.setItemMeta(itemMeta);
        } else {
            itemStack = player.getInventory().getItemInMainHand();
        }
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Save Storage Disk")) {
                List<String> lore = itemMeta.getLore();
                assert lore != null;
                if (!lore.get(0).equals("Blank")) {
                    TardisMessage.send(player, "DISK_ONLY_BLANK");
                    return true;
                }
                if (args.length < 2) {
                    TardisMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                if (!args[1].matches("[A-Za-z0-9_*]{2,16}")) {
                    TardisMessage.send(player, "SAVE_NAME_NOT_VALID");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis resultSetTardis = new ResultSetTardis(plugin, where, "", false, 0);
                if (!resultSetTardis.resultSet()) {
                    TardisMessage.send(player, "NO_TARDIS");
                    return false;
                } else {
                    Tardis tardis = resultSetTardis.getTardis();
                    int id = tardis.getTardisId();
                    Preset preset = tardis.getPreset();
                    // check has unique name - this will always return false in HARD & MEDIUM difficulty
                    // TODO Check for disk lore if MEDIUM difficulty
                    HashMap<String, Object> whereName = new HashMap<>();
                    whereName.put("tardis_id", id);
                    whereName.put("dest_name", args[1]);
                    whereName.put("type", 0);
                    ResultSetDestinations resultSetDestinations = new ResultSetDestinations(plugin, whereName, false);
                    if (resultSetDestinations.resultSet()) {
                        TardisMessage.send(player, "SAVE_EXISTS");
                        return true;
                    }
                    // get current destination
                    HashMap<String, Object> whereCurrentLocation = new HashMap<>();
                    whereCurrentLocation.put("tardis_id", id);
                    ResultSetCurrentLocation resultSetCurrentLocation = new ResultSetCurrentLocation(plugin, whereCurrentLocation);
                    if (!resultSetCurrentLocation.resultSet()) {
                        TardisMessage.send(player, "CURRENT_NOT_FOUND");
                        return true;
                    }
                    lore.set(0, args[1]);
                    lore.add(1, resultSetCurrentLocation.getWorld().getName());
                    lore.add(2, "" + resultSetCurrentLocation.getX());
                    lore.add(3, "" + resultSetCurrentLocation.getY());
                    lore.add(4, "" + resultSetCurrentLocation.getZ());
                    lore.add(5, preset.toString());
                    lore.add(6, resultSetCurrentLocation.getDirection().toString());
                    lore.add(7, (resultSetCurrentLocation.isSubmarine()) ? "true" : "false");
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);
                    if (plugin.getDifficulty().equals(Difficulty.MEDIUM)) {
                        // save the disk to storage
                        boolean isSpace = saveDiskToStorage(player, itemStack);
                        if (!isSpace) {
                            TardisMessage.send(player, "SAVE_STORAGE_FULL");
                            return true;
                        }
                    }
                    TardisMessage.send(player, "DISK_LOC_SAVED");
                    return true;
                }
            }
        }
        return true;
    }

    private boolean saveDiskToStorage(Player player, ItemStack itemStack) {
        UUID uuid = player.getUniqueId();
        // check if they have room for this save disk
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetDiskStorage resultSetDiskStorage = new ResultSetDiskStorage(plugin, where);
        if (resultSetDiskStorage.resultSet()) {
            try {
                ItemStack[] saves1 = TardisInventorySerializer.itemStacksFromString(resultSetDiskStorage.getSavesOne());
                int slot = getNextFreeSlot(saves1);
                if (slot != -1) {
                    saves1[slot] = itemStack;
                    String serialized = TardisInventorySerializer.itemStacksToString(saves1);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("saves_one", serialized);
                    HashMap<String, Object> whereUuid = new HashMap<>();
                    whereUuid.put("uuid", uuid.toString());
                    plugin.getQueryFactory().doUpdate("storage", set, whereUuid);
                    return true;
                } else {
                    ItemStack[] saves2 = TardisInventorySerializer.itemStacksFromString(resultSetDiskStorage.getSavesTwo());
                    slot = getNextFreeSlot(saves2);
                    if (slot != -1) {
                        saves2[slot] = itemStack;
                        String serialized = TardisInventorySerializer.itemStacksToString(saves2);
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("saves_two", serialized);
                        HashMap<String, Object> whereUuid = new HashMap<>();
                        whereUuid.put("uuid", uuid.toString());
                        plugin.getQueryFactory().doUpdate("storage", set, whereUuid);
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (IOException ioException) {
                plugin.debug("Could not get save disks: " + ioException);
            }
        }
        return false;
    }

    private int getNextFreeSlot(ItemStack[] itemStacks) {
        int slot = -1;
        for (int s = 27; s < itemStacks.length; s++) {
            ItemStack disk = itemStacks[s];
            if (disk == null) {
                slot = s;
                break;
            }
        }
        return slot;
    }

    public boolean writePlayer(Player player, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Player Storage Disk")) {
                List<String> lore = itemMeta.getLore();
                assert lore != null;
                if (!lore.get(0).equals("Blank")) {
                    TardisMessage.send(player, "DISK_ONLY_BLANK");
                    return true;
                }
                if (args.length < 2) {
                    TardisMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                if (!args[1].matches("[A-Za-z0-9_*]{2,16}")) {
                    TardisMessage.send(player, "PLAYER_NOT_VALID");
                    return false;
                }
                if (player.getName().equalsIgnoreCase(args[1])) {
                    TardisMessage.send(player, "DISK_NO_SAVE");
                    return true;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis resultSetTardis = new ResultSetTardis(plugin, where, "", false, 0);
                if (!resultSetTardis.resultSet()) {
                    TardisMessage.send(player, "NO_TARDIS");
                    return false;
                } else {
                    lore.set(0, args[1]);
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);
                    TardisMessage.send(player, "DISK_PLAYER_SAVED");
                    return true;
                }
            }
        }
        return true;
    }

    public boolean eraseDisk(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.hasItemMeta() && disks.contains(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName())) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = Collections.singletonList("Blank");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            TardisMessage.send(player, "DISK_ERASE");
        } else {
            TardisMessage.send(player, "DISK_HAND_ERASE");
        }
        return true;
    }

    public boolean writeSaveToControlDisk(Player player, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("Authorised Control Disk") && itemMeta.getPersistentDataContainer().has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUuid())) {
                if (args.length < 2) {
                    TardisMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                ResultSetTardisId resultSetTardisId = new ResultSetTardisId(plugin);
                if (!resultSetTardisId.fromUuid(player.getUniqueId().toString())) {
                    TardisMessage.send(player, "NO_TARDIS");
                    return false;
                } else {
                    String save;
                    if (args[1].equalsIgnoreCase("home")) {
                        save = "Home";
                    } else {
                        HashMap<String, Object> whereName = new HashMap<>();
                        whereName.put("tardis_id", resultSetTardisId.getTardisId());
                        whereName.put("dest_name", args[1]);
                        whereName.put("type", 0);
                        ResultSetDestinations resultSetDestinations = new ResultSetDestinations(plugin, whereName, false);
                        if (!resultSetDestinations.resultSet()) {
                            TardisMessage.send(player, "SAVE_NOT_FOUND", ChatColor.GREEN + "/tardis list saves" + ChatColor.RESET);
                            return true;
                        }
                        save = args[1];
                    }
                    List<String> lore = itemMeta.getLore();
                    assert lore != null;
                    lore.add(save);
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);
                    TardisMessage.send(player, "DISK_LOC_SAVED");
                    return true;
                }
            }
        }
        return true;
    }
}
