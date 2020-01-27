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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISSonicMenuListener extends TARDISMenuListener implements Listener {

    public TARDISSonicMenuListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Sonic Prefs Menu")) {
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
                    case 12:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        event.setCancelled(true);
                        // set display name of sonic in slot 18
                        ItemStack sonic = view.getItem(18);
                        if (sonic == null || !sonic.getType().equals(Material.BLAZE_ROD) || !sonic.hasItemMeta()) {
                            return;
                        }
                        // get Display name of selected sonic
                        ItemStack choice = view.getItem(slot);
                        ItemMeta choice_im = choice.getItemMeta();
                        String choice_name = choice_im.getDisplayName();
                        ItemMeta sonic_im = sonic.getItemMeta();
                        sonic_im.setDisplayName(choice_name);
                        sonic_im.setCustomModelData(choice_im.getCustomModelData());
                        sonic.setItemMeta(sonic_im);
                        break;
                    case 18:
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

    @EventHandler(ignoreCancelled = true)
    public void onSonicMenuClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!title.equals(ChatColor.DARK_RED + "Sonic Prefs Menu")) {
            return;
        }
        ItemStack sonic = view.getItem(18);
        if (sonic != null) {
            Player p = (Player) event.getPlayer();
            Location loc = p.getLocation();
            loc.getWorld().dropItemNaturally(loc, sonic);
            view.setItem(18, new ItemStack(Material.AIR));
        }
    }
}
