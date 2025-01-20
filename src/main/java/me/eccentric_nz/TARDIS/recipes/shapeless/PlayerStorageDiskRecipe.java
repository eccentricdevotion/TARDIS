package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
recipe:MUSIC_DISC_STRAD,LAPIS_BLOCK
result:MUSIC_DISC_WAIT
amount:1
lore:Blank
*/

public class PlayerStorageDiskRecipe {

    private final TARDIS plugin;

    public PlayerStorageDiskRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MUSIC_DISC_WAIT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "Player Storage Disk");
        im.setItemModel(RecipeItem.PLAYER_STORAGE_DISK.getModel());
        im.setLore(List.of("Blank"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "player_storage_disk");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.MUSIC_DISC_STRAD);
        r.addIngredient(Material.LAPIS_BLOCK);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Player Storage Disk", r);
    }
}
