/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMRecipe {

    private final TARDIS plugin;

    public TVMRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.CLOCK, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Vortex Manipulator"));
        im.lore(List.of(Component.text("Cheap and nasty time travel")));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "vortex-manipulator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // get shape
        r.shape("BBG", "WOC", "III");
        r.setIngredient('B', Material.STONE_BUTTON);
        r.setIngredient('G', Material.GLASS);
        r.setIngredient('W', Material.CLOCK);
        r.setIngredient('O', Material.GOLD_INGOT);
        r.setIngredient('C', Material.COMPASS);
        r.setIngredient('I', Material.IRON_INGOT);
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put("Vortex Manipulator", r);
        // add the recipe to the server
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Vortex Manipulator", r);
    }
}
