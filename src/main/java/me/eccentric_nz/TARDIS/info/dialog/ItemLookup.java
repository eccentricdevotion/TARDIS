/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.info.dialog;

import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.RoomVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicItem;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import org.bukkit.Material;

import java.util.HashMap;

public class ItemLookup {

    public static final HashMap<TARDISInfoMenu, InfoIcon> ITEMS = new HashMap<>();

    static {
        // TARDIS items
        ITEMS.put(TARDISInfoMenu.KEY_INFO, new InfoIcon(Material.GOLD_NUGGET, "TARDIS Key", KeyVariant.BRASS_YALE.getKey()));
        ITEMS.put(TARDISInfoMenu.SONIC_SCREWDRIVER_INFO, new InfoIcon(Material.BLAZE_ROD, "Sonic Screwdriver", SonicVariant.ELEVENTH.getKey()));
        ITEMS.put(TARDISInfoMenu.LOCATOR_INFO, new InfoIcon(Material.COMPASS, "TARDIS Locator", RecipeItem.TARDIS_LOCATOR.getModel()));
        ITEMS.put(TARDISInfoMenu.STATTENHEIM_REMOTE_INFO, new InfoIcon(Material.FLINT, "Stattenheim Remote", RecipeItem.STATTENHEIM_REMOTE.getModel()));
        ITEMS.put(TARDISInfoMenu.BIOME_READER_INFO, new InfoIcon(Material.BRICK, "TARDIS Biome Reader", RecipeItem.TARDIS_BIOME_READER.getModel()));
        ITEMS.put(TARDISInfoMenu.REMOTE_KEY_INFO, new InfoIcon(Material.OMINOUS_TRIAL_KEY, "TARDIS Remote Key", RecipeItem.TARDIS_REMOTE_KEY.getModel()));
        ITEMS.put(TARDISInfoMenu.ARTRON_CAPACITOR_INFO, new InfoIcon(Material.BUCKET, "Artron Capacitor", RecipeItem.ARTRON_CAPACITOR.getModel()));
        ITEMS.put(TARDISInfoMenu.ARTRON_STORAGE_CELL_INFO, new InfoIcon(Material.BUCKET, "Artron Storage Cell", RecipeItem.ARTRON_STORAGE_CELL.getModel()));
        ITEMS.put(TARDISInfoMenu.ARTRON_FURNACE_INFO, new InfoIcon(Material.FURNACE, "Artron Furnace", RecipeItem.TARDIS_ARTRON_FURNACE.getModel()));
        ITEMS.put(TARDISInfoMenu.PERCEPTION_FILTER_INFO, new InfoIcon(Material.OMINOUS_TRIAL_KEY, "Perception Filter", RecipeItem.PERCEPTION_FILTER.getModel()));
        ITEMS.put(TARDISInfoMenu.SONIC_GENERATOR_INFO, new InfoIcon(Material.FLOWER_POT, "Sonic Generator", SonicItem.SONIC_GENERATOR.getKey()));
        ITEMS.put(TARDISInfoMenu.SONIC_BLASTER_INFO, new InfoIcon(Material.GOLDEN_HOE, "Sonic Blaster", RecipeItem.SONIC_BLASTER.getModel()));
        ITEMS.put(TARDISInfoMenu.VORTEX_MANIPULATOR_INFO, new InfoIcon(Material.CLOCK, "Vortex Manipulator", RecipeItem.VORTEX_MANIPULATOR.getModel()));
        ITEMS.put(TARDISInfoMenu.HANDLES_INFO, new InfoIcon(Material.BIRCH_BUTTON, "Handles", RecipeItem.HANDLES.getModel()));
        // rooms
        ITEMS.put(TARDISInfoMenu.ALLAY, new InfoIcon(Material.LIGHT_BLUE_CONCRETE, "Allay House", RoomVariant.ALLAY.getKey()));
        ITEMS.put(TARDISInfoMenu.ANTIGRAVITY, new InfoIcon(Material.SANDSTONE, "Anti-gravity Well", RoomVariant.ANTIGRAVITY.getKey()));
        ITEMS.put(TARDISInfoMenu.APIARY, new InfoIcon(Material.BEE_NEST, "Apiary", RoomVariant.APIARY.getKey()));
        ITEMS.put(TARDISInfoMenu.AQUARIUM, new InfoIcon(Material.TUBE_CORAL_BLOCK, "Aquarium", RoomVariant.AQUARIUM.getKey()));
        ITEMS.put(TARDISInfoMenu.ARBORETUM, new InfoIcon(Material.OAK_LEAVES, "Arboretum", RoomVariant.ARBORETUM.getKey()));
        ITEMS.put(TARDISInfoMenu.BAKER, new InfoIcon(Material.END_STONE, "4th Doctor's Secondary Console", RoomVariant.BAKER.getKey()));
        ITEMS.put(TARDISInfoMenu.BAMBOO, new InfoIcon(Material.BAMBOO, "Bamboo", RoomVariant.BAMBOO.getKey()));
        ITEMS.put(TARDISInfoMenu.BEDROOM, new InfoIcon(Material.GLOWSTONE, "Bedroom", RoomVariant.BEDROOM.getKey()));
        ITEMS.put(TARDISInfoMenu.BIRDCAGE, new InfoIcon(Material.YELLOW_GLAZED_TERRACOTTA, "Bird Cage", RoomVariant.BIRDCAGE.getKey()));
        ITEMS.put(TARDISInfoMenu.CHEMISTRY, new InfoIcon(Material.BLAST_FURNACE, "Chemistry Lab", RoomVariant.CHEMISTRY.getKey()));
        ITEMS.put(TARDISInfoMenu.EMPTY, new InfoIcon(Material.GLASS, "Empty", RoomVariant.EMPTY.getKey()));
        ITEMS.put(TARDISInfoMenu.EYE, new InfoIcon(Material.SHROOMLIGHT, "Eye of Harmony", RoomVariant.EYE.getKey()));
        ITEMS.put(TARDISInfoMenu.FARM, new InfoIcon(Material.DIRT, "Mob Farm", RoomVariant.FARM.getKey()));
        ITEMS.put(TARDISInfoMenu.GARDEN, new InfoIcon(Material.CHERRY_LEAVES, "Flower Garden", RoomVariant.GARDEN.getKey()));
        ITEMS.put(TARDISInfoMenu.GEODE, new InfoIcon(Material.AMETHYST_BLOCK, "Geode", RoomVariant.GEODE.getKey()));
        ITEMS.put(TARDISInfoMenu.GRAVITY, new InfoIcon(Material.MOSSY_COBBLESTONE, "Gravity Well", RoomVariant.GRAVITY.getKey()));
        ITEMS.put(TARDISInfoMenu.GREENHOUSE, new InfoIcon(Material.MELON, "Greenhouse", RoomVariant.GREENHOUSE.getKey()));
        ITEMS.put(TARDISInfoMenu.HAPPY, new InfoIcon(Material.STRIPPED_OAK_LOG, "Happy Ghast Dock", RoomVariant.HAPPY.getKey()));
        ITEMS.put(TARDISInfoMenu.HARMONY, new InfoIcon(Material.STONE_BRICK_STAIRS, "Condenser", RoomVariant.HARMONY.getKey()));
        ITEMS.put(TARDISInfoMenu.HUTCH, new InfoIcon(Material.ACACIA_LOG, "Rabbit Hutch", RoomVariant.HUTCH.getKey()));
        ITEMS.put(TARDISInfoMenu.IGLOO, new InfoIcon(Material.PACKED_ICE, "Igloo", RoomVariant.IGLOO.getKey()));
        ITEMS.put(TARDISInfoMenu.IISTUBIL, new InfoIcon(Material.WHITE_GLAZED_TERRACOTTA, "Camel Stable", RoomVariant.IISTUBIL.getKey()));
        ITEMS.put(TARDISInfoMenu.KITCHEN, new InfoIcon(Material.PUMPKIN, "Kitchen", RoomVariant.KITCHEN.getKey()));
        ITEMS.put(TARDISInfoMenu.LAVA, new InfoIcon(Material.MAGMA_BLOCK, "Lava", RoomVariant.LAVA.getKey()));
        ITEMS.put(TARDISInfoMenu.LAZARUS, new InfoIcon(Material.FURNACE, "Genetic Manipulator", RoomVariant.LAZARUS.getKey()));
        ITEMS.put(TARDISInfoMenu.LIBRARY, new InfoIcon(Material.ENCHANTING_TABLE, "Library", RoomVariant.LIBRARY.getKey()));
        ITEMS.put(TARDISInfoMenu.MANGROVE, new InfoIcon(Material.MUDDY_MANGROVE_ROOTS, "Mangrove", RoomVariant.MANGROVE.getKey()));
        ITEMS.put(TARDISInfoMenu.MAZE, new InfoIcon(Material.LODESTONE, "Maze", RoomVariant.MAZE.getKey()));
        ITEMS.put(TARDISInfoMenu.MUSHROOM, new InfoIcon(Material.GRAVEL, "Mycellium", RoomVariant.MUSHROOM.getKey()));
        ITEMS.put(TARDISInfoMenu.NAUTILUS, new InfoIcon(Material.DEAD_HORN_CORAL_BLOCK, "Nautilus", RoomVariant.NAUTILUS.getKey()));
        ITEMS.put(TARDISInfoMenu.NETHER, new InfoIcon(Material.BLACKSTONE, "Nether", RoomVariant.NETHER.getKey()));
        ITEMS.put(TARDISInfoMenu.OBSERVATORY, new InfoIcon(Material.POLISHED_BLACKSTONE_BRICKS, "Astronomical Observatory", RoomVariant.OBSERVATORY.getKey()));
        ITEMS.put(TARDISInfoMenu.PASSAGE, new InfoIcon(Material.CLAY, "Passage", RoomVariant.PASSAGE.getKey()));
        ITEMS.put(TARDISInfoMenu.PEN, new InfoIcon(Material.MOSS_BLOCK, "Sniffer Pen", RoomVariant.PEN.getKey()));
        ITEMS.put(TARDISInfoMenu.POOL, new InfoIcon(Material.SNOW_BLOCK, "Pool", RoomVariant.POOL.getKey()));
        ITEMS.put(TARDISInfoMenu.RAIL, new InfoIcon(Material.HOPPER, "Rail Transfer Station", RoomVariant.RAIL.getKey()));
        ITEMS.put(TARDISInfoMenu.RENDERER, new InfoIcon(Material.TERRACOTTA, "Exterior Renderer", RoomVariant.RENDERER.getKey()));
        ITEMS.put(TARDISInfoMenu.SHELL, new InfoIcon(Material.DEAD_BRAIN_CORAL_BLOCK, "Shell", RoomVariant.SHELL.getKey()));
        ITEMS.put(TARDISInfoMenu.SMELTER, new InfoIcon(Material.CHEST, "Smelter", RoomVariant.SMELTER.getKey()));
        ITEMS.put(TARDISInfoMenu.STABLE, new InfoIcon(Material.HAY_BLOCK, "Horse Stable", RoomVariant.STABLE.getKey()));
        ITEMS.put(TARDISInfoMenu.STAIRCASE, new InfoIcon(Material.PURPLE_TERRACOTTA, "Emndless Staircase", RoomVariant.STAIRCASE.getKey()));
        ITEMS.put(TARDISInfoMenu.STALL, new InfoIcon(Material.BROWN_GLAZED_TERRACOTTA, "Llama Stall", RoomVariant.STALL.getKey()));
        ITEMS.put(TARDISInfoMenu.SURGERY, new InfoIcon(Material.RED_CONCRETE, "Hospital Surgery", RoomVariant.SURGERY.getKey()));
        ITEMS.put(TARDISInfoMenu.TRENZALORE, new InfoIcon(Material.BRICKS, "Trenzalore", RoomVariant.TRENZALORE.getKey()));
        ITEMS.put(TARDISInfoMenu.VAULT, new InfoIcon(Material.DISPENSER, "Storage Vault", RoomVariant.VAULT.getKey()));
        ITEMS.put(TARDISInfoMenu.VILLAGE, new InfoIcon(Material.OAK_LOG, "Village", RoomVariant.VILLAGE.getKey()));
        ITEMS.put(TARDISInfoMenu.WOOD, new InfoIcon(Material.OAK_PLANKS, "Wood Secondary Console", RoomVariant.WOOD.getKey()));
        ITEMS.put(TARDISInfoMenu.WORKSHOP, new InfoIcon(Material.RED_NETHER_BRICKS, "Workshop", RoomVariant.WORKSHOP.getKey()));
        ITEMS.put(TARDISInfoMenu.ZERO, new InfoIcon(Material.GRASS_BLOCK, "Zero Room", RoomVariant.ZERO.getKey()));
    }
}
