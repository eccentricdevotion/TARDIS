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
package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import me.eccentric_nz.tardischunkgenerator.worldgen.feature.GrassPlacer;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.CraftRegionAccessor;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class GallifreyGrassPopulator extends BlockPopulator {

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        if (isFeatureChunk(random, 30)) {
            int grassX = x * 16;
            int grassZ = z * 16;
            int grassY = 128;
            for (int i = 128; i > 60; i--) {
                if (limitedRegion.getType(grassX, grassY, grassZ).equals(Material.AIR)) {
                    grassY--;
                } else {
                    break;
                }
            }
            if (grassY > 60 && limitedRegion.isInRegion(grassX, grassY, grassZ) && !limitedRegion.getType(grassX, grassY, grassZ).equals(Material.WATER)) {
                WorldGenLevel level = ((CraftRegionAccessor) limitedRegion).getHandle();
                new GrassPlacer().place(TARDISFeatures.GRASS, level, new BlockPos(grassX, grassY, grassZ));
            }
        }
    }

    public boolean isFeatureChunk(Random random, int chance) {
        return random.nextInt(100) < chance;
    }
}
