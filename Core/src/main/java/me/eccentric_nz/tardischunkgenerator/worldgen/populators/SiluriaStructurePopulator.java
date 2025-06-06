/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.tardischunkgenerator.worldgen.SiluriaProcessData;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.SiluriaStructureUtility;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.TARDISLootTables;
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

import java.io.InputStream;
import java.util.*;

public class SiluriaStructurePopulator extends BlockPopulator {

    final List<SiluriaProcessData> toProcess = new ArrayList<>();
    final List<String> noStilts = List.of("east_west", "north_south", "cross");
    final List<Material> buildable = List.of(Material.GRASS_BLOCK, Material.WATER, Material.PODZOL, Material.SAND);
    private final TARDIS plugin;

    public SiluriaStructurePopulator(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        Iterator<SiluriaProcessData> iterator = toProcess.iterator();
        while (iterator.hasNext()) {
            SiluriaProcessData xz = iterator.next();
            if (xz.x == x && xz.z == z) {
                for (Map.Entry<BlockVector, String> grid : xz.grid.entrySet()) {
                    build(limitedRegion, (x * 16) + grid.getKey().getBlockX(), xz.y, (z * 16) + grid.getKey().getBlockZ(), random, grid.getValue());
                }
                iterator.remove();
            }
        }
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
                for (Map.Entry<BlockVector, HashMap<BlockVector, String>> centre : SiluriaStructureUtility.centres.entrySet()) {
                    toProcess.add(new SiluriaProcessData(x + centre.getKey().getBlockX(), startY, z + centre.getKey().getBlockZ(), centre.getValue()));
                }
                for (Map.Entry<BlockVector, String> vector : SiluriaStructureUtility.vectorZero.entrySet()) {
                    // initial large + walkways
                    build(limitedRegion, startX + vector.getKey().getBlockX(), startY, startZ + vector.getKey().getBlockZ(), random, vector.getValue());
                }
            }
        }
    }

    public boolean isFeatureChunk(long seed, int x, int z) {
        RandomSpreadStructurePlacement spread = new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 14357620);
        ChunkPos chunkPos = spread.getPotentialStructureChunk(seed, x, z);
        return chunkPos.x == x && chunkPos.z == z;
    }

    private void build(LimitedRegion limitedRegion, int startX, int startY, int startZ, Random random, String which) {
        String path = "schematics/siluria_" + which + ".tschm";
        // Get inputStream
        InputStream stream = plugin.getResource(path);
        if (stream != null) {
            // get JSON
            JsonObject obj = TARDISSchematicGZip.unzip(stream);
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            int h = dimensions.get("height").getAsInt() - 1;
            int w = dimensions.get("width").getAsInt();
            int d = dimensions.get("length").getAsInt() - 1;
            int level = 0;
            int row = 0;
            if (noStilts.contains(which)) {
                startY += 17;
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
                            case AIR -> {
                                // only set air blocks if the structure part is above the 'stilts'
                                if (level > h - ((which.equals("temple") ? 7 : 6)) && !noStilts.contains(which)) {
                                    limitedRegion.setBlockData(x, y, z, data);
                                }
                            }
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
                            default -> {
                                limitedRegion.setBlockData(x, y, z, data);
                                if (level == 0 && !noStilts.contains(which) && !data.getMaterial().equals(Material.AIR)) {
                                    // place Jungle Log under block if it is AIR or WATER
                                    int yy = y - 1;
                                    while (limitedRegion.getType(x, yy, z).isAir() || limitedRegion.getType(x, yy, z).equals(Material.WATER)) {
                                        limitedRegion.setType(x, yy, z, Material.JUNGLE_LOG);
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
