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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileEntryListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISIndexFileEntryListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIndexFileEntryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISIndexFileEntry)) {
            return;
        }
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 54) {
            return;
        }
        event.setCancelled(true);
        InventoryView view = event.getView();
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        if (slot == 26) {
            close(p);
        } else if (slot > 8) {
            // get item from slot 0
            ItemStack zero = view.getItem(0);
            ItemMeta zim = zero.getItemMeta();
            ItemMeta im = is.getItemMeta();
            String name = ComponentUtils.toEnumUppercase(zim.displayName()) + "_" + ComponentUtils.toEnumUppercase(im.displayName());
            try {
                TARDISInfoMenu tim = TARDISInfoMenu.valueOf(name);
                if (im.displayName().equals("Recipe")) {
                    new TISRecipe(plugin).show(p, tim);
                } else {
                    new TISInfo(plugin).show(p, tim);
                    close(p);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
