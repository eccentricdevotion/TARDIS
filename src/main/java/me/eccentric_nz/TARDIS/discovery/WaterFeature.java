package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.WeightedChoice;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class WaterFeature {

    public static final WeightedChoice<Material> tuffFloor = new WeightedChoice<Material>()
            .add(60, Material.CLAY)
            .add(10, Material.MOSSY_COBBLESTONE)
            .add(10, Material.PODZOL)
            .add(10, Material.MOSS_BLOCK)
            .add(10, Material.TUFF);
    public static final WeightedChoice<Material> cobbleFloor = new WeightedChoice<Material>()
            .add(60, Material.CLAY)
            .add(10, Material.MUD)
            .add(10, Material.SCULK)
            .add(10, Material.BLACKSTONE)
            .add(10, Material.COBBLESTONE);
    public static final WeightedChoice<Material> netherFloor = new WeightedChoice<Material>()
            .add(60, Material.SOUL_SOIL)
            .add(10, Material.SOUL_SAND)
            .add(10, Material.NETHERRACK);
    public static final WeightedChoice<Material> endFloor = new WeightedChoice<Material>()
            .add(60, Material.CLAY)
            .add(10, Material.BROWN_MUSHROOM_BLOCK)
            .add(10, Material.POTENT_SULFUR)
            .add(10, Material.PURPUR_BLOCK)
            .add(10, Material.END_STONE);
    public static final WeightedChoice<BlockData[]> tuffColumn = new WeightedChoice<BlockData[]>()
            .add(76, new BlockData[]{TARDISConstants.WATER, TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.TALL_GRASS.createBlockData("[half=lower]"), Material.TALL_GRASS.createBlockData("[half=upper]")})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.SHORT_GRASS.createBlockData()})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.AZALEA.createBlockData()})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.FLOWERING_AZALEA.createBlockData()})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=1]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=2]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=3]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=4]"), TARDISConstants.WATER});
    public static final WeightedChoice<BlockData[]> cobbleColumn = new WeightedChoice<BlockData[]>()
            .add(76, new BlockData[]{TARDISConstants.WATER, TARDISConstants.WATER})
            .add(6, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.TALL_GRASS.createBlockData("[half=lower]"), Material.TALL_GRASS.createBlockData("[half=upper]")})
            .add(2, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.SCULK_SENSOR.createBlockData()})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.SCULK_SHRIEKER.createBlockData()})
            .add(3, new BlockData[]{Material.DIRT.createBlockData(), Material.PODZOL.createBlockData(), Material.POINTED_DRIPSTONE.createBlockData()})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=1]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=2]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=3]"), TARDISConstants.WATER})
            .add(3, new BlockData[]{Material.SEA_PICKLE.createBlockData("[waterlogged=true,pickles=4]"), TARDISConstants.WATER});
    public static final WeightedChoice<BlockData[]> netherColumn = new WeightedChoice<BlockData[]>()
            .add(64, new BlockData[]{TARDISConstants.LAVA, TARDISConstants.LAVA})
            .add(6, new BlockData[]{Material.NETHERRACK.createBlockData(), Material.WARPED_NYLIUM.createBlockData(), Material.WARPED_ROOTS.createBlockData()})
            .add(6, new BlockData[]{Material.NETHERRACK.createBlockData(), Material.CRIMSON_NYLIUM.createBlockData(), Material.CRIMSON_ROOTS.createBlockData()})
            .add(6, new BlockData[]{Material.NETHERRACK.createBlockData(), Material.WARPED_NYLIUM.createBlockData(), Material.WARPED_FUNGUS.createBlockData()})
            .add(6, new BlockData[]{Material.NETHERRACK.createBlockData(), Material.CRIMSON_NYLIUM.createBlockData(), Material.CRIMSON_FUNGUS.createBlockData()})
            .add(3, new BlockData[]{Material.SOUL_SOIL.createBlockData(), Material.SOUL_SOIL.createBlockData(), Material.SOUL_FIRE.createBlockData()});
    public static final WeightedChoice<BlockData[]> endColumn = new WeightedChoice<BlockData[]>()
            .add(76, new BlockData[]{TARDISConstants.WATER, TARDISConstants.WATER})
            .add(5, new BlockData[]{Material.END_STONE.createBlockData(), Material.END_STONE.createBlockData(), Material.CHORUS_PLANT.createBlockData("[down=true]"), Material.CHORUS_FLOWER.createBlockData()})
            .add(1, new BlockData[]{Material.END_STONE.createBlockData(), Material.END_STONE.createBlockData(), Material.SHULKER_BOX.createBlockData()})
            .add(1, new BlockData[]{Material.END_STONE.createBlockData(), Material.END_STONE.createBlockData(), Material.ENDER_CHEST.createBlockData()})
            .add(3, new BlockData[]{Material.END_STONE.createBlockData(), Material.END_STONE.createBlockData(), Material.END_ROD.createBlockData()})
            .add(3, new BlockData[]{Material.END_STONE.createBlockData(), Material.BEDROCK.createBlockData()});
}
