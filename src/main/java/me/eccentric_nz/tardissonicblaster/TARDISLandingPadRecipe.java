/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardissonicblaster;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISLandingPadRecipe {

    private final TARDIS plugin;

    public TARDISLandingPadRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.SLIME_BLOCK, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("Landing Pad"));
        im.addItemFlags(ItemFlag.values());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(TARDIS.plugin, "landing_pad");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // set shape
        r.shape("-C-", "-S-", "-R-");
        r.setIngredient('C', Material.WHITE_CARPET);
        r.setIngredient('S', Material.SLIME_BLOCK);
        r.setIngredient('R', Material.REPEATER);
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put("Landing Pad", r);
    }
}
