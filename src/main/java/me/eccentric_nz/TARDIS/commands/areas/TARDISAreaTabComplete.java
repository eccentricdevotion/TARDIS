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
package me.eccentric_nz.TARDIS.commands.areas;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

/**
 * TabCompleter for /tardisarea
 */
public class TARDISAreaTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("start", "end", "parking", "remove", "show", "yard", "invisibility", "direction", "create", "add", "edit");
    private final List<String> INVISIBILTY_SUBS = new ArrayList<>();
    private final List<String> DIRECTION_SUBS = new ArrayList<>();

    public TARDISAreaTabComplete() {
        INVISIBILTY_SUBS.add("ALLOW");
        INVISIBILTY_SUBS.add("DENY");
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            INVISIBILTY_SUBS.add(preset.toString());
        }
        for (COMPASS compass : COMPASS.values()) {
            DIRECTION_SUBS.add(compass.toString());
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 3) {
            String sub = args[0];
            if (sub.equals("invisibility")) {
                return partial(lastArg, INVISIBILTY_SUBS);
            }
            if (sub.equals("direction")) {
                return partial(lastArg, DIRECTION_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
