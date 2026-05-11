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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
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
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Television"));
        is.editPersistentDataContainer(pdc -> pdc.set(plugin.getCustomBlockKey(), PersistentDataType.STRING, RecipeItem.TARDIS_TELEVISION.getModel().getKey()));
        // exact choice
        ItemStack capacitor = ItemStack.of(Material.BUCKET, 1);
        capacitor.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Artron Capacitor"));
        NamespacedKey key = new NamespacedKey(plugin, "tardis_television");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GGG", "GBG", "GEC");
            // exact choice
            ItemStack chameleon = ItemStack.of(Material.GLOWSTONE_DUST, 1);
            chameleon.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Chameleon Circuit"));
            chameleon.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addFloats(CircuitVariant.CHAMELEON.getFloats())
                    .build());
            // set the second line of lore
            Component uses = (plugin.getConfig().getString("circuits.uses.chameleon", "25").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? Component.text("unlimited", NamedTextColor.YELLOW)
                    : Component.text(plugin.getConfig().getString("circuits.uses.chameleon", "25"), NamedTextColor.YELLOW);
            chameleon.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Component.text("Uses left"),
                    uses
            )));
            r.setIngredient('C', new RecipeChoice.ExactChoice(chameleon));
        } else {
            r.shape("GGG", "GBG", "GEG");
        }
        r.setIngredient('G', Material.GRAY_CONCRETE);
        r.setIngredient('B', Material.BROWN_STAINED_GLASS_PANE);
        r.setIngredient('E', new RecipeChoice.ExactChoice(capacitor));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Television", r);
    }
}
