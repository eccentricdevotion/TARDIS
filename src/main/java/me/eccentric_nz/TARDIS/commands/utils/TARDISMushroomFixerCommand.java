package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TARDISMushroomFixerCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final List<String> SUBS = Arrays.asList("red", "brown", "stem");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (cmd.getName().equalsIgnoreCase("tardismushroom")) {
                if (!player.hasPermission("tardis.mushroom")) {
                    TARDISMessage.send(player, "NO_PERMS");
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
                    case "red" -> {
                        data = Material.RED_MUSHROOM_BLOCK.createBlockData();
                    }
                    case "brown" -> {
                        data = Material.BROWN_MUSHROOM_BLOCK.createBlockData();
                    }
                    default -> {
                        data = Material.MUSHROOM_STEM.createBlockData();
                    }
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
