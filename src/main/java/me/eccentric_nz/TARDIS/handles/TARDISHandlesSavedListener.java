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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISHandlesSavedListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> selectedSlot = new HashMap<>();

    public TARDISHandlesSavedListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onHandlesGUIClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISHandlesSavedInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 54) {
            event.setCancelled(true);
        }
        InventoryView view = event.getView();
        if (slot < 36) {
            ItemStack record = player.getItemOnCursor();
            if (!record.getType().isAir()) {
                if (record.getType().equals(Material.MUSIC_DISC_WARD)) {
                    ItemStack disk = view.getItem(slot);
                    if (disk != null && record.isSimilar(disk)) {
                        ItemMeta im = disk.getItemMeta();
                        List<Component> lore = im.lore();
                        // ckeck in
                        int pid = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("checked", 0);
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("program_id", pid);
                        plugin.getQueryFactory().doUpdate("programs", set, where);
                        player.setItemOnCursor(null);
                        lore.set(2, Component.text("Checked IN"));
                        im.lore(lore);
                        disk.setItemMeta(im);
                    }
                }
            } else {
                selectedSlot.put(uuid, slot);
                // add / remove "Selected"
                setSlots(view, slot);
            }
        }
        if (slot == 45) {
            // back to editor
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    player.openInventory(new TARDISHandlesProgramInventory(plugin, 0).getInventory()), 2L);
        }
        if (slot == 47) {
            // load program
            if (selectedSlot.containsKey(uuid)) {
                ItemStack is = view.getItem(selectedSlot.get(uuid));
                int pid = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(is.getItemMeta().lore().get(1)));
                selectedSlot.put(uuid, null);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        player.openInventory(new TARDISHandlesProgramInventory(plugin, pid).getInventory()), 2L);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_SELECT");
            }
        }
        if (slot == 48) {
            // deactivate program
            if (selectedSlot.containsKey(uuid)) {
                ItemStack is = view.getItem(selectedSlot.get(uuid));
                int pid = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(is.getItemMeta().lore().get(1)));
                HashMap<String, Object> where = new HashMap<>();
                where.put("program_id", pid);
                HashMap<String, Object> set = new HashMap<>();
                set.put("parsed", "");
                plugin.getQueryFactory().doUpdate("programs", set, where);
                // update lore
                ItemMeta im = is.getItemMeta();
                List<Component> lore = im.lore();
                lore.remove(3);
                im.lore(lore);
                is.setItemMeta(im);
                selectedSlot.put(uuid, null);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_SELECT");
            }
        }
        if (slot == 49) {
            // delete program
            if (selectedSlot.containsKey(uuid)) {
                ItemStack is = view.getItem(selectedSlot.get(uuid));
                int pid = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(is.getItemMeta().lore().get(1)));
                HashMap<String, Object> where = new HashMap<>();
                where.put("program_id", pid);
                plugin.getQueryFactory().doDelete("programs", where);
                // remove item stack
                event.getClickedInventory().clear(selectedSlot.get(uuid));
                setSlots(view, -1);
                selectedSlot.put(uuid, null);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_SELECT");
            }
        }
        if (slot == 51) {
            // check out program
            if (selectedSlot.containsKey(uuid)) {
                ItemStack is = view.getItem(selectedSlot.get(uuid));
                if (is != null) {
                    ItemMeta im = is.getItemMeta();
                    List<Component> lore = im.lore();
                    if (ComponentUtils.stripColour(lore.get(2)).equals("Checked OUT")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_CHECKED");
                        return;
                    }
                    lore.set(2, Component.text("Checked OUT"));
                    im.lore(lore);
                    is.setItemMeta(im);
                    setSlots(view, -1);
                    selectedSlot.put(uuid, null);
                    ItemStack clone = is.clone();
                    player.getWorld().dropItemNaturally(player.getLocation(), clone);
                    // check out
                    int pid = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("checked", 1);
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("program_id", pid);
                    plugin.getQueryFactory().doUpdate("programs", set, where);
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_SELECT");
            }
        }
        if (slot == 53) {
            // close
            selectedSlot.put(uuid, null);
            close(player);
        }
    }

    /**
     * Sets an ItemStack to the specified inventory slot updating the display name and setting any lore.
     *
     * @param view the inventory to update
     * @param slot the slot number to add a line of lore to
     */
    private void setSlots(InventoryView view, int slot) {
        for (int s = 0; s < 45; s++) {
            ItemStack is = view.getItem(s);
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                List<Component> lore = im.lore();
                if (s == slot && lore != null) {
                    if (lore.contains(Component.text("Selected", NamedTextColor.GREEN))) {
                        if (lore.contains(Component.text("Running", NamedTextColor.AQUA))) {
                            lore.remove(4);
                        } else {
                            lore.remove(3);
                        }
                    } else {
                        lore.add(Component.text("Selected", NamedTextColor.GREEN));
                    }
                }
                im.lore(lore);
                is.setItemMeta(im);
            }
        }
    }
}
