package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.discovery.Mutator;
import me.eccentric_nz.TARDIS.discovery.Parts;
import me.eccentric_nz.TARDIS.discovery.Placer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Fade {

    private final TARDIS plugin;

    public Fade(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void build(Player player) {
        // async task to populate box with block fade
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Parts parts = new Parts(plugin);
            parts.getCorners();
            Material[][][] SW = new Mutator(TARDISConstants.RANDOM.nextLong(), 10).addBlends(parts.getSouthWest());
            Material[][][] SE = new Mutator(TARDISConstants.RANDOM.nextLong(), 10).addBlends(parts.getSouthEast());
            Material[][][] NE = new Mutator(TARDISConstants.RANDOM.nextLong(), 10).addBlends(parts.getNorthEast());
            Material[][][] NW = new Mutator(TARDISConstants.RANDOM.nextLong(), 10).addBlends(parts.getNorthWest());
            // switch back to the main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                Location location = player.getLocation().add(0, 3, 0);
                Location start = location.clone().add(parts.getSePos().x(), parts.getSePos().y(), parts.getSePos().z());
                place(start, SE);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Location right = location.clone().add(parts.getSwPos().x(), parts.getSwPos().y(), parts.getSwPos().z());
                    place(right, SW);
                }, 5L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Location across = location.clone().add(parts.getNwPos().x(), parts.getNwPos().y(), parts.getNwPos().z());
                    place(across, NW);
                }, 10L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Location down = location.clone().add(parts.getNePos().x(), parts.getNePos().y(), parts.getNePos().z());
                    place(down, NE);
                }, 15L);
            });
        });
    }

    private void place(Location location, Material[][][] materials) {
        Placer placer = new Placer(plugin);
        List<Block> tuffBlocks = new ArrayList<>();
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
                    BlockData data = material.createBlockData();
                    Block block = world.getBlockAt(x + j, y + i, z + k);
                    world.setBlockData(x + j, y + i, z + k, data);
                    if (i > 22 && i < 25 && material != Material.AIR) {
                        Block below = block.getRelative(BlockFace.DOWN);
                        if (below.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 5) {
                            placer.makeVines(below);
                        }
                    }
                    if (material == Material.CLAY) {
                        placer.makeWaterFeature(block);
                    }
                    if (i < 24 && material == Material.TUFF) {
                        tuffBlocks.add(block);
                    }
                }
            }
        }
        for (Block block : tuffBlocks) {
            placer.makeGlowLichen(block);
            placer.makeMossCarpets(block);
            placer.makeHeads(block);
        }
    }
}
