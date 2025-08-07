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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.RepeaterControl;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.Player;

public class RepeaterAction {

    private final TARDIS plugin;

    public RepeaterAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void announce(Player player, Block block, int type) {
        Repeater repeater = (Repeater) block.getBlockData();
        RepeaterControl rc = RepeaterControl.getControl(type);
        int delay = repeater.getDelay();
        boolean missing = false;
        // check if player/tardis has system upgrade
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            if (rc == RepeaterControl.WORLD) {
                if ((delay == 2 || delay == 3) && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.INTER_DIMENSIONAL_TRAVEL)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Inter Dimensional Travel");
                    missing = true;
                }
            }
            if (rc == RepeaterControl.MULTIPLIER) {
                if (delay == 1 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.DISTANCE_1)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 1");
                    missing = true;
                }
                if (delay == 2 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.DISTANCE_2)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 2");
                    missing = true;
                }
                if (delay == 3 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.DISTANCE_3)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 3");
                    missing = true;
                }
            }
            if (missing) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    repeater.setDelay(1);
                    block.setBlockData(repeater);
                }, 2L);
            }
        }
        // message setting when clicked
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        if (rsp.resultSet() && rsp.isAnnounceRepeatersOn()) {
            if (delay == 4 || missing) {
                delay = 0;
            }
            plugin.getMessenger().announceRepeater(player, rc.getDescriptions().get(delay));
        }
    }
}
