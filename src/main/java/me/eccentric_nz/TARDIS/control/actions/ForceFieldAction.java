package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ForceFieldAction {

    private final TARDIS plugin;

    public ForceFieldAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleSheilds(Player player, Location blockLocation, int level) {
        if (TARDISPermission.hasPermission(player, "tardis.forcefield")) {
            if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(player.getUniqueId())) {
                plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                TARDISSounds.playTARDISSound(blockLocation, "tardis_force_field_down");
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FORCE_FIELD", "OFF");
            } else {
                // check there is enough artron
                if (level <= plugin.getArtronConfig().getInt("standby")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LOW");
                    return;
                }
                if (TARDISForceField.addToTracker(player)) {
                    TARDISSounds.playTARDISSound(blockLocation, "tardis_force_field_up");
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "FORCE_FIELD", "ON");
                }
            }
        }
    }
}
