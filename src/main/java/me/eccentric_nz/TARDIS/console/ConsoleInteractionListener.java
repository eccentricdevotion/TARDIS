package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.interaction.*;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInteraction;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.UUID;

public class ConsoleInteractionListener implements Listener {

    private final TARDIS plugin;

    public ConsoleInteractionListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConsoleInteractionClick(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Interaction interaction) {
            if (interaction.getPersistentDataContainer().has(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID())) {
                UUID uuid = interaction.getPersistentDataContainer().get(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID());
                ResultSetInteraction rsi = new ResultSetInteraction(plugin, uuid);
                if (rsi.resultSet()) {
                    ConsoleInteraction ci = rsi.getControl();
                    switch (ci) {
                        case SCREEN_LEFT, SCREEN_RIGHT -> new ScreenInteraction(plugin).display(interaction.getLocation(), ci == ConsoleInteraction.SCREEN_RIGHT);
                        case SCANNER -> new ScannerIntraction(plugin).process(rsi.getTardis_id(), event.getPlayer());
                        case ARTRON -> plugin.getMessenger().sendArtron(event.getPlayer(), rsi.getTardis_id(), 0);
                        case REBUILD -> new RebuildInteraction(plugin).process(rsi.getTardis_id(), event.getPlayer());
                        case SONIC_DOCK -> new SonicDockInteraction(plugin).process(event.getPlayer(), interaction, rsi.getTardis_id());
                        case HANDBRAKE -> new HandbrakeInteraction(plugin).process(rsi.getTardis_id(), rsi.getState(), event.getPlayer(), interaction.getLocation());
                        case THROTTLE -> new ThrottleInteraction(plugin).process(event.getPlayer());
                        case RELATIVITY_DIFFERENTIATOR -> new FlightModeInteraction(plugin).process(event.getPlayer());
                        default -> plugin.getMessenger().announceRepeater(event.getPlayer(), rsi.getControl().getAlternateName());
                    }
                }
            }
        }
    }
}
