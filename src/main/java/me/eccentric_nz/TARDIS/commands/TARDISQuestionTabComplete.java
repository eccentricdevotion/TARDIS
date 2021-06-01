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
import me.eccentric_nz.tardis.commands.TARDISCommandHelper.ROOT_COMMAND;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * TabCompleter for /tardis?
 */
public class TARDISQuestionTabComplete extends TARDISCompleter implements TabCompleter {

	private final TARDISPlugin plugin;
	private final ImmutableList<String> ROOT_SUBS;
	private final List<String> notThese = Arrays.asList("aliases", "description", "usage", "permission", "permission-message");

	public TARDISQuestionTabComplete(TARDISPlugin plugin) {
		this.plugin = plugin;
		List<String> roots = new ArrayList<>();
		for (ROOT_COMMAND rc : TARDISCommandHelper.ROOT_COMMAND.values()) {
			roots.add(rc.toString());
		}
		ROOT_SUBS = ImmutableList.copyOf(roots);
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		String lastArg = args[args.length - 1];
		if (args.length <= 1) {
			return partial(args[0], ROOT_SUBS);
		} else if (args.length == 2) {
			String sub = args[0];
			return partial(lastArg, removeUnwanted(Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection(
					"commands." + sub)).getKeys(false)));
		}
		return ImmutableList.of();
	}

	private ImmutableList<String> removeUnwanted(Set<String> set) {
		notThese.forEach(set::remove);
		return ImmutableList.copyOf(set);
	}
}
