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
package me.eccentric_nz.tardis.schematic;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.tardis.commands.TARDISCompleter;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TabCompleter for /tardisschematic
 */
public class TARDISSchematicTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("load", "paste", "save", "clear", "replace");
    private final List<String> FILE_SUBS = new ArrayList<>();
    private final List<String> MAT_SUBS = new ArrayList<>();

    public TARDISSchematicTabComplete(File userDir) {
        if (userDir.exists()) {
            for (String f : Objects.requireNonNull(userDir.list())) {
                if (f.endsWith(".tschm")) {
                    FILE_SUBS.add(f.substring(0, f.length() - 6));
                }
            }
        }
        for (Material m : Material.values()) {
            if (m.isBlock()) {
                MAT_SUBS.add(m.toString());
            }
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("load")) {
            return partial(args[1], FILE_SUBS);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("replace")) {
            return partial(args[1], MAT_SUBS);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("replace")) {
            return partial(args[2], MAT_SUBS);
        }
        return ImmutableList.of();
    }
}
