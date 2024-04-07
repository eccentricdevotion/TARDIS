package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.interaction.*;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInteraction;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
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
                    int id = rsi.getTardisId();
                    Player player = event.getPlayer();
                    int state = rsi.getState();
                    switch (ci) {
                        // section zero
                        case HANDBRAKE ->
                                new HandbrakeInteraction(plugin).process(id, state, player, interaction.getLocation());
                        case THROTTLE -> new ThrottleInteraction(plugin).process(player, interaction, id);
                        case RELATIVITY_DIFFERENTIATOR -> new FlightModeInteraction(plugin).process(player);
                        // section one
                        case WORLD -> new WorldInteraction(plugin).selectWorld(state, player, id);
                        case MULTIPLIER, X, Z -> new MultiplierXZInteraction(plugin).setRange(ci, state, id, player);
                        case HELMIC_REGULATOR -> {
                            // TODO add config options to planets.yml - helmic_regulator_order: [1-8|-1]
                            new HelmicRegulatorInteraction(plugin).selectWorld(state, id, player);
                        }
                        // section two
                        case RANDOMISER -> new RandomiserInteraction(plugin).generateDestination(id, player);
                        case WAYPOINT_SELECTOR -> new WayPointInteraction(plugin).openSaveGUI(id, player);
                        case FAST_RETURN -> new FastReturnInteraction(plugin).setBack(id, player);
                        case TELEPATHIC_CIRCUIT -> new TelepathicCircuitInteraction(plugin).openGUI(player);
                        // section three
                        case SONIC_DOCK -> new SonicDockInteraction(plugin).process(player, interaction, id);
                        case DIRECTION -> new DirectionInteraction(plugin).rotate(id, player);
                        // section four
                        case LIGHT_SWITCH -> new LightSwitchInteraction(plugin).toggle(id, player);
                        case INTERIOR_LIGHT_LEVEL_SWITCH -> new LightLevelInteraction(plugin).setInterior(state, id, interaction, player);
                        case EXTERIOR_LAMP_LEVEL_SWITCH -> new LampLevelInteraction(plugin).setExterior(state, id, interaction, player);
                        case DOOR_TOGGLE -> new DoorToggleInteraction(plugin).toggle(id, player);
                        // section five
                        case SCREEN_LEFT, SCREEN_RIGHT -> new ScreenInteraction(plugin).display(id, interaction.getLocation(), ci == ConsoleInteraction.SCREEN_RIGHT);
                        case SCANNER -> new ScannerIntraction(plugin).process(id, player);
                        case ARTRON -> plugin.getMessenger().sendArtron(player, id, 0);
                        case REBUILD -> new RebuildInteraction(plugin).process(id, player);
                        default -> plugin.getMessenger().announceRepeater(player, rsi.getControl().getAlternateName());
                    }
                }
            }
        }
    }
}
