/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.info;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * The TARDIS information system is a searchable database which was discovered
 * by the Fifth Doctor's companions Nyssa and Tegan from a readout in the
 * control room. The Fifth Doctor called it the TARDIS databank.
 *
 * @author bootthanoo, eccentric_nz
 */
public class TARDISInformationSystemListener implements Listener {

    private final TARDIS plugin;
    FileConfiguration pluginYml;

    public TARDISInformationSystemListener(TARDIS plugin) {
        this.plugin = plugin;
        this.pluginYml = YamlConfiguration.loadConfiguration(this.plugin.getPM().getPlugin("TARDIS").getResource("plugin.yml"));
    }

    /**
     * Listens for player typing a TARDIS Information System key code. The
     * player must be found in the trackInfoMenu HashMap, where their position
     * in the TIS is stored. The key code is then processed.
     *
     * @param event a player typing in chat
     */
    @EventHandler(ignoreCancelled = true)
    public void onTISChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (plugin.getTrackerKeeper().getInfoMenu().containsKey(uuid)) {
            event.setCancelled(true);
            String chat = event.getMessage();
            // always exit if 'e' is pressed
            if (chat.equalsIgnoreCase("E")) {
                exit(p);
                return;
            }
            if (chat.length() == 1) {
                switch (plugin.getTrackerKeeper().getInfoMenu().get(uuid)) {
                    // TOP level menu
                    case TIS:
                        if (chat.equalsIgnoreCase("M")) {
                            processKey(p, TARDISInfoMenu.MANUAL);
                        }
                        if (chat.equalsIgnoreCase("I")) {
                            processKey(p, TARDISInfoMenu.ITEMS);
                        }
                        if (chat.equalsIgnoreCase("C")) {
                            processKey(p, TARDISInfoMenu.COMPONENTS);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            processKey(p, TARDISInfoMenu.SONIC_COMPONENTS);
                        }
                        if (chat.equalsIgnoreCase("D")) {
                            processKey(p, TARDISInfoMenu.DISKS);
                        }
                        if (chat.equalsIgnoreCase("O")) {
                            processKey(p, TARDISInfoMenu.COMMANDS);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            processKey(p, TARDISInfoMenu.ROOMS);
                        }
                        if (chat.equalsIgnoreCase("T")) {
                            processKey(p, TARDISInfoMenu.TYPES);
                        }
                        break;
                    // SECOND level menu
                    case CONSOLE_BLOCKS:
                        if (chat.equalsIgnoreCase("v")) {
                            showInfo(p, TARDISInfoMenu.ADVANCED);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            showInfo(p, TARDISInfoMenu.STORAGE);
                        }
                        if (chat.equalsIgnoreCase("A")) {
                            showInfo(p, TARDISInfoMenu.CONSOLE_ARS);
                        }
                        if (chat.equalsIgnoreCase("r")) {
                            showInfo(p, TARDISInfoMenu.ARTRON);
                        }
                        if (chat.equalsIgnoreCase("B")) {
                            showInfo(p, TARDISInfoMenu.BACKDOOR);
                        }
                        if (chat.equalsIgnoreCase("u")) {
                            showInfo(p, TARDISInfoMenu.BUTTON);
                        }
                        if (chat.equalsIgnoreCase("C")) {
                            showInfo(p, TARDISInfoMenu.CHAMELEON);
                        }
                        if (chat.equalsIgnoreCase("o")) {
                            showInfo(p, TARDISInfoMenu.CONDENSER);
                        }
                        if (chat.equalsIgnoreCase("p")) {
                            showInfo(p, TARDISInfoMenu.CREEPER);
                        }
                        if (chat.equalsIgnoreCase("D")) {
                            showInfo(p, TARDISInfoMenu.DOOR);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            showInfo(p, TARDISInfoMenu.EPS);
                        }
                        if (chat.equalsIgnoreCase("m")) {
                            showInfo(p, TARDISInfoMenu.CONSOLE_FARM);
                        }
                        if (chat.equalsIgnoreCase("k")) {
                            showInfo(p, TARDISInfoMenu.HANDBRAKE);
                        }
                        break;
                    case CONSOLE_BLOCKS_2:
                        if (chat.equalsIgnoreCase("o")) {
                            showInfo(p, TARDISInfoMenu.TOGGLE);
                        }
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.INFO);
                        }
                        if (chat.equalsIgnoreCase("K")) {
                            showInfo(p, TARDISInfoMenu.KEYBOARD);
                        }
                        if (chat.equalsIgnoreCase("L")) {
                            showInfo(p, TARDISInfoMenu.LIGHT);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showInfo(p, TARDISInfoMenu.CONSOLE_RAIL);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            showInfo(p, TARDISInfoMenu.SAVE_SIGN);
                        }
                        if (chat.equalsIgnoreCase("c")) {
                            showInfo(p, TARDISInfoMenu.SCANNER);
                        }
                        if (chat.equalsIgnoreCase("t")) {
                            showInfo(p, TARDISInfoMenu.CONSOLE_STABLE);
                        }
                        if (chat.equalsIgnoreCase("T")) {
                            showInfo(p, TARDISInfoMenu.TERMINAL);
                        }
                        if (chat.equalsIgnoreCase("m")) {
                            showInfo(p, TARDISInfoMenu.TEMPORAL);
                        }
                        if (chat.equalsIgnoreCase("W")) {
                            showInfo(p, TARDISInfoMenu.WORLD_REPEATER);
                        }
                        if (chat.equalsIgnoreCase("X")) {
                            showInfo(p, TARDISInfoMenu.X_REPEATER);
                        }
                        if (chat.equalsIgnoreCase("Y")) {
                            showInfo(p, TARDISInfoMenu.Y_REPEATER);
                        }
                        if (chat.equalsIgnoreCase("Z")) {
                            showInfo(p, TARDISInfoMenu.Z_REPEATER);
                        }
                        break;
                    case ITEMS:
                        if (chat.equalsIgnoreCase("A")) {
                            processKey(p, TARDISInfoMenu.CELL);
                        }
                        if (chat.equalsIgnoreCase("F")) {
                            processKey(p, TARDISInfoMenu.FILTER);
                        }
                        if (chat.equalsIgnoreCase("K")) {
                            processKey(p, TARDISInfoMenu.KEY);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            processKey(p, TARDISInfoMenu.SONIC);
                        }
                        if (chat.equalsIgnoreCase("L")) {
                            processKey(p, TARDISInfoMenu.LOCATOR);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            processKey(p, TARDISInfoMenu.REMOTE);
                        }
                        break;
                    case DISKS:
                        if (chat.equalsIgnoreCase("A")) {
                            showInfo(p, TARDISInfoMenu.AREA_DISK);
                        }
                        if (chat.equalsIgnoreCase("B")) {
                            processKey(p, TARDISInfoMenu.BLANK);
                        }
                        if (chat.equalsIgnoreCase("i")) {
                            processKey(p, TARDISInfoMenu.BIOME_DISK);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            processKey(p, TARDISInfoMenu.PLAYER_DISK);
                        }
                        if (chat.equalsIgnoreCase("r")) {
                            processKey(p, TARDISInfoMenu.PRESET_DISK);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            processKey(p, TARDISInfoMenu.SAVE_DISK);
                        }
                        break;
                    case COMPONENTS:
                        if (chat.equalsIgnoreCase("C")) {
                            processKey(p, TARDISInfoMenu.C_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("h")) {
                            processKey(p, TARDISInfoMenu.ARS_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("I")) {
                            processKey(p, TARDISInfoMenu.I_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("v")) {
                            processKey(p, TARDISInfoMenu.INVISIBLE_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("L")) {
                            processKey(p, TARDISInfoMenu.L_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("M")) {
                            processKey(p, TARDISInfoMenu.M_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            processKey(p, TARDISInfoMenu.P_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            processKey(p, TARDISInfoMenu.R_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("n")) {
                            processKey(p, TARDISInfoMenu.SCANNER_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("o")) {
                            processKey(p, TARDISInfoMenu.RANDOMISER_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            processKey(p, TARDISInfoMenu.S_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("T")) {
                            processKey(p, TARDISInfoMenu.T_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("y")) {
                            processKey(p, TARDISInfoMenu.MEMORY_CIRCUIT);
                        }
                        break;
                    case SONIC_COMPONENTS:
                        if (chat.equalsIgnoreCase("A")) {
                            processKey(p, TARDISInfoMenu.A_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("B")) {
                            processKey(p, TARDISInfoMenu.BIO_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("D")) {
                            processKey(p, TARDISInfoMenu.D_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("I")) {
                            processKey(p, TARDISInfoMenu.IGNITE_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("m")) {
                            processKey(p, TARDISInfoMenu.E_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("O")) {
                            processKey(p, TARDISInfoMenu.OSCILLATOR_CIRCUIT);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            processKey(p, TARDISInfoMenu.PAINTER_CIRCUIT);
                        }
                        break;
                    case MANUAL:
                        if (chat.equalsIgnoreCase("T")) {
                            processKey(p, TARDISInfoMenu.TIME_TRAVEL);
                        }
                        if (chat.equalsIgnoreCase("C")) {
                            processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS);
                        }
                        if (chat.equalsIgnoreCase("o")) {
                            processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS_2);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            processKey(p, TARDISInfoMenu.TARDIS_CONTROLS);
                        }
                        break;
                    case TARDIS_CONTROLS:
                        if (chat.equalsIgnoreCase("A")) {
                            processKey(p, TARDISInfoMenu.ARTRON);
                        }
                        if (chat.equalsIgnoreCase("T")) {
                            processKey(p, TARDISInfoMenu.TIME_TRAVEL);
                        }
                        if (chat.equalsIgnoreCase("M")) {
                            showInfo(p, TARDISInfoMenu.MALFUNCTIONS);
                        }
                        if (chat.equalsIgnoreCase("l")) {
                            showInfo(p, TARDISInfoMenu.ALT_CONTROLS);
                        }
                        break;
                    case TIME_TRAVEL:

                        break;
//                    case ALT_CONTROLS:
//                        if (chat.equalsIgnoreCase("S")) {
//                            processKey(p, TARDISInfoMenu.SAVE_SIGN);
//                        }
//                        if (chat.equalsIgnoreCase("K")) {
//                            showInfo(p, TARDISInfoMenu.KEYBOARD);
//                        }
//                        break;
                    case DOOR:
                        showInfo(p, TARDISInfoMenu.DOOR);
                        break;
                    case COMMANDS:
                        if (chat.equalsIgnoreCase("T")) {
                            processKey(p, TARDISInfoMenu.TARDIS);
                        }
                        if (chat.equalsIgnoreCase("A")) {
                            showCommand(p, TARDISInfoMenu.TARDISADMIN);
                        }
                        if (chat.equalsIgnoreCase("C")) {
                            processKey(p, TARDISInfoMenu.TARDISAREA);
                        }
                        if (chat.equalsIgnoreCase("B")) {
                            processKey(p, TARDISInfoMenu.TARDISBIND);
                        }
                        if (chat.equalsIgnoreCase("k")) {
                            showCommand(p, TARDISInfoMenu.TARDISBOOK);
                        }
                        if (chat.equalsIgnoreCase("G")) {
                            showCommand(p, TARDISInfoMenu.TARDISGRAVITY);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            processKey(p, TARDISInfoMenu.TARDISPREFS);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showCommand(p, TARDISInfoMenu.TARDISRECIPE);
                        }
                        if (chat.equalsIgnoreCase("o")) {
                            processKey(p, TARDISInfoMenu.TARDISROOM);
                        }
                        if (chat.equalsIgnoreCase("x")) {
                            processKey(p, TARDISInfoMenu.TARDISTEXTURE);
                        }
                        if (chat.equalsIgnoreCase("v")) {
                            processKey(p, TARDISInfoMenu.TARDISTRAVEL);
                        }
                        break;
                    case ROOMS:
                        if (chat.equalsIgnoreCase("A")) {
                            showRoomInfo(p, TARDISInfoMenu.ANTIGRAVITY);
                        }
                        if (chat.equalsIgnoreCase("u")) {
                            showRoomInfo(p, TARDISInfoMenu.ARBORETUM);
                        }
                        if (chat.equalsIgnoreCase("B")) {
                            showRoomInfo(p, TARDISInfoMenu.BAKER);
                        }
                        if (chat.equalsIgnoreCase("d")) {
                            showRoomInfo(p, TARDISInfoMenu.BEDROOM);
                        }
                        if (chat.equalsIgnoreCase("y")) {
                            showRoomInfo(p, TARDISInfoMenu.EMPTY);
                        }
                        if (chat.equalsIgnoreCase("F")) {
                            showRoomInfo(p, TARDISInfoMenu.FARM);
                        }
                        if (chat.equalsIgnoreCase("G")) {
                            showRoomInfo(p, TARDISInfoMenu.GRAVITY);
                        }
                        if (chat.equalsIgnoreCase("n")) {
                            showRoomInfo(p, TARDISInfoMenu.GREENHOUSE);
                        }
                        if (chat.equalsIgnoreCase("H")) {
                            showRoomInfo(p, TARDISInfoMenu.HARMONY);
                        }
                        if (chat.equalsIgnoreCase("K")) {
                            showRoomInfo(p, TARDISInfoMenu.KITCHEN);
                        }
                        if (chat.equalsIgnoreCase("L")) {
                            showRoomInfo(p, TARDISInfoMenu.LIBRARY);
                        }
                        if (chat.equalsIgnoreCase("M")) {
                            showRoomInfo(p, TARDISInfoMenu.MUSHROOM);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            showRoomInfo(p, TARDISInfoMenu.PASSAGE);
                        }
                        if (chat.equalsIgnoreCase("o")) {
                            showRoomInfo(p, TARDISInfoMenu.POOL);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRoomInfo(p, TARDISInfoMenu.RAIL);
                        }
                        if (chat.equalsIgnoreCase("x")) {
                            showRoomInfo(p, TARDISInfoMenu.RENDERER);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            showRoomInfo(p, TARDISInfoMenu.STABLE);
                        }
                        if (chat.equalsIgnoreCase("T")) {
                            showRoomInfo(p, TARDISInfoMenu.TRENZALORE);
                        }
                        if (chat.equalsIgnoreCase("V")) {
                            showRoomInfo(p, TARDISInfoMenu.VAULT);
                        }
                        if (chat.equalsIgnoreCase("W")) {
                            showRoomInfo(p, TARDISInfoMenu.WOOD);
                        }
                        if (chat.equalsIgnoreCase("h")) {
                            showRoomInfo(p, TARDISInfoMenu.WORKSHOP);
                        }
                        break;
                    case TYPES:
                        if (chat.equalsIgnoreCase("B")) {
                            showInfo(p, TARDISInfoMenu.BUDGET);
                        }
                        if (chat.equalsIgnoreCase("i")) {
                            showInfo(p, TARDISInfoMenu.BIGGER);
                        }
                        if (chat.equalsIgnoreCase("D")) {
                            showInfo(p, TARDISInfoMenu.DELUXE);
                        }
                        if (chat.equalsIgnoreCase("l")) {
                            showInfo(p, TARDISInfoMenu.ELEVENTH);
                        }
                        if (chat.equalsIgnoreCase("f")) {
                            showInfo(p, TARDISInfoMenu.TWELFTH);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showInfo(p, TARDISInfoMenu.REDSTONE);
                        }
                        if (chat.equalsIgnoreCase("S")) {
                            showInfo(p, TARDISInfoMenu.STEAMPUNK);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            showInfo(p, TARDISInfoMenu.PLANK);
                        }
                        if (chat.equalsIgnoreCase("T")) {
                            showInfo(p, TARDISInfoMenu.TOM);
                        }
                        if (chat.equalsIgnoreCase("A")) {
                            showInfo(p, TARDISInfoMenu.ARS);
                        }
                        if (chat.equalsIgnoreCase("W")) {
                            showInfo(p, TARDISInfoMenu.WAR);
                        }
                        if (chat.equalsIgnoreCase("P")) {
                            showInfo(p, TARDISInfoMenu.PYRAMID);
                        }
                        if (chat.equalsIgnoreCase("M")) {
                            showInfo(p, TARDISInfoMenu.MASTER);
                        }
                        if (chat.equalsIgnoreCase("C")) {
                            showInfo(p, TARDISInfoMenu.CUSTOM);
                        }
                        break;
                    // THIRD level menus
                    case KEY:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.KEY_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.KEY_RECIPE);
                        }
                        break;
                    case SONIC:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SONIC_INFO);
                        }
                        if (chat.equalsIgnoreCase("T")) {
                            processKey(p, TARDISInfoMenu.SONIC_TYPES);
                        }
                        break;
                    case LOCATOR:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.LOCATOR_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.LOCATOR_RECIPE);
                        }
                        break;
                    case REMOTE:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.REMOTE_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.REMOTE_RECIPE);
                        }
                        break;
                    case BIOME_DISK:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.BIOME_DISK_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.BIOME_DISK_RECIPE);
                        }
                        break;
                    case BLANK:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.BLANK_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.BLANK_RECIPE);
                        }
                        break;
                    case PLAYER_DISK:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.PLAYER_DISK_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.PLAYER_DISK_RECIPE);
                        }
                        break;
                    case PRESET_DISK:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.PRESET_DISK_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.PRESET_DISK_RECIPE);
                        }
                        break;
                    case SAVE_DISK:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SAVE_DISK_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.SAVE_DISK_RECIPE);
                        }
                        break;
                    case A_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.A_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.A_CIRCUIT_RECIPE);
                        }
                        break;
                    case BIO_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.BIO_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.BIO_CIRCUIT_RECIPE);
                        }
                        break;
                    case C_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.C_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.C_CIRCUIT_RECIPE);
                        }
                        break;
                    case D_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.D_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.D_CIRCUIT_RECIPE);
                        }
                        break;
                    case E_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.E_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.E_CIRCUIT_RECIPE);
                        }
                        break;
                    case CELL:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.CELL_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.CELL_RECIPE);
                        }
                        break;
                    case FILTER:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.FILTER_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.FILTER_RECIPE);
                        }
                        break;
                    case ARS_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.ARS_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.ARS_CIRCUIT_RECIPE);
                        }
                        break;
                    case I_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.I_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.I_CIRCUIT_RECIPE);
                        }
                        break;
                    case IGNITE_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.IGNITE_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.IGNITE_CIRCUIT_RECIPE);
                        }
                        break;
                    case INVISIBLE_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.INVISIBLE_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.INVISIBLE_RECIPE);
                        }
                        break;
                    case L_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.L_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.L_CIRCUIT_RECIPE);
                        }
                        break;
                    case M_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.M_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.M_CIRCUIT_RECIPE);
                        }
                        break;
                    case OSCILLATOR_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.OSCILLATOR_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.OSCILLATOR_RECIPE);
                        }
                        break;
                    case P_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.P_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.P_CIRCUIT_RECIPE);
                        }
                        break;
                    case PAINTER_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.PAINTER_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.PAINTER_RECIPE);
                        }
                        break;
                    case R_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.R_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.R_CIRCUIT_RECIPE);
                        }
                        break;
                    case RANDOMISER_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.RANDOMISER_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.RANDOMISER_CIRCUIT_RECIPE);
                        }
                        break;
                    case S_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.S_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.S_CIRCUIT_RECIPE);
                        }
                        break;
                    case T_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.T_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.T_CIRCUIT_RECIPE);
                        }
                        break;
                    case MEMORY_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.MEMORY_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.MEMORY_CIRCUIT_RECIPE);
                        }
                        break;
                    case SCANNER_CIRCUIT:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SCANNER_CIRCUIT_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.SCANNER_CIRCUIT_RECIPE);
                        }
                        break;
                    case TARDIS:
                        if (chat.equalsIgnoreCase("ab")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_ABORT);
                        }
                        if (chat.equalsIgnoreCase("a")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_ADD);
                        }
                        if (chat.equalsIgnoreCase("c")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_CHAMELEON);
                        }
                        if (chat.equalsIgnoreCase("com")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_COMEHERE);
                        }
                        if (chat.equalsIgnoreCase("d")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_DIRECTION);
                        }
                        if (chat.equalsIgnoreCase("x")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_EXTERMINATE);
                        }
                        if (chat.equalsIgnoreCase("f")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_FIND);
                        }
                        if (chat.equalsIgnoreCase("h")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_HIDE);
                        }
                        if (chat.equalsIgnoreCase("m")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_HOME);
                        }
                        if (chat.equalsIgnoreCase("i")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_INSIDE);
                        }
                        if (chat.equalsIgnoreCase("a")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_JETTISON);
                        }
                        if (chat.equalsIgnoreCase("la")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_LAMPS);
                        }
                        if (chat.equalsIgnoreCase("l")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_LIST);
                        }
                        if (chat.equalsIgnoreCase("k")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_NAMEKEY);
                        }
                        if (chat.equalsIgnoreCase("o")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_OCCUPY);
                        }
                        if (chat.equalsIgnoreCase("b")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_REBUILD);
                        }
                        if (chat.equalsIgnoreCase("r")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_REMOVE);
                        }
                        if (chat.equalsIgnoreCase("rem")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_REMOVESAVE);
                        }
                        if (chat.equalsIgnoreCase("u")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_RESCUE);
                        }
                        if (chat.equalsIgnoreCase("roo")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_ROOM);
                        }
                        if (chat.equalsIgnoreCase("s")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_SAVE);
                        }
                        if (chat.equalsIgnoreCase("n")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_SECONDARY);
                        }
                        if (chat.equalsIgnoreCase("t")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_SETDEST);
                        }
                        if (chat.equalsIgnoreCase("p")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_UPDATE);
                        }
                        if (chat.equalsIgnoreCase("v")) {
                            showCommand(p, TARDISInfoMenu.TARDIS_VERSION);
                        }
                        break;
                    case TARDISTRAVEL:
                        if (chat.equalsIgnoreCase("h")) {
                            showCommand(p, TARDISInfoMenu.TARDISTRAVEL_HOME);
                        }
                        if (chat.equalsIgnoreCase("p")) {
                            showCommand(p, TARDISInfoMenu.TARDISTRAVEL_PLAYER);
                        }
                        if (chat.equalsIgnoreCase("c")) {
                            showCommand(p, TARDISInfoMenu.TARDISTRAVEL_COORDS);
                        }
                        if (chat.equalsIgnoreCase("d")) {
                            showCommand(p, TARDISInfoMenu.TARDISTRAVEL_DEST);
                        }
                        if (chat.equalsIgnoreCase("b")) {
                            showCommand(p, TARDISInfoMenu.TARDISTRAVEL_BIOME);
                        }
                        if (chat.equalsIgnoreCase("a")) {
                            showCommand(p, TARDISInfoMenu.TARDISTRAVEL_AREA);
                        }
                        break;
                    case TARDISPREFS:
                        if (chat.equalsIgnoreCase("a")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_AUTO);
                        }
                        if (chat.equalsIgnoreCase("p")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_EPS);
                        }
                        if (chat.equalsIgnoreCase("f")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_FLOOR);
                        }
                        if (chat.equalsIgnoreCase("h")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_HADS);
                        }
                        if (chat.equalsIgnoreCase("i")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_ISOMORPHIC);
                        }
                        if (chat.equalsIgnoreCase("k")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_KEY);
                        }
                        if (chat.equalsIgnoreCase("l")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_LAMP);
                        }
                        if (chat.equalsIgnoreCase("m")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_MESSAGE);
                        }
                        if (chat.equalsIgnoreCase("n")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_PLAIN);
                        }
                        if (chat.equalsIgnoreCase("q")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_QUOTES);
                        }
                        if (chat.equalsIgnoreCase("s")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_SFX);
                        }
                        if (chat.equalsIgnoreCase("u")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_SUBMARINE);
                        }
                        if (chat.equalsIgnoreCase("w")) {
                            showCommand(p, TARDISInfoMenu.TARDISPREFS_WALL);
                        }
                        break;
                    case TARDISBIND:
                        if (chat.equalsIgnoreCase("s")) {
                            showCommand(p, TARDISInfoMenu.TARDISBIND_SAVE);
                        }
                        if (chat.equalsIgnoreCase("c")) {
                            showCommand(p, TARDISInfoMenu.TARDISBIND_CMD);
                        }
                        if (chat.equalsIgnoreCase("p")) {
                            showCommand(p, TARDISInfoMenu.TARDISBIND_PLAYER);
                        }
                        if (chat.equalsIgnoreCase("a")) {
                            showCommand(p, TARDISInfoMenu.TARDISBIND_AREA);
                        }
                        if (chat.equalsIgnoreCase("b")) {
                            showCommand(p, TARDISInfoMenu.TARDISBIND_BIOME);
                        }
                        if (chat.equalsIgnoreCase("r")) {
                            showCommand(p, TARDISInfoMenu.TARDISBIND_REMOVE);
                        }
                        break;
                    case TARDISTEXTURE:
                        if (chat.equalsIgnoreCase("o")) {
                            showCommand(p, TARDISInfoMenu.TARDISTEXTURE_ON);
                        }
                        if (chat.equalsIgnoreCase("f")) {
                            showCommand(p, TARDISInfoMenu.TARDISTEXTURE_OFF);
                        }
                        if (chat.equalsIgnoreCase("i")) {
                            showCommand(p, TARDISInfoMenu.TARDISTEXTURE_IN);
                        }
                        if (chat.equalsIgnoreCase("u")) {
                            showCommand(p, TARDISInfoMenu.TARDISTEXTURE_OUT);
                        }
                        break;
                    case TARDISAREA:
                        if (chat.equalsIgnoreCase("s")) {
                            showCommand(p, TARDISInfoMenu.TARDISAREA_START);
                        }
                        if (chat.equalsIgnoreCase("n")) {
                            showCommand(p, TARDISInfoMenu.TARDISAREA_END);
                        }
                        if (chat.equalsIgnoreCase("h")) {
                            showCommand(p, TARDISInfoMenu.TARDISAREA_SHOW);
                        }
                        if (chat.equalsIgnoreCase("r")) {
                            showCommand(p, TARDISInfoMenu.TARDISAREA_REMOVE);
                        }
                        break;
                    case TARDISROOM:
                        if (chat.equalsIgnoreCase("a")) {
                            showCommand(p, TARDISInfoMenu.TARDISROOM_ADD);
                        }
                        if (chat.equalsIgnoreCase("s")) {
                            showCommand(p, TARDISInfoMenu.TARDISROOM_SEED);
                        }
                        if (chat.equalsIgnoreCase("c")) {
                            showCommand(p, TARDISInfoMenu.TARDISROOM_COST);
                        }
                        if (chat.equalsIgnoreCase("o")) {
                            showCommand(p, TARDISInfoMenu.TARDISROOM_OFFSET);
                        }
                        if (chat.equalsIgnoreCase("n")) {
                            showCommand(p, TARDISInfoMenu.TARDISROOM_ENABLED);
                        }
                        break;
                    // FOURTH level menus
                    case SONIC_TYPES:
                        if (chat.equalsIgnoreCase("Q")) {
                            processKey(p, TARDISInfoMenu.SONIC_Q);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            processKey(p, TARDISInfoMenu.SONIC_R);
                        }
                        if (chat.equalsIgnoreCase("D")) {
                            processKey(p, TARDISInfoMenu.SONIC_D);
                        }
                        if (chat.equalsIgnoreCase("m")) {
                            processKey(p, TARDISInfoMenu.SONIC_E);
                        }
                        if (chat.equalsIgnoreCase("A")) {
                            processKey(p, TARDISInfoMenu.SONIC_A);
                        }
                        break;
                    // FIFTH level menus - I've a feeling this is too deep!
                    case SONIC_Q:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SONIC_Q_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.SONIC_RECIPE);
                        }
                        break;
                    case SONIC_R:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SONIC_R_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.R_CIRCUIT_RECIPE);
                        }
                        break;
                    case SONIC_D:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SONIC_D_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.D_CIRCUIT_RECIPE);
                        }
                        break;
                    case SONIC_E:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SONIC_E_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.E_CIRCUIT_RECIPE);
                        }
                        break;
                    case SONIC_A:
                        if (chat.equalsIgnoreCase("I")) {
                            showInfo(p, TARDISInfoMenu.SONIC_A_INFO);
                        }
                        if (chat.equalsIgnoreCase("R")) {
                            showRecipe(p, TARDISInfoMenu.A_CIRCUIT_RECIPE);
                        }
                        break;
                    default:
                        exit(p);
                        break;
                }
            } else {
                TARDISMessage.send(p, "TIS_EXIT");
            }
        }
    }

    /**
     * Displays the next menu level based on the parent menu item that was
     * selected. Automatically pulls the key code and highlights it.
     *
     * @param p the player to show the menu to
     * @param item the parent menu item to get the children of
     */
    private void processKey(Player p, TARDISInfoMenu item) {
        plugin.getTrackerKeeper().getInfoMenu().put(p.getUniqueId(), item);
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        for (Map.Entry<String, String> m : TARDISInfoMenu.getChildren(item.toString()).entrySet()) {
            String menu = m.getKey().replaceFirst(m.getValue(), "§f" + m.getValue() + "§6");
            p.sendMessage("§6> " + menu);
        }
        p.sendMessage("§6> §fE§6xit");
    }

    /**
     * Displays information about a TARDIS room. Descriptions are stored in the
     * TARDISDescription enum. Other values are pulled directly from the
     * rooms.yml configuration file.
     *
     * @param p the player to show the room information to
     * @param item the room to display
     */
    private void showRoomInfo(Player p, TARDISInfoMenu item) {
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        p.sendMessage("§6" + TARDISDescription.valueOf(item.toString()).getDesc());
        String r = item.toString();
        p.sendMessage("§6Seed Block: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"));
        p.sendMessage("§6Offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"));
        p.sendMessage("§6Cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"));
        p.sendMessage("§6Enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"));
        exit(p);
    }

    /**
     * Displays information about an item or TARDIS type. Descriptions are
     * stored in the TARDISDescription enum.
     *
     * @param p the player to show the information to
     * @param item the item or TARDIS type to display
     */
    private void showInfo(Player p, TARDISInfoMenu item) {
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        p.sendMessage("§6" + TARDISDescription.valueOf(item.toString()).getDesc());
        exit(p);
    }

    /**
     * Displays the workbench recipe for an item or component.
     *
     * @param p the player to show the recipe to
     * @param item the recipe to display
     */
    private void showRecipe(Player p, TARDISInfoMenu item) {
        // do stuff
        String[] r = item.toString().split("_");
        String recipe = (r.length > 2) ? r[0] + "-" + r[1] : r[0];
        p.performCommand("tardisrecipe " + recipe);
        exit(p);
    }

    /**
     * Displays the description and usage of a command. Values are pulled
     * directly from the plugin.yml configuration file.
     *
     * @param p the player to show the command information to
     * @param item the command to display
     */
    private void showCommand(Player p, TARDISInfoMenu item) {
        String[] c = item.toString().toLowerCase(Locale.ENGLISH).split("_");
        String desc;
        String usage;
        if (c.length > 1) {
            desc = pluginYml.getString("commands." + c[0] + "." + c[1] + ".description");
            usage = pluginYml.getString("commands." + c[0] + "." + c[1] + ".usage").replace("<command>", c[0]);
        } else {
            desc = pluginYml.getString("commands." + c[0] + ".description");
            usage = pluginYml.getString("commands." + c[0] + ".usage").replace("<command>", c[0]);
        }
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        p.sendMessage("§6Description: " + desc);
        p.sendMessage("§6Usage: " + usage);
        exit(p);
    }

    /**
     * Exits the TARDIS Information System menu
     *
     * @param p the player to exit
     */
    private void exit(Player p) {
        plugin.getTrackerKeeper().getInfoMenu().remove(p.getUniqueId());
        p.sendMessage("§6---");
        TARDISMessage.send(p, "LOGGED_OUT_INFO");
    }
}
