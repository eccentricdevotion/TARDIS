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
    public static HashMap<String, Material> BIOME_BLOCKS = new HashMap<>() {{
//        put(Biome.CUSTOM.getKey().getKey(), Material.BARRIER);
        put("eye_of_harmony", Material.MAGMA_BLOCK);
        put("gallifrey_badlands", Material.TERRACOTTA);
        put("skaro_desert", Material.CYAN_TERRACOTTA);
        put(Biome.BADLANDS.getKey().getKey(), Material.RED_SAND);
        put(Biome.BAMBOO_JUNGLE.getKey().getKey(), Material.BAMBOO_BLOCK);
        put(Biome.BASALT_DELTAS.getKey().getKey(), Material.BASALT);
        put(Biome.BEACH.getKey().getKey(), Material.SAND);
        put(Biome.BIRCH_FOREST.getKey().getKey(), Material.BIRCH_PLANKS);
        put(Biome.CHERRY_GROVE.getKey().getKey(), Material.CHERRY_PLANKS);
        put(Biome.COLD_OCEAN.getKey().getKey(), Material.SALMON);
        put(Biome.CRIMSON_FOREST.getKey().getKey(), Material.CRIMSON_PLANKS);
        put(Biome.DARK_FOREST.getKey().getKey(), Material.DARK_OAK_PLANKS);
        put(Biome.DEEP_COLD_OCEAN.getKey().getKey(), Material.SEA_PICKLE);
        put(Biome.DEEP_DARK.getKey().getKey(), Material.SCULK);
        put(Biome.DEEP_FROZEN_OCEAN.getKey().getKey(), Material.SEAGRASS);
        put(Biome.DEEP_LUKEWARM_OCEAN.getKey().getKey(), Material.TUBE_CORAL_BLOCK);
        put(Biome.DEEP_OCEAN.getKey().getKey(), Material.PRISMARINE);
        put(Biome.DESERT.getKey().getKey(), Material.SANDSTONE);
        put(Biome.DRIPSTONE_CAVES.getKey().getKey(), Material.DRIPSTONE_BLOCK);
        put(Biome.END_BARRENS.getKey().getKey(), Material.END_STONE_BRICKS);
        put(Biome.END_HIGHLANDS.getKey().getKey(), Material.CHORUS_PLANT);
        put(Biome.END_MIDLANDS.getKey().getKey(), Material.PURPUR_BLOCK);
        put(Biome.ERODED_BADLANDS.getKey().getKey(), Material.YELLOW_TERRACOTTA);
        put(Biome.FLOWER_FOREST.getKey().getKey(), Material.ORANGE_TULIP);
        put(Biome.FOREST.getKey().getKey(), Material.OAK_PLANKS);
        put(Biome.FROZEN_OCEAN.getKey().getKey(), Material.BLUE_ICE);
        put(Biome.FROZEN_PEAKS.getKey().getKey(), Material.POWDER_SNOW_BUCKET);
        put(Biome.FROZEN_RIVER.getKey().getKey(), Material.ICE);
        put(Biome.GROVE.getKey().getKey(), Material.STRIPPED_SPRUCE_LOG);
        put(Biome.ICE_SPIKES.getKey().getKey(), Material.PACKED_ICE);
        put(Biome.JAGGED_PEAKS.getKey().getKey(), Material.GOAT_HORN);
        put(Biome.JUNGLE.getKey().getKey(), Material.JUNGLE_PLANKS);
        put(Biome.LUKEWARM_OCEAN.getKey().getKey(), Material.FIRE_CORAL_BLOCK);
        put(Biome.LUSH_CAVES.getKey().getKey(), Material.MOSS_BLOCK);
        put(Biome.MANGROVE_SWAMP.getKey().getKey(), Material.MANGROVE_PLANKS);
        put(Biome.MEADOW.getKey().getKey(), Material.RED_TULIP);
        put(Biome.MUSHROOM_FIELDS.getKey().getKey(), Material.MYCELIUM);
        put(Biome.NETHER_WASTES.getKey().getKey(), Material.NETHERRACK);
        put(Biome.OCEAN.getKey().getKey(), Material.KELP);
        put(Biome.OLD_GROWTH_BIRCH_FOREST.getKey().getKey(), Material.STRIPPED_BIRCH_WOOD);
        put(Biome.OLD_GROWTH_PINE_TAIGA.getKey().getKey(), Material.SPRUCE_LOG);
        put(Biome.OLD_GROWTH_SPRUCE_TAIGA.getKey().getKey(), Material.STRIPPED_SPRUCE_WOOD);
//        put(Biome.PALE_GARDEN.getKey().getKey(), Material.PALE_OAK_PLANKS); // 1.21.4
        put(Biome.PLAINS.getKey().getKey(), Material.GRASS_BLOCK);
        put(Biome.RIVER.getKey().getKey(), Material.FISHING_ROD);
        put(Biome.SAVANNA.getKey().getKey(), Material.ACACIA_PLANKS);
        put(Biome.SAVANNA_PLATEAU.getKey().getKey(), Material.ACACIA_LEAVES);
        put(Biome.SMALL_END_ISLANDS.getKey().getKey(), Material.END_STONE);
        put(Biome.SNOWY_BEACH.getKey().getKey(), Material.SMOOTH_SANDSTONE);
        put(Biome.SNOWY_PLAINS.getKey().getKey(), Material.SNOW_BLOCK);
        put(Biome.SNOWY_SLOPES.getKey().getKey(), Material.SNOWBALL);
        put(Biome.SNOWY_TAIGA.getKey().getKey(), Material.SPRUCE_LEAVES);
        put(Biome.SOUL_SAND_VALLEY.getKey().getKey(), Material.SOUL_SAND);
        put(Biome.SPARSE_JUNGLE.getKey().getKey(), Material.COCOA_BEANS);
        put(Biome.STONY_PEAKS.getKey().getKey(), Material.CALCITE);
        put(Biome.STONY_SHORE.getKey().getKey(), Material.STONE);
        put(Biome.SUNFLOWER_PLAINS.getKey().getKey(), Material.SUNFLOWER);
        put(Biome.SWAMP.getKey().getKey(), Material.LILY_PAD);
        put(Biome.TAIGA.getKey().getKey(), Material.SPRUCE_PLANKS);
        put(Biome.THE_END.getKey().getKey(), Material.OBSIDIAN);
        put(Biome.THE_VOID.getKey().getKey(), Material.BARRIER);
        put(Biome.WARM_OCEAN.getKey().getKey(), Material.BRAIN_CORAL_BLOCK);
        put(Biome.WARPED_FOREST.getKey().getKey(), Material.WARPED_PLANKS);
        put(Biome.WINDSWEPT_FOREST.getKey().getKey(), Material.OAK_LEAVES);
        put(Biome.WINDSWEPT_GRAVELLY_HILLS.getKey().getKey(), Material.GRAVEL);
        put(Biome.WINDSWEPT_HILLS.getKey().getKey(), Material.EMERALD_ORE);
        put(Biome.WINDSWEPT_SAVANNA.getKey().getKey(), Material.ACACIA_LOG);
        put(Biome.WOODED_BADLANDS.getKey().getKey(), Material.COARSE_DIRT);
    }};

    static {
        for (Biome biome : Biome.values()) {
            if (!END.contains(biome) && !NETHER.contains(biome) && biome != Biome.THE_VOID && biome != Biome.PALE_GARDEN) {
                OVERWORLD.add(biome);
            }
        }
        OVERWORLD.sort(Comparator.comparing(b -> b.getKey().getKey()));
    }
}
