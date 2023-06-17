/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.sudo;

import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural Configuration software to relocate the
 * console outside the ship (as it was too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
class SudoARS {

    private final TARDIS plugin;

    SudoARS(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean showARS(Player player, UUID uuid) {
        TARDISSudoTracker.SUDOERS.put(player.getUniqueId(), uuid);
        ItemStack[] tars = new TARDISARSInventory(plugin, player).getARS();
        Inventory ars = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
        ars.setContents(tars);
        player.openInventory(ars);
        return true;
    }
}
