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
package me.eccentric_nz.tardis.junk;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TardisJunkCommands implements CommandExecutor {

    private final TardisPlugin plugin;
    private final List<String> firstArgs = new ArrayList<>();

    public TardisJunkCommands(TardisPlugin plugin) {
        this.plugin = plugin;
        firstArgs.add("create");
        firstArgs.add("delete");
        firstArgs.add("find");
        firstArgs.add("floor");
        firstArgs.add("return");
        firstArgs.add("time");
        firstArgs.add("wall");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisjunk then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisjunk")) {
            if (args.length == 0) {
                // find Junk TARDIS
                return new TardisJunkFind(plugin).find(sender);
            }
            String first = args[0].toLowerCase(Locale.ENGLISH);
            if (args.length == 1 && firstArgs.contains(first)) {
                if (first.equals("create")) {
                    Player p = null;
                    if (sender instanceof Player) {
                        p = (Player) sender;
                    }
                    if (p == null) {
                        TardisMessage.send(sender, "CMD_PLAYER");
                        return false;
                    }
                    return new TardisJunkCreator(plugin, p).createJunkTARDIS();
                }
                if (first.equals("find")) {
                    return new TardisJunkFind(plugin).find(sender);
                }
                if (first.equals("time")) {
                    return new TardisJunkTime(plugin).elapsed(sender);
                }
                if (first.equals("return")) {
                    return new TardisJunkReturn(plugin).recall(sender);
                }
                if (first.equals("delete")) {
                    return new TardisJunkDelete(plugin).delete(sender);
                }
                return true;
            }
            if (args.length == 2 && first.equals("floor") || first.equals("wall")) {
                return new TardisJunkFloorWall(plugin).setJunkWallOrFloor(sender, args);
            }
        }
        return false;
    }
}
