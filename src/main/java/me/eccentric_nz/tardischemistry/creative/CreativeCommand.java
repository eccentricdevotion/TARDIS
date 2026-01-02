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
package me.eccentric_nz.tardischemistry.creative;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischemistry.element.ElementInventory;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CreativeCommand {

    private final TARDIS plugin;

    public CreativeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "tardis.chemistry.creative")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Creative");
            return true;
        }
        Creative creative;
        try {
            creative = Creative.valueOf(args[2].toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }
        // do stuff
        switch (creative) {
            case elements -> {
                player.openInventory(new ElementInventory(plugin).getInventory());
                return true;
            }
            case compounds -> {
                player.openInventory(new CompoundsCreativeInventory(plugin).getInventory());
                return true;
            }
            default -> { // lab & products
                player.openInventory(new ProductsCreativeInventory(plugin).getInventory());
                return true;
            }
        }
    }
}
