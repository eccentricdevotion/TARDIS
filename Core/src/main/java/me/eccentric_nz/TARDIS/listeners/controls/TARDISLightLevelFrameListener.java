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
package me.eccentric_nz.TARDIS.listeners.controls;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.actions.ConsoleLampAction;
import me.eccentric_nz.TARDIS.control.actions.LightLevelAction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISLightLevelFrameListener implements Listener {

    private final TARDIS plugin;

    public TARDISLightLevelFrameListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onLightLevelClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame frame) {
            // check if it is a light level item frame
            Location location = frame.getLocation();
            ResultSetLightLevel rs = new ResultSetLightLevel(plugin, location.toString());
            if (rs.resultSet()) {
                // which switch is it?
                int type = rs.getType();
                int start = switch (type) {
                    case 49 -> 1000; // exterior
                    case 50 -> 3000; // interior
                    default -> 9000; // console
                };
                // is the power off?
                if (!rs.isPowered()) {
                    start += 1000;
                }
                int limit = start + 7;
                ItemStack is = frame.getItem();
                ItemMeta im = is.getItemMeta();
                if (im.hasCustomModelData()) {
                    // switch the switches
                    int current = im.getCustomModelData();
                    if (!rs.isPowered() && ((type == 49 && current < 2000) || (type == 50 && current < 4000) || (type == 57 && current < 10000))) {
                        current += 1000;
                    }
                    int cmd = current + 1;
                    if (cmd > limit) {
                        cmd = start;
                    }
                    im.setCustomModelData(cmd);
                    is.setItemMeta(im);
                    frame.setItem(is);
                    if (type == 49 || type == 50) {
                        new LightLevelAction(plugin).illuminate(rs.getLevel(), rs.getControlId(), rs.isPowered(), type, rs.isPoliceBox(), rs.getTardis_id(), rs.isLightsOn());
                    } else {
                        new ConsoleLampAction(plugin).illuminate(rs.getTardis_id(), rs.getLevel(), rs.getControlId());
                    }
                }
            }
        }
    }
}
