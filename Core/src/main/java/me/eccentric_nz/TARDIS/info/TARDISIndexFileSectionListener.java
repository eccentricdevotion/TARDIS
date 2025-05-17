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
package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileSectionListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISIndexFileSectionListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIndexFileSectionClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Info Category")) {
            return;
        }
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 54) {
            return;
        }
        event.setCancelled(true);
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 45 -> {
                Player player = (Player) event.getWhoClicked();
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    ItemStack[] cats = new TARDISIndexFileInventory(plugin).getMenu();
                    Inventory gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "TARDIS Index File");
                    gui.setContents(cats);
                    player.openInventory(gui);
                }, 2L);
            }
            case 53 -> close(p);
            default -> {
                ItemMeta im = is.getItemMeta();
                String name = TARDISStringUtils.toEnumUppercase(im.getDisplayName());
                try {
                    TARDISInfoMenu tim = TARDISInfoMenu.valueOf(name);
                    TISCategory category = plugin.getTrackerKeeper().getInfoGUI().get(p.getUniqueId());
                    if (category == TISCategory.ROOMS) {
                        new TISRoomInfo(plugin).show(p, tim);
                        close(p);
                    } else if ((category.isFirstLevel() && !hasRecipe(tim)) || (category == TISCategory.MONSTERS && tim != TARDISInfoMenu.K9)) {
                        new TISInfo(plugin).show(p, tim);
                        close(p);
                    } else {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            ItemStack[] entries = new TARDISIndexFileEntry(plugin, tim).getMenu();
                            Inventory gui = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "TARDIS Info Entry");
                            gui.setContents(entries);
                            p.openInventory(gui);
                        }, 2L);
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    private boolean hasRecipe(TARDISInfoMenu tim) {
        switch (tim) {
            case EXTERIOR_LAMP_LEVEL_SWITCH, INTERIOR_LIGHT_LEVEL_SWITCH, TARDIS_MONITOR, MONITOR_FRAME -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
