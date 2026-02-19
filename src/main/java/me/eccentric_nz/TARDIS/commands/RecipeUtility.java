package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.recipes.TARDISShowSeedRecipeInventory;
import me.eccentric_nz.TARDIS.recipes.TARDISShowShapedRecipeInventory;
import me.eccentric_nz.TARDIS.recipes.TARDISShowShapelessRecipeInventory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;
import java.util.Locale;

public class RecipeUtility {

    private static final HashMap<String, String> RECIPE_ITEMS = new HashMap<>() {
        {
            for (RecipeItem recipeItem : RecipeItem.values()) {
                if (recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE && recipeItem.getCategory() != RecipeCategory.UNUSED) {
                    put(recipeItem.toTabCompletionString(), recipeItem.toRecipeString());
                }
            }
            // custom time rotors
            for (String r : TARDIS.plugin.getCustomRotorsConfig().getKeys(false)) {
                put("time-rotor-" + r.toLowerCase(Locale.ROOT), "Time Rotor " + TARDISStringUtils.capitalise(r));
            }
            // custom doors
            for (String d : TARDIS.plugin.getCustomDoorsConfig().getKeys(false)) {
                put("door-" + d.toLowerCase(Locale.ROOT), "Door " + TARDISStringUtils.capitalise(d));
            }
            // custom consoles
            for (String c : TARDIS.plugin.getCustomConsolesConfig().getConfigurationSection("consoles").getKeys(false)) {
                put("console-" + c.toLowerCase(Locale.ROOT), TARDISStringUtils.capitalise(c) + " Console");
            }
            // remove recipes form modules that are not enabled
            if (!TARDIS.plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
                remove("vortex-manipulator");
            }
            if (!TARDIS.plugin.getConfig().getBoolean("modules.regeneration")) {
                remove("elixir-of-life");
            }
            if (!TARDIS.plugin.getConfig().getBoolean("modules.sonic_blaster")) {
                remove("sonic-blaster");
                remove("blaster-battery");
                remove("landing-pad");
            }
            if (!TARDIS.plugin.getConfig().getBoolean("modules.weeping_angels")) {
                remove("judoon-ammunition");
                remove("k9");
            }
        }
    };


    private static final HashMap<String, Material> TYPES = new HashMap<>() {
        {
            // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
            put("ANCIENT", Material.SCULK); // ancient
            put("ARS", Material.QUARTZ_BLOCK); // ARS
            put("BIGGER", Material.GOLD_BLOCK); // bigger
            put("BONE", Material.WAXED_OXIDIZED_CUT_COPPER); // bone loosely based on a console by DT10 - https://www.youtube.com/watch?v=Ux4qt0qYm80
            put("BUDGET", Material.IRON_BLOCK); // budget
            put("CAVE", Material.DRIPSTONE_BLOCK); // dripstone cave
            put("COPPER", Material.WARPED_PLANKS); // copper schematic designed by vistaero
            put("CORAL", Material.NETHER_WART_BLOCK); // coral schematic designed by vistaero
            put("CURSED", Material.BLACK_CONCRETE); // designed by airomis (player at thatsnotacreeper.com)
            put("DELTA", Material.CRYING_OBSIDIAN); // delta
            put("DELUXE", Material.DIAMOND_BLOCK); // deluxe
            put("DINER", Material.STRIPPED_CRIMSON_HYPHAE); // diner
            put("DIVISION", Material.PINK_GLAZED_TERRACOTTA); // division
            put("EIGHTH", Material.CHISELED_STONE_BRICKS); // eighth
            put("ELEVENTH", Material.EMERALD_BLOCK); // eleventh
            put("ENDER", Material.PURPUR_BLOCK); // ender schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
            put("FACTORY", Material.YELLOW_CONCRETE_POWDER); // factory schematic designed by Razihel
            put("FIFTEENTH", Material.OCHRE_FROGLIGHT); // designed by airomis (player at thatsnotacreeper.com)
            put("FUGITIVE", Material.POLISHED_DEEPSLATE); // fugitive - based on TARDIS designed by DT10 - https://www.youtube.com/watch?v=aykwXVemSs8
            put("HELL_BENT", Material.WHITE_GLAZED_TERRACOTTA); // hell bent
            put("HOSPITAL", Material.WHITE_CONCRETE); // hospital
            put("MASTER", Material.NETHER_BRICKS); // master schematic designed by ShadowAssociate
            put("MECHANICAL", Material.POLISHED_ANDESITE); // mechanical schematic adapted from design by Plastic Straw
            put("ORIGINAL", Material.PACKED_MUD); // original
            put("PLANK", Material.BOOKSHELF); // plank
            put("PYRAMID", Material.SANDSTONE_STAIRS); // pyramid schematic designed by airomis (player at thatsnotacreeper.com)
            put("REDSTONE", Material.REDSTONE_BLOCK); // redstone
            put("ROTOR", Material.HONEYCOMB_BLOCK); // rotor
            put("RUSTIC", Material.COPPER_BULB); // rustic
            put("SIDRAT", Material.GREEN_CONCRETE); // sidrat
            put("STEAMPUNK", Material.COAL_BLOCK); // steampunk
            put("THIRTEENTH", Material.ORANGE_CONCRETE); // thirteenth designed by Razihel
            put("TOM", Material.LAPIS_BLOCK); // tom baker
            put("TWELFTH", Material.PRISMARINE); // twelfth
            put("WAR", Material.WHITE_TERRACOTTA); // war doctor
            put("WEATHERED", Material.WEATHERED_COPPER); // weathered
            put("LEGACY_BIGGER", Material.ORANGE_GLAZED_TERRACOTTA);
            put("LEGACY_DELUXE", Material.LIME_GLAZED_TERRACOTTA);
            put("LEGACY_ELEVENTH", Material.CYAN_GLAZED_TERRACOTTA);
            put("LEGACY_REDSTONE", Material.RED_GLAZED_TERRACOTTA);
        }
    };

    public static void showTARDISRecipe(TARDIS plugin, Player player, String type) {
        player.closeInventory();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        player.openInventory(new TARDISShowSeedRecipeInventory(plugin, type, TYPES.get(type)).getInventory());
    }

    public static void showItemRecipe(TARDIS plugin, Player player, String item) {
        switch (item) {
            case "bowl-of-custard", "jelly-baby", "biome-storage-disk", "player-storage-disk",
                 "preset-storage-disk", "save-storage-disk", "schematic-wand", "admin-upgrade",
                 "bio-scanner-upgrade", "redstone-upgrade", "diamond-upgrade", "emerald-upgrade", "painter-upgrade",
                 "ignite-upgrade", "pickup-arrows-upgrade", "knockback-upgrade", "brush-upgrade",
                 "judoon-ammunition" -> showShapelessRecipe(plugin, player, RECIPE_ITEMS.get(item));
            default -> showShapedRecipe(plugin, player, RECIPE_ITEMS.get(item));
        }
    }

    private static void showShapedRecipe(TARDIS plugin, Player player, String str) {
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(str);
        if (recipe == null) {
            plugin.debug(str);
        } else {
            player.discoverRecipe(recipe.getKey());
            player.closeInventory();
            plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
            player.openInventory(new TARDISShowShapedRecipeInventory(plugin, recipe, str).getInventory());
        }
    }

    private static void showShapelessRecipe(TARDIS plugin, Player player, String str) {
        ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(str);
        player.discoverRecipe(recipe.getKey());
        player.closeInventory();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        player.openInventory(new TARDISShowShapelessRecipeInventory(plugin, recipe, str).getInventory());
    }
}
