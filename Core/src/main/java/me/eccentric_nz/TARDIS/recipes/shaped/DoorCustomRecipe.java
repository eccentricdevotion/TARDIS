package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class DoorCustomRecipe {

    private final TARDIS plugin;

    public DoorCustomRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (String r : plugin.getCustomDoorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomDoorsConfig().getString(r + ".material"));
                ItemStack is = new ItemStack(material);
                ItemMeta im = is.getItemMeta();
                String dn = TARDISStringUtils.capitalise(r);
                im.setDisplayName(ChatColor.WHITE + "Door " + dn);
                im.setItemModel(new NamespacedKey(plugin, "block/tardis/" + r + "_0"));
                is.setItemMeta(im);
                NamespacedKey key = new NamespacedKey(plugin, "door_" + r);
                ShapedRecipe recipe = new ShapedRecipe(key, is);
                recipe.shape("#A#", "#D#", "###");
                recipe.setIngredient('#', plugin.getCraftingDifficulty() == CraftingDifficulty.HARD ? Material.GLASS : Material.GLASS_PANE);
                recipe.setIngredient('A', material);
                recipe.setIngredient('D', Material.IRON_DOOR);
                plugin.getServer().addRecipe(recipe);
                plugin.getFigura().getShapedRecipes().put("Door " + dn, recipe);
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom door item material for " + r + "!");
            }
        }
    }
}
