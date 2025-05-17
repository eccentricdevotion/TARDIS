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
package me.eccentric_nz.tardisshop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;

public class TARDISShopItemSpawner {

    private final TARDIS plugin;

    public TARDISShopItemSpawner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setItem(Location location, TARDISShopItem what) {
        String toEnum = TARDISStringUtils.toEnumUppercase(what.item());
        try {
            ShopItem si = ShopItem.valueOf(toEnum);
            ItemStack is = new ItemStack(si.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            if (si.getRecipeType() == ShopItemRecipe.MODELLED || si.getRecipeType() == ShopItemRecipe.TWA) {
                im.setItemModel(si.getModel());
            }
            if (si.getFloats() != null) {
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                component.setFloats(si.getFloats());
                im.setCustomModelDataComponent(component);
            }
            im.setDisplayName(what.item());
            im.getPersistentDataContainer().set(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER, 10001);
            is.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(is);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
            display.setBillboard(Display.Billboard.VERTICAL);
            display.setInvulnerable(true);
            TextDisplay text = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.65d, 0.5d), EntityType.TEXT_DISPLAY);
            text.setAlignment(TextDisplay.TextAlignment.CENTER);
            text.setText(what.item() + "\n" + ChatColor.RED + "Cost:" + ChatColor.RESET + String.format(" %.2f", what.cost()));
            text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
            text.setBillboard(Display.Billboard.VERTICAL);
        } catch (IllegalArgumentException e) {
            plugin.debug("Illegal shop item [" + toEnum + "] :" + e.getMessage());
        }
    }
}
