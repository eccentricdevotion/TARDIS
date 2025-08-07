/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Objects;

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
        player.teleport(Objects.requireNonNullElseGet(location, () -> plugin.getServer().getWorlds().getFirst().getSpawnLocation()));
        // regenerate
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ()-> new Regenerator().processPlayer(plugin, player), 10L);
    }
}
