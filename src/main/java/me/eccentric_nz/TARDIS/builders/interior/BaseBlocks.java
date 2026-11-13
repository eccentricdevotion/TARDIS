package me.eccentric_nz.TARDIS.builders.interior;

import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

public class BaseBlocks {

    public static BlockData[][][] set(String[][][] array, StructureVariant variant) {
        BlockData[][][] data = new BlockData[array.length][array[0].length][array[0][0].length];
        BlockData base = variant.base().createBlockData();
        BlockData wall = variant.wall().createBlockData();
        BlockData seed = variant.seed().createBlockData();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    String s = array[i][j][k];
                    if (s.contains("tuff_wall")) {
                        data[i][j][k] = wall;
                    } else if (s.contains("tuff")) {
                        data[i][j][k] = base;
                    } else if (s.contains("muddy_mangrove_roots")) {
                        data[i][j][k] = seed;
                    } else {
                        data[i][j][k] = Bukkit.createBlockData(s);
                    }
                }
            }
        }
        return data;
    }
}
