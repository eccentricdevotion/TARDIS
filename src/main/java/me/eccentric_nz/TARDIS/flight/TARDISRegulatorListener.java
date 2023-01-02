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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The helmic regulator was a component of the control panel of a Type 40 TARDIS. This device's principle function was
 * to control the accuracy of travel through the Time Vortex. If incorrectly handled, the helmic regulator could greatly
 * change the landing position of the TARDIS.
 *
 * @author eccentric_nz
 */
public class TARDISRegulatorListener extends TARDISRegulatorSlot implements Listener {

    private final TARDIS plugin;
    private final List<Integer> directions = Arrays.asList(16, 24, 26, 34);

    public TARDISRegulatorListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHelmicRegulatorClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals("Helmic Regulator")) {
            Player player = (Player) event.getPlayer();
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getRegulating().containsKey(uuid)) {
                plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getRegulating().get(uuid).getTaskId());
                plugin.getTrackerKeeper().getRegulating().remove(uuid);
                TARDISMessage.send(player, "HELMIC_ABORT");
                // get and set maximum adjustment from original location
                Location adjusted = new TARDISFlightAdjustment(plugin).getLocation(plugin.getTrackerKeeper().getFlightData().get(uuid), 10);
                plugin.getTrackerKeeper().getFlightData().get(uuid).setLocation(adjusted);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHelmicRegulatorOpen(InventoryOpenEvent event) {
        InventoryView view = event.getView();
        if (view.getTitle().equals("Helmic Regulator")) {
            Player player = (Player) event.getPlayer();
            UUID uuid = player.getUniqueId();
            // start the runnable
            TARDISRegulatorRunnable wr = new TARDISRegulatorRunnable(view);
            int id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, wr, 5L, 20L);
            wr.setTaskId(id);
            plugin.getTrackerKeeper().getRegulating().put(uuid, wr);
            // schedule a delayed task to close the inventory
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getServer().getScheduler().cancelTask(id);
                int final_slot = plugin.getTrackerKeeper().getRegulating().get(uuid).getSlot();
                plugin.getTrackerKeeper().getRegulating().remove(uuid);
                player.closeInventory();
                // adjust the landing location based on the final slot
                int blocks = 0;
                switch (final_slot) {
                    case 0, 4, 36, 40 -> blocks = 8;
                    case 1, 3, 9, 13, 27, 31, 37, 39 -> blocks = 6;
                    case 2, 10, 12, 18, 22, 28, 30, 38 -> blocks = 4;
                    case 11, 19, 21, 29 -> blocks = 2;
                    default -> {
                    }
                }
                // adjust location
                if (blocks != 0) {
                    Location adjusted = new TARDISFlightAdjustment(plugin).getLocation(plugin.getTrackerKeeper().getFlightData().get(uuid), blocks);
                    plugin.getTrackerKeeper().getFlightData().get(uuid).setLocation(adjusted);
                }
            }, 600L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHelmicRegulatorClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        UUID uuid = event.getWhoClicked().getUniqueId();
        if (view.getTitle().equals("Helmic Regulator") && plugin.getTrackerKeeper().getRegulating().containsKey(uuid)) {
            int slot = event.getRawSlot();
            int old_slot = plugin.getTrackerKeeper().getRegulating().get(uuid).getSlot();
            if (directions.contains(slot)) {
                switch (slot) {
                    case 16 -> { // up
                        int up = upSlot(old_slot);
                        if (bounds.contains(up)) {
                            view.setItem(old_slot, vortex);
                            view.setItem(up, box);
                            plugin.getTrackerKeeper().getRegulating().get(uuid).setSlot(up);
                        }
                    }
                    case 24 -> { // left
                        int left = leftSlot(old_slot);
                        if (bounds.contains(left)) {
                            view.setItem(old_slot, vortex);
                            view.setItem(left, box);
                            plugin.getTrackerKeeper().getRegulating().get(uuid).setSlot(left);
                        }
                    }
                    case 26 -> { // right
                        int right = rightSlot(old_slot);
                        if (bounds.contains(right)) {
                            view.setItem(old_slot, vortex);
                            view.setItem(right, box);
                            plugin.getTrackerKeeper().getRegulating().get(uuid).setSlot(right);
                        }
                    }
                    case 34 -> { // down
                        int down = downSlot(old_slot);
                        if (bounds.contains(down)) {
                            view.setItem(old_slot, vortex);
                            view.setItem(down, box);
                            plugin.getTrackerKeeper().getRegulating().get(uuid).setSlot(down);
                        }
                    }
                    default -> {
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
