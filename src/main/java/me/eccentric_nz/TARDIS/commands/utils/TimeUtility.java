package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Time;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TimeUtility {

    public static void setTime(TARDIS plugin, int ticks, Player player) {
        World world = getWorld(plugin, player);
        if (world == null) {
            // can't change time in TARDIS world
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TIME_TARDIS");
            return;
        }
        world.setTime(ticks);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TIME_SET", String.format("%s", ticks), world.getName());
    }

    public static void setTime(TARDIS plugin, String t, Player player) {
        World world = getWorld(plugin, player);
        if (world == null) {
            // can't change time in TARDIS world
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TIME_TARDIS");
            return;
        }
        Time time = Time.getByName().get(t);
        if (time != null) {
            long ticks = time.getTicks();
            world.setTime(ticks);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TIME_SET", String.format("%s", ticks), world.getName());
        }
    }

    public static World getWorld(TARDIS plugin, Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();
        if (plugin.getUtils().inTARDISWorld(player)) {
            // get TARDIS player is in
            int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player);
            // get current TARDIS location
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (rsc.resultSet()) {
                world = rsc.getCurrent().location().getWorld();
            } else {
                return null;
            }
        }
        return world;
    }
}
