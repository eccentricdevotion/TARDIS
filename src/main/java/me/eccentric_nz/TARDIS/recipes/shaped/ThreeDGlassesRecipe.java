package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

/*
easy_shape:---,P-P,CPM
easy_ingredients.P:PAPER
easy_ingredients.C:CYAN_STAINED_GLASS_PANE
easy_ingredients.M:MAGENTA_STAINED_GLASS_PANE
hard_shape:R-T,P-P,CPM
hard_ingredients.R:COMPARATOR
hard_ingredients.T:REDSTONE_TORCH
hard_ingredients.P:PAPER
hard_ingredients.C:CYAN_STAINED_GLASS_PANE
hard_ingredients.M:MAGENTA_STAINED_GLASS_PANE
result:LEATHER_HELMET
amount:1
*/

public class ThreeDGlassesRecipe {

    private final TARDIS plugin;

    public ThreeDGlassesRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "3-D Glasses");
        im.setItemModel(RecipeItem.THREE_D_GLASSES.getModel());
        EquippableComponent equippable = im.getEquippable();
        equippable.setCameraOverlay(Whoniverse.THREE_D_GLASSES_OVERLAY.getKey());
        equippable.setSlot(EquipmentSlot.HEAD);
        equippable.setDispensable(true);
        im.setEquippable(equippable);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "3-d_glasses");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("R T", "P P", "CPM");
            r.setIngredient('R', Material.COMPARATOR);
            r.setIngredient('T', Material.REDSTONE_TORCH);
        } else {
            r.shape("P P", "CPM");
        }
        r.setIngredient('P', Material.PAPER);
        r.setIngredient('C', Material.CYAN_STAINED_GLASS_PANE);
        r.setIngredient('M', Material.MAGENTA_STAINED_GLASS_PANE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("3-D Glasses", r);
    }
}
