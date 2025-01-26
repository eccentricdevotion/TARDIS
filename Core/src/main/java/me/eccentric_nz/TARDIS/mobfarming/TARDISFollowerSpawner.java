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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Equipment;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodEquipment;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TARDISFollowerSpawner {

    private final TARDIS plugin;

    public TARDISFollowerSpawner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void spawn(List<Follower> followers, Location location, Player player, COMPASS direction, boolean enter) {
        Location pl = location.clone();
        // will need to adjust this depending on direction Police Box is facing
        if (enter) {
            pl.setZ(location.getZ() + 1);
        } else {
            switch (direction) {
                case NORTH -> pl.add(1, 0, 1);
                case WEST -> pl.add(1, 0, -1);
                case SOUTH -> pl.add(-1, 0, -1);
                default -> pl.add(-1, 0, 1);
            }
        }
        for (Follower f : followers) {
            plugin.setTardisSpawn(true);
            // get data from follower
            TWAFollower follower = new MonsterSpawner().createFollower(pl, new Follower(f.getUuid(), f.getOwner(), f.getSpecies(), true, f.hasOption(), f.getColour(), f.getAmmo()));
            LivingEntity husk = (LivingEntity) follower.getBukkitEntity();
            if (f.getSpecies().equals(Monster.JUDOON)) {
                JudoonEquipment.set(player, husk, false);
            } else if (f.getSpecies().equals(Monster.K9)) {
                K9Equipment.set(player, husk, false);
            } else if (f.getSpecies().equals(Monster.OOD)) {
                OodEquipment.set(player, husk, false);
            }
            follower.setFollowing(true);
        }
        followers.clear();
    }

    public void spawnDivisionOod(Location location) {
        plugin.setTardisSpawn(true);
        LivingEntity husk = (LivingEntity) new MonsterSpawner().createFollower(location.clone().add(0.5d, 0, 0.5d), new Follower(UUID.randomUUID(), TARDISWeepingAngels.UNCLAIMED, Monster.OOD)).getBukkitEntity();
        plugin.getTardisAPI().setOodEquipment(husk, false);
    }

    public void removeDivisionOod(Location location) {
        for (Entity a : location.getWorld().getNearbyEntities(location, 32, 8, 32, (e) -> e.getType() == EntityType.ARMOR_STAND || e.getType() == EntityType.HUSK)) {
            if (a.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && a.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                UUID uuid = a.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (TARDISWeepingAngels.UNCLAIMED.equals(uuid)) {
                    a.remove();
                }
            }
        }
    }
}
