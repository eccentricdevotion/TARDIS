package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NetherPortalUtility {

    public static boolean o2n(TARDIS plugin, Player player) {
        int x, y, z, dx, dz;
        // get player coords
        Location l = player.getLocation();
        boolean overworld = !(l.getWorld().getEnvironment().equals(World.Environment.NETHER));
        x = l.getBlockX();
        y = l.getBlockY();
        z = l.getBlockZ();
        if ((y > 123) || (y < 1)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "O2N_Y");
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
        plugin.getMessenger().send(player, TardisModule.TARDIS, message, coords);
        return true;
    }

    public static boolean o2n(TARDIS plugin, CommandSender sender, int x, int y, int z, boolean overworld) {
        int dx, dz;
        if ((y > 123) || (y < 1)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "O2N_Y");
            return false;
        }
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
        plugin.getMessenger().send(sender, TardisModule.TARDIS, message, coords);
        return true;
    }
}
