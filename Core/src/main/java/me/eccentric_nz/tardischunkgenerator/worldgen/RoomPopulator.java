/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardischunkgenerator.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.Room;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBannerData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RoomPopulator extends BlockPopulator {

    private final TARDIS plugin;
    private final HashMap<Block, TARDISBannerData> bannerBlocks = new HashMap<>();
    private final List<String> rooms = List.of("ALLAY", "APIARY", "AQUARIUM", "ARBORETUM", "BAMBOO", "BEDROOM",
            "BIRDCAGE", "CHEMISTRY", "EMPTY", "FARM", "GARDEN", "GEODE", "GREENHOUSE", "HARMONY", "HUTCH", "IGLOO",
            "IISTUBIL", "KITCHEN", "LAVA", "LAZARUS", "LIBRARY", "MANGROVE", "MUSHROOM", "NETHER", "PEN", "POOL", "RAIL",
            "SHELL", "SMELTER", "STABLE", "STALL", "SURGERY", "TRENZALORE", "VAULT", "VILLAGE", "WORKSHOP");

    public RoomPopulator(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        String room = rooms.get(random.nextInt(rooms.size()));
        JsonObject obj = TARDISSchematicGZip.getObject(plugin, "rooms", room.toLowerCase(Locale.ROOT), false);;
        if (obj != null) {
            // initialise
            int level = 0;
            int row = 0;
            JsonObject dim = obj.get("dimensions").getAsJsonObject();
            int h = dim.get("height").getAsInt() - 1;
            int w = dim.get("width").getAsInt();
            int l = dim.get("length").getAsInt() - 1;
            int startX = x * 16;
            int startY = 64;
            int startZ = z * 16;
            // get input array
            JsonArray arr = obj.get("input").getAsJsonArray();
            while (level <= h && row < w) {
                JsonArray floor = arr.get(level).getAsJsonArray();
                JsonArray r = floor.get(row).getAsJsonArray();
                // loop like crazy
                for (int col = 0; col <= l; col++) {
                    JsonObject c = r.get(col).getAsJsonObject();
                    int lrX = startX + row;
                    int lrY = startY + level;
                    int lrZ = startZ + col;
                    BlockData data = plugin.getServer().createBlockData(c.get("data").getAsString());
                    Material type = data.getMaterial();
                    if (limitedRegion.isInRegion(lrX, lrY, lrZ)) {
                        // set farm
                        if (type.equals(Material.SPAWNER) && room.equals("FARM")) {
                            // replace with floor material
                            data = Material.LIGHT_GRAY_WOOL.createBlockData();
                        }
                        // set stable
                        if (type.equals(Material.SOUL_SAND) && (room.equals("STABLE") || room.equals("VILLAGE") || room.equals("LAVA") || room.equals("ALLAY") || room.equals("GEODE") || room.equals("HUTCH") || room.equals("IGLOO") || room.equals("IISTUBIL") || room.equals("MANGROVE") || room.equals("PEN") || room.equals("STALL") || room.equals("BAMBOO") || room.equals("BIRDCAGE") || room.equals("GARDEN"))) {
                            // replace with correct block
                            switch (Room.valueOf(room)) {
                                case ALLAY -> data = Material.LIGHT_GRAY_WOOL.createBlockData();
                                case VILLAGE -> data = Material.COBBLESTONE.createBlockData();
                                case HUTCH, STABLE, STALL, MAZE, GARDEN -> data = Material.GRASS_BLOCK.createBlockData();
                                case BAMBOO, BIRDCAGE -> data = Material.PODZOL.createBlockData();
                                case GEODE -> data = Material.CLAY.createBlockData();
                                case IGLOO -> data = Material.PACKED_ICE.createBlockData();
                                case IISTUBIL -> data = Material.TERRACOTTA.createBlockData();
                                case LAVA -> data = Material.NETHERRACK.createBlockData();
                                case MANGROVE -> data = TARDISConstants.WATER;
                                case PEN -> data = Material.MOSS_BLOCK.createBlockData();
                                case ZERO -> data = Material.PINK_CARPET.createBlockData();
                                default -> data = TARDISConstants.BLACK;
                            }
                        }
                        if ((type.equals(Material.SOUL_SAND) || type.equals(Material.CARVED_PUMPKIN)) && room.equals("SMELTER")) {
                            data = Material.CHEST.createBlockData();
                        }
                        if (type.equals(Material.DEAD_HORN_CORAL_BLOCK) && room.equals("AQUARIUM")) {
                            data = Material.LIGHT_GRAY_WOOL.createBlockData();
                        }
                        // always replace bedrock (the door space in ARS rooms)
                        // always remove sponge
                        if ((type.equals(Material.BEDROCK) && !room.equals("SHELL")) || (type.equals(Material.SOUL_SAND) && room.equals("SHELL")) || type.equals(Material.SPONGE)) {
                            data = TARDISConstants.AIR;
                        }
                        limitedRegion.setBlockData(lrX, lrY, lrZ, data);
                    }
                    if (col == l && row < w) {
                        row++;
                    }
                    if (col == l && row == w && level < h) {
                        row = 0;
                        level++;
                    }
                }
            }
        }
    }
}