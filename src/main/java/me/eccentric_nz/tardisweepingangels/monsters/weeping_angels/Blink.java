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
package me.eccentric_nz.tardisweepingangels.monsters.weeping_angels;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Blink implements Listener {

    private final TARDISWeepingAngels plugin;
    private final List<String> message = new ArrayList<>();

    public Blink(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        message.add("Don't blink. Blink and you're dead.");
        message.add("They are fast. Faster than you can believe.");
        message.add("Don't turn your back. Don't look away.");
        message.add("And don't blink. Good Luck.");
    }

    public static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        double epsilon = 0.0001f;
        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
        if (Math.abs(c.x) > e.x + ad.x) {
            return false;
        }
        if (Math.abs(c.y) > e.y + ad.y) {
            return false;
        }
        if (Math.abs(c.z) > e.z + ad.z) {
            return false;
        }
        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon) {
            return false;
        }
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon) {
            return false;
        }
        return Math.abs(d.x * c.y - d.y * c.x) <= e.x * ad.y + e.y * ad.x + epsilon;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Location observerPos = player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());
        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(16));

        Skeleton skeleton = null;
        // Get nearby entities
        for (Skeleton target : player.getWorld().getEntitiesByClass(Skeleton.class)) {
            // Bounding box of the given player
            Vector3D targetPos = new Vector3D(target.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (skeleton == null || skeleton.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                    // is it an angel?
                    EntityEquipment ee = target.getEquipment();
                    if (ee.getHelmet().getType().equals(Monster.WEEPING_ANGEL.getMaterial())) {
                        skeleton = target;
                    }
                }
            }
        }
        // freeze the closest skeleton
        if (skeleton != null) {
            skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, plugin.getConfig().getInt("angels.freeze_time"), 30, true, false));
            if (!player.isSneaking()) {
                player.sendMessage(plugin.pluginName + message.get(TARDISWeepingAngels.random.nextInt(4)));
            }
        }
    }
}
