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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodeldata.GUIUpgrade;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
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

    private final ItemStack[] menu;
    private final TARDIS plugin;
    private final Player player;
    private final String current_console;
    private final int level;

    public TARDISPluginThemeInventory(TARDIS plugin, Player player, String current_console, int level) {
        this.plugin = plugin;
        this.player = player;
        this.current_console = current_console;
        this.level = level;
        menu = getItemStack();
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
        for (Schematic a : Consoles.getBY_NAMES().values()) {
            if (!a.isCustom()) {
                ItemStack is = getConsoleStack(plugin, a, current_console, player, level);
                if (is != null) {
                    stack[i] = is;
                    i++;
                }
            }
        }
        if (plugin.getConfig().getBoolean("creation.previews")) {
            // info
            ItemStack info = new ItemStack(GUIChameleonConstructor.INFO.material(), 1);
            ItemMeta io = info.getItemMeta();
            io.setDisplayName("Info");
            io.setLore(List.of("Shift-left click", "a console block to", "transmat to a", "desktop preview."));
            io.setCustomModelData(GUIUpgrade.INFO.customModelData());
            info.setItemMeta(io);
            stack[GUIUpgrade.INFO.slot()] = info;
        }
        // archive consoles
        if (TARDISPermission.hasPermission(player, "tardis.archive")) {
            ItemStack arc = new ItemStack(GUIUpgrade.ARCHIVE_CONSOLES.material(), 1);
            ItemMeta hive_im = arc.getItemMeta();
            hive_im.setDisplayName("Archive Consoles");
            hive_im.setCustomModelData(GUIUpgrade.ARCHIVE_CONSOLES.customModelData());
            arc.setItemMeta(hive_im);
            stack[GUIUpgrade.ARCHIVE_CONSOLES.slot()] = arc;
        }
        if (plugin.getConfig().getBoolean("allow.repair")) {
            // repair
            if (TARDISPermission.hasPermission(player, "tardis.repair")) {
                ItemStack rep = new ItemStack(GUIUpgrade.REPAIR_CONSOLE.material(), 1);
                ItemMeta air_im = rep.getItemMeta();
                air_im.setDisplayName("Repair Console");
                air_im.setCustomModelData(GUIUpgrade.REPAIR_CONSOLE.customModelData());
                rep.setItemMeta(air_im);
                stack[GUIUpgrade.REPAIR_CONSOLE.slot()] = rep;
            }
            // clean
            if (TARDISPermission.hasPermission(player, "tardis.repair")) {
                ItemStack cle = new ItemStack(GUIUpgrade.CLEAN.material(), 1);
                ItemMeta an_im = cle.getItemMeta();
                an_im.setDisplayName("Clean");
                an_im.setCustomModelData(GUIUpgrade.CLEAN.customModelData());
                cle.setItemMeta(an_im);
                stack[GUIUpgrade.CLEAN.slot()] = cle;
            }
        }
        // custom consoles page
        ItemStack custom = new ItemStack(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta custom_im = custom.getItemMeta();
        custom_im.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        custom_im.setCustomModelData(GUIChameleonPresets.GO_TO_PAGE_2.customModelData());
        custom.setItemMeta(custom_im);
        stack[51] = custom;
        // close
        ItemStack close = new ItemStack(GUIUpgrade.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUIUpgrade.CLOSE.customModelData());
        close.setItemMeta(close_im);
        stack[GUIUpgrade.CLOSE.slot()] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
