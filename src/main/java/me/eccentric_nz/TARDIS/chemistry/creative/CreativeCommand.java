/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.chemistry.creative;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.chemistry.element.ElementInventory;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class CreativeCommand {

    private final TardisPlugin plugin;

    public CreativeCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player, String[] args) {
        if (!TardisPermission.hasPermission(player, "tardis.chemistry.creative")) {
            TardisMessage.send(player, "CHEMISTRY_SUB_PERM", "Creative");
            return true;
        }
        Creative creative;
        try {
            creative = Creative.valueOf(args[2].toLowerCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return false;
        }
        // do stuff
        switch (creative) {
            case elements -> {
                ItemStack[] emenu = new ElementInventory(plugin).getMenu();
                Inventory elements = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Atomic elements");
                elements.setContents(emenu);
                player.openInventory(elements);
                return true;
            }
            case compounds -> {
                ItemStack[] cmenu = new CompoundsCreativeInventory(plugin).getMenu();
                Inventory compounds = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Molecular compounds");
                compounds.setContents(cmenu);
                player.openInventory(compounds);
                return true;
            }
            default -> { // lab & products
                ItemStack[] lmenu = new ProductsCreativeInventory(plugin).getMenu();
                Inventory lab = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Products");
                lab.setContents(lmenu);
                player.openInventory(lab);
                return true;
            }
        }
    }
}
