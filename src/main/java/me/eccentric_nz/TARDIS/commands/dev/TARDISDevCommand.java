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
package me.eccentric_nz.TARDIS.commands.dev;

import com.google.common.collect.Sets;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.bStats.ARSRoomCounts;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.monitor.MonitorSnapshot;
import me.eccentric_nz.TARDIS.utility.Pluraliser;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

/**
 * Command /tardisadmin [arguments].
 * <p>
 * The Lord President was the most powerful member of the Time Lord Council and
 * had near absolute authority, and used a link to the Matrix, a vast computer
 * network containing the knowledge and experiences of all past generations of
 * Time Lords, to set Time Lord policy and remain alert to potential threats
 * from lesser civilisations.
 *
 * @author eccentric_nz
 */
public class TARDISDevCommand implements CommandExecutor {

    private final Set<String> firstsStr = Sets.newHashSet("add_regions", "advancements", "chunky", "list", "plurals", "stats", "tree", "snapshot", "displayitem");
    private final TARDIS plugin;

    public TARDISDevCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisdev then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisdev")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    new TARDISCommandHelper(plugin).getCommand("tardisadmin", sender);
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (!firstsStr.contains(first)) {
                    TARDISMessage.send(sender, "ARG_NOT_VALID");
                    return false;
                }
                if (args.length == 1) {
                    if (first.equals("add_regions")) {
                        return new TARDISAddRegionsCommand(plugin).doCheck(sender);
                    }
                    if (first.equals("stats")) {
                        ARSRoomCounts arsRoomCounts = new ARSRoomCounts(plugin);
                        for (Map.Entry<String, Integer> entry : arsRoomCounts.getRoomCounts().entrySet()) {
                            plugin.debug(entry.getKey() + ": " + entry.getValue());
                        }
                        plugin.debug(arsRoomCounts.getMedian());
                        return true;
                    }
                }
                if (first.equals("advancements")) {
                    TARDISAchievementFactory.checkAdvancement(args[1]);
                    return true;
                }
                if (first.equals("list")) {
                    return new TARDISDevListCommand(plugin).listStuff(sender, args);
                }
                if (first.equals("tree")) {
                    return new TARDISTreeCommand(plugin).grow(sender, args);
                }
                if (first.equals("plurals")) {
                    for (Material m : Material.values()) {
                        String str = m.toString().toLowerCase(Locale.ROOT).replace("_", " ");
                        plugin.getLogger().log(Level.INFO, str + " --> " + Pluraliser.pluralise(str));
                    }
                }
                if (first.equals("chunky")) {
                    if (!plugin.getPM().isPluginEnabled("Chunky")) {
                        plugin.getLogger().log(Level.WARNING, "Chunky plugin is not enabled!");
                        return true;
                    }
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky world " + args[1]);
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky radius 250");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky spawn");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky start");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "chunky confirm");
                    return true;
                }
                if (first.equals("snapshot")) {
                    if (sender instanceof Player player) {
                        if (args[1].equals("c")) {
                            player.performCommand("minecraft:clear @s minecraft:filled_map");
                        } else {
                            new MonitorSnapshot(plugin).get(args[1].equals("in"), player);
                        }
                    }
                }
                if (first.equals("displayitem")) {
                    if (sender instanceof Player player) {
                        return new TARDISDisplayItemCommand(plugin).display(player, args);
                    } else {
                        TARDISMessage.send(sender, "CMD_PLAYER");
                        return true;
                    }
                }
            } else {
                TARDISMessage.send(sender, "CMD_ADMIN");
                return false;
            }
        }
        return false;
    }
}
