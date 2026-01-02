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
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;

public class TARDISRegeneration {

    private final TARDIS plugin;

    public TARDISRegeneration(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.getPM().registerEvents(new ElixirOfLifeListener(plugin), plugin);
        plugin.getPM().registerEvents(new UntemperedSchismListener(plugin), plugin);
        plugin.getPM().registerEvents(new VoidListener(plugin), plugin);
        TARDISRegenerationCommand command = new TARDISRegenerationCommand(plugin);
        plugin.getCommand("tardisregeneration").setExecutor(command);
        plugin.getCommand("tardisregeneration").setTabCompleter(command);
        // add elixir recipe
        new ElixirOfLifeRecipe(plugin).addRecipe();
        new UntemperedSchismRecipe(plugin).addRecipe();
    }
}
