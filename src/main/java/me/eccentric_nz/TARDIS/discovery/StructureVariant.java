package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.utility.WeightedChoice;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public record StructureVariant(
        Material[] gradient,
        // variant blocks
        Material base, Material seed, Material wall,
        // water features
        WeightedChoice<Material> floor, WeightedChoice<BlockData[]> column,
        // hanging features
        BlockData hanging, BlockData hanging_light, BlockData hanging_end, Material decor,
        // floor carpet
        BlockData carpet
) {
}
