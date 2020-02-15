package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.planets.TARDISAngelsAPI;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

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
                case NORTH:
                    pl.add(1, 0, 1);
                    break;
                case WEST:
                    pl.add(1, 0, -1);
                    break;
                case SOUTH:
                    pl.add(-1, 0, -1);
                    break;
                default:
                    pl.add(-1, 0, 1);
                    break;
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
}
