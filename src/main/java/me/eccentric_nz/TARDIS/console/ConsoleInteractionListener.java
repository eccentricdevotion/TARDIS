/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
                    Player player = event.getPlayer();
                    if (player.isSneaking() && ci != ConsoleInteraction.SCREEN_LEFT && ci != ConsoleInteraction.SCREEN_RIGHT && ci != ConsoleInteraction.ARTRON) {
                        plugin.getMessenger().announceRepeater(player, rsi.getControl().getAlternateName());
                        return;
                    }
                    int id = rsi.getTardisId();
                    int state = rsi.getState();
                    switch (ci) {
                        // section zero
                        case HANDBRAKE -> new HandbrakeInteraction(plugin).process(id, state, player, interaction);
                        case THROTTLE -> new ThrottleInteraction(plugin).process(player, interaction, id);
                        case RELATIVITY_DIFFERENTIATOR -> new FlightModeInteraction(plugin).process(player, id, interaction);
                        // section one
                        case WORLD -> new WorldInteraction(plugin).selectWorld(state, player, interaction, id);
                        case MULTIPLIER, X, Z -> new MultiplierXZInteraction(plugin).setRange(ci, state, interaction, id, player);
                        case HELMIC_REGULATOR -> new HelmicRegulatorInteraction(plugin).selectWorld(state, id, player, interaction);
                        // section two
                        case RANDOMISER -> new RandomiserInteraction(plugin).generateDestination(id, player, interaction);
                        case WAYPOINT_SELECTOR -> new WayPointInteraction(plugin).openSaveGUI(id, player, interaction);
                        case FAST_RETURN -> new FastReturnInteraction(plugin).setBack(id, player, interaction);
                        case TELEPATHIC_CIRCUIT -> new TelepathicCircuitInteraction(plugin).process(player);
                        // section three
                        case SONIC_DOCK -> new SonicDockInteraction(plugin).process(player, interaction, id);
                        case DIRECTION -> new DirectionInteraction(plugin).rotate(id, player, interaction);
                        // section four
                        case LIGHT_SWITCH -> new LightSwitchInteraction(plugin).toggle(id, player, interaction);
                        case INTERIOR_LIGHT_LEVEL_SWITCH -> new LightLevelInteraction(plugin).setInterior(state, id, interaction, player);
                        case EXTERIOR_LAMP_LEVEL_SWITCH -> new LampLevelInteraction(plugin).setExterior(state, id, interaction, player);
                        case CONSOLE_LAMP -> new ConsoleLampInteraction(plugin).setLevel(state, id, interaction, player);
                        case DOOR_TOGGLE -> new DoorToggleInteraction(plugin).toggle(id, player, interaction);
                        // section five
                        case SCREEN_LEFT, SCREEN_RIGHT -> new ScreenInteraction(plugin).display(id, interaction, ci == ConsoleInteraction.SCREEN_RIGHT, player);
                        case SCANNER -> new ScannerInteraction(plugin).process(id, player, interaction);
                        case ARTRON -> new ArtronInteraction(plugin).show(id, player, interaction);
                        case REBUILD -> new RebuildInteraction(plugin).process(id, player, interaction);
                        // manual flight
                        case ASTROSEXTANT_RECTIFIER, ABSOLUTE_TESSERACTULATOR, GRAVITIC_ANOMALISER -> new ManualFlightInteraction(plugin).receiveInput(player.getUniqueId(), interaction);
                        // unknown
                        default -> plugin.getMessenger().announceRepeater(player, rsi.getControl().getAlternateName());
                    }
                }
            }
        }
    }
}
