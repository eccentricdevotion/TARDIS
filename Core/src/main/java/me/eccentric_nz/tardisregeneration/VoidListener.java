/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * @author eccentric_nz
 */
public class VoidListener implements Listener {

    private final TARDIS plugin;

    public VoidListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFallIntoVoid(EntityDamageEvent event) {
        if (!event.getCause().equals(DamageCause.VOID)) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!plugin.getUtils().inTARDISWorld(player)) {
            return;
        }
        Location location = player.getRespawnLocation();
        if (location == null) {
            // get the main server world
            World world = plugin.getServer().getWorlds().getFirst();
            location = world.getSpawnLocation();
        }
        player.teleport(location);
        // regenerate
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ()-> new Regenerator().processPlayer(plugin, player), 10L);
    }
}
