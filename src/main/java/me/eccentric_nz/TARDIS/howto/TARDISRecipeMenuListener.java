/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.howto;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A control room's look could be changed over time. The process by which an
 * operator could transform a control room was fairly simple, once compared by
 * the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISRecipeMenuListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISRecipeMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSeedMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS Seed Recipe")) {
            final Player p = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                final ItemStack is = inv.getItem(slot);
                if (is != null) {
                    event.setCancelled(true);
                    switch (slot) {
                        case 8:
                            // back to seeds
                            close(p);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    ItemStack[] seeds = new TARDISSeedsInventory(p).getMenu();
                                    Inventory gui = plugin.getServer().createInventory(p, 18, "§4TARDIS Seeds Menu");
                                    gui.setContents(seeds);
                                    p.openInventory(gui);
                                }
                            }, 2L);
                            break;
                        case 11:
                        case 20:
                            // wall & floor
                            close(p);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    ItemStack[] recipe = new TARDISHowtoWallsInventory(plugin).getMenu();
                                    Inventory gui = plugin.getServer().createInventory(p, 54, "§4TARDIS Wall & Floor Menu");
                                    gui.setContents(recipe);
                                    p.openInventory(gui);
                                }
                            }, 2L);
                            break;
                        case 19:
                            // chameleon
                            close(p);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    ItemStack[] chameleon = new TARDISChameleonInventory(plugin).getMenu();
                                    Inventory gui = plugin.getServer().createInventory(p, 54, "§4Police Box Wall Menu");
                                    gui.setContents(chameleon);
                                    p.openInventory(gui);
                                }
                            }, 2L);
                            break;
                        case 26:
                            // close
                            close(p);
                            break;
                        default:
                            break;
                    }
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
    public void close(final Player p) {
        plugin.getTrackerKeeper().getHowTo().remove(p.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }
}
