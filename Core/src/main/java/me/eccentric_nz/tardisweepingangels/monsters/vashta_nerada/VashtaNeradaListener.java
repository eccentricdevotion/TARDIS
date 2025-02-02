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
package me.eccentric_nz.tardisweepingangels.monsters.vashta_nerada;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class VashtaNeradaListener implements Listener {

    private final TARDIS plugin;

    public VashtaNeradaListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBookshelfBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (b != null && b.getType().equals(Material.BOOKSHELF)) {
            String name = WorldProcessor.sanitiseName(b.getWorld().getName());
            if (plugin.getMonstersConfig().getInt("vashta_nerada.worlds." + name) > 0 && TARDISConstants.RANDOM.nextInt(100) < plugin.getMonstersConfig().getInt("vashta_nerada.worlds." + name)) {
                Location l = getClearLocation(event.getPlayer());
                if (l != null) {
                    // spawn Vashta Nerada at location
                    spawnVashtaNerada(l);
                }
            }
        }
    }

    private Location getClearLocation(Player p) {
        Location ret = null;
        Block l = p.getLocation().getBlock();
        World w = l.getWorld();
        for (BlockFace f : plugin.getGeneralKeeper().getShuffledFaces()) {
            Block b = l.getRelative(f, 3);
            if (b.getType().equals(Material.AIR) && b.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                ret = new Location(w, b.getX() + 0.5d, b.getY(), b.getZ() + 0.5d);
                break;
            }
        }
        return ret;
    }

    private void spawnVashtaNerada(Location l) {
        LivingEntity vashta = new MonsterSpawner().create(l, Monster.VASHTA_NERADA);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            new Equipper(Monster.VASHTA_NERADA, vashta, false).setHelmetAndInvisibility();
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(vashta, EntityType.ZOMBIE, Monster.VASHTA_NERADA, l));
        }, 5L);
    }
}
