/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.GallifeyStructureUtility;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.SiluriaStructureUtility;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.SkaroStructureUtility;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter for /tardisschematic
 */
public class TARDISSchematicTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("load", "paste", "save", "clear", "replace", "convert", "remove", "flowers", "fixliquid");
    private final List<String> LOAD_SUBS = ImmutableList.of("console", "room", "structure", "user");
    private final List<String> LIQUID_SUBS = ImmutableList.of("water", "lava");
    private final List<String> CONSOLE_SUBS = new ArrayList<>(Consoles.getBY_PERMS().keySet());
    private final List<String> ROOM_SUBS = new ArrayList<>();
    private final List<String> STRUCTURE_SUBS = new ArrayList<>();
    private final List<String> FILE_SUBS = new ArrayList<>();
    private final List<String> MAT_SUBS = new ArrayList<>();
    private final List<String> LIGHT_SUBS = new ArrayList<>();

    public TARDISSchematicTabComplete(File userDir) {
        if (userDir.exists()) {
            for (String f : userDir.list()) {
                if (f.endsWith(".tschm")) {
                    FILE_SUBS.add(f.substring(0, f.length() - 6));
                }
            }
        }
        for (String r : TARDIS.plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            ROOM_SUBS.add(r.toLowerCase());
        }
        for (String g : GallifeyStructureUtility.structures) {
            STRUCTURE_SUBS.add("gallifrey_" + g);
        }
        for (String s : SiluriaStructureUtility.structures) {
            STRUCTURE_SUBS.add("siluria_" + s);
        }
        for (String d : SkaroStructureUtility.structures) {
            STRUCTURE_SUBS.add("dalek_" + d);
        }
        for (Material m : Material.values()) {
            if (m.isBlock()) {
                MAT_SUBS.add(m.toString());
            }
        }
        for (TardisLight l : TardisLight.values()) {
            LIGHT_SUBS.add(l.toString());
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("load")) {
            return partial(args[1], LOAD_SUBS);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("paste")) {
            return ImmutableList.of("no_air");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("replace")) {
            return partial(args[1], MAT_SUBS);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("convert")) {
            return partial(args[1], LIGHT_SUBS);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("liquid")) {
            return partial(args[1], LIQUID_SUBS);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("load")) {
            switch (args[1].toLowerCase()) {
                case "console" -> {
                    return partial(args[2], CONSOLE_SUBS);
                }
                case "room" -> {
                    return partial(args[2], ROOM_SUBS);
                }
                case "structure" -> {
                    return partial(args[2], STRUCTURE_SUBS);
                }
                default -> {
                    return partial(args[2], FILE_SUBS);
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("replace")) {
            return partial(args[2], MAT_SUBS);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("convert")) {
            return ImmutableList.of("SEA_LANTERN", "REDSTONE_LAMP");
        }
        return ImmutableList.of();
    }
}
