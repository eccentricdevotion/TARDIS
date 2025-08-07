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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of travelling through time, even in a
 * non-linear direction. In the 26th century individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
public class TARDISPresetInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;

    public TARDISPresetInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Chameleon Presets", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];

        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (!ChameleonPreset.NOT_THESE.contains(preset.getCraftMaterial()) && !preset.usesArmourStand()) {
                if (TARDISPermission.hasPermission(player, "tardis.preset." + preset.toString().toLowerCase(Locale.ROOT))) {
                    ItemStack is = ItemStack.of(preset.getGuiDisplay(), 1);
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.text(preset.getDisplayName()));
                    is.setItemMeta(im);
                    stacks[preset.getSlot()] = is;
                }
            }
        }
        // back
        ItemStack back = ItemStack.of(GUIChameleonPresets.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.displayName(Component.text("Back"));
        back.setItemMeta(but);
        stacks[GUIChameleonPresets.BACK.slot()] = back;
        // page two
        ItemStack page = ItemStack.of(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta two = page.getItemMeta();
        two.displayName(Component.text(plugin.getLanguage().getString("BUTTON_PAGE_2")));
        page.setItemMeta(two);
        stacks[GUIChameleonPresets.GO_TO_PAGE_2.slot()] = page;
        // Cancel / close
        ItemStack close = ItemStack.of(GUIChameleonPresets.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        stacks[GUIChameleonPresets.CLOSE.slot()] = close;
        return stacks;
    }
}
