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
package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class TISRecipe {

    private final TARDIS plugin;

    public TISRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Displays the workbench recipe for an item or component.
     *
     * @param player the player to show the recipe to
     * @param item   the recipe to display
     */
    public void show(Player player, TARDISInfoMenu item) {
        String recipe;
        if (item == TARDISInfoMenu.THREE_D_GLASSES_RECIPE) {
            recipe = "3-d-glasses";
        } else {
            // remove "TARDIS_" and "_RECIPE" from the string and replace underscores with dashes
            String[] find = new String[]{"TARDIS_", "_RECIPE", "_"};
            String[] repl = new String[]{"", "", "-"};
            recipe = StringUtils.replaceEach(item.toString(), find, repl).toLowerCase(Locale.ROOT);
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardisrecipe " + recipe));
    }
}
