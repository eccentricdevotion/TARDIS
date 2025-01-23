package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/*
recipe:MUSIC_DISC_STRAD,GLOWSTONE_DUST=TARDIS Chameleon Circuit
result:MUSIC_DISC_MALL
amount:1
lore:Blank
*/

public class PresetStorageDiskRecipe {

    private final TARDIS plugin;

    public PresetStorageDiskRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MUSIC_DISC_MALL, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "Preset Storage Disk");
        im.setItemModel(RecipeItem.PRESET_STORAGE_DISK.getModel());
        im.lore(List.of("Blank"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "preset_storage_disk");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.MUSIC_DISC_STRAD);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(NamedTextColor.WHITE + "TARDIS Chameleon Circuit");
        em.setItemModel(RecipeItem.TARDIS_CHAMELEON_CIRCUIT.getModel());
        // set the second line of lore
        List<String> circuit;
        String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? NamedTextColor.YELLOW + "unlimited"
                : NamedTextColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
        circuit = Arrays.asList("Uses left", uses);
        em.lore(circuit);
        exact.setItemMeta(em);
        r.addIngredient(new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Preset Storage Disk", r);
    }
}
