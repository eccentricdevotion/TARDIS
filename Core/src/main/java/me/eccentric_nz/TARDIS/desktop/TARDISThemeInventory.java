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
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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
public class TARDISThemeInventory {

    /**
     * Constructs an item stack for the Desktop Theme GUI.
     *
     * @param plugin an instance of the TARDIS plugin
     * @param schematic the console schematic to get the item stack for
     * @param currentConsole the players current console
     * @param player the player using the GUI
     * @param level the player's Artron level
     * @return an ItemStack with the console information
     */
    public ItemStack getConsoleStack(TARDIS plugin, Schematic schematic, String currentConsole, Player player, int level) {
        ItemStack is = null;
        // get console
        Material m = Material.getMaterial(schematic.getSeed());
        if (!m.equals(Material.COBBLESTONE)) {
            is = new ItemStack(m, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(schematic.getDescription());
            int cost = plugin.getArtronConfig().getInt("upgrades." + schematic.getPermission());
            if (currentConsole.equals(schematic.getPermission())) {
                cost = Math.round((plugin.getArtronConfig().getInt("just_wall_floor") / 100F) * cost);
            }
            List<String> lore = new ArrayList<>();
            lore.add("Cost: " + cost);
            if (!TARDISPermission.hasPermission(player, "tardis." + schematic.getPermission())) {
                lore.add(ChatColor.RED + plugin.getLanguage().getString("NO_PERM_CONSOLE"));
            } else if (level < cost && !currentConsole.equals(schematic.getPermission())) {
                lore.add(plugin.getLanguage().getString("UPGRADE_ABORT_ENERGY"));
            }
            if (currentConsole.equals(schematic.getPermission())) {
                lore.add(ChatColor.GREEN + plugin.getLanguage().getString("CURRENT_CONSOLE"));
            } else {
                lore.add(ChatColor.GREEN + plugin.getLanguage().getString("RESET"));
                lore.add(ChatColor.GREEN + plugin.getLanguage().getString("REMEMBER"));
            }
            im.setLore(lore);
//            switch (m) {
//            }
            is.setItemMeta(im);
        }
        return is;
    }
}
