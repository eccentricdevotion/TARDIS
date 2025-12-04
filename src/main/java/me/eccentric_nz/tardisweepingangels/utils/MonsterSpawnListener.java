package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MonsterSpawnListener implements Listener {

    @EventHandler
    public void onFollowerSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof TWAFollower || entity instanceof IronGolem) {
            TeamAdder.joinTeam(event.getEntity());
        }
    }
}
