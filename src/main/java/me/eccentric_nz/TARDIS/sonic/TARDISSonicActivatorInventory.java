/*
 * Copyright (C) 2024 eccentric_nz
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
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISSonicActivatorInventory {

    private final TARDIS plugin;
    private final ItemStack[] activator;

    TARDISSonicActivatorInventory(TARDIS plugin) {
        this.plugin = plugin;
        activator = getItemStack();
    }

    /**
     * Constructs an inventory for the Sonic Activator Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        List<TextComponent> lore = new ArrayList<>();
        lore.add(Component.text("To activate the generator"));
        lore.add(Component.text("add 1 of each of the following"));
        lore.add(Component.text("items to this inventory:"));
        // get the Sonic Generator recipe
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            lore.add(Component.text().color(NamedTextColor.AQUA).append(Component.text("GOLD_INGOT")).build());
            lore.add(Component.text().color(NamedTextColor.AQUA).append(Component.text("REDSTONE_BLOCK")).build());
        } else {
            lore.add(Component.text().color(NamedTextColor.AQUA).append(Component.text("GOLD_NUGGET")).build());
            lore.add(Component.text().color(NamedTextColor.AQUA).append(Component.text("REDSTONE")).build());
        }
        lore.add(Component.text().color(NamedTextColor.AQUA).append(Component.text("FLOWER_POT")).build());
        lore.add(Component.text().color(NamedTextColor.AQUA).append(Component.text("BLAZE_ROD")).build());
        lore.add(Component.text().color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC).append(Component.text("Then close the GUI.")).build());
        // info
        ItemStack info = new ItemStack(GUISonicActivator.INSTRUCTIONS.material(), 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.displayName(Component.text("Instructions"));
        info_im.lore(lore);
        info.setItemMeta(info_im);
        // close
        ItemStack close = new ItemStack(GUISonicActivator.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE")));
        close_im.setItemModel(GUISonicActivator.CLOSE.key());
        close.setItemMeta(close_im);

        return new ItemStack[]{null, null, null, null, null, null, null, info, close};
    }

    public ItemStack[] getActivator() {
        return activator;
    }
}
