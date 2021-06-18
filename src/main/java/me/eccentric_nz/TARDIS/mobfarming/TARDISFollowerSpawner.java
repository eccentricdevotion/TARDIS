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
package me.eccentric_nz.tardis.mobfarming;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.planets.TardisAngelsApi;
import me.eccentric_nz.tardisweepingangels.TardisWeepingAngelsApi;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class TardisFollowerSpawner {

    private final TardisPlugin plugin;

    public TardisFollowerSpawner(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawn(List<TardisFollower> followers, Location location, Player player, CardinalDirection direction, boolean enter) {
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
        TardisWeepingAngelsApi twa = TardisAngelsApi.getAPI(TardisPlugin.plugin);
        for (TardisFollower follower : followers) {
            plugin.setTardisSpawn(true);
            assert w != null;
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
}
