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
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISSonicBlasterRecipe {

    private final TARDIS plugin;

    public TARDISSonicBlasterRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GOLDEN_HOE, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text("Sonic Blaster"));
        im.lore(List.of(Component.text("The Squareness Gun")));
        im.setItemModel(RecipeItem.SONIC_BLASTER.getModel());
        im.addItemFlags(ItemFlag.values());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(TARDIS.plugin, "sonic_blaster");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // set shape
        r.shape("DTD", "TST", "EBE");
        r.setIngredient('D', Material.DISPENSER);
        r.setIngredient('T', Material.TNT);
        ItemStack oscillator = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta om = oscillator.getItemMeta();
        om.displayName(Component.text("Sonic Oscillator"));
        om.setItemModel(RecipeItem.SONIC_OSCILLATOR.getModel());
        oscillator.setItemMeta(om);
        r.setIngredient('S', new RecipeChoice.ExactChoice(oscillator));
        ItemStack battery = new ItemStack(Material.BUCKET, 1);
        ItemMeta bm = battery.getItemMeta();
        bm.displayName(Component.text("Blaster Battery"));
        bm.setItemModel(Whoniverse.BLASTER_BATTERY.getKey());
        bm.addItemFlags(ItemFlag.values());
        battery.setItemMeta(bm);
        r.setIngredient('E', new RecipeChoice.ExactChoice(battery));
        r.setIngredient('B', Material.OAK_BUTTON);
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put("Sonic Blaster", r);
    }
}
