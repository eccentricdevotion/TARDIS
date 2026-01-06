/*
 * Copyright (C) 2026 eccentric_nz
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

/**
 * The Absolute Tesseractulator is responsible for keeping track of a TARDIS's dimensional location. It uses the
 * Interstitial Antenna to collect data from the Vortex. A TARDIS knows where it's going by using digitally-modeled
 * time-cone isometry parallel-bussed into the image translator, with local motion being mapped over every
 * refresh-cycle.
 *
 * @author eccentric_nz
 */
public class ManualFlightListener implements Listener {

    private final TARDIS plugin;

    public ManualFlightListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block b = event.getClickedBlock();
        // only repeaters
        if (b != null && b.getType().equals(Material.REPEATER)) {
            Location loc = b.getLocation();
            if (plugin.getTrackerKeeper().getFlight().containsKey(uuid)) {
                if (loc.toString().equals(plugin.getTrackerKeeper().getFlight().get(uuid))) {
                    if (plugin.getTrackerKeeper().getCount().containsKey(uuid)) {
                        plugin.getTrackerKeeper().getCount().put(uuid, plugin.getTrackerKeeper().getCount().get(uuid) + 1);
                    } else {
                        plugin.getTrackerKeeper().getCount().put(uuid, 1);
                    }
                    event.setCancelled(true);
                }
                plugin.getTrackerKeeper().getFlight().remove(uuid);
            } else {
                // if it is a TARDIS repeater cancel the event
                if (plugin.getTrackerKeeper().getManualFlightLocations().containsKey(uuid) && plugin.getTrackerKeeper().getManualFlightLocations().get(uuid).contains(loc)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
