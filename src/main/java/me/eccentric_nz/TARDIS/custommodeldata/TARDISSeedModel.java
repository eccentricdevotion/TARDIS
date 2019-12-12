package me.eccentric_nz.TARDIS.custommodeldata;

import org.bukkit.Material;

import java.util.HashMap;

public class TARDISSeedModel {

    public static final HashMap<Material, Integer> materialMap = new HashMap<Material, Integer>() {
        {
            // ars
            put(Material.QUARTZ_BLOCK, 14);
            // bigger
            put(Material.GOLD_BLOCK, 15);
            // budget
            put(Material.IRON_BLOCK, 16);
            // coral
            put(Material.NETHER_WART_BLOCK, 17);
            // deluxe
            put(Material.DIAMOND_BLOCK, 18);
            // eleventh
            put(Material.EMERALD_BLOCK, 19);
            // ender
            put(Material.PURPUR_BLOCK, 20);
            // plank
            put(Material.BOOKSHELF, 21);
            // pyramid
            put(Material.SANDSTONE_STAIRS, 22);
            // redstone
            put(Material.REDSTONE_BLOCK, 23);
            // steampunk
            put(Material.COAL_BLOCK, 24);
            // thirteenth
            put(Material.ORANGE_CONCRETE, 25);
            // factory
            put(Material.YELLOW_CONCRETE_POWDER, 26);
            // tom
            put(Material.LAPIS_BLOCK, 27);
            // twelfth
            put(Material.PRISMARINE, 28);
            // war
            put(Material.WHITE_TERRACOTTA, 29);
            // legacy_bigger
            put(Material.ORANGE_GLAZED_TERRACOTTA, 33);
            // legacy_budget
            put(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, 34);
            // legacy_deluxe
            put(Material.LIME_GLAZED_TERRACOTTA, 35);
            // legacy_eleventh
            put(Material.CYAN_GLAZED_TERRACOTTA, 36);
            // legacy_redstone
            put(Material.RED_GLAZED_TERRACOTTA, 37);
            // master
            put(Material.NETHER_BRICKS, 39);
//            // custom
//            put(Material.OBSIDIAN, 45);
//            // templates
//            put(Material.COBBLESTONE, 41);
//            // archive
//            put(Material.BONE_BLOCK, 42);
        }
    };

    public static final HashMap<String, Integer> consoleMap = new HashMap<String, Integer>() {
        {
            put("ARS", 14);
            put("BIGGER", 15);
            put("BUDGET", 16);
            put("CORAL", 17);
            put("DELUXE", 18);
            put("ELEVENTH", 19);
            put("ENDER", 20);
            put("PLANK", 21);
            put("PYRAMID", 22);
            put("REDSTONE", 23);
            put("STEAMPUNK", 24);
            put("THIRTEENTH", 25);
            put("FACTORY", 26);
            put("TOM", 27);
            put("TWELFTH", 28);
            put("WAR", 29);
            put("LEGACY_BIGGER", 33);
            put("LEGACY_BUDGET", 34);
            put("LEGACY_DELUXE", 35);
            put("LEGACY_ELEVENTH", 36);
            put("LEGACY_REDSTONE", 37);
            put("MASTER", 39);
            put("CUSTOM", 26);
        }
    };

    public static int modelByMaterial(Material material) {
        return materialMap.get(material);
    }

    public static int modelByString(String console) {
        return consoleMap.get(console);
    }
}
