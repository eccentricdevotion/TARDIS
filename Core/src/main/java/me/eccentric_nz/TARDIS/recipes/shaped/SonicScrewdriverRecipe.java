package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.BlazeRod;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
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

    public static final HashMap<String, NamespacedKey> sonicModelLookup = new HashMap<>();
    private final TARDIS plugin;


    public SonicScrewdriverRecipe(TARDIS plugin) {
        this.plugin = plugin;
        sonicModelLookup.put("mark_1", BlazeRod.MARK1.getKey());
        sonicModelLookup.put("mark_2", BlazeRod.MARK2.getKey());
        sonicModelLookup.put("mark_3", BlazeRod.MARK3.getKey());
        sonicModelLookup.put("mark_4", BlazeRod.MARK4.getKey());
        sonicModelLookup.put("eighth", BlazeRod.EIGHTH.getKey());
        sonicModelLookup.put("ninth", BlazeRod.NINTH.getKey());
        sonicModelLookup.put("ninth_open", BlazeRod.NINTH_OPEN.getKey());
        sonicModelLookup.put("tenth", BlazeRod.TENTH.getKey());
        sonicModelLookup.put("tenth_open", BlazeRod.TENTH_OPEN.getKey());
        sonicModelLookup.put("eleventh", BlazeRod.ELEVENTH.getKey());
        sonicModelLookup.put("eleventh_open", BlazeRod.ELEVENTH_OPEN.getKey());
        sonicModelLookup.put("master", BlazeRod.MASTER.getKey());
        sonicModelLookup.put("sarah_jane", BlazeRod.SARAH_JANE.getKey());
        sonicModelLookup.put("river_song", BlazeRod.RIVER_SONG.getKey());
        sonicModelLookup.put("twelfth", BlazeRod.TWELFTH.getKey());
        sonicModelLookup.put("thirteenth", BlazeRod.THIRTEENTH.getKey());
        sonicModelLookup.put("fourteenth", BlazeRod.FOURTEENTH.getKey());
        sonicModelLookup.put("fourteenth_open", BlazeRod.FOURTEENTH_OPEN.getKey());
        sonicModelLookup.put("fifteenth", BlazeRod.FIFTEENTH.getKey());
        sonicModelLookup.put("sonic_probe", BlazeRod.SONIC_PROBE.getKey());
        sonicModelLookup.put("umbrella", BlazeRod.UMBRELLA.getKey());
        sonicModelLookup.put("war", BlazeRod.WAR.getKey());
    }

    public void addRecipe() {
        NamespacedKey sonicModel = sonicModelLookup.getOrDefault(plugin.getConfig().getString("sonic.default_model").toLowerCase(Locale.ROOT), BlazeRod.ELEVENTH.getKey());
        ItemStack is = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Sonic Screwdriver");
        im.setItemModel(sonicModel);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_screwdriver");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("Q", "I", "O");
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(ChatColor.WHITE + "Sonic Oscillator");
            em.setItemModel(RecipeItem.SONIC_OSCILLATOR.getModel());
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
