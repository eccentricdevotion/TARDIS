/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeMode;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISSiegeButton {

    private final TARDIS plugin;
    private final Player player;
    private final boolean powered;
    private final int id;

    public TARDISSiegeButton(TARDIS plugin, Player player, boolean powered, int id) {
        this.plugin = plugin;
        this.player = player;
        this.powered = powered;
        this.id = id;
    }

    public void clickButton() {
        if (!plugin.getConfig().getBoolean("siege.enabled")) {
            TARDISMessage.send(player, "SIEGE_DISABLED");
            return;
        }
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
            long now = System.currentTimeMillis();
            long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
            long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
            if (now < then) {
                TARDISMessage.send(player, "COOLDOWN", String.format("%d", cooldown / 1000));
                return;
            }
        }
        if (plugin.getConfig().getBoolean("allow.power_down") && !powered) {
            TARDISMessage.send(player, "POWER_DOWN");
            return;
        }
        HashMap<String, Object> wherein = new HashMap<>();
        wherein.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
        if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            TARDISMessage.send(player, "TARDIS_NO_REBUILD");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            TARDISMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
            return;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            TARDISMessage.send(player, "NOT_WHILE_MAT");
            return;
        }
        // not while a siege cube item
        if (plugin.getTrackerKeeper().getIsSiegeCube().contains(id)) {
            TARDISMessage.send(player, "SIEGE_CUBED");
            return;
        }
        plugin.getTrackerKeeper().getRebuildCooldown().put(uuid, System.currentTimeMillis());
        // toggle siege mode
        new TARDISSiegeMode(plugin).toggleViaSwitch(id, player);
    }
}
