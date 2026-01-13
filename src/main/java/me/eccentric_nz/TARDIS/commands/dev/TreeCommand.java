/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.utility.FractalFence;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.architectural.tree.TreeBuilder;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 *
 * @author eccentric_nz
 */
public class TreeCommand {

    private final TARDIS plugin;

    public TreeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean grow(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16);
            Block up = targetBlock.getRelative(BlockFace.UP);
            if (args[1].equalsIgnoreCase("worldgen")) {
                if (!targetBlock.getType().equals(Material.GRASS_BLOCK)) {
                    plugin.getMessenger().message(player, TardisModule.TARDIS, "You must be targeting a grass block!");
                    return true;
                }
                plugin.getTardisHelper().growTree("random", up.getLocation());
                return true;
            }
            if (args.length == 5) {
                try {
                    Material stem = Material.valueOf(args[2].toUpperCase(Locale.ROOT));
                    Material hat = Material.valueOf(args[3].toUpperCase(Locale.ROOT));
                    Material decor = Material.valueOf(args[4].toUpperCase(Locale.ROOT));
                    if (!stem.isBlock() || !hat.isBlock() || !decor.isBlock() || !plugin.getTardisHelper().getTreeMaterials().contains(stem) || !plugin.getTardisHelper().getTreeMaterials().contains(hat) || !plugin.getTardisHelper().getTreeMaterials().contains(decor)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_NOT_BLOCK");
                        return true;
                    }
                    plugin.getTardisHelper().growTree(up.getLocation(), targetBlock.getType(), stem, hat, decor);
                    return true;
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
                    return true;
                }
            } else if (args[1].equalsIgnoreCase("delta")) {
                int which = TARDISNumberParsers.parseInt(args[2]);
                FractalFence.grow(targetBlock, which);
                return true;
            } else {
                new TreeBuilder().place(targetBlock.getRelative(BlockFace.UP));
                return true;
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
            return true;
        }
    }
}
