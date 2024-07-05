/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.UUID;

/**
 * The TARDIS information system is a searchable database which was discovered by the Fifth Doctor's companions Nyssa
 * and Tegan from a readout in the control room. The Fifth Doctor called it the TARDIS data bank.
 *
 * @author bootthanoo, eccentric_nz
 */
public class TARDISInformationSystemListener implements Listener, CommandExecutor {

    private final TARDIS plugin;

    public TARDISInformationSystemListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
                plugin.getMessenger().send(p, TardisModule.TARDIS, "TIS_EXIT");
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            // open TIS GUI
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack[] cats = new TARDISIndexFileInventory(plugin).getMenu();
                Inventory gui = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "TARDIS Index File");
                gui.setContents(cats);
                p.openInventory(gui);
            }, 2L);
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
            if (chat.length() == 1 || chat.length() == 2) {
                processInput(p, uuid, chat);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "TIS_EXIT");
            }
        }
    }

    private void processInput(Player p, UUID uuid, String chat) {
        switch (plugin.getTrackerKeeper().getInfoMenu().get(uuid)) {
            // TOP level menu
            case TIS -> {
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
                if (chat.equalsIgnoreCase("2")) {
                    processKey(p, TARDISInfoMenu.ROOMS_2);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TYPES);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.FOOD_ACCESSORIES);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PLANETS);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.MONSTERS);
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
                exit(p);
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
                exit(p);
            }
            case CONSOLE_BLOCKS_3 -> {
                if (chat.equalsIgnoreCase("x")) {
                    processKey(p, TARDISInfoMenu.EXTERIOR_LAMP_LEVEL_SWITCH);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TARDISInfoMenu.INTERIOR_LIGHT_LEVEL_SWITCH);
                }
                if (chat.equalsIgnoreCase("C")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CHARGING_SENSOR);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("l")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FLIGHT_SENSOR);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("H")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HANDBRAKE_SENSOR);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MALFUNCTION_SENSOR);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("P")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.POWER_SENSOR);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.TARDIS_MONITOR);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.MONITOR_FRAME);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RELATIVITY_DIFFERENTIATOR);
                    exit(p);
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
                exit(p);
            }
            case ITEMS -> {
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.ARTRON_STORAGE_CELL);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.ARTRON_CAPACITOR);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.ARTRON_CAPACITOR_STORAGE);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BIOME_READER);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.PERCEPTION_FILTER);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TARDISInfoMenu.KEY);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TARDISInfoMenu.REMOTE_KEY);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SONIC_SCREWDRIVER);
                }
                if (chat.equalsIgnoreCase("L")) {
                    processKey(p, TARDISInfoMenu.LOCATOR);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.STATTENHEIM_REMOTE);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.ARTRON_FURNACE);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TARDISInfoMenu.SONIC_GENERATOR);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.SONIC_BLASTER);
                }
                if (chat.equalsIgnoreCase("V")) {
                    processKey(p, TARDISInfoMenu.VORTEX_MANIPULATOR);
                }
                if (chat.equalsIgnoreCase("H")) {
                    processKey(p, TARDISInfoMenu.HANDLES);
                }
            }
            case DISKS -> {
                if (chat.equalsIgnoreCase("A")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.AREA_DISK);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BLANK_STORAGE_DISK);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.AUTHORISED_CONTROL_DISK);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TARDISInfoMenu.BIOME_STORAGE_DISK);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PLAYER_STORAGE_DISK);
                }
                if (chat.equalsIgnoreCase("r")) {
                    processKey(p, TARDISInfoMenu.PRESET_STORAGE_DISK);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SAVE_STORAGE_DISK);
                }
            }
            case COMPONENTS -> {
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.CHAMELEON_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("h")) {
                    processKey(p, TARDISInfoMenu.ARS_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TARDISInfoMenu.INPUT_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("v")) {
                    processKey(p, TARDISInfoMenu.INVISIBILITY_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("L")) {
                    processKey(p, TARDISInfoMenu.LOCATOR_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TARDISInfoMenu.MATERIALISATION_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PERCEPTION_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.SCANNER_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.RANDOMISER_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.STATTENHEIM_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TEMPORAL_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("y")) {
                    processKey(p, TARDISInfoMenu.MEMORY_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("f")) {
                    processKey(p, TARDISInfoMenu.RIFT_CIRCUIT);
                }
            }
            case SONIC_COMPONENTS -> {
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("I")) {
                    processKey(p, TARDISInfoMenu.IGNITE_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("O")) {
                    processKey(p, TARDISInfoMenu.SONIC_OSCILLATOR);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.PAINTER_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.BRUSH_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("n")) {
                    processKey(p, TARDISInfoMenu.CONVERSION_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT);
                }
            }
            case MANUAL -> {
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TIME_TRAVEL);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS_2);
                }
                if (chat.equalsIgnoreCase("k")) {
                    processKey(p, TARDISInfoMenu.CONSOLE_BLOCKS_3);
                }
                if (chat.equalsIgnoreCase("U")) {
                    processKey(p, TARDISInfoMenu.UPDATEABLE_BLOCKS);
                }
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.TARDIS_CONTROLS);
                }
            }
            case TARDIS_CONTROLS -> {
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.ARTRON);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.TIME_TRAVEL);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MALFUNCTIONS);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("l")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ALT_CONTROLS);
                    exit(p);
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
                    processKey(p, TARDISInfoMenu.K9);
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
                    exit(p);
                }
            }
            case DOOR -> new TISInfo(plugin).show(p, TARDISInfoMenu.DOOR);
            case COMMANDS -> {
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
                if (chat.equalsIgnoreCase("v")) {
                    processKey(p, TARDISInfoMenu.TARDISTRAVEL);
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
                if (chat.equalsIgnoreCase("av")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.LAVA);
                }
                if (chat.equalsIgnoreCase("A")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.ANTIGRAVITY);
                }
                if (chat.equalsIgnoreCase("q")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.AQUARIUM);
                }
                if (chat.equalsIgnoreCase("u")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.ARBORETUM);
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
                if (chat.equalsIgnoreCase("H")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.HARMONY);
                }
                if (chat.equalsIgnoreCase("K")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.KITCHEN);
                }
                exit(p);
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
                if (chat.equalsIgnoreCase("Ll")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.STALL);
                }
                if (chat.equalsIgnoreCase("T")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.TRENZALORE);
                }
                if (chat.equalsIgnoreCase("V")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.VAULT);
                }
                if (chat.equalsIgnoreCase("W")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.WOOD);
                }
                if (chat.equalsIgnoreCase("h")) {
                    new TISRoomInfo(plugin).show(p, TARDISInfoMenu.WORKSHOP);
                }
                exit(p);
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
                exit(p);
            }
            case FOOD_ACCESSORIES -> {
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.BOW_TIE);
                }
                if (chat.equalsIgnoreCase("C")) {
                    processKey(p, TARDISInfoMenu.CUSTARD);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TARDISInfoMenu.JAMMY_DODGER);
                }
                if (chat.equalsIgnoreCase("F")) {
                    processKey(p, TARDISInfoMenu.FISH_FINGER);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TARDISInfoMenu.THREE_D_GLASSES);
                }
                if (chat.equalsIgnoreCase("J")) {
                    processKey(p, TARDISInfoMenu.JELLY_BABY);
                }
                if (chat.equalsIgnoreCase("W")) {
                    processKey(p, TARDISInfoMenu.FOB_WATCH);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.COMMUNICATOR);
                }
            }
            case PLANETS -> {
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SKARO);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TARDISInfoMenu.SILURIA);
                }
                if (chat.equalsIgnoreCase("G")) {
                    processKey(p, TARDISInfoMenu.GALLIFREY);
                }
            }
            // THIRD level menus
            case KEY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.KEY_INFO);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.KEY_RECIPE);
                }
            }
            case SONIC_SCREWDRIVER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_SCREWDRIVER_INFO);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("T")) {
                    processKey(p, TARDISInfoMenu.SONIC_TYPES);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_SCREWDRIVER_RECIPE);
                    exit(p);
                }
            }
            case LOCATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.LOCATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.LOCATOR_RECIPE);
                }
                exit(p);
            }
            case STATTENHEIM_REMOTE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.STATTENHEIM_REMOTE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.STATTENHEIM_REMOTE_RECIPE);
                }
                exit(p);
            }
            case BIOME_READER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BIOME_READER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIOME_READER_RECIPE);
                }
                exit(p);
            }
            case REMOTE_KEY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.REMOTE_KEY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.REMOTE_KEY_RECIPE);
                }
                exit(p);
            }
            case ARTRON_FURNACE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_FURNACE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_FURNACE_RECIPE);
                }
                exit(p);
            }
            case SONIC_GENERATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_GENERATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_GENERATOR_RECIPE);
                }
                exit(p);
            }
            case THREE_D_GLASSES -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.THREE_D_GLASSES_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.THREE_D_GLASSES_RECIPE);
                }
                exit(p);
            }
            case BOW_TIE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BOW_TIE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BOW_TIE_RECIPE);
                }
                exit(p);
            }
            case CUSTARD -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CUSTARD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.CUSTARD_RECIPE);
                }
                exit(p);
            }
            case FISH_FINGER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FISH_FINGER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.FISH_FINGER_RECIPE);
                }
                exit(p);
            }
            case JELLY_BABY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.JELLY_BABY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.JELLY_BABY_RECIPE);
                }
                exit(p);
            }
            case JAMMY_DODGER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.JAMMY_DODGER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.JAMMY_DODGER_RECIPE);
                }
                exit(p);
            }
            case FOB_WATCH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.FOB_WATCH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.FOB_WATCH_RECIPE);
                }
                exit(p);
            }
            case COMMUNICATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.COMMUNICATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.COMMUNICATOR_RECIPE);
                }
                exit(p);
            }
            case BIOME_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BIOME_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIOME_STORAGE_DISK_RECIPE);
                }
                exit(p);
            }
            case AUTHORISED_CONTROL_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.AUTHORISED_CONTROL_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.AUTHORISED_CONTROL_DISK_RECIPE);
                }
                exit(p);
            }
            case BLANK_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BLANK_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BLANK_STORAGE_DISK_RECIPE);
                }
                exit(p);
            }
            case PLAYER_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PLAYER_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PLAYER_STORAGE_DISK_RECIPE);
                }
                exit(p);
            }
            case PRESET_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PRESET_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PRESET_STORAGE_DISK_RECIPE);
                }
                exit(p);
            }
            case SAVE_STORAGE_DISK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SAVE_STORAGE_DISK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SAVE_STORAGE_DISK_RECIPE);
                }
                exit(p);
            }
            case SERVER_ADMIN_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case BIO_SCANNER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case CHAMELEON_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CHAMELEON_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.CHAMELEON_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case DIAMOND_DISRUPTOR_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case EMERALD_ENVIRONMENT_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case ARTRON_CAPACITOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_RECIPE);
                }
                exit(p);
            }
            case ARTRON_CAPACITOR_STORAGE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_STORAGE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_CAPACITOR_STORAGE_RECIPE);
                }
                exit(p);
            }
            case ARTRON_STORAGE_CELL -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARTRON_STORAGE_CELL_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARTRON_STORAGE_CELL_RECIPE);
                }
                exit(p);
            }
            case PERCEPTION_FILTER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PERCEPTION_FILTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PERCEPTION_FILTER_RECIPE);
                }
                exit(p);
            }
            case ARS_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ARS_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ARS_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case INPUT_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.INPUT_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.INPUT_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case IGNITE_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.IGNITE_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.IGNITE_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case KNOCKBACK_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case INVISIBILITY_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.INVISIBILITY_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.INVISIBILITY_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case LOCATOR_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.LOCATOR_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.LOCATOR_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case MATERIALISATION_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MATERIALISATION_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.MATERIALISATION_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_OSCILLATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_OSCILLATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_OSCILLATOR_RECIPE);
                }
                exit(p);
            }
            case PERCEPTION_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PERCEPTION_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PERCEPTION_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case PAINTER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PAINTER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PAINTER_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case REDSTONE_ACTIVATOR_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case RANDOMISER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RANDOMISER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RANDOMISER_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case STATTENHEIM_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.STATTENHEIM_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.STATTENHEIM_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case TEMPORAL_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TEMPORAL_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.TEMPORAL_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case MEMORY_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MEMORY_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.MEMORY_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SCANNER_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SCANNER_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SCANNER_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case PICKUP_ARROWS_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case BRUSH_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.BRUSH_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BRUSH_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case CONVERSION_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.CONVERSION_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.CONVERSION_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case EXTERIOR_LAMP_LEVEL_SWITCH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.EXTERIOR_LAMP_LEVEL_SWITCH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.EXTERIOR_LAMP_LEVEL_SWITCH_RECIPE);
                }
                exit(p);
            }
            case INTERIOR_LIGHT_LEVEL_SWITCH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.INTERIOR_LIGHT_LEVEL_SWITCH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.INTERIOR_LIGHT_LEVEL_SWITCH_RECIPE);
                }
                exit(p);
            }
            case HANDLES -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.HANDLES_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.HANDLES_RECIPE);
                }
                exit(p);
            }
            case TARDIS_MONITOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.TARDIS_MONITOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.TARDIS_MONITOR_RECIPE);
                }
                exit(p);
            }
            case MONITOR_FRAME -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.MONITOR_FRAME_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.MONITOR_FRAME_RECIPE);
                }
                exit(p);
            }
            case SONIC_BLASTER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_BLASTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SONIC_BLASTER_RECIPE);
                }
                exit(p);
            }
            case VORTEX_MANIPULATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.VORTEX_MANIPULATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.VORTEX_MANIPULATOR_RECIPE);
                }
                exit(p);
            }
            case K9 -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.K9);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.K9_RECIPE);
                }
                exit(p);
            }
            case TARDIS -> {
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
            }
            case TARDISTRAVEL -> {
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
            }
            case TARDISPREFS -> {
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
                if (chat.equalsIgnoreCase("m")) {
                    showCommand(p, TARDISInfoMenu.TARDISPREFS_MESSAGE);
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
            }
            case TARDISBIND -> {
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
            }
            case TARDISAREA -> {
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
            }
            case TARDISROOM -> {
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
            }
            case SKARO -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SKARO_INFO);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SKARO_MONSTERS);
                    exit(p);
                }
                if (chat.equalsIgnoreCase("t")) {
                    processKey(p, TARDISInfoMenu.SKARO_ITEMS);
                }
            }
            case SILURIA -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SILURIA_INFO);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SILURIA_MONSTERS);
                }
                exit(p);
            }
            case GALLIFREY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.GALLIFREY_INFO);
                }
                if (chat.equalsIgnoreCase("M")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.GALLIFREY_MONSTERS);
                }
                exit(p);
            }
            // FOURTH level menus
            case SKARO_ITEMS -> {
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.RIFT_CIRCUIT);
                }
                if (chat.equalsIgnoreCase("M")) {
                    processKey(p, TARDISInfoMenu.RIFT_MANIPULATOR);
                }
                if (chat.equalsIgnoreCase("u")) {
                    processKey(p, TARDISInfoMenu.RUST_BUCKET);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.RUST_PLAGUE_SWORD);
                }
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.ACID_BUCKET);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TARDISInfoMenu.ACID_BATTERY);
                }
            }
            case SONIC_TYPES -> {
                if (chat.equalsIgnoreCase("S")) {
                    processKey(p, TARDISInfoMenu.SONIC_STANDARD);
                }
                if (chat.equalsIgnoreCase("R")) {
                    processKey(p, TARDISInfoMenu.SONIC_REDSTONE);
                }
                if (chat.equalsIgnoreCase("D")) {
                    processKey(p, TARDISInfoMenu.SONIC_DIAMOND);
                }
                if (chat.equalsIgnoreCase("m")) {
                    processKey(p, TARDISInfoMenu.SONIC_EMERALD);
                }
                if (chat.equalsIgnoreCase("A")) {
                    processKey(p, TARDISInfoMenu.SONIC_ADMIN);
                }
                if (chat.equalsIgnoreCase("i")) {
                    processKey(p, TARDISInfoMenu.SONIC_BIO);
                }
                if (chat.equalsIgnoreCase("P")) {
                    processKey(p, TARDISInfoMenu.SONIC_PAINTER);
                }
                if (chat.equalsIgnoreCase("B")) {
                    processKey(p, TARDISInfoMenu.SONIC_BRUSH);
                }
                if (chat.equalsIgnoreCase("K")) {
                    processKey(p, TARDISInfoMenu.SONIC_KNOCKBACK);
                }
                if (chat.equalsIgnoreCase("c")) {
                    processKey(p, TARDISInfoMenu.SONIC_PICKUP_ARROWS);
                }
                if (chat.equalsIgnoreCase("g")) {
                    processKey(p, TARDISInfoMenu.SONIC_IGNITE);
                }
                if (chat.equalsIgnoreCase("o")) {
                    processKey(p, TARDISInfoMenu.SONIC_CONVERSION);
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
                exit(p);
            }
            case SONIC_REDSTONE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_REDSTONE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.REDSTONE_ACTIVATOR_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_DIAMOND -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_DIAMOND_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.DIAMOND_DISRUPTOR_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_EMERALD -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_EMERALD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.EMERALD_ENVIRONMENT_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_ADMIN -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_ADMIN_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.SERVER_ADMIN_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_PAINTER -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_PAINTER_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PAINTER_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_BRUSH -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_BRUSH_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BRUSH_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_IGNITE -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_IGNITE_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.IGNITE_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_KNOCKBACK -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_KNOCKBACK_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.KNOCKBACK_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_PICKUP_ARROWS -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_PICKUP_ARROWS_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.PICKUP_ARROWS_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case SONIC_BIO -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.SONIC_BIO_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.BIO_SCANNER_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case RIFT_CIRCUIT -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RIFT_CIRCUIT_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RIFT_CIRCUIT_RECIPE);
                }
                exit(p);
            }
            case RIFT_MANIPULATOR -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RIFT_MANIPULATOR_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RIFT_MANIPULATOR_RECIPE);
                }
                exit(p);
            }
            case RUST_BUCKET -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RUST_BUCKET_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RUST_BUCKET_RECIPE);
                }
                exit(p);
            }
            case RUST_PLAGUE_SWORD -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.RUST_PLAGUE_SWORD_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.RUST_PLAGUE_SWORD_RECIPE);
                }
                exit(p);
            }
            case ACID_BUCKET -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ACID_BUCKET_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ACID_BUCKET_RECIPE);
                }
                exit(p);
            }
            case ACID_BATTERY -> {
                if (chat.equalsIgnoreCase("I")) {
                    new TISInfo(plugin).show(p, TARDISInfoMenu.ACID_BATTERY_INFO);
                }
                if (chat.equalsIgnoreCase("R")) {
                    new TISRecipe(plugin).show(p, TARDISInfoMenu.ACID_BATTERY_RECIPE);
                }
                exit(p);
            }
            default -> exit(p);
        }
    }

    /**
     * Displays the next menu level based on the parent menu item that was selected. Automatically pulls the key code
     * and highlights it.
     *
     * @param p    the player to show the menu to
     * @param item the parent menu item to get the children of
     */
    private void processKey(Player p, TARDISInfoMenu item) {
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
    private void showCommand(Player p, TARDISInfoMenu item) {
        String[] c = item.toString().toLowerCase(Locale.ENGLISH).split("_");
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
        exit(p);
    }

    /**
     * Exits the TARDIS Information System menu
     *
     * @param p the player to exit
     */
    private void exit(Player p) {
        plugin.getTrackerKeeper().getInfoMenu().remove(p.getUniqueId());
        plugin.getMessenger().messageWithColour(p, "---", "#FFAA00");
        plugin.getMessenger().send(p, TardisModule.TARDIS, "LOGGED_OUT_INFO");
    }
}
