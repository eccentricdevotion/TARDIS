/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.arch.TARDISArchInventory;
import me.eccentric_nz.TARDIS.arch.TARDISArchPersister;
import me.eccentric_nz.TARDIS.autonomous.TARDISAutonomousDeath;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.flight.FlightReturnData;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

/**
 * Several events can trigger an Automatic Emergency Landing. Under these
 * circumstances a TARDIS will use the coordinate override to initiate an
 * Automatic Emergency Landing on the "nearest" available habitable planet.
 *
 * @author eccentric_nz
 */
public class TARDISTimeLordDeathListener implements Listener {

    private final TARDIS plugin;

    public TARDISTimeLordDeathListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player death. If the player is a time lord and the autonomous
     * circuit is engaged, then the TARDIS will automatically return to its
     * 'home' location, or the nearest Recharge area.
     *
     * @param event a player dying
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTimeLordDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();
        if (plugin.getConfig().getBoolean("allow.autonomous") && TARDISPermission.hasPermission(player, "tardis.autonomous")) {
            new TARDISAutonomousDeath(plugin).automate(player);
        }
        // always remove player from eye damage list
        plugin.getTrackerKeeper().getEyeDamage().remove(uuid);
        // spawn an ossified if configured
        if (plugin.getConfig().getBoolean("modules.weeping_angels") && plugin.getConfig().getBoolean("eye_of_harmony.ossified")) {
            // spawn an ossified at the player's location
            Location l = player.getLocation();
            LivingEntity e = new MonsterSpawner().create(l, Monster.OSSIFIED);
            new Equipper(Monster.OSSIFIED, e, false, false).setHelmetAndInvisibilty();
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, EntityType.ZOMBIE, Monster.OSSIFIED, l));
            String name = player.getName();
            e.setCustomName(name);
            e.setCustomNameVisible(true);
        }
        // save arched status
        if (plugin.isDisguisesOnServer() && plugin.getConfig().getBoolean("arch.enabled") && plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
            new TARDISArchPersister(plugin).save(uuid);
            if (plugin.getConfig().getBoolean("arch.clear_inv_on_death")) {
                // clear inventories
                new TARDISArchInventory().clear(uuid);
            }
        }
        // stop looping sounds
        if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(uuid)) {
            FlightReturnData frd = plugin.getTrackerKeeper().getFlyingReturnLocation().get(player.getUniqueId());
            plugin.getServer().getScheduler().cancelTask(frd.getSound());
        } else if (player.getPersistentDataContainer().has(plugin.getLoopKey(), PersistentDataType.INTEGER)) {
            int task = player.getPersistentDataContainer().get(plugin.getLoopKey(), PersistentDataType.INTEGER);
            plugin.getServer().getScheduler().cancelTask(task);
            player.getPersistentDataContainer().remove(plugin.getLoopKey());
        }
    }
}
