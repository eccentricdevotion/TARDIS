/*
 * Copyright (C) 2016 eccentric_nz
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDiskWriterCommand {

    private final TARDIS plugin;
    private final List<String> disks = new ArrayList<>();

    public TARDISDiskWriterCommand(TARDIS plugin) {
        this.plugin = plugin;
        this.disks.add("Save Storage Disk");
        this.disks.add("Player Storage Disk");
        this.disks.add("Biome Storage Disk");
        this.disks.add("Preset Storage Disk");
    }

    public boolean writeSave(Player player, String[] args) {
        ItemStack is;
        if (plugin.getDifficulty().equals(DIFFICULTY.MEDIUM)) {
            is = new ItemStack(Material.RECORD_4, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("Save Storage Disk");
            im.setLore(Arrays.asList("Blank"));
            is.setItemMeta(im);
        } else {
            is = player.getInventory().getItemInMainHand();
        }
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (is.getItemMeta().getDisplayName().equals("Save Storage Disk")) {
                List<String> lore = im.getLore();
                if (!lore.get(0).equals("Blank")) {
                    TARDISMessage.send(player, "DISK_ONLY_BLANK");
                    return true;
                }
                if (args.length < 2) {
                    TARDISMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                    TARDISMessage.send(player, "SAVE_NAME_NOT_VALID");
                    return false;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (!rs.resultSet()) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return false;
                } else {
                    Tardis tardis = rs.getTardis();
                    int id = tardis.getTardis_id();
                    PRESET preset = tardis.getPreset();
                    // check has unique name - this will always return false in HARD & MEDIUM difficulty
                    // TODO check for disk lore if MEDIUM difficulty
                    HashMap<String, Object> wherename = new HashMap<>();
                    wherename.put("tardis_id", id);
                    wherename.put("dest_name", args[1]);
                    wherename.put("type", 0);
                    ResultSetDestinations rsd = new ResultSetDestinations(plugin, wherename, false);
                    if (rsd.resultSet()) {
                        TARDISMessage.send(player, "SAVE_EXISTS");
                        return true;
                    }
                    // get current destination
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                        return true;
                    }
                    lore.set(0, args[1]);
                    lore.add(1, rsc.getWorld().getName());
                    lore.add(2, "" + rsc.getX());
                    lore.add(3, "" + rsc.getY());
                    lore.add(4, "" + rsc.getZ());
                    lore.add(5, preset.toString());
                    lore.add(6, rsc.getDirection().toString());
                    lore.add(7, (rsc.isSubmarine()) ? "true" : "false");
                    im.setLore(lore);
                    is.setItemMeta(im);
                    if (plugin.getDifficulty().equals(DIFFICULTY.MEDIUM)) {
                        // save the disk to storage
                        boolean isSpace = saveDiskToStorage(player, is);
                        if (!isSpace) {
                            TARDISMessage.send(player, "SAVE_STORAGE_FULL");
                            return true;
                        }
                    }
                    TARDISMessage.send(player, "DISK_LOC_SAVED");
                    return true;
                }
            }
        }
        return true;
    }

    private boolean saveDiskToStorage(Player player, ItemStack is) {
        UUID uuid = player.getUniqueId();
        // check if they have room for this save disk
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            try {
                ItemStack[] saves1 = TARDISSerializeInventory.itemStacksFromString(rs.getSavesOne());
                int slot = getNextFreeSlot(saves1);
                if (slot != -1) {
                    saves1[slot] = is;
                    String serialized = TARDISSerializeInventory.itemStacksToString(saves1);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("saves_one", serialized);
                    HashMap<String, Object> whereu = new HashMap<>();
                    whereu.put("uuid", uuid.toString());
                    new QueryFactory(plugin).doUpdate("storage", set, whereu);
                    return true;
                } else {
                    ItemStack[] saves2 = TARDISSerializeInventory.itemStacksFromString(rs.getSavesTwo());
                    slot = getNextFreeSlot(saves2);
                    if (slot != -1) {
                        saves2[slot] = is;
                        String serialized = TARDISSerializeInventory.itemStacksToString(saves2);
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("saves_two", serialized);
                        HashMap<String, Object> whereu = new HashMap<>();
                        whereu.put("uuid", uuid.toString());
                        new QueryFactory(plugin).doUpdate("storage", set, whereu);
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (IOException ex) {
                plugin.debug("Could not get save disks: " + ex);
            }
        }
        return false;
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

    public boolean writePlayer(Player player, String[] args) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (is.getItemMeta().getDisplayName().equals("Player Storage Disk")) {
                List<String> lore = im.getLore();
                if (!lore.get(0).equals("Blank")) {
                    TARDISMessage.send(player, "DISK_ONLY_BLANK");
                    return true;
                }
                if (args.length < 2) {
                    TARDISMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                    TARDISMessage.send(player, "PLAYER_NOT_VALID");
                    return false;
                }
                if (player.getName().equalsIgnoreCase(args[1])) {
                    TARDISMessage.send(player, "DISK_NO_SAVE");
                    return true;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (!rs.resultSet()) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return false;
                } else {
                    lore.set(0, args[1]);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    TARDISMessage.send(player, "DISK_PLAYER_SAVED");
                    return true;
                }
            }
        }
        return true;
    }

    public boolean eraseDisk(Player player) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.hasItemMeta() && disks.contains(is.getItemMeta().getDisplayName())) {
            ItemMeta im = is.getItemMeta();
            List<String> lore = Arrays.asList("Blank");
            im.setLore(lore);
            is.setItemMeta(im);
            TARDISMessage.send(player, "DISK_ERASE");
            return true;
        } else {
            TARDISMessage.send(player, "DISK_HAND_ERASE");
            return true;
        }
    }
}
