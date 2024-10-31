package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.flight.TARDISExteriorFlight;
import org.bukkit.Bukkit;
import org.bukkit.Input;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;

public class PlayerInputListener implements Listener {

    private final TARDIS plugin;

    public PlayerInputListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        Entity entity = player.getVehicle();
        if (entity instanceof ArmorStand as && ((CraftArmorStand) as).getHandle() instanceof TARDISArmourStand stand) {
            Input input = event.getInput();
            if (input.isSneak()) {
                stand.setStationary(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                    TARDIS.plugin.getTrackerKeeper().getStillFlyingNotReturning().remove(player.getUniqueId());
                    // teleport player back to the TARDIS interior
                    new TARDISExteriorFlight(TARDIS.plugin).stopFlying(player, as);
                    stand.setPlayer(null);
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.setInvulnerable(false);
                }, 3L);
            }
            if (input.isJump()) {
                stand.setStationary(!stand.isStationary());
            }
        }
    }
}
