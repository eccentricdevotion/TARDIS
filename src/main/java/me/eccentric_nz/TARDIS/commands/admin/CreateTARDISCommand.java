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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISBuildData;
import me.eccentric_nz.TARDIS.builders.utility.SeedBlockProcessor;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CreateTARDISCommand {

    private final TARDIS plugin;
    private final String wall;
    private final String floor;

    public CreateTARDISCommand(TARDIS plugin, String wall, String floor) {
        this.plugin = plugin;
        this.wall = wall;
        this.floor = floor;
    }

    public boolean buildTARDIS(CommandSender sender, Player player, String seed) {
        if (sender instanceof Player admin) {
            // tadmin create eccentric_nz BUDGET ORANGE_WOOL LIGHT_GRAY_WOOL

            if (Desktops.getBY_NAMES().containsKey(seed) && !seed.equals("SMALL") && !seed.equals("MEDIUM") && !seed.equals("TALL") && !seed.equals("ARCHIVE")) {
                Schematic schematic = Desktops.getBY_NAMES().get(seed);
                Material wallMaterial = Material.ORANGE_WOOL;
                Material floorMaterial = Material.LIGHT_GRAY_WOOL;
                // wall wall
                    try {
                        wallMaterial = Material.valueOf(wall.toUpperCase(Locale.ROOT));
                        if (!TARDISWalls.BLOCKS.contains(wallMaterial)) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WALL_NOT_VALID", wall);
                            TARDISWalls.BLOCKS.forEach((w) -> sender.sendMessage(w.toString()));
                            return true;
                        }
                    } catch (IllegalArgumentException e) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "SEED_MAT_NOT_VALID", wall);
                        return true;
                    }
                    try {
                        floorMaterial = Material.valueOf(floor);
                        if (!TARDISWalls.BLOCKS.contains(floorMaterial)) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "WALL_NOT_VALID", floor);
                            TARDISWalls.BLOCKS.forEach((w) -> sender.sendMessage(w.toString()));
                            return true;
                        }
                    } catch (IllegalArgumentException e) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "SEED_MAT_NOT_VALID", floor);
                        return true;
                    }
                // get target location
                Location target = admin.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                TARDISBuildData data = new TARDISBuildData();
                data.setSchematic(schematic);
                data.setWallType(wallMaterial);
                data.setFloorType(floorMaterial);
                // build a TARDIS
                return new SeedBlockProcessor(plugin).processBlock(data, target, player);
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_SEED");
                return true;
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
            return true;
        }
    }
}
