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
