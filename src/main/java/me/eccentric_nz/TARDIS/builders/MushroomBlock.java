package me.eccentric_nz.TARDIS.builders;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class MushroomBlock {

    private final Block block;
    private final BlockData blockData;

    public MushroomBlock(Block block, BlockData blockData) {
        this.block = block;
        this.blockData = blockData;
    }

    public Block getBlock() {
        return block;
    }

    public BlockData getBlockData() {
        return blockData;
    }
}
