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
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISBlasterBatteryRecipe {

    private final TARDIS plugin;
    private final HashMap<String, NamespacedKey> modelData = new HashMap<>();

    public TARDISBlasterBatteryRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BUCKET, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("Blaster Battery"));
        im.setItemModel(RecipeItem.BLASTER_BATTERY.getModel());
        im.addItemFlags(ItemFlag.values());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(TARDIS.plugin, "blaster_battery");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // set shape
        r.shape("-S-", "-R-", "-B-");
        r.setIngredient('S', Material.ORANGE_STAINED_GLASS);
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('B', Material.BUCKET);
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put("Blaster Battery", r);
    }
}
