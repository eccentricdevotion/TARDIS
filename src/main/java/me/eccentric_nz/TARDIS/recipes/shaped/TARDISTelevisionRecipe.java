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
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/*
easy_shape:GGG,GBG,GEG
easy_ingredients.G:GRAY_CONCRETE
easy_ingredients.B:BROWN_STAINED_GLASS_PANE
easy_ingredients.E:BUCKET:Artron Capacitor
hard_shape:GGG,GBG,GEC
hard_ingredients.G:GRAY_CONCRETE
hard_ingredients.B:BROWN_STAINED_GLASS_PANE
hard_ingredients.E:BUCKET:Artron Capacitor
hard_ingredients.C:GLOWSTONE_DUST=TARDIS Chameleon Circuit
result:BROWN_STAINED_GLASS
amount:1
*/

public class TARDISTelevisionRecipe {

    private final TARDIS plugin;

    public TARDISTelevisionRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.BROWN_STAINED_GLASS, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("TARDIS Television"));
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, RecipeItem.TARDIS_TELEVISION.getModel().getKey());
        is.setItemMeta(im);
        // exact choice
        ItemStack capac = ItemStack.of(Material.BUCKET, 1);
        ItemMeta itor = capac.getItemMeta();
        itor.displayName(ComponentUtils.toWhite("Artron Capacitor"));
        capac.setItemMeta(itor);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_television");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GGG", "GBG", "GEC");
            // exact choice
            ItemStack chameleon = ItemStack.of(Material.GLOWSTONE_DUST, 1);
            ItemMeta circuit = chameleon.getItemMeta();
            circuit.displayName(ComponentUtils.toWhite("TARDIS Chameleon Circuit"));
            CustomModelDataComponent ecomponent = circuit.getCustomModelDataComponent();
            ecomponent.setFloats(CircuitVariant.CHAMELEON.getFloats());
            circuit.setCustomModelDataComponent(ecomponent);
            // set the second line of lore
            Component uses = (plugin.getConfig().getString("circuits.uses.chameleon", "25").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? Component.text("unlimited", NamedTextColor.YELLOW)
                    : Component.text(plugin.getConfig().getString("circuits.uses.chameleon", "25"), NamedTextColor.YELLOW);
            circuit.lore(List.of(Component.text("Uses left"), uses));
            chameleon.setItemMeta(circuit);
            r.setIngredient('C', new RecipeChoice.ExactChoice(chameleon));
        } else {
            r.shape("GGG", "GBG", "GEG");
        }
        r.setIngredient('G', Material.GRAY_CONCRETE);
        r.setIngredient('B', Material.BROWN_STAINED_GLASS_PANE);
        r.setIngredient('E', new RecipeChoice.ExactChoice(capac));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Television", r);
    }
}
