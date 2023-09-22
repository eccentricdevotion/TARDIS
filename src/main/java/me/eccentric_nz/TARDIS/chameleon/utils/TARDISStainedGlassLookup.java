/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers;

/**
 * Lookup for Chameleon preset block materialisation.
 * This calculates stained-glass colours from block types.
 * For example a BRICKS block would map to RED_STAINED_GLASS.
 *
 * @author eccentric_nz
 */
public class TARDISStainedGlassLookup {

    public static Material stainedGlassFromMaterial(World world, Material material) {
        if (world == null) {
            world = Bukkit.getWorlds().get(0);
        }
        ServerLevel level = ((CraftWorld)world).getHandle();
        Block nmsBlock = CraftMagicNumbers.getBlock(material);
        MapColor mapColor = nmsBlock.defaultBlockState().getMapColor(level, new BlockPos(0, 75, 0));
        Color colorRGB = Color.fromRGB(mapColor.col);
        int red = colorRGB.getRed();
        int green = colorRGB.getGreen();
        int blue = colorRGB.getBlue();
        int distance = Integer.MAX_VALUE;
        DyeColor closest = null;
        for (DyeColor dye : DyeColor.values()) {
            Color color = dye.getColor();
            int dist = Math.abs(color.getRed() - red) + Math.abs(color.getGreen() - green) + Math.abs(color.getBlue() - blue);
            if (dist < distance) {
                distance = dist;
                closest = dye;
            }
        }
        if (closest != null) {
            return Material.getMaterial((closest.name() + "_STAINED_GLASS"));
        }
        return Material.BLUE_STAINED_GLASS;
    }
}
