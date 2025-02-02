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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ForceFieldAction {

    private final TARDIS plugin;

    public ForceFieldAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleSheilds(Player player, Location blockLocation, int level) {
        if (TARDISPermission.hasPermission(player, "tardis.forcefield")) {
            if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.FORCE_FIELD)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Force Field");
                return;
            }
            if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(player.getUniqueId())) {
                plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                TARDISSounds.playTARDISSound(blockLocation, "tardis_force_field_down");
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FORCE_FIELD", "OFF");
            } else {
                // check there is enough artron
                if (level <= plugin.getArtronConfig().getInt("standby")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LOW");
                    return;
                }
                if (TARDISForceField.addToTracker(player)) {
                    TARDISSounds.playTARDISSound(blockLocation, "tardis_force_field_up");
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "FORCE_FIELD", "ON");
                }
            }
        }
    }
}
