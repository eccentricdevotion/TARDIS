/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

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
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Vortex Manipulator"));
        is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("Cheap and nasty time travel")).build());
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
