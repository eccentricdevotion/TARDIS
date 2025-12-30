/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/*
easy_shape:R,G
easy_ingredients.R:REDSTONE
easy_ingredients.G:GOLD_NUGGET
hard_shape:C,G
hard_ingredients.C:COMPARATOR
hard_ingredients.G:GOLD_NUGGET
result:GOLD_NUGGET
amount:1
lore:Enter and exit your TARDIS
*/

public class TARDISKeyRecipe {

    private final TARDIS plugin;
    private final HashMap<String, List<Float>> cmdLookup = new HashMap<>();

    public TARDISKeyRecipe(TARDIS plugin) {
        this.plugin = plugin;
        cmdLookup.put("first", KeyVariant.BRASS_YALE.getFloats());
        cmdLookup.put("second", KeyVariant.BRASS_PLAIN.getFloats());
        cmdLookup.put("third", KeyVariant.SPADE_SHAPED.getFloats());
        cmdLookup.put("fifth", KeyVariant.SILVER_YALE.getFloats());
        cmdLookup.put("seventh", KeyVariant.SEAL_OF_RASSILON.getFloats());
        cmdLookup.put("ninth", KeyVariant.SILVER_VARIANT.getFloats());
        cmdLookup.put("tenth", KeyVariant.SILVER_PLAIN.getFloats());
        cmdLookup.put("eleventh", KeyVariant.SILVER_NEW.getFloats());
        cmdLookup.put("rose", KeyVariant.SILVER_ERA.getFloats());
        cmdLookup.put("sally", KeyVariant.SILVER_STRING.getFloats());
        cmdLookup.put("perception", KeyVariant.FILTER.getFloats());
        cmdLookup.put("susan", KeyVariant.BRASS_STRING.getFloats());
        cmdLookup.put("gold", KeyVariant.BROMLEY_GOLD.getFloats());
    }

    public void addRecipe() {
        List<Float> floats = cmdLookup.getOrDefault(plugin.getConfig().getString("preferences.default_key", "eleventh").toLowerCase(Locale.ROOT), KeyVariant.BRASS_YALE.getFloats());
        Material material;
        try {
            material = Material.valueOf(plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            material = Material.GOLD_NUGGET;
        }
        ItemStack is = ItemStack.of(material, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("TARDIS Key"));
        im.lore(List.of(Component.text("Enter and exit your TARDIS")));
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(floats);
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_key");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("C", "G");
            r.setIngredient('C', Material.COMPARATOR);
        } else {
            r.shape("R", "G");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('G', Material.GOLD_NUGGET);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Key", r);
    }
}
