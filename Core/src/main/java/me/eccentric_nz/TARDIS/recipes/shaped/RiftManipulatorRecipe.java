package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-A-,ACA,RAR
easy_ingredients.A:NETHER_BRICK=Acid Battery
easy_ingredients.C:GLOWSTONE_DUST=Rift Circuit
easy_ingredients.R:REDSTONE
hard_shape:-A-,ACA,NAN
hard_ingredients.A:NETHER_BRICK=Acid Battery
hard_ingredients.C:GLOWSTONE_DUST=Rift Circuit
hard_ingredients.N:NETHER_STAR
result:BEACON
amount:1
*/

public class RiftManipulatorRecipe {

    private final TARDIS plugin;

    public RiftManipulatorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BEACON, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Rift Manipulator");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "rift_manipulator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack rift = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = rift.getItemMeta();
        em.setDisplayName("Rift Circuit");
        em.setCustomModelData(RecipeItem.RIFT_CIRCUIT.getCustomModelData());
        rift.setItemMeta(em);
        ItemStack acid = new ItemStack(Material.NETHER_BRICK, 1);
        ItemMeta aim = acid.getItemMeta();
        aim.setDisplayName("Acid Battery");
        aim.setCustomModelData(RecipeItem.ACID_BATTERY.getCustomModelData());
        acid.setItemMeta(aim);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" A ", "ACA", "NAN");
            r.setIngredient('N', Material.NETHER_STAR);
        } else {
            r.shape(" A ", "ACA", "RAR");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('A', new RecipeChoice.ExactChoice(acid));
        r.setIngredient('C', new RecipeChoice.ExactChoice(rift));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Rift Manipulator", r);
    }
}
