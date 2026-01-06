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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.ChameleonControl;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateChameleonCircuitForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChameleonSignAction {

    private final TARDIS plugin;

    public ChameleonSignAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, Tardis tardis, int id) {
        UUID uuid = player.getUniqueId();
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.CHAMELEON_CIRCUIT)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Chameleon Circuit");
            return;
        }
        if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(uuid)) {
            new FloodgateChameleonCircuitForm(plugin, uuid, id, tardis.getPreset()).send();
        } else {
            new ChameleonControl(plugin).openGUI(player, id, tardis.getAdaption(), tardis.getPreset(), tardis.getItemPreset());
        }
    }
}
