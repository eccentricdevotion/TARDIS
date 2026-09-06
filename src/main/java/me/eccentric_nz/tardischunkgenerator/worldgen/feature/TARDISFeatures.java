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
package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.custombiome.CubicMaterial;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import org.bukkit.Material;

import java.util.concurrent.ThreadLocalRandom;

public class TARDISFeatures {

    public static final TARDISGrassData GRASS;
    public static final TARDISGrassData VASTIAL;
    public static final TARDISTreeData SKARO_TREE;
    public static final TARDISTreeData GALLIFREY_TREE_RED_SAND;
    public static final TARDISTreeData GALLIFREY_TREE_TERRACOTTA;
    public static final TARDISTreeData RANDOM_TREE;
    public static final TARDISTreeData LUSH;
    public static final TARDISTreeData FOREST;
    public static final TARDISTreeData JUNGLE;
    public static final TARDISTreeData TAIGA;
    public static final TARDISTreeData DESERT;
    public static final TARDISTreeData ACACIA;
    public static final TARDISTreeData SWAMP;
    public static final TARDISTreeData OCEAN;
    public static final TARDISTreeData FROZEN;
    public static final TARDISTreeData MUSHROOM;
    public static final TARDISTreeData CHERRY;
    public static final TARDISTreeData SCULK;
    public static final TARDISTreeData PALE;
    public static final TARDISTreeData DARK_OAK;
    public static final TARDISTreeData CAVE;

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
        GRASS = new TARDISGrassData(RuleBasedStateProvider.simple(Blocks.GRASS_BLOCK), BlockPredicate.matchesBlocks(Blocks.TERRACOTTA, Blocks.RED_SAND), UniformInt.of(2, 8), 2);
        VASTIAL = new TARDISGrassData(RuleBasedStateProvider.simple(Blocks.CONCRETE_POWDER.white()), BlockPredicate.matchesBlocks(Blocks.STONE, Blocks.GRASS_BLOCK), UniformInt.of(2, 8), 2);
        SKARO_TREE = new TARDISTreeData(Material.SAND, skaro_wood, skaro_leaves, skaro_decor, true);
        GALLIFREY_TREE_RED_SAND = new TARDISTreeData(Material.RED_SAND, gallifrey_sand_wood, gallifrey_sand_leaves, gallifrey_sand_decor, true);
        GALLIFREY_TREE_TERRACOTTA = new TARDISTreeData(Material.TERRACOTTA, gallifrey_terracotta_wood, gallifrey_terracotta_leaves, gallifrey_terracotta_decor, true);
        RANDOM_TREE = new TARDISTreeData(Material.GRASS_BLOCK, getRandomMaterial(), getRandomMaterial(), getRandomMaterial(), true);
        // Material base, Material stem, Material hat, Material decor
        ACACIA = new TARDISTreeData(Material.TERRACOTTA, Material.ACACIA_LOG, Material.ACACIA_LEAVES, Material.VERDANT_FROGLIGHT, true);
        CAVE = new TARDISTreeData(Material.DEEPSLATE, Material.CRIMSON_STEM, Material.CRIMSON_HYPHAE, Material.GLOWSTONE, true);
        CHERRY = new TARDISTreeData(Material.CALCITE, Material.CHERRY_LOG, Material.CHERRY_LEAVES, Material.OCHRE_FROGLIGHT, true);
        DESERT = new TARDISTreeData(Material.SANDSTONE, Material.CACTUS, Material.RED_SANDSTONE, Material.PEARLESCENT_FROGLIGHT, true);
        DARK_OAK = new TARDISTreeData(Material.DRIPSTONE_BLOCK, Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES, Material.GLOWSTONE, true);
        FOREST = new TARDISTreeData(Material.STONE, Material.OAK_LOG, Material.OAK_LEAVES, Material.SHROOMLIGHT, true);
        FROZEN = new TARDISTreeData(Material.STONE, Material.SNOW_BLOCK, Material.BLUE_ICE, Material.SEA_LANTERN, true);
        JUNGLE = new TARDISTreeData(Material.MOSSY_COBBLESTONE, Material.JUNGLE_LOG, Material.JUNGLE_LEAVES, Material.OCHRE_FROGLIGHT, true);
        LUSH = new TARDISTreeData(Material.STONE, Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES, Material.GLOWSTONE, true);
        MUSHROOM = new TARDISTreeData(Material.MYCELIUM, Material.MUSHROOM_STEM, Material.RED_MUSHROOM_BLOCK, Material.SHROOMLIGHT, true);
        OCEAN = new TARDISTreeData(Material.PRISMARINE, Material.BIRCH_LOG, Material.BIRCH_LEAVES, Material.SEA_LANTERN, true);
        SCULK = new TARDISTreeData(Material.DEEPSLATE, Material.WARPED_STEM, Material.WARPED_HYPHAE, Material.SHROOMLIGHT, true);
        PALE = new TARDISTreeData(Material.SULFUR, Material.PALE_OAK_LOG, Material.PALE_OAK_LEAVES, Material.PEARLESCENT_FROGLIGHT, true);
        SWAMP = new TARDISTreeData(Material.MUD, Material.MANGROVE_LOG, Material.MANGROVE_LOG, Material.VERDANT_FROGLIGHT, true);
        TAIGA = new TARDISTreeData(Material.STONE, Material.SPRUCE_LOG, Material.SPRUCE_LEAVES, Material.JACK_O_LANTERN, true);
    }

    private static Material getRandomMaterial() {
        return CubicMaterial.cubes.get(ThreadLocalRandom.current().nextInt(CubicMaterial.cubes.size()));
    }
}
