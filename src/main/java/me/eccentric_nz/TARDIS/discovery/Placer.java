package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Rotatable;

public class Placer {

    private final TARDIS plugin;
    public final BlockData vines = Material.CAVE_VINES_PLANT.createBlockData();
    public final BlockData glow = Material.CAVE_VINES_PLANT.createBlockData("[berries=true]");

    public Placer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void makeWaterFeature(Block block) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            block.setType(WaterFeature.floor.next());
            BlockData[] data = WaterFeature.column.next();
            int i = 1;
            for (BlockData b : data) {
                block.getRelative(BlockFace.UP, i).setBlockData(b);
                i++;
            }
        }, 20L);
    }

    public void makeVines(Block below) {
        int r = TARDISConstants.RANDOM.nextInt(10);
        if (r < 1) {
            // set a spore blossom
            below.setType(Material.SPORE_BLOSSOM);
        } else {
            below.setType(Material.CAVE_VINES);
            for (int i = 0; i < r; i++) {
                below.getRelative(BlockFace.DOWN, i).setBlockData(TARDISConstants.RANDOM.nextInt(4) == 1 ? vines : glow);
            }
            below.getRelative(BlockFace.DOWN, r).setType(Material.CAVE_VINES);
        }
    }

    public void makeGlowLichen(Block tuff) {
        BlockFace[] faces = {
                BlockFace.UP,
                BlockFace.NORTH,
                BlockFace.SOUTH,
                BlockFace.EAST,
                BlockFace.WEST
        };
        for (BlockFace face : faces) {
            Block targetBlock = tuff.getRelative(face);
            // only place if the target space is air and passes a random chance threshold
            if (targetBlock.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 2) {
                targetBlock.setType(Material.GLOW_LICHEN);
                // configure the lichen to attach properly to the specific face of the tuff block
                if (targetBlock.getBlockData() instanceof MultipleFacing lichen) {
                    lichen.setFace(face.getOppositeFace(), true);
                    targetBlock.setBlockData(lichen);
                }
            }
        }
    }

    public void makeMossCarpets(Block tuff) {
        Block above = tuff.getRelative(BlockFace.UP);
        // check if the space above is air and passes a random chance threshold
        if (above.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 5) {
            above.setType(Material.MOSS_CARPET);
        }
    }

    public void makeHeads(Block tuff) {
        Block above = tuff.getRelative(BlockFace.UP);
        // check if the space above is air and passes a random chance threshold
        if (above.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 1) {
            above.setType(Material.TUFF_WALL);
            Block head = above.getRelative(BlockFace.UP);
            Rotatable rotatable = (Rotatable) Material.SKELETON_SKULL.createBlockData();
            rotatable.setRotation(BlockFace.values()[TARDISConstants.RANDOM.nextInt(6,18)]);
            head.setBlockData(rotatable);
        }
    }
}
