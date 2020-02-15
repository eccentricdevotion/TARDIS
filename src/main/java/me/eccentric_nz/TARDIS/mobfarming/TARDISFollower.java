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
