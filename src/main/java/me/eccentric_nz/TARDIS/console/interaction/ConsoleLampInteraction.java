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
import me.eccentric_nz.TARDIS.control.actions.ConsoleLampAction;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class ConsoleLampInteraction {

    private final TARDIS plugin;

    public ConsoleLampInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setLevel(int state, int id, Interaction interaction, Player player) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        int unary = interaction.getPersistentDataContainer().getOrDefault(plugin.getUnaryKey(), PersistentDataType.INTEGER, 1);
        int setLevel = state + unary;
        if (setLevel > 7) {
            setLevel = 6;
            unary = -1;
        }
        if (setLevel < 0) {
            setLevel = 1;
            unary = 1;
        }
        interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.INTEGER, unary);
        new InteractionStateSaver(plugin).write("CONSOLE_LAMP", setLevel, id);
        plugin.getMessenger().announceRepeater(player, "Console Lamp level: " + InteractionResponse.levels.get(setLevel));
        // set light level
        new ConsoleLampAction(plugin).illuminate(id, setLevel, -1);
    }
}
