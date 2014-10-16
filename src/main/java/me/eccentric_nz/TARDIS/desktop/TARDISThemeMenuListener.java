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
package me.eccentric_nz.TARDIS.desktop;

import java.util.Set;
import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A control room's look could be changed over time. The process by which an
 * operator could transform a control room was fairly simple, once compared by
 * the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISThemeMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISThemeMenuListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS Upgrade Menu")) {
            Player p = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                switch (slot) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        event.setCancelled(true);
                        if (slot == 10 && !plugin.getConfig().getBoolean("creation.custom_schematic")) {
                            return;
                        }
                        // get Display name of selected console
                        ItemStack choice = inv.getItem(slot);
                        ItemMeta choice_im = choice.getItemMeta();
                        String choice_name = TARDISARS.ARSFor(choice_im.getDisplayName()).getActualName();
                        if (p.hasPermission("tardis." + choice_name.toLowerCase())) {
                            // remember the upgrade choice
                            TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(p.getUniqueId());
                            if (tud.getLevel() > plugin.getArtronConfig().getInt("upgrades." + choice_name.toLowerCase())) {
                                tud.setSchematic(SCHEMATIC.valueOf(choice_name));
                                plugin.getTrackerKeeper().getUpgrades().put(p.getUniqueId(), tud);
                                // open the wall block GUI
                                wall(p);
                            }
                        }
                        break;
                    case 26:
                        // close
                        event.setCancelled(true);
                        close(p);
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onThemeMenuDrag(InventoryDragEvent event) {
        Inventory inv = event.getInventory();
        String title = inv.getTitle();
        if (!title.equals("§4TARDIS Upgrade Menu")) {
            return;
        }
        Set<Integer> slots = event.getRawSlots();
        for (Integer slot : slots) {
            if ((slot >= 0 && slot < 27)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(final Player p) {
        plugin.getTrackerKeeper().getUpgrades().remove(p.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }

    /**
     * Closes the inventory and opens the Wall block selector menu.
     *
     * @param p the player using the GUI
     */
    private void wall(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
                ItemStack[] wall_blocks = new TARDISWallsInventory(plugin).getMenu();
                Inventory wall = plugin.getServer().createInventory(p, 54, "§4TARDIS Wall Menu");
                wall.setContents(wall_blocks);
                p.openInventory(wall);
            }
        }, 1L);
    }
}
