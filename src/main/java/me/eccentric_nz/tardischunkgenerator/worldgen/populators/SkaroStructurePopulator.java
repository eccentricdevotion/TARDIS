package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import me.eccentric_nz.tardischunkgenerator.helpers.WeepingAngels;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.BlockVector;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SkaroStructurePopulator extends BlockPopulator {

    private final TARDISHelper plugin;
    private final List<EntityType> animals = Arrays.asList(EntityType.SHEEP, EntityType.COW, EntityType.PIG, EntityType.CHICKEN, EntityType.HORSE, EntityType.GOAT);

    public SkaroStructurePopulator(TARDISHelper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        // get a chunk position
        if (isFeatureChunk(worldInfo.getSeed(), x, z)) {
            int xx = x * 16;
            int y = 128;
            int zz = z * 16;
            for (int i = 128; i > 60; i--) {
                if (!limitedRegion.getType(xx, y, zz).equals(Material.SAND) && !limitedRegion.getType(xx, y, zz).equals(Material.WATER)) {
                    y--;
                } else {
                    break;
                }
            }
            if (limitedRegion.isInRegion(xx, y, zz)) {
                if (limitedRegion.getType(xx, y - 1, zz).equals(Material.WATER)) {
                    // build an island
                    // get a spiral
                    IslandSpiral spiral = new IslandSpiral();
                    double[][] island = spiral.createMatrix(16, 16, random, 0.01);
                    // create a blob
                    boolean[][] blob = WaterCircle.makeBlob();
                    // loop through the chunk coords and set blocks
                    // top four layers SAND
                    // the rest SANDSTONE
                    for (int r = 1; r < 15; r++) {
                        for (int c = 1; c < 15; c++) {
                            if (blob[r][c]) {
                                double n = island[r][c];
                                int top = (n > 0) ? (int) (n * 6) : 0;
                                int bottom = -6 - (int) (n * 40);
                                for (int h = top; h >= bottom; h--) {
                                    int wx = xx + r;
                                    int wy = y + h;
                                    int wz = zz + c;
                                    if (limitedRegion.isInRegion(wx, wy, wz)) {
                                        Material material;
                                        if (h > top - 3) {
                                            material = Material.SAND;
                                        } else {
                                            material = Material.SANDSTONE;
                                        }
                                        limitedRegion.setType(wx, wy, wz, material);
                                    }
                                }
                            }
                        }
                    }
                    // build a small dalek structure
                    build(limitedRegion, xx + 2, y + 2, zz + 3, random, "small");
                    // spawn a dalek or three?
                    if (plugin.getServer().getPluginManager().getPlugin("TARDISWeepingAngels") != null && plugin.getServer().getPluginManager().getPlugin("TARDISWeepingAngels").isEnabled()) {
                        for (int i = 0; i < random.nextInt(3) + 1; i++) {
                            LivingEntity le = (LivingEntity) limitedRegion.spawnEntity(new Location(null, xx + 8, y + 3, zz + 8), EntityType.SKELETON);
                            if (plugin.getServer().getPluginManager().isPluginEnabled("TARDISWeepingAngels")) {
                                WeepingAngels.getAPI().setDalekEquipment(le, false);
                            }
                        }
                    }
                } else {
                    build(limitedRegion, xx, y, zz, random, "large");
                    // have we got a 48 x 48 block buffer zone?
                    if (limitedRegion.getBuffer() > 15) {
                        List<BlockVector> grid;
                        // choose a random direction
                        int dir = random.nextInt(4);
                        switch (dir) {
                            case 0 -> grid = SkaroStructureUtility.vectorLeft;
                            case 1 -> grid = SkaroStructureUtility.vectorUp;
                            case 2 -> grid = SkaroStructureUtility.vectorRight;
                            default -> grid = SkaroStructureUtility.vectorDown;
                        }
                        int i = 0;
                        for (BlockVector vector : grid) {
                            build(limitedRegion, xx + vector.getBlockX(), (i == 0) ? y : -99, zz + vector.getBlockZ(), random, SkaroStructureUtility.structures.get(i));
                            i++;
                        }
                    }
                }
            }
        }
    }

    public boolean isFeatureChunk(long seed, int x, int z) {
        RandomSpreadStructurePlacement spread = new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.TRIANGULAR, 165745295);
        ChunkPos chunkPos = spread.getPotentialStructureChunk(seed, x, z);
        return chunkPos.x == x && chunkPos.z == z;
    }

    private void build(LimitedRegion limitedRegion, int startX, int startY, int startZ, Random random, String which) {
        String path = "schematics" + File.separator + "dalek_" + which + ".tschm";
        // Get inputStream
        InputStream stream = plugin.getResource(path);
        if (stream != null) {
            // get JSON
            JsonObject obj = TARDISSchematicReader.unzip(plugin.getResource(path));
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            int h = dimensions.get("height").getAsInt() - 1;
            int w = dimensions.get("width").getAsInt();
            int d = dimensions.get("length").getAsInt() - 1;
            int level = 0;
            int row = 0;
            if (startY == -99) {
                startY = 129;
                // set startY to highest block Y at x, z
                for (int i = 128; i > 60; i--) {
                    if (!limitedRegion.getType(startX, startY, startZ).equals(Material.SAND) && !limitedRegion.getType(startX, startY, startZ).equals(Material.WATER)) {
                        startY--;
                    } else {
                        break;
                    }
                }
                startY += 1;
            }
            // get input array
            JsonArray arr = obj.get("input").getAsJsonArray();
            while (level <= h && row < w) {
                JsonArray floor = arr.get(level).getAsJsonArray();
                JsonArray r = floor.get(row).getAsJsonArray();
                // loop like crazy
                for (int col = 0; col <= d; col++) {
                    JsonObject c = r.get(col).getAsJsonObject();
                    int x = startX + row;
                    int y = startY + level;
                    int z = startZ + col;
                    BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
                    Material type = data.getMaterial();
                    if (limitedRegion.isInRegion(x, y, z)) {
                        switch (type) {
                            case CHEST -> {
                                limitedRegion.setBlockData(x, y, z, data);
                                if (limitedRegion.getType(x, y, z).equals(Material.CHEST)) {
                                    // set chest contents
                                    Chest container = (Chest) limitedRegion.getBlockState(x, y, z);
                                    container.setLootTable(TARDISLootTables.LOOT.get(random.nextInt(11)));
                                    container.update();
                                }
                            }
                            case SPONGE -> {
                                if (!limitedRegion.getType(x, y, z).isOccluding()) {
                                    limitedRegion.setType(x, y, z, Material.AIR);
                                }
                            }
                            case SPAWNER -> {
                                limitedRegion.setBlockData(x, y, z, data);
                                CreatureSpawner cs = (CreatureSpawner) limitedRegion.getBlockState(x, y, z);
                                cs.setSpawnedType(EntityType.SKELETON);
                                cs.update();
                            }
                            case SOUL_SAND -> {
                                limitedRegion.setType(x, y, z, Material.GRASS_BLOCK);
                                // spawn an animal or three at this location
                                EntityType entityType = animals.get(random.nextInt(animals.size()));
                                for (int i = 0; i < random.nextInt(3) + 1; i++) {
                                    LivingEntity le = (LivingEntity) limitedRegion.spawnEntity(new Location(null, x, y + 1, z), entityType);
                                    le.setRemoveWhenFarAway(false);
                                    // if more than one, make a baby
                                    if (i > 0) {
                                        ((Ageable) le).setBaby();
                                    }
                                }
                            }
                            default -> {
                                limitedRegion.setBlockData(x, y, z, data);
                                if (level == 0) {
                                    // place sand under block if it is not AIR
                                    int yy = y - 1;
                                    while (limitedRegion.getType(x, yy, z).isAir() || limitedRegion.getType(x, yy, z).equals(Material.WATER)) {
                                        limitedRegion.setType(x, yy, z, Material.SAND);
                                        yy--;
                                    }
                                }
                            }
                        }
                    }
                    if (col == d && row < w) {
                        row++;
                    }
                    if (col == d && row == w && level < h) {
                        row = 0;
                        level++;
                    }
                }
            }
        }
    }
}
