package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.Input;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.util.Vector;

public class PlayerInputListener implements Listener {

    private final TARDIS plugin;

    public PlayerInputListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        Entity stand = player.getVehicle();
        if (stand != null && stand.getType() == EntityType.ARMOR_STAND) {
            Input input = event.getInput();
            Entity chicken = stand.getVehicle();
            if (chicken != null) {
                if (input.isSneak()) {
                    chicken.setVelocity(new Vector(0, 0, 0));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                        // kill chicken
                        chicken.removePassenger(stand);
                        chicken.remove();
                        ArmorStand as = (ArmorStand) stand;
                        TARDIS.plugin.getTrackerKeeper().getStillFlyingNotReturning().remove(player.getUniqueId());
                        // teleport player back to the TARDIS interior
                        new TARDISExteriorFlight(TARDIS.plugin).stopFlying(player, as);
                    }, 3L);
                }
            }
        }
    }
}
