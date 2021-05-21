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
import me.eccentric_nz.TARDIS.planets.TARDISAngelsAPI;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import me.eccentric_nz.tardisweepingangels.utils.FollowerChecker;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class TARDISFollower {

    private Monster monster;
    private int persist;
    private boolean valid = false;
    private boolean following = false;

    public TARDISFollower(Entity entity, UUID player) {
        checkEntity(entity, player);
    }

    private void checkEntity(Entity entity, UUID player) {
        TARDISWeepingAngelsAPI twa = TARDISAngelsAPI.getAPI(TARDIS.plugin);
        FollowerChecker followerChecker = twa.isClaimedMonster(entity, player);
        if (!followerChecker.getMonster().equals(Monster.WEEPING_ANGEL)) {
            valid = true;
            monster = followerChecker.getMonster();
            persist = followerChecker.getPersist();
            following = followerChecker.isFollowing();
        }
    }

    public Monster getMonster() {
        return monster;
    }

    public int getPersist() {
        return persist;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isFollowing() {
        return following;
    }
}
