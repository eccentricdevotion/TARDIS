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
package me.eccentric_nz.tardischunkgenerator.helpers;

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftMagicNumbers;

import java.util.logging.Level;

public class GetBlockColours {

    private static Color getColor(Material material) {
        Block block = CraftMagicNumbers.getBlock(material);
        MapColor mc = block.defaultMapColor();
        return Color.fromRGB(mc.col);
    }

    public static void list() {
        // output lines
        for (Material m : Material.values()) {
            if (m.isBlock()) {
                Color color = getColor(m);
                TARDIS.plugin.getLogger().log(Level.INFO, String.format("%s(new Color(%d, %d, %d)),", m, color.getRed(), color.getGreen(), color.getBlue()));
            }
        }
    }
}
