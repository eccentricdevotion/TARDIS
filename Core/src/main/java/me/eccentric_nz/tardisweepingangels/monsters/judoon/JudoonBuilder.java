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
package me.eccentric_nz.tardisweepingangels.monsters.judoon;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class JudoonBuilder implements Listener {

    private final TARDIS plugin;

    public JudoonBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onIronTrapdoorPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType().equals(Material.IRON_TRAPDOOR)) {
            Block placed = event.getBlockPlaced();
            // check the blocks below
            Block below = placed.getRelative(BlockFace.DOWN);
            if (!below.getType().equals(Material.OBSIDIAN)) {
                return;
            }
            Block bottom = below.getRelative(BlockFace.DOWN);
            if (!bottom.getType().equals(Material.OBSIDIAN)) {
                return;
            }
            Block east = below.getRelative(BlockFace.EAST);
            Block west = below.getRelative(BlockFace.WEST);
            Block north = below.getRelative(BlockFace.NORTH);
            Block south = below.getRelative(BlockFace.SOUTH);
            if ((east.getType().equals(Material.RED_NETHER_BRICK_WALL) && west.getType().equals(Material.RED_NETHER_BRICK_WALL)) || (north.getType().equals(Material.RED_NETHER_BRICK_WALL) && south.getType().equals(Material.RED_NETHER_BRICK_WALL))) {
                if (!TARDISPermission.hasPermission(event.getPlayer(), "tardisweepingangels.build.judoon")) {
                    plugin.getMessenger().send(event.getPlayer(), TardisModule.MONSTERS, "WA_PERM_BUILD", "a Judoon!");
                    return;
                }
                // we're building a Judoon
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    placed.setType(Material.AIR);
                    below.setType(Material.AIR);
                    bottom.setType(Material.AIR);
                    if (east.getType().equals(Material.RED_NETHER_BRICK_WALL)) {
                        east.setType(Material.AIR);
                        west.setType(Material.AIR);
                    } else {
                        north.setType(Material.AIR);
                        south.setType(Material.AIR);
                    }
                    Location l = bottom.getLocation().add(0.5d, 0, 0.5d);
                    LivingEntity husk = (LivingEntity) new MonsterSpawner().createFollower(l, new Follower(UUID.randomUUID(), event.getPlayer().getUniqueId(), Monster.JUDOON)).getBukkitEntity();
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        JudoonEquipment.set(event.getPlayer(), husk, false);
                        plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(husk, EntityType.HUSK, Monster.JUDOON, l));
                    }, 2L);
                }, 20L);
            }
        }
    }
}
