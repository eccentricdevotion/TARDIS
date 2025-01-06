package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;

/*
easy_shape:-A-,RER,GGG
easy_ingredients.A:AMETHYST_SHARD
easy_ingredients.R:REDSTONE
easy_ingredients.E:equipment.material
easy_ingredients.G:GLASS_PANE
hard_shape:-A-,RER,GGG
hard_ingredients.A:SPYGLASS
hard_ingredients.R:REDSTONE
hard_ingredients.E:equipment.material
hard_ingredients.G:GLASS
result:equipment.material
amount:1
*/

public class MicroscopeRecipes {

    private final TARDIS plugin;

    public MicroscopeRecipes(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (LabEquipment equipment : LabEquipment.values()) {
            String name = equipment.getName();
            ItemStack is = new ItemStack(equipment.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.WHITE + name);
            im.setItemModel(equipment.getModel());
            is.setItemMeta(im);
            NamespacedKey key = new NamespacedKey(plugin, equipment.toString().toLowerCase(Locale.ROOT));
            ShapedRecipe r = new ShapedRecipe(key, is);
            r.shape(" A ", "RER", "GGG");
            if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
                r.setIngredient('A', Material.SPYGLASS);
                r.setIngredient('G', Material.GLASS);
            } else {
                r.setIngredient('A', Material.AMETHYST_SHARD);
                r.setIngredient('G', Material.GLASS_PANE);
            }
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('E', equipment.getMaterial());
            plugin.getServer().addRecipe(r);
            plugin.getFigura().getShapedRecipes().put(name, r);
        }
    }
}
