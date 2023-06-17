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
package me.eccentric_nz.TARDIS.display;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TARDISDisplayCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> SUBS = new ArrayList<>();

    public TARDISDisplayCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (TARDISDisplayType dt : TARDISDisplayType.values()) {
            SUBS.add(dt.toString());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player p) {
            UUID uuid = p.getUniqueId();
            if (args.length == 0) {
                if (p.hasPermission("tardis.display")) {
                    if (plugin.getTrackerKeeper().getDisplay().containsKey(uuid)) {
                        plugin.getTrackerKeeper().getDisplay().remove(uuid);
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "DISPLAY_DISABLED");
                    } else {
                        plugin.getTrackerKeeper().getDisplay().put(uuid, TARDISDisplayType.ALL);
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "DISPLAY_ENABLED");
                    }
                } else {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "DISPLAY_PERMISSION");
                }
            }
            if (args.length == 1) {
                try {
                    TARDISDisplayType displayType = TARDISDisplayType.valueOf(args[0].toUpperCase());
                    plugin.getTrackerKeeper().getDisplay().put(uuid, displayType);
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "DISPLAY_ENABLED");
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "DISPLAY_INVALID");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], SUBS);
        }
        return ImmutableList.of();
    }
}
