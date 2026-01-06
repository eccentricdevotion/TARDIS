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

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Room;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class RoomsCommand {

    private final TARDIS plugin;

    public RoomsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean build(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            // get players current location
            Location location = player.getLocation();
            long delay = 0;
            // /tdev rooms [room]...
            for (int i = 1; i < args.length; i++) {
                try {
                    Room r = Room.valueOf(args[i].toUpperCase(Locale.ROOT));
                    JsonObject json = SchematicGZip.getObject(plugin, "rooms", r.toString().toLowerCase(Locale.ROOT), false);
                    Location l = location.clone().add(20 * i, 10, 0);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // paste schematic
                        RoomPaster paster = new RoomPaster(plugin, player, json, l);
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, paster, 1L, 3L);
                        paster.setTask(task);
                    }, delay);
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid room [" + args[i] + "] in room list!");
                }
                delay += 10;
            }
            return true;
        }
        return false;
    }
}
