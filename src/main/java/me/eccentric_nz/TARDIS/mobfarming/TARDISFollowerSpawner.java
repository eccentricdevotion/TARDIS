/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.planets.TARDISAngelsAPI;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class TARDISFollowerSpawner {

    private final TARDIS plugin;

    public TARDISFollowerSpawner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void spawn(List<TARDISFollower> followers, Location location, Player player, COMPASS direction, boolean enter) {
        Location pl = location.clone();
        World w = location.getWorld();
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
        TARDISWeepingAngelsAPI twa = TARDISAngelsAPI.getAPI(TARDIS.plugin);
        for (TARDISFollower follower : followers) {
            plugin.setTardisSpawn(true);
            ArmorStand stand = (ArmorStand) w.spawnEntity(pl, EntityType.ARMOR_STAND);
            if (follower.getMonster().equals(Monster.JUDOON)) {
                twa.setJudoonEquipment(player, stand, follower.getPersist());
            } else if (follower.getMonster().equals(Monster.K9)) {
                twa.setK9Equipment(player, stand, false);
            } else if (follower.getMonster().equals(Monster.OOD)) {
                twa.setOodEquipment(player, stand, false);
            }
            if (follower.isFollowing()) {
                twa.setFollowing(stand, player);
            }
        }
        followers.clear();
    }

    public void spawnDivisionOod(Location location) {
        TARDISWeepingAngelsAPI twa = TARDISAngelsAPI.getAPI(TARDIS.plugin);
        plugin.setTardisSpawn(true);
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        twa.setOodEquipment(null, stand, false);
    }

    public void removeDivisionOod(Location location) {
        for (Entity a : location.getWorld().getNearbyEntities(location, 32, 8, 32, (e) -> e.getType() == EntityType.ARMOR_STAND)) {
            if (a.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && a.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER)) {
                UUID fetched = a.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (fetched.equals(TARDISWeepingAngels.UNCLAIMED)) {
                    a.remove();
                }
            }
        }
    }
}
