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
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.potion.PotionType;

import java.util.List;

/*
easy_shape:-D-,P-E,-W-
easy_ingredients.D:DIAMOND
easy_ingredients.P:GLOWSTONE_DUST=Perception Circuit
easy_ingredients.E:EMERALD
easy_ingredients.W:POTION>INVISIBILITY
hard_shape:-D-,P-E,-W-
hard_ingredients.D:DIAMOND
hard_ingredients.P:GLOWSTONE_DUST=Perception Circuit
hard_ingredients.E:EMERALD
hard_ingredients.W:POTION>INVISIBILITY
result:GLOWSTONE_DUST
amount:1
lore:Uses left~5
*/

public class TARDISInvisibilityCircuitRecipe {

    private final TARDIS plugin;

    public TARDISInvisibilityCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("TARDIS Invisibility Circuit"));
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.INVISIBILITY.getFloats());
        im.setCustomModelDataComponent(component);
        Component uses = (plugin.getConfig().getString("circuits.uses.invisibility", "5").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? Component.text("unlimited", NamedTextColor.YELLOW)
                : Component.text(plugin.getConfig().getString("circuits.uses.invisibility", "5"), NamedTextColor.YELLOW);
        im.lore(List.of(Component.text("Uses left"), uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_invisibility_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.displayName(ComponentUtils.toWhite("Perception Circuit"));
        CustomModelDataComponent ecomponent = em.getCustomModelDataComponent();
        ecomponent.setFloats(CircuitVariant.PERCEPTION.getFloats());
        em.setCustomModelDataComponent(ecomponent);
        exact.setItemMeta(em);
        ItemStack potion = ItemStack.of(Material.POTION, 1);
        PotionMeta pm = (PotionMeta) potion.getItemMeta();
        pm.setBasePotionType(PotionType.INVISIBILITY);
        potion.setItemMeta(pm);
        r.shape(" D ", "P E", " W ");
        r.setIngredient('D', Material.DIAMOND);
        r.setIngredient('P', new RecipeChoice.ExactChoice(exact));
        r.setIngredient('E', Material.EMERALD);
        r.setIngredient('W', new RecipeChoice.ExactChoice(potion));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Invisibility Circuit", r);
    }
}
