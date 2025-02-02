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
package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TARDISBlueprint {

    private final TARDIS plugin;

    public TARDISBlueprint(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, String[] args, String blueprint) {
        Player player = null;
        if (args[0].equals("@s") && sender instanceof Player) {
            player = (Player) sender;
        } else if (args[0].equals("@p")) {
            List<Entity> near = Bukkit.selectEntities(sender, "@p");
            if (!near.isEmpty() && near.getFirst() instanceof Player) {
                player = (Player) near.getFirst();
                if (player == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_NEARBY_PLAYER");
                    return;
                }
            }
        } else {
            player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                return;
            }
        }
        if (player != null) {
            ItemStack bp = plugin.getTardisAPI().getTARDISBlueprintItem(blueprint, player);
            player.getInventory().addItem(bp);
            player.updateInventory();
            plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_ITEM", sender.getName(), "a TARDIS Blueprint Disk");
        }
    }
}
