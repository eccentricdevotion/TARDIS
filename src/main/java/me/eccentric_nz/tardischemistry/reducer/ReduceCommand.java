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
package me.eccentric_nz.tardischemistry.reducer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ReduceCommand {

    private final TARDIS plugin;

    public ReduceCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean use(Player player) {
        if (!TARDISPermission.hasPermission(player, "tardis.reducer.use")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CHEMISTRY_SUB_PERM", "Reduction");
            return true;
        }
        // do stuff
        ItemStack[] menu = new ReducerInventory(plugin).getMenu();
        Inventory reductions = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Material reducer");
        reductions.setContents(menu);
        player.openInventory(reductions);
        return true;
    }
}
