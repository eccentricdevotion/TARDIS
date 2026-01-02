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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
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
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        // check if item in hand is a Handles program disk
        ItemStack disk = player.getInventory().getItemInMainHand();
        if (disk.getType().equals(Material.MUSIC_DISC_WARD) && disk.hasItemMeta()) {
            ItemMeta dim = disk.getItemMeta();
            if (dim.hasDisplayName() && ComponentUtils.stripColour(dim.displayName()).equals("Handles Program Disk")) {
                // get the program_id from the disk
                int pid = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(dim.lore().get(1)));
                // get the name - must be 32 chars or fewer
                String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if (name.length() < 3 || name.length() > 32) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NAME_NOT_VALID");
                    return true;
                }
                // rename the disk
                HashMap<String, Object> set = new HashMap<>();
                set.put("name", name);
                HashMap<String, Object> wherep = new HashMap<>();
                wherep.put("program_id", pid);
                plugin.getQueryFactory().doUpdate("programs", set, wherep);
                List<Component> lore = dim.lore();
                lore.set(0, Component.text(name));
                dim.lore(lore);
                disk.setItemMeta(dim);
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "HANDLES_DISK");
        }
        return true;
    }
}
