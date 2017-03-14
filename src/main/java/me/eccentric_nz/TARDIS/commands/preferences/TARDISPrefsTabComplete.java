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
package me.eccentric_nz.TARDIS.commands.preferences;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls.Pair;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * TabCompleter for /tardisprefs
 */
public class TARDISPrefsTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("auto", "auto_powerup", "auto_siege", "build", "beacon", "ctm", "difficulty", "dnd", "eps", "eps_message", "farm", "flight", "floor", "hads", "hads_type", "hum", "isomorphic", "junk", "key", "key_menu", "lamp", "language", "lanterns", "minecart", "policebox_textures", "quotes", "renderer", "sfx", "siege_floor", "siege_wall", "sign", "sonic", "submarine", "telepathy", "travelbar", "wall", "wool_lights");
    private final ImmutableList<String> DIFF_SUBS = ImmutableList.of("easy", "hard");
    private final ImmutableList<String> ONOFF_SUBS = ImmutableList.of("on", "off");
    private final ImmutableList<String> HADS_SUBS = ImmutableList.of("DISPLACEMENT", "DISPERSAL");
    private final ImmutableList<String> HUM_SUBS = ImmutableList.of("alien", "atmosphere", "computer", "copper", "coral", "galaxy", "learning", "mind", "neon", "sleeping", "void", "random");
    private final ImmutableList<String> FLIGHT_SUBS = ImmutableList.of("normal", "regulator", "manual");
    private final ImmutableList<String> KEY_SUBS;
    private final ImmutableList<String> MAT_SUBS;
    private final ImmutableList<String> LANGUAGE_SUBS = ImmutableList.of("AFRIKAANS", "ALBANIAN", "ARABIC", "ARMENIAN", "AZERBAIJANI", "BASHKIR", "BASQUE", "BELARUSIAN", "BOSNIAN", "BULGARIAN", "CATALAN", "CHINESE", "CROATIAN", "CZECH", "DANISH", "DUTCH", "ENGLISH", "ESTONIAN", "FINNISH", "FRENCH", "GALICIAN", "GEORGIAN", "GERMAN", "GREEK", "HAITIAN", "HEBREW", "HUNGARIAN", "ICELANDIC", "INDONESIAN", "IRISH", "ITALIAN", "JAPANESE", "KAZAKH", "KIRGHIZ", "KOREAN", "LATIN", "LATVIAN", "LITHUANIAN", "MACEDONIAN", "MALAGASY", "MALAY", "MALTESE", "MONGOLIAN", "NORWEGIAN", "PERSIAN", "POLISH", "PORTUGUESE", "ROMANIAN", "RUSSIAN", "SERBIAN", "SLOVAK", "SLOVENIAN", "SPANISH", "SWAHILI", "SWEDISH", "TAGALOG", "TAJIK", "TATAR", "THAI", "TURKISH", "UKRAINIAN", "UZBEK", "VIETNAMESE", "WELSH");

    public TARDISPrefsTabComplete(TARDIS plugin) {
        HashMap<String, Pair> map = new TARDISWalls().blocks;
        List<String> mats = new ArrayList<String>();
        for (String key : map.keySet()) {
            mats.add(key);
        }
        this.MAT_SUBS = ImmutableList.copyOf(mats);
        if (plugin.getConfig().getBoolean("travel.give_key") && !plugin.getConfig().getBoolean("allow.all_blocks")) {
            this.KEY_SUBS = ImmutableList.copyOf(plugin.getBlocksConfig().getStringList("keys"));
        } else {
            List<String> keys = new ArrayList<String>();
            Material[] materialValues = Material.values();
            for (Material key : materialValues) {
                if (!key.isBlock()) {
                    keys.add(key.toString());
                }
            }
            this.KEY_SUBS = ImmutableList.copyOf(keys);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("add") || sub.equals("remove")) {
                // return null to default to online player name matching
                return null;
            } else if (sub.equals("floor") || sub.equals("wall") || sub.equals("siege_floor") || sub.equals("siege_wall")) {
                return partial(lastArg, MAT_SUBS);
            } else if (sub.equals("key")) {
                return partial(lastArg, KEY_SUBS);
            } else if (sub.equals("language")) {
                return partial(lastArg, LANGUAGE_SUBS);
            } else if (sub.equals("flight")) {
                return partial(lastArg, FLIGHT_SUBS);
            } else if (sub.equals("difficulty")) {
                return partial(lastArg, DIFF_SUBS);
            } else if (sub.equals("hads_type")) {
                return partial(lastArg, HADS_SUBS);
            } else if (sub.equals("hum")) {
                return partial(lastArg, HUM_SUBS);
            } else {
                return partial(lastArg, ONOFF_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
