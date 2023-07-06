/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
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
public class TARDISKeyMenuListener extends TARDISMenuListener {

    public TARDISKeyMenuListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrefsMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Key Prefs Menu")) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                switch (slot) {
                    case 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16 -> {
                        event.setCancelled(true);
                        // set display name of key in slot 18
                        ItemStack key = view.getItem(18);
                        if (key == null || !key.getType().equals(Material.GOLD_NUGGET) || !key.hasItemMeta()) {
                            return;
                        }
                        // get display name of selected key
                        ItemStack choice = view.getItem(slot);
                        ItemMeta choiceMeta = choice.getItemMeta();
                        String displayName = choiceMeta.getDisplayName();
                        ItemMeta keyMeta = key.getItemMeta();
                        keyMeta.setDisplayName(displayName);
                        keyMeta.setCustomModelData(choiceMeta.getCustomModelData());
                        // personalise
                        keyMeta.getPersistentDataContainer().set(TARDIS.plugin.getTimeLordUuidKey(), TARDIS.plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                        // set lore
                        List<String> lore;
                        if (keyMeta.hasLore()) {
                            lore = keyMeta.getLore();
                        } else {
                            lore = new ArrayList<>();
                        }
                        String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
                        if (!lore.contains(format + "This key belongs to")) {
                            lore.add(format + "This key belongs to");
                            lore.add(format + player.getName());
                            keyMeta.setLore(lore);
                        }
                        key.setItemMeta(keyMeta);
                    }
                    case 18 -> { }
                    case 26 -> {
                        // close
                        event.setCancelled(true);
                        close(player);
                    }
                    default -> event.setCancelled(true);
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
    public void onKeyMenuClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!title.equals(ChatColor.DARK_RED + "TARDIS Key Prefs Menu")) {
            return;
        }
        ItemStack key = view.getItem(18);
        if (key != null) {
            Player p = (Player) event.getPlayer();
            Location loc = p.getLocation();
            loc.getWorld().dropItemNaturally(loc, key);
            view.setItem(18, new ItemStack(Material.AIR));
        }
    }
}
