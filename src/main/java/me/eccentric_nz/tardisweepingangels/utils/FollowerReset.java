package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRestoreFollowers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class FollowerReset {

    private final TARDIS plugin;

    public FollowerReset(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void tame() {
        ResultSetRestoreFollowers rsf = new ResultSetRestoreFollowers(plugin);
        if (rsf.resultSet()) {
            for (Follower follower : rsf.getFollowers()) {
                Entity husk = Bukkit.getServer().getEntity(follower.getUuid());
                if (husk != null) {
                    new ResetMonster(plugin, husk).reset();
                }
            }
        }
    }
}
