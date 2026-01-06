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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIWallFloor;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the TARDIS revealed that she had archived 30 versions. Once a control room was reconfigured, the TARDIS archived the
 * old design "for neatness". The TARDIS effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
public class WallsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final String title;
    private final Inventory inventory;

    public WallsInventory(TARDIS plugin, String title) {
        this.plugin = plugin;
        this.title = title;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text(title, NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get BLOCKS
        for (Material entry : TARDISWalls.BLOCKS) {
            if (i > 52) {
                break;
            }
            ItemStack is = ItemStack.of(entry, 1);
            stack[i] = is;
            if (i % 9 == 7) {
                i += 2;
            } else {
                i++;
            }
        }

        // scroll up
        ItemStack scroll_up = ItemStack.of(GUIWallFloor.BUTTON_SCROLL_U.material(), 1);
        ItemMeta uim = scroll_up.getItemMeta();
        uim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_U")));
        scroll_up.setItemMeta(uim);
        stack[GUIWallFloor.BUTTON_SCROLL_U.slot()] = scroll_up;
        // scroll down
        ItemStack scroll_down = ItemStack.of(GUIWallFloor.BUTTON_SCROLL_D.material(), 1);
        ItemMeta dim = scroll_down.getItemMeta();
        dim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_D")));
        scroll_down.setItemMeta(dim);
        stack[GUIWallFloor.BUTTON_SCROLL_D.slot()] = scroll_down;
        // default wall
        ItemStack wall = ItemStack.of(GUIWallFloor.WALL.material(), 1);
        ItemMeta wim = wall.getItemMeta();
        wim.displayName(Component.text("Default Wall Block"));
        wall.setItemMeta(wim);
        stack[GUIWallFloor.WALL.slot()] = wall;
        // default floor
        ItemStack floor = ItemStack.of(GUIWallFloor.FLOOR.material(), 1);
        ItemMeta fim = floor.getItemMeta();
        fim.displayName(Component.text("Default Floor Block"));
        floor.setItemMeta(fim);
        stack[GUIWallFloor.FLOOR.slot()] = floor;
        // close
        ItemStack close = ItemStack.of(GUIWallFloor.BUTTON_ABORT.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text("Abort upgrade"));
        close.setItemMeta(close_im);
        stack[GUIWallFloor.BUTTON_ABORT.slot()] = close;

        return stack;
    }

    public String getTitle() {
        return title;
    }
}
