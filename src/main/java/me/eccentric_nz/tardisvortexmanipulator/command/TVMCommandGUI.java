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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author macgeek
 */
public class TVMCommandGUI {

    private final TARDIS plugin;

    public TVMCommandGUI(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player) {
        // get tachyon level
        TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, player.getUniqueId().toString());
        if (rs.resultSet()) {
            // open gui
            ItemStack[] gui = new TVMGUI(plugin, rs.getTachyonLevel()).getGUI();
            Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Vortex Manipulator");
            vmg.setContents(gui);
            player.openInventory(vmg);
        }
        return true;
    }
}
