/*
 * Copyright (C) 2022 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.bStats.ARSRoomCounts;
import me.eccentric_nz.TARDIS.builders.FractalFence;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.Pluraliser;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Command /tardisadmin [arguments].
 * <p>
 * The Lord President was the most powerful member of the Time Lord Council and had near absolute authority, and used a
 * link to the Matrix, a vast computer network containing the knowledge and experiences of all past generations of Time
 * Lords, to set Time Lord policy and remain alert to potential threats from lesser civilisations.
 *
 * @author eccentric_nz
 */
public class TARDISDevCommand implements CommandExecutor {

    private final Set<String> firstsStr = Sets.newHashSet("add_regions", "advancements", "list", "stats", "tree", "plurals", "chunky");
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
                    if (sender instanceof Player player) {
                        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16);
                        Block up = targetBlock.getRelative(BlockFace.UP);
                        if (args.length == 1) {
                            if (!targetBlock.getType().equals(Material.GRASS_BLOCK)) {
                                TARDISMessage.message(sender, plugin.getPluginName() + "You must be targeting a grass block!");
                                return true;
                            }
                            plugin.getTardisHelper().growTree("random", up.getLocation());
                            return true;
                        }
                        if (args.length == 4) {
                            try {
                                Material stem = Material.valueOf(args[1].toUpperCase(Locale.ROOT));
                                Material hat = Material.valueOf(args[2].toUpperCase(Locale.ROOT));
                                Material decor = Material.valueOf(args[3].toUpperCase(Locale.ROOT));
                                if (!stem.isBlock() || !hat.isBlock() || !decor.isBlock() ||
                                        !plugin.getTardisHelper().getTreeMatrials().contains(stem) ||
                                        !plugin.getTardisHelper().getTreeMatrials().contains(hat) ||
                                        !plugin.getTardisHelper().getTreeMatrials().contains(decor)) {
                                    TARDISMessage.send(sender, "ARG_NOT_BLOCK");
                                    return true;
                                }
                                plugin.getTardisHelper().growTree(up.getLocation(), targetBlock.getType(), stem, hat, decor);
                                return true;
                            } catch (IllegalArgumentException e) {
                                TARDISMessage.send(sender, "MATERIAL_NOT_VALID");
                                return true;
                            }
                        } else {
                            int which = TARDISNumberParsers.parseInt(args[1]);
                            FractalFence.grow(targetBlock, which);
                        }
                    }
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
                return true;
            } else {
                TARDISMessage.send(sender, "CMD_ADMIN");
                return false;
            }
        }
        return false;
    }
}
