/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TVMRecipe {

    private final TARDIS plugin;

    public TVMRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    /*
      shape: BBG,WOC,III
      ingredients:
        B: STONE_BUTTON
        G: GLASS
        W: CLOCK
        O: GOLD_INGOT
        C: COMPASS
        I: IRON_INGOT
      result: CLOCK
      amount: 1
      lore: "Cheap and nasty time travel"
     */
    public ShapedRecipe makeRecipe() {
        ItemStack is = new ItemStack(Material.CLOCK, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(NamedTextColor.WHITE + "Vortex Manipulator"));
        im.lore(List.of(Component.text("Cheap and nasty time travel")));
        im.setItemModel(RecipeItem.VORTEX_MANIPULATOR.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "vortex-manipulator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // set shape
        r.shape("BBG", "WOC", "III");
        r.setIngredient('B', Material.STONE_BUTTON);
        r.setIngredient('G', Material.GLASS);
        r.setIngredient('W', Material.CLOCK);
        r.setIngredient('O', Material.GOLD_INGOT);
        r.setIngredient('C', Material.COMPASS);
        r.setIngredient('I', Material.IRON_INGOT);
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put("Vortex Manipulator", r);
        // return the recipe
        return r;
    }
}
