package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetApiaries;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TARDISBeeWaker implements Runnable {

    private final TARDIS plugin;

    public TARDISBeeWaker(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        World world = plugin.getServer().getWorlds().get(0);
        if (world != null) {
            long ticks = world.getTime();
            if (ticks > 0 && ticks < 600) {
                wakeBees();
            }
        }
    }

    private void wakeBees() {
        // get apiaries
        ResultSetApiaries rsa = new ResultSetApiaries(plugin);
        if (rsa.resultSet()) {
            for (Location location : rsa.getData()) {
                // scan chunk for beehives and bee nests
                Chunk chunk = location.getChunk();
                if (chunk.isLoaded()) {
                    int cx = chunk.getX() << 4; // chunks x
                    int cz = chunk.getZ() << 4; // chunks z
                    for (int x = cx; x < cx + 16; x++) {
                        for (int z = cz; z < cz + 16; z++) {
                            for (int y = 51; y < 96; y++) { // limit Y values to within TARDIS ARS levels
                                Block block = chunk.getWorld().getBlockAt(x, y, z);
                                Material material = block.getType();
                                if (material.equals(Material.BEE_NEST) || material.equals(Material.BEEHIVE)) {
                                    plugin.getTardisHelper().releaseBees(block);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
