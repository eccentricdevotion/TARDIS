/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.preferences.TARDISKeyMenuListener;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
public class TARDISSonicMenuListener extends TARDISMenuListener {

    public TARDISSonicMenuListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Sonic Prefs Menu")) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 35) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        switch (slot) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 -> {
                event.setCancelled(true);
                // set custom model data of sonic in slot 27
                ItemStack sonic = view.getItem(27);
                if (sonic == null || !sonic.getType().equals(Material.BLAZE_ROD) || !sonic.hasItemMeta()) {
                    return;
                }
                ItemStack choice = view.getItem(slot);
                ItemMeta choice_im = choice.getItemMeta();
                ItemMeta sonic_im = sonic.getItemMeta();
                sonic_im.setCustomModelData(choice_im.getCustomModelData());
                sonic.setItemMeta(sonic_im);
            }
            case 27 -> {
                // get item on cursor
                ItemStack cursor = event.getCursor();
                if (cursor == null || !cursor.getType().equals(Material.BLAZE_ROD) || !cursor.hasItemMeta()) {
                    return;
                }
                ItemMeta meta = cursor.getItemMeta();
                if (!meta.hasDisplayName()) {
                    return;
                }
                // set wool colour from display name of placed sonic
                ChatColor color = TARDISStaticUtils.getColor(meta.getDisplayName());
                Material material = TARDISKeyMenuListener.REVERSE_LOOKUP.get(color);
                ItemStack choice = view.getItem(28);
                choice.setType(material);
            }
            case 28 -> {
                event.setCancelled(true);
                // set display name colour of sonic in slot 27
                ItemStack sonic = view.getItem(27);
                if (sonic == null || !sonic.getType().equals(Material.BLAZE_ROD) || !sonic.hasItemMeta()) {
                    return;
                }
                // get current colour of wool
                ItemStack choice = view.getItem(28);
                Material wool = getNextWool(choice.getType());
                // set wool colour to next in line
                choice.setType(wool);
                ChatColor display = TARDISKeyMenuListener.COLOUR_LOOKUP.get(wool);
                ItemMeta sonic_im = sonic.getItemMeta();
                if (display != ChatColor.WHITE) {
                    sonic_im.setDisplayName(display + "Sonic Screwdriver");
                } else {
                    sonic_im.setDisplayName("Sonic Screwdriver");
                }
                sonic.setItemMeta(sonic_im);
            }
            case 35 -> {
                // close
                event.setCancelled(true);
                close(p);
            }
            default -> event.setCancelled(true);
        }
    }

    private Material getNextWool(Material current) {
        Material index = TARDISKeyMenuListener.COLOUR_LOOKUP.higherKey(current);
        return (index != null) ? index : Material.WHITE_WOOL;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSonicMenuClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Sonic Prefs Menu")) {
            return;
        }
        ItemStack sonic = view.getItem(27);
        if (sonic == null) {
            return;
        }
        Player p = (Player) event.getPlayer();
        Location loc = p.getLocation();
        loc.getWorld().dropItemNaturally(loc, sonic);
        view.setItem(27, new ItemStack(Material.AIR));
    }
}
