package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class GravityCommand {

    private final TARDIS plugin;
    private final HashMap<String, Double> gravityDirection = new HashMap<>();

    public GravityCommand(TARDIS plugin) {
        this.plugin = plugin;
        gravityDirection.put("down", 0D);
        gravityDirection.put("up", 1D);
        gravityDirection.put("north", 2D);
        gravityDirection.put("west", 3D);
        gravityDirection.put("south", 4D);
        gravityDirection.put("east", 5D);

    }

    public void make(Player player, String direction, double distance, double velocity) {
        if (!plugin.getConfig().getBoolean("allow.external_gravity")) {
            // check they are still in the TARDIS world
            if (!plugin.getUtils().inTARDISWorld(player)) {
                String mess_stub = (player.getLocation().getWorld().getName().toUpperCase(Locale.ROOT).contains("TARDIS_WORLD_")) ? "GRAVITY_OWN_WORLD" : "GRAVITY_A_WORLD";
                plugin.getMessenger().send(player, TardisModule.TARDIS, mess_stub);
                return;
            }
        }
        if (velocity > plugin.getConfig().getDouble("growth.gravity_max_velocity")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "GRAVITY_FAST");
            return;
        }
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getGravity().put(uuid, new Double[]{gravityDirection.get(direction), distance, velocity});
        plugin.getMessenger().send(player, TardisModule.TARDIS, "GRAVITY_CLICK_SAVE");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getGravity().remove(uuid), 1200L);

    }

    public void remove(Player player) {
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getGravity().put(uuid, new Double[]{6D});
        plugin.getMessenger().send(player, TardisModule.TARDIS, "GRAVITY_CLICK_REMOVE");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getGravity().remove(uuid), 1200L);
    }
}
