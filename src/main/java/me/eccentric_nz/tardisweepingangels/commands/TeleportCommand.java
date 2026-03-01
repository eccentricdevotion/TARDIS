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
package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TeleportCommand {

    private final TARDIS plugin;

    public TeleportCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void add(Player player) {
        Location location = player.getLocation();
        String tpLoc = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        List<String> list = plugin.getMonstersConfig().getStringList("angels.teleport_locations");
        list.add(tpLoc);
        plugin.getMonstersConfig().set("angels.teleport_locations", list);
        try {
            String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
            plugin.getMonstersConfig().save(new File(monstersPath));
            plugin.getMessenger().message(player, TardisModule.MONSTERS, "Added a weeping angels' teleport location successfully.");
        } catch (IOException io) {
            plugin.debug("Could not save monsters.yml, " + io.getMessage());
        }
    }

    public void replace(Player player) {
        Location location = player.getLocation();
        String tpLoc = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        List<String> list = List.of(tpLoc);
        plugin.getMonstersConfig().set("angels.teleport_locations", list);
        try {
            String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
            plugin.getMonstersConfig().save(new File(monstersPath));
            plugin.getMessenger().message(player, TardisModule.MONSTERS, "Added a weeping angels' teleport location successfully.");
        } catch (IOException io) {
            plugin.debug("Could not save monsters.yml, " + io.getMessage());
        }
    }

    public void toggle(CommandSender sender, boolean b) {
        plugin.getMonstersConfig().set("angels.teleport_to_location", b);
        try {
            String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
            plugin.getMonstersConfig().save(new File(monstersPath));
            plugin.getMessenger().message(sender, TardisModule.MONSTERS, "Set 'angels.teleport_to_location' to " + b);
        } catch (IOException io) {
            plugin.debug("Could not save monsters.yml, " + io.getMessage());
        }
    }
}
