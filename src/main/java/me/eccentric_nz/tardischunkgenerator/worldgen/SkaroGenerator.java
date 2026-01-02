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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.SkaroBiomeProvider;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.SkaroStructurePopulator;
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
public class SkaroGenerator extends ChunkGenerator {

    private final TARDIS plugin;

    public SkaroGenerator(TARDIS plugin) {
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
     * Sets the entire world to the DESERT biome
     *
     * @param worldInfo the information about this world
     * @return the Skaro biome provider
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new SkaroBiomeProvider();
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = super.getDefaultPopulators(world);
        populators.add(new TARDISTreeBlockPopulator(TARDISTree.SKARO, 4));
        populators.add(new SkaroStructurePopulator(plugin));
        return populators;
    }
}
