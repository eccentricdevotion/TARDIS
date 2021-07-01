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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

class TARDISChameleonControl {

    private final TARDIS plugin;

    TARDISChameleonControl(TARDIS plugin) {
        this.plugin = plugin;
    }

    void openGUI(Player player, int id, Adaption adapt, PRESET preset) {
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasChameleon()) {
            TARDISMessage.send(player, "CHAM_MISSING");
            return;
        }
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
            return;
        }
        if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
            TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            TARDISMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
            return;
        }
        // open Chameleon Circuit GUI
        ItemStack[] cc = new TARDISChameleonInventory(plugin, adapt, preset).getMenu();
        Inventory cc_gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
        cc_gui.setContents(cc);
        player.openInventory(cc_gui);
    }
}
