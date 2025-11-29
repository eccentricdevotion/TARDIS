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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekEquipment;
import org.bukkit.Location;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISSpawnListener implements Listener {

    private final TARDIS plugin;
    private final List<SpawnReason> good_spawns = new ArrayList<>();

    public TARDISSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
        good_spawns.add(SpawnReason.BEEHIVE);
        good_spawns.add(SpawnReason.BREEDING);
        good_spawns.add(SpawnReason.BUILD_COPPERGOLEM);
        good_spawns.add(SpawnReason.BUILD_IRONGOLEM);
        good_spawns.add(SpawnReason.BUILD_SNOWMAN);
        good_spawns.add(SpawnReason.BUILD_WITHER);
        good_spawns.add(SpawnReason.CURED);
        good_spawns.add(SpawnReason.CUSTOM);
        good_spawns.add(SpawnReason.DISPENSE_EGG);
        good_spawns.add(SpawnReason.DROWNED);
        good_spawns.add(SpawnReason.EGG);
        good_spawns.add(SpawnReason.ENDER_PEARL);
        good_spawns.add(SpawnReason.INFECTION);
        good_spawns.add(SpawnReason.JOCKEY);
        good_spawns.add(SpawnReason.LIGHTNING);
        good_spawns.add(SpawnReason.MOUNT);
        good_spawns.add(SpawnReason.NETHER_PORTAL);
        good_spawns.add(SpawnReason.OCELOT_BABY);
        good_spawns.add(SpawnReason.PATROL);
        good_spawns.add(SpawnReason.PIGLIN_ZOMBIFIED);
        good_spawns.add(SpawnReason.RAID);
        good_spawns.add(SpawnReason.REANIMATE);
        good_spawns.add(SpawnReason.REINFORCEMENTS);
        good_spawns.add(SpawnReason.SHEARED);
        good_spawns.add(SpawnReason.SHOULDER_ENTITY);
        good_spawns.add(SpawnReason.SILVERFISH_BLOCK);
        good_spawns.add(SpawnReason.SLIME_SPLIT);
        good_spawns.add(SpawnReason.SPAWNER_EGG);
        good_spawns.add(SpawnReason.VILLAGE_DEFENSE);
        good_spawns.add(SpawnReason.VILLAGE_INVASION);
        if (plugin.getConfig().getBoolean("allow.animal_spawners")) {
            good_spawns.add(SpawnReason.SPAWNER);
        }
    }

    /**
     * Listens for entity spawn events. If WorldGuard is enabled it blocks mob-spawning inside the TARDIS, so this
     * checks to see if we are doing the spawning and un-cancels WorldGuard's setCancelled(true).
     * <p>
     * It also prevents natural mob spawning in the TARDIS DEEP_OCEAN biome.
     *
     * @param event A creature spawn event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        SpawnReason spawnReason = event.getSpawnReason();
        Location l = event.getLocation();
        if (l.getWorld().getName().contains("TARDIS")) {
            if (event.getEntityType().equals(EntityType.ARMOR_STAND)) {
                return;
            }
            if (plugin.isTardisSpawn()) {
                plugin.setTardisSpawn(false);
                return;
            }
            // if not an allowable TARDIS spawn reason, cancel
            if (!good_spawns.contains(spawnReason)) {
                event.setCancelled(true);
            }
            if (plugin.getConfig().getBoolean("allow.animal_spawners") && spawnReason.equals(SpawnReason.SPAWNER)) {
                event.setCancelled(event.getEntity() instanceof Enemy);
            }
            if (spawnReason.equals(SpawnReason.BUILD_SNOWMAN) && plugin.getConfig().getBoolean("modules.weeping_angels")) {
                if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                    // spawn a Dalek instead
                    LivingEntity le = (LivingEntity) l.getWorld().spawnEntity(l, EntityType.SKELETON);
                    DalekEquipment.set(le, false);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> event.getEntity().remove(), 2L);
                }
            }
        }
    }
}
