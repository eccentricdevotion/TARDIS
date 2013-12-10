/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.preferences;

import com.google.common.collect.ImmutableList;
import java.util.*;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

/**
 * TabCompleter for /tardisprefs
 */
public class TARDISPrefsTabComplete implements TabCompleter {

    private final List<String> ROOT_SUBS = ImmutableList.of("auto", "beacon", "dnd", "eps", "eps_message", "floor", "hads", "isomorphic", "key", "lamp", "plain", "platform", "quotes", "sfx", "sign", "submarine", "wall");
    private final List<String> ONOFF_SUBS = ImmutableList.of("on", "off");
    private final ImmutableList<String> KEY_SUBS;
    private final ImmutableList<String> MAT_SUBS;

    public TARDISPrefsTabComplete() {
        HashMap<String, int[]> map = new TARDISWalls().blocks;
        List<String> mats = new ArrayList<String>();
        for (String key : map.keySet()) {
            mats.add(key);
        }
        this.MAT_SUBS = ImmutableList.copyOf(mats);
        List<String> keys = new ArrayList<String>();
        Material[] materialValues = Material.values();
        for (Material key : materialValues) {
            keys.add(key.toString());
        }
        this.KEY_SUBS = ImmutableList.copyOf(keys);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];

        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("add") || sub.equals("remove")) {
                return null;
            } else if (sub.equals("floor") || sub.equals("wall")) {
                return partial(lastArg, MAT_SUBS);
            } else if (sub.equals("key")) {
                return partial(lastArg, KEY_SUBS);
            } else {
                return partial(lastArg, ONOFF_SUBS);
            }
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }
}
