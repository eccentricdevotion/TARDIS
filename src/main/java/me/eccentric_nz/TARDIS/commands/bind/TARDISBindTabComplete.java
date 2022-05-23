/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.bind;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter for /tardisbind command
 */
public class TARDISBindTabComplete extends TARDISCompleter implements TabCompleter {

    private final List<String> ROOT_SUBS = ImmutableList.of("add", "remove");
    private final ImmutableList<String> FIRST_SUBS = ImmutableList.of("save", "player", "area", "biome", "hide", "rebuild", "home", "cave", "make_her_blue", "occupy", "chameleon", "transmat");
    private final List<String> CHAM_SUBS = new ArrayList<>();
    private final List<String> BIOME_SUBS = new ArrayList<>();

    public TARDISBindTabComplete() {
        CHAM_SUBS.add("OFF");
        CHAM_SUBS.add("ADAPT");
        for (PRESET p : PRESET.values()) {
            CHAM_SUBS.add(p.toString());
        }
        for (Biome b : Biome.values()) {
            BIOME_SUBS.add(b.toString());
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            return partial(args[1], FIRST_SUBS);
        } else if (args.length == 3) {
            String sub = args[1];
            switch (sub) {
                case "player":
                    return null;
                case "chameleon":
                    return partial(lastArg, CHAM_SUBS);
                case "biome":
                    return partial(lastArg, BIOME_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
