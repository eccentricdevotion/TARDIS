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
package me.eccentric_nz.tardisweepingangels.commands;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.DyeColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter
 */
public class TabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ONOFF_SUBS = ImmutableList.of("on", "off");
    private final ImmutableList<String> TP_SUBS = ImmutableList.of("replace", "true", "false");
    private final ImmutableList<String> WORLD_SUBS;
    private final ImmutableList<String> MONSTER_SUBS;
    private final List<String> COLOUR_SUBS = new ArrayList<>();
    ImmutableList<String> CMD_SUBS = ImmutableList.of("spawn", "equip", "disguise", "kill", "count", "follow", "stay", "remove", "set", "give", "teleport");

    public TabComplete(TARDIS plugin) {
        List<String> tmp = new ArrayList<>();
        for (Monster m : Monster.values()) {
            tmp.add(m.toString());
        }
        MONSTER_SUBS = ImmutableList.copyOf(tmp);
        List<String> worlds = new ArrayList<>();
        plugin.getServer().getWorlds().forEach((w) -> worlds.add(w.getName()));
        WORLD_SUBS = ImmutableList.copyOf(worlds);
        for (DyeColor dye : DyeColor.values()) {
            COLOUR_SUBS.add(dye.toString());
        }
        COLOUR_SUBS.add("flying");
        COLOUR_SUBS.add("true");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1 -> {
                return partial(args[0], CMD_SUBS);
            }
            case 2 -> {
                if (args[0].equals("give")) {
                    return null;
                } else if (args[0].equals("teleport")) {
                    return partial(args[1], TP_SUBS);
                } else {
                    return partial(args[1], MONSTER_SUBS);
                }
            }
            case 3 -> {
                return switch (args[0]) {
                    case "disguise" -> partial(args[2], ONOFF_SUBS);
                    case "give" -> partial(args[2], MONSTER_SUBS);
                    case "follow" -> List.of("15");
                    case "spawn", "equip" -> partial(args[2], COLOUR_SUBS);
                    default -> partial(args[2], WORLD_SUBS);
                };
            }
            default -> {
            }
        }
        return ImmutableList.of();
    }
}
