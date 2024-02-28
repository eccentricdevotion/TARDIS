package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
recipe:MUSIC_DISC_STRAD,EMERALD
result:MUSIC_DISC_CAT
amount:1
lore:Blank
*/

public class BiomeStorageDiskRecipe {

    private final TARDIS plugin;

    public BiomeStorageDiskRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MUSIC_DISC_CAT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Biome Storage Disk");
        im.setCustomModelData(10000001);
        im.setLore(List.of("Blank"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "biome_storage_disk");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.MUSIC_DISC_STRAD);
        r.addIngredient(Material.EMERALD);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Biome Storage Disk", r);
    }
}
