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
import me.eccentric_nz.TARDIS.control.TARDISControlInventory;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ControlMenuAction {

    private final TARDIS plugin;

    public ControlMenuAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, int id) {
        if (player.isSneaking()) {
            // keyboard
        } else {
            UUID playerUUID = player.getUniqueId();
            // controls GUI
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                TARDISFloodgate.sendControlForm(playerUUID);
            } else {
                TARDISControlInventory controls = new TARDISControlInventory(plugin, id);
                player.openInventory(controls.getInventory());
            }
        }
    }
}
