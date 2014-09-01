/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone
 * through at least twelve redesigns, though the TARDIS revealed that she had
 * archived 30 versions. Once a control room was reconfigured, the TARDIS
 * archived the old design "for neatness". The TARDIS effectively "curated" a
 * museum of control rooms — both those in the Doctor's personal past and future
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
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        int i = 0;
        // get consoles
        for (TARDISARS a : TARDISARS.values()) {
            if (a.isConsole()) {
                if (a.equals(TARDISARS.CUSTOM) && !plugin.getConfig().getBoolean("creation.custom_schematic")) {
                    // don't add a custom console if it is disabled in the config
                    continue;
                }
                Material m = Material.getMaterial(a.getId());
                ItemStack is = new ItemStack(m, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(a.getDescriptiveName());
                int cost = plugin.getArtronConfig().getInt("upgrades." + a.getActualName().toLowerCase());
                List<String> lore = new ArrayList<String>();
                lore.add("Cost: " + cost);
                if (!player.hasPermission("tardis." + a.getActualName().toLowerCase())) {
                    lore.add(ChatColor.RED + plugin.getLanguage().getString("NO_PERM_CONSOLE"));
                } else if (level < cost && !current_console.equals(a.getActualName())) {
                    lore.add(plugin.getLanguage().getString("UPGRADE_ABORT_ENERGY"));
                }
                if (current_console.equals(a.getActualName())) {
                    lore.add(ChatColor.GREEN + plugin.getLanguage().getString("CURRENT_CONSOLE"));
                }
                im.setLore(lore);
                is.setItemMeta(im);
                stack[i] = is;
                i++;
            }
        }
        // close
        ItemStack close = new ItemStack(Material.TRAP_DOOR, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close.setItemMeta(close_im);
        stack[26] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
