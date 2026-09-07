package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISTree;
import org.bukkit.Material;
import org.bukkit.block.Biome;

/**
 * A compact underground interpretation of a vanilla biome.
 * The biome itself remains vanilla; this only controls the materials and
 * small decorative details used inside the custom cavern.
 */
public enum BiomeStyle {

    BADLANDS,
    CAVE,
    CHERRY,
    COLD_OCEAN,
    DESERT,
    DRIPSTONE,
    FOREST,
    FROZEN,
    JUNGLE,
    LUSH,
    MUSHROOM,
    OCEAN,
    PALE,
    SAVANNA,
    SCULK,
    SNOWY,
    STONY,
    SULFUR,
    SWAMP,
    TAIGA,
    WARM_OCEAN,
    WINDSWEPT;

    public static BiomeProfile getProfile(BiomeStyle style) {
        return switch (style) {
            case BADLANDS -> new BiomeProfile(Material.TERRACOTTA, Material.RED_SAND, Material.ORANGE_TERRACOTTA, Material.RED_TERRACOTTA, Material.DEAD_BUSH, Material.TERRACOTTA, Material.VERDANT_FROGLIGHT, TARDISTree.ACACIA);
            case CHERRY -> new BiomeProfile(Material.CALCITE, Material.PINK_TERRACOTTA, Material.TUFF, Material.CHERRY_LOG, Material.PINK_PETALS, Material.MOSS_CARPET, Material.OCHRE_FROGLIGHT, TARDISTree.CHERRY);
            case COLD_OCEAN -> new BiomeProfile(Material.SNOW_BLOCK, Material.GRAVEL, Material.SAND, Material.POWDER_SNOW, Material.DRIED_KELP_BLOCK, Material.SEAGRASS, Material.VERDANT_FROGLIGHT, TARDISTree.PALE);
            case DESERT -> new BiomeProfile(Material.SANDSTONE, Material.SAND, Material.SMOOTH_SANDSTONE, Material.CUT_SANDSTONE, Material.DEAD_BUSH, Material.SANDSTONE, Material.PEARLESCENT_FROGLIGHT, TARDISTree.DESERT);
            case DRIPSTONE -> new BiomeProfile(Material.DRIPSTONE_BLOCK, Material.DIORITE, Material.ANDESITE, Material.GRANITE, Material.POINTED_DRIPSTONE, Material.POINTED_DRIPSTONE, Material.GLOWSTONE, TARDISTree.DARK_OAK);
            case FOREST -> new BiomeProfile(Material.STONE, Material.ROOTED_DIRT, Material.ANDESITE, Material.MOSSY_COBBLESTONE, Material.MOSS_CARPET, Material.FERN, Material.SHROOMLIGHT, TARDISTree.FOREST);
            case FROZEN -> new BiomeProfile(Material.STONE, Material.PACKED_ICE, Material.BLUE_ICE, Material.ICE, Material.SNOW, Material.SNOW_BLOCK, Material.SEA_LANTERN, TARDISTree.FROZEN);
            case JUNGLE -> new BiomeProfile(Material.MOSSY_COBBLESTONE, Material.MOSS_BLOCK, Material.MUD, Material.JUNGLE_LOG, Material.FERN, Material.MOSS_CARPET, Material.OCHRE_FROGLIGHT, TARDISTree.JUNGLE);
            case LUSH -> new BiomeProfile(Material.STONE, Material.MOSS_BLOCK, Material.TUFF, Material.MOSSY_COBBLESTONE, Material.MOSS_CARPET, Material.FERN, Material.GLOWSTONE, TARDISTree.ACACIA);
            case MUSHROOM -> new BiomeProfile(Material.MYCELIUM, Material.MYCELIUM, Material.TUFF, Material.MUSHROOM_STEM, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.SHROOMLIGHT, TARDISTree.MUSHROOM);
            case OCEAN -> new BiomeProfile(Material.PRISMARINE, Material.GRAVEL, Material.DARK_PRISMARINE, Material.SEA_LANTERN, Material.SEAGRASS, Material.PRISMARINE, Material.SEA_LANTERN, TARDISTree.OCEAN);
            case PALE -> new BiomeProfile(Material.DIORITE, Material.PALE_MOSS_BLOCK, Material.ANDESITE, Material.MANGROVE_ROOTS, Material.CLOSED_EYEBLOSSOM, Material.OPEN_EYEBLOSSOM, Material.PEARLESCENT_FROGLIGHT, TARDISTree.PALE);
            case SAVANNA -> new BiomeProfile(Material.DIRT, Material.GRASS_BLOCK, Material.TUFF, Material.STRIPPED_ACACIA_LOG, Material.SHORT_GRASS, Material.GLOW_LICHEN, Material.SHROOMLIGHT, TARDISTree.ACACIA);
            case SCULK -> new BiomeProfile(Material.DEEPSLATE, Material.SCULK, Material.TUFF, Material.SCULK_CATALYST, Material.SCULK_VEIN, Material.GLOW_LICHEN, Material.SHROOMLIGHT, TARDISTree.SCULK);
            case SNOWY -> new BiomeProfile(Material.STONE, Material.SNOW_BLOCK, Material.DIORITE, Material.BLACKSTONE, Material.PACKED_ICE, Material.ICE, Material.PEARLESCENT_FROGLIGHT, TARDISTree.FROZEN);
            case STONY -> new BiomeProfile(Material.GRANITE, Material.COARSE_DIRT, Material.DIORITE, Material.PALE_MOSS_BLOCK, Material.LILY_OF_THE_VALLEY, Material.SHORT_DRY_GRASS, Material.OCHRE_FROGLIGHT, TARDISTree.FOREST);
            case SULFUR -> new BiomeProfile(Material.SULFUR, Material.CINNABAR, Material.SULFUR, Material.DEEPSLATE, Material.SULFUR_SPIKE, Material.POTENT_SULFUR, Material.VERDANT_FROGLIGHT, TARDISTree.PALE);
            case SWAMP -> new BiomeProfile(Material.MUD, Material.MUDDY_MANGROVE_ROOTS, Material.CLAY, Material.ROOTED_DIRT, Material.MOSS_CARPET, Material.HANGING_ROOTS, Material.VERDANT_FROGLIGHT, TARDISTree.SWAMP);
            case TAIGA -> new BiomeProfile(Material.STONE, Material.PODZOL, Material.TUFF, Material.SPRUCE_LOG, Material.FERN, Material.MOSS_CARPET, Material.JACK_O_LANTERN, TARDISTree.TAIGA);
            case WINDSWEPT -> new BiomeProfile(Material.SULFUR, Material.CINNABAR, Material.SULFUR, Material.DEEPSLATE, Material.SULFUR_SPIKE, Material.POTENT_SULFUR, Material.PEARLESCENT_FROGLIGHT, TARDISTree.DARK_OAK);
            case WARM_OCEAN -> new BiomeProfile(Material.DEAD_BRAIN_CORAL_BLOCK, Material.DEAD_BUBBLE_CORAL_BLOCK, Material.DEAD_HORN_CORAL_BLOCK, Material.DEAD_FIRE_CORAL_BLOCK, Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_HORN_CORAL_FAN, Material.SEA_LANTERN, TARDISTree.OCEAN);
            // CAVE
            default -> new BiomeProfile(Material.DEEPSLATE, Material.COBBLED_DEEPSLATE, Material.DEEPSLATE, Material.TUFF, Material.GLOW_LICHEN, Material.SCULK_VEIN, Material.GLOWSTONE, TARDISTree.CAVE);
        };
    }

