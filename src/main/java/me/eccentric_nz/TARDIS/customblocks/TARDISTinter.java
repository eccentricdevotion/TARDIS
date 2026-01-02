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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.keys.TintVariant;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class TARDISTinter {

    private final TARDIS plugin;
    private final Transformation transformation = new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, new Vector3f(1.01f, 1.01f, 1.01f), TARDISConstants.AXIS_ANGLE_ZERO);

    public TARDISTinter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void paintBlock(Player player, Block block, String dye, PlayerInventory inv) {
        Location location = block.getLocation().add(0.5d, 0.5d, 0.5d);
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        String glass = dye.replace("DYE", "STAINED_GLASS");
        Material material = Material.valueOf(glass);
        ItemStack is = ItemStack.of(material, 1);
        ItemMeta im = is.getItemMeta();
        switch (glass) {
            case "BLACK_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_BLACK.getKey());
            case "BLUE_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_BLUE.getKey());
            case "BROWN_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_BROWN.getKey());
            case "CYAN_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_CYAN.getKey());
            case "GRAY_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_GRAY.getKey());
            case "GREEN_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_GREEN.getKey());
            case "LIGHT_BLUE_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_LIGHT_BLUE.getKey());
            case "LIGHT_GRAY_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_LIGHT_GRAY.getKey());
            case "LIME_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_LIME.getKey());
            case "MAGENTA_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_MAGENTA.getKey());
            case "ORANGE_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_ORANGE.getKey());
            case "PINK_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_PINK.getKey());
            case "PURPLE_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_PURPLE.getKey());
            case "RED_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_RED.getKey());
            case "WHITE_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_WHITE.getKey());
            case "YELLOW_STAINED_GLASS" -> im.setItemModel(TintVariant.TINT_YELLOW.getKey());
        }
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.BYTE, (byte)1);
        is.setItemMeta(im);
        display.setItemStack(is);
        display.setTransformation(transformation);
        display.setInvulnerable(true);
        Block air = null;
        // search for air block to get light level
        for (BlockFace f : plugin.getGeneralKeeper().getBlockFaces()) {
            air = block.getRelative(f);
            if (air.getType().isAir()) {
                break;
            }
        }
        if (air != null) {
            display.setBrightness(new Display.Brightness(0, air.getLightFromSky()));
        }
        // remove one dye
        ItemStack eight = inv.getItem(8);
        int a = eight.getAmount();
        int a2 = a - 1;
        if (a2 > 0) {
            inv.getItem(8).setAmount(a2);
        } else {
            inv.setItem(8, null);
        }
        player.updateInventory();
    }
}
