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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUISonicActivator;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISSonicActivatorInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    TARDISSonicActivatorInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 9, Component.text("Sonic Activator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Sonic Activator Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("To activate the generator"));
        lore.add(Component.text("add 1 of each of the following"));
        lore.add(Component.text("items to this inventory:"));
        // get the Sonic Generator recipe
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            lore.add(Component.text("GOLD_INGOT", NamedTextColor.AQUA));
            lore.add(Component.text("REDSTONE_BLOCK", NamedTextColor.AQUA));
        } else {
            lore.add(Component.text("GOLD_NUGGET", NamedTextColor.AQUA));
            lore.add(Component.text("REDSTONE", NamedTextColor.AQUA));
        }
        lore.add(Component.text("FLOWER_POT", NamedTextColor.AQUA));
        lore.add(Component.text("BLAZE_ROD", NamedTextColor.AQUA));
        lore.add(Component.text("Then close the GUI.", NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC));
        // info
        ItemStack info = ItemStack.of(GUISonicActivator.INSTRUCTIONS.material(), 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.displayName(Component.text("Instructions"));
        info_im.lore(lore);
        info.setItemMeta(info_im);
        // close
        ItemStack close = ItemStack.of(GUISonicActivator.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);

        return new ItemStack[]{null, null, null, null, null, null, null, info, close};
    }
}
