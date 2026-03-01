/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author eccentric_nz
 */
public class DiskWriterCommand {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_*]{2,16}");
    private final TARDIS plugin;
    private final List<String> disks = new ArrayList<>();

    public DiskWriterCommand(TARDIS plugin) {
        this.plugin = plugin;
        disks.add("Save Storage Disk");
        disks.add("Player Storage Disk");
        disks.add("Biome Storage Disk");
        disks.add("Preset Storage Disk");
    }

    public void writeSave(Player player, String name) {
        ItemStack is;
        boolean makeAndSaveDisk = !plugin.getConfig().getBoolean("difficulty.disk_in_hand_for_write");
        if (makeAndSaveDisk) {
            is = ItemStack.of(Material.MUSIC_DISC_CHIRP, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text("Save Storage Disk"));
            im.lore(List.of(Component.text("Blank")));
            is.setItemMeta(im);
        } else {
            is = player.getInventory().getItemInMainHand();
        }
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Save Storage Disk")) {
                List<Component> lore = im.lore();
                if (!ComponentUtils.stripColour(lore.getFirst()).equals("Blank")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_ONLY_BLANK");
                    return;
                }
                if (!LETTERS_NUMBERS.matcher(name).matches()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NAME_NOT_VALID");
                    return;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                } else {
                    Tardis tardis = rs.getTardis();
                    int id = tardis.getTardisId();
                    ChameleonPreset preset = tardis.getPreset();
                    // check has unique name - this will always return false in HARD & MEDIUM difficulty
                    HashMap<String, Object> wherename = new HashMap<>();
                    wherename.put("tardis_id", id);
                    wherename.put("dest_name", name);
                    wherename.put("type", 0);
                    ResultSetDestinations rsd = new ResultSetDestinations(plugin, wherename, false);
                    if (rsd.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_EXISTS");
                        return;
                    }
                    // get current destination
                    ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                    if (!rsc.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                        return;
                    }
                    Current current = rsc.getCurrent();
                    lore.set(0, Component.text(name));
                    lore.add(1, Component.text(current.location().getWorld().getName()));
                    lore.add(2, Component.text(current.location().getBlockX()));
                    lore.add(3, Component.text(current.location().getBlockY()));
                    lore.add(4, Component.text(current.location().getBlockZ()));
                    lore.add(5, Component.text(preset.toString()));
                    lore.add(6, Component.text(current.direction().toString()));
                    lore.add(7, Component.text((current.submarine()) ? "true" : "false"));
                    im.lore(lore);
                    is.setItemMeta(im);
                    if (makeAndSaveDisk) {
                        // save the disk to storage
                        boolean isSpace = saveDiskToStorage(player, is);
                        if (!isSpace) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_STORAGE_FULL");
                            return;
                        }
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_LOC_SAVED");
                }
            }
        }
    }

    private boolean saveDiskToStorage(Player player, ItemStack is) {
        UUID uuid = player.getUniqueId();
        // check if they have room for this save disk
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            try {
                ItemStack[] saves1 = SerializeInventory.itemStacksFromString(rs.getSavesOne());
                int slot = getNextFreeSlot(saves1);
                if (slot != -1) {
                    saves1[slot] = is;
                    String serialized = SerializeInventory.itemStacksToString(saves1);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("saves_one", serialized);
                    HashMap<String, Object> whereu = new HashMap<>();
                    whereu.put("uuid", uuid.toString());
                    plugin.getQueryFactory().doUpdate("storage", set, whereu);
                    return true;
                } else {
                    ItemStack[] saves2 = SerializeInventory.itemStacksFromString(rs.getSavesTwo());
                    slot = getNextFreeSlot(saves2);
                    if (slot != -1) {
                        saves2[slot] = is;
                        String serialized = SerializeInventory.itemStacksToString(saves2);
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("saves_two", serialized);
                        HashMap<String, Object> whereu = new HashMap<>();
                        whereu.put("uuid", uuid.toString());
                        plugin.getQueryFactory().doUpdate("storage", set, whereu);
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

    public void writePlayer(Player player, Player target) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Player Storage Disk")) {
                List<Component> lore = im.lore();
                if (!ComponentUtils.stripColour(lore.getFirst()).equals("Blank")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_ONLY_BLANK");
                    return;
                }
                if (player.getName().equalsIgnoreCase(target.getName())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_NO_SAVE");
                    return;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                } else {
                    lore.set(0, Component.text(target.getName()));
                    im.lore(lore);
                    is.setItemMeta(im);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_PLAYER_SAVED");
                }
            }
        }
    }

    public void eraseDisk(Player player) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.hasItemMeta() && disks.contains(ComponentUtils.stripColour(is.getItemMeta().displayName()))) {
            ItemMeta im = is.getItemMeta();
            im.lore(List.of(Component.text("Blank")));
            is.setItemMeta(im);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_ERASE");
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_HAND_ERASE");
        }
    }

    public void writeSaveToControlDisk(Player player, String name) {
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Authorised Control Disk")
                    && im.getPersistentDataContainer().has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID())) {
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (!rs.fromUUID(player.getUniqueId().toString())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                } else {
                    String save;
                    if (name.equalsIgnoreCase("home")) {
                        save = "Home";
                    } else {
                        HashMap<String, Object> wherename = new HashMap<>();
                        wherename.put("tardis_id", rs.getTardisId());
                        wherename.put("dest_name", name);
                        wherename.put("type", 0);
                        ResultSetDestinations rsd = new ResultSetDestinations(plugin, wherename, false);
                        if (!rsd.resultSet()) {
                            plugin.getMessenger().sendColouredCommand(player, "SAVE_NOT_FOUND", "/tardis list saves", plugin);
                            return;
                        }
                        save = name;
                    }
                    List<Component> lore = im.lore();
                    lore.add(Component.text(save));
                    im.lore(lore);
                    is.setItemMeta(im);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_LOC_SAVED");
                }
            }
        }
    }
}
