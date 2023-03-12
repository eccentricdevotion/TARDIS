package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.tardischunkgenerator.TARDISHelper;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.GallifeyStructureUtility;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.TARDISLootTables;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.TARDISSchematicReader;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.BlockVector;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GallifreyStructurePopulator extends BlockPopulator {

    private final TARDISHelper plugin;
    List<Material> buildable = Arrays.asList(Material.RED_SAND, Material.WATER, Material.TERRACOTTA, Material.BROWN_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.RED_TERRACOTTA, Material.WHITE_TERRACOTTA);

    public GallifreyStructurePopulator(TARDISHelper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        if (isFeatureChunk(worldInfo.getSeed(), x, z)) {
            int startX = x * 16;
            int startY = 128;
            int startZ = z * 16;
            for (int i = 128; i > 60; i--) {
                if (!buildable.contains(limitedRegion.getType(startX, startY, startZ))) {
                    startY--;
                } else {
                    break;
                }
            }
            if (limitedRegion.isInRegion(startX, startY, startZ) && limitedRegion.getBuffer() > 15) {
                List<BlockVector> grid;
                // choose a random direction
                int dir = random.nextInt(4);
                switch (dir) {
                    case 0 -> grid = GallifeyStructureUtility.vectorLeft;
                    case 1 -> grid = GallifeyStructureUtility.vectorUp;
                    case 2 -> grid = GallifeyStructureUtility.vectorRight;
                    default -> grid = GallifeyStructureUtility.vectorDown;
                }
                int i = 0;
                for (BlockVector vector : grid) {
                    // large, farm, house, water, temple
                    build(limitedRegion, startX + vector.getBlockX(), (i == 0) ? startY + vector.getBlockY() : -99, startZ + vector.getBlockZ(), random, GallifeyStructureUtility.structures.get(i));
                    i++;
                }
            }
        }
    }

    public boolean isFeatureChunk(long seed, int x, int z) {
        RandomSpreadStructurePlacement spread = new RandomSpreadStructurePlacement(20, 10, RandomSpreadType.TRIANGULAR, 10387321);
        ChunkPos chunkPos = spread.getPotentialStructureChunk(seed, x, z);
        return chunkPos.x == x && chunkPos.z == z;
    }

    private void build(LimitedRegion limitedRegion, int startX, int startY, int startZ, Random random, String which) {
        String path = "schematics" + File.separator + "gallifrey_" + which + ".tschm";
        // Get inputStream
        InputStream stream = plugin.getResource(path);
        if (stream != null) {
            // get JSON
            JsonObject obj = TARDISSchematicReader.unzip(stream);
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
                    if (!buildable.contains(limitedRegion.getType(startX, startY, startZ))) {
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
                BlockData data;
                Material type;
                // loop like crazy
                for (int col = 0; col <= d; col++) {
                    JsonObject c = r.get(col).getAsJsonObject();
                    int x = startX + row;
                    int y = startY + level;
                    int z = startZ + col;
                    data = plugin.getServer().createBlockData(c.get("data").getAsString());
                    type = data.getMaterial();
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
                                // don't set a block, let the default generated blocks stay as they are
                            }
                            case SPAWNER -> {
                                limitedRegion.setBlockData(x, y, z, data);
                                CreatureSpawner cs = (CreatureSpawner) limitedRegion.getBlockState(x, y, z);
                                cs.setSpawnedType(EntityType.VILLAGER);
                                cs.update();
                            }
                            default -> {
                                limitedRegion.setBlockData(x, y, z, data);
                                if (level == 0) {
                                    // place red sand under block if it is AIR
                                    int yy = y - 1;
                                    while (limitedRegion.getType(x, yy, z).isAir() || limitedRegion.getType(x, yy, z).equals(Material.WATER)) {
                                        limitedRegion.setType(x, yy, z, Material.RED_SAND);
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
