package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Interaction;

import java.util.UUID;

/**
 * <p>The Astrosextant Rectifier will attempt to confirm that a TARDIS has arrived at the correct space-time
 * coordinates.</p><p>The Gravitic Anomaliser is a component of the TARDIS. It generates a localised field of artificial
 * gravity around the outside of the vessel.</p><p>The Absolute Tesseractulator is responsible for keeping track of a
 * TARDIS's dimensional location. It uses the Interstitial Antenna to collect data from the Vortex. A TARDIS knows where
 * it's going by using digitally-modelled time-cone isometry parallel-bussed into the image translator, with local
 * motion being mapped over every refresh-cycle. This information is displayed on the Gyro-Series Dials. It will detect
 * time travel induced by exterior forces (even if the TARDIS's drive is not activated). It is possible that a TARDIS
 * occasionally materializes in space to "get its bearings."</p>
 */
public class ManualFlightInteraction {

    private final TARDIS plugin;

    public ManualFlightInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void receiveInput(int id, UUID uuid, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(uuid)) {
            if (interaction.getLocation().toString().equals(plugin.getTrackerKeeper().getFlight().get(uuid))) {
                plugin.getTrackerKeeper().getCount().put(uuid, plugin.getTrackerKeeper().getCount().getOrDefault(uuid, 0) + 1);
            }
            plugin.getTrackerKeeper().getFlight().remove(uuid);
        }
    }
}
