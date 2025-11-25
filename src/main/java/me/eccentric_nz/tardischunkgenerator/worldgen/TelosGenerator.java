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
import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.TelosBiomeProvider;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.GallifreyGrassPopulator;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.TelosStructurePopulator;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.TelosVastialPopulator;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TelosGenerator extends ChunkGenerator {

    private final TARDIS plugin;

    public TelosGenerator(TARDIS plugin) {
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
     * Sets the entire world to snowy and frozen biomes
     *
     * @param worldInfo the information about this world
     * @return the Telos biome provider
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new TelosBiomeProvider();
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = super.getDefaultPopulators(world);
        populators.add(new TelosVastialPopulator());
        populators.add(new TelosStructurePopulator(plugin));
        return populators;
    }
}
