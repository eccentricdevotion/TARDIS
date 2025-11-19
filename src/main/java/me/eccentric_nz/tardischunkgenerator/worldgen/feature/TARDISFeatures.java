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
package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.custombiome.CubicMaterial;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import org.bukkit.Material;

import java.util.concurrent.ThreadLocalRandom;

public class TARDISFeatures {

    public static final TARDISGrassData GRASS;
    public static final TARDISGrassData VASTIAL;
    public static final TARDISTreeData SKARO_TREE;
    public static final TARDISTreeData GALLIFREY_TREE_RED_SAND;
    public static final TARDISTreeData GALLIFREY_TREE_TERRACOTTA;
    public static final TARDISTreeData RANDOM_TREE;

    static {
        // get configured materials
        Material skaro_wood;
        Material skaro_leaves;
        Material skaro_decor;
        Material gallifrey_sand_wood;
        Material gallifrey_sand_leaves;
        Material gallifrey_sand_decor;
        Material gallifrey_terracotta_wood;
        Material gallifrey_terracotta_leaves;
        Material gallifrey_terracotta_decor;
        try {
            skaro_wood = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("skaro.wood", "ACACIA_LOG"));
            skaro_leaves = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("skaro.leaves", "SLIME_BLOCK"));
            skaro_decor = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("skaro.decoration", "HONEY_BLOCK"));
            gallifrey_sand_wood = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("gallifrey.red_sand.wood", "STRIPPED_BIRCH_LOG"));
            gallifrey_sand_leaves = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("gallifrey.red_sand.leaves", "COBWEB"));
            gallifrey_sand_decor = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("gallifrey.red_sand.decoration", "RED_WOOL"));
            gallifrey_terracotta_wood = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("gallifrey.terracotta.wood", "STRIPPED_BIRCH_LOG"));
            gallifrey_terracotta_leaves = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("gallifrey.terracotta.leaves", "COBWEB"));
            gallifrey_terracotta_decor = Material.valueOf(TARDIS.plugin.getGeneratorConfig().getString("gallifrey.terracotta.decoration", "RED_WOOL"));
        } catch (IllegalArgumentException e) {
            skaro_wood = Material.ACACIA_LOG;
            skaro_leaves = Material.SLIME_BLOCK;
            skaro_decor = Material.HONEY_BLOCK;
            gallifrey_sand_wood = Material.STRIPPED_BIRCH_LOG;
            gallifrey_sand_leaves = Material.COBWEB;
            gallifrey_sand_decor = Material.RED_WOOL;
            gallifrey_terracotta_wood = Material.STRIPPED_BIRCH_LOG;
            gallifrey_terracotta_leaves = Material.COBWEB;
            gallifrey_terracotta_decor = Material.RED_WOOL;
        }
        GRASS = new TARDISGrassData(RuleBasedBlockStateProvider.simple(Blocks.GRASS_BLOCK), BlockPredicate.matchesBlocks(Blocks.TERRACOTTA, Blocks.RED_SAND), UniformInt.of(2, 8), 2);
        VASTIAL = new TARDISGrassData(RuleBasedBlockStateProvider.simple(Blocks.WHITE_CONCRETE_POWDER), BlockPredicate.matchesBlocks(Blocks.STONE, Blocks.GRASS_BLOCK), UniformInt.of(2, 8), 2);
        SKARO_TREE = new TARDISTreeData(Material.SAND, skaro_wood, skaro_leaves, skaro_decor, true);
        GALLIFREY_TREE_RED_SAND = new TARDISTreeData(Material.RED_SAND, gallifrey_sand_wood, gallifrey_sand_leaves, gallifrey_sand_decor, true);
        GALLIFREY_TREE_TERRACOTTA = new TARDISTreeData(Material.TERRACOTTA, gallifrey_terracotta_wood, gallifrey_terracotta_leaves, gallifrey_terracotta_decor, true);
        RANDOM_TREE = new TARDISTreeData(Material.GRASS_BLOCK, getRandomMaterial(), getRandomMaterial(), getRandomMaterial(), true);
    }

    private static Material getRandomMaterial() {
        return CubicMaterial.cubes.get(ThreadLocalRandom.current().nextInt(CubicMaterial.cubes.size()));
    }
}
