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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.SerializeInventory;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class HandlesProgramListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> scroll_start = new HashMap<>();
    private final HashMap<UUID, List<HandlesBlock>> scroll_list = new HashMap<>();
    private final HashMap<UUID, HandlesCategory> scroll_category = new HashMap<>();
    private final List<Material> allowed = List.of(Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_CAT, Material.MUSIC_DISC_BLOCKS);

    public HandlesProgramListener(TARDIS plugin) {
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
        InventoryView view = event.getView();
        if (!(event.getInventory().getHolder(false) instanceof HandlesProgramInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (!scroll_list.containsKey(uuid)) {
            scroll_list.put(uuid, HandlesBlock.getControls());
            scroll_category.put(uuid, HandlesCategory.CONTROL);
        }
        int slot = event.getRawSlot();
        if (slot >= 0 && slot < 36) {
            // only allow Storage Disks
            ItemStack item = player.getItemOnCursor();
            if (!allowed.contains(item.getType())) {
                event.setCancelled(true);
            }
        }
        if (slot < 0 || (slot > 35 && slot < 54)) {
            event.setCancelled(true);
        }
        switch (slot) {
            case 36 -> {
                // set control blocks
                scroll_list.put(uuid, HandlesBlock.getControls());
                setList(uuid, HandlesCategory.CONTROL, view);
                scroll_start.put(uuid, 0);
            }
            case 37 -> {
                // set operator blocks
                scroll_list.put(uuid, HandlesBlock.getOperators());
                setList(uuid, HandlesCategory.OPERATOR, view);
                scroll_start.put(uuid, 0);
            }
            case 38 -> {
                // set variable blocks
                scroll_list.put(uuid, HandlesBlock.getVariables());
                setList(uuid, HandlesCategory.VARIABLE, view);
                scroll_start.put(uuid, 0);
            }
            case 39 -> {
                // set number blocks
                scroll_list.put(uuid, HandlesBlock.getNumbers());
                setList(uuid, HandlesCategory.NUMBER, view);
                scroll_start.put(uuid, 0);
            }
            case 40 -> {
                // set event blocks
                scroll_list.put(uuid, HandlesBlock.getEvents());
                setList(uuid, HandlesCategory.EVENT, view);
                scroll_start.put(uuid, 0);
            }
            case 41 -> {
                // set command blocks
                scroll_list.put(uuid, HandlesBlock.getCommands());
                setList(uuid, HandlesCategory.COMMAND, view);
                scroll_start.put(uuid, 0);
            }
            case 42 -> {
                // set selector blocks
                scroll_list.put(uuid, HandlesBlock.getSelectors());
                setList(uuid, HandlesCategory.SELECTOR, view);
                scroll_start.put(uuid, 0);
            }
            case 43 ->
                // go to saved disks
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            player.openInventory(new HandlesSavedInventory(plugin, uuid.toString()).getInventory()), 2L);
            case 44 -> {
                // save program
                int pid = saveDisk(view, uuid.toString(), player);
                if (pid != -1) {
                    close(player);
                    ItemStack is = ItemStack.of(Material.MUSIC_DISC_WARD, 1);
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.text("Handles Program Disk"));
                    im.lore(List.of(Component.text("Untitled Disk"), Component.text(pid), Component.text("Checked OUT")));
                    im.addItemFlags(ItemFlag.values());
                    is.setItemMeta(im);
                    player.getWorld().dropItemNaturally(player.getLocation(), is);
                    plugin.getMessenger().sendColouredCommand(player, "HANDLES_SAVED", "/tardishandles disk [name]", plugin);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_NOTHING");
                }
            }
            case 45, 46, 47, 48, 49, 50, 51 -> {
                // duplicate Item stack on cursor
                ItemStack is = view.getItem(slot);
                ItemStack cursor = player.getItemOnCursor();
                if (is == null || cursor.isSimilar(is)) {
                    player.setItemOnCursor(null);
                } else {
                    player.setItemOnCursor(is.clone());
                }
                if (is != null) {
                    is.setAmount(1);
                }
            }
            case 52 -> {
                // scroll left
                if (scroll_category.get(uuid).getSize() > 7) {
                    int startl;
                    int max = scroll_list.get(uuid).size() - 7;
                    if (scroll_start.containsKey(uuid)) {
                        startl = scroll_start.get(uuid) + 1;
                        if (startl >= max) {
                            startl = max;
                        }
                    } else {
                        startl = 1;
                    }
                    scroll_start.put(uuid, startl);
                    for (int i = 0; i < 7; i++) {
                        // setSlot(Inventory inv, int slot, TARDISHandlesBlock block)
                        setSlot(view, (45 + i), scroll_list.get(uuid).get(startl + i));
                    }
                }
            }
            case 53 -> {
                // scroll right
                if (scroll_category.get(uuid).getSize() > 7) {
                    int startr;
                    if (scroll_start.containsKey(uuid)) {
                        startr = scroll_start.get(uuid) - 1;
                        if (startr <= 0) {
                            startr = 0;
                        }
                    } else {
                        startr = 0;
                    }
                    scroll_start.put(uuid, startr);
                    for (int i = 0; i < 7; i++) {
                        // setSlot(Inventory inv, int slot, TARDISHandlesBlock block)
                        setSlot(view, (45 + i), scroll_list.get(uuid).get(startr + i));
                    }
                }
            }
            default -> {
                ItemStack item = player.getItemOnCursor();
                if (slot > 53 && item.getType().equals(Material.PAPER)) {
                    event.setCancelled(true);
                    player.setItemOnCursor(null);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHandlesProgramClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof HandlesProgramInventory)) {
            return;
        }
        Player p = (Player) event.getPlayer();
        ItemStack item = p.getItemOnCursor();
        if (item.getType().equals(Material.PAPER)) {
            p.setItemOnCursor(null);
        }
    }

    /**
     * Sets an ItemStack to the specified inventory slot updating the display name and setting any lore.
     *
     * @param view  the inventory to update
     * @param slot  the slot number to update
     * @param block the program block
     */
    private void setSlot(InventoryView view, int slot, HandlesBlock block) {
        ItemStack is = ItemStack.of(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(block.getDisplayName()));
        if (block.getLore() != null) {
            im.lore(block.getLore());
        }
        is.setItemMeta(im);
        view.setItem(slot, is);
    }

    private void setList(UUID uuid, HandlesCategory category, InventoryView view) {
        scroll_category.put(uuid, category);
        for (int i = 0; i < 7; i++) {
            if (i < category.getSize()) {
                setSlot(view, (45 + i), scroll_list.get(uuid).get(i));
            } else {
                view.setItem(45 + i, null);
            }
        }
    }

    private int saveDisk(InventoryView view, String uuid, Player player) {
        int pid = -1;
        ItemStack[] stack = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            if (view.getItem(i) != null) {
                stack[i] = view.getItem(i);
                pid++;
            }
        }
        // there should be a minimum size for a valid program e.g. selector + command
        if (pid > 1) {
            // validate the program
            HandlesValidator validator = new HandlesValidator(plugin, stack, player);
            if (validator.validateDisk()) {
                String serialized = SerializeInventory.itemStacksToString(stack);
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", uuid);
                set.put("name", "Untitled Disk");
                set.put("inventory", serialized);
                set.put("checked", 1);
                pid = plugin.getQueryFactory().doSyncInsert("programs", set);
            } else {
                // not valid so reset pid
                pid = -1;
            }
        }
        return pid;
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }
}
