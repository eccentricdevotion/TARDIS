package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BleachRecipe {

    private final TARDIS plugin;
    private final NamespacedKey bedKey;
    private final NamespacedKey woolKey;
    private final NamespacedKey carpetKey;
    private final NamespacedKey bannerKey;
    private final NamespacedKey powderKey;

    public BleachRecipe(TARDIS plugin) {
        this.plugin = plugin;
        bedKey = new NamespacedKey(this.plugin, "bleached_bed");
        woolKey = new NamespacedKey(this.plugin, "bleached_wool");
        carpetKey = new NamespacedKey(this.plugin, "bleached_carpet");
        bannerKey = new NamespacedKey(this.plugin, "bleached_banner");
        powderKey = new NamespacedKey(this.plugin, "bleached_powder");
    }

    // concrete, concrete powder
    public void setRecipes() {
        // bleach
        ItemStack bleach = new ItemStack(Material.WHITE_DYE, 1);
        ItemMeta bm = bleach.getItemMeta();
        bm.setDisplayName("Bleach");
        bm.setCustomModelData(10000001);
        bm.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
        bleach.setItemMeta(bm);
        // beds
        ItemStack bed = new ItemStack(Material.WHITE_BED, 1);
        ShapelessRecipe bedRecipe = new ShapelessRecipe(bedKey, bed);
        bedRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredBeds = new RecipeChoice.MaterialChoice(Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED, Material.RED_BED, Material.YELLOW_BED);
        bedRecipe.addIngredient(colouredBeds);
        plugin.getServer().addRecipe(bedRecipe);
        // wool
        ItemStack wool = new ItemStack(Material.WHITE_WOOL, 1);
        ShapelessRecipe woolRecipe = new ShapelessRecipe(woolKey, wool);
        woolRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredWool = new RecipeChoice.MaterialChoice(Material.BLACK_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.CYAN_WOOL, Material.GRAY_WOOL, Material.GREEN_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIGHT_GRAY_WOOL, Material.LIME_WOOL, Material.MAGENTA_WOOL, Material.ORANGE_WOOL, Material.PINK_WOOL, Material.PURPLE_WOOL, Material.RED_WOOL, Material.YELLOW_WOOL);
        woolRecipe.addIngredient(colouredWool);
        plugin.getServer().addRecipe(woolRecipe);
        // carpet
        ItemStack carpet = new ItemStack(Material.WHITE_CARPET, 1);
        ShapelessRecipe carpetRecipe = new ShapelessRecipe(carpetKey, carpet);
        carpetRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredCarpet = new RecipeChoice.MaterialChoice(Material.BLACK_CARPET, Material.BLUE_CARPET, Material.BROWN_CARPET, Material.CYAN_CARPET, Material.GRAY_CARPET, Material.GREEN_CARPET, Material.LIGHT_BLUE_CARPET, Material.LIGHT_GRAY_CARPET, Material.LIME_CARPET, Material.MAGENTA_CARPET, Material.ORANGE_CARPET, Material.PINK_CARPET, Material.PURPLE_CARPET, Material.RED_CARPET, Material.YELLOW_CARPET);
        carpetRecipe.addIngredient(colouredCarpet);
        plugin.getServer().addRecipe(carpetRecipe);
        // banners
        ItemStack banner = new ItemStack(Material.WHITE_BANNER, 1);
        ShapelessRecipe bannerRecipe = new ShapelessRecipe(bannerKey, banner);
        bannerRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredBanners = new RecipeChoice.MaterialChoice(Material.BLACK_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.CYAN_BANNER, Material.GRAY_BANNER, Material.GREEN_BANNER, Material.LIGHT_BLUE_BANNER, Material.LIGHT_GRAY_BANNER, Material.LIME_BANNER, Material.MAGENTA_BANNER, Material.ORANGE_BANNER, Material.PINK_BANNER, Material.PURPLE_BANNER, Material.RED_BANNER, Material.YELLOW_BANNER);
        bannerRecipe.addIngredient(colouredBanners);
        plugin.getServer().addRecipe(bannerRecipe);
        // concrete powder
        ItemStack powder = new ItemStack(Material.WHITE_CONCRETE_POWDER, 1);
        ShapelessRecipe powderRecipe = new ShapelessRecipe(powderKey, powder);
        powderRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredPowder = new RecipeChoice.MaterialChoice(Material.BLACK_CONCRETE_POWDER, Material.BLUE_CONCRETE_POWDER, Material.BROWN_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE_POWDER, Material.LIME_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER);
        powderRecipe.addIngredient(colouredPowder);
        plugin.getServer().addRecipe(powderRecipe);
    }
}
