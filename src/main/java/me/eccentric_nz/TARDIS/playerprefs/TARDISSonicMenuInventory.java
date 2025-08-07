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
package me.eccentric_nz.TARDIS.playerprefs;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUISonicPreferences;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISSonicMenuInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISSonicMenuInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 36, Component.text("Sonic Prefs Menu", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Sonic Screwdriver Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[36];
        for (GUISonicPreferences sonic : GUISonicPreferences.values()) {
            if (sonic.getMaterial() == Material.BLAZE_ROD) {
                ItemStack is = ItemStack.of(sonic.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(ComponentUtils.toWhite("Sonic Screwdriver"));
                im.lore(List.of(Component.text(sonic.getName())));
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                component.setFloats(sonic.getFloats());
                im.setCustomModelDataComponent(component);
                is.setItemMeta(im);
                stack[sonic.getSlot()] = is;
            }
        }
        // coloured wool
        ItemStack wool = ItemStack.of(Material.WHITE_WOOL);
        ItemMeta wool_im = wool.getItemMeta();
        wool_im.displayName(Component.text("Display name colour"));
        wool_im.lore(List.of(Component.text("Click to select")));
        wool.setItemMeta(wool_im);
        stack[28] = wool;
        // info
        ItemStack info = ItemStack.of(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.displayName(Component.text("Instructions"));
        info_im.lore(List.of(
                Component.text("Put your Sonic Screwdriver"),
                Component.text("in the bottom left most slot"),
                Component.text("and then click on the"),
                Component.text("Sonic of your choice.")
        ));
        info.setItemMeta(info_im);
        stack[31] = info;
        // info 2
        ItemStack name = ItemStack.of(Material.BOOK, 1);
        ItemMeta name_im = name.getItemMeta();
        name_im.displayName(Component.text("Name"));
        name_im.lore(List.of(
                Component.text("If you want to have"),
                Component.text("a coloured display name"),
                Component.text("click the wool block"),
                Component.text("to choose a colour.")
        ));
        name.setItemMeta(name_im);
        stack[32] = name;
        // close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);
        stack[35] = close;

        return stack;
    }
}
