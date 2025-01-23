package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
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
        im.setDisplayName(NamedTextColor.WHITE + "TARDIS Television");
        im.setItemModel(RecipeItem.TARDIS_TELEVISION.getModel());
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, RecipeItem.TARDIS_TELEVISION.getModel().getKey());
        is.setItemMeta(im);
        // exact choice
        ItemStack capac = new ItemStack(Material.BUCKET, 1);
        ItemMeta itor = capac.getItemMeta();
        itor.setDisplayName(NamedTextColor.WHITE + "Artron Capacitor");
        itor.setItemModel(RecipeItem.ARTRON_CAPACITOR.getModel());
        capac.setItemMeta(itor);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_television");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GGG", "GBG", "GEC");
            // exact choice
            ItemStack chameleon = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta circuit = chameleon.getItemMeta();
            circuit.setDisplayName(NamedTextColor.WHITE + "TARDIS Chameleon Circuit");
            circuit.setItemModel(RecipeItem.TARDIS_CHAMELEON_CIRCUIT.getModel());
            // set the second line of lore
            List<String> lore;
            String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? NamedTextColor.YELLOW + "unlimited"
                    : NamedTextColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
            lore = Arrays.asList("Uses left", uses);
            circuit.lore(lore);
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
