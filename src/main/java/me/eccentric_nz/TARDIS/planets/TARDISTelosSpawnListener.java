package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class TARDISTelosSpawnListener implements Listener {

    private final TARDIS plugin;

    public TARDISTelosSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCybermanSpawn(CreatureSpawnEvent event) {
        if (!event.getLocation().getWorld().getName().equalsIgnoreCase("telos")) {
            return;
        }
        CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();
        // if configured prevent spawns (unless from spawners and plugins)
        if (!plugin.getPlanetsConfig().getBoolean("planets.telos.spawn_other_mobs") && spawnReason != CreatureSpawnEvent.SpawnReason.SPAWNER && spawnReason != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.setCancelled(true);
            return;
        }
        if (spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            if (!event.getEntity().getType().equals(EntityType.ZOMBIE)) {
                return;
            }
            LivingEntity le = event.getEntity();
            // it's a Cyberman - disguise it!
            plugin.getTardisAPI().setCyberEquipment(le, false);
        }
    }
}
