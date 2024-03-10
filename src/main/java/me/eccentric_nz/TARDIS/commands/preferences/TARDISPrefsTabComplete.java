/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.universaltranslator.Language;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter for /tardisprefs
 */
public class TARDISPrefsTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = TARDISPrefsCommands.getRootArgs();
    private final ImmutableList<String> DIFF_SUBS = ImmutableList.of("easy", "hard");
    private final ImmutableList<String> ONOFF_SUBS = ImmutableList.of("on", "off");
    private final ImmutableList<String> HADS_SUBS = ImmutableList.of("DISPLACEMENT", "DISPERSAL");
    private final ImmutableList<String> HUM_SUBS = ImmutableList.of("alien", "atmosphere", "computer", "copper", "coral", "galaxy", "learning", "mind", "neon", "sleeping", "void", "random");
    private final ImmutableList<String> FLIGHT_SUBS = ImmutableList.of("normal", "regulator", "manual");
    private final ImmutableList<String> KEY_SUBS;
    private final List<String> LIGHT_SUBS = new ArrayList<>();
    private final ImmutableList<String> MAT_SUBS;
    private final List<String> LANGUAGE_SUBS = new ArrayList<>();

    public TARDISPrefsTabComplete(TARDIS plugin) {
        List<String> mats = new ArrayList<>();
        TARDISWalls.BLOCKS.forEach((key) -> mats.add(key.toString()));
        for (Language l : Language.values()) {
            LANGUAGE_SUBS.add(l.toString());
        }
        for (TardisLight tl : TardisLight.values()) {
            LIGHT_SUBS.add(tl.toString());
        }
        MAT_SUBS = ImmutableList.copyOf(mats);
        if (plugin.getConfig().getBoolean("travel.give_key") && !plugin.getConfig().getBoolean("allow.all_blocks")) {
            KEY_SUBS = ImmutableList.copyOf(plugin.getBlocksConfig().getStringList("keys"));
        } else {
            List<String> keys = new ArrayList<>();
            for (Material key : Material.values()) {
                if (!key.isBlock()) {
                    keys.add(key.toString());
                }
            }
            KEY_SUBS = ImmutableList.copyOf(keys);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            return switch (sub) {
                case "add", "remove" ->
                    null; // return null to default to online player name matching
                case "floor", "wall", "siege_floor", "siege_wall" ->
                    partial(lastArg, MAT_SUBS);
                case "key" ->
                    partial(lastArg, KEY_SUBS);
                case "language", "translate" ->
                    partial(lastArg, LANGUAGE_SUBS);
                case "flight" ->
                    partial(lastArg, FLIGHT_SUBS);
                case "difficulty" ->
                    partial(lastArg, DIFF_SUBS);
                case "hads_type" ->
                    partial(lastArg, HADS_SUBS);
                case "hum" ->
                    partial(lastArg, HUM_SUBS);
                case "lights" ->
                    partial(lastArg, LIGHT_SUBS);
                default ->
                    partial(lastArg, ONOFF_SUBS);
            };
        } else if (args.length == 3 && args[0].equalsIgnoreCase("translate")) {
            return partial(lastArg, LANGUAGE_SUBS);
        } else if (args.length == 4 && args[0].equalsIgnoreCase("translate")) {
            // return null to default to online player name matching
            return null;
        }
        return ImmutableList.of();
    }
}
