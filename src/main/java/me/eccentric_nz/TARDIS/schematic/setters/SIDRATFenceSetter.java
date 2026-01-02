package me.eccentric_nz.TARDIS.schematic.setters;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;
import java.util.Map;

public class SIDRATFenceSetter {

    public static void update(HashMap<Block, BlockData> fences) {
        for (Map.Entry<Block, BlockData> map : fences.entrySet()) {
            map.getKey().setBlockData(map.getValue());
        }
    }
}
