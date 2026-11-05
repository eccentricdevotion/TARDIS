package me.eccentric_nz.TARDIS.schematic;

import org.bukkit.Material;

import java.util.HashMap;

public class Gradient {

    private final HashMap<Material, Material[]> gradients = new HashMap<>();

    public void init() {
        gradients.put(Material.SMOOTH_SANDSTONE, smoothSandstone);
        gradients.put(Material.STRIPPED_PALE_OAK_WOOD, strippedPaleOakWood);
        gradients.put(Material.NETHERRACK, netherrack);
        gradients.put(Material.MOSSY_COBBLESTONE, mossyCobblestone);
        gradients.put(Material.WHITE_TERRACOTTA, whiteTerracotta);
        gradients.put(Material.QUARTZ_BLOCK, quartzBlock);
        gradients.put(Material.PRISMARINE, prismarine);
        gradients.put(Material.CINNABAR, cinnabar);
        gradients.put(Material.TUFF, tuff);
        gradients.put(Material.COBBLESTONE, cobblestone);
        gradients.put(Material.DEAD_HORN_CORAL_BLOCK, deadHornCoralBlock);
        gradients.put(Material.END_STONE, endStone);
    }

    private final Material[] tuff = new Material[]{
            Material.MUDDY_MANGROVE_ROOTS,
            Material.GREEN_TERRACOTTA,
            Material.MOSS_BLOCK,
            Material.MOSSY_COBBLESTONE,
            Material.COBBLESTONE
    };
    private final Material[] cobblestone = new Material[]{
            Material.WARPED_HYPHAE,
            Material.PURPLE_TERRACOTTA,
            Material.BLACKSTONE,
            Material.MUD,
            Material.COBBLED_DEEPSLATE
    };
    private final Material[] deadHornCoralBlock = new Material[]{
            Material.CRIMSON_HYPHAE,
            Material.NETHER_WART_BLOCK,
            Material.NETHERRACK,
            Material.CINNABAR,
            Material.DRIPSTONE_BLOCK
    };
    private final Material[] endStone = new Material[]{
            Material.CRYING_OBSIDIAN,
            Material.PURPLE_TERRACOTTA,
            Material.BROWN_MUSHROOM_BLOCK,
            Material.SULFUR,
            Material.POTENT_SULFUR
    };
    private final Material[] smoothSandstone = new Material[]{
            Material.CHISELED_RESIN_BRICKS,
            Material.ORANGE_TERRACOTTA,
            Material.SMOOTH_RED_SANDSTONE,
            Material.YELLOW_TERRACOTTA,
            Material.STRIPPED_JUNGLE_WOOD
    };
    private final Material[] strippedPaleOakWood = new Material[]{
            Material.CINNABAR,
            Material.GRANITE,
            Material.PACKED_MUD,
            Material.STRIPPED_POPLAR_WOOD,
            Material.WHITE_TERRACOTTA
    };
    private final Material[] netherrack = new Material[]{
            Material.PINK_GLAZED_TERRACOTTA,
            Material.PINK_WOOL,
            Material.PINK_CONCRETE,
            Material.STRIPPED_CRIMSON_HYPHAE,
            Material.PURPLE_TERRACOTTA
    };
    private final Material[] mossyCobblestone = new Material[]{
            Material.GREEN_GLAZED_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.GREEN_WOOL,
            Material.LIME_TERRACOTTA,
            Material.MOSS_BLOCK
    };
    private final Material[] whiteTerracotta = new Material[]{
            Material.RED_GLAZED_TERRACOTTA,
            Material.NETHER_WART_BLOCK,
            Material.STRIPPED_MANGROVE_WOOD,
            Material.CINNABAR,
            Material.GRANITE
    };
    private final Material[] quartzBlock = new Material[]{
            Material.GRAY_GLAZED_TERRACOTTA,
            Material.COBBLED_DEEPSLATE,
            Material.PALE_MOSS_BLOCK,
            Material.STRIPPED_POPLAR_WOOD,
            Material.MUSHROOM_STEM
    };
    private final Material[] prismarine = new Material[]{
            Material.CYAN_GLAZED_TERRACOTTA,
            Material.WARPED_WART_BLOCK,
            Material.CYAN_CONCRETE,
            Material.CYAN_WOOL,
            Material.STRIPPED_WARPED_HYPHAE
    };
    private final Material[] cinnabar = new Material[]{
            Material.BLACK_GLAZED_TERRACOTTA,
            Material.BLACK_TERRACOTTA,
            Material.CHERRY_WOOD,
            Material.SOUL_SAND,
            Material.DRIPSTONE_BLOCK
    };

    public HashMap<Material, Material[]> getGradients() {
        return gradients;
    }
}
