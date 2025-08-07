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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.recipes.smithing.*;

public class TARDISSmithingRecipe {

    private final TARDIS plugin;

    public TARDISSmithingRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addSmithingRecipes() {
        new AdminRepairRecipe(plugin).addRecipe();
        new BioscannerRepairRecipe(plugin).addRecipe();
        new BrushRepairRecipe(plugin).addRecipe();
        new ConversionRepairRecipe(plugin).addRecipe();
        new DiamondRepairRecipe(plugin).addRecipe();
        new EmeraldRepairRecipe(plugin).addRecipe();
        new IgniteRepairRecipe(plugin).addRecipe();
        new KnockbackRepairRecipe(plugin).addRecipe();
        new PainterRepairRecipe(plugin).addRecipe();
        new PickupArrowsRepairRecipe(plugin).addRecipe();
        new RedstoneRepairRecipe(plugin).addRecipe();
        new CapacitorRepairRecipe(plugin).addRecipe();
    }
}
