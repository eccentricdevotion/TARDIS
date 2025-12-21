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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIUpgrade;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone
 * through at least twelve redesigns, though the TARDIS revealed that she had
 * archived 30 versions. Once a control room was reconfigured, the TARDIS
 * archived the old design "for neatness". The TARDIS effectively "curated" a
 * museum of control rooms — both those in the Doctor's personal past and future
 *
 * @author eccentric_nz
 */
public class TARDISPluginThemeInventory extends TARDISThemeInventory {

    private final TARDIS plugin;
    private final Player player;
    private final String current_console;
    private final int level;

    public TARDISPluginThemeInventory(TARDIS plugin, Player player, String current_console, int level) {
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
        // get built-in plugin consoles
        for (Schematic a : Desktops.getBY_NAMES().values()) {
            if (!a.isCustom()) {
                ItemStack is = getConsoleStack(plugin, a, current_console, player, level);
                if (is != null) {
                    stack[i] = is;
                    i++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("desktop.previews")) {
            // info
            ItemStack info = ItemStack.of(GUIChameleonConstructor.INFO.material(), 1);
            ItemMeta io = info.getItemMeta();
            io.displayName(Component.text("Info"));
            io.lore(List.of(
                    Component.text("Shift-left click"),
                    Component.text("a console block to"),
                    Component.text("transmat to a"),
                    Component.text("desktop preview.")
            ));
            info.setItemMeta(io);
            stack[GUIUpgrade.INFO.slot()] = info;
        }
        // archive consoles
        if (TARDISPermission.hasPermission(player, "tardis.archive")) {
            ItemStack arc = ItemStack.of(GUIUpgrade.ARCHIVE_CONSOLES.material(), 1);
            ItemMeta hive_im = arc.getItemMeta();
            hive_im.displayName(Component.text("Archive Consoles"));
            arc.setItemMeta(hive_im);
            stack[GUIUpgrade.ARCHIVE_CONSOLES.slot()] = arc;
        }
        if (plugin.getConfig().getBoolean("allow.repair")) {
            // repair
            if (TARDISPermission.hasPermission(player, "tardis.repair")) {
                ItemStack rep = ItemStack.of(GUIUpgrade.REPAIR_CONSOLE.material(), 1);
                ItemMeta air_im = rep.getItemMeta();
                air_im.displayName(Component.text("Repair Console"));
                rep.setItemMeta(air_im);
                stack[GUIUpgrade.REPAIR_CONSOLE.slot()] = rep;
            }
            // clean
            if (TARDISPermission.hasPermission(player, "tardis.repair")) {
                ItemStack cle = ItemStack.of(GUIUpgrade.CLEAN.material(), 1);
                ItemMeta an_im = cle.getItemMeta();
                an_im.displayName(Component.text("Clean"));
                cle.setItemMeta(an_im);
                stack[GUIUpgrade.CLEAN.slot()] = cle;
            }
        }
        // customise console
        ItemStack cons = ItemStack.of(GUIUpgrade.CONSOLE_ROTOR.material(), 1);
        ItemMeta ole_im = cons.getItemMeta();
        ole_im.displayName(Component.text("Customise Console"));
        cons.setItemMeta(ole_im);
        stack[GUIUpgrade.CONSOLE_ROTOR.slot()] = cons;
        // custom consoles page
        ItemStack custom = ItemStack.of(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta custom_im = custom.getItemMeta();
        custom_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_PAGE_2", "Go to page 2")));
        custom.setItemMeta(custom_im);
        stack[51] = custom;
        // close
        ItemStack close = ItemStack.of(GUIUpgrade.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);
        stack[GUIUpgrade.CLOSE.slot()] = close;

        return stack;
    }
}
