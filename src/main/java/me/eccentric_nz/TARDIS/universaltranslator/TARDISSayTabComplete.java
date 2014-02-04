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
package me.eccentric_nz.TARDIS.universaltranslator;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

/**
 * TabCompleter for /tardissay command
 */
public class TARDISSayTabComplete implements TabCompleter {

    private final ImmutableList<String> LANGUAGE_SUBS = ImmutableList.of("ARABIC", "BULGARIAN", "CATALAN", "CHINESE_SIMPLIFIED", "CHINESE_TRADITIONAL", "CZECH", "DANISH", "DUTCH", "ENGLISH", "ESTONIAN", "FINNISH", "FRENCH", "GERMAN", "GREEK", "HAITIAN_CREOLE", "HEBREW", "HINDI", "HMONG_DAW", "HUNGARIAN", "INDONESIAN", "ITALIAN", "JAPANESE", "KOREAN", "LATVIAN", "LITHUANIAN", "MALAY", "NORWEGIAN", "PERSIAN", "POLISH", "PORTUGUESE", "ROMANIAN", "RUSSIAN", "SLOVAK", "SLOVENIAN", "SPANISH", "SWEDISH", "THAI", "TURKISH", "UKRAINIAN", "URDU", "VIETNAMESE");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length <= 1) {
            return partial(args[0], LANGUAGE_SUBS);
        }
        return ImmutableList.of();
    }

    private List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<String>(from.size()));
    }
}
