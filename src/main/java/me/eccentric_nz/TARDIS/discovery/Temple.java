package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.interior.AbandonedBuilder;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Temple {

    private final TARDIS plugin;

    public Temple(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void build(Location location) {
        // async task to populate box with block fade
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Parts parts = new Parts(plugin, location);
            parts.getCorners();
            Mutator mutator = new Mutator(TARDISConstants.RANDOM.nextLong(), 10, location);
            StructureVariant variant = mutator.getVariant();
            Material[][][] SW = mutator.applyGradient(parts.getSouthWest(), variant.gradient());
            Material[][][] SE = new Mutator(TARDISConstants.RANDOM.nextLong(), 10, location).applyGradient(parts.getSouthEast(), variant.gradient());
            Material[][][] NE = new Mutator(TARDISConstants.RANDOM.nextLong(), 10, location).applyGradient(parts.getNorthEast(), variant.gradient());
            Material[][][] NW = new Mutator(TARDISConstants.RANDOM.nextLong(), 10, location).applyGradient(parts.getNorthWest(), variant.gradient());
            // switch back to the main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                Location start = location.clone().add(parts.getSePos().x(), parts.getSePos().y(), parts.getSePos().z());
                place(start, SE, variant);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Location right = location.clone().add(parts.getSwPos().x(), parts.getSwPos().y(), parts.getSwPos().z());
                    place(right, SW, variant);
                }, 5L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Location across = location.clone().add(parts.getNwPos().x(), parts.getNwPos().y(), parts.getNwPos().z());
                    place(across, NW, variant);
                }, 10L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Location down = location.clone().add(parts.getNePos().x(), parts.getNePos().y(), parts.getNePos().z());
                    place(down, NE, variant);
                }, 15L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // blend into environment
                    Location bounds = location.clone().add(parts.getNwPos().x(), parts.getNwPos().y(), parts.getNwPos().z());
                    StructureBlender.blendStructureOutwards(location.getWorld(), bounds.blockX(), bounds.blockX() + 63, bounds.blockY(), bounds.blockY() + 25, bounds.blockZ(), bounds.blockZ() + 63);
                    // spawn an abandoned TARDIS
                    Location spawn = location.clone().add(-1, 4, -1);
                    // get a random interior schematic
                    List<Schematic> schematics = new ArrayList<>(Desktops.getBY_NAMES().values());
                    Schematic s = schematics.get(TARDISConstants.RANDOM.nextInt(schematics.size()));
                    // get fade variant
                    StructureVariant v = null;
                    if (s.getPermission().equals("fade")) {
                        StructureVariant[] structureVariants = plugin.getBuildKeeper().getFadeVariants().getLookup().values().toArray(new StructureVariant[0]);
                        v = structureVariants[TARDISConstants.RANDOM.nextInt(structureVariants.length)];
                    }
                    // get a random exterior preset - must have a door
                    COMPASS direction = COMPASS.values()[TARDISConstants.RANDOM.nextInt(COMPASS.values().length)].forPreset();
                    new AbandonedBuilder(plugin).spawn(spawn, s, v, ChameleonPreset.FACTORY, "", direction, null);
                }, 30L);
            });
        });
    }

    private void place(Location location, Material[][][] materials, StructureVariant variant) {
        Placer placer = new Placer(plugin);
        List<Block> baseBlocks = new ArrayList<>();
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        int h = materials.length;
        int r = materials[0].length;
        int w = materials[0][0].length;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < r; j++) {
                for (int k = 0; k < w; k++) {
                    Material material = materials[i][j][k];
                    Block block = world.getBlockAt(x + j, y + i, z + k);
                    BlockState state = block.getState();
                    state.setType(material);
                    state.update(true, false);
                    if (i > 22 && i < 25 && material != Material.AIR) {
                        Block below = block.getRelative(BlockFace.DOWN);
                        if (below.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 5) {
                            placer.addHangingLights(below, variant, 10);
                        }
                    }
                    if (material == Material.CLAY) {
                        placer.addWaterFeature(block, variant);
                    }
                    if (i < 24 && material == variant.base()) {
                        baseBlocks.add(block);
                    }
                }
            }
        }
        for (Block block : baseBlocks) {
            placer.addGlowLichen(block);
            placer.addCarpet(block, variant);
            placer.addHead(block, variant);
            // add suspicious gravel with TARDIS key and other TARDIS items
            placer.addBrushable(block, variant);
        }
    }
}
