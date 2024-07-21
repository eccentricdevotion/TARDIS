package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;

/*
easy_shape:-Q-,-I-,-I-
easy_ingredients.Q:QUARTZ
easy_ingredients.I:IRON_INGOT
hard_shape:-Q-,-I-,-O-
hard_ingredients.Q:QUARTZ
hard_ingredients.I:IRON_INGOT
hard_ingredients.O:GLOWSTONE_DUST=Sonic Oscillator
result:BLAZE_ROD
amount:1
*/

public class SonicScrewdriverRecipe {

    private final TARDIS plugin;
    private final HashMap<String, Integer> sonicModelLookup = new HashMap<>();


    public SonicScrewdriverRecipe(TARDIS plugin) {
        this.plugin = plugin;
        sonicModelLookup.put("mark_1", 10000001);
        sonicModelLookup.put("mark_2", 10000002);
        sonicModelLookup.put("mark_3", 10000003);
        sonicModelLookup.put("mark_4", 10000004);
        sonicModelLookup.put("eighth", 10000008);
        sonicModelLookup.put("ninth", 10000009);
        sonicModelLookup.put("ninth_open", 12000009);
        sonicModelLookup.put("tenth", 10000010);
        sonicModelLookup.put("tenth_open", 12000010);
        sonicModelLookup.put("eleventh", 10000011);
        sonicModelLookup.put("eleventh_open", 12000011);
        sonicModelLookup.put("master", 10000032);
        sonicModelLookup.put("sarah_jane", 10000033);
        sonicModelLookup.put("river_song", 10000031);
        sonicModelLookup.put("twelfth", 10000012);
        sonicModelLookup.put("thirteenth", 10000013);
        sonicModelLookup.put("fourteenth", 10000014);
        sonicModelLookup.put("fourteenth_open", 12000014);
        sonicModelLookup.put("sonic_probe", 10000034);
        sonicModelLookup.put("umbrella", 10000035);
        sonicModelLookup.put("war", 10000085);
    }

    public void addRecipe() {
        int sonicModel = sonicModelLookup.getOrDefault(plugin.getConfig().getString("sonic.default_model").toLowerCase(Locale.ENGLISH), 10000011);
        ItemStack is = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Sonic Screwdriver");
        im.setCustomModelData(sonicModel);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_screwdriver");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("Q", "I", "O");
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName("Sonic Oscillator");
            em.setCustomModelData(RecipeItem.SONIC_OSCILLATOR.getCustomModelData());
            exact.setItemMeta(em);
            r.setIngredient('O', new RecipeChoice.ExactChoice(exact));
        } else {
            r.shape("Q", "I", "I");
        }
        r.setIngredient('Q', Material.QUARTZ);
        r.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Sonic Screwdriver", r);
    }
}
