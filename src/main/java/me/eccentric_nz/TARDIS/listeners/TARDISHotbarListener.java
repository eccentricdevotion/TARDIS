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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
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
                if (is.getType().equals(Material.COMPASS) && im.getDisplayName().equals("TARDIS Locator")) {
                    // get TARDIS location
                    ResultSetTardisID rs = new ResultSetTardisID(plugin);
                    if (rs.fromUUID(player.getUniqueId().toString())) {
                        HashMap<String, Object> wherecl = new HashMap<>();
                        wherecl.put("tardis_id", rs.getTardis_id());
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                        if (!rsc.resultSet()) {
                            return;
                        }
                        Location pb = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        player.setCompassTarget(pb);
                    }
                } else {
                    Location bedspawn = player.getBedSpawnLocation();
                    // if player has bed spawn set
                    player.setCompassTarget(Objects.requireNonNullElseGet(bedspawn, () -> player.getWorld().getSpawnLocation()));
                }
            }
        }
    }
}
