package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HeatBlockRunnable implements Runnable {

    private final TARDIS plugin;

    public HeatBlockRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (String s : plugin.getTrackerKeeper().getHeatBlocks()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(s);
            if (location != null) {
                BlockData blockData = location.getBlock().getBlockData();
                if (blockData.getMaterial().equals(Material.MUSHROOM_STEM) && blockData.getAsString().equals("minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=true]")) {
                    // it's a heat block
                    meltBlock(location);
                }
            }
        }
    }

    private void meltBlock(Location location) {
        if (location != null) {
            int inner_count = 0;
            World world = location.getWorld();
            if (world != null) {
                Collections.shuffle(INNER_VECTORS);
                for (Vector inner : INNER_VECTORS) {
                    if (TARDISConstants.RANDOM.nextInt(100) > 25) {
                        Block ib = location.getWorld().getHighestBlockAt(location.clone().add(inner));
                        Material im = ib.getType();
                        if (im.equals(Material.SNOW)) {
                            Snow snow = (Snow) ib.getBlockData();
                            int layers = snow.getLayers() - 1;
                            if (layers > 0) {
                                snow.setLayers(layers);
                                ib.setBlockData(snow);
                            } else {
                                ib.setBlockData(TARDISConstants.AIR);
                            }
                            return;
                        } else if (im.equals(Material.ICE) || im.equals(Material.PACKED_ICE)) {
                            ib.setBlockData(Material.WATER.createBlockData());
                            return;
                        }
                        inner_count++;
                        if (inner_count == 8) {
                            Collections.shuffle(OUTER_VECTORS);
                            // not melting left in the inner blocks
                            for (Vector outer : OUTER_VECTORS) {
                                Block ob = location.getWorld().getHighestBlockAt(location.clone().add(outer));
                                Material om = ob.getType();
                                if (om.equals(Material.SNOW)) {
                                    ob.setBlockData(TARDISConstants.AIR);
                                    return;
                                } else if (om.equals(Material.ICE) || om.equals(Material.PACKED_ICE)) {
                                    ob.setBlockData(Material.WATER.createBlockData());
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static final List<Vector> INNER_VECTORS = new ArrayList<>(
        Arrays.asList(
            new Vector(-1.0, 0.0, 1.0),
            new Vector(0.0, 0.0, 1.0),
            new Vector(1.0, 0.0, 1.0),
            new Vector(-1.0, 0.0, 0.0),
            new Vector(1.0, 0.0, 0.0),
            new Vector(-1.0, 0.0, -1.0),
            new Vector(0.0, 0.0, -1.0),
            new Vector(1.0, 0.0, -1.0)
        )
    );

    private static final List<Vector> OUTER_VECTORS = new ArrayList<>(
        Arrays.asList(
            new Vector(-2.0, 0.0, 2.0),
            new Vector(-1.0, 0.0, 2.0),
            new Vector(0.0, 0.0, 2.0),
            new Vector(1.0, 0.0, 2.0),
            new Vector(2.0, 0.0, 2.0),
            new Vector(-2.0, 0.0, 1.0),
            new Vector(2.0, 0.0, 1.0),
            new Vector(-2.0, 0.0, 0.0),
            new Vector(2.0, 0.0, 0.0),
            new Vector(-2.0, 0.0, -1.0),
            new Vector(2.0, 0.0, -1.0),
            new Vector(-2.0, 0.0, -2.0),
            new Vector(-1.0, 0.0, -2.0),
            new Vector(0.0, 0.0, -2.0),
            new Vector(1.0, 0.0, -2.0),
            new Vector(2.0, 0.0, -2.0)
        )
    );
}
