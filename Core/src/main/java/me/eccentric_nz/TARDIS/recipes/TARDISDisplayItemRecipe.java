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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author eccentric_nz
 */
public class TARDISDisplayItemRecipe {

    private final TARDIS plugin;

    public TARDISDisplayItemRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addDisplayItemRecipes() {
        for (TARDISDisplayItem tdi : TARDISDisplayItem.values()) {
            if (tdi.getCraftMaterial() != null) {
                ItemStack is;
                if (tdi == TARDISDisplayItem.CLASSIC_DOOR || tdi == TARDISDisplayItem.BONE_DOOR) {
                    is = new ItemStack(tdi.getCraftMaterial(), 1);
                } else {
                    is = new ItemStack(tdi.getMaterial(), 1);
                }
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.WHITE + tdi.getDisplayName());
                if (tdi.getCustomModel() != null) {
                    im.setItemModel(tdi.getCustomModel());
                    im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
                }
                is.setItemMeta(im);
                NamespacedKey key = new NamespacedKey(plugin, tdi.getName());
                ShapedRecipe r = new ShapedRecipe(key, is);
                r.shape("#A#", "#D#", "###");
                r.setIngredient('#', plugin.getCraftingDifficulty() == CraftingDifficulty.HARD ? Material.GLASS : Material.GLASS_PANE);
                r.setIngredient('A', tdi.getCraftMaterial());
                r.setIngredient('D', tdi.getMaterial());
                plugin.getServer().addRecipe(r);
                plugin.getFigura().getShapedRecipes().put(tdi.getDisplayName(), r);
            }
        }
    }
}
