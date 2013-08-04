/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTemporalLocatorListener implements Listener {

    private final TARDIS plugin;

    public TARDISTemporalLocatorListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onTemporalTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Temporal Locator")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            String playerNameStr = player.getName();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                ItemStack is = inv.getItem(slot);
                if (is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    List<String> lore = im.getLore();
                    long time = getTime(lore);
                    plugin.trackSetTime.put(playerNameStr, time);
                    player.sendMessage(plugin.pluginName + "Your temporal location will be set to " + time + " ticks when exiting the TARDIS.");
                }
                close(player);
            }
        }
    }

    /**
     * Converts an Item Stacks lore to a destination string in the correct
     * format for entry into the database.
     *
     * @param lore the lore to read
     * @return the destination string
     */
    private long getTime(List<String> lore) {
        String[] data = lore.get(0).split(" ");
        return Long.parseLong(data[0]);
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(final Player p) {
        final String n = p.getName();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }
}
