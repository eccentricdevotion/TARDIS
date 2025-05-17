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
package me.eccentric_nz.TARDIS.commands;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RootCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TabCompleter for /tardis?
 */
public class TARDISQuestionTabComplete extends TARDISCompleter implements TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS;
    private final List<String> notThese = List.of("aliases", "description", "usage", "permission", "permission-message");

    public TARDISQuestionTabComplete(TARDIS plugin) {
        this.plugin = plugin;
        List<String> roots = new ArrayList<>();
        for (RootCommand rc : RootCommand.values()) {
            roots.add(rc.toString());
        }
        ROOT_SUBS = ImmutableList.copyOf(roots);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            return partial(lastArg, removeUnwanted(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands." + sub).getKeys(false)));
        }
        return ImmutableList.of();
    }

    private ImmutableList<String> removeUnwanted(Set<String> set) {
        notThese.forEach(set::remove);
        return ImmutableList.copyOf(set);
    }
}
