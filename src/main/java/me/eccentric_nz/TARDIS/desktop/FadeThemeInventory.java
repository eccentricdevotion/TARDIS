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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUIUpgrade;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone
 * through at least twelve redesigns, though the TARDIS revealed that she had
 * archived 30 versions. Once a control room was reconfigured, the TARDIS
 * archived the old design "for neatness". The TARDIS effectively "curated" a
 * museum of control rooms — both those in the Doctor's personal past and future
 *
 * @author eccentric_nz
 */
public class FadeThemeInventory extends DesktopThemeInventory {

    private final TARDIS plugin;
    private final Player player;
    private final String current_console;
    private final int level;

    public FadeThemeInventory(TARDIS plugin, Player player, String current_console, int level) {
        this.plugin = plugin;
        this.player = player;
        this.current_console = current_console;
        this.level = level;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Upgrade Menu", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get fade consoles
        for (Map.Entry<Material, Material[]> gradient : plugin.getBuildKeeper().getFade().getGradients().entrySet()) {
            List<ItemStack> blocks = new ArrayList<>();
            for (Material m : gradient.getValue()) {
                blocks.add(new ItemStack(m));
            }
            Key key = gradient.getKey().key();
            ItemStack is = new ItemStack(Material.LIGHT_GRAY_BUNDLE);
            is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(TARDISStringUtils.capitalise(key.value())));
            is.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents(blocks));
            is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                    .addHiddenComponents(TARDISConstants.HIDE)
                    .build());
            is.setData(DataComponentTypes.ITEM_MODEL, key);
            stack[i] = is;
            i++;
        }
        if (plugin.getConfig().getBoolean("desktop.previews")) {
            // info
            ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
            info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Info"));
            info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Component.text("Shift-left click"),
                    Component.text("a console block to"),
                    Component.text("transmat to a"),
                    Component.text("desktop preview.")
            )));
            stack[GUIUpgrade.INFO.slot()] = info;
        }
        // archive consoles
        if (TARDISPermission.hasPermission(player, "tardis.archive")) {
            ItemStack archive = ItemStack.of(GUIUpgrade.ARCHIVE_CONSOLES.material(), 1);
            archive.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Archive Consoles"));
            stack[GUIUpgrade.ARCHIVE_CONSOLES.slot()] = archive;
        }
        // customise console
        ItemStack console = ItemStack.of(GUIUpgrade.CONSOLE_ROTOR.material(), 1);
        console.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Customise Console"));
        stack[GUIUpgrade.CONSOLE_ROTOR.slot()] = console;
        // built-in consoles page
        ItemStack builtin = ItemStack.of(GUIUpgrade.BUILTIN.material(), 1);
        builtin.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_BUILTIN", "Go to plugin desktops")));
        stack[GUIUpgrade.BUILTIN.slot()] = builtin;
        // custom consoles page
        ItemStack custom = ItemStack.of(GUIUpgrade.CUSTOM.material(), 1);
        custom.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CUSTOM", "Go to custom desktops")));
        stack[GUIUpgrade.CUSTOM.slot()] = custom;
        // close
        stack[GUIUpgrade.CLOSE.slot()] = GUIItemFactory.close();

        return stack;
    }
}
