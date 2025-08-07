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
package me.eccentric_nz.tardissonicblaster;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TARDISSonicBlasterRecipe {

    private final TARDIS plugin;

    public TARDISSonicBlasterRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addShapedRecipes() {
        // blaster battery
        ItemStack battery = ItemStack.of(Material.BUCKET, 1);
        ItemMeta batteryMeta = battery.getItemMeta();
        batteryMeta.displayName(ComponentUtils.toWhite("Blaster Battery"));
//        batteryMeta.setItemModel(RecipeItem.BLASTER_BATTERY.getModel());
        battery.setItemMeta(batteryMeta);
        NamespacedKey batteryKey = new NamespacedKey(plugin, "blaster_battery");
        ShapedRecipe bbr = new ShapedRecipe(batteryKey, battery);
        bbr.shape("-S-", "-R-", "-B-");
        bbr.setIngredient('S', Material.ORANGE_STAINED_GLASS);
        bbr.setIngredient('R', Material.REDSTONE);
        bbr.setIngredient('B', Material.BUCKET);
        plugin.getServer().addRecipe(bbr);
        plugin.getFigura().getShapedRecipes().put("Blaster Battery", bbr);

        // sonic blaster
        ItemStack blaster = ItemStack.of(Material.GOLDEN_HOE, 1);
        ItemMeta blasterMeta = blaster.getItemMeta();
        blasterMeta.displayName(ComponentUtils.toWhite("Sonic Blaster"));
        blasterMeta.lore(List.of(Component.text("The Squareness Gun")));
//        blasterMeta.setItemModel(RecipeItem.SONIC_BLASTER.getModel());
        blasterMeta.addItemFlags(ItemFlag.values());
        blasterMeta.setAttributeModifiers(Multimaps.forMap(Map.of()));
        blaster.setItemMeta(blasterMeta);
        NamespacedKey key = new NamespacedKey(TARDIS.plugin, "sonic_blaster");
        ShapedRecipe sbr = new ShapedRecipe(key, blaster);
        // set shape
        sbr.shape("DTD", "TST", "EBE");
        ItemStack exact = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.displayName(ComponentUtils.toWhite("Sonic Oscillator"));
        CustomModelDataComponent ecomponent = em.getCustomModelDataComponent();
        ecomponent.setFloats(CircuitVariant.SONIC.getFloats());
        em.setCustomModelDataComponent(ecomponent);
        exact.setItemMeta(em);
        sbr.setIngredient('D', Material.DISPENSER);
        sbr.setIngredient('T', Material.TNT);
        sbr.setIngredient('S', new RecipeChoice.ExactChoice(exact));
        sbr.setIngredient('E', new RecipeChoice.ExactChoice(battery));
        sbr.setIngredient('B', Material.OAK_BUTTON);
        plugin.getFigura().getShapedRecipes().put("Sonic Blaster", sbr);

        // landing pad
        ItemStack pad = ItemStack.of(Material.SLIME_BLOCK, 1);
        ItemMeta padMeta = pad.getItemMeta();
        padMeta.displayName(ComponentUtils.toWhite("Landing Pad"));
//        padMeta.setItemModel(RecipeItem.LANDING_PAD.getModel());
        pad.setItemMeta(padMeta);
        NamespacedKey padKey = new NamespacedKey(plugin, "landing_pad");
        ShapedRecipe lpr = new ShapedRecipe(padKey, pad);
        lpr.shape("-C-", "-S-", "-R-");
        lpr.setIngredient('C', Material.WHITE_CARPET);
        lpr.setIngredient('S', Material.SLIME_BLOCK);
        lpr.setIngredient('R', Material.REPEATER);
        plugin.getServer().addRecipe(lpr);
        plugin.getFigura().getShapedRecipes().put("Landing Pad", lpr);
    }
}
