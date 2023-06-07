package me.eccentric_nz.tardischunkgenerator.helpers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;

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
                Bukkit.getLogger().log(Level.INFO, String.format("%s(new Color(%d, %d, %d)),", m, color.getRed(), color.getGreen(), color.getBlue()));
            }
        }
    }
}
