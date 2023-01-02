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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class TARDISHandlesDiskCommand {

    private final TARDIS plugin;

    TARDISHandlesDiskCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean renameDisk(Player player, String[] args) {
        // check perms
        if (!TARDISPermission.hasPermission(player, "tardis.handles.program")) {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
        // check if item in hand is a Handles program disk
        ItemStack disk = player.getInventory().getItemInMainHand();
        if (disk != null && disk.getType().equals(Material.MUSIC_DISC_WARD) && disk.hasItemMeta()) {
            ItemMeta dim = disk.getItemMeta();
            if (dim.hasDisplayName() && ChatColor.stripColor(dim.getDisplayName()).equals("Handles Program Disk")) {
                // get the program_id from the disk
                int pid = TARDISNumberParsers.parseInt(dim.getLore().get(1));
                // get the name - must be 32 chars or less
                String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if (name.length() < 3 || name.length() > 32) {
                    TARDISMessage.send(player, "SAVE_NAME_NOT_VALID");
                    return true;
                }
                // rename the disk
                HashMap<String, Object> set = new HashMap<>();
                set.put("name", name);
                HashMap<String, Object> wherep = new HashMap<>();
                wherep.put("program_id", pid);
                plugin.getQueryFactory().doUpdate("programs", set, wherep);
                List<String> lore = dim.getLore();
                lore.set(0, name);
                dim.setLore(lore);
                disk.setItemMeta(dim);
            }
        } else {
            TARDISMessage.send(player, "HANDLES_DISK");
        }
        return true;
    }
}
