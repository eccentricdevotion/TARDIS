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
package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.BiomeProfile;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.BiomeStyle;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.CustomTree;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class CaveTreePopulator extends BlockPopulator {

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        int attempts = 3;
        for (int i = 0; i < attempts; ++i) {
            int treeX = x * 16 + random.nextInt(16);
            int treeZ = z * 16 + random.nextInt(16);
            int treeY = Math.min(random.nextInt(128) + 32, 128);
            if (!limitedRegion.isInRegion(treeX, treeY, treeZ)) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, " Tree location (" + treeX + "," + treeY + "," + treeZ + ") is not in limited region!");
                return;
            }
            Material startBlock = limitedRegion.getType(treeX, treeY, treeZ);
            // find first air block
            while (!startBlock.isAir()) {
                treeY--;
                startBlock = limitedRegion.getType(treeX, treeY, treeZ);
            }
            // find next solid block
            while (startBlock.isAir() && treeY > 3) {
                treeY--;
                startBlock = limitedRegion.getType(treeX, treeY, treeZ);
            }
            Biome biome = worldInfo.vanillaBiomeProvider().getBiome(worldInfo, x, treeY, z);
            BiomeStyle biomeStyle = BiomeStyle.fromBiome(biome);
            BiomeProfile biomeProfile = BiomeStyle.getProfile(biomeStyle);
            TARDISTree TREE = biomeProfile.tree();
            if (limitedRegion.isInRegion(treeX, treeY, treeZ) && !limitedRegion.getType(treeX, treeY, treeZ).equals(Material.WATER)) {
                CustomTree.grow(TREE, treeX, treeY + 1, treeZ, limitedRegion, biomeProfile);
            }
        }
    }
}
