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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TARDISShowShapedRecipeInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final ShapedRecipe recipe;
    private final Inventory inventory;
    private final String str;

    public TARDISShowShapedRecipeInventory(TARDIS plugin, ShapedRecipe recipe, String str) {
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
        String[] recipeShape = recipe.getShape();
        Map<Character, RecipeChoice> ingredientMap = recipe.getChoiceMap();
        int glowstoneCount = 0;
        for (int j = 0; j < recipeShape.length; j++) {
            for (int k = 0; k < recipeShape[j].length(); k++) {
                ItemStack item = null;
                RecipeChoice choice = ingredientMap.get(recipeShape[j].toCharArray()[k]);
                if (choice instanceof RecipeChoice.ExactChoice exact) {
                    item = exact.getItemStack();
                } else if (choice instanceof RecipeChoice.MaterialChoice mat) {
                    item = mat.getItemStack();
                }
                if (item == null) {
                    continue;
                }
                ItemMeta im = item.getItemMeta();
                if (item.getType().equals(Material.GLOWSTONE_DUST) && !str.endsWith("Tie")) {
                    String dn = getDisplayName(str, glowstoneCount);
                    im.displayName(ComponentUtils.toWhite(dn));
                    glowstoneCount++;
                }
                if (str.endsWith("TARDIS Remote Key")) {
                    Material material;
                    try {
                        material = Material.valueOf(plugin.getConfig().getString("preferences.key"));
                    } catch (IllegalArgumentException e) {
                        material = Material.GOLD_NUGGET;
                    }
                    if (item.getType().equals(material)) {
                        im.displayName(ComponentUtils.toWhite("TARDIS Key"));
                    }
                }
                if (str.equals("Acid Battery") && item.getType().equals(Material.WATER_BUCKET)) {
                    im.displayName(ComponentUtils.toWhite("Acid Bucket"));
                }
                if (str.equals("Rift Manipulator") && item.getType().equals(Material.NETHER_BRICK)) {
                    im.displayName(ComponentUtils.toWhite("Acid Battery"));
                }
                if (str.equals("Rust Plague Sword") && item.getType().equals(Material.LAVA_BUCKET)) {
                    im.displayName(ComponentUtils.toWhite("Rust Bucket"));
                }
                item.setItemMeta(im);
                stacks[j * 9 + k] = item;
            }
        }
        ItemStack result = recipe.getResult();
        ItemMeta im = result.getItemMeta();
        im.displayName(ComponentUtils.toWhite(str));
        if (str.equals("TARDIS Invisibility Circuit")) {
            // set the second line of lore
            List<Component> lore = im.lore();
            Component uses = (plugin.getConfig().getString("circuits.uses.invisibility", "5").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? Component.text("unlimited", NamedTextColor.YELLOW)
                    : Component.text(plugin.getConfig().getString("circuits.uses.invisibility", "5"), NamedTextColor.YELLOW);
            lore.set(1, uses);
            im.lore(lore);
        }
        if (str.equals("Blank Storage Disk") || str.equals("Save Storage Disk") || str.equals("Preset Storage Disk") || str.equals("Biome Storage Disk") || str.equals("Player Storage Disk") || str.equals("Authorised Control Disk")) {
            im.addItemFlags(ItemFlag.values());
            im.setAttributeModifiers(Multimaps.forMap(Map.of()));
        }
        if (str.startsWith("Door")) {
            String r = str.replace("Door ", "").toLowerCase(Locale.ROOT);
            if (r.equals("door")) {
                r = "tardis_door";
            }
            im.setItemModel(new NamespacedKey(plugin, r + "_closed"));
        }
        if (str.startsWith("Time Rotor")) {
            String r = str.replace("Time Rotor ", "").toLowerCase(Locale.ROOT);
            im.setItemModel(new NamespacedKey(plugin, "time_rotor_" + r + "_off"));
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
