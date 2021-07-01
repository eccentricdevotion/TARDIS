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
package me.eccentric_nz.tardis.commands.dev;

import com.google.common.collect.Sets;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advancement.TardisAdvancementFactory;
import me.eccentric_nz.tardis.bstats.ArsRoomCounts;
import me.eccentric_nz.tardis.builders.FractalFence;
import me.eccentric_nz.tardis.commands.TardisCommandHelper;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.Chunk;
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

/**
 * Command /tardisadmin [arguments].
 * <p>
 * The Lord President was the most powerful member of the Time Lord Council and had near absolute authority, and used a
 * link to the Matrix, a vast computer network containing the knowledge and experiences of all past generations of Time
 * Lords, to set Time Lord policy and remain alert to potential threats from lesser civilisations.
 *
 * @author eccentric_nz
 */
public class TardisDevCommand implements CommandExecutor {

    private final Set<String> firstsStr = Sets.newHashSet("add_regions", "advancements", "list", "set_biome", "stats", "tree");
    private final TardisPlugin plugin;

    public TardisDevCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisdev then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisdev")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    new TardisCommandHelper(plugin).getCommand("tardisadmin", sender);
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (!firstsStr.contains(first)) {
                    TardisMessage.send(sender, "ARG_NOT_VALID");
                    return false;
                }
                if (args.length == 1) {
                    if (first.equals("add_regions")) {
                        return new TardisAddRegionsCommand(plugin).doCheck(sender);
                    }
                    if (first.equals("stats")) {
                        ArsRoomCounts arsRoomCounts = new ArsRoomCounts(plugin);
                        for (Map.Entry<String, Integer> entry : arsRoomCounts.getRoomCounts().entrySet()) {
                            plugin.debug(entry.getKey() + ": " + entry.getValue());
                        }
                        plugin.debug(arsRoomCounts.getMedian());
                        return true;
                    }
                }
                if (first.equals("advancements")) {
                    TardisAdvancementFactory.checkAdvancement(args[1]);
                    return true;
                }
                if (first.equals("list")) {
                    return new TardisDevListCommand(plugin).listStuff(sender, args);
                }
                if (args.length < 2) {
                    TardisMessage.send(sender, "TOO_FEW_ARGS");
                    return false;
                }
                if (first.equals("set_biome")) {
                    if (sender instanceof Player p) {
                        Chunk chunk = p.getLocation().getChunk();
                        plugin.getTardisHelper().setCustomBiome(args[1], chunk);
                        return true;
                    }
                }
                if (first.equals("tree")) {
                    if (sender instanceof Player player) {
                        Block l = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation().getBlock();
                        int which = TardisNumberParsers.parseInt(args[1]);
                        FractalFence.grow(l, which);
                    }
                }
                return true;
            } else {
                TardisMessage.send(sender, "CMD_ADMIN");
                return false;
            }
        }
        return false;
    }
}