    public static BiomeStyle fromBiome(Biome biome) {
        if (biome == null) {
            return BiomeStyle.CAVE;
        }
        String key = biome.getKey().getKey();
        switch (key) {
            case "badlands", "eroded_badlands", "wooded_badlands" -> {
                return BiomeStyle.BADLANDS;
            }
            case "bamboo_jungle", "jungle", "sparse_jungle" -> {
                return BiomeStyle.JUNGLE;
            }
            case "beach", "deep_ocean", "ocean", "river" -> {
                return BiomeStyle.OCEAN;
            }
            case "cold_ocean", "deep_cold_ocean", "deep_frozen_ocean", "frozen_ocean" -> {
                return BiomeStyle.COLD_OCEAN;
            }
            case "deep_lukewarm_ocean", "lukewarm_ocean", "warm_ocean" -> {
                return BiomeStyle.WARM_OCEAN;
            }
            case "birch_forest", "dark_forest", "flower_forest", "forest", "old_growth_birch_forest" -> {
                return BiomeStyle.FOREST;
            }
            case "cherry_grove" -> {
                return BiomeStyle.CHERRY;
            }
            case "deep_dark" -> {
                return BiomeStyle.SCULK;
            }
            case "desert" -> {
                return BiomeStyle.DESERT;
            }
            case "dripstone_caves" -> {
                return BiomeStyle.DRIPSTONE;
            }
            case "frozen_peaks", "frozen_river", "ice_spikes" -> {
                return BiomeStyle.FROZEN;
            }
            case "grove", "old_growth_pine_taiga", "old_growth_spruce_taiga", "snowy_taiga", "taiga" -> {
                return BiomeStyle.TAIGA;
            }
            case "lush_caves", "plains", "meadow", "sunflower_plains" -> {
                return BiomeStyle.LUSH;
            }
            case "mangrove_swamp", "swamp" -> {
                return BiomeStyle.SWAMP;
            }
            case "mushroom_fields" -> {
                return BiomeStyle.MUSHROOM;
            }
            case "pale_garden" -> {
                return BiomeStyle.PALE;
            }
            case "savanna", "savanna_plateau", "windswept_savanna" -> {
                return BiomeStyle.SAVANNA;
            }
            case "snowy_beach", "snowy_plains", "snowy_slopes", "jagged_peaks" -> {
                return BiomeStyle.SNOWY;
            }
            case "stony_peaks", "stony_shore" -> {
                return BiomeStyle.STONY;
            }
            case "sulfur_caves" -> {
                return BiomeStyle.SULFUR;
            }
            case "windswept_forest", "windswept_gravelly_hills", "windswept_hills" -> {
                return BiomeStyle.WINDSWEPT;
            }
            default ->  {
                return BiomeStyle.CAVE;
            }
        }
    }
}
