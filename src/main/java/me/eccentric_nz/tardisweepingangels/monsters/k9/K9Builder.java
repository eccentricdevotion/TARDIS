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
package me.eccentric_nz.tardisweepingangels.monsters.k9;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class K9Builder implements Listener {

    private final TARDIS plugin;

    public K9Builder(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDaylightDetectorPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType().equals(Material.DAYLIGHT_DETECTOR)) {
            Block placed = event.getBlockPlaced();
            // check the blocks below
            Block below = placed.getRelative(BlockFace.DOWN);
            if (!below.getType().equals(Material.IRON_BLOCK)) {
                return;
            }
            Block east = below.getRelative(BlockFace.EAST);
            Block west = below.getRelative(BlockFace.WEST);
            Block north = below.getRelative(BlockFace.NORTH);
            Block south = below.getRelative(BlockFace.SOUTH);
            if ((east.getType().equals(Material.LEVER) && west.getType().equals(Material.TRIPWIRE_HOOK)) || (east.getType().equals(Material.TRIPWIRE_HOOK) && west.getType().equals(Material.LEVER)) || (north.getType().equals(Material.LEVER) && south.getType().equals(Material.TRIPWIRE_HOOK)) || (north.getType().equals(Material.TRIPWIRE_HOOK) && south.getType().equals(Material.LEVER))) {
                if (!TARDISPermission.hasPermission(event.getPlayer(), "tardisweepingangels.build.k9")) {
                    plugin.getMessenger().send(event.getPlayer(), TardisModule.MONSTERS, "WA_PERM_BUILD", "K9");
                    return;
                }
                Player player = event.getPlayer();
                if (!plugin.getMonstersConfig().getBoolean("k9.worlds." + placed.getWorld().getName())) {
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_BUILD");
                    return;
                }
                // we're building K9
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    placed.setType(Material.AIR);
                    below.setType(Material.AIR);
                    if (east.getType().equals(Material.LEVER) || east.getType().equals(Material.TRIPWIRE_HOOK)) {
                        east.setType(Material.AIR);
                        west.setType(Material.AIR);
                    } else {
                        north.setType(Material.AIR);
                        south.setType(Material.AIR);
                    }
                    Location l = below.getLocation().add(0.5d, 0, 0.5d);
                    LivingEntity e = (LivingEntity) new MonsterSpawner().createFollower(l, new Follower(UUID.randomUUID(), player.getUniqueId(), Monster.K9)).getBukkitEntity();
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        K9Equipment.set(player, e, false);
                        plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, EntityType.HUSK, Monster.K9, l));
                    }, 2L);
                }, 20L);
            }
        }
    }
}
