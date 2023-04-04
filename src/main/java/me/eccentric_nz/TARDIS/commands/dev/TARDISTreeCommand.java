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

import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.FractalFence;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTreeCommand {

    private final TARDIS plugin;

    public TARDISTreeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean grow(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16);
            Block up = targetBlock.getRelative(BlockFace.UP);
            if (args.length == 1) {
                if (!targetBlock.getType().equals(Material.GRASS_BLOCK)) {
                    TARDISMessage.message(player, plugin.getPluginName() + "You must be targeting a grass block!");
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
                    if (!stem.isBlock() || !hat.isBlock() || !decor.isBlock() || !plugin.getTardisHelper().getTreeMatrials().contains(stem) || !plugin.getTardisHelper().getTreeMatrials().contains(hat) || !plugin.getTardisHelper().getTreeMatrials().contains(decor)) {
                        TARDISMessage.send(player, "ARG_NOT_BLOCK");
                        return true;
                    }
                    plugin.getTardisHelper().growTree(up.getLocation(), targetBlock.getType(), stem, hat, decor);
                    return true;
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(player, "MATERIAL_NOT_VALID");
                    return true;
                }
            } else {
                int which = TARDISNumberParsers.parseInt(args[1]);
                FractalFence.grow(targetBlock, which);
                return true;
            }
        } else {
            TARDISMessage.send(sender, "CMD_PLAYER");
            return true;
        }
    }
}
