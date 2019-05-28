/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the TARDIS revealed that she had archived 30 versions. Once a control room was reconfigured, the TARDIS archived the
 * old design "for neatness". The TARDIS effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
public class TARDISThemeInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;
    private final Player player;
    private final String current_console;
    private final int level;

    public TARDISThemeInventory(TARDIS plugin, Player player, String current_console, int level) {
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
        // get consoles
        for (SCHEMATIC a : CONSOLES.getBY_NAMES().values()) {
            Material m = Material.getMaterial(a.getSeed());
            if (!m.equals(Material.COBBLESTONE)) {
                ItemStack is = new ItemStack(m, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(a.getDescription());
                int cost = plugin.getArtronConfig().getInt("upgrades." + a.getPermission());
                if (current_console.equals(a.getPermission())) {
                    cost = Math.round((plugin.getArtronConfig().getInt("just_wall_floor") / 100F) * cost);
                }
                List<String> lore = new ArrayList<>();
                lore.add("Cost: " + cost);
                if (!player.hasPermission("tardis." + a.getPermission())) {
                    lore.add(ChatColor.RED + plugin.getLanguage().getString("NO_PERM_CONSOLE"));
                } else if (level < cost && !current_console.equals(a.getPermission())) {
                    lore.add(plugin.getLanguage().getString("UPGRADE_ABORT_ENERGY"));
                }
                if (current_console.equals(a.getPermission())) {
                    lore.add(ChatColor.GREEN + plugin.getLanguage().getString("CURRENT_CONSOLE"));
                } else {
                    lore.add(ChatColor.GREEN + plugin.getLanguage().getString("RESET"));
                    lore.add(ChatColor.GREEN + plugin.getLanguage().getString("REMEMBER"));
                }
                im.setLore(lore);
                is.setItemMeta(im);
                stack[i] = is;
                i++;
            }
        }
        // archive consoles
        if (player.hasPermission("tardis.archive")) {
            ItemStack arc = new ItemStack(Material.BOWL, 1);
            ItemMeta hive_im = arc.getItemMeta();
            hive_im.setDisplayName("Archive Consoles");
            arc.setItemMeta(hive_im);
            stack[46] = arc;
        }
        if (plugin.getConfig().getBoolean("allow.repair")) {
            // repair
            if (player.hasPermission("tardis.repair")) {
                ItemStack rep = new ItemStack(Material.BOWL, 1);
                ItemMeta air_im = rep.getItemMeta();
                air_im.setDisplayName("Repair Console");
                rep.setItemMeta(air_im);
                stack[47] = rep;
            }
            // clean
            if (player.hasPermission("tardis.repair")) {
                ItemStack cle = new ItemStack(Material.BOWL, 1);
                ItemMeta an_im = cle.getItemMeta();
                an_im.setDisplayName("Clean");
                cle.setItemMeta(an_im);
                stack[48] = cle;
            }
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close.setItemMeta(close_im);
        stack[53] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
