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

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.commands.TARDISCompleter;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import me.eccentric_nz.tardis.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Teleport to the spawn point of worlds on the server
 *
 * @author eccentric_nz
 */
public class TARDISTeleportCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

	private final TARDISPlugin plugin;
	private final List<String> ROOT_SUBS = new ArrayList<>();

	public TARDISTeleportCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
		for (World w : plugin.getServer().getWorlds()) {
			ROOT_SUBS.add(TARDISAliasResolver.getWorldAlias(w));
		}
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tardisteleport")) {
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			}
			if (args.length == 2) {
				// get player from argument
				player = plugin.getServer().getPlayer(args[1]);
				if (player == null || !player.isOnline()) {
					TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
					return true;
				}
			}
			if (player == null) {
				return true;
			}
			if (args.length < 1) {
				TARDISMessage.send(player, "ARG_TP");
				return false;
			}
			World world = TARDISAliasResolver.getWorldFromAlias(args[0]);
			if (world != null) {
				Location spawn;
				if (args.length == 4 && args[3].equals("not_for_players")) {
					int x = TARDISNumberParsers.parseInt(args[1]);
					int z = TARDISNumberParsers.parseInt(args[2]);
					spawn = new Location(world, x, 64, z);
				} else {
					spawn = world.getSpawnLocation();
				}
				while (!world.getChunkAt(spawn).isLoaded()) {
					world.getChunkAt(spawn).load();
				}
				int highest = (world.getEnvironment() == Environment.NETHER) ? spawn.getBlockY() - 1 : world.getHighestBlockYAt(spawn);
				float yaw = player.getLocation().getYaw();
				float pitch = player.getLocation().getPitch();
				spawn.setYaw(yaw);
				spawn.setPitch(pitch);
				spawn.setY(highest + 1);
				player.teleport(spawn);
			} else {
				TARDISMessage.send(player, "WORLD_NOT_FOUND");
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		// Remember that we can return null to default to online player name matching
		if (args.length <= 1) {
			List<String> part = partial(args[0], ROOT_SUBS);
			return (part.size() > 0) ? part : null;
		} else if (args.length == 2) {
			return null;
		}
		return ImmutableList.of();
	}
}
