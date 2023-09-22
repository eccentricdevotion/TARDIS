package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;

public class FollowerSaver {

    private final TARDIS plugin;

    public FollowerSaver(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void persist() {
        for (World world : plugin.getServer().getWorlds()) {
            for (Entity e : world.getEntitiesByClass(Husk.class)) {
                if (((CraftEntity) e).getHandle() instanceof TWAFollower follower) {
                    // save entity in followers table
                    new FollowerPersister(plugin).save(follower);
                }
            }
        }
    }
}
