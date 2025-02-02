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
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.particles.Regeneration;
import me.eccentric_nz.TARDIS.particles.TARDISParticleRunnable;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RegenerationEmitter extends TARDISParticleRunnable implements Runnable {

    private final TARDIS plugin;
    private final Player player;
    private final Location location;
    private final float yaw;

    public RegenerationEmitter(TARDIS plugin, Player player, Location location, float yaw) {
        super(plugin, player.getUniqueId());
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.yaw = yaw;
    }

    @Override
    public void cancel() {
        plugin.getServer().getScheduler().cancelTask(taskID);
        taskID = 0;
        if (player.isOnline()) {
            player.setInvulnerable(false);
            // eject the player
            Entity vehicle = player.getVehicle();
            if (vehicle != null) {
                vehicle.eject();
                // remove the display entity
                vehicle.remove();
            }
            // reset player scale
            player.getAttribute(Attribute.SCALE).setBaseValue(1.0d);
            // remove invisibility
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            // show the player again
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (p.getWorld() == player.getWorld()) {
                    p.showPlayer(plugin, player);
                }
            }
            // add potion effects
            if (plugin.getRegenerationConfig().getBoolean("effects.negative")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1800, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 1800, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1800, 1));
            }
            if (plugin.getRegenerationConfig().getBoolean("effects.positive")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1800, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1800, 1));
            }
            if (plugin.getRegenerationConfig().getBoolean("restore.health")) {
                player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getDefaultValue());
            }
            if (plugin.getRegenerationConfig().getBoolean("restore.food") && player.getFoodLevel() < 20) {
                player.setFoodLevel(20);
            }
            // reset skin
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getSkinChanger().remove(player);
                // remove potion effects
                player.removePotionEffect(PotionEffectType.SLOWNESS);
                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }, 1800L);
        }
    }

    @Override
    public void run() {
        if (t < 20) {
            TARDISParticleRunnable runnable = new Regeneration(plugin, player, location, yaw);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, 1L);
            runnable.setTaskID(task);
            t++;
        } else {
            cancel();
        }
    }
}
