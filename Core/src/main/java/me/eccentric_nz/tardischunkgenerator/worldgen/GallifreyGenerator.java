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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.worldgen;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.GallifreyBiomeProvider;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.GallifreyGrassPopulator;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.GallifreyStructurePopulator;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.TARDISTreeBlockPopulator;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class GallifreyGenerator extends ChunkGenerator {

    private final TARDIS plugin;

    public GallifreyGenerator(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Generates an empty world!
     */
    @Override
    public boolean shouldGenerateNoise() {
        return true;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    /**
     * Sets the entire world to BADLANDS biomes
     *
     * @param worldInfo the information about this world
     * @return the Gallifrey biome provider
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new GallifreyBiomeProvider();
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = super.getDefaultPopulators(world);
        populators.add(new GallifreyGrassPopulator());
        populators.add(new TARDISTreeBlockPopulator(TARDISTree.GALLIFREY_SAND, 4));
        populators.add(new TARDISTreeBlockPopulator(TARDISTree.GALLIFREY_TERRACOTTA, 4));
        populators.add(new GallifreyStructurePopulator(plugin));
        return populators;
    }
}
