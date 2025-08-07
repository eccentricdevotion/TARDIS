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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISIndexFileListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIndexFileClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISIndexFileInventory)) {
            return;
        }
        event.setCancelled(true);
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            return;
        }
        event.setCancelled(true);
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        if (slot == 26) {
            close(p);
        } else {
            ItemMeta im = is.getItemMeta();
            String name = ComponentUtils.toEnumUppercase(im.displayName());
            try {
                TISCategory category = TISCategory.valueOf(name);
                plugin.getTrackerKeeper().getInfoGUI().put(p.getUniqueId(), category);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        p.openInventory(new TARDISIndexFileSection(plugin, category).getInventory()), 2L);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
