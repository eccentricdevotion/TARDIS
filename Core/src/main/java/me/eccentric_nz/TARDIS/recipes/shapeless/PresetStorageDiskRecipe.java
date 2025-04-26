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
package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

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
        im.setDisplayName(ChatColor.WHITE + "Preset Storage Disk");
        im.setLore(List.of("Blank"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "preset_storage_disk");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.MUSIC_DISC_STRAD);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "TARDIS Chameleon Circuit");
        // set the second line of lore
        List<String> circuit;
        String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
        circuit = List.of("Uses left", uses);
        em.setLore(circuit);
        exact.setItemMeta(em);
        r.addIngredient(new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Preset Storage Disk", r);
    }
}
