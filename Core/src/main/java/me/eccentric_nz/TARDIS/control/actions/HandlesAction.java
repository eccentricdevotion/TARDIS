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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetProgram;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProcessor;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesProgramInventory;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class HandlesAction {

    private final TARDIS plugin;

    public HandlesAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void cyberIt(Player player) {
        if (!TARDISPermission.hasPermission(player, "tardis.handles.use")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return;
        }
        TARDISSounds.playTARDISSound(player, "handles", 5L);
        if (!TARDISPermission.hasPermission(player, "tardis.handles.program")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return;
        }
        if (player.isSneaking()) {
            // open programming GUI
            ItemStack[] handles = new TARDISHandlesProgramInventory(plugin, 0).getHandles();
            Inventory hgui = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Handles Program");
            hgui.setContents(handles);
            player.openInventory(hgui);
        } else {
            // check if item in hand is a Handles program disk
            ItemStack disk = player.getInventory().getItemInMainHand();
            if (disk != null && disk.getType().equals(Material.MUSIC_DISC_WARD) && disk.hasItemMeta()) {
                ItemMeta dim = disk.getItemMeta();
                if (dim.hasDisplayName() && ChatColor.stripColor(dim.getDisplayName()).equals("Handles Program Disk")) {
                    // get the program_id from the disk
                    int pid = TARDISNumberParsers.parseInt(dim.getLore().get(1));
                    // query the database
                    ResultSetProgram rsp = new ResultSetProgram(plugin, pid);
                    if (rsp.resultSet()) {
                        // send program to processor
                        new TARDISHandlesProcessor(plugin, rsp.getProgram(), player, pid).processDisk();
                        // check in the disk
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("checked", 0);
                        HashMap<String, Object> wherep = new HashMap<>();
                        wherep.put("program_id", pid);
                        plugin.getQueryFactory().doUpdate("programs", set, wherep);
                        player.getInventory().setItemInMainHand(null);
                    }
                }
            }
        }
    }
}
