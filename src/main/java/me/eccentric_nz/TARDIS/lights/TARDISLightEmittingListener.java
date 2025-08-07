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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TARDISLightEmittingListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISLightEmittingListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLightEmittingBlockClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISLightEmittingInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 27) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TARDISLightEmittingListener");
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        switch (slot) {
            case 24 -> {
                // back
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                // get TARDIS id
                if (rst.fromUUID(uuid.toString())) {
                    player.openInventory(new TARDISLightsInventory(plugin, rst.getTardisId(), uuid).getInventory());
                }
            }
            case 26 -> close(player); // close
            default -> {
                // get block type and data
                ItemStack choice = event.getView().getItem(slot);
                setEmittingLightBlock(player, choice.getType().toString());
            }
        }
    }

    private void setEmittingLightBlock(Player player, String block) {
        // remember block
        Sequences.CONVERTERS.put(player.getUniqueId(), block);
        // get player's TARDIS
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        UUID uuid = player.getUniqueId();
        if (rst.fromUUID(uuid.toString())) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // go back to Lights GUI
                player.openInventory(new TARDISLightsInventory(plugin, rst.getTardisId(), uuid).getInventory());
            }, 5L);
        }
    }
}
