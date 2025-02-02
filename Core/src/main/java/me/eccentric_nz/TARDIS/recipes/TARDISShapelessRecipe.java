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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.recipes.shapeless.*;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISShapelessRecipe {

    private final TARDIS plugin;
    private final HashMap<String, ShapelessRecipe> shapelessRecipes;

    public TARDISShapelessRecipe(TARDIS plugin) {
        this.plugin = plugin;
        shapelessRecipes = new HashMap<>();
    }

    public void addShapelessRecipes() {
        new AdminUpgradeRecipe(plugin).addRecipe();
        new AppleJellyBabyRecipe(plugin).addRecipe();
        new BiomeStorageDiskRecipe(plugin).addRecipe();
        new BioscannerUpgradeRecipe(plugin).addRecipe();
        new BlueberryJellyBabyRecipe(plugin).addRecipe();
        new BowlofCustardRecipe(plugin).addRecipe();
        new BrushUpgradeRecipe(plugin).addRecipe();
        new BubblegumJellyBabyRecipe(plugin).addRecipe();
        new CappuccinoJellyBabyRecipe(plugin).addRecipe();
        new ConversionUpgradeRecipe(plugin).addRecipe();
        new DiamondUpgradeRecipe(plugin).addRecipe();
        new EarlGreyJellyBabyRecipe(plugin).addRecipe();
        new EmeraldUpgradeRecipe(plugin).addRecipe();
        new GrapeJellyBabyRecipe(plugin).addRecipe();
        new IgniteUpgradeRecipe(plugin).addRecipe();
        new IslandPunchJellyBabyRecipe(plugin).addRecipe();
        new KnockbackUpgradeRecipe(plugin).addRecipe();
        new LemonJellyBabyRecipe(plugin).addRecipe();
        new LicoriceJellyBabyRecipe(plugin).addRecipe();
        new LimeJellyBabyRecipe(plugin).addRecipe();
        new OrangeJellyBabyRecipe(plugin).addRecipe();
        new PainterUpgradeRecipe(plugin).addRecipe();
        new PickupArrowsUpgradeRecipe(plugin).addRecipe();
        new PlayerStorageDiskRecipe(plugin).addRecipe();
        new PresetStorageDiskRecipe(plugin).addRecipe();
        new RaspberryJellyBabyRecipe(plugin).addRecipe();
        new RedstoneUpgradeRecipe(plugin).addRecipe();
        new SaveStorageDiskRecipe(plugin).addRecipe();
        new StrawberryJellyBabyRecipe(plugin).addRecipe();
        new TARDISSchematicWandRecipe(plugin).addRecipe();
        new VanillaJellyBabyRecipe(plugin).addRecipe();
        new VodkaJellyBabyRecipe(plugin).addRecipe();
        new WatermelonJellyBabyRecipe(plugin).addRecipe();
    }

    public HashMap<String, ShapelessRecipe> getShapelessRecipes() {
        return shapelessRecipes;
    }
}
