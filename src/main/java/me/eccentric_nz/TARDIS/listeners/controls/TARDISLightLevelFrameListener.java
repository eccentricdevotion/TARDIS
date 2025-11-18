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
package me.eccentric_nz.TARDIS.listeners.controls;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.actions.ConsoleLampAction;
import me.eccentric_nz.TARDIS.control.actions.LightLevelAction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
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
            ResultSetLightLevel rs = new ResultSetLightLevel(plugin);
            if (rs.fromLocation(location.toString())) {
                // which switch is it?
                int type = rs.getType();
                int start = 0;
                ItemStack is = frame.getItem();
                ItemMeta im = is.getItemMeta();
                if (im.hasItemModel()) {
                    // switch the switches
                    String current = im.getItemModel().getKey();
                    boolean isOff = current.endsWith("_off");
                    String[] split = current.replace("_off", "").split("_");
                    String num = split[split.length - 1];
                    int which = rs.getLevel();
                    int cmd = which + 1;
                    if (cmd > 7) {
                        cmd = start;
                    }
                    String key = current.replace(num, "" + cmd);
                    if (!rs.isPowered() && !isOff) {
                        key += "_off";
                    }
                    NamespacedKey model = new NamespacedKey(plugin, key);
                    im.setItemModel(model);
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
