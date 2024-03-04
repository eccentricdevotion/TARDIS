package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
recipe:BONE,REDSTONE
result:BONE
amount:1
lore:Right-click start~Left-click end
*/

public class TARDISSchematicWandRecipe {

    private final TARDIS plugin;

    public TARDISSchematicWandRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BONE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Schematic Wand");
        im.setCustomModelData(10000001);
        im.setLore(List.of("Right-click start", "Left-click end"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_schematic_wand");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.BONE);
        r.addIngredient(Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("TARDIS Schematic Wand", r);
    }
}
