/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.universaltranslator;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter for /tardissay command
 */
public class TARDISSayTabComplete extends TARDISCompleter implements TabCompleter {

    private final List<String> LANGUAGE_SUBS = new ArrayList<>();

    public TARDISSayTabComplete() {
        for (Language l : Language.values()) {
            LANGUAGE_SUBS.add(l.toString());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length <= 1) {
            return partial(args[0], LANGUAGE_SUBS);
        }
        return ImmutableList.of();
    }
}
