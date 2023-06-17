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
package me.eccentric_nz.TARDIS.commands.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TARDISMushroomFixerCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;

    public TARDISMushroomFixerCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    private final List<String> SUBS = Arrays.asList("red", "brown", "stem");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (cmd.getName().equalsIgnoreCase("tardismushroom")) {
                if (!player.hasPermission("tardis.mushroom")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                    return true;
                }
                if (args.length < 2) {
                    return false;
                }
                String type = args[0].toLowerCase(Locale.ROOT);
                if (!SUBS.contains(type)) {
                    return false;
                }
                int radius;
                try {
                    radius = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
                BlockData data;
                switch (type) {
                    case "red" -> data = Material.RED_MUSHROOM_BLOCK.createBlockData();
                    case "brown" -> data = Material.BROWN_MUSHROOM_BLOCK.createBlockData();
                    default -> data = Material.MUSHROOM_STEM.createBlockData();
                }
                Block block = player.getLocation().add(-radius, -radius, -radius).getBlock();
                World world = block.getWorld();
                int sx = block.getX();
                int sy = block.getY();
                int sz = block.getZ();
                int diameter = radius * 2;
                for (int x = sx; x < sx + diameter; x++) {
                    for (int y = sy; y < sy + diameter; y++) {
                        for (int z = sz; z < sz + diameter; z++) {
                            Block mushroom = world.getBlockAt(x, y, z);
                            Material material = mushroom.getType();
                            if (material == data.getMaterial()) {
                                mushroom.setBlockData(data, false);
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return partial(lastArg, SUBS);
        }
        return null;
    }
}
