/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * A TARDIS has fifteen Power Rooms that contain all of the machinery that
 * powers and operates the ship. They are located deep in a TARDIS's interior
 * and serve as the "nerve centre" of the capsule.
 *
 * @author eccentric_nz
 */
public class TARDISCondensables {

    private final TARDIS plugin;
    private HashMap<String, Integer> condensables;

    public TARDISCondensables(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void makeCondensables() {
        condensables = new HashMap<String, Integer>();
        if (plugin.getConfig().getBoolean("preferences.use_default_condensables")) {
            condensables.put("ACACIA_DOOR_ITEM", 3);
            condensables.put("ACACIA_FENCE", 1);
            condensables.put("ACACIA_FENCE_GATE", 5);
            condensables.put("ACACIA_STAIRS", 1);
            condensables.put("ACACIA_STAIRS", 1);
            condensables.put("ANVIL", 200);
            condensables.put("APPLE", 10);
            condensables.put("ARMOR_STAND", 8);
            condensables.put("ARROW", 4);
            condensables.put("BAKED_POTATO", 14);
            condensables.put("BANNER", 120);
            condensables.put("BEACON", 700);
            condensables.put("BED", 68);
            condensables.put("BEDROCK", 100);
            condensables.put("BEEF", 3);
            condensables.put("BIRCH_DOOR_ITEM", 3);
            condensables.put("BIRCH_FENCE", 1);
            condensables.put("BIRCH_FENCE_GATE", 5);
            condensables.put("BIRCH_WOOD_STAIRS", 1);
            condensables.put("BIRCH_WOOD_STAIRS", 1);
            condensables.put("BLAZE_ROD", 200);
            condensables.put("BOAT", 3);
            condensables.put("BONE", 2);
            condensables.put("BOOK", 45);
            condensables.put("BOOKSHELF", 140);
            condensables.put("BOW", 15);
            condensables.put("BOWL", 1);
            condensables.put("BREAD", 30);
            condensables.put("BREWING_STAND_ITEM", 200);
            condensables.put("BRICK", 21);
            condensables.put("BRICK_STAIRS", 21);
            condensables.put("BROWN_MUSHROOM", 2);
            condensables.put("BUCKET", 22);
            condensables.put("CACTUS", 10);
            condensables.put("CAKE", 180);
            condensables.put("CARPET", 13);
            condensables.put("CARROT", 9);
            condensables.put("CARROT_ITEM", 9);
            condensables.put("CAULDRON", 154);
            condensables.put("CAULDRON_ITEM", 154);
            condensables.put("CHEST", 4);
            condensables.put("CLAY", 12);
            condensables.put("CLAY_BALL", 3);
            condensables.put("CLAY_BRICK", 5);
            condensables.put("COAL", 15);
            condensables.put("COAL_BLOCK", 135);
            condensables.put("COAL_ORE", 15);
            condensables.put("COBBLESTONE", 1);
            condensables.put("COBBLESTONE_STAIRS", 2);
            condensables.put("COBBLE_WALL", 2);
            condensables.put("COCOA", 1);
            condensables.put("COMPASS", 22);
            condensables.put("COOKED_BEEF", 7);
            condensables.put("COOKED_CHICKEN", 7);
            condensables.put("COOKED_FISH", 7);
            condensables.put("COOKED_MUTTON", 7);
            condensables.put("COOKED_RABBIT", 7);
            condensables.put("COOKIE", 3);
            condensables.put("DARK_OAK_DOOR_ITEM", 3);
            condensables.put("DARK_OAK_FENCE", 1);
            condensables.put("DARK_OAK_FENCE_GATE", 5);
            condensables.put("DARK_OAK_STAIRS", 1);
            condensables.put("DARK_OAK_STAIRS", 1);
            condensables.put("DAYLIGHT_DETECTOR", 130);
            condensables.put("DAYLIGHT_DETECTOR_INVERTED", 130);
            condensables.put("DIAMOND", 200);
            condensables.put("DIAMOND_AXE", 650);
            condensables.put("DIAMOND_BLOCK", 2000);
            condensables.put("DIAMOND_BOOTS", 850);
            condensables.put("DIAMOND_CHESTPLATE", 1750);
            condensables.put("DIAMOND_HELMET", 1000);
            condensables.put("DIAMOND_HOE", 400);
            condensables.put("DIAMOND_LEGGINGS", 1500);
            condensables.put("DIAMOND_PICKAXE", 650);
            condensables.put("DIAMOND_SPADE", 210);
            condensables.put("DIAMOND_SWORD", 420);
            condensables.put("DIODE", 110);
            condensables.put("DIODE_BLOCK_OFF", 110);
            condensables.put("DIODE_BLOCK_ON", 110);
            condensables.put("DIRT", 1);
            condensables.put("DISPENSER", 58);
            condensables.put("DOUBLE_PLANT", 1);
            condensables.put("DOUBLE_STEP", 1);
            condensables.put("DOUBLE_STONE_SLAB_2", 1);
            condensables.put("DROPPER", 58);
            condensables.put("EGG", 1);
            condensables.put("EMERALD", 200);
            condensables.put("EMERALD_BLOCK", 2000);
            condensables.put("ENCHANTMENT_TABLE", 1365);
            condensables.put("ENDER_STONE", 2);
            condensables.put("EYE_OF_ENDER", 200);
            condensables.put("FEATHER", 3);
            condensables.put("FENCE", 1);
            condensables.put("FENCE_GATE", 5);
            condensables.put("FISHING_ROD", 10);
            condensables.put("FLINT", 2);
            condensables.put("FLOWER_POT_ITEM", 15);
            condensables.put("FURNACE", 9);
            condensables.put("GLASS", 30);
            condensables.put("GLOWSTONE", 40);
            condensables.put("GLOWSTONE_DUST", 10);
            condensables.put("GOLDEN_APPLE", 100);
            condensables.put("GOLDEN_CARROT", 100);
            condensables.put("GOLD_AXE", 16);
            condensables.put("GOLD_BLOCK", 450);
            condensables.put("GOLD_BOOTS", 16);
            condensables.put("GOLD_CHESTPLATE", 17);
            condensables.put("GOLD_HELMET", 16);
            condensables.put("GOLD_HOE", 16);
            condensables.put("GOLD_INGOT", 105);
            condensables.put("GOLD_LEGGINGS", 16);
            condensables.put("GOLD_NUGGET", 12);
            condensables.put("GOLD_PICKAXE", 16);
            condensables.put("GOLD_PLATE", 205);
            condensables.put("GOLD_RECORD", 100);
            condensables.put("GOLD_SPADE", 16);
            condensables.put("GOLD_SWORD", 16);
            condensables.put("GRASS", 1);
            condensables.put("GRAVEL", 1);
            condensables.put("GREEN_RECORD", 100);
            condensables.put("GRILLED_PORK", 7);
            condensables.put("HARD_CLAY", 12);
            condensables.put("HAY_BLOCK", 81);
            condensables.put("HOPPER", 110);
            condensables.put("INK_SACK", 10);
            condensables.put("IRON_AXE", 22);
            condensables.put("IRON_BLOCK", 190);
            condensables.put("IRON_BOOTS", 44);
            condensables.put("IRON_CHESTPLATE", 44);
            condensables.put("IRON_DOOR", 132);
            condensables.put("IRON_FENCE", 8);
            condensables.put("IRON_HELMET", 44);
            condensables.put("IRON_HOE", 22);
            condensables.put("IRON_INGOT", 22);
            condensables.put("IRON_LEGGINGS", 44);
            condensables.put("IRON_PICKAXE", 26);
            condensables.put("IRON_PLATE", 44);
            condensables.put("IRON_SPADE", 26);
            condensables.put("IRON_SWORD", 24);
            condensables.put("IRON_TRAPDOOR", 88);
            condensables.put("JACK_O_LANTERN", 56);
            condensables.put("JUKEBOX", 208);
            condensables.put("JUNGLE_DOOR_ITEM", 3);
            condensables.put("JUNGLE_FENCE", 1);
            condensables.put("JUNGLE_FENCE_GATE", 5);
            condensables.put("JUNGLE_WOOD_STAIRS", 1);
            condensables.put("JUNGLE_WOOD_STAIRS", 1);
            condensables.put("LADDER", 1);
            condensables.put("LAPIS_BLOCK", 950);
            condensables.put("LAVA_BUCKET", 40);
            condensables.put("LEASH", 70);
            condensables.put("LEATHER", 10);
            condensables.put("LEATHER_BOOTS", 42);
            condensables.put("LEATHER_CHESTPLATE", 85);
            condensables.put("LEATHER_HELMET", 52);
            condensables.put("LEATHER_LEGGINGS", 75);
            condensables.put("LEAVES", 1);
            condensables.put("LEAVES_2", 1);
            condensables.put("LEVER", 1);
            condensables.put("LOG", 2);
            condensables.put("LOG_2", 2);
            condensables.put("MELON_SEEDS", 2);
            condensables.put("MILK_BUCKET", 40);
            condensables.put("MINECART", 23);
            condensables.put("MOSSY_COBBLESTONE", 10);
            condensables.put("MUSHROOM_SOUP", 5);
            condensables.put("MUTTON", 3);
            condensables.put("MYCEL", 1);
            condensables.put("NETHERRACK", 2);
            condensables.put("NETHER_BRICK", 4);
            condensables.put("NETHER_BRICK_ITEM", 4);
            condensables.put("NETHER_BRICK_STAIRS", 21);
            condensables.put("NETHER_FENCE", 10);
            condensables.put("NETHER_STALK", 2);
            condensables.put("NETHER_WARTS", 2);
            condensables.put("NOTE_BLOCK", 40);
            condensables.put("OBSIDIAN", 130);
            condensables.put("PACKED_ICE", 100);
            condensables.put("PAINTING", 25);
            condensables.put("PAPER", 10);
            condensables.put("PISTON_BASE", 60);
            condensables.put("PISTON_STICKY_BASE", 110);
            condensables.put("POISONOUS_POTATO", 1);
            condensables.put("PORK", 3);
            condensables.put("POTATO", 9);
            condensables.put("POTATO_ITEM", 9);
            condensables.put("POWERED_MINECART", 32);
            condensables.put("POWERED_RAIL", 650);
            condensables.put("PRISMARINE", 32);
            condensables.put("PRISMARINE_CRYSTALS", 16);
            condensables.put("PRISMARINE_SHARD", 8);
            condensables.put("PUMPKIN", 50);
            condensables.put("PUMPKIN_PIE", 60);
            condensables.put("PUMPKIN_SEEDS", 2);
            condensables.put("QUARTZ", 15);
            condensables.put("QUARTZ_BLOCK", 60);
            condensables.put("QUARTZ_ORE", 15);
            condensables.put("QUARTZ_STAIRS", 22);
            condensables.put("RABBIT", 3);
            condensables.put("RABBIT_FOOT", 100);
            condensables.put("RABBIT_HIDE", 10);
            condensables.put("RABBIT_STEW", 33);
            condensables.put("RAILS", 22);
            condensables.put("RAW_BEEF", 5);
            condensables.put("RAW_CHICKEN", 5);
            condensables.put("RAW_FISH", 5);
            condensables.put("REDSTONE", 32);
            condensables.put("REDSTONE_BLOCK", 288);
            condensables.put("REDSTONE_COMPARATOR", 110);
            condensables.put("REDSTONE_COMPARATOR_OFF", 110);
            condensables.put("REDSTONE_COMPARATOR_ON", 110);
            condensables.put("REDSTONE_LAMP_OFF", 160);
            condensables.put("REDSTONE_TORCH_OFF", 32);
            condensables.put("REDSTONE_TORCH_ON", 32);
            condensables.put("REDSTONE_WIRE", 32);
            condensables.put("RED_MUSHROOM", 2);
            condensables.put("RED_ROSE", 2);
            condensables.put("RED_SANDSTONE", 3);
            condensables.put("RED_SANDSTONE_STAIRS", 19);
            condensables.put("ROTTEN_FLESH", 1);
            condensables.put("SADDLE", 100);
            condensables.put("SAND", 1);
            condensables.put("SANDSTONE", 3);
            condensables.put("SANDSTONE_STAIRS", 18);
            condensables.put("SAPLING", 2);
            condensables.put("SEA_LANTERN", 160);
            condensables.put("SEEDS", 2);
            condensables.put("SHEARS", 44);
            condensables.put("SIGN", 2);
            condensables.put("SLIME_BALL", 50);
            condensables.put("SLIME_BLOCK", 450);
            condensables.put("SMOOTH_BRICK", 4);
            condensables.put("SMOOTH_STAIRS", 24);
            condensables.put("SNOWBALL", 1);
            condensables.put("SNOW_BALL", 1);
            condensables.put("SNOW_BLOCK", 4);
            condensables.put("SOIL", 1);
            condensables.put("SOUL_SAND", 2);
            condensables.put("SPIDER_EYE", 10);
            condensables.put("SPONGE", 80);
            condensables.put("SPRUCE_DOOR_ITEM", 3);
            condensables.put("SPRUCE_FENCE", 1);
            condensables.put("SPRUCE_FENCE_GATE", 5);
            condensables.put("SPRUCE_WOOD_STAIRS", 1);
            condensables.put("SPRUCE_WOOD_STAIRS", 1);
            condensables.put("STAINED_CLAY", 15);
            condensables.put("STAINED_GLASS", 32);
            condensables.put("STAINED_GLASS_PANE", 6);
            condensables.put("STEP", 2);
            condensables.put("STICK", 1);
            condensables.put("STONE", 30);
            condensables.put("STONE_AXE", 3);
            condensables.put("STONE_BUTTON", 6);
            condensables.put("STONE_HOE", 3);
            condensables.put("STONE_PICKAXE", 4);
            condensables.put("STONE_PLATE", 6);
            condensables.put("STONE_SLAB", 3);
            condensables.put("STONE_SPADE", 2);
            condensables.put("STONE_SWORD", 3);
            condensables.put("STORAGE_MINECART", 30);
            condensables.put("STRING", 5);
            condensables.put("SUGAR", 10);
            condensables.put("SUGAR_CANE", 10);
            condensables.put("SULPHUR", 20);
            condensables.put("THIN_GLASS", 5);
            condensables.put("TNT", 100);
            condensables.put("TORCH", 4);
            condensables.put("TRAPPED_CHEST", 30);
            condensables.put("TRAP_DOOR", 6);
            condensables.put("TRIPWIRE", 2);
            condensables.put("TRIPWIRE_HOOK", 25);
            condensables.put("VINE", 1);
            condensables.put("WALL_SIGN", 1);
            condensables.put("WATCH", 6);
            condensables.put("WATER_BUCKET", 40);
            condensables.put("WATER_LILY", 1);
            condensables.put("WHEAT", 9);
            condensables.put("WOOD", 1);
            condensables.put("WOOD_AXE", 2);
            condensables.put("WOOD_BUTTON", 1);
            condensables.put("WOOD_DOOR", 3);
            condensables.put("WOOD_DOUBLE_STEP", 2);
            condensables.put("WOOD_HOE", 2);
            condensables.put("WOOD_PICKAXE", 2);
            condensables.put("WOOD_PLATE", 1);
            condensables.put("WOOD_SPADE", 1);
            condensables.put("WOOD_STAIRS", 1);
            condensables.put("WOOD_STEP", 1);
            condensables.put("WOOD_SWORD", 1);
            condensables.put("WOOL", 20);
            condensables.put("WORKBENCH", 3);
            condensables.put("YELLOW_FLOWER", 2);
        } else {
            Set<String> items = plugin.getCondensablesConfig().getKeys(false);
            for (String item : items) {
                condensables.put(item, plugin.getCondensablesConfig().getInt(item));
            }
        }
    }

    public HashMap<String, Integer> getCondensables() {
        return condensables;
    }
}
