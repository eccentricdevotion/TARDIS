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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
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
public class TARDISThemeMenuListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISThemeMenuListener(TARDIS plugin) {
        super(plugin);
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
                    case 11:
                        event.setCancelled(true);
                        // get Display name of selected console
                        ItemStack choice = inv.getItem(slot);
                        String perm = CONSOLES.SCHEMATICFor(choice.getType()).getPermission();
                        if (p.hasPermission("tardis." + perm)) {
                            // remember the upgrade choice
                            SCHEMATIC schm = CONSOLES.SCHEMATICFor(perm);
                            TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(p.getUniqueId());
                            int upgrade = plugin.getArtronConfig().getInt("upgrades." + perm);
                            int needed = (tud.getPrevious().getPermission().equals(schm.getPermission())) ? upgrade / 2 : upgrade;
                            if (tud.getLevel() > needed) {
                                tud.setSchematic(schm);
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
