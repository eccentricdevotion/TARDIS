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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sounds implements Listener {

    private final TARDIS plugin;
    private final List<UUID> tracker = new ArrayList<>();

    public Sounds(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTargetPlayer(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        if (MonsterEquipment.isMonster(entity) && event.getTarget() instanceof Player player) {
            UUID uuid = entity.getUniqueId();
            if (tracker.contains(uuid)) {
                return;
            }
            tracker.add(uuid);
            PersistentDataContainer pdc = entity.getPersistentDataContainer();
            String which = "";
            long delay = 50L;
            if (pdc.has(TARDISWeepingAngels.CLOCKWORK_DROID, PersistentDataType.INTEGER)) {
                delay = 80L;
                which = "clockwork_droid";
            }
            if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                which = "cyberman";
                delay = 80L;
            }
            if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                which = "dalek";
                delay = 180L;
            }
            if (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)) {
                which = "dalek_sec";
            }
            if (pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)) {
                which = "davros";
            }
            if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                which = "sea_devil";
            }
            if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                which = "empty_child";
            }
            if (pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)) {
                delay = 100L;
                which = "hath";
            }
            if (pdc.has(TARDISWeepingAngels.HEAVENLY_HOST, PersistentDataType.INTEGER)) {
                which = "heavenly_host";
            }
            if (pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)) {
                delay = 180L;
                which = "item.trident.thunder";
            }
            if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                delay = 180L;
                which = "headless_monk";
            }
            if (pdc.has(TARDISWeepingAngels.NIMON, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "nimon";
            }
            if (pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
                delay = 180L;
                which = "racnoss";
            }
            if (pdc.has(TARDISWeepingAngels.SCARECROW, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "scarecrow";
            }
            if (pdc.has(TARDISWeepingAngels.BEAST, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "entity.ravager.roar";
            }
            if (pdc.has(TARDISWeepingAngels.CYBERSHADE, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "entity.hoglin.converted_to_zombified";
            }
            if (pdc.has(TARDISWeepingAngels.VAMPIRE, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "entity.fish.swim";
            }
            if (pdc.has(TARDISWeepingAngels.SMILER, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "entity.rabbit.attack";
            }
            if (pdc.has(TARDISWeepingAngels.SUTEKH, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "sutekh";
            }
            if (pdc.has(TARDISWeepingAngels.OMEGA, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "omega";
            }
            if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                which = "silurian";
            }
            if (pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                which = "slitheen";
            }
            if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                which = "sontaran";
                delay = 55L;
            }
            if (pdc.has(TARDISWeepingAngels.SYCORAX, PersistentDataType.INTEGER)) {
                delay = 70L;
                which = "sycorax";
            }
            if (pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)) {
                which = "vashta";
                delay = 30L;
            }
            if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                which = "warrior";
            }
            if (pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                which = "zygon";
                delay = 100L;
            }
            if (!entity.getPassengers().isEmpty() && entity.getPassengers().getFirst().getType().equals(EntityType.GUARDIAN)) {
                delay = 90L;
                which = "silence";
            }
            if (entity instanceof Guardian && entity.getVehicle() != null && entity.getVehicle().getType().equals(EntityType.SKELETON)) {
                delay = 90L;
                which = "silence";
            }
            if (!which.isEmpty()) {
                String sound = which;
                // schedule delayed task
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.playSound(entity.getLocation(), sound, 1.0f, 1.0f);
                    tracker.remove(uuid);
                }, delay);
            }
        }
    }
}
