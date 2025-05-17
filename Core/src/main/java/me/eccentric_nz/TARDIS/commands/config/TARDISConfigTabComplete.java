/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.config;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Config;
import me.eccentric_nz.TARDIS.utility.protection.TARDISWorldGuardFlag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter for /tardisadmin
 */
public class TARDISConfigTabComplete extends TARDISCompleter implements TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS;
    private final ImmutableList<String> BOOL_SUBS = ImmutableList.of("true", "false");
    private final ImmutableList<String> COLOURS = ImmutableList.of("AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GRAY", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW");
    private final List<String> CONFIG_SUBS = new ArrayList<>();
    private final ImmutableList<String> DB_SUBS = ImmutableList.of("mysql", "sqlite");
    private final ImmutableList<String> MAPPING_SUBS = ImmutableList.of("dynmap", "BlueMap", "squaremap");
    private final ImmutableList<String> CRAFTING_SUBS = ImmutableList.of("easy", "hard");
    private final List<String> FILE_SUBS = new ArrayList<>();
    private final ImmutableList<String> FLAG_SUBS;
    private final ImmutableList<String> KEYS = ImmutableList.of("first", "second", "third", "fifth", "seventh", "ninth", "tenth", "eleventh", "susan", "rose", "sally", "perception", "gold");
    private final ImmutableList<String> LANG_SUBS = ImmutableList.of("ar", "bg", "ca", "zh", "cs", "da", "nl", "en", "et", "fi", "fr", "de", "el", "ht", "he", "hi", "mww", "hu", "id", "it", "ja", "ko", "lv", "lt", "ms", "no", "fa", "pl", "pt", "ro", "ru", "sk", "sl", "es", "sv", "th", "tr", "uk", "ur", "vi");
    private final List<String> PRESETS = new ArrayList<>();
    private final ImmutableList<String> REGION_SUBS = ImmutableList.of("entry", "exit");
    private final ImmutableList<String> SIEGE_SUBS = ImmutableList.of("enabled", "breeding", "growth", "butcher", "creeper", "healing", "texture", "true", "false");
    private final ImmutableList<String> SONICS = ImmutableList.of("mark_1", "mark_2", "mark_3", "mark_4", "eighth", "ninth", "ninth_open", "tenth", "tenth_open", "eleventh", "eleventh_open", "master", "sarah_jane", "river_song", "war", "twelfth");
    private final ImmutableList<String> TIPS_NEXT_SUBS = ImmutableList.of("FREE", "HIGHEST");
    private final ImmutableList<String> TIPS_SUBS = ImmutableList.of("400", "800", "1200", "1600");
    private final ImmutableList<String> TOWNY_SUBS = ImmutableList.of("none", "wilderness", "town", "nation");
    private final ImmutableList<String> USE_CLAY_SUBS = ImmutableList.of("WOOL", "TERRACOTTA", "CONCRETE");
    private final ImmutableList<String> VORTEX_SUBS = ImmutableList.of("kill", "teleport");
    private final List<String> WORLD_SUBS = new ArrayList<>();

    public TARDISConfigTabComplete(TARDIS plugin) {
        this.plugin = plugin;
        ROOT_SUBS = ImmutableList.copyOf(combineLists());
        if (plugin.isWorldGuardOnServer()) {
            FLAG_SUBS = ImmutableList.copyOf(TARDISWorldGuardFlag.getFLAG_LOOKUP().keySet());
        } else {
            FLAG_SUBS = ImmutableList.of("none", "build", "entry");
        }
        for (ChameleonPreset p : ChameleonPreset.values()) {
            PRESETS.add(p.toString());
        }
        for (Config c : Config.values()) {
            FILE_SUBS.add(c.toString());
        }
        plugin.getServer().getWorlds().forEach((w) -> WORLD_SUBS.add(w.getName()));
        CONFIG_SUBS.addAll(this.plugin.getConfig().getDefaultSection().getKeys(false));
        CONFIG_SUBS.remove("debug");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            return switch (sub) {
                case "include", "exclude" -> partial(lastArg, WORLD_SUBS);
                case "options" -> partial(lastArg, CONFIG_SUBS);
                case "crafting" -> partial(lastArg, CRAFTING_SUBS);
                case "provider" -> partial(lastArg, MAPPING_SUBS);
                case "respect_towny" -> partial(lastArg, TOWNY_SUBS);
                case "respect_worldguard" -> partial(lastArg, FLAG_SUBS);
                case "region_flag" -> partial(lastArg, REGION_SUBS);
                case "reload" -> partial(lastArg, FILE_SUBS);
                case "vortex_fall" -> partial(lastArg, VORTEX_SUBS);
                case "tips_next" -> partial(lastArg, TIPS_NEXT_SUBS);
                case "sign_colour" -> partial(lastArg, COLOURS);
                case "siege" -> partial(lastArg, SIEGE_SUBS);
                case "default_key" -> partial(lastArg, KEYS);
                case "default_preset" -> partial(lastArg, PRESETS);
                case "default_model" -> partial(lastArg, SONICS);
                case "database" -> partial(lastArg, DB_SUBS);
                case "language" -> partial(lastArg, LANG_SUBS);
                case "tips_limit" -> partial(lastArg, TIPS_SUBS);
                case "use_clay" -> partial(lastArg, USE_CLAY_SUBS);
                default -> partial(lastArg, BOOL_SUBS);
            };
        }
        return ImmutableList.of();
    }

    private List<String> combineLists() {
        List<String> newList = new ArrayList<>(
                plugin.getGeneralKeeper().getTardisConfigCommand().firstsStr.size()
                        + plugin.getGeneralKeeper().getTardisConfigCommand().firstsBool.size()
                        + plugin.getGeneralKeeper().getTardisConfigCommand().firstsInt.size()
                        + plugin.getGeneralKeeper().getTardisConfigCommand().firstsStrArtron.size()
                        + plugin.getGeneralKeeper().getTardisConfigCommand().firstsIntArtron.size()
        );
        newList.addAll(plugin.getGeneralKeeper().getTardisConfigCommand().firstsStr.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisConfigCommand().firstsBool.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisConfigCommand().firstsInt.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisConfigCommand().firstsStrArtron);
        newList.addAll(plugin.getGeneralKeeper().getTardisConfigCommand().firstsIntArtron);
        return newList;
    }
}
