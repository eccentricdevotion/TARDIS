/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardissonicblaster;

import me.eccentric_nz.TARDIS.TARDIS;

public class TARDISSonicBlaster {

    private final TARDIS plugin;

    public TARDISSonicBlaster(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.getPM().registerEvents(new TARDISSonicBlasterListener(plugin), plugin);
        plugin.getPM().registerEvents(new TARDISSonicBlasterCraftListener(), plugin);
        new TARDISSonicBlasterRecipe(plugin).addShapedRecipes();
        double maxUsableDistance = Math.sqrt(plugin.getBlasterConfig().getDouble("max_blocks"));
        long cooldown = plugin.getBlasterConfig().getLong("cooldown") * 1000;
        plugin.setBlasterSettings(new BlasterSettings(maxUsableDistance, cooldown));
    }
}
