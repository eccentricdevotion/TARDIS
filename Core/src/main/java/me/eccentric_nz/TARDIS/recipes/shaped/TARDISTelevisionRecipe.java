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
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
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
        ItemStack is = new ItemStack(Material.BROWN_STAINED_GLASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Television");
        im.setItemModel(RecipeItem.TARDIS_TELEVISION.getModel());
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, RecipeItem.TARDIS_TELEVISION.getModel().getKey());
        is.setItemMeta(im);
        // exact choice
        ItemStack capac = new ItemStack(Material.BUCKET, 1);
        ItemMeta itor = capac.getItemMeta();
        itor.setDisplayName(ChatColor.WHITE + "Artron Capacitor");
        itor.setItemModel(RecipeItem.ARTRON_CAPACITOR.getModel());
        capac.setItemMeta(itor);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_television");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GGG", "GBG", "GEC");
            // exact choice
            ItemStack chameleon = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta circuit = chameleon.getItemMeta();
            circuit.setDisplayName(ChatColor.WHITE + "TARDIS Chameleon Circuit");
            circuit.setItemModel(RecipeItem.TARDIS_CHAMELEON_CIRCUIT.getModel());
            // set the second line of lore
            List<String> lore;
            String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? ChatColor.YELLOW + "unlimited"
                    : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
            lore = List.of("Uses left", uses);
            circuit.setLore(lore);
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
