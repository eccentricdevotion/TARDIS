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
package me.eccentric_nz.tardis.commands;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TabCompleter for /tardisroom
 */
public class TARDISRoomTabComplete extends TARDISCompleter implements TabCompleter {

	private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("add", "blocks", "required");
	private final ArrayList<String> ROOM_SUBS = new ArrayList<>();

	public TARDISRoomTabComplete(TARDISPlugin plugin) {
		// rooms - only add if enabled in the config
		Objects.requireNonNull(plugin.getRoomsConfig().getConfigurationSection("rooms")).getKeys(false).forEach((r) -> {
			if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
				ROOM_SUBS.add(r);
			}
		});
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		// Remember that we can return null to default to online player name matching
		String lastArg = args[args.length - 1];

		if (args.length <= 1) {
			return partial(args[0], ROOT_SUBS);
		} else if (args.length == 2) {
			String sub = args[0];
			if (sub.equals("required")) {
				return partial(lastArg, ROOM_SUBS);
			}
		}
		return ImmutableList.of();
	}
}
