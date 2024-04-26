package me.eccentric_nz.TARDIS.console.telepathic;

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EnvironmentBiomes {

    public static final List<Biome> END = List.of(Biome.THE_END, Biome.END_BARRENS, Biome.END_MIDLANDS, Biome.END_HIGHLANDS, Biome.SMALL_END_ISLANDS);
    public static final List<Biome> NETHER = List.of(Biome.NETHER_WASTES, Biome.BASALT_DELTAS, Biome.CRIMSON_FOREST, Biome.SOUL_SAND_VALLEY, Biome.WARPED_FOREST);
    public static List<Biome> OVERWORLD = new ArrayList<>();
    public static HashMap<Biome, Material> BIOME_BLOCKS = new HashMap<>() {{
        put(Biome.BADLANDS, Material.RED_SAND);
        put(Biome.BAMBOO_JUNGLE, Material.BAMBOO_BLOCK);
        put(Biome.BASALT_DELTAS, Material.BASALT);
        put(Biome.BEACH, Material.SAND);
        put(Biome.BIRCH_FOREST, Material.BIRCH_PLANKS);
        put(Biome.CHERRY_GROVE, Material.CHERRY_PLANKS);
        put(Biome.COLD_OCEAN, Material.SALMON);
        put(Biome.CRIMSON_FOREST, Material.CRIMSON_PLANKS);
        put(Biome.CUSTOM, Material.BARRIER);
        put(Biome.DARK_FOREST, Material.DARK_OAK_PLANKS);
        put(Biome.DEEP_COLD_OCEAN, Material.SEA_PICKLE);
        put(Biome.DEEP_DARK, Material.SCULK);
        put(Biome.DEEP_FROZEN_OCEAN, Material.SEAGRASS);
        put(Biome.DEEP_LUKEWARM_OCEAN, Material.TUBE_CORAL_BLOCK);
        put(Biome.DEEP_OCEAN, Material.PRISMARINE);
        put(Biome.DESERT, Material.SANDSTONE);
        put(Biome.DRIPSTONE_CAVES, Material.DRIPSTONE_BLOCK);
        put(Biome.END_BARRENS, Material.END_STONE_BRICKS);
        put(Biome.END_HIGHLANDS, Material.CHORUS_PLANT);
        put(Biome.END_MIDLANDS, Material.PURPUR_BLOCK);
        put(Biome.ERODED_BADLANDS, Material.YELLOW_TERRACOTTA);
        put(Biome.FLOWER_FOREST, Material.ORANGE_TULIP);
        put(Biome.FOREST, Material.OAK_PLANKS);
        put(Biome.FROZEN_OCEAN, Material.BLUE_ICE);
        put(Biome.FROZEN_PEAKS, Material.POWDER_SNOW_BUCKET);
        put(Biome.FROZEN_RIVER, Material.ICE);
        put(Biome.GROVE, Material.STRIPPED_SPRUCE_LOG);
        put(Biome.ICE_SPIKES, Material.PACKED_ICE);
        put(Biome.JAGGED_PEAKS, Material.GOAT_HORN);
        put(Biome.JUNGLE, Material.JUNGLE_PLANKS);
        put(Biome.LUKEWARM_OCEAN, Material.FIRE_CORAL_BLOCK);
        put(Biome.LUSH_CAVES, Material.MOSS_BLOCK);
        put(Biome.MANGROVE_SWAMP, Material.MANGROVE_PLANKS);
        put(Biome.MEADOW, Material.RED_TULIP);
        put(Biome.MUSHROOM_FIELDS, Material.MYCELIUM);
        put(Biome.NETHER_WASTES, Material.NETHERRACK);
        put(Biome.OCEAN, Material.KELP);
        put(Biome.OLD_GROWTH_BIRCH_FOREST, Material.STRIPPED_BIRCH_WOOD);
        put(Biome.OLD_GROWTH_PINE_TAIGA, Material.SPRUCE_LOG);
        put(Biome.OLD_GROWTH_SPRUCE_TAIGA, Material.STRIPPED_SPRUCE_WOOD);
        put(Biome.PLAINS, Material.GRASS_BLOCK);
        put(Biome.RIVER, Material.FISHING_ROD);
        put(Biome.SAVANNA, Material.ACACIA_PLANKS);
        put(Biome.SAVANNA_PLATEAU, Material.ACACIA_LEAVES);
        put(Biome.SMALL_END_ISLANDS, Material.END_STONE);
        put(Biome.SNOWY_BEACH, Material.SMOOTH_SANDSTONE);
        put(Biome.SNOWY_PLAINS, Material.SNOW_BLOCK);
        put(Biome.SNOWY_SLOPES, Material.SNOWBALL);
        put(Biome.SNOWY_TAIGA, Material.SPRUCE_LEAVES);
        put(Biome.SOUL_SAND_VALLEY, Material.SOUL_SAND);
        put(Biome.SPARSE_JUNGLE, Material.COCOA_BEANS);
        put(Biome.STONY_PEAKS, Material.CALCITE);
        put(Biome.STONY_SHORE, Material.STONE);
        put(Biome.SUNFLOWER_PLAINS, Material.SUNFLOWER);
        put(Biome.SWAMP, Material.LILY_PAD);
        put(Biome.TAIGA, Material.SPRUCE_PLANKS);
        put(Biome.THE_END, Material.OBSIDIAN);
        put(Biome.THE_VOID, Material.BARRIER);
        put(Biome.WARM_OCEAN, Material.BRAIN_CORAL_BLOCK);
        put(Biome.WARPED_FOREST, Material.WARPED_PLANKS);
        put(Biome.WINDSWEPT_FOREST, Material.OAK_LEAVES);
        put(Biome.WINDSWEPT_GRAVELLY_HILLS, Material.GRAVEL);
        put(Biome.WINDSWEPT_HILLS, Material.EMERALD_ORE);
        put(Biome.WINDSWEPT_SAVANNA, Material.ACACIA_LOG);
        put(Biome.WOODED_BADLANDS, Material.COARSE_DIRT);
    }};

    static {
        for (Biome biome : Biome.values()) {
            if (!END.contains(biome) && !NETHER.contains(biome) && biome != Biome.CUSTOM && biome != Biome.THE_VOID) {
                OVERWORLD.add(biome);
            }
        }
        OVERWORLD.sort(Comparator.comparing(Enum::toString));
    }
}
