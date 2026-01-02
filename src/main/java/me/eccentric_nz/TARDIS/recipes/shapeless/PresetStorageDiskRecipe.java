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
package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

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
        ItemStack is = ItemStack.of(Material.MUSIC_DISC_MALL, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Preset Storage Disk"));
        im.lore(List.of(Component.text("Blank")));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "preset_storage_disk");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.MUSIC_DISC_STRAD);
        ItemStack exact = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.displayName(ComponentUtils.toWhite("TARDIS Chameleon Circuit"));
        CustomModelDataComponent ecomponent = em.getCustomModelDataComponent();
        ecomponent.setFloats(CircuitVariant.CHAMELEON.getFloats());
        em.setCustomModelDataComponent(ecomponent);
        // set the second line of lore
        List<Component> circuit;
        Component uses = (plugin.getConfig().getString("circuits.uses.chameleon", "25").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? Component.text("unlimited", NamedTextColor.YELLOW)
                : Component.text(plugin.getConfig().getString("circuits.uses.chameleon", "25"), NamedTextColor.YELLOW);
        circuit = List.of(Component.text("Uses left"), uses);
        em.lore(circuit);
        exact.setItemMeta(em);
        r.addIngredient(new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Preset Storage Disk", r);
    }
}
