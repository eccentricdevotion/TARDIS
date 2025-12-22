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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISTemporalLocatorListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISTemporalLocatorListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onTemporalTerminalClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISTemporalLocatorInventory)) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            List<Component> lore = im.lore();
            long time = getTime(lore);
            plugin.getTrackerKeeper().getSetTime().put(player.getUniqueId(), time);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TEMPORAL_SET", String.format("%d", time));
            // damage the circuit if configured
            DamageUtility.run(plugin, DiskCircuit.TEMPORAL, plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId()), player);
        }
        close(player);
    }

    /**
     * Converts an Item Stacks lore to a destination string in the correct format for entry into the database.
     *
     * @param lore the lore to read
     * @return the destination string
     */
    private long getTime(List<Component> lore) {
        String[] data = ComponentUtils.stripColour(lore.getFirst()).split(" ");
        return TARDISNumberParsers.parseLong(data[0]);
    }
}
