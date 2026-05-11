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
package me.eccentric_nz.TARDIS.sonic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUISonicActivator;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * @author eccentric_nz
 */
class SonicActivatorInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    SonicActivatorInventory(TARDIS plugin) {
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
        ItemLore.Builder lore = ItemLore.lore();
        lore.addLine(Component.text("To activate the generator"));
        lore.addLine(Component.text("add 1 of each of the following"));
        lore.addLine(Component.text("items to this inventory:"));
        // get the Sonic Generator recipe
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            lore.addLine(Component.text("GOLD_INGOT", NamedTextColor.AQUA));
            lore.addLine(Component.text("REDSTONE_BLOCK", NamedTextColor.AQUA));
        } else {
            lore.addLine(Component.text("GOLD_NUGGET", NamedTextColor.AQUA));
            lore.addLine(Component.text("REDSTONE", NamedTextColor.AQUA));
        }
        lore.addLine(Component.text("FLOWER_POT", NamedTextColor.AQUA));
        lore.addLine(Component.text("BLAZE_ROD", NamedTextColor.AQUA));
        lore.addLine(Component.text("Then close the GUI.", NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC));
        // info
        ItemStack info = ItemStack.of(GUISonicActivator.INSTRUCTIONS.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Instructions"));
        info.setData(DataComponentTypes.LORE, lore.build());
        // close
        ItemStack close = GUIItemFactory.close();

        return new ItemStack[]{null, null, null, null, null, null, null, info, close};
    }
}
