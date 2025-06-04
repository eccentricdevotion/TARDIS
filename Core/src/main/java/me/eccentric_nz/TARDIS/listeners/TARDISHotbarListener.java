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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.flight.FlightVisibility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TARDISHotbarListener implements Listener {

    private final TARDIS plugin;

    public TARDISHotbarListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSelectTARDISItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack is = inv.getItem(event.getNewSlot());
        if (is != null) {
            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                ItemMeta im = is.getItemMeta();
                if (im.getPersistentDataContainer().has(plugin.getOldBlockKey(), PersistentDataType.INTEGER)) {
                    int which = im.getPersistentDataContainer().get(plugin.getOldBlockKey(), PersistentDataType.INTEGER);
                    im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which);
                    is.setItemMeta(im);
                }
                if (is.getType().equals(Material.COMPASS) && im.getDisplayName().endsWith("TARDIS Locator")) {
                    // get TARDIS location
                    ResultSetTardisID rs = new ResultSetTardisID(plugin);
                    if (rs.fromUUID(player.getUniqueId().toString())) {
                        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, rs.getTardisId());
                        if (!rsc.resultSet()) {
                            return;
                        }
                        player.setCompassTarget(rsc.getCurrent().location());
                    }
                } else {
                    Location respawn = player.getRespawnLocation();
                    // if player has respawn location set else get world spawn
                    player.setCompassTarget(Objects.requireNonNullElseGet(respawn, () -> player.getWorld().getSpawnLocation()));
                }
            }
            if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(player.getUniqueId())) {
                // change behaviour of flight
                switch (is.getType()) {
                    case GLASS_BOTTLE -> {
                        // hide the armour stand entity from the player
                        Entity as = player.getVehicle();
                        if (as instanceof ArmorStand stand) {
                            new FlightVisibility(plugin).hide(stand, player);
                        }
                    }
                    case ARROW -> {}
                    default -> {
                        if (plugin.getTrackerKeeper().getHiddenFlight().containsKey(player.getUniqueId())) {
                            // remove entity hiding
                            new FlightVisibility(plugin).show(player);
                        }
                    }
                }
            }
        } else if (plugin.getTrackerKeeper().getHiddenFlight().containsKey(player.getUniqueId())) {
            // remove entity hiding
            new FlightVisibility(plugin).show(player);
        }
    }
}
