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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.TARDISLootTables;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.InputStream;
import java.util.Random;

public class TelosStructurePopulator extends BlockPopulator {

    private final TARDIS plugin;
    private final IslandBlockPopulator.WeightedChoice<Material> STONES = new IslandBlockPopulator.WeightedChoice<Material>()
            .add(70, Material.STONE)
            .add(10, Material.ANDESITE)
            .add(10, Material.DIORITE)
            .add(10, Material.GRANITE)
            .add(5, Material.COAL_ORE);

    public TelosStructurePopulator(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        // get a chunk position
        if (isFeatureChunk(worldInfo.getSeed(), x, z)) {
            int xx = x * 16;
            int y = 24 + TARDISConstants.RANDOM.nextInt(20);
            int zz = z * 16;
            if (limitedRegion.isInRegion(xx, y, zz) && limitedRegion.getBuffer() > 15) {
                // have we got a 48 x 48 block buffer zone?
                build(limitedRegion, xx, y, zz, random);
            }
        }
    }

    public boolean isFeatureChunk(long seed, int x, int z) {
        RandomSpreadStructurePlacement spread = new RandomSpreadStructurePlacement(24, 8, RandomSpreadType.TRIANGULAR, 165745295);
        ChunkPos chunkPos = spread.getPotentialStructureChunk(seed, x, z);
        return chunkPos.x == x && chunkPos.z == z;
    }

    private void build(LimitedRegion limitedRegion, int startX, int startY, int startZ, Random random) {
        plugin.debug("x = " + startX + ", y = " + startY + ", z = " + startZ);
        String path = "schematics/cryo_chamber.tschm";
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
                            case WAXED_OXIDIZED_COPPER_CHEST -> {
                                limitedRegion.setBlockData(x, y, z, data);
                                if (limitedRegion.getType(x, y, z).equals(Material.WAXED_OXIDIZED_COPPER_CHEST)) {
                                    // set chest contents
                                    Chest container = (Chest) limitedRegion.getBlockState(x, y, z);
                                    container.setLootTable(TARDISLootTables.LOOT.get(random.nextInt(11)));
                                    container.update();
                                }
                            }
                            case SPONGE -> { }
                            case SOUL_SAND -> {
                                limitedRegion.setType(x, y, z, Material.SPAWNER);
                                // change to zombie spawner
                                CreatureSpawner cs = (CreatureSpawner) limitedRegion.getBlockState(x, y, z);
                                cs.setSpawnedType(EntityType.ZOMBIE);
                                cs.update();
                            }
                            case STONE -> {
                                Material stone = STONES.next();
                                limitedRegion.setType(x, y, z, stone);
                            }
                            default -> limitedRegion.setBlockData(x, y, z, data);
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
            if (obj.has("armour_stands")) {
                JsonArray stands = obj.get("armour_stands").getAsJsonArray();
                for (int i = 0; i < stands.size(); i++) {
                    JsonObject stand = stands.get(i).getAsJsonObject();
                    JsonObject rel = stand.get("rel_location").getAsJsonObject();
                    int asx = rel.get("x").getAsInt();
                    int asy = rel.get("y").getAsInt();
                    int asz = rel.get("z").getAsInt();
                    COMPASS facing = COMPASS.valueOf(BlockFace.valueOf(stand.get("facing").getAsString()).getOppositeFace().toString());
                    Location asl = new Location(limitedRegion.getWorld(), startX + asx + 0.5d, startY + asy, startZ + asz + 0.5d);
                    ArmorStand as = (ArmorStand) limitedRegion.spawnEntity(asl, EntityType.ARMOR_STAND);
                    as.setRotation(facing.getYaw(), 0);
                    as.setVisible(stand.get("invisible").getAsBoolean());
                    if (stand.has("head")) {
                        JsonObject head = stand.get("head").getAsJsonObject();
                        Material material = Material.valueOf(head.get("material").getAsString());
                        NamespacedKey nsk = NamespacedKey.fromString(head.get("model").getAsString());
                        ItemStack is = ItemStack.of(material);
                        ItemMeta im = is.getItemMeta();
                        im.setItemModel(nsk);
                        is.setItemMeta(im);
                        as.getEquipment().setHelmet(is);
                    }
                }
            }
        }
    }
}
