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
package me.eccentric_nz.tardischunkgenerator.worldgen;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.TARDISVoidBiomeProvider;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.RoomPopulator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.List;
import java.util.Random;

public class RoomGenerator extends ChunkGenerator {
    private final TARDIS plugin;

    public RoomGenerator(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
            for (int bx = 0; bx < 16; bx++) {
                for (int bz = 0; bz < 16; bz++) {
                    chunkData.setBlock(bx, 63, bz, Material.BEDROCK);
                }
            }
    }

    /**
     * Sets the entire world to the PLAINS biome
     *
     * @param worldInfo the information about this world
     * @return a biome provider with just PLAINS
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new TARDISVoidBiomeProvider();
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 8, 68, 8);
    }

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

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = super.getDefaultPopulators(world);
        populators.add(new RoomPopulator(plugin));
        return populators;
    }
}
