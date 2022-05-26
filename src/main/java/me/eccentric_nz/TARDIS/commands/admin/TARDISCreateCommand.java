package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISBuildData;
import me.eccentric_nz.TARDIS.builders.TARDISSeedBlockProcessor;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class TARDISCreateCommand {

    private final TARDIS plugin;

    public TARDISCreateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean buildTARDIS(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            // tadmin create eccentric_nz budget ORANGE_WOOL LIGHT_GRAY_WOOL
            if (args.length < 3) {
                return false;
            }
            // args[1] player
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                return true;
            }
            // args[2] schematic
            String seed = args[2].toUpperCase(Locale.ROOT);
            if (Consoles.getBY_NAMES().containsKey(seed) && !seed.equals("SMALL") && !seed.equals("MEDIUM") && !seed.equals("TALL") && !seed.equals("ARCHIVE")) {
                Schematic schematic = Consoles.getBY_NAMES().get(seed);
                Material wallMaterial = Material.ORANGE_WOOL;
                Material floorMaterial = Material.LIGHT_GRAY_WOOL;
                // args[3] wall
                if (args.length > 3) {
                    try {
                        wallMaterial = Material.valueOf(args[3].toUpperCase(Locale.ROOT));
                        if (!TARDISWalls.BLOCKS.contains(wallMaterial)) {
                            TARDISMessage.send(sender, "WALL_NOT_VALID", args[3]);
                            TARDISWalls.BLOCKS.forEach((w) -> sender.sendMessage(w.toString()));
                            return true;
                        }
                    } catch (IllegalArgumentException e) {
                        TARDISMessage.send(sender, "SEED_MAT_NOT_VALID", args[3]);
                        return true;
                    }
                }
                // args[4] floor
                if (args.length > 4) {
                    try {
                        floorMaterial = Material.valueOf(args[4].toUpperCase(Locale.ROOT));
                        if (!TARDISWalls.BLOCKS.contains(floorMaterial)) {
                            TARDISMessage.send(sender, "WALL_NOT_VALID", args[4]);
                            TARDISWalls.BLOCKS.forEach((w) -> sender.sendMessage(w.toString()));
                            return true;
                        }
                    } catch (IllegalArgumentException e) {
                        TARDISMessage.send(sender, "SEED_MAT_NOT_VALID", args[4]);
                        return true;
                    }
                }
                // get target location
                Location target = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                TARDISBuildData data = new TARDISBuildData();
                data.setSchematic(schematic);
                data.setWallType(wallMaterial);
                data.setFloorType(floorMaterial);
                // build a TARDIS
                return new TARDISSeedBlockProcessor(plugin).processBlock(data, target, player);
            } else {
                TARDISMessage.send(sender, "ARG_SEED");
                return true;
            }
        } else {
            TARDISMessage.send(sender, "CMD_PLAYER");
            return true;
        }
    }
}
