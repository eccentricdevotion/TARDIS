/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TabCompleter for /tardis command
 */
public class TARDISTabComplete extends TARDISCompleter implements TabCompleter {

    private final TARDIS plugin;
    private final List<String> ROOT_SUBS = new ArrayList<>();
    private final List<String> ON_OFF_SUBS = ImmutableList.of("on", "off");
    private final List<String> DOOR_SUBS = ImmutableList.of("open", "close");
    private final List<String> ITEM_SUBS = ImmutableList.of("hand", "inventory");
    private final List<String> DIR_SUBS = ImmutableList.of("north", "west", "south", "east");
    private final List<String> LIST_SUBS = ImmutableList.of("companions", "saves", "areas", "rechargers");
    private final List<String> ARCHIVE_SUBS = ImmutableList.of("add", "description", "remove", "scan", "update", "y");
    private final List<String> EXTRA_SUBS = ImmutableList.of("blocks", "unlock");
    private final List<String> Y_SUBS = ImmutableList.of("64", "65");
    private final List<String> CONSOLE_SIZE_SUBS = ImmutableList.of("SMALL", "MEDIUM", "TALL", "MASSIVE");
    private final List<String> TRANSMAT_SUBS = ImmutableList.of("tp", "add", "remove", "update", "list");
    private final List<String> UPD_SUBS = new ArrayList<>();
    private final List<String> SEC_SUBS = new ArrayList<>();
    private final List<String> RECHARGER_SUBS;
    private final List<String> PRESET_SUBS = new ArrayList<>();

    public TARDISTabComplete(TARDIS plugin) {
        this.plugin = plugin;
        for (TardisCommand tc : TardisCommand.values()) {
            ROOT_SUBS.add(tc.toString());
        }
        RECHARGER_SUBS = getPublicRechargers();
        for (PRESET preset : PRESET.values()) {
            PRESET_SUBS.add(preset.toString());
        }
        for (Updateable u : Updateable.values()) {
            UPD_SUBS.add(u.getName());
            if (u.isSecondary()) {
                SEC_SUBS.add(u.getName());
            }
        }
        SEC_SUBS.add("remove");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase();
            switch (sub) {
                case "add":
                case "remove":
                case "rescue":
                case "save_player":
                    return null;
                case "archive":
                    return partial(lastArg, ARCHIVE_SUBS);
                case "bell":
                    return partial(lastArg, ON_OFF_SUBS);
                case "direction":
                    return partial(lastArg, DIR_SUBS);
                case "door":
                    return partial(lastArg, DOOR_SUBS);
                case "home":
                    return partial(lastArg, Collections.singletonList("set"));
                case "item":
                    return partial(lastArg, ITEM_SUBS);
                case "list":
                    return partial(lastArg, LIST_SUBS);
                case "room":
                case "jettison":
                    return partial(lastArg, plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false));
                case "transmat":
                    return partial(lastArg, TRANSMAT_SUBS);
                case "secondary":
                    return partial(lastArg, SEC_SUBS);
                case "update":
                    return partial(lastArg, UPD_SUBS);
                default:
                    break;
            }
        } else if (args.length == 3) {
            String sub = args[1].toLowerCase();
            if (sub.equals("rechargers")) {
                return partial(lastArg, RECHARGER_SUBS);
            }
            if (sub.equals("scan")) {
                return partial(lastArg, CONSOLE_SIZE_SUBS);
            }
            if (sub.equals("set")) {
                return partial(lastArg, PRESET_SUBS);
            }
            if (args[0].equals("update")) {
                return partial(lastArg, EXTRA_SUBS);
            }
        } else if (args.length == 4) {
            String sub = args[1].toLowerCase();
            if (sub.equals("add") || sub.equals("update")) {
                return partial(lastArg, CONSOLE_SIZE_SUBS);
            }
            if (sub.equals("y")) {
                return partial(lastArg, Y_SUBS);
            }
        }
        return ImmutableList.of();
    }

    private List<String> getPublicRechargers() {
        List<String> ret = new ArrayList<>();
        plugin.getConfig().getConfigurationSection("rechargers").getKeys(false).forEach((r) -> {
            if (!r.startsWith("rift")) {
                ret.add(r);
            }
        });
        return ret;
    }
}
