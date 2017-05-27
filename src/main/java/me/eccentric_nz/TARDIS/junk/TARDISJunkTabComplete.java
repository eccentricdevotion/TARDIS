/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * TabCompleter for /tardistravel
 */
public class TARDISJunkTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("create", "find", "floor", "return", "delete", "time", "wall");
    private final ImmutableList<String> MAT_SUBS;

    public TARDISJunkTabComplete() {
        HashMap<String, TARDISWalls.Pair> map = new TARDISWalls().blocks;
        List<String> mats = new ArrayList<>();
        map.keySet().forEach((key) -> {
            mats.add(key);
        });
        this.MAT_SUBS = ImmutableList.copyOf(mats);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            if (args[0].equals("floor") || args[0].equals("wall")) {
                return partial(lastArg, MAT_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
