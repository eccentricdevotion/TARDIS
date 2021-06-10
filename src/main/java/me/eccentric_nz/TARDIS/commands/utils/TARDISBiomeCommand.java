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
package me.eccentric_nz.tardis.commands.utils;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TARDISBiomeCommand implements CommandExecutor {

    private final TARDISPlugin plugin;

    public TARDISBiomeCommand(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisbiome")) {
            Player player;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            // get location
            Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 20).getLocation();
            // get biome
            TARDISBiome biome = TARDISStaticUtils.getBiomeAt(eyeLocation);
            TARDISMessage.message(player, "The TARDISBiome is: " + biome.getKey());
            return true;
        }
        return false;
    }
}
