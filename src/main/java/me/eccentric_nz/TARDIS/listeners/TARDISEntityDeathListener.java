package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class TARDISEntityDeathListener implements Listener {

    private final TARDIS plugin;

    public TARDISEntityDeathListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDiedInTARDIS(EntityDeathEvent event) {
        if (event.getEntity() instanceof Monster monster) {
            Location location = monster.getLocation();
            if (plugin.getUtils().inTARDISWorld(location)) {
                // get the TARDIS id the monster was killed in

            }
        }
    }
}
