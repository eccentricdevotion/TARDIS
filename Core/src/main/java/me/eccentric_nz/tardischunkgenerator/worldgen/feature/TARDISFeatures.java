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

import java.util.concurrent.ThreadLocalRandom;
import me.eccentric_nz.tardischunkgenerator.custombiome.CubicMaterial;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import org.bukkit.Material;

public class TARDISFeatures {

    public static TARDISGrassData GRASS;
    public static TARDISTreeData SKARO_TREE;
    public static TARDISTreeData GALLIFREY_TREE_RED_SAND;
    public static TARDISTreeData GALLIFREY_TREE_TERRACOTTA;
    public static TARDISTreeData RANDOM_TREE;

    static {
        GRASS = new TARDISGrassData(RuleBasedBlockStateProvider.simple(Blocks.GRASS_BLOCK), BlockPredicate.matchesBlocks(Blocks.TERRACOTTA, Blocks.RED_SAND), UniformInt.of(2, 8), 2);
        SKARO_TREE = new TARDISTreeData(Material.SAND, Material.ACACIA_LOG, Material.SLIME_BLOCK, Material.HONEY_BLOCK, true);
        GALLIFREY_TREE_RED_SAND = new TARDISTreeData(Material.RED_SAND, Material.STRIPPED_BIRCH_LOG, Material.COBWEB, Material.RED_WOOL, true);
        GALLIFREY_TREE_TERRACOTTA = new TARDISTreeData(Material.TERRACOTTA, Material.STRIPPED_BIRCH_LOG, Material.COBWEB, Material.RED_WOOL, true);
        RANDOM_TREE = new TARDISTreeData(Material.GRASS_BLOCK, getRandomMaterial(), getRandomMaterial(), getRandomMaterial(), true);
    }

    private static Material getRandomMaterial() {
        return CubicMaterial.cubes.get(ThreadLocalRandom.current().nextInt(CubicMaterial.cubes.size()));
    }
}
