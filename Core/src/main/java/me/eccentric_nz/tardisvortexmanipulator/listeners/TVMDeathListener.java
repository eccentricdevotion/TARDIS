/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TVMDeathListener implements Listener {

    private final TARDIS plugin;

    public TVMDeathListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFallIntoVoid(EntityDamageEvent event) {
        if (!event.getCause().equals(DamageCause.VOID) && !event.getCause().equals(DamageCause.SUFFOCATION)) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        UUID uuid = player.getUniqueId();
        if (plugin.getTvmSettings().getTravellers().contains(uuid)) {
            Location l = player.getLocation();
            World w = l.getWorld();
            if (!w.getEnvironment().equals(Environment.NETHER)) {
                plugin.debug("Highest block");
                double y = w.getHighestBlockYAt(l);
                l.setY(y);
            } else {
                l = w.getSpawnLocation();
                plugin.debug("Nether spawn location");
            }
            player.teleport(l);
            plugin.getTvmSettings().getTravellers().remove(uuid);
        }
    }
}
