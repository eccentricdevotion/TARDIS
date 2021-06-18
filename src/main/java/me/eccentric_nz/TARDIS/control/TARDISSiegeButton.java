/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.siegemode.TardisSiegeMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisSiegeButton {

    private final TardisPlugin plugin;
    private final Player player;
    private final boolean powered;
    private final int id;

    TardisSiegeButton(TardisPlugin plugin, Player player, boolean powered, int id) {
        this.plugin = plugin;
        this.player = player;
        this.powered = powered;
        this.id = id;
    }

    public void clickButton() {
        if (!plugin.getConfig().getBoolean("siege.enabled")) {
            TardisMessage.send(player, "SIEGE_DISABLED");
            return;
        }
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
            long now = System.currentTimeMillis();
            long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
            long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
            if (now < then) {
                TardisMessage.send(player, "COOLDOWN", String.format("%d", cooldown / 1000));
                return;
            }
        }
        if (plugin.getConfig().getBoolean("allow.power_down") && !powered) {
            TardisMessage.send(player, "POWER_DOWN");
            return;
        }
        HashMap<String, Object> wherein = new HashMap<>();
        wherein.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
        if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            TardisMessage.send(player, "TARDIS_NO_REBUILD");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            TardisMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
            return;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            TardisMessage.send(player, "NOT_WHILE_MAT");
            return;
        }
        // not while a siege cube item
        if (plugin.getTrackerKeeper().getIsSiegeCube().contains(id)) {
            TardisMessage.send(player, "SIEGE_CUBED");
            return;
        }
        plugin.getTrackerKeeper().getRebuildCooldown().put(uuid, System.currentTimeMillis());
        // toggle siege mode
        new TardisSiegeMode(plugin).toggleViaSwitch(id, player);
    }
}
