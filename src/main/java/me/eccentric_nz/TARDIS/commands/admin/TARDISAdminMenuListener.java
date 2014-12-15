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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The architectural reconfiguration system is a component of the Doctor's
 * TARDIS in the shape of a tree that, according to the Eleventh Doctor,
 * "reconstructs the particles according to your needs." It is basically "a
 * machine that makes machines," perhaps somewhat like a 3D printer. It is,
 * according to Gregor Van Baalen's scanner, "more valuable than the total sum
 * of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISAdminMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISAdminMenuListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onAdminMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Admin Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot < 54) {
                String option = getDisplay(inv, slot);
                if (slot == 52) {
                    final Player p = (Player) event.getWhoClicked();
                    // close this gui and load the previous / next page
                    if (option.equals("Previous page")) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Inventory ppm = plugin.getServer().createInventory(p, 54, "§4Admin Menu");
                                ppm.setContents(new TARDISAdminMenuInventory(plugin).getMenu());
                                p.openInventory(ppm);
                            }
                        }, 1L);
                    } else {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Inventory ppm = plugin.getServer().createInventory(p, 54, "§4Admin Menu");
                                ppm.setContents(new TARDISAdminPageTwoInventory(plugin).getMenu());
                                p.openInventory(ppm);
                            }
                        }, 1L);
                    }
                    return;
                }
                if (slot == 53 && option.equals("Player Preferences")) {
                    final Player p = (Player) event.getWhoClicked();
                    // close this gui and load the Player Prefs Menu
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            Inventory ppm = plugin.getServer().createInventory(p, 18, "§4Player Prefs Menu");
                            ppm.setContents(new TARDISPrefsMenuInventory(plugin, p.getUniqueId()).getMenu());
                            p.openInventory(ppm);
                        }
                    }, 1L);
                    return;
                }
                if (!option.isEmpty()) {
                    boolean bool = plugin.getConfig().getBoolean(option);
                    plugin.getConfig().set(option, !bool);
                    String lore = (bool) ? "false" : "true";
                    setLore(inv, slot, lore);
                    plugin.saveConfig();
                }
            }
        }
    }

    private String getDisplay(Inventory inv, int slot) {
        ItemStack is = inv.getItem(slot);
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            return im.getDisplayName();
        } else {
            return "";
        }
    }

    private void setLore(Inventory inv, int slot, String str) {
        List<String> lore = (str != null) ? Arrays.asList(str) : null;
        ItemStack is = inv.getItem(slot);
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }
}
