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
package me.eccentric_nz.TARDIS.info;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;
import java.util.UUID;

/**
 * The TARDIS information system is a searchable database which was discovered by the Fifth Doctor's companions Nyssa
 * and Tegan from a readout in the control room. The Fifth Doctor called it the TARDIS data bank.
 *
 * @author bootthanoo, eccentric_nz
 */
public class TARDISInformationSystemListener implements Listener {

    private final TARDIS plugin;

    public TARDISInformationSystemListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static void processInput(Player p, UUID uuid, String chat, TARDIS plugin) {
        switch (plugin.getTrackerKeeper().getInfoMenu().get(uuid)) {
            // TOP level menu
            case TIS -> {
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TARDISInfoMenu.MANUAL, plugin);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TARDISInfoMenu.ITEMS, plugin);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.COMPONENTS, plugin);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SONIC_COMPONENTS, plugin);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TARDISInfoMenu.DISKS, plugin);
                }
                if (chat.equalsIgnoreCase("O")) {
                    processKey(p, TARDISInfoMenu.COMMANDS, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.ROOMS, plugin);
                }
                if (chat.equalsIgnoreCase("2")) {
                    processKey(p, TARDISInfoMenu.ROOMS_2, plugin);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TYPES, plugin);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.FOOD_ACCESSORIES, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PLANETS, plugin);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.MONSTERS, plugin);
                }
            }
            // SECOND level menu
            case CONSOLE_BLOCKS -> {
                if (chat.equalsIgnoreCase("v")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ADVANCED);
                }
                if (chat.equalsIgnoreCase("S")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.STORAGE);
                }
                if (chat.equalsIgnoreCase("A")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CONSOLE_ARS);
                }
                if (chat.equalsIgnoreCase("r")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON);
                }
                if (chat.equalsIgnoreCase("B")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BACKDOOR);
                }
                if (chat.equalsIgnoreCase("u")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BUTTON);
                }
                if (chat.equalsIgnoreCase("C")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CHAMELEON);
                }
                if (chat.equalsIgnoreCase("o")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CONDENSER);
                }
                if (chat.equalsIgnoreCase("D")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DOOR);
                }
                if (chat.equalsIgnoreCase("k")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HANDBRAKE);
                }
                exit(p, plugin);
            }
            case CONSOLE_BLOCKS_2 -> {
                if (chat.equalsIgnoreCase("o")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TOGGLE);
                }
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.INFO);
                }
                if (chat.equalsIgnoreCase("K")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.KEYBOARD);
                }
                if (chat.equalsIgnoreCase("L")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.LIGHT);
                }
                if (chat.equalsIgnoreCase("S")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SAVE_SIGN);
                }
                if (chat.equalsIgnoreCase("c")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SCANNER);
                }
                if (chat.equalsIgnoreCase("T")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TERMINAL);
                }
                if (chat.equalsIgnoreCase("m")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TEMPORAL);
                }
                if (chat.equalsIgnoreCase("W")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.WORLD_REPEATER);
                }
                if (chat.equalsIgnoreCase("X")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.X_REPEATER);
                }
                if (chat.equalsIgnoreCase("Y")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.Y_REPEATER);
                }
                if (chat.equalsIgnoreCase("Z")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.Z_REPEATER);
                }
                exit(p, plugin);
            }
            case CONSOLE_BLOCKS_3 -> {
                if (chat.equalsIgnoreCase("x")) {
                    processKey(p, TARDISInfoMenu.EXTERIOR_LAMP_LEVEL_SWITCH, plugin);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TARDISInfoMenu.INTERIOR_LIGHT_LEVEL_SWITCH, plugin);
                }
                if (chat.equalsIgnoreCase("C")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CHARGING_SENSOR);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("l")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FLIGHT_SENSOR);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("H")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HANDBRAKE_SENSOR);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MALFUNCTION_SENSOR);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.POWER_SENSOR);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.TARDIS_MONITOR, plugin);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.MONITOR_FRAME, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RELATIVITY_DIFFERENTIATOR);
                    exit(p, plugin);
                }
            }
            case UPDATEABLE_BLOCKS -> {
                if (chat.equalsIgnoreCase("C")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CREEPER);
                }
                if (chat.equalsIgnoreCase("P")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.EPS);
                }
                if (chat.equalsIgnoreCase("m")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_FARM);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_RAIL);
                }
                if (chat.equalsIgnoreCase("b")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_STABLE);
                }
                if (chat.equalsIgnoreCase("Ll")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_STALL);
                }
                if (chat.equalsIgnoreCase("y")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_ALLAY);
                }
                if (chat.equalsIgnoreCase("B")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_BAMBOO);
                }
                if (chat.equalsIgnoreCase("g")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_BIRDCAGE);
                }
                if (chat.equalsIgnoreCase("u")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_FUEL);
                }
                if (chat.equalsIgnoreCase("H")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_HUTCH);
                }
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_IGLOO);
                }
                if (chat.equalsIgnoreCase("Ii")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_IISTUBIL);
                }
                if (chat.equalsIgnoreCase("v")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_LAVA);
                }
                if (chat.equalsIgnoreCase("n")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_PEN);
                }
                if (chat.equalsIgnoreCase("S")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_SMELT);
                }
                if (chat.equalsIgnoreCase("Va")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_VAULT);
                }
                if (chat.equalsIgnoreCase("Vi")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.UPDATEABLE_VILLAGE);
                }
                exit(p, plugin);
            }
            case ITEMS -> {
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.ARTRON_STORAGE_CELL, plugin);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.ARTRON_CAPACITOR, plugin);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.ARTRON_CAPACITOR_STORAGE, plugin);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BIOME_READER, plugin);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.PERCEPTION_FILTER, plugin);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TARDISInfoMenu.KEY, plugin);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TARDISInfoMenu.REMOTE_KEY, plugin);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SONIC_SCREWDRIVER, plugin);
                }
                if (chat.equalsIgnoreCase("L")) {
                    processKey(p, TARDISInfoMenu.LOCATOR, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.STATTENHEIM_REMOTE, plugin);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TARDIS_TELEVISION, plugin);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.ARTRON_FURNACE, plugin);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TARDISInfoMenu.SONIC_GENERATOR, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.SONIC_BLASTER, plugin);
                }
                if (chat.equalsIgnoreCase("V")) {
                    processKey(p, TARDISInfoMenu.VORTEX_MANIPULATOR, plugin);
                }
                if (chat.equalsIgnoreCase("H")) {
                    processKey(p, TARDISInfoMenu.HANDLES, plugin);
                }
            }
            case DISKS -> {
                if (chat.equalsIgnoreCase("A")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.AREA_DISK);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BLANK_STORAGE_DISK, plugin);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.AUTHORISED_CONTROL_DISK, plugin);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TARDISInfoMenu.BIOME_STORAGE_DISK, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PLAYER_STORAGE_DISK, plugin);
                }
                if (chat.equalsIgnoreCase("r")) {
                    processKey(p, TARDISInfoMenu.PRESET_STORAGE_DISK, plugin);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SAVE_STORAGE_DISK, plugin);
                }
            }
            case COMPONENTS -> {
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.CHAMELEON_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("h")) {
                    processKey(p, TARDISInfoMenu.ARS_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TARDISInfoMenu.INPUT_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("v")) {
                    processKey(p, TARDISInfoMenu.INVISIBILITY_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("L")) {
                    processKey(p, TARDISInfoMenu.LOCATOR_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TARDISInfoMenu.MATERIALISATION_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PERCEPTION_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.SCANNER_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.RANDOMISER_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.STATTENHEIM_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TEMPORAL_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("y")) {
                    processKey(p, TARDISInfoMenu.MEMORY_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("f")) {
                    processKey(p, TARDISInfoMenu.RIFT_CIRCUIT, plugin);
                }
            }
            case SONIC_COMPONENTS -> {
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TARDISInfoMenu.IGNITE_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("O")) {
                    processKey(p, TARDISInfoMenu.SONIC_OSCILLATOR, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PAINTER_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.BRUSH_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.CONVERSION_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT, plugin);
                }
            }
            case MANUAL -> {
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TIME_TRAVEL, plugin);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS_2, plugin);
                }
                if (chat.equalsIgnoreCase("k")) {
                    processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS_3, plugin);
                }
                if (chat.equalsIgnoreCase("U")) {
                    processKey(p, TARDISInfoMenu.UPDATEABLE_BLOCKS, plugin);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.TARDIS_CONTROLS, plugin);
                }
            }
            case TARDIS_CONTROLS -> {
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.ARTRON, plugin);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TIME_TRAVEL, plugin);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MALFUNCTIONS);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("l")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ALT_CONTROLS);
                    exit(p, plugin);
                }
            }
            case TIME_TRAVEL -> {
                if (chat.equalsIgnoreCase("F")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FLYING);
                }
                if (chat.equalsIgnoreCase("C")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CAMERA);
                }
                if (chat.equalsIgnoreCase("A")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ALT_CONTROLS);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MALFUNCTIONS);
                }
            }
            case MONSTERS -> {
                if (chat.equalsIgnoreCase("C")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CYBERMAN);
                }
                if (chat.equalsIgnoreCase("D")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DALEK);
                }
                if (chat.equalsIgnoreCase("a")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DALEK_SEC);
                }
                if (chat.equalsIgnoreCase("av")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DAVROS);
                }
                if (chat.equalsIgnoreCase("p")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.EMPTY_CHILD);
                }
                if (chat.equalsIgnoreCase("H")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HATH);
                }
                if (chat.equalsIgnoreCase("k")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HEADLESS_MONK);
                }
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ICE_WARRIOR);
                }
                if (chat.equalsIgnoreCase("J")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.JUDOON);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TARDISInfoMenu.K9, plugin);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MIRE);
                }
                if (chat.equalsIgnoreCase("O")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.OOD);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RACNOSS);
                }
                if (chat.equalsIgnoreCase("S")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SEA_DEVIL);
                }
                if (chat.equalsIgnoreCase("l")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SILENT);
                }
                if (chat.equalsIgnoreCase("n")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SILURIAN);
                }
                if (chat.equalsIgnoreCase("li")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SLITHEEN);
                }
                if (chat.equalsIgnoreCase("on")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONTARAN);
                }
                if (chat.equalsIgnoreCase("x")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.STRAX);
                }
                if (chat.equalsIgnoreCase("f")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TOCLAFANE);
                }
                if (chat.equalsIgnoreCase("V")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.VASHTA_NERADA);
                }
                if (chat.equalsIgnoreCase("W")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.WEEPING_ANGEL);
                }
                if (chat.equalsIgnoreCase("Z")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ZYGON);
                }
                if (!chat.equalsIgnoreCase("K")) {
                    exit(p, plugin);
                }
            }
            case DOOR -> new TISInfo(plugin).show(p, TARDISInfoMenu.DOOR);
            case COMMANDS -> {
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TARDIS, plugin);
                }
                if (chat.equalsIgnoreCase("A")) {
                    showCommand(p, TARDISInfoMenu.TARDISADMIN, plugin);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.TARDISAREA, plugin);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.TARDISBIND, plugin);
                }
                if (chat.equalsIgnoreCase("k")) {
                    showCommand(p, TARDISInfoMenu.TARDISBOOK, plugin);
                }
                if (chat.equalsIgnoreCase("G")) {
                    showCommand(p, TARDISInfoMenu.TARDISGRAVITY, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.TARDISPREFS, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showCommand(p, TARDISInfoMenu.TARDISRECIPE, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.TARDISROOM, plugin);
                }
                if (chat.equalsIgnoreCase("v")) {
                    processKey(p, TARDISInfoMenu.TARDISTRAVEL, plugin);
                }
            }
            case ROOMS -> {
                if (chat.equalsIgnoreCase("^")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.ALLAY);
                }
                if (chat.equalsIgnoreCase("ia")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.APIARY);
                }
                if (chat.equalsIgnoreCase("oo")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.BAMBOO);
                }
                if (chat.equalsIgnoreCase("he")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.CHEMISTRY);
                }
                if (chat.equalsIgnoreCase("Cl")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.CLOISTER);
                }
                if (chat.equalsIgnoreCase("a")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.GALLERY);
                }
                if (chat.equalsIgnoreCase("de")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.GARDEN);
                }
                if (chat.equalsIgnoreCase("eo")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.GEODE);
                }
                if (chat.equalsIgnoreCase("ut")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.HUTCH);
                }
                if (chat.equalsIgnoreCase("gl")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.IGLOO);
                }
                if (chat.equalsIgnoreCase("Ii")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.IISTUBIL);
                }
                if (chat.equalsIgnoreCase(".")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.LAUNDRY);
                }
                if (chat.equalsIgnoreCase("av")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.LAVA);
                }
                if (chat.equalsIgnoreCase("-")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.ANTIGRAVITY);
                }
                if (chat.equalsIgnoreCase("q")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.AQUARIUM);
                }
                if (chat.equalsIgnoreCase("u")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.ARBORETUM);
                }
                if (chat.equalsIgnoreCase("l")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.ARCHITECTURAL);
                }
                if (chat.equalsIgnoreCase("B")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.BAKER);
                }
                if (chat.equalsIgnoreCase("d")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.BEDROOM);
                }
                if (chat.equalsIgnoreCase("c")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.BIRDCAGE);
                }
                if (chat.equalsIgnoreCase("y")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.EMPTY);
                }
                if (chat.equalsIgnoreCase("*")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.EYE);
                }
                if (chat.equalsIgnoreCase("F")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.FARM);
                }
                if (chat.equalsIgnoreCase("G")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.GRAVITY);
                }
                if (chat.equalsIgnoreCase("n")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.GREENHOUSE);
                }
                if (chat.equalsIgnoreCase("y")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.HAPPY);
                }
                if (chat.equalsIgnoreCase("H")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.HARMONY);
                }
                if (chat.equalsIgnoreCase("K")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.KITCHEN);
                }
                exit(p, plugin);
            }
            case ROOMS_2 -> {
                if (chat.equalsIgnoreCase("za")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.LAZARUS);
                }
                if (chat.equalsIgnoreCase("ng")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.MANGROVE);
                }
                if (chat.equalsIgnoreCase("ze")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.MAZE);
                }
                if (chat.equalsIgnoreCase("th")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.NETHER);
                }
                if (chat.equalsIgnoreCase("y")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.OBSERVATORY);
                }
                if (chat.equalsIgnoreCase("Pe")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.PEN);
                }
                if (chat.equalsIgnoreCase("lt")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.SMELTER);
                }
                if (chat.equalsIgnoreCase("rg")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.SURGERY);
                }
                if (chat.equalsIgnoreCase("i")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.VILLAGE);
                }
                if (chat.equalsIgnoreCase("er")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.ZERO);
                }
                if (chat.equalsIgnoreCase("L")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.LIBRARY);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.MUSHROOM);
                }
                if (chat.equalsIgnoreCase("P")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.PASSAGE);
                }
                if (chat.equalsIgnoreCase("o")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.POOL);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.RAIL);
                }
                if (chat.equalsIgnoreCase("x")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.RENDERER);
                }
                if (chat.equalsIgnoreCase("Sh")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.SHELL);
                }
                if (chat.equalsIgnoreCase("S")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.STABLE);
                }
                if (chat.equalsIgnoreCase("c")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.STAIRCASE);
                }
                if (chat.equalsIgnoreCase("Ll")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.STALL);
                }
                if (chat.equalsIgnoreCase("T")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.TRENZALORE);
                }
                if (chat.equalsIgnoreCase("V")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.VAULT);
                }
                if (chat.equalsIgnoreCase("b")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.WARDROBE);
                }
                if (chat.equalsIgnoreCase("W")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.WOOD);
                }
                if (chat.equalsIgnoreCase("h")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.WORKSHOP);
                }
                if (chat.equalsIgnoreCase("u")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.NAUTILUS);
                }
                exit(p, plugin);
            }
            case TYPES -> {
                if (chat.equalsIgnoreCase("B")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BUDGET);
                }
                if (chat.equalsIgnoreCase("i")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BIGGER);
                }
                if (chat.equalsIgnoreCase("D")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DELUXE);
                }
                if (chat.equalsIgnoreCase("8")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.EIGHTH);
                }
                if (chat.equalsIgnoreCase("l")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ELEVENTH);
                }
                if (chat.equalsIgnoreCase("f")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TWELFTH);
                }
                if (chat.equalsIgnoreCase("n")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.THIRTEENTH);
                }
                if (chat.equalsIgnoreCase("y")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FACTORY);
                }
                if (chat.equalsIgnoreCase("u")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FUGITIVE);
                }
                if (chat.equalsIgnoreCase("+")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HOSPITAL);
                }
                if (chat.equalsIgnoreCase("+")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CURSED);
                }
                if (chat.equalsIgnoreCase("*")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FIFTEENTH);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.REDSTONE);
                }
                if (chat.equalsIgnoreCase("^")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ROTOR);
                }
                if (chat.equalsIgnoreCase("$")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RUSTIC);
                }
                if (chat.equalsIgnoreCase("@")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SIDRAT);
                }
                if (chat.equalsIgnoreCase("S")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.STEAMPUNK);
                }
                if (chat.equalsIgnoreCase("P")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PLANK);
                }
                if (chat.equalsIgnoreCase("T")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TOM);
                }
                if (chat.equalsIgnoreCase("A")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARS);
                }
                if (chat.equalsIgnoreCase("W")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.WAR);
                }
                if (chat.equalsIgnoreCase("y")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PYRAMID);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MASTER);
                }
                if (chat.equalsIgnoreCase("^")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MECHANICAL);
                }
                if (chat.equalsIgnoreCase("C")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CUSTOM);
                }
                if (chat.equalsIgnoreCase("d")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ENDER);
                }
                if (chat.equalsIgnoreCase("o")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CORAL);
                }
                if (chat.equalsIgnoreCase("1")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.COPPER_11TH);
                }
                if (chat.equals("=")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DELTA);
                }
                if (chat.equals("/")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DIVISION);
                }
                if (chat.equalsIgnoreCase("v")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CAVE);
                }
                if (chat.equalsIgnoreCase("h")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.WEATHERED);
                }
                if (chat.equalsIgnoreCase("g")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ORIGINAL);
                }
                if (chat.equals("*")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ANCIENT);
                }
                if (chat.equals("!")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BONE);
                }
                exit(p, plugin);
            }
            case FOOD_ACCESSORIES -> {
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BOW_TIE, plugin);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.CUSTARD, plugin);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TARDISInfoMenu.JAMMY_DODGER, plugin);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.FISH_FINGER, plugin);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TARDISInfoMenu.THREE_D_GLASSES, plugin);
                }
                if (chat.equalsIgnoreCase("J")) {
                    processKey(p, TARDISInfoMenu.JELLY_BABY, plugin);
                }
                if (chat.equalsIgnoreCase("W")) {
                    processKey(p, TARDISInfoMenu.FOB_WATCH, plugin);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.COMMUNICATOR, plugin);
                }
                if (chat.equalsIgnoreCase("H")) {
                    processKey(p, TARDISInfoMenu.SPACE_HELMET, plugin);
                }
            }
            case PLANETS -> {
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SKARO, plugin);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TARDISInfoMenu.SILURIA, plugin);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TARDISInfoMenu.GALLIFREY, plugin);
                }
            }
            // THIRD level menus
            case KEY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.KEY_INFO);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.KEY_RECIPE);
                }
            }
            case SONIC_SCREWDRIVER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_SCREWDRIVER_INFO);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.SONIC_TYPES, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_SCREWDRIVER_RECIPE);
                    exit(p, plugin);
                }
            }
            case LOCATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.LOCATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.LOCATOR_RECIPE);
                }
                exit(p, plugin);
            }
            case STATTENHEIM_REMOTE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.STATTENHEIM_REMOTE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.STATTENHEIM_REMOTE_RECIPE);
                }
                exit(p, plugin);
            }
            case BIOME_READER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BIOME_READER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIOME_READER_RECIPE);
                }
                exit(p, plugin);
            }
            case REMOTE_KEY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.REMOTE_KEY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.REMOTE_KEY_RECIPE);
                }
                exit(p, plugin);
            }
            case ARTRON_FURNACE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_FURNACE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_FURNACE_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_GENERATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_GENERATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_GENERATOR_RECIPE);
                }
                exit(p, plugin);
            }
            case THREE_D_GLASSES -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.THREE_D_GLASSES_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.THREE_D_GLASSES_RECIPE);
                }
                exit(p, plugin);
            }
            case BOW_TIE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BOW_TIE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BOW_TIE_RECIPE);
                }
                exit(p, plugin);
            }
            case CUSTARD -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CUSTARD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.CUSTARD_RECIPE);
                }
                exit(p, plugin);
            }
            case FISH_FINGER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FISH_FINGER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.FISH_FINGER_RECIPE);
                }
                exit(p, plugin);
            }
            case JELLY_BABY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.JELLY_BABY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.JELLY_BABY_RECIPE);
                }
                exit(p, plugin);
            }
            case JAMMY_DODGER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.JAMMY_DODGER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.JAMMY_DODGER_RECIPE);
                }
                exit(p, plugin);
            }
            case FOB_WATCH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FOB_WATCH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.FOB_WATCH_RECIPE);
                }
                exit(p, plugin);
            }
            case COMMUNICATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.COMMUNICATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.COMMUNICATOR_RECIPE);
                }
                exit(p, plugin);
            }
            case SPACE_HELMET -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SPACE_HELMET_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SPACE_HELMET_RECIPE);
                }
                exit(p, plugin);
            }
            case BIOME_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BIOME_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIOME_STORAGE_DISK_RECIPE);
                }
                exit(p, plugin);
            }
            case AUTHORISED_CONTROL_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.AUTHORISED_CONTROL_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.AUTHORISED_CONTROL_DISK_RECIPE);
                }
                exit(p, plugin);
            }
            case BLANK_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BLANK_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BLANK_STORAGE_DISK_RECIPE);
                }
                exit(p, plugin);
            }
            case PLAYER_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PLAYER_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PLAYER_STORAGE_DISK_RECIPE);
                }
                exit(p, plugin);
            }
            case PRESET_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PRESET_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PRESET_STORAGE_DISK_RECIPE);
                }
                exit(p, plugin);
            }
            case SAVE_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SAVE_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SAVE_STORAGE_DISK_RECIPE);
                }
                exit(p, plugin);
            }
            case SERVER_ADMIN_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case BIO_SCANNER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case CHAMELEON_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CHAMELEON_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.CHAMELEON_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case DIAMOND_DISRUPTOR_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case EMERALD_ENVIRONMENT_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case ARTRON_CAPACITOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_RECIPE);
                }
                exit(p, plugin);
            }
            case ARTRON_CAPACITOR_STORAGE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_STORAGE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_STORAGE_RECIPE);
                }
                exit(p, plugin);
            }
            case ARTRON_STORAGE_CELL -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_STORAGE_CELL_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_STORAGE_CELL_RECIPE);
                }
                exit(p, plugin);
            }
            case PERCEPTION_FILTER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PERCEPTION_FILTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PERCEPTION_FILTER_RECIPE);
                }
                exit(p, plugin);
            }
            case ARS_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARS_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARS_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case INPUT_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.INPUT_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.INPUT_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case IGNITE_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.IGNITE_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.IGNITE_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case KNOCKBACK_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case INVISIBILITY_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.INVISIBILITY_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.INVISIBILITY_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case LOCATOR_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.LOCATOR_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.LOCATOR_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case MATERIALISATION_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MATERIALISATION_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.MATERIALISATION_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_OSCILLATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_OSCILLATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_OSCILLATOR_RECIPE);
                }
                exit(p, plugin);
            }
            case PERCEPTION_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PERCEPTION_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PERCEPTION_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case PAINTER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PAINTER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PAINTER_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case REDSTONE_ACTIVATOR_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case RANDOMISER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RANDOMISER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RANDOMISER_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case STATTENHEIM_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.STATTENHEIM_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.STATTENHEIM_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case TEMPORAL_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TEMPORAL_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.TEMPORAL_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case MEMORY_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MEMORY_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.MEMORY_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SCANNER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SCANNER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SCANNER_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case PICKUP_ARROWS_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case BRUSH_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BRUSH_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BRUSH_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case CONVERSION_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CONVERSION_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.CONVERSION_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case EXTERIOR_LAMP_LEVEL_SWITCH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.EXTERIOR_LAMP_LEVEL_SWITCH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.EXTERIOR_LAMP_LEVEL_SWITCH_RECIPE);
                }
                exit(p, plugin);
            }
            case INTERIOR_LIGHT_LEVEL_SWITCH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.INTERIOR_LIGHT_LEVEL_SWITCH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.INTERIOR_LIGHT_LEVEL_SWITCH_RECIPE);
                }
                exit(p, plugin);
            }
            case HANDLES -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HANDLES_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.HANDLES_RECIPE);
                }
                exit(p, plugin);
            }
            case TARDIS_MONITOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TARDIS_MONITOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.TARDIS_MONITOR_RECIPE);
                }
                exit(p, plugin);
            }
            case MONITOR_FRAME -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MONITOR_FRAME_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.MONITOR_FRAME_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_BLASTER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_BLASTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_BLASTER_RECIPE);
                }
                exit(p, plugin);
            }
            case VORTEX_MANIPULATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.VORTEX_MANIPULATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.VORTEX_MANIPULATOR_RECIPE);
                }
                exit(p, plugin);
            }
            case K9 -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.K9);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.K9_RECIPE);
                }
                exit(p, plugin);
            }
            case TARDIS -> {
                if (chat.equalsIgnoreCase("ab")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_ABORT, plugin);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_ADD, plugin);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_CHAMELEON, plugin);
                }
                if (chat.equalsIgnoreCase("com")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_COMEHERE, plugin);
                }
                if (chat.equalsIgnoreCase("d")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_DIRECTION, plugin);
                }
                if (chat.equalsIgnoreCase("x")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_EXTERMINATE, plugin);
                }
                if (chat.equalsIgnoreCase("f")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_FIND, plugin);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_HIDE, plugin);
                }
                if (chat.equalsIgnoreCase("m")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_HOME, plugin);
                }
                if (chat.equalsIgnoreCase("i")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_INSIDE, plugin);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_JETTISON, plugin);
                }
                if (chat.equalsIgnoreCase("la")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_LAMPS, plugin);
                }
                if (chat.equalsIgnoreCase("l")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_LIST, plugin);
                }
                if (chat.equalsIgnoreCase("k")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_NAMEKEY, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_OCCUPY, plugin);
                }
                if (chat.equalsIgnoreCase("b")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_REBUILD, plugin);
                }
                if (chat.equalsIgnoreCase("r")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_REMOVE, plugin);
                }
                if (chat.equalsIgnoreCase("rem")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_REMOVESAVE, plugin);
                }
                if (chat.equalsIgnoreCase("u")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_RESCUE, plugin);
                }
                if (chat.equalsIgnoreCase("roo")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_ROOM, plugin);
                }
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_SAVE, plugin);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_SECONDARY, plugin);
                }
                if (chat.equalsIgnoreCase("t")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_SETDEST, plugin);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_UPDATE, plugin);
                }
                if (chat.equalsIgnoreCase("v")) {
                    showCommand(p, TARDISInfoMenu.TARDIS_VERSION, plugin);
                }
            }
            case TARDISTRAVEL -> {
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TARDISInfoMenu.TARDISTRAVEL_HOME, plugin);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TARDISInfoMenu.TARDISTRAVEL_PLAYER, plugin);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TARDISInfoMenu.TARDISTRAVEL_COORDS, plugin);
                }
                if (chat.equalsIgnoreCase("d")) {
                    showCommand(p, TARDISInfoMenu.TARDISTRAVEL_DEST, plugin);
                }
                if (chat.equalsIgnoreCase("b")) {
                    showCommand(p, TARDISInfoMenu.TARDISTRAVEL_BIOME, plugin);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TARDISInfoMenu.TARDISTRAVEL_AREA, plugin);
                }
            }
            case TARDISPREFS -> {
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_AUTO, plugin);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_EPS, plugin);
                }
                if (chat.equalsIgnoreCase("f")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_FLOOR, plugin);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_HADS, plugin);
                }
                if (chat.equalsIgnoreCase("i")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_ISOMORPHIC, plugin);
                }
                if (chat.equalsIgnoreCase("k")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_KEY, plugin);
                }
                if (chat.equalsIgnoreCase("m")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_MESSAGE, plugin);
                }
                if (chat.equalsIgnoreCase("q")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_QUOTES, plugin);
                }
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_SFX, plugin);
                }
                if (chat.equalsIgnoreCase("u")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_SUBMARINE, plugin);
                }
                if (chat.equalsIgnoreCase("w")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_WALL, plugin);
                }
            }
            case TARDISBIND -> {
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TARDISInfoMenu.TARDISBIND_SAVE, plugin);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TARDISInfoMenu.TARDISBIND_CMD, plugin);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TARDISInfoMenu.TARDISBIND_PLAYER, plugin);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TARDISInfoMenu.TARDISBIND_AREA, plugin);
                }
                if (chat.equalsIgnoreCase("b")) {
                    showCommand(p, TARDISInfoMenu.TARDISBIND_BIOME, plugin);
                }
                if (chat.equalsIgnoreCase("r")) {
                    showCommand(p, TARDISInfoMenu.TARDISBIND_REMOVE, plugin);
                }
            }
            case TARDISAREA -> {
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TARDISInfoMenu.TARDISAREA_START, plugin);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showCommand(p, TARDISInfoMenu.TARDISAREA_END, plugin);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TARDISInfoMenu.TARDISAREA_SHOW, plugin);
                }
                if (chat.equalsIgnoreCase("r")) {
                    showCommand(p, TARDISInfoMenu.TARDISAREA_REMOVE, plugin);
                }
            }
            case TARDISROOM -> {
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TARDISInfoMenu.TARDISROOM_ADD, plugin);
                }
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TARDISInfoMenu.TARDISROOM_SEED, plugin);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TARDISInfoMenu.TARDISROOM_COST, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    showCommand(p, TARDISInfoMenu.TARDISROOM_OFFSET, plugin);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showCommand(p, TARDISInfoMenu.TARDISROOM_ENABLED, plugin);
                }
            }
            case SKARO -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SKARO_INFO);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SKARO_MONSTERS);
                    exit(p, plugin);
                }
                if (chat.equalsIgnoreCase("t")) {
                    processKey(p, TARDISInfoMenu.SKARO_ITEMS, plugin);
                }
            }
            case SILURIA -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SILURIA_INFO);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SILURIA_MONSTERS);
                }
                exit(p, plugin);
            }
            case GALLIFREY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.GALLIFREY_INFO);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.GALLIFREY_MONSTERS);
                }
                exit(p, plugin);
            }
            // FOURTH level menus
            case SKARO_ITEMS -> {
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.RIFT_CIRCUIT, plugin);
                }
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TARDISInfoMenu.RIFT_MANIPULATOR, plugin);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.RUST_BUCKET, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.RUST_PLAGUE_SWORD, plugin);
                }
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.ACID_BUCKET, plugin);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TARDISInfoMenu.ACID_BATTERY, plugin);
                }
            }
            case SONIC_TYPES -> {
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SONIC_STANDARD, plugin);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.SONIC_REDSTONE, plugin);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TARDISInfoMenu.SONIC_DIAMOND, plugin);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TARDISInfoMenu.SONIC_EMERALD, plugin);
                }
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.SONIC_ADMIN, plugin);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TARDISInfoMenu.SONIC_BIO, plugin);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.SONIC_PAINTER, plugin);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.SONIC_BRUSH, plugin);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TARDISInfoMenu.SONIC_KNOCKBACK, plugin);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TARDISInfoMenu.SONIC_PICKUP_ARROWS, plugin);
                }
                if (chat.equalsIgnoreCase("g")) {
                    processKey(p, TARDISInfoMenu.SONIC_IGNITE, plugin);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.SONIC_CONVERSION, plugin);
                }
            }
            // FIFTH level menus - I've a feeling this is too deep!
            case SONIC_STANDARD -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_STANDARD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_SCREWDRIVER_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_REDSTONE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_REDSTONE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_DIAMOND -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_DIAMOND_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_EMERALD -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_EMERALD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_ADMIN -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_ADMIN_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_PAINTER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_PAINTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PAINTER_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_BRUSH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_BRUSH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BRUSH_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_IGNITE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_IGNITE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.IGNITE_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_KNOCKBACK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_KNOCKBACK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_PICKUP_ARROWS -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_PICKUP_ARROWS_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case SONIC_BIO -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_BIO_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case RIFT_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RIFT_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RIFT_CIRCUIT_RECIPE);
                }
                exit(p, plugin);
            }
            case RIFT_MANIPULATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RIFT_MANIPULATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RIFT_MANIPULATOR_RECIPE);
                }
                exit(p, plugin);
            }
            case RUST_BUCKET -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RUST_BUCKET_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RUST_BUCKET_RECIPE);
                }
                exit(p, plugin);
            }
            case RUST_PLAGUE_SWORD -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RUST_PLAGUE_SWORD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RUST_PLAGUE_SWORD_RECIPE);
                }
                exit(p, plugin);
            }
            case ACID_BUCKET -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ACID_BUCKET_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ACID_BUCKET_RECIPE);
                }
                exit(p, plugin);
            }
            case ACID_BATTERY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ACID_BATTERY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ACID_BATTERY_RECIPE);
                }
                exit(p, plugin);
            }
            default -> exit(p, plugin);
        }
    }

    /**
     * Displays the next menu level based on the parent menu item that was selected. Automatically pulls the key code
     * and highlights it.
     *
     * @param p    the player to show the menu to
     * @param item the parent menu item to get the children of
     */
    private static void processKey(Player p, TARDISInfoMenu item, TARDIS plugin) {
        plugin.getTrackerKeeper().getInfoMenu().put(p.getUniqueId(), item);
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        TARDISInfoMenu.getChildren(item.toString()).forEach((key, value) -> {
            String[] split = key.split(value, 2);
            if (split.length > 1) {
                String first = "> " + split[0];
                plugin.getMessenger().sendInfo(p, first, value, split[1]);
            }
        });
        plugin.getMessenger().sendInfo(p, "> ", "E", "xit");
    }

    /**
     * Displays the description and usage of a command. Values are pulled directly from the plugin.yml configuration
     * file.
     *
     * @param p    the player to show the command information to
     * @param item the command to display
     */
    private static void showCommand(Player p, TARDISInfoMenu item, TARDIS plugin) {
        String[] c = item.toString().toLowerCase(Locale.ROOT).split("_");
        String desc;
        String usage;
        if (c.length > 1) {
            desc = plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + "." + c[1] + ".description");
            usage = plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + "." + c[1] + ".usage", "/" + c[0] + " " + c[1]).replace("<command>", c[0]);
        } else {
            desc = plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + ".description");
            usage = plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + ".usage", "/" + c[0]).replace("<command>", c[0]);
        }
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        plugin.getMessenger().messageWithColour(p, "Description: " + desc, "#FFAA00");
        plugin.getMessenger().messageWithColour(p, "Usage: " + usage, "#FFAA00");
        exit(p, plugin);
    }

    /**
     * Exits the TARDIS Information System menu
     *
     * @param p the player to exit
     */
    public static void exit(Player p, TARDIS plugin) {
        plugin.getTrackerKeeper().getInfoMenu().remove(p.getUniqueId());
        plugin.getMessenger().messageWithColour(p, "---", "#FFAA00");
        plugin.getMessenger().send(p, TardisModule.TARDIS, "LOGGED_OUT_INFO");
    }

    /**
     * Listens for player typing a TARDIS Information System key code. The player must be found in the trackInfoMenu
     * HashMap, where their position in the TIS is stored. The key code is then processed.
     *
     * @param event a player typing in chat
     */
    @EventHandler(ignoreCancelled = true)
    public void onTISChat(AsyncChatEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (plugin.getTrackerKeeper().getInfoMenu().containsKey(uuid)) {
            event.setCancelled(true);
            String chat = ComponentUtils.stripColour(event.message());
            // always exit if 'e' is pressed
            if (chat.equalsIgnoreCase("E")) {
                exit(p, plugin);
                return;
            }
            if (chat.length() == 1 || chat.length() == 2) {
                processInput(p, uuid, chat, plugin);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "TIS_EXIT");
            }
        }
    }
}
