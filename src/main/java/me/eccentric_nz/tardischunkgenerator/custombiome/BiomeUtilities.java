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
package me.eccentric_nz.tardischunkgenerator.custombiome;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.BiomeSearchResult;

public class BiomeUtilities {

    public static Location searchBiome(World world, Biome biome, Location policeBox) {
        BiomeSearchResult searchResult = world.locateNearestBiome(policeBox, 6400, biome);
        return searchResult != null ? searchResult.getLocation() : null;
    }

    public static boolean isUnderground(Biome biome) {
        switch (biome.key().value()) {
            case "deep_dark", "dripstone_caves", "lush_caves", "sulfur_caves" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
