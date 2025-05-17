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
package me.eccentric_nz.TARDIS.commands.give;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.*;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

/**
 * TabCompleter for /tardisgive
 */
public class TARDISGiveTabComplete extends TARDISCompleter implements TabCompleter {

    private static final List<String> BLUEPRINT_SUBS = new ArrayList<>();
    private final Set<String> GIVE_SUBS = new HashSet<>();
    private final ImmutableList<String> GIVE_KNOWLEDGE = ImmutableList.of("knowledge", "1", "2", "64");
    private final ImmutableList<String> KIT_SUBS;
    private final List<String> SEED_SUBS = new ArrayList<>();
    private final List<String> SYSUPS_SUBS = new ArrayList<>();
    private final List<String> MAT_SUBS = new ArrayList<>();

    public TARDISGiveTabComplete(TARDIS plugin) {
        GIVE_SUBS.add("artron");
        GIVE_SUBS.add("blueprint");
        GIVE_SUBS.add("kit");
        GIVE_SUBS.add("recipes");
        GIVE_SUBS.add("seed");
        GIVE_SUBS.add("system-upgrade");
        GIVE_SUBS.add("tachyon");
        GIVE_SUBS.add("acid-bucket");
        GIVE_SUBS.add("rust-bucket");
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() != RecipeCategory.SONIC_UPGRADES && recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE) {
                GIVE_SUBS.add(recipeItem.toTabCompletionString());
            }
        }
        for (String r : TARDIS.plugin.getCustomDoorsConfig().getKeys(false)) {
            GIVE_SUBS.add("door-" + r.toLowerCase(Locale.ROOT));
        }
        for (String r : TARDIS.plugin.getCustomRotorsConfig().getKeys(false)) {
            GIVE_SUBS.add("time-rotor-" + r.toLowerCase(Locale.ROOT));
        }
        Set<String> kits = plugin.getKitsConfig().getConfigurationSection("kits").getKeys(false);
        KIT_SUBS = ImmutableList.copyOf(kits);
        for (BlueprintBase base : BlueprintBase.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_BASE_" + base.toString());
        }
        for (BlueprintConsole console : BlueprintConsole.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_CONSOLE_" + console.toString());
        }
        for (BlueprintFeature feature : BlueprintFeature.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_FEATURE_" + feature.toString());
        }
        for (BlueprintPreset preset : BlueprintPreset.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_PRESET_" + preset.toString());
        }
        for (BlueprintRoom room : BlueprintRoom.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_ROOM_" + room.toString());
        }
        for (BlueprintSonic sonic : BlueprintSonic.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_SONIC_" + sonic.toString());
        }
        for (BlueprintTravel travel : BlueprintTravel.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_TRAVEL_" + travel.toString());
        }
        for (String seed : Consoles.getBY_NAMES().keySet()) {
            if (!seed.equals("SMALL") && !seed.equals("MEDIUM") && !seed.equals("TALL") && !seed.equals("ARCHIVE")) {
                SEED_SUBS.add(seed);
            }
        }
        for (SystemTree st: SystemTree.values()) {
            SYSUPS_SUBS.add(st.toString());
        }
        TARDISWalls.BLOCKS.forEach((m) -> MAT_SUBS.add(m.toString()));
    }

    public static List<String> getBlueprints() {
        return BLUEPRINT_SUBS;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return null;
        } else if (args.length == 2) {
            return partial(lastArg, GIVE_SUBS);
        } else if (args.length == 3) {
            String sub = args[1];
            return switch (sub) {
                case "kit" -> partial(lastArg, KIT_SUBS);
                case "blueprint" -> partial(lastArg, BLUEPRINT_SUBS);
                case "seed" -> partial(lastArg, SEED_SUBS);
                case "system-upgrade" -> partial(lastArg, SYSUPS_SUBS);
                default -> partial(lastArg, GIVE_KNOWLEDGE);
            };
        } else if (args[1].equalsIgnoreCase("seed")) {
            return partial(lastArg, MAT_SUBS);
        }
        return ImmutableList.of();
    }
}
