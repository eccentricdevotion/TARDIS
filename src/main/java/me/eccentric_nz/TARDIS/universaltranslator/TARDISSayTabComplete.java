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
package me.eccentric_nz.TARDIS.universaltranslator;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * TabCompleter for /tardissay command
 */
public class TARDISSayTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> LANGUAGE_SUBS = ImmutableList.of("AFRIKAANS", "ALBANIAN", "ARABIC", "ARMENIAN", "AZERBAIJANI", "BASHKIR", "BASQUE", "BELARUSIAN", "BOSNIAN", "BULGARIAN", "CATALAN", "CHINESE", "CROATIAN", "CZECH", "DANISH", "DUTCH", "ENGLISH", "ESTONIAN", "FINNISH", "FRENCH", "GALICIAN", "GEORGIAN", "GERMAN", "GREEK", "HAITIAN", "HEBREW", "HUNGARIAN", "ICELANDIC", "INDONESIAN", "IRISH", "ITALIAN", "JAPANESE", "KAZAKH", "KIRGHIZ", "KOREAN", "LATIN", "LATVIAN", "LITHUANIAN", "MACEDONIAN", "MALAGASY", "MALAY", "MALTESE", "MONGOLIAN", "NORWEGIAN", "PERSIAN", "POLISH", "PORTUGUESE", "ROMANIAN", "RUSSIAN", "SERBIAN", "SLOVAK", "SLOVENIAN", "SPANISH", "SWAHILI", "SWEDISH", "TAGALOG", "TAJIK", "TATAR", "THAI", "TURKISH", "UKRAINIAN", "UZBEK", "VIETNAMESE", "WELSH");

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length <= 1) {
            return partial(args[0], LANGUAGE_SUBS);
        }
        return ImmutableList.of();
    }
}
