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
package me.eccentric_nz.tardisweepingangels.monsters.weeping_angels;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AngelBuilder implements Listener {

    private final TARDIS plugin;

    public AngelBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSkullPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType().equals(Material.SKELETON_SKULL)) {
            Block placed = event.getBlockPlaced();
            // check the blocks below
            Block below = placed.getRelative(BlockFace.DOWN);
            if (!below.getType().equals(Material.COBBLESTONE_WALL)) {
                return;
            }
            Block bottom = below.getRelative(BlockFace.DOWN);
            if (!bottom.getType().equals(Material.COBBLESTONE_WALL)) {
                return;
            }
            Block east = below.getRelative(BlockFace.EAST);
            Block west = below.getRelative(BlockFace.WEST);
            Block north = below.getRelative(BlockFace.NORTH);
            Block south = below.getRelative(BlockFace.SOUTH);
            if ((east.getType().equals(Material.COBBLESTONE_WALL) && west.getType().equals(Material.COBBLESTONE_WALL)) || (north.getType().equals(Material.COBBLESTONE_WALL) && south.getType().equals(Material.COBBLESTONE_WALL))) {
                if (!TARDISPermission.hasPermission(event.getPlayer(), "tardisweepingangels.build.angel")) {
                    plugin.getMessenger().send(event.getPlayer(), TardisModule.MONSTERS, "WA_PERM_BUILD", "Weeping Angel!");
                    return;
                }
                // we're building an angel
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    placed.setType(Material.AIR);
                    below.setType(Material.AIR);
                    bottom.setType(Material.AIR);
                    if (east.getType().equals(Material.COBBLESTONE_WALL)) {
                        east.setType(Material.AIR);
                        west.setType(Material.AIR);
                    } else {
                        north.setType(Material.AIR);
                        south.setType(Material.AIR);
                    }
                    Location l = bottom.getLocation();
                    LivingEntity angel = new MonsterSpawner().create(l, Monster.WEEPING_ANGEL);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        new Equipper(Monster.WEEPING_ANGEL, angel, false).setHelmetAndInvisibility();
                        plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(angel, EntityType.SKELETON, Monster.WEEPING_ANGEL, l));
                    }, 5L);
                }, 20L);
            }
        }
    }
}
