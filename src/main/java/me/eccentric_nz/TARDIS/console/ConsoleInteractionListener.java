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
                        // section zero
                        case HANDBRAKE -> new HandbrakeInteraction(plugin).process(rsi.getTardis_id(), rsi.getState(), event.getPlayer(), interaction.getLocation());
                        case THROTTLE -> new ThrottleInteraction(plugin).process(event.getPlayer());
                        case RELATIVITY_DIFFERENTIATOR -> new FlightModeInteraction(plugin).process(event.getPlayer());
                        // section one
                        case WORLD -> new WorldInteraction(plugin).selectWorld();
                        case MULTIPLIER -> new MultiplierInteraction(plugin).setRange();
                        case X -> new XInteraction(plugin).setRange();
                        case Z -> new ZInteraction(plugin).setRange();
                        case HELMIC_REGULATOR -> {
                            // TODO add config options to planets.yml - helmic_regulator: [1-8|-1]
                            new HelmicRegulatorInteraction(plugin).selectWorld();
                        }
                        // section two
                        case RANDOMISER -> new RandomiserInteraction(plugin).generateDestination();
                        case WAYPOINT_SELECTOR -> new WayPointInteraction(plugin).openSaveGUI();
                        case FAST_RETURN -> new FastReturnInteraction(plugin).setBack();
                        case TELEPATHIC_CIRCUIT -> new TelepathicCircuitInteraction(plugin).openGUI();
                        // section three
                        case SONIC_DOCK -> new SonicDockInteraction(plugin).process(event.getPlayer(), interaction, rsi.getTardis_id());
                        case DIRECTION -> new DirectionInteraction(plugin).rotate();
                        // section four
                        case LIGHT_SWITCH -> new LightSwitchInteraction(plugin).toggle();
                        case INTERIOR_LIGHT_LEVEL_SWITCH -> new LightLevelInteraction(plugin).setInterior();
                        case EXTERIOR_LAMP_LEVEL_SWITCH -> new LampLevelInteraction(plugin).setExterior();
                        case DOOR_TOGGLE -> new DoorToggleInteraction(plugin).toggle();
                        // section five
                        case SCREEN_LEFT, SCREEN_RIGHT -> new ScreenInteraction(plugin).display(rsi.getTardis_id(), interaction.getLocation(), ci == ConsoleInteraction.SCREEN_RIGHT);
                        case SCANNER -> new ScannerIntraction(plugin).process(rsi.getTardis_id(), event.getPlayer());
                        case ARTRON -> plugin.getMessenger().sendArtron(event.getPlayer(), rsi.getTardis_id(), 0);
                        case REBUILD -> new RebuildInteraction(plugin).process(rsi.getTardis_id(), event.getPlayer());
                        default -> plugin.getMessenger().announceRepeater(event.getPlayer(), rsi.getControl().getAlternateName());
                    }
                }
            }
        }
    }
}
