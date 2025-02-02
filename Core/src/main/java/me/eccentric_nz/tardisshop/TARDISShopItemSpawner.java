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
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

public class TARDISShopItemSpawner {

    private final TARDIS plugin;

    public TARDISShopItemSpawner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setItemOld(Location location, TARDISShopItem what) {
        String toEnum = TARDISStringUtils.toEnumUppercase(what.getItem());
        try {
            ShopItem si = ShopItem.valueOf(toEnum);
            ItemStack is = new ItemStack(si.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setItemModel(si.getModel());
            im.setDisplayName(what.getItem());
            im.getPersistentDataContainer().set(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER, 10001);
            is.setItemMeta(im);
            Item item = location.getWorld().dropItem(location, is);
            item.setVelocity(new Vector(0, 0, 0));
            item.getPersistentDataContainer().set(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER, 10001);
            item.setCustomName(what.getItem());
            item.setCustomNameVisible(true);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setInvulnerable(true);
            item.setVelocity(new Vector(0, 0, 0));
            ArmorStand armourStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, -1.05d, 0), EntityType.ARMOR_STAND);
            armourStand.setSmall(true);
            armourStand.setVisible(false);
            armourStand.setCustomName(ChatColor.RED + "Cost:" + ChatColor.RESET + String.format(" %.2f", what.getCost()));
            armourStand.setCustomNameVisible(true);
            armourStand.setGravity(false);
            armourStand.setInvulnerable(true);
            armourStand.setVelocity(new Vector(0, 0, 0));
        } catch (IllegalArgumentException e) {
            plugin.debug("Illegal shop item [" + toEnum + "] :" + e.getMessage());
        }
    }

    public void setItem(Location location, TARDISShopItem what) {
        String toEnum = TARDISStringUtils.toEnumUppercase(what.getItem());
        try {
            ShopItem si = ShopItem.valueOf(toEnum);
            ItemStack is = new ItemStack(si.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setItemModel(si.getModel());
            im.setDisplayName(what.getItem());
            im.getPersistentDataContainer().set(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER, 10001);
            is.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(is);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
            display.setBillboard(Display.Billboard.VERTICAL);
            display.setInvulnerable(true);
            TextDisplay text = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.65d, 0.5d), EntityType.TEXT_DISPLAY);
            text.setAlignment(TextDisplay.TextAlignment.CENTER);
            text.setText(what.getItem() + "\n" + ChatColor.RED + "Cost:" + ChatColor.RESET + String.format(" %.2f", what.getCost()));
            text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
            text.setBillboard(Display.Billboard.VERTICAL);
        } catch (IllegalArgumentException e) {
            plugin.debug("Illegal shop item [" + toEnum + "] :" + e.getMessage());
        }
    }
}
