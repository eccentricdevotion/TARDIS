/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Inspired by Nether Portal Calculator v1.0 by D3Phoenix http://ilurker.rooms.cwal.net/portal.html
 * <p>
 * Choose a location for a portal in the OVERWORLD and, without lighting it, build the frame. Walk into your portal
 * frame as if you were going to use it, and run the command /tnp. You will be messaged the coordinates for placing your
 * NETHER-side portal. Light your portal and go to the NETHER. Go to the calculated coordinates in the NETHER using F3.
 * Destroy and replace the block below your feet at these coordinates with obsidian. Turn your character until the F3
 * Facing (F) number from earlier matches up. This is the direction you will be facing when exiting the portal. Place a
 * second obsidian block in the floor either to your left or right (it doesn't matter). These two obsidian will form the
 * base of your portal. Build a walkway from the portal base at least a few blocks in the matching "F" number direction
 * to ensure a smooth transition through the portal. (This ensures that you won't be staring at a wall after going
 * through a portal.) Complete the NETHER portal frame and light it. Disable or destroy the NETHER portal that the game
 * spawned for you when you first entered the NETHER. Exit the NETHER through the new portal you just built. If you did
 * everything right, you should have a pair of perfectly linked NETHER portals. You can repeat this process as many
 * times as you like.
 *
 * @author eccentric_nz
 */
public class TARDISNetherPortalCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISNetherPortalCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisnetherportal")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                // must provide coords
                if (args.length < 4) {
                    new TARDISCommandHelper(plugin).getCommand("tardisnetherportal", sender);
                    return true;
                }
                int x = TARDISNumberParsers.parseInt(args[0]);
                int y = TARDISNumberParsers.parseInt(args[1]);
                int z = TARDISNumberParsers.parseInt(args[2]);
                return o2n(sender, x, y, z, args[3].equalsIgnoreCase("overworld"));
            } else {
                // get player's coords and environment
                return o2n(player);
            }
        }
        return false;
    }

    private boolean o2n(Player player) {
        int x, y, z, dx, dz;
        // get player coords
        Location l = player.getLocation();
        boolean overworld = !(l.getWorld().getEnvironment().equals(Environment.NETHER));
        x = l.getBlockX();
        y = l.getBlockY();
        z = l.getBlockZ();
        if ((y > 123) || (y < 1)) {
            TARDISMessage.send(player, "O2N_Y");
            return false;
        }
        // get player direction
        String d = TARDISStaticUtils.getPlayersDirection(player, false);
        String message = (overworld) ? "O2N_COORDS_N" : "O2N_COORDS_O";
        // get destination coords
        if (overworld) {
            dx = x / 8;
            dz = z / 8;
        } else {
            dx = x * 8;
            dz = z * 8;
        }
        String coords = "X: " + dx + ", " + "Y: " + y + ", " + "Z: " + dz + ", facing " + d;
        TARDISMessage.send(player, message, coords);
        return true;
    }

    private boolean o2n(CommandSender sender, int x, int y, int z, boolean overworld) {
        int dx, dz;
        if ((y > 123) || (y < 1)) {
            TARDISMessage.send(sender, "O2N_Y");
            return false;
        }
        // get player direction
        String message = (overworld) ? "O2N_COORDS_N" : "O2N_COORDS_O";
        // get destination coords
        if (overworld) {
            dx = x / 8;
            dz = z / 8;
        } else {
            dx = x * 8;
            dz = z * 8;
        }
        String coords = "X: " + dx + ", " + "Y: " + y + ", " + "Z: " + dz;
        TARDISMessage.send(sender, message, coords);
        return true;
    }
}
