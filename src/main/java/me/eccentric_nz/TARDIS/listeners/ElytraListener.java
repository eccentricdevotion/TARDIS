package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import java.util.Objects;

public class ElytraListener implements Listener {

    private final TARDIS plugin;

    public ElytraListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onElytraFlyInVortex(EntityToggleGlideEvent event) {
        if (!event.isGliding()) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }
        if (player.isOp()) {
            return;
        }
        if (!plugin.getUtils().inTARDISWorld(player)) {
            return;
        }
        event.setCancelled(true);
        // get tardis_id
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            // teleport player to their TARDIS console
            Location idl = TARDISDoorListener.getDoor(1, rs.getTardisId()).getL();
            player.teleport(idl);
        } else {
            Location respawn = player.getRespawnLocation();
            // if player has respawn location set
            player.teleport(Objects.requireNonNullElseGet(respawn, () -> plugin.getServer().getWorlds().getFirst().getSpawnLocation()));
        }
    }
}
