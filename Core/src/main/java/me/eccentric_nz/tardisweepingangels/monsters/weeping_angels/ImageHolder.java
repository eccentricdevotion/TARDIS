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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Locale;

public class ImageHolder implements Listener {

    private final TARDIS plugin;

    public ImageHolder(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChatAboutWeepingAngel(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        if (message.toLowerCase(Locale.ROOT).contains("angel") && TARDISConstants.RANDOM.nextInt(100) < plugin.getMonstersConfig().getInt("angels.spawn_from_chat.chance")) {
            int dist = plugin.getMonstersConfig().getInt("angels.spawn_from_chat.distance_from_player");
            Block b = event.getPlayer().getLocation().getBlock().getRelative(plugin.getGeneralKeeper().getFaces().get(TARDISConstants.RANDOM.nextInt(4)), dist);
            // get the highest block in a random direction
            Location highest = b.getWorld().getHighestBlockAt(b.getLocation()).getLocation();
            Location l = highest.add(0, 1, 0);
            // spawn an angel
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                LivingEntity angel = new MonsterSpawner().create(l, Monster.WEEPING_ANGEL);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    new Equipper(Monster.WEEPING_ANGEL, angel, false).setHelmetAndInvisibility();
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(angel, EntityType.SKELETON, Monster.WEEPING_ANGEL, l));
                }, 5L);
            }, 20L);
        }
    }
}
