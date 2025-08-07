/*
 * Copyright (C) 2025 eccentric_nz
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
import java.util.Locale;

public class TeleportCommand {

    private final TARDIS plugin;

    public TeleportCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean add(CommandSender sender, String[] args) {
        String message = "Added a weeping angels' teleport location successfully.";
        String a = (args.length > 1) ? args[1].toLowerCase(Locale.ROOT) : "";
        if (a.equals("true") || a.equals("false")) {
            message = "Set 'angels.teleport_to_location' to " + a;
            plugin.getMonstersConfig().set("angels.teleport_to_location", Boolean.valueOf(a));
        } else if (sender instanceof Player player) {
            Location location = player.getLocation();
            String tpLoc = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
            List<String> list;
            if (a.equals("replace")) {
                list = List.of(tpLoc);
            } else {
                list = plugin.getMonstersConfig().getStringList("angels.teleport_locations");
                list.add(tpLoc);
            }
            plugin.getMonstersConfig().set("angels.teleport_locations", list);
        }
        try {
            String monstersPath = plugin.getDataFolder() + File.separator + "monsters.yml";
            plugin.getMonstersConfig().save(new File(monstersPath));
            plugin.getMessenger().message(sender, TardisModule.MONSTERS, message);
        } catch (IOException io) {
            plugin.debug("Could not save monsters.yml, " + io.getMessage());
        }
        return true;
    }
}
