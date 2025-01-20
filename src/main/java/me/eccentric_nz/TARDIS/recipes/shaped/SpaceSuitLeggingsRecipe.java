package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

public class SpaceSuitLeggingsRecipe {

    private final TARDIS plugin;

    public SpaceSuitLeggingsRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "TARDIS Space Suit Leggings");
        im.setItemModel(RecipeItem.TARDIS_SPACE_SUIT_LEGGINGS.getModel());
        im.setMaxStackSize(1);
        EquippableComponent equippable = im.getEquippable();
        equippable.setSlot(EquipmentSlot.LEGS);
        equippable.setModel(Whoniverse.SPACE_SUIT.getKey());
        im.setEquippable(equippable);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "space_suit_leggings");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" H ", "YYY", "BGB");
        r.setIngredient('Y', Material.ORANGE_DYE);
        r.setIngredient('G', Material.BLACK_DYE);
        r.setIngredient('B', Material.ORANGE_WOOL);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            ItemStack exact = new ItemStack(Material.LEATHER_LEGGINGS, 1);
            LeatherArmorMeta am = (LeatherArmorMeta) exact.getItemMeta();
            Color black = Color.fromARGB(-14869215); // [argb0xFF1D1D21] not BLACK!
            am.setColor(black);
            exact.setItemMeta(am);
            r.setIngredient('H', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('H', Material.LEATHER_LEGGINGS);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Space Suit Leggings", r);
    }
}
