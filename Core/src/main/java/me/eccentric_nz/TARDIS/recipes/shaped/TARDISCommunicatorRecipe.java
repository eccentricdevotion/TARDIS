package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

/*
easy_shape:N--,IIH,--I
easy_ingredients.N:NOTE_BLOCK
easy_ingredients.I:IRON_INGOT
easy_ingredients.H:HOPPER
hard_shape:N--,IIH,--D
hard_ingredients.N:NOTE_BLOCK
hard_ingredients.I:IRON_INGOT
hard_ingredients.H:HOPPER
hard_ingredients.D:DIAMOND
result:LEATHER_HELMET
amount:1
*/

public class TARDISCommunicatorRecipe {

    private final TARDIS plugin;

    public TARDISCommunicatorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Communicator");
        im.setItemModel(RecipeItem.TARDIS_COMMUNICATOR.getModel());
        EquippableComponent equippable = im.getEquippable();
        equippable.setSlot(EquipmentSlot.HEAD);
        equippable.setDispensable(true);
        equippable.setCameraOverlay(Whoniverse.COMMUNICATOR_OVERLAY.getKey());
        im.setEquippable(equippable);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_communicator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("N  ", "IIH", "  D");
            r.setIngredient('D', Material.DIAMOND);
        } else {
            r.shape("N  ", "IIH", "  I");
        }
        r.setIngredient('N', Material.NOTE_BLOCK);
        r.setIngredient('I', Material.IRON_INGOT);
        r.setIngredient('H', Material.HOPPER);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Communicator", r);
    }
}
