package me.eccentric_nz.TARDIS.builders.interior;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.discovery.Placer;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreviewFade {

    private final TARDIS plugin;
    private final StructureVariant variant;
    private final Location location;
    private final List<BlockData> bars = new ArrayList<>();


    public PreviewFade(TARDIS plugin, StructureVariant variant, World world) {
        this.plugin = plugin;
        this.variant = variant;
        // calculate startX, startZ
        TARDISInteriorPositioning tintPos = new TARDISInteriorPositioning(plugin);
        TIPSData pos = tintPos.getTIPSData(-64);
        // get the correct chunk for ARS
        location = new Location(world, pos.getCentreX(), 63, pos.getCentreZ());
    }

    public void build() {
        // async task to populate box with block fade
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String[][][] template = new Template().load(plugin);
            BlockData[][][] base = setBaseBlocks(template, variant);
            BlockData[][][] fade = new FadeMutator(TARDISConstants.RANDOM.nextLong(), 7, variant).applyGradient(base, variant.gradient());
            // switch back to the main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                place(location, fade, variant);
            });
        });
    }

    public BlockData[][][] setBaseBlocks(String[][][] array, StructureVariant variant) {
        BlockData[][][] data = new BlockData[array.length][array[0].length][array[0][0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    String material = array[i][j][k];
                    if (material.contains("tuff_wall")) {
                        data[i][j][k] = variant.wall().createBlockData();
                    } else if (material.contains("tuff")) {
                        data[i][j][k] = variant.base().createBlockData();
                    } else if (material.contains("muddy_mangrove_roots")) {
                        data[i][j][k] = variant.seed().createBlockData();
                    } else {
                        data[i][j][k] = Bukkit.createBlockData(material);
                    }
                }
            }
        }
        return data;
    }

    private void place(Location location, BlockData[][][] materials, StructureVariant variant) {
        int b = 0;
        int d = 0;
        int p = 0;
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
                    BlockData data = materials[i][j][k];
                    Block block = world.getBlockAt(x + j, y + i, z + k);
                    BlockState state = block.getState();
                    state.setBlockData(data);
                    // add hanging lights
                    if (i > 12 && i < 15 && !data.getMaterial().isAir()) {
                        Block below = block.getRelative(BlockFace.DOWN);
                        if (below.getType().isAir() && TARDISConstants.RANDOM.nextInt(100) < 4) {
                            placer.addHangingLights(below, variant, 5);
                        }
                    }
                    if (data.getMaterial() == Material.CLAY) {
                        placer.addWaterFeature(block, variant);
                    }
                    // process TARDIS special blocks
                    if (data.getMaterial() == Material.BLUE_WOOL) {
                        state.setType(Material.BARRIER);
                        // spawn an item display entity
                        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.BLUE_BOX, world, x + j, y + i, z + k);
                    }
                    if (data.getMaterial() == Material.NOTE_BLOCK) {
                        // set block data to BARRIER
                        state.setType(Material.BARRIER);
                        // spawn an item display entity
                        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, world, x + j, y + i, z + k);
                    }
                    if (data.getMaterial() == Material.IRON_DOOR) {
                        // iron door bottom
                        if (d == 0) {
                            // remember spawn location for this console preview
                            HashMap<String, Object> set = new HashMap<>();
                            set.put("tardis_id", -64);
                            set.put("name", "fade");
                            set.put("world", world.getKey().asString());
                            set.put("x", x + j + 0.5d);
                            set.put("y", y + i);
                            set.put("z", (z + k + 1));
                            plugin.getQueryFactory().doInsert("transmats", set);
                            d++;
                        }
                    }
                    if (data.getMaterial() == Material.JUKEBOX) {
                        // set block data to correct BARRIER + Item Display
                        state.setType(Material.BARRIER);
                        // spawn an item display entity
                        TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ADVANCED_CONSOLE, world, x + j, y + i, z + k);
                    }
                    if (data.getMaterial() == Material.COMMAND_BLOCK) {
                        state.setType(variant.base());
                    }
                    if (data.getMaterial() == Material.RED_MUSHROOM_BLOCK) { // red mushroom block for repeaters
                        BlockData repeater = Material.REPEATER.createBlockData();
                        Directional directional = (Directional) repeater;
                        switch (p) {
                            case 1 -> {
                                directional.setFacing(BlockFace.NORTH);
                                state.setBlockData(directional);
                            }
                            case 2 -> {
                                directional.setFacing(BlockFace.SOUTH);
                                state.setBlockData(directional);
                            }
                            case 3 -> {
                                directional.setFacing(BlockFace.EAST);
                                state.setBlockData(directional);
                            }
                            default -> { // 0
                                directional.setFacing(BlockFace.WEST);
                                state.setBlockData(directional);
                            }
                        }
                        p++;
                    }
                    state.update(true, false);
                    if (i < 13 && data.getMaterial() == variant.base()) {
                        baseBlocks.add(block);
                    }
                }
            }
        }
        for (Block block : baseBlocks) {
            placer.addGlowLichen(block);
            placer.addCarpet(block, variant);
            placer.addHead(block, variant);
        }
    }
}
