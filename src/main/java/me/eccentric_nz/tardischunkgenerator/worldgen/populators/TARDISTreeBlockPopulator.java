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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.CustomTree;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import org.bukkit.Material;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

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
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, TREE + " tree location (" + treeX + "," + treeY + "," + treeZ + ") is not in limited region!");
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
