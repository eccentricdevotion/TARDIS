/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.info;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.chatgui.TardisChatGuiUpdater;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * The TARDIS information system is a searchable database which was discovered by the Fifth Doctor's companions Nyssa
 * and Tegan from a readout in the control room. The Fifth Doctor called it the TARDIS databank.
 *
 * @author bootthanoo, eccentric_nz
 */
public class TardisInformationSystemListener implements Listener, CommandExecutor {

    private final TardisPlugin plugin;

    public TardisInformationSystemListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return true;
        }
        UUID uuid = p.getUniqueId();
        if (plugin.getTrackerKeeper().getInfoMenu().containsKey(uuid)) {
            if (args[0].equalsIgnoreCase("E")) {
                exit(p);
                return true;
            }
            if (args.length == 1) {
                processInput(p, uuid, args[0]);
            } else {
                TardisMessage.send(p, "TIS_EXIT");
            }
        }
        return true;
    }

    /**
     * Listens for player typing a TARDIS Information System key code. The player must be found in the trackInfoMenu
     * HashMap, where their position in the TIS is stored. The key code is then processed.
     *
     * @param event a player typing in chat
     */
    @EventHandler(ignoreCancelled = true)
    public void onTISChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (plugin.getTrackerKeeper().getInfoMenu().containsKey(uuid)) {
            event.setCancelled(true);
            String chat = ChatColor.stripColor(event.getMessage());
            // always exit if 'e' is pressed
            if (chat.equalsIgnoreCase("E")) {
                exit(p);
                return;
            }
            if (chat.length() == 1) {
                processInput(p, uuid, chat);
            } else {
                TardisMessage.send(p, "TIS_EXIT");
            }
        }
    }

    private void processInput(Player p, UUID uuid, String chat) {
        switch (plugin.getTrackerKeeper().getInfoMenu().get(uuid)) {
            // TOP level menu
            case TIS:
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TardisInfoMenu.MANUAL);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TardisInfoMenu.ITEMS);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TardisInfoMenu.COMPONENTS);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TardisInfoMenu.SONIC_COMPONENTS);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TardisInfoMenu.DISKS);
                }
                if (chat.equalsIgnoreCase("O")) {
                    processKey(p, TardisInfoMenu.COMMANDS);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TardisInfoMenu.ROOMS);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TardisInfoMenu.TYPES);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TardisInfoMenu.FOOD_ACCESSORIES);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TardisInfoMenu.PLANETS);
                }
                break;
            // SECOND level menu
            case CONSOLE_BLOCKS:
                if (chat.equalsIgnoreCase("v")) {
                    showInfo(p, TardisInfoMenu.ADVANCED);
                }
                if (chat.equalsIgnoreCase("S")) {
                    showInfo(p, TardisInfoMenu.STORAGE);
                }
                if (chat.equalsIgnoreCase("A")) {
                    showInfo(p, TardisInfoMenu.CONSOLE_ARS);
                }
                if (chat.equalsIgnoreCase("r")) {
                    showInfo(p, TardisInfoMenu.ARTRON);
                }
                if (chat.equalsIgnoreCase("B")) {
                    showInfo(p, TardisInfoMenu.BACKDOOR);
                }
                if (chat.equalsIgnoreCase("u")) {
                    showInfo(p, TardisInfoMenu.BUTTON);
                }
                if (chat.equalsIgnoreCase("C")) {
                    showInfo(p, TardisInfoMenu.CHAMELEON);
                }
                if (chat.equalsIgnoreCase("o")) {
                    showInfo(p, TardisInfoMenu.CONDENSER);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showInfo(p, TardisInfoMenu.CREEPER);
                }
                if (chat.equalsIgnoreCase("D")) {
                    showInfo(p, TardisInfoMenu.DOOR);
                }
                if (chat.equalsIgnoreCase("P")) {
                    showInfo(p, TardisInfoMenu.EPS);
                }
                if (chat.equalsIgnoreCase("m")) {
                    showInfo(p, TardisInfoMenu.CONSOLE_FARM);
                }
                if (chat.equalsIgnoreCase("k")) {
                    showInfo(p, TardisInfoMenu.HANDBRAKE);
                }
                break;
            case CONSOLE_BLOCKS_2:
                if (chat.equalsIgnoreCase("o")) {
                    showInfo(p, TardisInfoMenu.TOGGLE);
                }
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.INFO);
                }
                if (chat.equalsIgnoreCase("K")) {
                    showInfo(p, TardisInfoMenu.KEYBOARD);
                }
                if (chat.equalsIgnoreCase("L")) {
                    showInfo(p, TardisInfoMenu.LIGHT);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showInfo(p, TardisInfoMenu.CONSOLE_RAIL);
                }
                if (chat.equalsIgnoreCase("S")) {
                    showInfo(p, TardisInfoMenu.SAVE_SIGN);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showInfo(p, TardisInfoMenu.SCANNER);
                }
                if (chat.equalsIgnoreCase("b")) {
                    showInfo(p, TardisInfoMenu.CONSOLE_STABLE);
                }
                if (chat.equalsIgnoreCase("Ll")) {
                    showInfo(p, TardisInfoMenu.CONSOLE_STALL);
                }
                if (chat.equalsIgnoreCase("T")) {
                    showInfo(p, TardisInfoMenu.TERMINAL);
                }
                if (chat.equalsIgnoreCase("m")) {
                    showInfo(p, TardisInfoMenu.TEMPORAL);
                }
                if (chat.equalsIgnoreCase("W")) {
                    showInfo(p, TardisInfoMenu.WORLD_REPEATER);
                }
                if (chat.equalsIgnoreCase("X")) {
                    showInfo(p, TardisInfoMenu.X_REPEATER);
                }
                if (chat.equalsIgnoreCase("Y")) {
                    showInfo(p, TardisInfoMenu.Y_REPEATER);
                }
                if (chat.equalsIgnoreCase("Z")) {
                    showInfo(p, TardisInfoMenu.Z_REPEATER);
                }
                break;
            case ITEMS:
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TardisInfoMenu.CELL);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TardisInfoMenu.READER);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TardisInfoMenu.FILTER);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TardisInfoMenu.KEY);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TardisInfoMenu.R_KEY);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TardisInfoMenu.SONIC);
                }
                if (chat.equalsIgnoreCase("L")) {
                    processKey(p, TardisInfoMenu.LOCATOR);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TardisInfoMenu.REMOTE);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TardisInfoMenu.GENERATOR);
                }
                break;
            case DISKS:
                if (chat.equalsIgnoreCase("A")) {
                    showInfo(p, TardisInfoMenu.AREA_DISK);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TardisInfoMenu.BLANK);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TardisInfoMenu.CONTROL_DISK);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TardisInfoMenu.BIOME_DISK);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TardisInfoMenu.PLAYER_DISK);
                }
                if (chat.equalsIgnoreCase("r")) {
                    processKey(p, TardisInfoMenu.PRESET_DISK);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TardisInfoMenu.SAVE_DISK);
                }
                break;
            case COMPONENTS:
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TardisInfoMenu.C_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("h")) {
                    processKey(p, TardisInfoMenu.ARS_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TardisInfoMenu.I_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("v")) {
                    processKey(p, TardisInfoMenu.INVISIBLE_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("L")) {
                    processKey(p, TardisInfoMenu.L_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TardisInfoMenu.M_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TardisInfoMenu.P_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TardisInfoMenu.R_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TardisInfoMenu.SCANNER_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TardisInfoMenu.RANDOMISER_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TardisInfoMenu.S_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TardisInfoMenu.T_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("y")) {
                    processKey(p, TardisInfoMenu.MEMORY_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("f")) {
                    processKey(p, TardisInfoMenu.RIFT_CIRCUIT);
                }
                break;
            case SONIC_COMPONENTS:
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TardisInfoMenu.A_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TardisInfoMenu.BIO_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TardisInfoMenu.D_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TardisInfoMenu.IGNITE_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TardisInfoMenu.KNOCKBACK_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TardisInfoMenu.E_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("O")) {
                    processKey(p, TardisInfoMenu.OSCILLATOR_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TardisInfoMenu.PAINTER_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TardisInfoMenu.ARROW_CIRCUIT);
                }
                break;
            case MANUAL:
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TardisInfoMenu.TIME_TRAVEL);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TardisInfoMenu.CONSOLE_BLOCKS);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TardisInfoMenu.CONSOLE_BLOCKS_2);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TardisInfoMenu.TARDIS_CONTROLS);
                }
                break;
            case TARDIS_CONTROLS:
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TardisInfoMenu.ARTRON);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TardisInfoMenu.TIME_TRAVEL);
                }
                if (chat.equalsIgnoreCase("M")) {
                    showInfo(p, TardisInfoMenu.MALFUNCTIONS);
                }
                if (chat.equalsIgnoreCase("l")) {
                    showInfo(p, TardisInfoMenu.ALT_CONTROLS);
                }
                break;
            case TIME_TRAVEL:
                break;
            case DOOR:
                showInfo(p, TardisInfoMenu.DOOR);
                break;
            case COMMANDS:
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TardisInfoMenu.TARDIS);
                }
                if (chat.equalsIgnoreCase("A")) {
                    showCommand(p, TardisInfoMenu.TARDISADMIN);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TardisInfoMenu.TARDISAREA);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TardisInfoMenu.TARDISBIND);
                }
                if (chat.equalsIgnoreCase("k")) {
                    showCommand(p, TardisInfoMenu.TARDISBOOK);
                }
                if (chat.equalsIgnoreCase("G")) {
                    showCommand(p, TardisInfoMenu.TARDISGRAVITY);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TardisInfoMenu.TARDISPREFS);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showCommand(p, TardisInfoMenu.TARDISRECIPE);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TardisInfoMenu.TARDISROOM);
                }
                if (chat.equalsIgnoreCase("x")) {
                    processKey(p, TardisInfoMenu.TARDISTEXTURE);
                }
                if (chat.equalsIgnoreCase("v")) {
                    processKey(p, TardisInfoMenu.TARDISTRAVEL);
                }
                break;
            case ROOMS:
                if (chat.equalsIgnoreCase("A")) {
                    showRoomInfo(p, TardisInfoMenu.ANTIGRAVITY);
                }
                if (chat.equalsIgnoreCase("q")) {
                    showRoomInfo(p, TardisInfoMenu.AQUARIUM);
                }
                if (chat.equalsIgnoreCase("u")) {
                    showRoomInfo(p, TardisInfoMenu.ARBORETUM);
                }
                if (chat.equalsIgnoreCase("B")) {
                    showRoomInfo(p, TardisInfoMenu.BAKER);
                }
                if (chat.equalsIgnoreCase("d")) {
                    showRoomInfo(p, TardisInfoMenu.BEDROOM);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showRoomInfo(p, TardisInfoMenu.BIRDCAGE);
                }
                if (chat.equalsIgnoreCase("y")) {
                    showRoomInfo(p, TardisInfoMenu.EMPTY);
                }
                if (chat.equalsIgnoreCase("F")) {
                    showRoomInfo(p, TardisInfoMenu.FARM);
                }
                if (chat.equalsIgnoreCase("G")) {
                    showRoomInfo(p, TardisInfoMenu.GRAVITY);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showRoomInfo(p, TardisInfoMenu.GREENHOUSE);
                }
                if (chat.equalsIgnoreCase("H")) {
                    showRoomInfo(p, TardisInfoMenu.HARMONY);
                }
                if (chat.equalsIgnoreCase("K")) {
                    showRoomInfo(p, TardisInfoMenu.KITCHEN);
                }
                if (chat.equalsIgnoreCase("L")) {
                    showRoomInfo(p, TardisInfoMenu.LIBRARY);
                }
                if (chat.equalsIgnoreCase("M")) {
                    showRoomInfo(p, TardisInfoMenu.MUSHROOM);
                }
                if (chat.equalsIgnoreCase("P")) {
                    showRoomInfo(p, TardisInfoMenu.PASSAGE);
                }
                if (chat.equalsIgnoreCase("o")) {
                    showRoomInfo(p, TardisInfoMenu.POOL);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRoomInfo(p, TardisInfoMenu.RAIL);
                }
                if (chat.equalsIgnoreCase("x")) {
                    showRoomInfo(p, TardisInfoMenu.RENDERER);
                }
                if (chat.equalsIgnoreCase("Sh")) {
                    showRoomInfo(p, TardisInfoMenu.SHELL);
                }
                if (chat.equalsIgnoreCase("S")) {
                    showRoomInfo(p, TardisInfoMenu.STABLE);
                }
                if (chat.equalsIgnoreCase("Ll")) {
                    showRoomInfo(p, TardisInfoMenu.STALL);
                }
                if (chat.equalsIgnoreCase("T")) {
                    showRoomInfo(p, TardisInfoMenu.TRENZALORE);
                }
                if (chat.equalsIgnoreCase("V")) {
                    showRoomInfo(p, TardisInfoMenu.VAULT);
                }
                if (chat.equalsIgnoreCase("W")) {
                    showRoomInfo(p, TardisInfoMenu.WOOD);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showRoomInfo(p, TardisInfoMenu.WORKSHOP);
                }
                break;
            case TYPES:
                if (chat.equalsIgnoreCase("B")) {
                    showInfo(p, TardisInfoMenu.BUDGET);
                }
                if (chat.equalsIgnoreCase("i")) {
                    showInfo(p, TardisInfoMenu.BIGGER);
                }
                if (chat.equalsIgnoreCase("D")) {
                    showInfo(p, TardisInfoMenu.DELUXE);
                }
                if (chat.equalsIgnoreCase("l")) {
                    showInfo(p, TardisInfoMenu.ELEVENTH);
                }
                if (chat.equalsIgnoreCase("f")) {
                    showInfo(p, TardisInfoMenu.TWELFTH);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showInfo(p, TardisInfoMenu.THIRTEENTH);
                }
                if (chat.equalsIgnoreCase("y")) {
                    showInfo(p, TardisInfoMenu.FACTORY);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showInfo(p, TardisInfoMenu.REDSTONE);
                }
                if (chat.equalsIgnoreCase("S")) {
                    showInfo(p, TardisInfoMenu.STEAMPUNK);
                }
                if (chat.equalsIgnoreCase("P")) {
                    showInfo(p, TardisInfoMenu.PLANK);
                }
                if (chat.equalsIgnoreCase("T")) {
                    showInfo(p, TardisInfoMenu.TOM);
                }
                if (chat.equalsIgnoreCase("A")) {
                    showInfo(p, TardisInfoMenu.ARS);
                }
                if (chat.equalsIgnoreCase("W")) {
                    showInfo(p, TardisInfoMenu.WAR);
                }
                if (chat.equalsIgnoreCase("y")) {
                    showInfo(p, TardisInfoMenu.PYRAMID);
                }
                if (chat.equalsIgnoreCase("M")) {
                    showInfo(p, TardisInfoMenu.MASTER);
                }
                if (chat.equalsIgnoreCase("C")) {
                    showInfo(p, TardisInfoMenu.CUSTOM);
                }
                if (chat.equalsIgnoreCase("d")) {
                    showInfo(p, TardisInfoMenu.ENDER);
                }
                if (chat.equalsIgnoreCase("o")) {
                    showInfo(p, TardisInfoMenu.CORAL);
                }
                if (chat.equalsIgnoreCase("1")) {
                    showInfo(p, TardisInfoMenu.COPPER_11TH);
                }
                if (chat.equalsIgnoreCase("=")) {
                    showInfo(p, TardisInfoMenu.DELTA);
                }
                if (chat.equalsIgnoreCase("v")) {
                    showInfo(p, TardisInfoMenu.CAVE);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showInfo(p, TardisInfoMenu.WEATHERED);
                }
                break;
            case FOOD_ACCESSORIES:
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TardisInfoMenu.BOW_TIE);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TardisInfoMenu.CUSTARD);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TardisInfoMenu.JAMMY_DODGER);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TardisInfoMenu.FISH_FINGER);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TardisInfoMenu.GLASSES);
                }
                if (chat.equalsIgnoreCase("J")) {
                    processKey(p, TardisInfoMenu.JELLY_BABY);
                }
                if (chat.equalsIgnoreCase("W")) {
                    processKey(p, TardisInfoMenu.WATCH);
                }
                break;
            case PLANETS:
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TardisInfoMenu.SKARO);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TardisInfoMenu.SILURIA);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TardisInfoMenu.GALLIFREY);
                }
                break;
            // THIRD level menus
            case KEY:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.KEY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.KEY_RECIPE);
                }
                break;
            case SONIC:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SONIC_INFO);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TardisInfoMenu.SONIC_TYPES);
                }
                break;
            case LOCATOR:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.LOCATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.LOCATOR_RECIPE);
                }
                break;
            case REMOTE:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.REMOTE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.REMOTE_RECIPE);
                }
                break;
            case READER:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.READER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.READER_RECIPE);
                }
                break;
            case R_KEY:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.R_KEY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.R_KEY_RECIPE);
                }
                break;
            case GENERATOR:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.GENERATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.GENERATOR_RECIPE);
                }
                break;
            case GLASSES:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.GLASSES_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.GLASSES_RECIPE);
                }
                break;
            case BOW_TIE:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.BOW_TIE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.BOW_TIE_RECIPE);
                }
                break;
            case CUSTARD:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.CUSTARD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.CUSTARD_RECIPE);
                }
                break;
            case FISH_FINGER:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.FISH_FINGER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.FISH_FINGER_RECIPE);
                }
                break;
            case JELLY_BABY:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.JELLY_BABY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.JELLY_BABY_RECIPE);
                }
                break;
            case JAMMY_DODGER:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.JAMMY_DODGER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.JAMMY_DODGER_RECIPE);
                }
                break;
            case WATCH:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.WATCH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.WATCH_RECIPE);
                }
                break;
            case BIOME_DISK:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.BIOME_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.BIOME_DISK_RECIPE);
                }
                break;
            case CONTROL_DISK:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.CONTROL_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.CONTROL_DISK_RECIPE);
                }
                break;
            case BLANK:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.BLANK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.BLANK_RECIPE);
                }
                break;
            case PLAYER_DISK:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.PLAYER_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.PLAYER_DISK_RECIPE);
                }
                break;
            case PRESET_DISK:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.PRESET_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.PRESET_DISK_RECIPE);
                }
                break;
            case SAVE_DISK:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SAVE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.SAVE_DISK_RECIPE);
                }
                break;
            case A_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.A_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.A_CIRCUIT_RECIPE);
                }
                break;
            case BIO_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.BIO_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.BIO_CIRCUIT_RECIPE);
                }
                break;
            case C_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.C_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.C_CIRCUIT_RECIPE);
                }
                break;
            case D_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.D_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.D_CIRCUIT_RECIPE);
                }
                break;
            case E_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.E_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.E_CIRCUIT_RECIPE);
                }
                break;
            case CELL:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.CELL_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.CELL_RECIPE);
                }
                break;
            case FILTER:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.FILTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.FILTER_RECIPE);
                }
                break;
            case ARS_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.ARS_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.ARS_CIRCUIT_RECIPE);
                }
                break;
            case I_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.I_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.I_CIRCUIT_RECIPE);
                }
                break;
            case IGNITE_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.IGNITE_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.IGNITE_CIRCUIT_RECIPE);
                }
                break;
            case KNOCKBACK_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.KNOCKBACK_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.KNOCKBACK_CIRCUIT_RECIPE);
                }
                break;
            case INVISIBLE_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.INVISIBLE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.INVISIBLE_RECIPE);
                }
                break;
            case L_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.L_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.L_CIRCUIT_RECIPE);
                }
                break;
            case M_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.M_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.M_CIRCUIT_RECIPE);
                }
                break;
            case OSCILLATOR_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.OSCILLATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.OSCILLATOR_RECIPE);
                }
                break;
            case P_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.P_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.P_CIRCUIT_RECIPE);
                }
                break;
            case PAINTER_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.PAINTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.PAINTER_RECIPE);
                }
                break;
            case R_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.R_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.R_CIRCUIT_RECIPE);
                }
                break;
            case RANDOMISER_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.RANDOMISER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.RANDOMISER_CIRCUIT_RECIPE);
                }
                break;
            case S_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.S_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.S_CIRCUIT_RECIPE);
                }
                break;
            case T_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.T_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.T_CIRCUIT_RECIPE);
                }
                break;
            case MEMORY_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.MEMORY_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.MEMORY_CIRCUIT_RECIPE);
                }
                break;
            case SCANNER_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SCANNER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.SCANNER_CIRCUIT_RECIPE);
                }
                break;
            case ARROW_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.ARROW_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.ARROW_CIRCUIT_RECIPE);
                }
                break;
            case TARDIS:
                if (chat.equalsIgnoreCase("ab")) {
                    showCommand(p, TardisInfoMenu.TARDIS_ABORT);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TardisInfoMenu.TARDIS_ADD);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TardisInfoMenu.TARDIS_CHAMELEON);
                }
                if (chat.equalsIgnoreCase("com")) {
                    showCommand(p, TardisInfoMenu.TARDIS_COMEHERE);
                }
                if (chat.equalsIgnoreCase("d")) {
                    showCommand(p, TardisInfoMenu.TARDIS_DIRECTION);
                }
                if (chat.equalsIgnoreCase("x")) {
                    showCommand(p, TardisInfoMenu.TARDIS_EXTERMINATE);
                }
                if (chat.equalsIgnoreCase("f")) {
                    showCommand(p, TardisInfoMenu.TARDIS_FIND);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TardisInfoMenu.TARDIS_HIDE);
                }
                if (chat.equalsIgnoreCase("m")) {
                    showCommand(p, TardisInfoMenu.TARDIS_HOME);
                }
                if (chat.equalsIgnoreCase("i")) {
                    showCommand(p, TardisInfoMenu.TARDIS_INSIDE);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TardisInfoMenu.TARDIS_JETTISON);
                }
                if (chat.equalsIgnoreCase("la")) {
                    showCommand(p, TardisInfoMenu.TARDIS_LAMPS);
                }
                if (chat.equalsIgnoreCase("l")) {
                    showCommand(p, TardisInfoMenu.TARDIS_LIST);
                }
                if (chat.equalsIgnoreCase("k")) {
                    showCommand(p, TardisInfoMenu.TARDIS_NAMEKEY);
                }
                if (chat.equalsIgnoreCase("o")) {
                    showCommand(p, TardisInfoMenu.TARDIS_OCCUPY);
                }
                if (chat.equalsIgnoreCase("b")) {
                    showCommand(p, TardisInfoMenu.TARDIS_REBUILD);
                }
                if (chat.equalsIgnoreCase("r")) {
                    showCommand(p, TardisInfoMenu.TARDIS_REMOVE);
                }
                if (chat.equalsIgnoreCase("rem")) {
                    showCommand(p, TardisInfoMenu.TARDIS_REMOVESAVE);
                }
                if (chat.equalsIgnoreCase("u")) {
                    showCommand(p, TardisInfoMenu.TARDIS_RESCUE);
                }
                if (chat.equalsIgnoreCase("roo")) {
                    showCommand(p, TardisInfoMenu.TARDIS_ROOM);
                }
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TardisInfoMenu.TARDIS_SAVE);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showCommand(p, TardisInfoMenu.TARDIS_SECONDARY);
                }
                if (chat.equalsIgnoreCase("t")) {
                    showCommand(p, TardisInfoMenu.TARDIS_SETDEST);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TardisInfoMenu.TARDIS_UPDATE);
                }
                if (chat.equalsIgnoreCase("v")) {
                    showCommand(p, TardisInfoMenu.TARDIS_VERSION);
                }
                break;
            case TARDISTRAVEL:
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TardisInfoMenu.TARDISTRAVEL_HOME);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TardisInfoMenu.TARDISTRAVEL_PLAYER);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TardisInfoMenu.TARDISTRAVEL_COORDS);
                }
                if (chat.equalsIgnoreCase("d")) {
                    showCommand(p, TardisInfoMenu.TARDISTRAVEL_DEST);
                }
                if (chat.equalsIgnoreCase("b")) {
                    showCommand(p, TardisInfoMenu.TARDISTRAVEL_BIOME);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TardisInfoMenu.TARDISTRAVEL_AREA);
                }
                break;
            case TARDISPREFS:
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_AUTO);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_EPS);
                }
                if (chat.equalsIgnoreCase("f")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_FLOOR);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_HADS);
                }
                if (chat.equalsIgnoreCase("i")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_ISOMORPHIC);
                }
                if (chat.equalsIgnoreCase("k")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_KEY);
                }
                if (chat.equalsIgnoreCase("m")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_MESSAGE);
                }
                if (chat.equalsIgnoreCase("q")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_QUOTES);
                }
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_SFX);
                }
                if (chat.equalsIgnoreCase("u")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_SUBMARINE);
                }
                if (chat.equalsIgnoreCase("w")) {
                    showCommand(p, TardisInfoMenu.TARDISPREFS_WALL);
                }
                break;
            case TARDISBIND:
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TardisInfoMenu.TARDISBIND_SAVE);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TardisInfoMenu.TARDISBIND_CMD);
                }
                if (chat.equalsIgnoreCase("p")) {
                    showCommand(p, TardisInfoMenu.TARDISBIND_PLAYER);
                }
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TardisInfoMenu.TARDISBIND_AREA);
                }
                if (chat.equalsIgnoreCase("b")) {
                    showCommand(p, TardisInfoMenu.TARDISBIND_BIOME);
                }
                if (chat.equalsIgnoreCase("r")) {
                    showCommand(p, TardisInfoMenu.TARDISBIND_REMOVE);
                }
                break;
            case TARDISTEXTURE:
                if (chat.equalsIgnoreCase("o")) {
                    showCommand(p, TardisInfoMenu.TARDISTEXTURE_ON);
                }
                if (chat.equalsIgnoreCase("f")) {
                    showCommand(p, TardisInfoMenu.TARDISTEXTURE_OFF);
                }
                if (chat.equalsIgnoreCase("i")) {
                    showCommand(p, TardisInfoMenu.TARDISTEXTURE_IN);
                }
                if (chat.equalsIgnoreCase("u")) {
                    showCommand(p, TardisInfoMenu.TARDISTEXTURE_OUT);
                }
                break;
            case TARDISAREA:
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TardisInfoMenu.TARDISAREA_START);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showCommand(p, TardisInfoMenu.TARDISAREA_END);
                }
                if (chat.equalsIgnoreCase("h")) {
                    showCommand(p, TardisInfoMenu.TARDISAREA_SHOW);
                }
                if (chat.equalsIgnoreCase("r")) {
                    showCommand(p, TardisInfoMenu.TARDISAREA_REMOVE);
                }
                break;
            case TARDISROOM:
                if (chat.equalsIgnoreCase("a")) {
                    showCommand(p, TardisInfoMenu.TARDISROOM_ADD);
                }
                if (chat.equalsIgnoreCase("s")) {
                    showCommand(p, TardisInfoMenu.TARDISROOM_SEED);
                }
                if (chat.equalsIgnoreCase("c")) {
                    showCommand(p, TardisInfoMenu.TARDISROOM_COST);
                }
                if (chat.equalsIgnoreCase("o")) {
                    showCommand(p, TardisInfoMenu.TARDISROOM_OFFSET);
                }
                if (chat.equalsIgnoreCase("n")) {
                    showCommand(p, TardisInfoMenu.TARDISROOM_ENABLED);
                }
                break;
            case SKARO:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SKARO_INFO);
                }
                if (chat.equalsIgnoreCase("M")) {
                    showInfo(p, TardisInfoMenu.SKARO_MONSTERS);
                }
                if (chat.equalsIgnoreCase("t")) {
                    processKey(p, TardisInfoMenu.SKARO_ITEMS);
                }
                break;
            case SILURIA:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SILURIA_INFO);
                }
                if (chat.equalsIgnoreCase("M")) {
                    showInfo(p, TardisInfoMenu.SILURIA_MONSTERS);
                }
                break;
            case GALLIFREY:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.GALLIFREY_INFO);
                }
                if (chat.equalsIgnoreCase("M")) {
                    showInfo(p, TardisInfoMenu.GALLIFREY_MONSTERS);
                }
                break;
            // FOURTH level menus
            case SKARO_ITEMS:
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TardisInfoMenu.RIFT_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TardisInfoMenu.RIFT_MANIPULATOR);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TardisInfoMenu.RUST_BUCKET);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TardisInfoMenu.RUST_PLAGUE_SWORD);
                }
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TardisInfoMenu.ACID_BUCKET);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TardisInfoMenu.ACID_BATTERY);
                }
                break;
            case SONIC_TYPES:
                if (chat.equalsIgnoreCase("Q")) {
                    processKey(p, TardisInfoMenu.SONIC_Q);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TardisInfoMenu.SONIC_R);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TardisInfoMenu.SONIC_D);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TardisInfoMenu.SONIC_E);
                }
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TardisInfoMenu.SONIC_A);
                }
                break;
            // FIFTH level menus - I've a feeling this is too deep!
            case SONIC_Q:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SONIC_Q_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.SONIC_RECIPE);
                }
                break;
            case SONIC_R:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SONIC_R_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.R_CIRCUIT_RECIPE);
                }
                break;
            case SONIC_D:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SONIC_D_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.D_CIRCUIT_RECIPE);
                }
                break;
            case SONIC_E:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SONIC_E_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.E_CIRCUIT_RECIPE);
                }
                break;
            case SONIC_A:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.SONIC_A_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.A_CIRCUIT_RECIPE);
                }
                break;
            case RIFT_CIRCUIT:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.RIFT_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.RIFT_CIRCUIT_RECIPE);
                }
                break;
            case RIFT_MANIPULATOR:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.RIFT_MANIPULATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.RIFT_MANIPULATOR_RECIPE);
                }
                break;
            case RUST_BUCKET:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.RUST_BUCKET_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showInfo(p, TardisInfoMenu.RUST_BUCKET_RECIPE);
                }
                break;
            case RUST_PLAGUE_SWORD:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.RUST_PLAGUE_SWORD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.RUST_PLAGUE_SWORD_RECIPE);
                }
                break;
            case ACID_BUCKET:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.ACID_BUCKET_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showInfo(p, TardisInfoMenu.ACID_BUCKET_RECIPE);
                }
                break;
            case ACID_BATTERY:
                if (chat.equalsIgnoreCase("I")) {
                    showInfo(p, TardisInfoMenu.ACID_BATTERY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    showRecipe(p, TardisInfoMenu.ACID_BATTERY_RECIPE);
                }
                break;
            default:
                exit(p);
                break;
        }
    }

    /**
     * Displays the next menu level based on the parent menu item that was selected. Automatically pulls the key code
     * and highlights it.
     *
     * @param p    the player to show the menu to
     * @param item the parent menu item to get the children of
     */
    private void processKey(Player p, TardisInfoMenu item) {
        plugin.getTrackerKeeper().getInfoMenu().put(p.getUniqueId(), item);
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        TardisInfoMenu.getChildren(item.toString()).forEach((key, value) -> {
            String[] split = key.split(value, 2);
            String first = "> " + split[0];
            TardisChatGuiUpdater.sendTextComponent(first, value, split[1], p);
        });
        TardisChatGuiUpdater.sendTextComponent("> ", "E", "xit", p);
    }

    /**
     * Displays information about a TARDIS room. Descriptions are stored in the TARDISDescription enum. Other values are
     * pulled directly from the rooms.yml configuration file.
     *
     * @param p    the player to show the room information to
     * @param item the room to display
     */
    private void showRoomInfo(Player p, TardisInfoMenu item) {
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        p.sendMessage(ChatColor.GOLD + "" + TardisDescription.valueOf(item.toString()).getDesc());
        String r = item.toString();
        p.sendMessage(ChatColor.GOLD + "Seed Block: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"));
        p.sendMessage(ChatColor.GOLD + "Offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"));
        p.sendMessage(ChatColor.GOLD + "Cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"));
        p.sendMessage(ChatColor.GOLD + "Enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"));
        exit(p);
    }

    /**
     * Displays information about an item or TARDIS type. Descriptions are stored in the TARDISDescription enum.
     *
     * @param p    the player to show the information to
     * @param item the item or TARDIS type to display
     */
    private void showInfo(Player p, TardisInfoMenu item) {
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        p.sendMessage(ChatColor.GOLD + "" + TardisDescription.valueOf(item.toString()).getDesc());
        exit(p);
    }

    /**
     * Displays the workbench recipe for an item or component.
     *
     * @param p    the player to show the recipe to
     * @param item the recipe to display
     */
    private void showRecipe(Player p, TardisInfoMenu item) {
        // do stuff
        String[] r = item.toString().split("_");
        String recipe = (r.length == 3) ? r[0] + "-" + r[1] : r[0];
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> p.performCommand("tardisrecipe " + recipe));
        exit(p);
    }

    /**
     * Displays the description and usage of a command. Values are pulled directly from the plugin.yml configuration
     * file.
     *
     * @param p    the player to show the command information to
     * @param item the command to display
     */
    private void showCommand(Player p, TardisInfoMenu item) {
        String[] c = item.toString().toLowerCase(Locale.ENGLISH).split("_");
        String desc;
        String usage;
        if (c.length > 1) {
            desc = plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + "." + c[1] + ".description");
            usage = Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + "." + c[1] + ".usage")).replace("<command>", c[0]);
        } else {
            desc = plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + ".description");
            usage = Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getString("commands." + c[0] + ".usage")).replace("<command>", c[0]);
        }
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        p.sendMessage(ChatColor.GOLD + "Description: " + desc);
        p.sendMessage(ChatColor.GOLD + "Usage: " + usage);
        exit(p);
    }

    /**
     * Exits the TARDIS Information System menu
     *
     * @param p the player to exit
     */
    private void exit(Player p) {
        plugin.getTrackerKeeper().getInfoMenu().remove(p.getUniqueId());
        p.sendMessage(ChatColor.GOLD + "---");
        TardisMessage.send(p, "LOGGED_OUT_INFO");
    }
}
