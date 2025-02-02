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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class SiegeAction {

    private final TARDIS plugin;

    public SiegeAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void clickButton(TARDISCircuitChecker tcc, Player player, boolean powered, int id) {
        if (tcc != null && !tcc.hasMaterialisation()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
            return;
        }
        if (!plugin.getConfig().getBoolean("siege.enabled")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_DISABLED");
            return;
        }
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getRebuildCooldown().containsKey(uuid)) {
            long now = System.currentTimeMillis();
            long cooldown = plugin.getConfig().getLong("police_box.rebuild_cooldown");
            long then = plugin.getTrackerKeeper().getRebuildCooldown().get(uuid) + cooldown;
            if (now < then) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "COOLDOWN", String.format("%d", cooldown / 1000));
                return;
            }
        }
        if (plugin.getConfig().getBoolean("allow.power_down") && !powered) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
            return;
        }
        HashMap<String, Object> wherein = new HashMap<>();
        wherein.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherein, false);
        if (rst.resultSet() && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_NO_REBUILD");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_IN_VORTEX");
            return;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
            return;
        }
        // not while a siege cube item
        if (plugin.getTrackerKeeper().getIsSiegeCube().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_CUBED");
            return;
        }
        plugin.getTrackerKeeper().getRebuildCooldown().put(uuid, System.currentTimeMillis());
        // toggle siege mode
        new TARDISSiegeMode(plugin).toggleViaSwitch(id, player);
    }
}
