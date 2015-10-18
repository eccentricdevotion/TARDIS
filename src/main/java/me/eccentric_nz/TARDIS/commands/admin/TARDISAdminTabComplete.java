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
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardFlag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * TabCompleter for /tardisadmin
 */
public class TARDISAdminTabComplete extends TARDISCompleter implements TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> BOOL_SUBS = ImmutableList.of("true", "false");
    private final ImmutableList<String> COLOURS = ImmutableList.of("AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW");
    private final ImmutableList<String> CONFIG_SUBS = ImmutableList.of("worlds", "rechargers", "storage", "creation", "police_box", "travel", "preferences", "allow", "growth", "rooms");
    private final ImmutableList<String> DB_SUBS = ImmutableList.of("mysql", "sqlite");
    private final ImmutableList<String> DIFFICULTY_SUBS = ImmutableList.of("easy", "medium", "hard");
    private final ImmutableList<String> FLAG_SUBS;
    private final ImmutableList<String> KEYS = ImmutableList.of("first", "second", "third", "fifth", "seventh", "ninth", "tenth", "eleventh", "susan", "rose", "sally", "perception", "gold");
    private final ImmutableList<String> LANG_SUBS = ImmutableList.of("ar", "bg", "ca", "zh", "cs", "da", "nl", "en", "et", "fi", "fr", "de", "el", "ht", "he", "hi", "mww", "hu", "id", "it", "ja", "ko", "lv", "lt", "ms", "no", "fa", "pl", "pt", "ro", "ru", "sk", "sl", "es", "sv", "th", "tr", "uk", "ur", "vi");
    private final ImmutableList<String> PRESETS;
    private final ImmutableList<String> REGION_SUBS = ImmutableList.of("entry", "exit");
    private final ImmutableList<String> ROOT_SUBS;
    private final ImmutableList<String> SIEGE_SUBS = ImmutableList.of("enabled", "breeding", "growth", "butcher", "creeper", "healing", "texture", "true", "false");
    private final ImmutableList<String> SONICS = ImmutableList.of("mark_1", "mark_2", "mark_3", "mark_4", "eighth", "ninth", "ninth_open", "tenth", "tenth_open", "eleventh", "eleventh_open", "master", "sarah_jane", "river_song", "war");
    private final ImmutableList<String> TIPS_SUBS = ImmutableList.of("400", "800", "1200", "1600");
    private final ImmutableList<String> TOWNY_SUBS = ImmutableList.of("none", "wilderness", "town", "nation");
    private final ImmutableList<String> VORTEX_SUBS = ImmutableList.of("kill", "teleport");

    public TARDISAdminTabComplete(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.isWorldGuardOnServer()) {
            this.FLAG_SUBS = ImmutableList.copyOf(TARDISWorldGuardFlag.getFLAG_LOOKUP().keySet());
        } else {
            this.FLAG_SUBS = ImmutableList.of("none", "build", "entry");
        }
        List<String> tmpPresets = new ArrayList<String>();
        for (PRESET p : PRESET.values()) {
            tmpPresets.add(p.toString());
        }
        this.PRESETS = ImmutableList.copyOf(tmpPresets);
        this.ROOT_SUBS = ImmutableList.copyOf(combineLists());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
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
            if (sub.equals("region_flag")) {
                return partial(lastArg, REGION_SUBS);
            }
            if (sub.equals("vortex_fall")) {
                return partial(lastArg, VORTEX_SUBS);
            }
            if (sub.equals("sign_colour")) {
                return partial(lastArg, COLOURS);
            }
            if (sub.equals("siege")) {
                return partial(lastArg, SIEGE_SUBS);
            }
            if (sub.equals("default_key")) {
                return partial(lastArg, KEYS);
            }
            if (sub.equals("default_preset")) {
                return partial(lastArg, PRESETS);
            }
            if (sub.equals("default_sonic")) {
                return partial(lastArg, SONICS);
            }
            if (sub.equals("database")) {
                return partial(lastArg, DB_SUBS);
            }
            if (sub.equals("language")) {
                return partial(lastArg, LANG_SUBS);
            }
            if (sub.equals("tips_limit")) {
                return partial(lastArg, TIPS_SUBS);
            }
            if (sub.equals("delete") || sub.equals("enter") || sub.equals("purge") || sub.equals("desiege")) { // return null to default to online player name matching
                return null;
            } else {
                return partial(lastArg, BOOL_SUBS);
            }
        } else if (args.length == 3) {
            return partial(lastArg, BOOL_SUBS);
        }
        return ImmutableList.of();
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
