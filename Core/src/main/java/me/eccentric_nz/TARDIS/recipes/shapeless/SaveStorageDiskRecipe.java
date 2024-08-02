package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
recipe:MUSIC_DISC_STRAD,REDSTONE
result:MUSIC_DISC_CHIRP
amount:1
lore:Blank
*/

public class SaveStorageDiskRecipe {

    private final TARDIS plugin;

    public SaveStorageDiskRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MUSIC_DISC_CHIRP, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Save Storage Disk");
        im.setCustomModelData(10000001);
        im.setLore(List.of("Blank"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "save_storage_disk");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.MUSIC_DISC_STRAD);
        r.addIngredient(Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Save Storage Disk", r);
    }
}
