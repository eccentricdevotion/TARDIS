/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.storage;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author eccentric_nz
 */
public class TVMBlock {

    private Block block;
    private BlockData blockData;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }
}
