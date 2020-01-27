/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.element;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ElementGUIListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> scroll = new HashMap<>();
    private final List<UUID> scrolling = new ArrayList<>();
    private final ItemStack[][] blocks;
    private final int rows;

    public ElementGUIListener(TARDIS plugin) {
        this.plugin = plugin;
        rows = Element.values().length / 8 + 1;
        blocks = getWallBlocks();
    }

    @EventHandler
    public void onElementMenuOpen(InventoryOpenEvent event) {
        String name = event.getView().getTitle();
        if (name.equals(ChatColor.DARK_RED + "Atomic elements")) {
            Player p = (Player) event.getPlayer();
            scroll.put(p.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onElementMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Atomic elements")) {
            Player p = (Player) event.getWhoClicked();
            UUID uuid = p.getUniqueId();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                switch (slot) {
                    case 17:
                    case 26:
                    case 44:
                        event.setCancelled(true);
                        break;
                    case 8:
                        // scroll up
                        event.setCancelled(true);
                        if (!scrolling.contains(uuid)) {
                            scrolling.add(uuid);
                            scroll(view, scroll.get(uuid) + 1, true, uuid);
                        }
                        break;
                    case 35:
                        // scroll down
                        event.setCancelled(true);
                        if (!scrolling.contains(uuid)) {
                            scrolling.add(uuid);
                            scroll(view, scroll.get(uuid) - 1, false, uuid);
                        }
                        break;
                    case 53:
                        // close
                        event.setCancelled(true);
                        close(p);
                        break;
                    default:
                        event.setCancelled(true);
                        // get clicked ItemStack
                        ItemStack choice = view.getItem(slot).clone();
                        choice.setAmount(event.getClick().equals(ClickType.SHIFT_LEFT) ? 64 : 1);
                        // add ItemStack to inventory if there is room
                        p.getInventory().addItem(choice);
                        break;
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void scroll(InventoryView view, int row, boolean up, UUID uuid) {
        if ((up && row < (rows - 5)) || (!up && row >= 0)) {
            scroll.put(uuid, row);
            setSlots(view, row, uuid);
        } else {
            scrolling.remove(uuid);
        }
    }

    private void setSlots(InventoryView view, int row, UUID uuid) {
        int slot = 0;
        for (int r = row; r < row + 6; r++) {
            for (int c = 0; c < 8; c++) {
                view.setItem(slot, blocks[r][c]);
                if (slot % 9 == 7) {
                    slot += 2;
                } else {
                    slot++;
                }
            }
        }
        scrolling.remove(uuid);
    }

    private ItemStack[][] getWallBlocks() {
        ItemStack[][] stacks = new ItemStack[rows][8];
        int r = 0;
        int c = 0;
        for (Element entry : Element.values()) {
            ItemStack is = ElementBuilder.getElement(entry);
            stacks[r][c] = is;
            c++;
            if (c == 8) {
                r++;
                c = 0;
            }
        }
        return stacks;
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
