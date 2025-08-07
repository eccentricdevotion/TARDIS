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
package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicBiome;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicStructure;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateBiomesForm;
import me.eccentric_nz.TARDIS.floodgate.FloodgateStructuresForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.entity.Player;

public class TARDISTravelGUI {

    private final TARDIS plugin;

    public TARDISTravelGUI(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player, int id, String which) {
        // check for telepathic circuit
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true)) {
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            if (!tcc.hasTelepathic()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TELEPATHIC_CIRCUIT");
                return true;
            }
        }
        if (which.equals("biome")) {
            // open biomes GUI
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                new FloodgateBiomesForm(plugin, player.getUniqueId(), id).send();
            } else {
                player.openInventory(new TARDISTelepathicBiome(plugin, id).getInventory());
            }
        } else {
            // open Structure GUI
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                new FloodgateStructuresForm(plugin, player.getUniqueId(), id).send();
            } else {
                player.openInventory(new TARDISTelepathicStructure(plugin).getInventory());
            }
        }
        return true;
    }
}
