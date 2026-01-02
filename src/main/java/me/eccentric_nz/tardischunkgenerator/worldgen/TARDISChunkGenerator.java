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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.worldgen;

import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.TARDISVoidBiomeProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * @author eccentric_nz
 */
public class TARDISChunkGenerator extends ChunkGenerator {

    /**
     * Generates an empty void world!
     */
    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    /**
     * Sets the entire world to the VOID biome
     *
     * @param worldInfo a world info object
     * @return the Void biome provider
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new TARDISVoidBiomeProvider();
    }

    /**
     * Gets the fixed spawn location of a world.
     *
     * @param world  the world from which to get the spawn location
     * @param random a pseudorandom number generator
     * @return the spawn location of the world
     */
    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 70, 0);
    }
}
