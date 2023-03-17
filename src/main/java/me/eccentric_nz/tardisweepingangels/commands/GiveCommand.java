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
package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisweepingangels.utils.HeadBuilder;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {

    private final TARDIS plugin;

    public GiveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean give(CommandSender sender, String[] args) {
        if (args.length < 3) {
            return false;
        }
        // get the player
        Player player = plugin.getServer().getPlayer(args[1]);
        if (player == null) {
            TARDISMessage.send(sender, MODULE.MONSTERS, "PLAYER_NOT_FOUND");
            return true;
        }
        // check monster type
        String upper = args[2].toUpperCase();
        Monster monster;
        try {
            monster = Monster.valueOf(upper);
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(sender, MODULE.MONSTERS, "WA_INVALID");
            return true;
        }
        if (monster == Monster.K9 || monster == Monster.TOCLAFANE) {
            TARDISMessage.send(sender, MODULE.MONSTERS, "WA_HELMET");
            return true;
        }
        ItemStack is = HeadBuilder.getItemStack(monster);
        player.getInventory().addItem(is);
        player.updateInventory();
        TARDISMessage.send(sender, MODULE.MONSTERS, "WA_GIVE", player.getName(), monster.getName());
        String who = "The server";
        if (sender instanceof Player) {
            who = sender.getName();
        }
        if (!who.equals(player.getName())) {
            TARDISMessage.send(sender, MODULE.MONSTERS, "WA_GIVE_WHO", who, monster.getName());
        }
        return true;
    }
}
