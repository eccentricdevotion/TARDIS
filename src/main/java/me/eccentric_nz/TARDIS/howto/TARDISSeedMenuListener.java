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
package me.eccentric_nz.TARDIS.howto;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * A control room's look could be changed over time. The process by which an operator could transform a control room was
 * fairly simple, once compared by the Fifth Doctor to changing a "desktop theme".
 *
 * @author eccentric_nz
 */
public class TARDISSeedMenuListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISSeedMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSeedMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(NamedTextColor.DARK_RED + "TARDIS Seeds Menu")) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 44) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is != null) {
            event.setCancelled(true);
            // close
            if (slot == 44) {
                close(p);
                return;
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack[] recipe = new TARDISSeedRecipeInventory(plugin, is.getType()).getMenu();
                Inventory gui = plugin.getServer().createInventory(p, 27, NamedTextColor.DARK_RED + "TARDIS Seed Recipe");
                gui.setContents(recipe);
                p.openInventory(gui);
            }, 2L);
        }
    }

    /**
     * Closes the inventory.
     *
     * @param player the player using the GUI
     */
    @Override
    public void close(Player player) {
        plugin.getTrackerKeeper().getHowTo().remove(player.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, player::closeInventory, 1L);
    }
}
