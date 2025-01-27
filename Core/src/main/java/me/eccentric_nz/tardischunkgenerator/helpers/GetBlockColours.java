package me.eccentric_nz.tardischunkgenerator.helpers;

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.util.CraftMagicNumbers;

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
