/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
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
        sonicModelLookup.put("mark_1", SonicVariant.MARK1.getKey());
        sonicModelLookup.put("mark_2", SonicVariant.MARK2.getKey());
        sonicModelLookup.put("mark_3", SonicVariant.MARK3.getKey());
        sonicModelLookup.put("mark_4", SonicVariant.MARK4.getKey());
        sonicModelLookup.put("eighth", SonicVariant.EIGHTH.getKey());
        sonicModelLookup.put("ninth", SonicVariant.NINTH.getKey());
        sonicModelLookup.put("ninth_open", SonicVariant.NINTH_OPEN.getKey());
        sonicModelLookup.put("tenth", SonicVariant.TENTH.getKey());
        sonicModelLookup.put("tenth_open", SonicVariant.TENTH_OPEN.getKey());
        sonicModelLookup.put("eleventh", SonicVariant.ELEVENTH.getKey());
        sonicModelLookup.put("eleventh_open", SonicVariant.ELEVENTH_OPEN.getKey());
        sonicModelLookup.put("master", SonicVariant.MASTER.getKey());
        sonicModelLookup.put("sarah_jane", SonicVariant.SARAH_JANE.getKey());
        sonicModelLookup.put("river_song", SonicVariant.RIVER_SONG.getKey());
        sonicModelLookup.put("twelfth", SonicVariant.TWELFTH.getKey());
        sonicModelLookup.put("thirteenth", SonicVariant.THIRTEENTH.getKey());
        sonicModelLookup.put("fourteenth", SonicVariant.FOURTEENTH.getKey());
        sonicModelLookup.put("fourteenth_open", SonicVariant.FOURTEENTH_OPEN.getKey());
        sonicModelLookup.put("fifteenth", SonicVariant.FIFTEENTH.getKey());
        sonicModelLookup.put("sonic_probe", SonicVariant.SONIC_PROBE.getKey());
        sonicModelLookup.put("umbrella", SonicVariant.UMBRELLA.getKey());
        sonicModelLookup.put("war", SonicVariant.WAR.getKey());
    }

    public void addRecipe() {
        NamespacedKey sonicModel = sonicModelLookup.getOrDefault(plugin.getConfig().getString("sonic.default_model").toLowerCase(Locale.ROOT), SonicVariant.ELEVENTH.getKey());
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
