/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.preferences;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicMenuInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;

/**
 * Command /tardisprefs [arguments].
 * <p>
 * Children begin instruction at the Time Lord Academy, at the age of 8, in a special ceremony. The Gallifreyans are
 * forced to look into the Untempered Schism, which shows the entirety of the Time Vortex and the power that the Time
 * Lords have.
 *
 * @author eccentric_nz
 */
public class TARDISPrefsCommands implements CommandExecutor {

    private static final ImmutableList<String> firstArgs = ImmutableList.of(
            "auto", "auto_powerup", "auto_rescue", "auto_siege",
            "beacon", "build",
            "close_gui",
            "difficulty", "dnd",
            "eps", "eps_message",
            "farm", "flight", "floor", "forcefield",
            "hads", "hads_type", "hum",
            "isomorphic",
            "key", "key_menu",
            "junk",
            "language", "lanterns", "lock_containers",
            "minecart",
            "quotes",
            "renderer",
            "sfx", "siege_floor", "siege_wall", "sign", "sonic", "submarine",
            "telepathy", "translate", "travelbar",
            "wall", "wool_lights"
    );
    private final TARDIS plugin;

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns all the root arguments for the command.
     *
     * @return a list of root arguments
     */
    public static ImmutableList<String> getRootArgs() {
        return firstArgs;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisprefs then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisprefs")) {
            if (args.length == 0) {
                new TARDISCommandHelper(plugin).getCommand("tardisprefs", sender);
                return true;
            }
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            String pref = args[0].toLowerCase(Locale.ENGLISH);
            if (firstArgs.contains(pref)) {
                if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
                    if (TARDISPermission.isNegated(player, "tardis.prefs." + pref)) {
                        TARDISMessage.send(player, "NO_PERMS");
                        return true;
                    }
                    if (pref.equals("sonic")) {
                        // open sonic prefs menu
                        ItemStack[] sonics = new TARDISSonicMenuInventory(plugin).getMenu();
                        Inventory sim = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Sonic Prefs Menu");
                        sim.setContents(sonics);
                        player.openInventory(sim);
                        return true;
                    }
                    if (pref.equals("key_menu")) {
                        // open sonic prefs menu
                        ItemStack[] keys = new TARDISKeyMenuInventory().getMenu();
                        Inventory sim = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "TARDIS Key Prefs Menu");
                        sim.setContents(keys);
                        player.openInventory(sim);
                        return true;
                    }
                    String uuid = player.getUniqueId().toString();
                    // get the players preferences
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                    HashMap<String, Object> set = new HashMap<>();
                    // if no prefs record found, make one
                    if (!rsp.resultSet()) {
                        set.put("uuid", uuid);
                        plugin.getQueryFactory().doInsert("player_prefs", set);
                    }
                    switch (pref) {
                        case "difficulty" -> {
                            return new TARDISSetDifficultyCommand(plugin).setDiff(player, args);
                        }
                        case "eps_message" -> {
                            return new TARDISEPSMessageCommand().setMessage(player, args);
                        }
                        case "flight" -> {
                            return new TARDISSetFlightCommand().setMode(player, args);
                        }
                        case "hads_type" -> {
                            return new TARDISHadsTypeCommand().setHadsPref(player, args);
                        }
                        case "hum" -> {
                            return new TARDISHumCommand().setHumPref(player, args);
                        }
                        case "isomorphic" -> {
                            return new TARDISIsomorphicCommand(plugin).toggleIsomorphicControls(player);
                        }
                        case "key" -> {
                            return new TARDISSetKeyCommand(plugin).setKeyPref(player, args);
                        }
                        case "language", "translate" -> {
                            return new TARDISSetLanguageCommand(plugin).setLanguagePref(player, args);
                        }
                        case "wall", "floor", "siege_wall", "siege_floor" -> {
                            return new TARDISFloorCommand().setFloorOrWallBlock(player, args);
                        }
                        default -> {
                            if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                                TARDISMessage.send(player, "PREF_ON_OFF", pref);
                                return false;
                            }
                            if (pref.equals("forcefield") && TARDISPermission.hasPermission(player, "tardis.forcefield")) {
                                // add tardis + location
                                if (args[1].equalsIgnoreCase("on")) {
                                    // check power
                                    ResultSetArtronLevel rsal = new ResultSetArtronLevel(plugin, uuid);
                                    if (rsal.resultset()) {
                                        if (rsal.getArtronLevel() <= plugin.getArtronConfig().getInt("standby")) {
                                            TARDISMessage.send(player, "POWER_LOW");
                                            return true;
                                        }
                                        if (TARDISForceField.addToTracker(player)) {
                                            TARDISMessage.send(player, "PREF_WAS_ON", "The TARDIS force field");
                                        }
                                    } else {
                                        TARDISMessage.send(player, "POWER_LEVEL");
                                        return true;
                                    }
                                } else {
                                    plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                                    TARDISMessage.send(player, "PREF_WAS_OFF", "The TARDIS force field");
                                }
                                return true;
                            }
                            return switch (pref) {
                                case "build" -> new TARDISBuildCommand(plugin).toggleCompanionBuilding(player, args);
                                case "junk" -> new TARDISJunkPreference(plugin).toggle(player, args[1]);
                                default -> new TARDISToggleOnOffCommand(plugin).toggle(player, args);
                            };
                        }
                    }
                } else {
                    TARDISMessage.send(player, "NO_PERMS");
                    return false;
                }
            } else {
                TARDISMessage.send(player, "PREF_NOT_VALID");
            }
        }
        return false;
    }
}
