package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;

public class BiomeSetter {

    public static void setBiome(BuildData bd, boolean umbrella, int loops) {
        World world = bd.getLocation().getWorld();
        int x = bd.getLocation().getBlockX();
        int y = bd.getLocation().getBlockY();
        int z = bd.getLocation().getBlockZ();
        List<Chunk> chunks = new ArrayList<>();
        Chunk chunk = bd.getLocation().getChunk();
        chunks.add(chunk);
        // load the chunk
        int cx = bd.getLocation().getBlockX() >> 4;
        int cz = bd.getLocation().getBlockZ() >> 4;
        if (!world.loadChunk(cx, cz, false)) {
            world.loadChunk(cx, cz, true);
        }
        while (!chunk.isLoaded()) {
            world.loadChunk(chunk);
        }
        // set the biome
        for (int c = -3; c < 4; c++) {
            for (int r = -3; r < 4; r++) {
                for (int l = -1; l < 5; l += 2) {
                    world.setBiome(x + c, y + l, z + r, Biome.DEEP_OCEAN);
                }
                // TODO check re-adding umbrella if rebuilding
                if (umbrella && TARDISConstants.NO_RAIN.contains(bd.getBiome())) {
                    // add an invisible roof
                    if (loops == 3) {
                        TARDISBlockSetters.setBlock(world, x + c, 255, z + r, Material.BARRIER);
                    } else {
                        TARDISBlockSetters.setBlockAndRemember(world, x + c, 255, z + r, Material.BARRIER, bd.getTardisID());
                    }
                }
                Chunk tmp_chunk = world.getChunkAt(new Location(world, x + c, 64, z + r));
                if (!chunks.contains(tmp_chunk)) {
                    chunks.add(tmp_chunk);
                }
            }
        }
        // refresh the chunks
        chunks.forEach((c) -> {
            TARDIS.plugin.getTardisHelper().refreshChunk(c);
        });
    }

    public static boolean restoreBiome(Location l, Biome biome) {
        if (l != null && biome != null) {
            int sbx = l.getBlockX();
            int sby = l.getBlockY();
            int sbz = l.getBlockZ();
            World w = l.getWorld();
            List<Chunk> chunks = new ArrayList<>();
            Chunk chunk = l.getChunk();
            chunks.add(chunk);
            // reset biome and it's not The End
            if (l.getBlock().getBiome().equals(Biome.DEEP_OCEAN) || l.getBlock().getBiome().equals(Biome.THE_VOID) || (l.getBlock().getBiome().equals(Biome.THE_END) && !l.getWorld().getEnvironment().equals(World.Environment.THE_END)) && biome != null) {
                // reset the biome
                for (int c = -3; c < 4; c++) {
                    for (int r = -3; r < 4; r++) {
                        try {
                            for (int y = -1; y < 5; y += 2) {
                                w.setBiome(sbx + c, sby + y, sbz + r, biome);
                            }
                            Chunk tmp_chunk = w.getChunkAt(new Location(w, sbx + c, 64, sbz + r));
                            if (!chunks.contains(tmp_chunk)) {
                                chunks.add(tmp_chunk);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
                // refresh the chunks
                chunks.forEach((c) -> {
                    TARDIS.plugin.getTardisHelper().refreshChunk(c);
                });
            }
            return true;
        }
        return true;
    }
}
