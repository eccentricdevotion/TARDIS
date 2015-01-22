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
package me.eccentric_nz.TARDIS.listeners;

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTemporalLocatorListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISTemporalLocatorListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onTemporalTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Temporal Locator")) {
            event.setCancelled(true);
            final Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                ItemStack is = inv.getItem(slot);
                if (is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    List<String> lore = im.getLore();
                    long time = getTime(lore);
                    plugin.getTrackerKeeper().getSetTime().put(player.getUniqueId(), time);
                    TARDISMessage.send(player, "TEMPORAL_SET", String.format("%d", time));
                    // damage the circuit if configured
                    if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getString("preferences.difficulty").equals("hard") && plugin.getConfig().getInt("circuits.uses.temporal") > 0) {
                        int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                        tcc.getCircuits();
                        // decrement uses
                        int uses_left = tcc.getTemporalUses();
                        new TARDISCircuitDamager(plugin, DISK_CIRCUIT.TEMPORAL, uses_left, id, player).damage();
                    }
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
        return TARDISNumberParsers.parseLong(data[0]);
    }
}
