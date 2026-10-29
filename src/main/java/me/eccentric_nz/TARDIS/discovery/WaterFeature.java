package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.WeightedChoice;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class WaterFeature {

    public static final WeightedChoice<Material> floor = new WeightedChoice<Material>()
            .add(60, Material.CLAY)
            .add(10, Material.MOSSY_COBBLESTONE)
            .add(10, Material.PODZOL)
            .add(10, Material.MOSS_BLOCK)
            .add(10, Material.TUFF);
    public static final WeightedChoice<BlockData[]> column = new WeightedChoice<BlockData[]>()
            .add(76, new BlockData[]{TARDISConstants.WATER, TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.TALL_GRASS.createBlockData("[half=lower]"), Material.TALL_GRASS.createBlockData("[half=upper]")})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.SHORT_GRASS.createBlockData()})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.AZALEA.createBlockData()})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.FLOWERING_AZALEA.createBlockData()})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=1]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=2]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=3]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=4]"), TARDISConstants.WATER});
}
