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
package me.eccentric_nz.tardisweepingangels.monsters.weeping_angels;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
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

import java.util.ArrayList;
import java.util.List;

public class Blink implements Listener {

    private final TARDIS plugin;
    private final List<String> message = new ArrayList<>();

    public Blink(TARDIS plugin) {
        this.plugin = plugin;
        message.add("Don't blink. Blink and you're dead.");
        message.add("They are fast. Faster than you can believe.");
        message.add("Don't turn your back. Don't look away.");
        message.add("And don't blink. Good Luck.");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Location observerPos = player.getEyeLocation();
        TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
        TARDISVector3D observerStart = new TARDISVector3D(observerPos);
        TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));

        Skeleton skeleton = null;
        // Get nearby entities
        for (Skeleton target : player.getWorld().getEntitiesByClass(Skeleton.class)) {
            // Bounding box of the given player
            TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
            TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
            TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
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
            skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, plugin.getMonstersConfig().getInt("angels.freeze_time"), 30, true, false));
            if (!player.isSneaking()) {
                plugin.getMessenger().message(player, TardisModule.MONSTERS, message.get(TARDISConstants.RANDOM.nextInt(4)));
            }
        }
    }
}
