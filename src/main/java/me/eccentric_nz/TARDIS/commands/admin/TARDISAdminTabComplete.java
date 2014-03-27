/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.admin;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardFlag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

/**
 * TabCompleter for /tardisadmin
 */
public class TARDISAdminTabComplete implements TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> DIFFICULTY_SUBS = ImmutableList.of("easy", "hard");
    private final ImmutableList<String> BOOL_SUBS = ImmutableList.of("true", "false");
    private final ImmutableList<String> DB_SUBS = ImmutableList.of("mysql", "sqlite");
    private final ImmutableList<String> TOWNY_SUBS = ImmutableList.of("none", "wilderness", "town", "nation");
    private final ImmutableList<String> FLAG_SUBS = ImmutableList.copyOf(TARDISWorldGuardFlag.getFLAG_LOOKUP().keySet());
    private final ImmutableList<String> CONFIG_SUBS = ImmutableList.of("worlds", "rechargers", "storage", "creation", "police_box", "travel", "preferences", "allow", "growth", "rooms");
    private final ImmutableList<String> COLOURS = ImmutableList.of("AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW");

    public TARDISAdminTabComplete(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], combineLists());
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("config")) {
                return partial(lastArg, CONFIG_SUBS);
            }
            if (sub.equals("difficulty")) {
                return partial(lastArg, DIFFICULTY_SUBS);
            }
            if (sub.equals("respect_towny")) {
                return partial(lastArg, TOWNY_SUBS);
            }
            if (sub.equals("respect_worldguard")) {
                return partial(lastArg, FLAG_SUBS);
            }
            if (sub.equals("sign_colour")) {
                return partial(lastArg, COLOURS);
            }
            if (sub.equals("database")) {
                return partial(lastArg, DB_SUBS);
            }
            if (sub.equals("delete") || sub.equals("enter") || sub.equals("purge")) { // return null to default to online player name matching
                return null;
            } else {
                return partial(lastArg, BOOL_SUBS);
            }
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }

    private List<String> combineLists() {
        List<String> newList = new ArrayList<String>(plugin.getGeneralKeeper().getTardisAdminCommand().firstsStr.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsBool.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsInt.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsStrArtron.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsIntArtron.size());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsStr.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsBool.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsInt.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsStrArtron);
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsIntArtron);
        return newList;
    }
}
