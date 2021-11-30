/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISRoomsUpdater {

    private final TARDIS plugin;
    private final FileConfiguration rooms_config;
    private final HashMap<String, String> stringOptions = new HashMap<>();
    private final HashMap<String, Integer> integerOptions = new HashMap<>();
    private final HashMap<String, Boolean> booleanOptions = new HashMap<>();

    public TARDISRoomsUpdater(TARDIS plugin, FileConfiguration rooms_config) {
        this.plugin = plugin;
        this.rooms_config = rooms_config;
        // boolean
        booleanOptions.put("rooms.ANTIGRAVITY.enabled", true);
        booleanOptions.put("rooms.ANTIGRAVITY.user", false);
        booleanOptions.put("rooms.APIARY.enabled", true);
        booleanOptions.put("rooms.APIARY.user", false);
        booleanOptions.put("rooms.AQUARIUM.enabled", true);
        booleanOptions.put("rooms.AQUARIUM.user", false);
        booleanOptions.put("rooms.ARBORETUM.enabled", true);
        booleanOptions.put("rooms.ARBORETUM.user", false);
        booleanOptions.put("rooms.BAKER.enabled", true);
        booleanOptions.put("rooms.BAKER.user", false);
        booleanOptions.put("rooms.BAMBOO.enabled", true);
        booleanOptions.put("rooms.BAMBOO.user", false);
        booleanOptions.put("rooms.BEDROOM.enabled", true);
        booleanOptions.put("rooms.BEDROOM.user", false);
        booleanOptions.put("rooms.BIRDCAGE.enabled", true);
        booleanOptions.put("rooms.BIRDCAGE.user", false);
        booleanOptions.put("rooms.CHEMISTRY.enabled", true);
        booleanOptions.put("rooms.CHEMISTRY.user", false);
        booleanOptions.put("rooms.EMPTY.enabled", true);
        booleanOptions.put("rooms.EMPTY.user", false);
        booleanOptions.put("rooms.FARM.enabled", true);
        booleanOptions.put("rooms.FARM.user", false);
        booleanOptions.put("rooms.GEODE.enabled", true);
        booleanOptions.put("rooms.GEODE.user", false);
        booleanOptions.put("rooms.GRAVITY.enabled", true);
        booleanOptions.put("rooms.GRAVITY.user", false);
        booleanOptions.put("rooms.GREENHOUSE.enabled", true);
        booleanOptions.put("rooms.GREENHOUSE.user", false);
        booleanOptions.put("rooms.HARMONY.enabled", true);
        booleanOptions.put("rooms.HARMONY.user", false);
        booleanOptions.put("rooms.HUTCH.enabled", true);
        booleanOptions.put("rooms.HUTCH.user", false);
        booleanOptions.put("rooms.IGLOO.enabled", true);
        booleanOptions.put("rooms.IGLOO.user", false);
        booleanOptions.put("rooms.KITCHEN.enabled", true);
        booleanOptions.put("rooms.KITCHEN.user", false);
        booleanOptions.put("rooms.LAZARUS.enabled", true);
        booleanOptions.put("rooms.LAZARUS.user", false);
        booleanOptions.put("rooms.LIBRARY.enabled", true);
        booleanOptions.put("rooms.LIBRARY.user", false);
        booleanOptions.put("rooms.MAZE.enabled", true);
        booleanOptions.put("rooms.MAZE.user", false);
        booleanOptions.put("rooms.MUSHROOM.enabled", true);
        booleanOptions.put("rooms.MUSHROOM.user", false);
        booleanOptions.put("rooms.NETHER.enabled", true);
        booleanOptions.put("rooms.NETHER.user", false);
        booleanOptions.put("rooms.PASSAGE.enabled", true);
        booleanOptions.put("rooms.PASSAGE.user", false);
        booleanOptions.put("rooms.POOL.enabled", true);
        booleanOptions.put("rooms.POOL.user", false);
        booleanOptions.put("rooms.RAIL.enabled", true);
        booleanOptions.put("rooms.RAIL.user", false);
        booleanOptions.put("rooms.RENDERER.enabled", true);
        booleanOptions.put("rooms.RENDERER.user", false);
        booleanOptions.put("rooms.SHELL.enabled", true);
        booleanOptions.put("rooms.SHELL.user", false);
        booleanOptions.put("rooms.SMELTER.enabled", true);
        booleanOptions.put("rooms.SMELTER.user", false);
        booleanOptions.put("rooms.STABLE.enabled", true);
        booleanOptions.put("rooms.STABLE.user", false);
        booleanOptions.put("rooms.STALL.enabled", true);
        booleanOptions.put("rooms.STALL.user", false);
        booleanOptions.put("rooms.TRENZALORE.enabled", true);
        booleanOptions.put("rooms.TRENZALORE.user", false);
        booleanOptions.put("rooms.VAULT.enabled", true);
        booleanOptions.put("rooms.VAULT.user", false);
        booleanOptions.put("rooms.VILLAGE.enabled", true);
        booleanOptions.put("rooms.VILLAGE.user", false);
        booleanOptions.put("rooms.WOOD.enabled", true);
        booleanOptions.put("rooms.WOOD.user", false);
        booleanOptions.put("rooms.WORKSHOP.enabled", true);
        booleanOptions.put("rooms.WORKSHOP.user", false);
        booleanOptions.put("rooms.ZERO.enabled", true);
        booleanOptions.put("rooms.ZERO.user", false);
        // integer
        integerOptions.put("rooms.ANTIGRAVITY.cost", 625);
        integerOptions.put("rooms.ANTIGRAVITY.offset", -4);
        integerOptions.put("rooms.APIARY.cost", 450);
        integerOptions.put("rooms.APIARY.offset", -4);
        integerOptions.put("rooms.AQUARIUM.cost", 450);
        integerOptions.put("rooms.AQUARIUM.offset", -4);
        integerOptions.put("rooms.ARBORETUM.cost", 325);
        integerOptions.put("rooms.ARBORETUM.offset", -4);
        integerOptions.put("rooms.BAKER.cost", 350);
        integerOptions.put("rooms.BAKER.offset", -4);
        integerOptions.put("rooms.BAMBOO.cost", 475);
        integerOptions.put("rooms.BAMBOO.offset", -4);
        integerOptions.put("rooms.BEDROOM.cost", 475);
        integerOptions.put("rooms.BEDROOM.offset", -4);
        integerOptions.put("rooms.BIRDCAGE.cost", 350);
        integerOptions.put("rooms.BIRDCAGE.offset", -4);
        integerOptions.put("rooms.CHEMISTRY.cost", 550);
        integerOptions.put("rooms.CHEMISTRY.offset", -4);
        integerOptions.put("rooms.EMPTY.cost", 250);
        integerOptions.put("rooms.EMPTY.offset", -4);
        integerOptions.put("rooms.FARM.cost", 350);
        integerOptions.put("rooms.FARM.offset", -4);
        integerOptions.put("rooms.GEODE.cost", 650);
        integerOptions.put("rooms.GEODE.offset", -4);
        integerOptions.put("rooms.GRAVITY.cost", 625);
        integerOptions.put("rooms.GRAVITY.offset", -20);
        integerOptions.put("rooms.GREENHOUSE.cost", 450);
        integerOptions.put("rooms.GREENHOUSE.offset", -4);
        integerOptions.put("rooms.HARMONY.cost", 450);
        integerOptions.put("rooms.HARMONY.offset", -4);
        integerOptions.put("rooms.HUTCH.cost", 450);
        integerOptions.put("rooms.HUTCH.offset", -4);
        integerOptions.put("rooms.IGLOO.cost", 650);
        integerOptions.put("rooms.IGLOO.offset", -4);
        integerOptions.put("rooms.KITCHEN.cost", 450);
        integerOptions.put("rooms.KITCHEN.offset", -4);
        integerOptions.put("rooms.LAZARUS.cost", 750);
        integerOptions.put("rooms.LAZARUS.offset", -4);
        integerOptions.put("rooms.LIBRARY.cost", 550);
        integerOptions.put("rooms.LIBRARY.offset", -4);
        integerOptions.put("rooms.MAZE.cost", 650);
        integerOptions.put("rooms.MAZE.offset", -4);
        integerOptions.put("rooms.MUSHROOM.cost", 350);
        integerOptions.put("rooms.MUSHROOM.offset", -4);
        integerOptions.put("rooms.NETHER.cost", 450);
        integerOptions.put("rooms.NETHER.offset", -4);
        integerOptions.put("rooms.PASSAGE.cost", 200);
        integerOptions.put("rooms.PASSAGE.offset", -4);
        integerOptions.put("rooms.POOL.cost", 450);
        integerOptions.put("rooms.POOL.offset", -4);
        integerOptions.put("rooms.RAIL.cost", 650);
        integerOptions.put("rooms.RAIL.offset", -4);
        integerOptions.put("rooms.RENDERER.cost", 650);
        integerOptions.put("rooms.RENDERER.offset", -4);
        integerOptions.put("rooms.SHELL.cost", 550);
        integerOptions.put("rooms.SHELL.offset", -4);
        integerOptions.put("rooms.SMELTER.cost", 750);
        integerOptions.put("rooms.SMELTER.offset", -4);
        integerOptions.put("rooms.STABLE.cost", 350);
        integerOptions.put("rooms.STABLE.offset", -4);
        integerOptions.put("rooms.STALL.cost", 350);
        integerOptions.put("rooms.STALL.offset", -4);
        integerOptions.put("rooms.TRENZALORE.cost", 550);
        integerOptions.put("rooms.TRENZALORE.offset", -4);
        integerOptions.put("rooms.VAULT.cost", 350);
        integerOptions.put("rooms.VAULT.offset", -4);
        integerOptions.put("rooms.VILLAGE.cost", 550);
        integerOptions.put("rooms.VILLAGE.offset", -4);
        integerOptions.put("rooms.WOOD.cost", 350);
        integerOptions.put("rooms.WOOD.offset", -4);
        integerOptions.put("rooms.WORKSHOP.cost", 400);
        integerOptions.put("rooms.WORKSHOP.offset", -4);
        integerOptions.put("rooms.ZERO.cost", 650);
        integerOptions.put("rooms.ZERO.offset", -4);
        // string
        stringOptions.put("rooms.ANTIGRAVITY.seed", "SANDSTONE");
        stringOptions.put("rooms.APIARY.seed", "BEE_NEST");
        stringOptions.put("rooms.AQUARIUM.seed", "TUBE_CORAL_BLOCK");
        stringOptions.put("rooms.ARBORETUM.seed", "OAK_LEAVES");
        stringOptions.put("rooms.BAKER.seed", "END_STONE");
        stringOptions.put("rooms.BAMBOO.seed", "BAMBOO");
        stringOptions.put("rooms.BEDROOM.seed", "GLOWSTONE");
        stringOptions.put("rooms.BIRDCAGE.seed", "YELLOW_GLAZED_TERRACOTTA");
        stringOptions.put("rooms.CHEMISTRY.seed", "BLAST_FURNACE");
        stringOptions.put("rooms.EMPTY.seed", "GLASS");
        stringOptions.put("rooms.FARM.seed", "DIRT");
        stringOptions.put("rooms.GEODE.seed", "AMETHYST_BLOCK");
        stringOptions.put("rooms.GRAVITY.seed", "MOSSY_COBBLESTONE");
        stringOptions.put("rooms.GREENHOUSE.seed", "MELON");
        stringOptions.put("rooms.HARMONY.seed", "BRICK_STAIRS");
        stringOptions.put("rooms.HUTCH.seed", "ACACIA_LOG");
        stringOptions.put("rooms.IGLOO.seed", "PACKED_ICE");
        stringOptions.put("rooms.KITCHEN.seed", "PUMPKIN");
        stringOptions.put("rooms.LAZARUS.seed", "FURNACE");
        stringOptions.put("rooms.LIBRARY.seed", "ENCHANTMENT_TABLE");
        stringOptions.put("rooms.MAZE.seed", "LODESTONE");
        stringOptions.put("rooms.MUSHROOM.seed", "GRAVEL");
        stringOptions.put("rooms.NETHER.seed", "BLACKSTONE");
        stringOptions.put("rooms.PASSAGE.seed", "CLAY");
        stringOptions.put("rooms.POOL.seed", "SNOW_BLOCK");
        stringOptions.put("rooms.RAIL.seed", "HOPPER");
        stringOptions.put("rooms.RENDERER.seed", "TERRACOTTA");
        stringOptions.put("rooms.SHELL.seed", "DEAD_BRAIN_CORAL_BLOCK");
        stringOptions.put("rooms.SMELTER.seed", "CHEST");
        stringOptions.put("rooms.STABLE.seed", "HAY_BLOCK");
        stringOptions.put("rooms.STALL.seed", "BROWN_GLAZED_TERRACOTTA");
        stringOptions.put("rooms.TRENZALORE.seed", "BRICK");
        stringOptions.put("rooms.VAULT.seed", "DISPENSER");
        stringOptions.put("rooms.VILLAGE.seed", "LOG");
        stringOptions.put("rooms.WOOD.seed", "WOOD");
        stringOptions.put("rooms.WORKSHOP.seed", "WORKBENCH");
        stringOptions.put("rooms.ZERO.seed", "WOOD_BUTTON");
    }

    public void checkRoomsConfig() {
        int i = 0;
        // boolean values
        for (Map.Entry<String, Boolean> entry : booleanOptions.entrySet()) {
            if (!rooms_config.contains(entry.getKey())) {
                rooms_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // int values
        for (Map.Entry<String, Integer> entry : integerOptions.entrySet()) {
            if (!rooms_config.contains(entry.getKey()) || (entry.getKey().equals("rooms.RAIL.offset")) && rooms_config.getInt("rooms.RAIL.offset") == -2) {
                rooms_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // string values
        for (Map.Entry<String, String> entry : stringOptions.entrySet()) {
            if (!rooms_config.contains(entry.getKey())) {
                rooms_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        // 1.13 material updates
        if (rooms_config.getString("rooms.GREENHOUSE.seed").equals("MELON_BLOCK")) {
            rooms_config.set("rooms.ARBORETUM.seed", "OAK_LEAVES");
            rooms_config.set("rooms.BAKER.seed", "END_STONE");
            rooms_config.set("rooms.GREENHOUSE.seed", "MELON");
            rooms_config.set("rooms.HARMONY.seed", "STONE_BRICK_STAIRS");
            rooms_config.set("rooms.HUTCH.seed", "ACACIA_LOG");
            rooms_config.set("rooms.LIBRARY.seed", "ENCHANTING_TABLE");
            rooms_config.set("rooms.RENDERER.seed", "TERRACOTTA");
            rooms_config.set("rooms.TRENZALORE.seed", "BRICKS");
            rooms_config.set("rooms.VILLAGE.seed", "OAK_LOG");
            rooms_config.set("rooms.WOOD.seed", "OAK_PLANKS");
            rooms_config.set("rooms.WORKSHOP.seed", "CRAFTING_TABLE");
            rooms_config.set("rooms.ZERO.seed", "OAK_BUTTON");
            i++;
        }
        try {
            rooms_config.save(new File(plugin.getDataFolder(), "rooms.yml"));
            if (i > 0) {
                plugin.getLogger().log(Level.INFO, "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to rooms.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save rooms.yml, " + io.getMessage());
        }
    }
}
