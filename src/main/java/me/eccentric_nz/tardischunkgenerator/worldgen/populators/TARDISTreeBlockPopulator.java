package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import me.eccentric_nz.tardischunkgenerator.worldgen.feature.CustomTree;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;
import java.util.logging.Level;

public class TARDISTreeBlockPopulator extends BlockPopulator {

    private final TARDISTree TREE;
    private final int chance;

    public TARDISTreeBlockPopulator(TARDISTree TREE, int chance) {
        this.TREE = TREE;
        this.chance = chance;
    }

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {

        if (chance > 0 && random.nextInt(10) > chance) {
            return;
        }
        int treeX = x * 16 + random.nextInt(16);
        int treeZ = z * 16 + random.nextInt(16);
        int treeY = 128;
        if (!limitedRegion.isInRegion(treeX, treeY, treeZ)) {
            Bukkit.getLogger().log(Level.INFO, TREE + " tree location (" + treeX + "," + treeY + "," + treeZ + ") is not in limited region!");
            return;
        }
        for (int i = 128; i > 60; i--) {
            if (limitedRegion.getType(treeX, treeY, treeZ).equals(Material.AIR)) {
                treeY--;
            } else {
                break;
            }
        }
        if (treeY > 60 && limitedRegion.isInRegion(treeX, treeY, treeZ) && !limitedRegion.getType(treeX, treeY, treeZ).equals(Material.WATER)) {
            CustomTree.grow(TREE, treeX, treeY, treeZ, limitedRegion);
        }
    }
}
