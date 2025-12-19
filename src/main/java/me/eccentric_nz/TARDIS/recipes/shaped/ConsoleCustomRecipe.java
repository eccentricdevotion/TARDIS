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
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/*
easy_shape:CBC,LWL,CBC
easy_ingredients.C:CONCRETE_POWDER
easy_ingredients.B:BAMBOO_BUTTON
easy_ingredients.L:LEVER
easy_ingredients.W:REDSTONE
hard_shape:CBC,ORO,CBC
hard_ingredients.C:CONCRETE_POWDER
hard_ingredients.B:BAMBOO_BUTTON
hard_ingredients.O:COMPARATOR
hard_ingredients.R:REDSTONE_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class ConsoleCustomRecipe {

    private final TARDIS plugin;
    private final List<Material> used = List.of(
            Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE, Material.LIGHT_BLUE_CONCRETE,
            Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.PINK_CONCRETE, Material.GRAY_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE, Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE, Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE, Material.BLACK_CONCRETE,
            Material.WAXED_OXIDIZED_COPPER
    );

    public ConsoleCustomRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (String console : plugin.getCustomConsolesConfig().getConfigurationSection("consoles").getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomConsolesConfig().getString("consoles." + console + ".material"));
                if (!used.contains(material)) {
                    ItemStack is = ItemStack.of(material, 1);
                    ItemMeta im = is.getItemMeta();
                    String dn = TARDISStringUtils.capitalise(console) + " Console";
                    im.displayName(ComponentUtils.toWhite(dn));
                    im.lore(List.of(Component.text("Integration with interaction")));
                    String pdc = "console_" + console;
                    im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, pdc);
                    is.setItemMeta(im);
                    NamespacedKey key = new NamespacedKey(plugin, console + "_console");
                    ShapedRecipe r = new ShapedRecipe(key, is);
                    r.shape("CBC", "ORO", "CBC");
                    r.setIngredient('C', material);
                    r.setIngredient('O', Material.COMPARATOR);
                    r.setIngredient('R', Material.REDSTONE_BLOCK);
                    r.setIngredient('B', Material.BAMBOO_BUTTON);
                    plugin.getServer().addRecipe(r);
                    plugin.getFigura().getShapedRecipes().put(dn, r);
                    ColourType.BY_MATERIAL.put(material, new NamespacedKey(plugin, pdc));
                } else {
                    plugin.debug("Custom console material for " + console + "is already in use!");
                }
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom console material for " + console + "!");
            }
        }
    }
}
