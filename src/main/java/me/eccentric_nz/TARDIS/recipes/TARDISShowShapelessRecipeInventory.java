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
package me.eccentric_nz.TARDIS.recipes;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;
import java.util.Map;

public class TARDISShowShapelessRecipeInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final ShapelessRecipe recipe;
    private final Inventory inventory;
    private final String str;

    public TARDISShowShapelessRecipeInventory(TARDIS plugin, ShapelessRecipe recipe, String str) {
        this.plugin = plugin;
        this.recipe = recipe;
        this.str = str;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text(str + " recipe", NamedTextColor.DARK_RED));
        this.inventory.setContents(getRecipeItems());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getRecipeItems() {
        ItemStack[] stacks = new ItemStack[27];
        List<RecipeChoice> ingredients = recipe.getChoiceList();
        int glowstoneCount = 0;
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack item = null;
            RecipeChoice choice = ingredients.get(i);
            if (choice instanceof RecipeChoice.ExactChoice exact) {
                item = exact.getItemStack();
            } else if (choice instanceof RecipeChoice.MaterialChoice mat) {
                item = mat.getItemStack();
            }
            if (item == null) {
                continue;
            }
            ItemMeta im = item.getItemMeta();
            if (item.getType().equals(Material.GLOWSTONE_DUST)) {
                String dn = getDisplayName(str, glowstoneCount);
                im.displayName(ComponentUtils.toWhite(dn));
                glowstoneCount++;
            }
            if (item.getType().equals(Material.MUSIC_DISC_STRAD)) {
                im.displayName(Component.text("Blank Storage Disk"));
                im.addItemFlags(ItemFlag.values());
                im.setAttributeModifiers(Multimaps.forMap(Map.of()));
            }
            if (item.getType().equals(Material.BLAZE_ROD)) {
                im.displayName(Component.text("Sonic Screwdriver"));
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                component.setFloats(SonicVariant.TENTH.getFloats());
                im.setCustomModelDataComponent(component);
            }
            item.setItemMeta(im);
            stacks[i * 9] = item;
        }
        ItemStack result = recipe.getResult();
        ItemMeta im = result.getItemMeta();
        im.displayName(ComponentUtils.toWhite(str));
        if (str.equals("Blank Storage Disk") || str.equals("Save Storage Disk") || str.equals("Preset Storage Disk") || str.equals("Biome Storage Disk") || str.equals("Player Storage Disk") || str.equals("Authorised Control Disk")) {
            im.addItemFlags(ItemFlag.values());
            im.setAttributeModifiers(Multimaps.forMap(Map.of()));
        }
        RecipeItem recipeItem = RecipeItem.getByName(str);
        if (recipeItem != RecipeItem.NOT_FOUND) {
            if (recipeItem.getCategory().equals(RecipeCategory.SONIC_UPGRADES)) {
                im.displayName(ComponentUtils.toWhite("Sonic Screwdriver"));
                im.lore(List.of(Component.text("Upgrades:"), Component.text(str)));
            }
        }
        result.setAmount(1);
        result.setItemMeta(im);
        stacks[17] = result;
        return stacks;
    }

    private String getDisplayName(String recipe, int quartzCount) {
        switch (recipe) {
            case "TARDIS Locator" -> {
                return "TARDIS Locator Circuit"; // 1965
            }
            case "Stattenheim Remote" -> {
                return "TARDIS Stattenheim Circuit"; // 1963
            }
            case "TARDIS Chameleon Circuit", "TARDIS Remote Key" -> {
                return "TARDIS Materialisation Circuit"; // 1964
            }
            case "TARDIS Invisibility Circuit", "Perception Filter" -> {
                return "Perception Circuit"; // 1978
            }
            case "Sonic Screwdriver", "Server Admin Circuit", "Sonic Dock" -> {
                return "Sonic Oscillator"; // 1967
            }
            case "Fob Watch", "Preset Storage Disk", "TARDIS Television" -> {
                return "TARDIS Chameleon Circuit"; // 1966
            }
            case "TARDIS Biome Reader", "Emerald Upgrade" -> {
                return "Emerald Environment Circuit"; // 1972
            }
            case "Rift Manipulator" -> {
                return "Rift Circuit"; // 1983
            }
            case "Admin Upgrade" -> {
                return "Server Admin Circuit";
            }
            case "Bio-scanner Upgrade" -> {
                return "Bio-scanner Circuit";
            }
            case "Redstone Upgrade" -> {
                return "Redstone Activator Circuit";
            }
            case "Diamond Upgrade" -> {
                return "Diamond Disruptor Circuit";
            }
            case "Painter Upgrade" -> {
                return "Painter Circuit";
            }
            case "Ignite Upgrade" -> {
                return "Ignite Circuit";
            }
            case "Pickup Arrows Upgrade" -> {
                return "Pickup Arrows Circuit";
            }
            case "Knockback Upgrade" -> {
                return "Knockback Circuit";
            }
            case "Brush Upgrade" -> {
                return "Brush Circuit";
            }
            case "Conversion Upgrade" -> {
                return "Conversion Circuit";
            }
            default -> {  // TARDIS Stattenheim Circuit"
                if (quartzCount == 0) {
                    return "TARDIS Locator Circuit"; // 1965
                } else {
                    return "TARDIS Materialisation Circuit"; // 1964
                }
            }
        }
    }
}
