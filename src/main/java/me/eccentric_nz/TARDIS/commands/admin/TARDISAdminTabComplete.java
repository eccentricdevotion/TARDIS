/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardFlag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

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
    private final ImmutableList<String> USE_CLAY_SUBS = ImmutableList.of("WOOL", "TERRACOTTA", "CONCRETE");
    private final ImmutableList<String> SIEGE_SUBS = ImmutableList.of("enabled", "breeding", "growth", "butcher", "creeper", "healing", "texture", "true", "false");
    private final ImmutableList<String> SONICS = ImmutableList.of("mark_1", "mark_2", "mark_3", "mark_4", "eighth", "ninth", "ninth_open", "tenth", "tenth_open", "eleventh", "eleventh_open", "master", "sarah_jane", "river_song", "war", "twelfth");
    private final ImmutableList<String> TIPS_SUBS = ImmutableList.of("400", "800", "1200", "1600");
    private final ImmutableList<String> TOWNY_SUBS = ImmutableList.of("none", "wilderness", "town", "nation");
    private final ImmutableList<String> VORTEX_SUBS = ImmutableList.of("kill", "teleport");
    private final ImmutableList<String> LIST_SUBS = ImmutableList.of("abandoned", "portals", "save");
    private final ImmutableList<String> COMPASS_SUBS = ImmutableList.of("NORTH", "EAST", "SOUTH", "WEST");
    private final ImmutableList<String> WORLD_SUBS;
    private final ImmutableList<String> SEED_SUBS;
    private final ImmutableList<String> ENTITY_SUBS;
    private final ImmutableList<String> MUSHROOM_SUBS = ImmutableList.of("the_moment", "siege_cube", "tardis_blue", "tardis_white", "tardis_orange", "tardis_magenta", "tardis_light_blue", "tardis_yellow", "tardis_lime", "tardis_pink", "tardis_gray", "tardis_light_gray", "tardis_cyan", "tardis_purple", "tardis_brown", "tardis_green", "tardis_red", "tardis_black", "tardis_blue_south", "tardis_white_south", "tardis_orange_south", "tardis_magenta_south", "tardis_light_blue_south", "tardis_yellow_south", "tardis_lime_south", "tardis_pink_south", "tardis_gray_south", "tardis_light_gray_south", "tardis_cyan_south", "tardis_purple_south", "tardis_brown_south", "tardis_green_south", "tardis_red_south", "tardis_black_south", "tardis_blue_west", "tardis_white_west", "tardis_orange_west", "tardis_magenta_west", "tardis_light_blue_west", "tardis_yellow_west", "tardis_lime_west", "tardis_pink_west", "tardis_gray_west", "tardis_light_gray_west", "tardis_cyan_west", "tardis_purple_west", "tardis_brown_west", "tardis_green_west", "tardis_red_west", "tardis_black_west", "tardis_blue_north", "tardis_white_north", "tardis_orange_north", "tardis_magenta_north", "tardis_light_blue_north", "tardis_yellow_north", "tardis_lime_north", "tardis_pink_north", "tardis_gray_north", "tardis_light_gray_north", "tardis_cyan_north", "tardis_purple_north", "tardis_brown_north", "tardis_green_north", "tardis_red_north", "tardis_black_north", "ars", "bigger", "budget", "coral", "deluxe", "eleventh", "ender", "plank", "pyramid", "redstone", "steampunk", "thirteenth", "factory", "tom", "twelfth", "war", "small", "medium", "tall", "legacy_bigger", "legacy_budget", "legacy_deluxe", "legacy_eleventh", "legacy_redstone", "pandorica");

    public TARDISAdminTabComplete(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.isWorldGuardOnServer()) {
            FLAG_SUBS = ImmutableList.copyOf(TARDISWorldGuardFlag.getFLAG_LOOKUP().keySet());
        } else {
            FLAG_SUBS = ImmutableList.of("none", "build", "entry");
        }
        List<String> tmpPresets = new ArrayList<>();
        for (PRESET p : PRESET.values()) {
            tmpPresets.add(p.toString());
        }
        PRESETS = ImmutableList.copyOf(tmpPresets);
        ROOT_SUBS = ImmutableList.copyOf(combineLists());
        List<String> worlds = new ArrayList<>();
        plugin.getServer().getWorlds().forEach((w) -> worlds.add(w.getName()));
        WORLD_SUBS = ImmutableList.copyOf(worlds);
        SEED_SUBS = ImmutableList.copyOf(CONSOLES.getBY_NAMES().keySet());
        List<String> tmpEntities = new ArrayList<>();
        for (EntityType e : EntityType.values()) {
            if (e.getEntityClass() != null && Creature.class.isAssignableFrom(e.getEntityClass())) {
                tmpEntities.add(e.toString());
            }
        }
        ENTITY_SUBS = ImmutableList.copyOf(tmpEntities);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0];
            if (sub.equals("include") || sub.equals("exclude")) {
                return partial(lastArg, WORLD_SUBS);
            }
            if (sub.equals("config")) {
                return partial(lastArg, CONFIG_SUBS);
            }
            if (sub.equals("disguise")) {
                return partial(lastArg, ENTITY_SUBS);
            }
            if (sub.equals("difficulty")) {
                return partial(lastArg, DIFFICULTY_SUBS);
            }
            if (sub.equals("list")) {
                return partial(lastArg, LIST_SUBS);
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
            if (sub.equals("spawn_abandoned")) {
                return partial(lastArg, SEED_SUBS);
            }
            if (sub.equals("use_clay")) {
                return partial(lastArg, USE_CLAY_SUBS);
            }
            if (sub.equals("mushroom")) {
                return partial(lastArg, MUSHROOM_SUBS);
            }
            if (sub.equals("delete") || sub.equals("enter") || sub.equals("purge") || sub.equals("desiege") || sub.equals("repair") || sub.equals("set_size") || sub.equals("undisguise")) {
                // return null to default to online player name matching
                return null;
            } else {
                return partial(lastArg, BOOL_SUBS);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("spawn_abandoned")) {
                return partial(lastArg, PRESETS);
            } else if (args[0].equalsIgnoreCase("set_size")) {
                return partial(lastArg, SEED_SUBS);
            } else if (args[0].equalsIgnoreCase("disguise")) {
                return null;
            } else {
                return partial(lastArg, BOOL_SUBS);
            }
        } else if (args.length == 4) {
            return partial(lastArg, COMPASS_SUBS);
        } else if (args.length == 5) {
            return partial(lastArg, WORLD_SUBS);
        }
        return ImmutableList.of();
    }

    private List<String> combineLists() {
        List<String> newList = new ArrayList<>(plugin.getGeneralKeeper().getTardisAdminCommand().firstsStr.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsBool.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsInt.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsStrArtron.size() + plugin.getGeneralKeeper().getTardisAdminCommand().firstsIntArtron.size());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsStr.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsBool.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsInt.keySet());
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsStrArtron);
        newList.addAll(plugin.getGeneralKeeper().getTardisAdminCommand().firstsIntArtron);
        return newList;
    }
}
