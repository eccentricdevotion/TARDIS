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
package me.eccentric_nz.tardis.howto;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.listeners.TARDISMenuListener;
import me.eccentric_nz.tardis.rooms.TARDISWalls;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISWallFloorMenuListener extends TARDISMenuListener implements Listener {

    private final TARDISPlugin plugin;
    private final HashMap<UUID, Integer> scroll = new HashMap<>();
    private final List<UUID> scrolling = new ArrayList<>();
    private final ItemStack[][] blocks;
    private final int rows;

    public TARDISWallFloorMenuListener(TARDISPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        rows = TARDISWalls.BLOCKS.size() / 8 + 1;
        blocks = getWallBlocks();
    }

    @EventHandler
    public void onWallFloorMenuOpen(InventoryOpenEvent event) {
        if (event.getView().getTitle().equals(ChatColor.DARK_RED + "tardis Wall & Floor Menu")) {
            Player p = (Player) event.getPlayer();
            scroll.put(p.getUniqueId(), 0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWallFloorMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "tardis Wall & Floor Menu")) {
            Player p = (Player) event.getWhoClicked();
            UUID uuid = p.getUniqueId();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 54) {
                event.setCancelled(true);
                switch (slot) {
                    case 8:
                        // scroll up
                        if (!scrolling.contains(uuid)) {
                            scrolling.add(uuid);
                            scroll(view, scroll.get(uuid) + 1, true, uuid);
                        }
                        break;
                    case 26:
                        // scroll down
                        if (!scrolling.contains(uuid)) {
                            scrolling.add(uuid);
                            scroll(view, scroll.get(uuid) - 1, false, uuid);
                        }
                        break;
                    case 44:
                        // back to seeds
                        back(p);
                        break;
                    case 53:
                        // close
                        close(p);
                        break;
                    default:
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

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    @Override
    public void close(Player p) {
        plugin.getTrackerKeeper().getHowTo().remove(p.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
    }

    /**
     * Goes back to the seeds menu.
     *
     * @param p the player using the GUI
     */
    private void back(Player p) {
        close(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ItemStack[] seeds = new TARDISSeedsInventory(plugin, p).getMenu();
            Inventory gui = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "tardis Seeds Menu");
            gui.setContents(seeds);
            p.openInventory(gui);
        }, 2L);
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
        for (Material entry : TARDISWalls.BLOCKS) {
            ItemStack is = new ItemStack(entry, 1);
            stacks[r][c] = is;
            c++;
            if (c == 8) {
                r++;
                c = 0;
            }
        }
        return stacks;
    }
}
