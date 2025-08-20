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
package me.eccentric_nz.tardischunkgenerator.custombiome;

import org.bukkit.Location;
import org.bukkit.World;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.GenUtils;
import org.terraform.utils.Vector2f;

import java.util.Locale;

public class TerraBiomeLocator {

    private final World world;
    private final Location current;
    private final String biome;

    public TerraBiomeLocator(World world, Location current, String biome) {
        this.world = world;
        this.current = current;
        this.biome = biome;
    }

    public Location execute() {
        try {
            TerraformWorld terraformWorld = TerraformWorld.get(world);
            BiomeBank biomeBank = BiomeBank.valueOf(biome.toUpperCase(Locale.ROOT));
            Vector2f location;
            int x = current.getBlockX();
            int z = current.getBlockZ();
            if (biomeBank.getType() == BiomeType.BEACH || biomeBank.getType() == BiomeType.RIVER) {
                location = GenUtils.locateHeightDependentBiome(terraformWorld, biomeBank, new Vector2f(x, z), 5000, 25);
            } else {
                location = GenUtils.locateHeightIndependentBiome(terraformWorld, biomeBank, new Vector2f(x, z));
            }
            if (location != null) {
                return new Location(world, location.x, world.getHighestBlockYAt((int) location.x, (int) location.y), location.y);
            }
            return null;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
