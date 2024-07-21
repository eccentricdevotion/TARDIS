package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

/*
easy_shape:GGG,GBG,GEG
easy_ingredients.G:GRAY_CONCRETE
easy_ingredients.B:BROWN_STAINED_GLASS_PANE
easy_ingredients.E:BUCKET:Artron Capacitor
hard_shape:GGG,GBG,GEC
hard_ingredients.G:GRAY_CONCRETE
hard_ingredients.B:BROWN_STAINED_GLASS_PANE
hard_ingredients.E:BUCKET:Artron Capacitor
hard_ingredients.C:GLOWSTONE_DUST=TARDIS Chameleon Circuit
result:BROWN_STAINED_GLASS
amount:1
*/

public class TARDISTelevisionRecipe {

    private final TARDIS plugin;

    public TARDISTelevisionRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BROWN_STAINED_GLASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Television");
        im.setCustomModelData(1);
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
        is.setItemMeta(im);
        // exact choice
        ItemStack capac = new ItemStack(Material.BUCKET, 1);
        ItemMeta itor = capac.getItemMeta();
        itor.setDisplayName("Artron Capacitor");
        itor.setCustomModelData(10000003);
        capac.setItemMeta(itor);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_television");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GGG", "GBG", "GEC");
            // exact choice
            ItemStack chameleon = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta circuit = chameleon.getItemMeta();
            circuit.setDisplayName("TARDIS Chameleon Circuit");
            circuit.setCustomModelData(RecipeItem.TARDIS_CHAMELEON_CIRCUIT.getCustomModelData());
            // set the second line of lore
            List<String> lore;
            String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? ChatColor.YELLOW + "unlimited"
                    : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
            lore = Arrays.asList("Uses left", uses);
            circuit.setLore(lore);
            chameleon.setItemMeta(circuit);
            r.setIngredient('C', new RecipeChoice.ExactChoice(chameleon));
        } else {
            r.shape("GGG", "GBG", "GEG");
        }
        r.setIngredient('G', Material.GRAY_CONCRETE);
        r.setIngredient('B', Material.BROWN_STAINED_GLASS_PANE);
        r.setIngredient('E', new RecipeChoice.ExactChoice(capac));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Television", r);
    }
}
