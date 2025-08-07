package me.eccentric_nz.tardischunkgenerator.worldgen;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.FlatBiomeProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Wall;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;

public class PlotGenerator extends ChunkGenerator {

    private final Wall wall_EW;
    private final Wall wall_NS;
    private final Wall wall_NW;
    private final Wall wall_NE;
    private final Wall wall_SW;
    private final Wall wall_SE;
    private final Gate gate_EW;
    private final Gate gate_NS;
    private final Sign angled;
    private final int size;
    private Material rock;
    private Material middle;
    private Material surface;
    private Material road_side;
    private Material road_line;

    public PlotGenerator(TARDIS plugin) {
        Material wall;
        Material gate;
        Material sign;
        try {
            rock = Material.valueOf(plugin.getGeneratorConfig().getString("plot.rock", "STONE").toUpperCase(Locale.ROOT));
            middle = Material.valueOf(plugin.getGeneratorConfig().getString("plot.middle", "DIRT").toUpperCase(Locale.ROOT));
            surface = Material.valueOf(plugin.getGeneratorConfig().getString("plot.surface", "GRASS_BLOCK").toUpperCase(Locale.ROOT));
            wall = Material.valueOf(plugin.getGeneratorConfig().getString("plot.wall", "RESIN_BLOCK_WALL").toUpperCase(Locale.ROOT));
            if (!(wall.createBlockData() instanceof Wall)) {
                plugin.getLogger().log(Level.INFO, "Specified wall material is not a wall!");
                plugin.getLogger().log(Level.INFO, "Using default wall.");
                wall = Material.RESIN_BRICK_WALL;
            }
            gate = Material.valueOf(plugin.getGeneratorConfig().getString("plot.gate", "BAMBOO_FENCE_GATE").toUpperCase(Locale.ROOT));
            if (!(gate.createBlockData() instanceof Gate)) {
                plugin.getLogger().log(Level.INFO, "Specified gate material is not a gate!");
                plugin.getLogger().log(Level.INFO, "Using default gate.");
                gate = Material.BAMBOO_FENCE_GATE;
            }
            sign = Material.valueOf(plugin.getGeneratorConfig().getString("plot.sign", "BAMBOO_SIGN").toUpperCase(Locale.ROOT));
            if (!(sign.createBlockData() instanceof Sign)) {
                sign = Material.BAMBOO_SIGN;
            }
            road_side = Material.valueOf(plugin.getGeneratorConfig().getString("plot.road_side", "GRAY_CONCRETE").toUpperCase(Locale.ROOT));
            road_line = Material.valueOf(plugin.getGeneratorConfig().getString("plot.road_line", "WHITE_CONCRETE").toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().log(Level.WARNING, "Invalid material specified! " + e.getMessage());
            plugin.getLogger().log(Level.INFO, "Using default materials.");
            rock = Material.STONE;
            middle = Material.DIRT;
            surface = Material.GRASS_BLOCK;
            wall = Material.RESIN_BRICK_WALL;
            gate = Material.BAMBOO_FENCE_GATE;
            sign = Material.BAMBOO_SIGN;
            road_side = Material.GRAY_CONCRETE;
            road_line = Material.WHITE_CONCRETE;
        }
        wall_EW = (Wall) wall.createBlockData("[east=low,west=low,up=false]");
        wall_NS = (Wall) wall.createBlockData("[north=low,south=low,up=false]");
        wall_NE = (Wall) wall.createBlockData("[north=low,east=low]");
        wall_NW = (Wall) wall.createBlockData("[north=low,west=low]");
        wall_SE = (Wall) wall.createBlockData("[east=low,south=low]");
        wall_SW = (Wall) wall.createBlockData("[west=low,south=low]");
        gate_EW = (Gate) gate.createBlockData("[in_wall=true,facing=east]");
        gate_NS = (Gate) gate.createBlockData("[in_wall=true,facing=north]");
        angled = (Sign) sign.createBlockData("[rotation=6]");
        size = Math.max(plugin.getGeneratorConfig().getInt("plot.size"), 2);
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            boolean isTopLeft = Math.floorMod(x, size) == 0 && Math.floorMod(z, size) == 0;
            for (int bx = 0; bx < 16; bx++) {
                for (int bz = 0; bz < 16; bz++) {
                    for (int by = 1; by <= 65; by++) {
                        if (by < 60) {
                            chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, rock);
                        } else if (by < 64) {
                            chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, middle);
                        } else if (by == 64) {
                            // roads && surface
                            if (isTopLeft) {
                                // road top left corner
                                if (bx < 2 || (bx > 2 && bx < 5) || bz < 2 || (bz > 2 && bz < 5)) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, road_side);
                                } else if (bx == 2) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz % 4 > 1) ? road_side : road_line);
                                } else if (bz == 2) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx % 4 > 1) ? road_side : road_line);
                                } else {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, surface);
                                }
                            } else if (Math.floorMod(x, size) == 0) {
                                // road left side
                                if (bx < 2 || (bx > 2 && bx < 5)) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, road_side);
                                } else if (bx == 2) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz % 4 > 1) ? road_side : road_line);
                                } else {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, surface);
                                }
                            } else if (Math.floorMod(z, size) == 0) {
                                // road top side
                                if (bz < 2 || (bz > 2 && bz < 5)) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, road_side);
                                } else if (bz == 2) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx % 4 > 1) ? road_side : road_line);
                                } else {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, surface);
                                }
                            } else {
                                // just plot
                                chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, surface);
                            }
                        } else {
                            // fences & gates - 65
                            if (isTopLeft) {
                                // fence/gate top left corner
                                if (bx == 5 && bz == 5) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, wall_SE);
                                } else {
                                    if (bx > 5 && bz == 5) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx == 10) ? gate_NS : wall_EW);
                                    }
                                    if (bz > 5 && bx == 5) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz == 10) ? gate_EW : wall_NS);
                                    }
                                }
                            } else if (Math.floorMod(x, size) == size - 1 && Math.floorMod(z, size) == size - 1) {
                                // fence/gate bottom right corner
                                if (bx == 15 && bz == 15) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, wall_NW);
                                } else {
                                    if (bx == 15) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz == 7) ? gate_EW : wall_NS);
                                    }
                                    if (bz == 15) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx == 7) ? gate_NS : wall_EW);
                                    }
                                }
                            } else if (Math.floorMod(x, size) == 0 && Math.floorMod(z, size) == size - 1) {
                                // fence/gate bottom left corner
                                if (bx == 5 && bz == 15) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, wall_NE);
                                } else {
                                    if (bx == 5) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz == 10) ? gate_EW : wall_NS);
                                    }
                                    if (bx > 5 && bz == 15) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx == 10) ? gate_NS : wall_EW);
                                    }
                                }
                            } else if (Math.floorMod(x, size) == size - 1 && Math.floorMod(z, size) == 0) {
                                // fence/gate top right corner
                                if (bz == 5 && bx == 15) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, wall_SW);
                                } else {
                                    if (bz == 5) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx == 7) ? gate_NS : wall_EW);
                                    } else if (bz > 5 && bx == 15) {
                                        chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz == 10) ? gate_EW : wall_NS);
                                    }
                                }
                            } else if (Math.floorMod(x, size) == 0) {
                                // fence/gate left side
                                if (bx == 5) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz == 7) ? gate_EW : wall_NS);
                                }
                            } else if (Math.floorMod(z, size) == 0) {
                                // fence/gate top side
                                if (bz == 5) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx == 7) ? gate_NS : wall_EW);
                                }
                            } else if (Math.floorMod(x, size) == size - 1) {
                                // fence/gate right side
                                if (bx == 15) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bz == 7) ? gate_EW : wall_NS);
                                }
                            } else if (Math.floorMod(z, size) == size - 1) {
                                // fence/gate bottom side
                                if (bz == 15) {
                                    chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, (bx == 7) ? gate_NS : wall_EW);
                                }
                            }
                        }
                    }
                    // set a sign
                    if (isTopLeft) {
                        chunkData.setBlock(5, chunkData.getMinHeight() + 66, 5, angled);
                    }
                }
            }
        }
    }

    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            for (int bx = 0; bx < 16; bx++) {
                for (int bz = 0; bz < 16; bz++) {
                    chunkData.setBlock(bx, chunkData.getMinHeight(), bz, Material.BEDROCK);
                }
            }
        }
    }

    /**
     * Sets the entire world to the PLAINS biome
     *
     * @param worldInfo the information about this world
     * @return a biome provider with just PLAINS
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new FlatBiomeProvider();
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 65, 0);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }
}
