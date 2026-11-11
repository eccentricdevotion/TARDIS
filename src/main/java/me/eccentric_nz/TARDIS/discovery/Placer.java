package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.WeightedChoice;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrushableBlock;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Rotatable;

public class Placer {

    private final TARDIS plugin;

    public Placer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addWaterFeature(Block block, StructureVariant variant) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            block.setType(variant.floor().next());
            BlockData[] data = variant.column().next();
            int i = 1;
            for (BlockData b : data) {
                block.getRelative(BlockFace.UP, i).setBlockData(b);
                i++;
            }
        }, 20L);
    }

    public void addHangingLights(Block below, StructureVariant variant, int length) {
        int r = TARDISConstants.RANDOM.nextInt(length);
        if (r < 1) {
            // set decor
            below.setType(variant.decor());
        } else {
            BlockState state = below.getState();
            state.setBlockData(variant.hanging());
            state.update(true, false);
            for (int i = 0; i < r; i++) {
                BlockState relative = below.getRelative(BlockFace.DOWN, i).getState();
                if (relative.getType().isAir()) {
                    relative.setBlockData(TARDISConstants.RANDOM.nextInt(4) == 1 ? variant.hanging() : variant.hanging_light());
                    relative.update(true, false);
                }
            }
            BlockState end = below.getRelative(BlockFace.DOWN, r).getState();
            if (end.getType().isAir()) {
                end.setBlockData(variant.hanging_end());
                end.update(true, false);
            }
        }
    }

    public void addGlowLichen(Block tuff) {
        BlockFace[] faces = {
                BlockFace.UP,
                BlockFace.NORTH,
                BlockFace.SOUTH,
                BlockFace.EAST,
                BlockFace.WEST
        };
        for (BlockFace face : faces) {
            Block targetBlock = tuff.getRelative(face);
            BlockState state = targetBlock.getState();
            // only place if the target space is air and passes a random chance threshold
            if (state.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 2) {
                state.setType(Material.GLOW_LICHEN);
                // configure the lichen to attach properly to the specific face of the tuff block
                if (state.getBlockData() instanceof MultipleFacing lichen) {
                    lichen.setFace(face.getOppositeFace(), true);
                    state.setBlockData(lichen);
                }
                state.update(true, false);
            }
        }
    }

    public void addCarpet(Block base, StructureVariant variant) {
        Block above = base.getRelative(BlockFace.UP);
        // check if the space above is air and passes a random chance threshold
        if (above.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 5) {
            above.setBlockData(variant.carpet());
        }
    }

    public void addHead(Block base, StructureVariant variant) {
        Block above = base.getRelative(BlockFace.UP);
        // check if the space above is air and passes a random chance threshold
        if (above.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 1) {
            above.setType(variant.wall());
            Block head = above.getRelative(BlockFace.UP);
            Rotatable rotatable = (Rotatable) Material.SKELETON_SKULL.createBlockData();
            rotatable.setRotation(BlockFace.values()[TARDISConstants.RANDOM.nextInt(6, 18)]);
            head.setBlockData(rotatable);
        }
    }

    private final WeightedChoice<String> items = new WeightedChoice<String>()
            .add(25, "TARDIS Key")
            .add(10, "Stattenheim Remote")
            .add(10, "Artron Capacitor")
            .add(10, "Artron Storage Cell")
            .add(10, "Sonic Screwdriver")
            .add(5, "3-D Glasses")
            .add(5, "Red Bow Tie");

    public void addBrushable(Block base, StructureVariant variant) {
        Block above = base.getRelative(BlockFace.UP);
        // check if the space above is air and passes a random chance threshold
        if (above.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 1) {
            base.setType(variant.base() == Material.END_STONE ? Material.SUSPICIOUS_SAND : Material.SUSPICIOUS_GRAVEL);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (base.getState() instanceof BrushableBlock brushable) {
                    brushable.setItem(plugin.getFigura().getShapedRecipes().get(items.next()).getResult());
                    brushable.update(true);
                }
            }, 5L);
        }
    }
}
