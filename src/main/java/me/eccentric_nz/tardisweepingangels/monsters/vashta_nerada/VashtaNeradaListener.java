/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
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
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VashtaNeradaListener implements Listener {

    private final TARDISWeepingAngels plugin;
    private final List<BlockFace> faces = new ArrayList<>();

    public VashtaNeradaListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        faces.add(BlockFace.EAST);
        faces.add(BlockFace.NORTH);
        faces.add(BlockFace.SOUTH);
        faces.add(BlockFace.WEST);
    }

    @EventHandler
    public void onBookshelfBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (b != null && b.getType().equals(Material.BOOKSHELF)) {
            String name = WorldProcessor.sanitiseName(b.getWorld().getName());
            if (plugin.getConfig().getInt("vashta_nerada.worlds." + name) > 0 && TARDISWeepingAngels.random.nextInt(100) < plugin.getConfig().getInt("vashta_nerada.worlds." + name)) {
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
        Collections.shuffle(faces, TARDISWeepingAngels.random);
        for (BlockFace f : faces) {
            Block b = l.getRelative(f, 3);
            if (b.getType().equals(Material.AIR) && b.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                ret = new Location(w, b.getX() + 0.5d, b.getY(), b.getZ() + 0.5d);
                break;
            }
        }
        return ret;
    }

    private void spawnVashtaNerada(Location l) {
        LivingEntity v = (LivingEntity) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
        v.setSilent(true);
        Zombie vashta = (Zombie) v;
        vashta.setAdult();
        PotionEffect p = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, true, false);
        v.addPotionEffect(p);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            new Equipper(Monster.VASHTA_NERADA, v, false, false).setHelmetAndInvisibilty();
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(v, EntityType.ZOMBIE, Monster.VASHTA_NERADA, l));
        }, 5L);
    }
}
