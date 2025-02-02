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
package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.InteractionResponse;
import me.eccentric_nz.TARDIS.console.models.WXYZModel;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WorldInteraction {

    private final TARDIS plugin;

    public WorldInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void selectWorld(int state, Player player, Interaction interaction, int id) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        /*
        THIS => 1,
        NORMAL => 2,
        NETHER => 3,
        THE_END => 4
         */
        boolean missing = false;
        int next = state + 1;
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && (next == 3 || next == 4) && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.INTER_DIMENSIONAL_TRAVEL)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Inter Dimensional Travel");
            missing = true;
        }
        if (next > 4 || missing) {
            next = 1;
        }
        if (!missing) {
            // set custom model data for lamp level switch item display
            UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
            if (uuid != null) {
                ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
                new WXYZModel().setState(display, plugin, 1);
            }
            new InteractionStateSaver(plugin).write("WORLD", next, id);
        }
        plugin.getMessenger().announceRepeater(player, InteractionResponse.environment.get(next));
    }
}
